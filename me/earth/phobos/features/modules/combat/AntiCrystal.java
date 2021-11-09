//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.modules.combat;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import java.util.*;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.earth.phobos.event.events.*;
import me.earth.phobos.util.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.math.*;

public class AntiCrystal extends Module
{
    private final List<BlockPos> targets;
    private final TimerUtil timer;
    private final TimerUtil breakTimer;
    private final TimerUtil checkTimer;
    public Setting<Float> range;
    public Setting<Float> wallsRange;
    public Setting<Float> minDmg;
    public Setting<Float> selfDmg;
    public Setting<Integer> placeDelay;
    public Setting<Integer> breakDelay;
    public Setting<Integer> checkDelay;
    public Setting<Integer> wasteAmount;
    public Setting<Switch> switcher;
    public Setting<Updates> mode;
    public Setting<Boolean> rotate;
    public Setting<Boolean> packet;
    public Setting<Boolean> instant;
    public Setting<Boolean> resetBreakTimer;
    public Setting<Integer> rotations;
    private float yaw;
    private float pitch;
    private boolean rotating;
    private int rotationPacketsSpoofed;
    private Entity breakTarget;
    
    public AntiCrystal() {
        super("AntiCrystal", "Depending on you and ur opponents ping if urs is lower u can out place their ca to have essentially a godmode.", Category.COMBAT, true, false, false);
        this.targets = new ArrayList<BlockPos>();
        this.timer = new TimerUtil();
        this.breakTimer = new TimerUtil();
        this.checkTimer = new TimerUtil();
        this.range = (Setting<Float>)this.register(new Setting("Range", (T)6.0f, (T)0.0f, (T)10.0f));
        this.wallsRange = (Setting<Float>)this.register(new Setting("WallsRange", (T)3.5f, (T)0.0f, (T)10.0f));
        this.minDmg = (Setting<Float>)this.register(new Setting("MinDmg", (T)6.0f, (T)0.0f, (T)40.0f));
        this.selfDmg = (Setting<Float>)this.register(new Setting("SelfDmg", (T)2.0f, (T)0.0f, (T)10.0f));
        this.placeDelay = (Setting<Integer>)this.register(new Setting("PlaceDelay", (T)0, (T)0, (T)500));
        this.breakDelay = (Setting<Integer>)this.register(new Setting("BreakDelay", (T)0, (T)0, (T)500));
        this.checkDelay = (Setting<Integer>)this.register(new Setting("CheckDelay", (T)0, (T)0, (T)500));
        this.wasteAmount = (Setting<Integer>)this.register(new Setting("WasteAmount", (T)1, (T)1, (T)5));
        this.switcher = (Setting<Switch>)this.register(new Setting("Switch", (T)Switch.NONE));
        this.mode = (Setting<Updates>)this.register(new Setting("Updates", (T)Updates.TICK));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (T)true));
        this.packet = (Setting<Boolean>)this.register(new Setting("Packet", (T)true));
        this.instant = (Setting<Boolean>)this.register(new Setting("Predict", (T)false));
        this.resetBreakTimer = (Setting<Boolean>)this.register(new Setting("ResetBreak", (T)true));
        this.rotations = (Setting<Integer>)this.register(new Setting("Spoofs", (T)1, (T)1, (T)20));
    }
    
    @Override
    public void onToggle() {
        this.rotating = false;
    }
    
    private Entity getDeadlyCrystal() {
        Entity bestcrystal = null;
        float highestDamage = 0.0f;
        for (final Entity crystal : AntiCrystal.mc.world.loadedEntityList) {
            if (crystal instanceof EntityEnderCrystal && AntiCrystal.mc.player.getDistanceSq(crystal) <= 169.0) {
                final float damage;
                if ((damage = DamageUtil.calculateDamage(crystal, (Entity)AntiCrystal.mc.player)) < this.minDmg.getValue()) {
                    continue;
                }
                if (bestcrystal == null) {
                    bestcrystal = crystal;
                    highestDamage = damage;
                }
                else {
                    if (damage <= highestDamage) {
                        continue;
                    }
                    bestcrystal = crystal;
                    highestDamage = damage;
                }
            }
        }
        return bestcrystal;
    }
    
    private int getSafetyCrystals(final Entity deadlyCrystal) {
        int count = 0;
        for (final Entity entity : AntiCrystal.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal) && DamageUtil.calculateDamage(entity, (Entity)AntiCrystal.mc.player) <= 2.0f) {
                if (deadlyCrystal.getDistanceSq(entity) > 144.0) {
                    continue;
                }
                ++count;
            }
        }
        return count;
    }
    
    private BlockPos getPlaceTarget(final Entity deadlyCrystal) {
        BlockPos closestPos = null;
        float smallestDamage = 10.0f;
        for (final BlockPos pos : BlockUtil.possiblePlacePositions(this.range.getValue())) {
            final float damage = DamageUtil.calculateDamage(pos, (Entity)AntiCrystal.mc.player);
            if (damage <= 2.0f && deadlyCrystal.getDistanceSq(pos) <= 144.0) {
                if (AntiCrystal.mc.player.getDistanceSq(pos) >= MathUtil.square(this.wallsRange.getValue()) && BlockUtil.rayTracePlaceCheck(pos, true, 1.0f)) {
                    continue;
                }
                if (closestPos == null) {
                    smallestDamage = damage;
                    closestPos = pos;
                }
                else {
                    if (damage >= smallestDamage) {
                        if (damage != smallestDamage) {
                            continue;
                        }
                        if (AntiCrystal.mc.player.getDistanceSq(pos) >= AntiCrystal.mc.player.getDistanceSq(closestPos)) {
                            continue;
                        }
                    }
                    smallestDamage = damage;
                    closestPos = pos;
                }
            }
        }
        return closestPos;
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getStage() == 0 && this.rotate.getValue() && this.rotating) {
            if (event.getPacket() instanceof CPacketPlayer) {
                final CPacketPlayer packet = (CPacketPlayer)event.getPacket();
                packet.yaw = this.yaw;
                packet.pitch = this.pitch;
            }
            ++this.rotationPacketsSpoofed;
            if (this.rotationPacketsSpoofed >= this.rotations.getValue()) {
                this.rotating = false;
                this.rotationPacketsSpoofed = 0;
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (fullNullCheck()) {
            return;
        }
        if (event.getPacket() instanceof SPacketSpawnObject && this.instant.getValue()) {
            final SPacketSpawnObject packet2 = (SPacketSpawnObject)event.getPacket();
            final BlockPos pos = new BlockPos(packet2.getX(), packet2.getY(), packet2.getZ());
            if (packet2.getType() == 51 && this.targets.contains(pos.down()) && AutoCrystal.mc.player.getDistanceSq(pos) <= MathUtil.square(this.range.getValue())) {
                this.attackCrystalPredict(packet2.getEntityID(), pos);
                this.targets.clear();
            }
        }
    }
    
    @Override
    public void onTick() {
        if (this.mode.getValue() == Updates.TICK) {
            this.doAntiCrystal();
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.mode.getValue() == Updates.UPDATE) {
            this.doAntiCrystal();
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (this.mode.getValue() == Updates.WALKING) {
            this.doAntiCrystal();
        }
    }
    
    private void doAntiCrystal() {
        if (!fullNullCheck() && this.checkTimer.passedMs(this.checkDelay.getValue())) {
            final Entity deadlyCrystal = this.getDeadlyCrystal();
            if (deadlyCrystal != null) {
                if (this.getSafetyCrystals(deadlyCrystal) < this.wasteAmount.getValue()) {
                    final BlockPos placeTarget = this.getPlaceTarget(deadlyCrystal);
                    if (placeTarget != null) {
                        this.targets.add(placeTarget);
                    }
                    this.placeCrystal(deadlyCrystal);
                }
                this.breakTarget = this.getBreakTarget(deadlyCrystal);
                this.breakCrystal();
            }
            this.checkTimer.reset();
        }
    }
    
    public Entity getBreakTarget(final Entity deadlyCrystal) {
        Entity smallestCrystal = null;
        float smallestDamage = 10.0f;
        for (final Entity entity : AntiCrystal.mc.world.loadedEntityList) {
            final float damage;
            if (entity instanceof EntityEnderCrystal && (damage = DamageUtil.calculateDamage(entity, (Entity)AntiCrystal.mc.player)) <= this.selfDmg.getValue() && entity.getDistanceSq(deadlyCrystal) <= 144.0) {
                if (AntiCrystal.mc.player.getDistanceSq(entity) > MathUtil.square(this.wallsRange.getValue()) && EntityUtil.rayTraceHitCheck(entity, true)) {
                    continue;
                }
                if (smallestCrystal == null) {
                    smallestCrystal = entity;
                    smallestDamage = damage;
                }
                else {
                    if (damage >= smallestDamage) {
                        if (smallestDamage != damage) {
                            continue;
                        }
                        if (AntiCrystal.mc.player.getDistanceSq(entity) >= AntiCrystal.mc.player.getDistanceSq(smallestCrystal)) {
                            continue;
                        }
                    }
                    smallestCrystal = entity;
                    smallestDamage = damage;
                }
            }
        }
        return smallestCrystal;
    }
    
    private void placeCrystal(final Entity deadlyCrystal) {
        final boolean offhand = AntiCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
        if (this.timer.passedMs(this.placeDelay.getValue()) && (this.switcher.getValue() == Switch.NORMAL || this.switcher.getValue() == Switch.SILENT || AntiCrystal.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || offhand) && !this.targets.isEmpty() && this.getSafetyCrystals(deadlyCrystal) <= this.wasteAmount.getValue()) {
            if (this.switcher.getValue() == Switch.NORMAL && AntiCrystal.mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && !offhand) {
                this.doSwitch();
            }
            if (!this.targets.isEmpty()) {
                this.rotateToPos(this.targets.get(this.targets.size() - 1));
                BlockUtil.placeCrystalOnBlock(this.targets.get(this.targets.size() - 1), offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, true, true, this.switcher.getValue() == Switch.SILENT);
            }
            this.timer.reset();
        }
    }
    
    private void doSwitch() {
        int crystalSlot = (AntiCrystal.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? AntiCrystal.mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (AntiCrystal.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }
        if (crystalSlot != -1) {
            AntiCrystal.mc.player.inventory.currentItem = crystalSlot;
        }
    }
    
    private void breakCrystal() {
        if (this.breakTimer.passedMs(this.breakDelay.getValue()) && this.breakTarget != null && DamageUtil.canBreakWeakness((EntityPlayer)AntiCrystal.mc.player)) {
            this.rotateTo(this.breakTarget);
            EntityUtil.attackEntity(this.breakTarget, this.packet.getValue(), true);
            this.breakTimer.reset();
            this.targets.clear();
        }
    }
    
    private void attackCrystalPredict(final int entityID, final BlockPos pos) {
        final CPacketUseEntity attackPacket = new CPacketUseEntity();
        attackPacket.entityId = entityID;
        attackPacket.action = CPacketUseEntity.Action.ATTACK;
        AutoCrystal.mc.player.connection.sendPacket((Packet)attackPacket);
        AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
        if (this.resetBreakTimer.getValue()) {
            this.breakTimer.reset();
        }
    }
    
    private void rotateTo(final Entity entity) {
        if (this.rotate.getValue()) {
            final float[] angle = MathUtil.calcAngle(AntiCrystal.mc.player.getPositionEyes(AntiCrystal.mc.getRenderPartialTicks()), entity.getPositionVector());
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.rotating = true;
        }
    }
    
    private void rotateToPos(final BlockPos pos) {
        if (this.rotate.getValue()) {
            final float[] angle = MathUtil.calcAngle(AntiCrystal.mc.player.getPositionEyes(AntiCrystal.mc.getRenderPartialTicks()), new Vec3d((double)(pos.getX() + 0.5f), (double)(pos.getY() - 0.5f), (double)(pos.getZ() + 0.5f)));
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.rotating = true;
        }
    }
    
    public enum Switch
    {
        NONE, 
        SILENT, 
        NORMAL;
    }
    
    public enum Updates
    {
        TICK, 
        UPDATE, 
        WALKING;
    }
}
