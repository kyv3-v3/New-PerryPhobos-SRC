/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketConfirmTeleport
 *  net.minecraft.network.play.client.CPacketInput
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketVehicleMove
 *  net.minecraft.network.play.server.SPacketMoveVehicle
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.earth.phobos.features.modules.movement;

import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BoatFly
extends Module {
    public static BoatFly INSTANCE;
    public Setting<Double> speed = this.register(new Setting<Double>("Speed", 3.0, 1.0, 10.0));
    public Setting<Double> verticalSpeed = this.register(new Setting<Double>("VerticalSpeed", 3.0, 1.0, 10.0));
    public Setting<Boolean> noKick = this.register(new Setting<Boolean>("No-Kick", true));
    public Setting<Boolean> packet = this.register(new Setting<Boolean>("Packet", true));
    public Setting<Integer> packets = this.register(new Setting<Object>("Packets", Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(5), v -> this.packet.getValue()));
    public Setting<Integer> interact = this.register(new Setting<Integer>("Delay", 2, 1, 20));
    private int teleportID;

    public BoatFly() {
        super("BoatFly", "Boatfly for 2b.", Module.Category.MOVEMENT, true, false, false);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (BoatFly.mc.field_71439_g == null) {
            return;
        }
        if (BoatFly.mc.field_71441_e == null || BoatFly.mc.field_71439_g.func_184187_bx() == null) {
            return;
        }
        BoatFly.mc.field_71439_g.func_184187_bx();
        BoatFly.mc.field_71439_g.func_184187_bx().func_189654_d(true);
        BoatFly.mc.field_71439_g.func_184187_bx().field_70181_x = 0.0;
        if (BoatFly.mc.field_71474_y.field_74314_A.func_151470_d()) {
            BoatFly.mc.field_71439_g.func_184187_bx().field_70122_E = false;
            BoatFly.mc.field_71439_g.func_184187_bx().field_70181_x = this.verticalSpeed.getValue() / 10.0;
        }
        if (BoatFly.mc.field_71474_y.field_151444_V.func_151470_d()) {
            BoatFly.mc.field_71439_g.func_184187_bx().field_70122_E = false;
            BoatFly.mc.field_71439_g.func_184187_bx().field_70181_x = -(this.verticalSpeed.getValue() / 10.0);
        }
        double[] normalDir = this.directionSpeed(this.speed.getValue() / 2.0);
        if (BoatFly.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f || BoatFly.mc.field_71439_g.field_71158_b.field_192832_b != 0.0f) {
            BoatFly.mc.field_71439_g.func_184187_bx().field_70159_w = normalDir[0];
            BoatFly.mc.field_71439_g.func_184187_bx().field_70179_y = normalDir[1];
        } else {
            BoatFly.mc.field_71439_g.func_184187_bx().field_70159_w = 0.0;
            BoatFly.mc.field_71439_g.func_184187_bx().field_70179_y = 0.0;
        }
        if (this.noKick.getValue().booleanValue()) {
            if (BoatFly.mc.field_71474_y.field_74314_A.func_151470_d()) {
                if (BoatFly.mc.field_71439_g.field_70173_aa % 8 < 2) {
                    BoatFly.mc.field_71439_g.func_184187_bx().field_70181_x = -0.04f;
                }
            } else if (BoatFly.mc.field_71439_g.field_70173_aa % 8 < 4) {
                BoatFly.mc.field_71439_g.func_184187_bx().field_70181_x = -0.08f;
            }
        }
        this.handlePackets(BoatFly.mc.field_71439_g.func_184187_bx().field_70159_w, BoatFly.mc.field_71439_g.func_184187_bx().field_70181_x, BoatFly.mc.field_71439_g.func_184187_bx().field_70179_y);
    }

    public void handlePackets(double x, double y, double z) {
        if (this.packet.getValue().booleanValue()) {
            Vec3d vec = new Vec3d(x, y, z);
            if (BoatFly.mc.field_71439_g.func_184187_bx() == null) {
                return;
            }
            Vec3d position = BoatFly.mc.field_71439_g.func_184187_bx().func_174791_d().func_178787_e(vec);
            BoatFly.mc.field_71439_g.func_184187_bx().func_70107_b(position.field_72450_a, position.field_72448_b, position.field_72449_c);
            BoatFly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketVehicleMove(BoatFly.mc.field_71439_g.func_184187_bx()));
            for (int i = 0; i < this.packets.getValue(); ++i) {
                BoatFly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportID++));
            }
        }
    }

    @SubscribeEvent
    public void onSendPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketVehicleMove && BoatFly.mc.field_71439_g.func_184218_aH() && BoatFly.mc.field_71439_g.field_70173_aa % this.interact.getValue() == 0) {
            BoatFly.mc.field_71442_b.func_187097_a((EntityPlayer)BoatFly.mc.field_71439_g, BoatFly.mc.field_71439_g.field_184239_as, EnumHand.OFF_HAND);
        }
        if ((event.getPacket() instanceof CPacketPlayer.Rotation || event.getPacket() instanceof CPacketInput) && BoatFly.mc.field_71439_g.func_184218_aH()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onReceivePacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketMoveVehicle && BoatFly.mc.field_71439_g.func_184218_aH()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            this.teleportID = ((SPacketPlayerPosLook)event.getPacket()).field_186966_g;
        }
    }

    private double[] directionSpeed(double speed) {
        float forward = BoatFly.mc.field_71439_g.field_71158_b.field_192832_b;
        float side = BoatFly.mc.field_71439_g.field_71158_b.field_78902_a;
        float yaw = BoatFly.mc.field_71439_g.field_70126_B + (BoatFly.mc.field_71439_g.field_70177_z - BoatFly.mc.field_71439_g.field_70126_B) * mc.func_184121_ak();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        return new double[]{posX, posZ};
    }
}

