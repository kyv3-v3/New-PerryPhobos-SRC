/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.NonNullList
 */
package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;

public class PacketMend
extends Module {
    public Setting<Boolean> sneakOnly = this.register(new Setting<Boolean>("SneakOnly", false));
    public Setting<Boolean> noEntityCollision = this.register(new Setting<Boolean>("No Collision", true));
    public Setting<Boolean> silentSwitch = this.register(new Setting<Boolean>("Silent Switch", true));
    public Setting<Integer> minDamage = this.register(new Setting<Integer>("Min Damage", 100, 1, 100));
    public Setting<Integer> maxHeal = this.register(new Setting<Integer>("Repair To", 90, 1, 100));
    public Setting<Boolean> predict = this.register(new Setting<Boolean>("Predict", false));
    public Setting<Boolean> DisableWhenDone = this.register(new Setting<Boolean>("AutoDisable", true));
    public Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    char toMend;

    public PacketMend() {
        super("PacketMend", "Automatically mends.", Module.Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (PacketMend.mc.field_71439_g == null || PacketMend.mc.field_71441_e == null) {
            return;
        }
        int sumOfDamage = 0;
        NonNullList armour = PacketMend.mc.field_71439_g.field_71071_by.field_70460_b;
        for (int i = 0; i < armour.size(); ++i) {
            ItemStack itemStack = (ItemStack)armour.get(i);
            if (itemStack.field_190928_g) continue;
            float damageOnArmor = itemStack.func_77958_k() - itemStack.func_77952_i();
            float damagePercent = 100.0f - 100.0f * (1.0f - damageOnArmor / (float)itemStack.func_77958_k());
            if (!(damagePercent >= (float)this.maxHeal.getValue().intValue()) && this.DisableWhenDone.getValue().booleanValue()) {
                this.toggle();
            }
            if (damagePercent <= (float)this.maxHeal.getValue().intValue()) {
                if (damagePercent <= (float)this.minDamage.getValue().intValue()) {
                    this.toMend = (char)(this.toMend | 1 << i);
                }
                if (!this.predict.getValue().booleanValue()) continue;
                sumOfDamage = (int)((float)sumOfDamage + ((float)(itemStack.func_77958_k() * this.maxHeal.getValue()) / 100.0f - (float)(itemStack.func_77958_k() - itemStack.func_77952_i())));
                continue;
            }
            this.toMend = (char)(this.toMend & ~(1 << i));
        }
        if (this.toMend > '\u0000') {
            if (this.predict.getValue().booleanValue()) {
                int totalXp = PacketMend.mc.field_71441_e.field_72996_f.stream().filter(entity -> entity instanceof EntityXPOrb).filter(entity -> entity.func_70068_e((Entity)PacketMend.mc.field_71439_g) <= 1.0).mapToInt(entity -> ((EntityXPOrb)entity).field_70530_e).sum();
                if (totalXp * 2 < sumOfDamage) {
                    this.mendArmor(PacketMend.mc.field_71439_g.field_71071_by.field_70461_c);
                }
            } else {
                this.mendArmor(PacketMend.mc.field_71439_g.field_71071_by.field_70461_c);
            }
        }
    }

    private void mendArmor(int oldSlot) {
        if (this.noEntityCollision.getValue().booleanValue()) {
            for (EntityPlayer entityPlayer : PacketMend.mc.field_71441_e.field_73010_i) {
                if (!(entityPlayer.func_70032_d((Entity)PacketMend.mc.field_71439_g) < 1.0f) || entityPlayer == PacketMend.mc.field_71439_g) continue;
                return;
            }
        }
        if (this.sneakOnly.getValue().booleanValue() && !PacketMend.mc.field_71439_g.func_70093_af()) {
            return;
        }
        int newSlot = this.findXPSlot();
        if (newSlot == -1) {
            return;
        }
        if (oldSlot != newSlot) {
            if (this.silentSwitch.getValue().booleanValue()) {
                PacketMend.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(newSlot));
            } else {
                PacketMend.mc.field_71439_g.field_71071_by.field_70461_c = newSlot;
            }
            PacketMend.mc.field_71442_b.func_78750_j();
        }
        if (this.rotate.getValue().booleanValue()) {
            PacketMend.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(0.0f, 90.0f, true));
        }
        PacketMend.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        if (this.silentSwitch.getValue().booleanValue()) {
            PacketMend.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(oldSlot));
        } else {
            PacketMend.mc.field_71439_g.field_71071_by.field_70461_c = oldSlot;
        }
        PacketMend.mc.field_71442_b.func_78750_j();
    }

    private int findXPSlot() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            if (PacketMend.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() != Items.field_151062_by) continue;
            slot = i;
            break;
        }
        return slot;
    }
}

