/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.earth.phobos.features.modules.combat;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import me.earth.phobos.Phobos;
import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.BlockUtil;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.PairUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoCity
extends Module {
    private static final BlockPos[] surroundOffset = new BlockPos[]{new BlockPos(0, 0, 0), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0)};
    public Setting<Boolean> raytrace = this.register(new Setting<Boolean>("Raytrace", false));
    public Setting<Integer> range = this.register(new Setting<Integer>("Range", 5, 1, 6));
    public Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    public Setting<Boolean> autodisable = this.register(new Setting<Boolean>("Auto Disable", true));
    public Setting<Integer> rotations = this.register(new Setting<Integer>("Spoofs", 1, 1, 20));

    public AutoCity() {
        super("AutoCity", "Automatically mines ur opponent out of their hole.", Module.Category.COMBAT, true, false, false);
    }

    public static ArrayList<PairUtil<EntityPlayer, ArrayList<BlockPos>>> GetPlayersReadyToBeCitied() {
        ArrayList<PairUtil<EntityPlayer, ArrayList<BlockPos>>> arrayList = new ArrayList<PairUtil<EntityPlayer, ArrayList<BlockPos>>>();
        for (EntityPlayer entity : Objects.requireNonNull(EntityUtil.getNearbyPlayers(6.0)).stream().filter(entityPlayer -> !Phobos.friendManager.isFriend((EntityPlayer)entityPlayer)).collect(Collectors.toList())) {
            ArrayList<BlockPos> arrayList2 = new ArrayList<BlockPos>();
            for (int i = 0; i < 4; ++i) {
                BlockPos blockPos = EntityUtil.GetPositionVectorBlockPos((Entity)entity, surroundOffset[i]);
                if (AutoCity.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150343_Z) continue;
                boolean bl = false;
                switch (i) {
                    case 0: {
                        bl = BlockUtil.canPlaceCrystal(blockPos.func_177964_d(2), true, false, false);
                        break;
                    }
                    case 1: {
                        bl = BlockUtil.canPlaceCrystal(blockPos.func_177965_g(2), true, false, false);
                        break;
                    }
                    case 2: {
                        bl = BlockUtil.canPlaceCrystal(blockPos.func_177970_e(2), true, false, false);
                        break;
                    }
                    case 3: {
                        bl = BlockUtil.canPlaceCrystal(blockPos.func_177985_f(2), true, false, false);
                    }
                }
                if (!bl) continue;
                arrayList2.add(blockPos);
            }
            if (arrayList2.isEmpty()) continue;
            arrayList.add(new PairUtil(entity, arrayList2));
        }
        return arrayList;
    }

    @Override
    public void onEnable() {
        ArrayList<PairUtil<EntityPlayer, ArrayList<BlockPos>>> arrayList = AutoCity.GetPlayersReadyToBeCitied();
        if (arrayList.isEmpty()) {
            Command.sendMessage("There is no one to city!");
            this.toggle();
            return;
        }
        EntityPlayer entityPlayer = null;
        BlockPos blockPos = null;
        double d = 50.0;
        for (PairUtil<EntityPlayer, ArrayList<BlockPos>> pairUtil : arrayList) {
            for (BlockPos blockPos2 : pairUtil.getSecond()) {
                if (blockPos == null) {
                    entityPlayer = pairUtil.getFirst();
                    blockPos = blockPos2;
                    continue;
                }
                double d2 = blockPos2.func_185332_f(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p());
                if (!(d2 < d)) continue;
                d = d2;
                blockPos = blockPos2;
                entityPlayer = pairUtil.getFirst();
            }
        }
        if (blockPos == null || entityPlayer == null) {
            Command.sendMessage("Couldn't find any blocks to mine!");
            this.toggle();
            return;
        }
        BlockUtil.SetCurrentBlock(blockPos);
        Command.sendMessage("Attempting to mine a block by your target: " + entityPlayer.func_70005_c_());
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent updateWalkingPlayerEvent) {
        boolean bl;
        boolean bl2 = bl = AutoCity.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151046_w;
        if (!bl) {
            for (int i = 0; i < 9; ++i) {
                ItemStack itemStack = AutoCity.mc.field_71439_g.field_71071_by.func_70301_a(i);
                if (itemStack.func_190926_b() || itemStack.func_77973_b() != Items.field_151046_w) continue;
                bl = true;
                AutoCity.mc.field_71439_g.field_71071_by.field_70461_c = i;
                AutoCity.mc.field_71442_b.func_78765_e();
                break;
            }
        }
        if (!bl) {
            Command.sendMessage("No pickaxe!");
            this.toggle();
            return;
        }
        BlockPos blockPos = BlockUtil.GetCurrBlock();
        if (blockPos == null) {
            if (this.autodisable.getValue().booleanValue()) {
                Command.sendMessage("Done!");
                this.toggle();
            }
            return;
        }
        if (this.rotate.getValue().booleanValue()) {
            Phobos.rotationManager.updateRotations();
            Phobos.rotationManager.lookAtPos(blockPos);
            updateWalkingPlayerEvent.setCanceled(true);
        }
        BlockUtil.Update(this.range.getValue().intValue(), this.raytrace.getValue());
    }
}

