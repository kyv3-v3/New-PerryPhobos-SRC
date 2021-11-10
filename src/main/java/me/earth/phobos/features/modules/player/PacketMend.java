



package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.item.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;
import java.util.*;
import net.minecraft.init.*;

public class PacketMend extends Module
{
    public Setting<Boolean> sneakOnly;
    public Setting<Boolean> noEntityCollision;
    public Setting<Boolean> silentSwitch;
    public Setting<Integer> minDamage;
    public Setting<Integer> maxHeal;
    public Setting<Boolean> predict;
    public Setting<Boolean> DisableWhenDone;
    public Setting<Boolean> rotate;
    char toMend;
    
    public PacketMend() {
        super("PacketMend", "Automatically mends.", Module.Category.PLAYER, true, false, false);
        this.sneakOnly = (Setting<Boolean>)this.register(new Setting("SneakOnly", (T)false));
        this.noEntityCollision = (Setting<Boolean>)this.register(new Setting("No Collision", (T)true));
        this.silentSwitch = (Setting<Boolean>)this.register(new Setting("Silent Switch", (T)true));
        this.minDamage = (Setting<Integer>)this.register(new Setting("Min Damage", (T)100, (T)1, (T)100));
        this.maxHeal = (Setting<Integer>)this.register(new Setting("Repair To", (T)90, (T)1, (T)100));
        this.predict = (Setting<Boolean>)this.register(new Setting("Predict", (T)false));
        this.DisableWhenDone = (Setting<Boolean>)this.register(new Setting("AutoDisable", (T)true));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (T)true));
    }
    
    public void onUpdate() {
        if (PacketMend.mc.player == null || PacketMend.mc.world == null) {
            return;
        }
        int sumOfDamage = 0;
        final List<ItemStack> armour = (List<ItemStack>)PacketMend.mc.player.inventory.armorInventory;
        for (int i = 0; i < armour.size(); ++i) {
            final ItemStack itemStack = armour.get(i);
            if (!itemStack.isEmpty) {
                final float damageOnArmor = (float)(itemStack.getMaxDamage() - itemStack.getItemDamage());
                final float damagePercent = 100.0f - 100.0f * (1.0f - damageOnArmor / itemStack.getMaxDamage());
                if (damagePercent < this.maxHeal.getValue() && this.DisableWhenDone.getValue()) {
                    this.toggle();
                }
                if (damagePercent <= this.maxHeal.getValue()) {
                    if (damagePercent <= this.minDamage.getValue()) {
                        this.toMend |= (char)(1 << i);
                    }
                    if (this.predict.getValue()) {
                        sumOfDamage += (int)(itemStack.getMaxDamage() * this.maxHeal.getValue() / 100.0f - (itemStack.getMaxDamage() - itemStack.getItemDamage()));
                    }
                }
                else {
                    this.toMend &= (char)~(1 << i);
                }
            }
        }
        if (this.toMend > '\0') {
            if (this.predict.getValue()) {
                final int totalXp = PacketMend.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityXPOrb).filter(entity -> entity.getDistanceSq((Entity)PacketMend.mc.player) <= 1.0).mapToInt(entity -> entity.xpValue).sum();
                if (totalXp * 2 < sumOfDamage) {
                    this.mendArmor(PacketMend.mc.player.inventory.currentItem);
                }
            }
            else {
                this.mendArmor(PacketMend.mc.player.inventory.currentItem);
            }
        }
    }
    
    private void mendArmor(final int oldSlot) {
        if (this.noEntityCollision.getValue()) {
            for (final EntityPlayer entityPlayer : PacketMend.mc.world.playerEntities) {
                if (entityPlayer.getDistance((Entity)PacketMend.mc.player) < 1.0f && entityPlayer != PacketMend.mc.player) {
                    return;
                }
            }
        }
        if (this.sneakOnly.getValue() && !PacketMend.mc.player.isSneaking()) {
            return;
        }
        final int newSlot = this.findXPSlot();
        if (newSlot == -1) {
            return;
        }
        if (oldSlot != newSlot) {
            if (this.silentSwitch.getValue()) {
                PacketMend.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(newSlot));
            }
            else {
                PacketMend.mc.player.inventory.currentItem = newSlot;
            }
            PacketMend.mc.playerController.syncCurrentPlayItem();
        }
        if (this.rotate.getValue()) {
            PacketMend.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(0.0f, 90.0f, true));
        }
        PacketMend.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        if (this.silentSwitch.getValue()) {
            PacketMend.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(oldSlot));
        }
        else {
            PacketMend.mc.player.inventory.currentItem = oldSlot;
        }
        PacketMend.mc.playerController.syncCurrentPlayItem();
    }
    
    private int findXPSlot() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            if (PacketMend.mc.player.inventory.getStackInSlot(i).getItem() == Items.EXPERIENCE_BOTTLE) {
                slot = i;
                break;
            }
        }
        return slot;
    }
}
