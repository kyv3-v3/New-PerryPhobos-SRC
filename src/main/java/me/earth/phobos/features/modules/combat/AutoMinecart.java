/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.block.BlockWeb
 *  net.minecraft.block.material.Material
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityMinecartTNT
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package me.earth.phobos.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.BlockUtil;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.InventoryUtil;
import me.earth.phobos.util.MathUtil;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class AutoMinecart
extends Module {
    private final Setting<Boolean> web = this.register(new Setting<Boolean>("Web", false));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", false));
    private final Setting<Boolean> packet = this.register(new Setting<Boolean>("PacketPlace", false));
    private final Setting<Integer> blocksPerTick = this.register(new Setting<Integer>("Block/Place", 8, 1, 20));
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Carts", 20, 1, 50));
    public Setting<Float> minHP = this.register(new Setting<Float>("MinHP", Float.valueOf(4.0f), Float.valueOf(0.0f), Float.valueOf(36.0f)));
    int wait;
    int waitFlint;
    int originalSlot;
    private boolean check;

    public AutoMinecart() {
        super("AutoMinecart", "Places and explodes minecarts on other players.", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        if (AutoMinecart.fullNullCheck()) {
            this.toggle();
        }
        this.wait = 0;
        this.waitFlint = 0;
        this.originalSlot = AutoMinecart.mc.field_71439_g.field_71071_by.field_70461_c;
        this.check = true;
    }

    @Override
    public void onUpdate() {
        EntityPlayer target;
        if (AutoMinecart.fullNullCheck()) {
            this.toggle();
        }
        int i = InventoryUtil.findStackInventory(Items.field_151142_bV);
        for (int j = 0; j < 9; ++j) {
            if (AutoMinecart.mc.field_71439_g.field_71071_by.func_70301_a(j).func_77973_b() != Items.field_190931_a || i == -1) continue;
            AutoMinecart.mc.field_71442_b.func_187098_a(AutoMinecart.mc.field_71439_g.field_71069_bz.field_75152_c, i, 0, ClickType.QUICK_MOVE, (EntityPlayer)AutoMinecart.mc.field_71439_g);
            AutoMinecart.mc.field_71442_b.func_78765_e();
        }
        int webSlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
        int tntSlot = InventoryUtil.getItemHotbar(Items.field_151142_bV);
        int flintSlot = InventoryUtil.getItemHotbar(Items.field_151033_d);
        int railSlot = InventoryUtil.findHotbarBlock(Blocks.field_150408_cc);
        int picSlot = InventoryUtil.getItemHotbar(Items.field_151046_w);
        if (tntSlot == -1 || railSlot == -1 || flintSlot == -1 || picSlot == -1 || this.web.getValue().booleanValue() && webSlot == -1) {
            Command.sendMessage("<" + this.getDisplayName() + "> " + (Object)ChatFormatting.RED + "No (tnt minecart/activator rail/flint/pic/webs) in hotbar disabling...");
            this.toggle();
        }
        if ((target = this.getTarget()) == null) {
            return;
        }
        BlockPos pos = new BlockPos(target.field_70165_t, target.field_70163_u, target.field_70161_v);
        Vec3d hitVec = new Vec3d((Vec3i)pos).func_72441_c(0.0, -0.5, 0.0);
        if (AutoMinecart.mc.field_71439_g.func_174818_b(pos) <= MathUtil.square(6.0)) {
            this.check = true;
            if (AutoMinecart.mc.field_71441_e.func_180495_p(pos).func_177230_c() != Blocks.field_150408_cc && !AutoMinecart.mc.field_71441_e.func_72872_a(EntityMinecartTNT.class, new AxisAlignedBB(pos)).isEmpty()) {
                InventoryUtil.switchToHotbarSlot(flintSlot, false);
                BlockUtil.rightClickBlock(pos.func_177977_b(), hitVec, EnumHand.MAIN_HAND, EnumFacing.UP, this.packet.getValue());
            }
            if (AutoMinecart.mc.field_71441_e.func_180495_p(pos).func_177230_c() != Blocks.field_150408_cc && AutoMinecart.mc.field_71441_e.func_72872_a(EntityMinecartTNT.class, new AxisAlignedBB(pos)).isEmpty() && AutoMinecart.mc.field_71441_e.func_72872_a(EntityMinecartTNT.class, new AxisAlignedBB(pos.func_177984_a())).isEmpty() && AutoMinecart.mc.field_71441_e.func_72872_a(EntityMinecartTNT.class, new AxisAlignedBB(pos.func_177977_b())).isEmpty()) {
                InventoryUtil.switchToHotbarSlot(railSlot, false);
                BlockUtil.rightClickBlock(pos.func_177977_b(), hitVec, EnumHand.MAIN_HAND, EnumFacing.UP, this.packet.getValue());
                this.wait = 0;
            }
            if (this.web.getValue().booleanValue() && this.wait != 0 && AutoMinecart.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150408_cc && !target.field_70134_J && (BlockUtil.isPositionPlaceable(pos.func_177984_a(), false) == 1 || BlockUtil.isPositionPlaceable(pos.func_177984_a(), false) == 3) && AutoMinecart.mc.field_71441_e.func_72872_a(EntityMinecartTNT.class, new AxisAlignedBB(pos.func_177984_a())).isEmpty()) {
                InventoryUtil.switchToHotbarSlot(webSlot, false);
                BlockUtil.placeBlock(pos.func_177984_a(), EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), false);
            }
            if (AutoMinecart.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150408_cc) {
                InventoryUtil.switchToHotbarSlot(tntSlot, false);
                for (int u = 0; u < this.blocksPerTick.getValue(); ++u) {
                    BlockUtil.rightClickBlock(pos, hitVec, EnumHand.MAIN_HAND, EnumFacing.UP, this.packet.getValue());
                }
            }
            if (this.wait < this.delay.getValue()) {
                ++this.wait;
                return;
            }
            this.check = false;
            this.wait = 0;
            InventoryUtil.switchToHotbarSlot(picSlot, false);
            if (AutoMinecart.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150408_cc && !AutoMinecart.mc.field_71441_e.func_72872_a(EntityMinecartTNT.class, new AxisAlignedBB(pos)).isEmpty()) {
                AutoMinecart.mc.field_71442_b.func_180512_c(pos, EnumFacing.UP);
            }
            InventoryUtil.switchToHotbarSlot(flintSlot, false);
            if (AutoMinecart.mc.field_71441_e.func_180495_p(pos).func_177230_c().func_176194_O().func_177621_b().func_185904_a() != Material.field_151581_o && !AutoMinecart.mc.field_71441_e.func_72872_a(EntityMinecartTNT.class, new AxisAlignedBB(pos)).isEmpty()) {
                BlockUtil.rightClickBlock(pos.func_177977_b(), hitVec, EnumHand.MAIN_HAND, EnumFacing.UP, this.packet.getValue());
            }
        }
    }

    @Override
    public String getDisplayInfo() {
        if (this.check) {
            return (Object)ChatFormatting.GREEN + "Placing";
        }
        return (Object)ChatFormatting.RED + "Breaking";
    }

    @Override
    public void onDisable() {
        AutoMinecart.mc.field_71439_g.field_71071_by.field_70461_c = this.originalSlot;
    }

    private EntityPlayer getTarget() {
        EntityPlayer target = null;
        double distance = Math.pow(6.0, 2.0) + 1.0;
        for (EntityPlayer player : AutoMinecart.mc.field_71441_e.field_73010_i) {
            if (EntityUtil.isntValid((Entity)player, 6.0) || player.func_70090_H() || player.func_180799_ab() || !EntityUtil.isTrapped(player, false, false, false, false, false, false) || player.func_110143_aJ() + player.func_110139_bj() > this.minHP.getValue().floatValue()) continue;
            if (target == null) {
                target = player;
                distance = AutoMinecart.mc.field_71439_g.func_70068_e((Entity)player);
                continue;
            }
            if (!(AutoMinecart.mc.field_71439_g.func_70068_e((Entity)player) < distance)) continue;
            target = player;
            distance = AutoMinecart.mc.field_71439_g.func_70068_e((Entity)player);
        }
        return target;
    }
}

