//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.manager;

import me.earth.phobos.features.*;
import net.minecraft.entity.player.*;
import me.earth.phobos.features.modules.client.*;
import java.util.concurrent.*;
import me.earth.phobos.features.command.*;
import java.util.*;
import me.earth.phobos.*;
import java.util.function.*;

public class TotemPopManager extends Feature
{
    private final Set<EntityPlayer> toAnnounce;
    private Notifications notifications;
    private Map<EntityPlayer, Integer> poplist;
    
    public TotemPopManager() {
        this.toAnnounce = new HashSet<EntityPlayer>();
        this.poplist = new ConcurrentHashMap<EntityPlayer, Integer>();
    }
    
    public void onUpdate() {
        if (this.notifications.totemAnnounce.passedMs((int)this.notifications.delay.getValue()) && this.notifications.isOn() && (boolean)this.notifications.totemPops.getValue()) {
            for (final EntityPlayer player : this.toAnnounce) {
                if (player == null) {
                    continue;
                }
                int playerNumber = 0;
                for (final char character : player.getName().toCharArray()) {
                    playerNumber += character;
                    playerNumber *= 10;
                }
                Command.sendOverwriteMessage("§c" + player.getName() + " popped §a" + this.getTotemPops(player) + "§c Totem" + ((this.getTotemPops(player) == 1) ? "" : "s") + ".", playerNumber, (boolean)this.notifications.totemNoti.getValue());
                this.toAnnounce.remove(player);
                this.notifications.totemAnnounce.reset();
                break;
            }
        }
    }
    
    public void onLogout() {
        this.onOwnLogout((boolean)this.notifications.clearOnLogout.getValue());
    }
    
    public void init() {
        this.notifications = (Notifications)Phobos.moduleManager.getModuleByClass((Class)Notifications.class);
    }
    
    public void onTotemPop(final EntityPlayer player) {
        this.popTotem(player);
        if (!player.equals((Object)TotemPopManager.mc.player)) {
            this.toAnnounce.add(player);
            this.notifications.totemAnnounce.reset();
        }
    }
    
    public void onDeath(final EntityPlayer player) {
        if (this.getTotemPops(player) != 0 && !player.equals((Object)TotemPopManager.mc.player) && this.notifications.isOn() && (boolean)this.notifications.totemPops.getValue()) {
            int playerNumber = 0;
            for (final char character : player.getName().toCharArray()) {
                playerNumber += character;
                playerNumber *= 10;
            }
            Command.sendOverwriteMessage("§c" + player.getName() + " died after popping §a" + this.getTotemPops(player) + "§c Totem" + ((this.getTotemPops(player) == 1) ? "" : "s") + ".", playerNumber, (boolean)this.notifications.totemNoti.getValue());
            this.toAnnounce.remove(player);
        }
        this.resetPops(player);
    }
    
    public void onLogout(final EntityPlayer player, final boolean clearOnLogout) {
        if (clearOnLogout) {
            this.resetPops(player);
        }
    }
    
    public void onOwnLogout(final boolean clearOnLogout) {
        if (clearOnLogout) {
            this.clearList();
        }
    }
    
    public void clearList() {
        this.poplist = new ConcurrentHashMap<EntityPlayer, Integer>();
    }
    
    public void resetPops(final EntityPlayer player) {
        this.setTotemPops(player, 0);
    }
    
    public void popTotem(final EntityPlayer player) {
        this.poplist.merge(player, 1, Integer::sum);
    }
    
    public void setTotemPops(final EntityPlayer player, final int amount) {
        this.poplist.put(player, amount);
    }
    
    public int getTotemPops(final EntityPlayer player) {
        final Integer pops = this.poplist.get(player);
        if (pops == null) {
            return 0;
        }
        return pops;
    }
    
    public String getTotemPopString(final EntityPlayer player) {
        return "§f" + ((this.getTotemPops(player) <= 0) ? "" : ("-" + this.getTotemPops(player) + " "));
    }
}
