



package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.*;
import net.minecraft.util.text.*;
import net.minecraft.network.*;
import me.earth.phobos.event.events.*;
import net.minecraft.network.play.server.*;
import net.minecraft.init.*;
import me.earth.phobos.util.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class AutoLog extends Module
{
    private static AutoLog INSTANCE;
    private final Setting<Float> health;
    private final Setting<Boolean> bed;
    private final Setting<Float> range;
    private final Setting<Boolean> logout;
    
    public AutoLog() {
        super("AutoLog",  "Logs when in danger.",  Category.MISC,  false,  false,  false);
        this.health = (Setting<Float>)this.register(new Setting("Health", 16.0f, 0.1f, 36.0f));
        this.bed = (Setting<Boolean>)this.register(new Setting("Beds", true));
        this.range = (Setting<Float>)this.register(new Setting("BedRange", 6.0f, 0.1f, 36.0f,  v -> this.bed.getValue()));
        this.logout = (Setting<Boolean>)this.register(new Setting("LogoutOff", true));
        this.setInstance();
    }
    
    public static AutoLog getInstance() {
        if (AutoLog.INSTANCE == null) {
            AutoLog.INSTANCE = new AutoLog();
        }
        return AutoLog.INSTANCE;
    }
    
    private void setInstance() {
        AutoLog.INSTANCE = this;
    }
    
    @Override
    public void onTick() {
        if (!nullCheck() && AutoLog.mc.player.getHealth() <= this.health.getValue()) {
            Phobos.moduleManager.disableModule("AutoReconnect");
            AutoLog.mc.player.connection.sendPacket((Packet)new SPacketDisconnect((ITextComponent)new TextComponentString("AutoLogged")));
            if (this.logout.getValue()) {
                this.disable();
            }
        }
    }
    
    @SubscribeEvent
    public void onReceivePacket(final PacketEvent.Receive event) {
        final SPacketBlockChange packet;
        if (event.getPacket() instanceof SPacketBlockChange && this.bed.getValue() && (packet = (SPacketBlockChange)event.getPacket()).getBlockState().getBlock() == Blocks.BED && AutoLog.mc.player.getDistanceSqToCenter(packet.getBlockPosition()) <= MathUtil.square(this.range.getValue())) {
            Phobos.moduleManager.disableModule("AutoReconnect");
            AutoLog.mc.player.connection.sendPacket((Packet)new SPacketDisconnect((ITextComponent)new TextComponentString("AutoLogged")));
            if (this.logout.getValue()) {
                this.disable();
            }
        }
    }
    
    static {
        AutoLog.INSTANCE = new AutoLog();
    }
}
