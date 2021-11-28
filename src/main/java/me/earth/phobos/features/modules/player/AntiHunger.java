/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.earth.phobos.features.modules.player;

import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiHunger
extends Module {
    public Setting<Boolean> cancelSprint = this.register(new Setting<Boolean>("Sprint", true));
    public Setting<Boolean> ground = this.register(new Setting<Boolean>("Ground", true));

    public AntiHunger() {
        super("AntiHunger", "Prevents you from getting hungry as fast.", Module.Category.PLAYER, true, false, false);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        CPacketPlayer packet;
        if (this.ground.getValue().booleanValue() && event.getPacket() instanceof CPacketPlayer) {
            packet = (CPacketPlayer)event.getPacket();
            boolean bl = packet.field_149474_g = AntiHunger.mc.field_71439_g.field_70143_R >= 0.0f || AntiHunger.mc.field_71442_b.field_78778_j;
        }
        if (this.cancelSprint.getValue().booleanValue() && event.getPacket() instanceof CPacketEntityAction && ((packet = (CPacketEntityAction)event.getPacket()).func_180764_b() == CPacketEntityAction.Action.START_SPRINTING || packet.func_180764_b() == CPacketEntityAction.Action.STOP_SPRINTING)) {
            event.setCanceled(true);
        }
    }
}

