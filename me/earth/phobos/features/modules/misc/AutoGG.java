//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.entity.player.*;
import java.util.concurrent.*;
import java.io.*;
import me.earth.phobos.features.command.*;
import me.earth.phobos.features.modules.combat.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.event.entity.player.*;
import me.earth.phobos.*;
import me.earth.phobos.event.events.*;
import net.minecraft.world.*;
import me.earth.phobos.manager.*;
import java.util.*;
import me.earth.phobos.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public class AutoGG extends Module
{
    private static final String path = "phobos/autogg.txt";
    private final Setting<Boolean> onOwnDeath;
    private final Setting<Boolean> greentext;
    private final Setting<Boolean> loadFiles;
    private final Setting<Integer> targetResetTimer;
    private final Setting<Integer> delay;
    private final Setting<Boolean> test;
    private final TimerUtil timer;
    private final TimerUtil cooldownTimer;
    public Map<EntityPlayer, Integer> targets;
    public List<String> messages;
    public EntityPlayer cauraTarget;
    private boolean cooldown;
    
    public AutoGG() {
        super("AutoGG", "Automatically GGs.", Category.MISC, true, false, false);
        this.onOwnDeath = (Setting<Boolean>)this.register(new Setting("OwnDeath", (T)false));
        this.greentext = (Setting<Boolean>)this.register(new Setting("Greentext", (T)false));
        this.loadFiles = (Setting<Boolean>)this.register(new Setting("LoadFiles", (T)false));
        this.targetResetTimer = (Setting<Integer>)this.register(new Setting("Reset", (T)30, (T)0, (T)90));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", (T)10, (T)0, (T)30));
        this.test = (Setting<Boolean>)this.register(new Setting("Test", (T)false));
        this.timer = new TimerUtil();
        this.cooldownTimer = new TimerUtil();
        this.targets = new ConcurrentHashMap<EntityPlayer, Integer>();
        this.messages = new ArrayList<String>();
        final File file = new File("phobos/autogg.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void onEnable() {
        this.loadMessages();
        this.timer.reset();
        this.cooldownTimer.reset();
    }
    
    @Override
    public void onTick() {
        if (this.loadFiles.getValue()) {
            this.loadMessages();
            Command.sendMessage("<AutoGG> Loaded messages.");
            this.loadFiles.setValue(false);
        }
        if (AutoCrystal.target != null && this.cauraTarget != AutoCrystal.target) {
            this.cauraTarget = AutoCrystal.target;
        }
        if (this.test.getValue()) {
            this.announceDeath((EntityPlayer)AutoGG.mc.player);
            this.test.setValue(false);
        }
        if (!this.cooldown) {
            this.cooldownTimer.reset();
        }
        if (this.cooldownTimer.passedS(this.delay.getValue()) && this.cooldown) {
            this.cooldown = false;
            this.cooldownTimer.reset();
        }
        if (AutoCrystal.target != null) {
            this.targets.put(AutoCrystal.target, (int)(this.timer.getPassedTimeMs() / 1000L));
        }
        this.targets.replaceAll((p, v) -> Integer.valueOf((int)(this.timer.getPassedTimeMs() / 1000L)));
        for (final EntityPlayer player : this.targets.keySet()) {
            if (this.targets.get(player) <= this.targetResetTimer.getValue()) {
                continue;
            }
            this.targets.remove(player);
            this.timer.reset();
        }
    }
    
    @SubscribeEvent
    public void onEntityDeath(final DeathEvent event) {
        if (this.targets.containsKey(event.player) && !this.cooldown) {
            this.announceDeath(event.player);
            this.cooldown = true;
            this.targets.remove(event.player);
        }
        if (event.player == this.cauraTarget && !this.cooldown) {
            this.announceDeath(event.player);
            this.cooldown = true;
        }
        if (event.player == AutoGG.mc.player && this.onOwnDeath.getValue()) {
            this.announceDeath(event.player);
            this.cooldown = true;
        }
    }
    
    @SubscribeEvent
    public void onAttackEntity(final AttackEntityEvent event) {
        if (event.getTarget() instanceof EntityPlayer && !Phobos.friendManager.isFriend(event.getEntityPlayer())) {
            this.targets.put((EntityPlayer)event.getTarget(), 0);
        }
    }
    
    @SubscribeEvent
    public void onSendAttackPacket(final PacketEvent.Send event) {
        final CPacketUseEntity packet;
        if (event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && packet.getEntityFromWorld((World)AutoGG.mc.world) instanceof EntityPlayer && !Phobos.friendManager.isFriend(Objects.requireNonNull((EntityPlayer)packet.getEntityFromWorld((World)AutoGG.mc.world)))) {
            this.targets.put((EntityPlayer)packet.getEntityFromWorld((World)AutoGG.mc.world), 0);
        }
    }
    
    public void loadMessages() {
        this.messages = FileManager.readTextFileAllLines("phobos/autogg.txt");
    }
    
    public String getRandomMessage() {
        this.loadMessages();
        final Random rand = new Random();
        if (this.messages.size() == 0) {
            return "<player> is a noob hahaha perry's fobus on tope";
        }
        if (this.messages.size() == 1) {
            return this.messages.get(0);
        }
        return this.messages.get(MathUtil.clamp(rand.nextInt(this.messages.size()), 0, this.messages.size() - 1));
    }
    
    public void announceDeath(final EntityPlayer target) {
        AutoGG.mc.player.connection.sendPacket((Packet)new CPacketChatMessage((this.greentext.getValue() ? ">" : "") + this.getRandomMessage().replaceAll("<player>", target.getDisplayNameString())));
    }
}
