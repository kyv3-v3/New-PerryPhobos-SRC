/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockFalling
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.entity.Entity
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 */
package me.earth.phobos.features.modules.combat;

import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.BlockUtil;
import me.earth.phobos.util.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class SelfAnvil
extends Module {
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    private final Setting<Boolean> onlyHole = this.register(new Setting<Boolean>("HoleOnly", false));
    private final Setting<Boolean> helpingBlocks = this.register(new Setting<Boolean>("HelpingBlocks", true));
    private final Setting<Boolean> chat = this.register(new Setting<Boolean>("Chat Msgs", true));
    private final Setting<Boolean> packet = this.register(new Setting<Boolean>("Packet", false));
    private final Setting<Integer> blocksPerTick = this.register(new Setting<Integer>("Blocks/Tick", 2, 1, 8));
    private BlockPos placePos;
    private BlockPos playerPos;
    private int blockSlot;
    private int obbySlot;
    private int lastBlock;
    private int blocksThisTick;

    public SelfAnvil() {
        super("SelfAnvil", "Self Anvil (on some servers bypasses burrow patches).", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        this.playerPos = new BlockPos(SelfAnvil.mc.field_71439_g.field_70165_t, SelfAnvil.mc.field_71439_g.field_70163_u, SelfAnvil.mc.field_71439_g.field_70161_v);
        this.placePos = this.playerPos.func_177967_a(EnumFacing.UP, 2);
        this.blockSlot = this.findBlockSlot();
        this.obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        this.lastBlock = SelfAnvil.mc.field_71439_g.field_71071_by.field_70461_c;
        if (!this.doFirstChecks()) {
            this.disable();
        }
    }

    @Override
    public void onTick() {
        this.blocksThisTick = 0;
        this.doSelfAnvil();
    }

    private void doSelfAnvil() {
        if (this.helpingBlocks.getValue().booleanValue() && BlockUtil.isPositionPlaceable(this.placePos, false, true) == 2) {
            InventoryUtil.switchToHotbarSlot(this.obbySlot, false);
            this.doHelpBlocks();
        }
        if (this.blocksThisTick < this.blocksPerTick.getValue() && BlockUtil.isPositionPlaceable(this.placePos, false, true) == 3) {
            InventoryUtil.switchToHotbarSlot(this.blockSlot, false);
            BlockUtil.placeBlock(this.placePos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), false);
            InventoryUtil.switchToHotbarSlot(this.lastBlock, false);
            SelfAnvil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)SelfAnvil.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
            this.disable();
        }
    }

    private void doHelpBlocks() {
        if (this.blocksThisTick >= this.blocksPerTick.getValue()) {
            return;
        }
        for (EnumFacing side1 : EnumFacing.values()) {
            if (side1 == EnumFacing.DOWN || BlockUtil.isPositionPlaceable(this.placePos.func_177972_a(side1), false, true) != 3) continue;
            BlockUtil.placeBlock(this.placePos.func_177972_a(side1), EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), false);
            ++this.blocksThisTick;
            return;
        }
        for (EnumFacing side1 : EnumFacing.values()) {
            if (side1 == EnumFacing.DOWN) continue;
            for (EnumFacing side2 : EnumFacing.values()) {
                if (BlockUtil.isPositionPlaceable(this.placePos.func_177972_a(side1).func_177972_a(side2), false, true) != 3) continue;
                BlockUtil.placeBlock(this.placePos.func_177972_a(side1).func_177972_a(side2), EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), false);
                ++this.blocksThisTick;
                return;
            }
        }
        for (EnumFacing side1 : EnumFacing.values()) {
            for (EnumFacing side2 : EnumFacing.values()) {
                for (EnumFacing side3 : EnumFacing.values()) {
                    if (BlockUtil.isPositionPlaceable(this.placePos.func_177972_a(side1).func_177972_a(side2).func_177972_a(side3), false, true) != 3) continue;
                    BlockUtil.placeBlock(this.placePos.func_177972_a(side1).func_177972_a(side2).func_177972_a(side3), EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), false);
                    ++this.blocksThisTick;
                    return;
                }
            }
        }
    }

    private int findBlockSlot() {
        for (int i = 0; i < 9; ++i) {
            Block block;
            ItemStack item = SelfAnvil.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (!(item.func_77973_b() instanceof ItemBlock) || !((block = Block.func_149634_a((Item)SelfAnvil.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b())) instanceof BlockFalling)) continue;
            return i;
        }
        return -1;
    }

    private boolean doFirstChecks() {
        int canPlace = BlockUtil.isPositionPlaceable(this.placePos, false, true);
        if (SelfAnvil.fullNullCheck() || !SelfAnvil.mc.field_71441_e.func_175623_d(this.playerPos)) {
            return false;
        }
        if (!BlockUtil.isBothHole(this.playerPos) && this.onlyHole.getValue().booleanValue()) {
            return false;
        }
        if (this.blockSlot == -1) {
            if (this.chat.getValue().booleanValue()) {
                Command.sendMessage("<" + this.getDisplayName() + "> \u00a7cNo Anvils in hotbar.");
            }
            return false;
        }
        if (canPlace == 2) {
            if (!this.helpingBlocks.getValue().booleanValue()) {
                if (this.chat.getValue().booleanValue()) {
                    Command.sendMessage("<" + this.getDisplayName() + "> \u00a7cNowhere to place.");
                }
                return false;
            }
            if (this.obbySlot == -1) {
                if (this.chat.getValue().booleanValue()) {
                    Command.sendMessage("<" + this.getDisplayName() + "> \u00a7cNo Obsidian in hotbar.");
                }
                return false;
            }
        } else if (canPlace != 3) {
            if (this.chat.getValue().booleanValue()) {
                Command.sendMessage("<" + this.getDisplayName() + "> \u00a7cNot enough room.");
            }
            return false;
        }
        return true;
    }
}

