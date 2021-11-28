/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.INetHandlerPlayServer
 *  net.minecraft.network.play.client.CPacketConfirmTeleport
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.earth.phobos.features.modules.movement;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import me.earth.phobos.event.events.MoveEvent;
import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.event.events.PushEvent;
import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PacketFlyNew
extends Module {
    private final Setting<types> type = this.register(new Setting<types>("Type", types.DOWN));
    private final Setting<modes> mode = this.register(new Setting<modes>("Mode", modes.FAST));
    private final ArrayList<Packet<INetHandlerPlayServer>> packets;
    public Setting<Integer> factorAmount = this.register(new Setting<Float>("Factor", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(10.0f)));
    public Setting<Boolean> limit = this.register(new Setting<Boolean>("Limit", true));
    private int teleportID = -1;
    private int frequency = 1;
    private boolean frequencyUp = true;
    private float rotationYaw = -1.0f;
    private float rotationPitch = -1.0f;

    public PacketFlyNew() {
        super("PacketFlyNew", "Uses packets to allow you to fly and move.", Module.Category.MOVEMENT, true, false, false);
        this.packets = new ArrayList();
    }

    public void sendOffsets(double x, double y, double z) {
        CPacketPlayer.PositionRotation spoofPos = null;
        switch (this.type.getValue()) {
            case UP: {
                spoofPos = new CPacketPlayer.PositionRotation(x, y + 1337.0, z, this.rotationYaw, this.rotationPitch, PacketFlyNew.mc.field_71439_g.field_70122_E);
                break;
            }
            case DOWN: {
                spoofPos = new CPacketPlayer.PositionRotation(x, y - 1337.0, z, this.rotationYaw, this.rotationPitch, PacketFlyNew.mc.field_71439_g.field_70122_E);
                break;
            }
            case BOUNDED: {
                spoofPos = new CPacketPlayer.PositionRotation(x, 256.0, z, this.rotationYaw, this.rotationPitch, PacketFlyNew.mc.field_71439_g.field_70122_E);
                break;
            }
            case CONCEAL: {
                spoofPos = new CPacketPlayer.PositionRotation(x + (double)new Random().nextInt(2000000) - 1000000.0, y + (double)new Random().nextInt(2000000) - 1000000.0, z + (double)new Random().nextInt(2000000) - 1000000.0, this.rotationYaw, this.rotationPitch, PacketFlyNew.mc.field_71439_g.field_70122_E);
                break;
            }
            case LIMITJITTER: {
                spoofPos = new CPacketPlayer.PositionRotation(x, y + (double)new Random().nextInt(512) - 256.0, z, this.rotationYaw, this.rotationPitch, PacketFlyNew.mc.field_71439_g.field_70122_E);
                break;
            }
            case PRESERVE: {
                spoofPos = new CPacketPlayer.PositionRotation(x + (double)new Random().nextInt(2000000) - 1000000.0, y, z + (double)new Random().nextInt(2000000) - 1000000.0, this.rotationYaw, this.rotationPitch, PacketFlyNew.mc.field_71439_g.field_70122_E);
            }
        }
        if (spoofPos == null) {
            return;
        }
        this.packets.add((Packet<INetHandlerPlayServer>)spoofPos);
        Objects.requireNonNull(mc.func_147114_u()).func_147297_a(spoofPos);
    }

    @Override
    public void onEnable() {
        this.packets.clear();
        this.rotationYaw = PacketFlyNew.mc.field_71439_g.field_70177_z;
        this.rotationPitch = PacketFlyNew.mc.field_71439_g.field_70125_A;
        this.sendOffsets(PacketFlyNew.mc.field_71439_g.field_70165_t, PacketFlyNew.mc.field_71439_g.field_70163_u, PacketFlyNew.mc.field_71439_g.field_70161_v);
    }

    private boolean isInsideBlock() {
        return !PacketFlyNew.mc.field_71441_e.func_184144_a((Entity)PacketFlyNew.mc.field_71439_g, PacketFlyNew.mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625, -0.0625, -0.0625)).isEmpty();
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        PacketFlyNew.mc.field_71439_g.field_70159_w = 0.0;
        PacketFlyNew.mc.field_71439_g.field_70181_x = 0.0;
        PacketFlyNew.mc.field_71439_g.field_70179_y = 0.0;
        if (event.getStage() == 1) {
            event.setCanceled(true);
            return;
        }
        double motionY = 0.0;
        if (PacketFlyNew.mc.field_71439_g.field_71158_b.field_78901_c) {
            motionY = this.isInsideBlock() ? 0.016 : 0.032;
            if (!PacketFlyNew.mc.field_71439_g.field_70122_E && !this.isInsideBlock() && PacketFlyNew.mc.field_71439_g.field_70173_aa % 15 == 0) {
                motionY = -0.032;
            }
        } else if (PacketFlyNew.mc.field_71439_g.field_71158_b.field_78899_d) {
            motionY = this.isInsideBlock() ? -0.032 : -0.062;
        } else if (!PacketFlyNew.mc.field_71439_g.field_70122_E && !this.isInsideBlock() && PacketFlyNew.mc.field_71439_g.field_70173_aa % 15 == 0) {
            motionY = -0.032;
        }
        int currentFactor = 1;
        int clock = 0;
        while (currentFactor <= (this.mode.getValue().equals((Object)modes.FACTOR) ? this.factorAmount.getValue() : 1)) {
            if (clock++ <= (this.limit.getValue() != false ? 5 : 1)) continue;
            double vSpeed = this.isInsideBlock() ? (PacketFlyNew.mc.field_71439_g.field_71158_b.field_78901_c || PacketFlyNew.mc.field_71439_g.field_71158_b.field_78899_d ? 0.016 : 0.032) : (PacketFlyNew.mc.field_71439_g.field_71158_b.field_78901_c ? 0.0032 : (PacketFlyNew.mc.field_71439_g.field_71158_b.field_78899_d ? 0.032 : 0.062));
            double[] strafing = MathUtil.directionSpeed(vSpeed);
            double motionX = strafing[0] * (double)currentFactor;
            double motionZ = strafing[1] * (double)currentFactor;
            event.setX(motionX);
            event.setY(motionY);
            event.setZ(motionZ);
            this.doMovement(motionX, motionY, motionZ);
            ++currentFactor;
            if (motionX == 0.0 && motionY == 0.0 && motionZ == 0.0) {
                return;
            }
            if (this.frequencyUp) {
                ++this.frequency;
                if (this.frequency != 3) continue;
                this.frequencyUp = false;
                continue;
            }
            --this.frequency;
            if (this.frequency != 1) continue;
            this.frequencyUp = true;
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 1) {
            return;
        }
        PacketFlyNew.mc.field_71439_g.field_70159_w = 0.0;
        PacketFlyNew.mc.field_71439_g.field_70181_x = 0.0;
        PacketFlyNew.mc.field_71439_g.field_70179_y = 0.0;
        PacketFlyNew.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
    }

    @SubscribeEvent
    public void onPushOutOfBlocks(PushEvent event) {
        if (event.getStage() == 1) {
            event.setCanceled(true);
        }
    }

    private void doMovement(double x, double y, double z) {
        CPacketPlayer.PositionRotation newPos = new CPacketPlayer.PositionRotation(PacketFlyNew.mc.field_71439_g.field_70165_t + x, PacketFlyNew.mc.field_71439_g.field_70163_u + y, PacketFlyNew.mc.field_71439_g.field_70161_v + z, this.rotationYaw, this.rotationPitch, PacketFlyNew.mc.field_71439_g.field_70122_E);
        this.packets.add((Packet<INetHandlerPlayServer>)newPos);
        Objects.requireNonNull(mc.func_147114_u()).func_147297_a((Packet)newPos);
        for (int i = 0; i < this.frequency; ++i) {
            this.sendOffsets(PacketFlyNew.mc.field_71439_g.field_70165_t, PacketFlyNew.mc.field_71439_g.field_70163_u, PacketFlyNew.mc.field_71439_g.field_70161_v);
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer.Position || event.getPacket() instanceof CPacketPlayer.Rotation) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packetPlayer = (CPacketPlayer)event.getPacket();
            if (this.packets.contains((Object)packetPlayer)) {
                this.packets.remove((Object)packetPlayer);
            } else {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (PacketFlyNew.mc.field_71439_g == null || PacketFlyNew.mc.field_71441_e == null) {
            return;
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook flag = (SPacketPlayerPosLook)event.getPacket();
            this.teleportID = flag.func_186965_f();
            PacketFlyNew.mc.field_71439_g.func_70107_b(flag.func_148932_c(), flag.func_148928_d(), flag.func_148933_e());
            PacketFlyNew.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportID++));
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityVelocity) {
            SPacketEntityVelocity packet = (SPacketEntityVelocity)event.getPacket();
            if (packet.field_149417_a == PacketFlyNew.mc.field_71439_g.field_145783_c) {
                packet.field_149415_b = 0;
                packet.field_149416_c = 0;
                packet.field_149414_d = 0;
            }
        }
    }

    private static enum types {
        UP,
        DOWN,
        PRESERVE,
        LIMITJITTER,
        BOUNDED,
        CONCEAL;

    }

    private static enum modes {
        FAST,
        FACTOR;

    }
}

