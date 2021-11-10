



package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.*;
import java.util.concurrent.*;
import me.earth.phobos.event.events.*;
import net.minecraft.network.play.server.*;
import net.minecraft.client.network.*;
import java.util.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.earth.phobos.util.*;
import me.earth.phobos.features.command.*;

public class AntiVanish extends Module
{
    private final Queue<UUID> toLookUp;
    
    public AntiVanish() {
        super("AntiVanish", "Notifies you when players vanish.", Category.MISC, true, false, false);
        this.toLookUp = new ConcurrentLinkedQueue<UUID>();
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        final SPacketPlayerListItem sPacketPlayerListItem;
        if (event.getPacket() instanceof SPacketPlayerListItem && (sPacketPlayerListItem = (SPacketPlayerListItem)event.getPacket()).getAction() == SPacketPlayerListItem.Action.UPDATE_LATENCY) {
            for (final SPacketPlayerListItem.AddPlayerData addPlayerData : sPacketPlayerListItem.getEntries()) {
                try {
                    Objects.requireNonNull(AntiVanish.mc.getConnection()).getPlayerInfo(addPlayerData.getProfile().getId());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public void onUpdate() {
        final UUID lookUp;
        if (PlayerUtil.timer.passedS(5.0) && (lookUp = this.toLookUp.poll()) != null) {
            try {
                final String name = PlayerUtil.getNameFromUUID(lookUp);
                if (name != null) {
                    Command.sendMessage("§c" + name + " has gone into vanish.");
                }
            }
            catch (Exception ex) {}
            PlayerUtil.timer.reset();
        }
    }
    
    @Override
    public void onLogout() {
        this.toLookUp.clear();
    }
}
