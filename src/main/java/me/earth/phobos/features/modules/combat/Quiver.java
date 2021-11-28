/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.init.PotionTypes
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemAir
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemTippedArrow
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.potion.PotionUtils
 *  net.minecraft.util.math.BlockPos
 */
package me.earth.phobos.features.modules.combat;

import java.util.ArrayList;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.InventoryUtil;
import me.earth.phobos.util.RotationUtil;
import me.earth.phobos.util.TimerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTippedArrow;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;

public class Quiver
extends Module {
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 200, 0, 500));
    private final Setting<Integer> holdLength = this.register(new Setting<Integer>("Hold Length", 350, 100, 1000));
    private final Setting<mainEnum> main = this.register(new Setting<mainEnum>("Main", mainEnum.SPEED));
    private final Setting<mainEnum> secondary = this.register(new Setting<mainEnum>("Secondary", mainEnum.STRENGTH));
    private final TimerUtil delayTimer = new TimerUtil();
    private final TimerUtil holdTimer = new TimerUtil();
    private int stage;
    private ArrayList<Integer> map;
    private int strSlot = -1;
    private int speedSlot = -1;
    private int oldSlot = 1;

    public Quiver() {
        super("Quiver", "Automatically shoots yourself with good effects.", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        if (Quiver.nullCheck()) {
            return;
        }
        InventoryUtil.switchToHotbarSlot(ItemBow.class, false);
        this.clean();
        this.oldSlot = Quiver.mc.field_71439_g.field_71071_by.field_70461_c;
        Quiver.mc.field_71474_y.field_74313_G.field_74513_e = false;
    }

    @Override
    public void onDisable() {
        if (Quiver.nullCheck()) {
            return;
        }
        InventoryUtil.switchToHotbarSlot(this.oldSlot, false);
        Quiver.mc.field_71474_y.field_74313_G.field_74513_e = false;
        this.clean();
    }

    @Override
    public void onUpdate() {
        if (Quiver.nullCheck()) {
            return;
        }
        if (Quiver.mc.field_71462_r != null) {
            return;
        }
        if (InventoryUtil.findItemInventorySlot((Item)Items.field_151031_f, true) == -1) {
            Command.sendMessage("Couldn't find bow in inventory! Toggling!");
            this.toggle();
        }
        RotationUtil.faceVector(EntityUtil.getInterpolatedPos((Entity)Quiver.mc.field_71439_g, Quiver.mc.field_71428_T.field_194148_c).func_72441_c(0.0, 3.0, 0.0), false);
        if (this.stage == 0) {
            this.map = this.mapArrows();
            for (int a : this.map) {
                ItemStack arrow = (ItemStack)Quiver.mc.field_71439_g.field_71069_bz.func_75138_a().get(a);
                if ((PotionUtils.func_185191_c((ItemStack)arrow).equals((Object)PotionTypes.field_185223_F) || PotionUtils.func_185191_c((ItemStack)arrow).equals((Object)PotionTypes.field_185225_H) || PotionUtils.func_185191_c((ItemStack)arrow).equals((Object)PotionTypes.field_185224_G)) && this.strSlot == -1) {
                    this.strSlot = a;
                }
                if (!PotionUtils.func_185191_c((ItemStack)arrow).equals((Object)PotionTypes.field_185243_o) && !PotionUtils.func_185191_c((ItemStack)arrow).equals((Object)PotionTypes.field_185244_p) && !PotionUtils.func_185191_c((ItemStack)arrow).equals((Object)PotionTypes.field_185245_q) || this.speedSlot != -1) continue;
                this.speedSlot = a;
            }
            ++this.stage;
        } else if (this.stage == 1) {
            if (!this.delayTimer.passedMs(this.delay.getValue().intValue())) {
                return;
            }
            this.delayTimer.reset();
            ++this.stage;
        } else if (this.stage == 2) {
            this.switchTo(this.main.getValue());
            ++this.stage;
        } else if (this.stage == 3) {
            if (!this.delayTimer.passedMs(this.delay.getValue().intValue())) {
                return;
            }
            this.delayTimer.reset();
            ++this.stage;
        } else if (this.stage == 4) {
            Quiver.mc.field_71474_y.field_74313_G.field_74513_e = true;
            this.holdTimer.reset();
            ++this.stage;
        } else if (this.stage == 5) {
            if (!this.holdTimer.passedMs(this.holdLength.getValue().intValue())) {
                return;
            }
            this.holdTimer.reset();
            ++this.stage;
        } else if (this.stage == 6) {
            Quiver.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, Quiver.mc.field_71439_g.func_174811_aO()));
            Quiver.mc.field_71439_g.func_184602_cy();
            Quiver.mc.field_71474_y.field_74313_G.field_74513_e = false;
            ++this.stage;
        } else if (this.stage == 7) {
            if (!this.delayTimer.passedMs(this.delay.getValue().intValue())) {
                return;
            }
            this.delayTimer.reset();
            ++this.stage;
        } else if (this.stage == 8) {
            this.map = this.mapArrows();
            this.strSlot = -1;
            this.speedSlot = -1;
            for (int a : this.map) {
                ItemStack arrow = (ItemStack)Quiver.mc.field_71439_g.field_71069_bz.func_75138_a().get(a);
                if ((PotionUtils.func_185191_c((ItemStack)arrow).equals((Object)PotionTypes.field_185223_F) || PotionUtils.func_185191_c((ItemStack)arrow).equals((Object)PotionTypes.field_185225_H) || PotionUtils.func_185191_c((ItemStack)arrow).equals((Object)PotionTypes.field_185224_G)) && this.strSlot == -1) {
                    this.strSlot = a;
                }
                if (!PotionUtils.func_185191_c((ItemStack)arrow).equals((Object)PotionTypes.field_185243_o) && !PotionUtils.func_185191_c((ItemStack)arrow).equals((Object)PotionTypes.field_185244_p) && !PotionUtils.func_185191_c((ItemStack)arrow).equals((Object)PotionTypes.field_185245_q) || this.speedSlot != -1) continue;
                this.speedSlot = a;
            }
            ++this.stage;
        }
        if (this.stage == 9) {
            this.switchTo(this.secondary.getValue());
            ++this.stage;
        } else if (this.stage == 10) {
            if (!this.delayTimer.passedMs(this.delay.getValue().intValue())) {
                return;
            }
            ++this.stage;
        } else if (this.stage == 11) {
            Quiver.mc.field_71474_y.field_74313_G.field_74513_e = true;
            this.holdTimer.reset();
            ++this.stage;
        } else if (this.stage == 12) {
            if (!this.holdTimer.passedMs(this.holdLength.getValue().intValue())) {
                return;
            }
            this.holdTimer.reset();
            ++this.stage;
        } else if (this.stage == 13) {
            Quiver.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, Quiver.mc.field_71439_g.func_174811_aO()));
            Quiver.mc.field_71439_g.func_184602_cy();
            Quiver.mc.field_71474_y.field_74313_G.field_74513_e = false;
            ++this.stage;
        } else if (this.stage == 14) {
            ArrayList<Integer> map = this.mapEmpty();
            if (!map.isEmpty()) {
                int a;
                a = map.get(0);
                Quiver.mc.field_71442_b.func_187098_a(Quiver.mc.field_71439_g.field_71069_bz.field_75152_c, a, 0, ClickType.PICKUP, (EntityPlayer)Quiver.mc.field_71439_g);
            }
            ++this.stage;
        } else if (this.stage == 15) {
            this.setEnabled(false);
        }
    }

    private void switchTo(Enum<mainEnum> mode) {
        if (mode.toString().equalsIgnoreCase("STRENGTH") && this.strSlot != -1) {
            this.switchTo(this.strSlot);
        }
        if (mode.toString().equalsIgnoreCase("SPEED") && this.speedSlot != -1) {
            this.switchTo(this.speedSlot);
        }
    }

    private ArrayList<Integer> mapArrows() {
        ArrayList<Integer> map = new ArrayList<Integer>();
        for (int a = 9; a < 45; ++a) {
            if (!(((ItemStack)Quiver.mc.field_71439_g.field_71069_bz.func_75138_a().get(a)).func_77973_b() instanceof ItemTippedArrow)) continue;
            ItemStack arrow = (ItemStack)Quiver.mc.field_71439_g.field_71069_bz.func_75138_a().get(a);
            if (PotionUtils.func_185191_c((ItemStack)arrow).equals((Object)PotionTypes.field_185223_F) || PotionUtils.func_185191_c((ItemStack)arrow).equals((Object)PotionTypes.field_185225_H) || PotionUtils.func_185191_c((ItemStack)arrow).equals((Object)PotionTypes.field_185224_G)) {
                map.add(a);
            }
            if (!PotionUtils.func_185191_c((ItemStack)arrow).equals((Object)PotionTypes.field_185243_o) && !PotionUtils.func_185191_c((ItemStack)arrow).equals((Object)PotionTypes.field_185244_p) && !PotionUtils.func_185191_c((ItemStack)arrow).equals((Object)PotionTypes.field_185245_q)) continue;
            map.add(a);
        }
        return map;
    }

    private ArrayList<Integer> mapEmpty() {
        ArrayList<Integer> map = new ArrayList<Integer>();
        for (int a = 9; a < 45; ++a) {
            if (!(((ItemStack)Quiver.mc.field_71439_g.field_71069_bz.func_75138_a().get(a)).func_77973_b() instanceof ItemAir) && Quiver.mc.field_71439_g.field_71069_bz.func_75138_a().get(a) != ItemStack.field_190927_a) continue;
            map.add(a);
        }
        return map;
    }

    private void switchTo(int from) {
        if (from == 9) {
            return;
        }
        Quiver.mc.field_71442_b.func_187098_a(Quiver.mc.field_71439_g.field_71069_bz.field_75152_c, from, 0, ClickType.PICKUP, (EntityPlayer)Quiver.mc.field_71439_g);
        Quiver.mc.field_71442_b.func_187098_a(Quiver.mc.field_71439_g.field_71069_bz.field_75152_c, 9, 0, ClickType.PICKUP, (EntityPlayer)Quiver.mc.field_71439_g);
        Quiver.mc.field_71442_b.func_187098_a(Quiver.mc.field_71439_g.field_71069_bz.field_75152_c, from, 0, ClickType.PICKUP, (EntityPlayer)Quiver.mc.field_71439_g);
        Quiver.mc.field_71442_b.func_78765_e();
    }

    private void clean() {
        this.holdTimer.reset();
        this.delayTimer.reset();
        this.map = null;
        this.speedSlot = -1;
        this.strSlot = -1;
        this.stage = 0;
    }

    private static enum mainEnum {
        STRENGTH,
        SPEED;

    }
}

