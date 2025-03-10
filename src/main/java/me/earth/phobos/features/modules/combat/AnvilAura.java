/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAnvil
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.earth.phobos.features.modules.combat;

import java.util.ArrayList;
import java.util.List;
import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.BlockUtil;
import me.earth.phobos.util.InventoryUtil;
import me.earth.phobos.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AnvilAura
extends Module {
    public Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(6.0f), Float.valueOf(0.0f), Float.valueOf(10.0f)));
    public Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    public Setting<Boolean> packet = this.register(new Setting<Boolean>("Packet", true));
    public Setting<Boolean> switcher = this.register(new Setting<Boolean>("Switch", true));
    public Setting<Integer> rotations = this.register(new Setting<Integer>("Spoofs", 1, 1, 20));
    private float yaw;
    private float pitch;
    private boolean rotating;
    private int rotationPacketsSpoofed;
    private BlockPos placeTarget;

    public AnvilAura() {
        super("AnvilAura", "WIP.", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {
        this.doAnvilAura();
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() == 0 && this.rotate.getValue().booleanValue() && this.rotating) {
            if (event.getPacket() instanceof CPacketPlayer) {
                CPacketPlayer packet = (CPacketPlayer)event.getPacket();
                packet.field_149476_e = this.yaw;
                packet.field_149473_f = this.pitch;
            }
            ++this.rotationPacketsSpoofed;
            if (this.rotationPacketsSpoofed >= this.rotations.getValue()) {
                this.rotating = false;
                this.rotationPacketsSpoofed = 0;
            }
        }
    }

    public void doAnvilAura() {
        EntityPlayer finalTarget = this.getTarget();
        if (finalTarget != null) {
            this.placeTarget = this.getTargetPos((Entity)finalTarget);
        }
        if (this.placeTarget != null && finalTarget != null) {
            this.placeAnvil(this.placeTarget);
        }
    }

    public void placeAnvil(BlockPos pos) {
        if (this.rotate.getValue().booleanValue()) {
            this.rotateToPos(pos);
        }
        if (this.switcher.getValue().booleanValue() && !this.isHoldingAnvil()) {
            this.doSwitch();
        }
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, false, this.packet.getValue(), AnvilAura.mc.field_71439_g.func_70093_af());
    }

    public boolean isHoldingAnvil() {
        InventoryUtil.findHotbarBlock(BlockObsidian.class);
        return AnvilAura.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBlock && ((ItemBlock)AnvilAura.mc.field_71439_g.func_184614_ca().func_77973_b()).func_179223_d() instanceof BlockAnvil || AnvilAura.mc.field_71439_g.func_184592_cb().func_77973_b() instanceof ItemBlock && ((ItemBlock)AnvilAura.mc.field_71439_g.func_184592_cb().func_77973_b()).func_179223_d() instanceof BlockAnvil;
    }

    public void doSwitch() {
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        if (obbySlot == -1) {
            for (int l = 0; l < 9; ++l) {
                ItemStack stack = AnvilAura.mc.field_71439_g.field_71071_by.func_70301_a(l);
                Block block = ((ItemBlock)stack.func_77973_b()).func_179223_d();
                if (!(block instanceof BlockObsidian)) continue;
                obbySlot = l;
            }
        }
        if (obbySlot != -1) {
            AnvilAura.mc.field_71439_g.field_71071_by.field_70461_c = obbySlot;
        }
    }

    public EntityPlayer getTarget() {
        double shortestDistance = -1.0;
        EntityPlayer target = null;
        for (EntityPlayer player : AnvilAura.mc.field_71441_e.field_73010_i) {
            if (this.getPlaceableBlocksAboveEntity((Entity)player).isEmpty() || shortestDistance != -1.0 && !(AnvilAura.mc.field_71439_g.func_70068_e((Entity)player) < MathUtil.square(shortestDistance))) continue;
            shortestDistance = AnvilAura.mc.field_71439_g.func_70032_d((Entity)player);
            target = player;
        }
        return target;
    }

    public BlockPos getTargetPos(Entity target) {
        double distance = -1.0;
        BlockPos finalPos = null;
        for (BlockPos pos : this.getPlaceableBlocksAboveEntity(target)) {
            if (distance != -1.0 && !(AnvilAura.mc.field_71439_g.func_174818_b(pos) < MathUtil.square(distance))) continue;
            finalPos = pos;
            distance = AnvilAura.mc.field_71439_g.func_70011_f((double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p());
        }
        return finalPos;
    }

    public List<BlockPos> getPlaceableBlocksAboveEntity(Entity target) {
        BlockPos pos;
        new BlockPos(Math.floor(AnvilAura.mc.field_71439_g.field_70165_t), Math.floor(AnvilAura.mc.field_71439_g.field_70163_u), Math.floor(AnvilAura.mc.field_71439_g.field_70161_v));
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        for (int i = (int)Math.floor(AnvilAura.mc.field_71439_g.field_70163_u + 2.0); i <= 256 && BlockUtil.isPositionPlaceable(pos = new BlockPos(Math.floor(AnvilAura.mc.field_71439_g.field_70165_t), (double)i, Math.floor(AnvilAura.mc.field_71439_g.field_70161_v)), false) != 0 && BlockUtil.isPositionPlaceable(pos, false) != -1 && BlockUtil.isPositionPlaceable(pos, false) != 2; ++i) {
            positions.add(pos);
        }
        return positions;
    }

    private void rotateToPos(BlockPos pos) {
        if (this.rotate.getValue().booleanValue()) {
            float[] angle = MathUtil.calcAngle(AnvilAura.mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((double)((float)pos.func_177958_n() + 0.5f), (double)((float)pos.func_177956_o() - 0.5f), (double)((float)pos.func_177952_p() + 0.5f)));
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.rotating = true;
        }
    }
}

