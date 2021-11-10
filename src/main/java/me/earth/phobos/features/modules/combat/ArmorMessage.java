



package me.earth.phobos.features.modules.combat;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.entity.player.*;
import me.earth.phobos.event.events.*;
import me.earth.phobos.*;
import net.minecraft.item.*;
import me.earth.phobos.util.*;
import me.earth.phobos.features.command.*;
import java.util.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.init.*;

public class ArmorMessage extends Module
{
    private final Setting<Integer> armorThreshhold;
    private final Setting<Boolean> notifySelf;
    private final Setting<Boolean> notification;
    private final Map<EntityPlayer, Integer> entityArmorArraylist;
    
    public ArmorMessage() {
        super("ArmorMessage", "Message friends/urself when their armor is low.", Category.COMBAT, true, false, false);
        this.armorThreshhold = (Setting<Integer>)this.register(new Setting("Armor%", (T)20, (T)1, (T)100));
        this.notifySelf = (Setting<Boolean>)this.register(new Setting("NotifySelf", (T)true));
        this.notification = (Setting<Boolean>)this.register(new Setting("Notification", (T)true));
        this.entityArmorArraylist = new HashMap<EntityPlayer, Integer>();
    }
    
    @SubscribeEvent
    public void onUpdate(final UpdateWalkingPlayerEvent event) {
        for (final EntityPlayer player : ArmorMessage.mc.world.playerEntities) {
            if (!player.isDead) {
                if (!Phobos.friendManager.isFriend(player.getName())) {
                    continue;
                }
                for (final ItemStack stack : player.inventory.armorInventory) {
                    if (stack == ItemStack.EMPTY) {
                        continue;
                    }
                    final int percent = DamageUtil.getRoundedDamage(stack);
                    if (percent <= this.armorThreshhold.getValue() && !this.entityArmorArraylist.containsKey(player)) {
                        if (player == ArmorMessage.mc.player && this.notifySelf.getValue()) {
                            Command.sendMessage(player.getName() + " watchout your " + this.getArmorPieceName(stack) + " low dura!", (boolean)this.notification.getValue());
                        }
                        else {
                            ArmorMessage.mc.player.sendChatMessage("/msg " + player.getName() + " " + player.getName() + " watchout your " + this.getArmorPieceName(stack) + " low dura!");
                        }
                        this.entityArmorArraylist.put(player, player.inventory.armorInventory.indexOf((Object)stack));
                    }
                    if (!this.entityArmorArraylist.containsKey(player) || this.entityArmorArraylist.get(player) != player.inventory.armorInventory.indexOf((Object)stack)) {
                        continue;
                    }
                    if (percent <= this.armorThreshhold.getValue()) {
                        continue;
                    }
                    this.entityArmorArraylist.remove(player);
                }
                if (!this.entityArmorArraylist.containsKey(player)) {
                    continue;
                }
                if (player.inventory.armorInventory.get((int)this.entityArmorArraylist.get(player)) != ItemStack.EMPTY) {
                    continue;
                }
                this.entityArmorArraylist.remove(player);
            }
        }
    }
    
    private String getArmorPieceName(final ItemStack stack) {
        if (stack.getItem() == Items.DIAMOND_HELMET || stack.getItem() == Items.GOLDEN_HELMET || stack.getItem() == Items.IRON_HELMET || stack.getItem() == Items.CHAINMAIL_HELMET || stack.getItem() == Items.LEATHER_HELMET) {
            return "helmet is";
        }
        if (stack.getItem() == Items.DIAMOND_CHESTPLATE || stack.getItem() == Items.GOLDEN_CHESTPLATE || stack.getItem() == Items.IRON_CHESTPLATE || stack.getItem() == Items.CHAINMAIL_CHESTPLATE || stack.getItem() == Items.LEATHER_CHESTPLATE) {
            return "chestplate is";
        }
        if (stack.getItem() == Items.DIAMOND_LEGGINGS || stack.getItem() == Items.GOLDEN_LEGGINGS || stack.getItem() == Items.IRON_LEGGINGS || stack.getItem() == Items.CHAINMAIL_LEGGINGS || stack.getItem() == Items.LEATHER_LEGGINGS) {
            return "leggings are";
        }
        return "boots are";
    }
}
