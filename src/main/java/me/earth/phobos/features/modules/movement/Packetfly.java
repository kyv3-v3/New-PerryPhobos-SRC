/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.util.internal.ConcurrentSet
 *  net.minecraft.client.gui.GuiDownloadTerrain
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketConfirmTeleport
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.earth.phobos.features.modules.movement;

import io.netty.util.internal.ConcurrentSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import me.earth.phobos.event.events.MoveEvent;
import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.event.events.PushEvent;
import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.TimerUtil;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Packetfly
extends Module {
    private static Packetfly instance;
    private final Set<CPacketPlayer> packets = new ConcurrentSet();
    private final Map<Integer, IDtime> teleportmap = new ConcurrentHashMap<Integer, IDtime>();
    public Setting<Boolean> flight = this.register(new Setting<Boolean>("Flight", true));
    public Setting<Integer> flightMode = this.register(new Setting<Integer>("FMode", 0, 0, 1));
    public Setting<Boolean> doAntiFactor = this.register(new Setting<Boolean>("Factorize", true));
    public Setting<Double> antiFactor = this.register(new Setting<Double>("AntiFactor", 2.5, 0.1, 3.0));
    public Setting<Double> extraFactor = this.register(new Setting<Double>("ExtraFactor", 1.0, 0.1, 3.0));
    public Setting<Boolean> strafeFactor = this.register(new Setting<Boolean>("StrafeFactor", true));
    public Setting<Integer> loops = this.register(new Setting<Integer>("Loops", 1, 1, 10));
    public Setting<Boolean> clearTeleMap = this.register(new Setting<Boolean>("ClearMap", true));
    public Setting<Integer> mapTime = this.register(new Setting<Integer>("ClearTime", 30, 1, 500));
    public Setting<Boolean> clearIDs = this.register(new Setting<Boolean>("ClearIDs", true));
    public Setting<Boolean> setYaw = this.register(new Setting<Boolean>("SetYaw", true));
    public Setting<Boolean> setID = this.register(new Setting<Boolean>("SetID", true));
    public Setting<Boolean> setMove = this.register(new Setting<Boolean>("SetMove", false));
    public Setting<Boolean> nocliperino = this.register(new Setting<Boolean>("NoClip", false));
    public Setting<Boolean> sendTeleport = this.register(new Setting<Boolean>("Teleport", true));
    public Setting<Boolean> resetID = this.register(new Setting<Boolean>("ResetID", true));
    public Setting<Boolean> setPos = this.register(new Setting<Boolean>("SetPos", false));
    public Setting<Boolean> invalidPacket = this.register(new Setting<Boolean>("InvalidPacket", true));
    private int flightCounter;
    private int teleportID;

    public Packetfly() {
        super("Packetfly", "Uses packets to fly!", Module.Category.MOVEMENT, true, false, false);
        instance = this;
    }

    public static Packetfly getInstance() {
        if (instance == null) {
            instance = new Packetfly();
        }
        return instance;
    }

    @Override
    public void onToggle() {
    }

    @Override
    public void onTick() {
        this.teleportmap.entrySet().removeIf(idTime -> this.clearTeleMap.getValue() != false && ((IDtime)idTime.getValue()).getTimer().passedS(this.mapTime.getValue().intValue()));
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        double speed;
        if (event.getStage() == 1) {
            return;
        }
        Packetfly.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
        boolean checkCollisionBoxes = this.checkHitBoxes();
        double d = Packetfly.mc.field_71439_g.field_71158_b.field_78901_c && (checkCollisionBoxes || !EntityUtil.isMoving()) ? (this.flight.getValue().booleanValue() && !checkCollisionBoxes ? (this.flightMode.getValue() == 0 ? (this.resetCounter(10) ? -0.032 : 0.062) : (this.resetCounter(20) ? -0.032 : 0.062)) : 0.062) : (Packetfly.mc.field_71439_g.field_71158_b.field_78899_d ? -0.062 : (!checkCollisionBoxes ? (this.resetCounter(4) ? (this.flight.getValue().booleanValue() ? -0.04 : 0.0) : 0.0) : (speed = 0.0)));
        if (this.doAntiFactor.getValue().booleanValue() && checkCollisionBoxes && EntityUtil.isMoving() && speed != 0.0) {
            speed /= this.antiFactor.getValue().doubleValue();
        }
        double[] strafing = this.getMotion(this.strafeFactor.getValue() != false && checkCollisionBoxes ? 0.031 : 0.26);
        for (int i = 1; i < this.loops.getValue() + 1; ++i) {
            Packetfly.mc.field_71439_g.field_70159_w = strafing[0] * (double)i * this.extraFactor.getValue();
            Packetfly.mc.field_71439_g.field_70181_x = speed * (double)i;
            Packetfly.mc.field_71439_g.field_70179_y = strafing[1] * (double)i * this.extraFactor.getValue();
            this.sendPackets(Packetfly.mc.field_71439_g.field_70159_w, Packetfly.mc.field_71439_g.field_70181_x, Packetfly.mc.field_71439_g.field_70179_y, this.sendTeleport.getValue());
        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (this.setMove.getValue().booleanValue() && this.flightCounter != 0) {
            event.setX(Packetfly.mc.field_71439_g.field_70159_w);
            event.setY(Packetfly.mc.field_71439_g.field_70181_x);
            event.setZ(Packetfly.mc.field_71439_g.field_70179_y);
            if (this.nocliperino.getValue().booleanValue() && this.checkHitBoxes()) {
                Packetfly.mc.field_71439_g.field_70145_X = true;
            }
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer && !this.packets.remove(event.getPacket())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPushOutOfBlocks(PushEvent event) {
        if (event.getStage() == 1) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook && !Packetfly.fullNullCheck()) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
            if (Packetfly.mc.field_71439_g.func_70089_S() && Packetfly.mc.field_71441_e.func_175668_a(new BlockPos(Packetfly.mc.field_71439_g.field_70165_t, Packetfly.mc.field_71439_g.field_70163_u, Packetfly.mc.field_71439_g.field_70161_v), false) && !(Packetfly.mc.field_71462_r instanceof GuiDownloadTerrain) && this.clearIDs.getValue().booleanValue()) {
                this.teleportmap.remove(packet.func_186965_f());
            }
            if (this.setYaw.getValue().booleanValue()) {
                packet.field_148936_d = Packetfly.mc.field_71439_g.field_70177_z;
                packet.field_148937_e = Packetfly.mc.field_71439_g.field_70125_A;
            }
            if (this.setID.getValue().booleanValue()) {
                this.teleportID = packet.func_186965_f();
            }
        }
    }

    private boolean checkHitBoxes() {
        return !Packetfly.mc.field_71441_e.func_184144_a((Entity)Packetfly.mc.field_71439_g, Packetfly.mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625, -0.0625, -0.0625)).isEmpty();
    }

    private boolean resetCounter(int counter) {
        if (++this.flightCounter >= counter) {
            this.flightCounter = 0;
            return true;
        }
        return false;
    }

    private double[] getMotion(double speed) {
        float moveForward = Packetfly.mc.field_71439_g.field_71158_b.field_192832_b;
        float moveStrafe = Packetfly.mc.field_71439_g.field_71158_b.field_78902_a;
        float rotationYaw = Packetfly.mc.field_71439_g.field_70126_B + (Packetfly.mc.field_71439_g.field_70177_z - Packetfly.mc.field_71439_g.field_70126_B) * mc.func_184121_ak();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? -45 : 45);
            } else if (moveStrafe < 0.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            } else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        double posX = (double)moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + (double)moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
        double posZ = (double)moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - (double)moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));
        return new double[]{posX, posZ};
    }

    private void sendPackets(double x, double y, double z, boolean teleport) {
        Vec3d vec = new Vec3d(x, y, z);
        Vec3d position = Packetfly.mc.field_71439_g.func_174791_d().func_178787_e(vec);
        Vec3d outOfBoundsVec = this.outOfBoundsVec(vec, position);
        this.packetSender((CPacketPlayer)new CPacketPlayer.Position(position.field_72450_a, position.field_72448_b, position.field_72449_c, Packetfly.mc.field_71439_g.field_70122_E));
        if (this.invalidPacket.getValue().booleanValue()) {
            this.packetSender((CPacketPlayer)new CPacketPlayer.Position(outOfBoundsVec.field_72450_a, outOfBoundsVec.field_72448_b, outOfBoundsVec.field_72449_c, Packetfly.mc.field_71439_g.field_70122_E));
        }
        if (this.setPos.getValue().booleanValue()) {
            Packetfly.mc.field_71439_g.func_70107_b(position.field_72450_a, position.field_72448_b, position.field_72449_c);
        }
        this.teleportPacket(position, teleport);
    }

    private void teleportPacket(Vec3d pos, boolean shouldTeleport) {
        if (shouldTeleport) {
            Packetfly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(++this.teleportID));
            this.teleportmap.put(this.teleportID, new IDtime(pos, new TimerUtil()));
        }
    }

    private Vec3d outOfBoundsVec(Vec3d offset, Vec3d position) {
        return position.func_72441_c(0.0, 1337.0, 0.0);
    }

    private void packetSender(CPacketPlayer packet) {
        this.packets.add(packet);
        Packetfly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)packet);
    }

    private void clean() {
        this.teleportmap.clear();
        this.flightCounter = 0;
        if (this.resetID.getValue().booleanValue()) {
            this.teleportID = 0;
        }
        this.packets.clear();
    }

    public static class IDtime {
        private final Vec3d pos;
        private final TimerUtil timer;

        public IDtime(Vec3d pos, TimerUtil timer) {
            this.pos = pos;
            this.timer = timer;
            this.timer.reset();
        }

        public Vec3d getPos() {
            return this.pos;
        }

        public TimerUtil getTimer() {
            return this.timer;
        }
    }
}

