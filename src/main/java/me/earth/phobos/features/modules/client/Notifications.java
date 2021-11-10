



package me.earth.phobos.features.modules.client;

import me.earth.phobos.features.modules.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import me.earth.phobos.features.setting.*;
import java.awt.*;
import me.earth.phobos.features.command.*;
import net.minecraft.network.play.server.*;
import com.mojang.realmsclient.gui.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import me.earth.phobos.util.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import java.text.*;
import me.earth.phobos.*;
import net.minecraft.entity.item.*;
import net.minecraft.potion.*;
import java.util.*;
import java.util.stream.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.init.*;
import me.earth.phobos.manager.*;
import me.earth.phobos.event.events.*;
import net.minecraft.util.text.*;

public class Notifications extends Module
{
    private static final String fileName = "phobos/util/ModuleMessage_List.txt";
    private static final List<String> modules;
    private static Notifications INSTANCE;
    private final TimerUtil timer;
    private final List<EntityPlayer> burrowedPlayers;
    private final Set<EntityPlayer> sword;
    private final TimerUtil weak;
    private final TimerUtil strgh;
    private final TimerUtil voids;
    private final Set<EntityPlayer> str;
    private final Set<Entity> ghasts;
    private final Set<Entity> donkeys;
    private final Set<Entity> mules;
    private final Set<Entity> llamas;
    public Setting<Boolean> totemPops;
    public Setting<Boolean> totemNoti;
    public Setting<Integer> delay;
    public Setting<Boolean> clearOnLogout;
    public Setting<Boolean> moduleMessage;
    private final Setting<Boolean> bold;
    private final Setting<Boolean> readfile;
    public Setting<Boolean> list;
    public Setting<Boolean> watermark;
    public Setting<Boolean> visualRange;
    public Setting<Boolean> VisualRangeSound;
    public Setting<Boolean> coords;
    public Setting<Boolean> leaving;
    public Setting<Boolean> pearls;
    public Setting<Boolean> crash;
    public Setting<Boolean> popUp;
    public Setting<Boolean> burrow;
    public Setting<Boolean> strength;
    public Setting<Integer> strcheckrate;
    public Setting<Boolean> weakness;
    public Setting<Boolean> strongness;
    public Setting<Integer> checkrate;
    public Setting<Boolean> thirtytwokay;
    public Setting<Boolean> voider;
    public Setting<Integer> voidcheckrate;
    public Setting<Boolean> tp;
    public Setting<Boolean> rb;
    public Setting<Boolean> entities;
    public Setting<Boolean> Desktop;
    public Setting<Boolean> Ghasts;
    public Setting<Boolean> Donkeys;
    public Setting<Boolean> Mules;
    public Setting<Boolean> Llamas;
    public Setting<Boolean> Chat;
    public Setting<Boolean> Sound;
    public TimerUtil totemAnnounce;
    Image image;
    private final TrayIcon icon;
    private boolean flag;
    private List<EntityPlayer> knownPlayers;
    private boolean check;
    private boolean last;
    
    public Notifications() {
        super("Notifications",  "Sends Messages.",  Category.CLIENT,  true,  false,  false);
        this.timer = new TimerUtil();
        this.burrowedPlayers = new ArrayList<EntityPlayer>();
        this.sword = Collections.newSetFromMap(new WeakHashMap<EntityPlayer,  Boolean>());
        this.weak = new TimerUtil();
        this.strgh = new TimerUtil();
        this.voids = new TimerUtil();
        this.str = Collections.newSetFromMap(new WeakHashMap<EntityPlayer,  Boolean>());
        this.ghasts = new HashSet<Entity>();
        this.donkeys = new HashSet<Entity>();
        this.mules = new HashSet<Entity>();
        this.llamas = new HashSet<Entity>();
        this.totemPops = (Setting<Boolean>)this.register(new Setting("TotemPops", true));
        this.totemNoti = (Setting<Boolean>)this.register(new Setting("TotemNoti", false,  v -> this.totemPops.getValue()));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", 0, 0, 5000,  v -> this.totemPops.getValue(),  "Delays messages."));
        this.clearOnLogout = (Setting<Boolean>)this.register(new Setting("LogoutClear", false));
        this.moduleMessage = (Setting<Boolean>)this.register(new Setting("ModuleMessage", true));
        this.bold = (Setting<Boolean>)this.register(new Setting("BoldMsgs", false,  v -> this.moduleMessage.getValue()));
        this.readfile = (Setting<Boolean>)this.register(new Setting("LoadFile", false,  v -> this.moduleMessage.getValue()));
        this.list = (Setting<Boolean>)this.register(new Setting("List", false,  v -> this.moduleMessage.getValue()));
        this.watermark = (Setting<Boolean>)this.register(new Setting("Watermark", true,  v -> this.moduleMessage.getValue()));
        this.visualRange = (Setting<Boolean>)this.register(new Setting("VisualRange", false));
        this.VisualRangeSound = (Setting<Boolean>)this.register(new Setting("VisualRangeSound", false));
        this.coords = (Setting<Boolean>)this.register(new Setting("Coords", true,  v -> this.visualRange.getValue()));
        this.leaving = (Setting<Boolean>)this.register(new Setting("Leaving", true,  v -> this.visualRange.getValue()));
        this.pearls = (Setting<Boolean>)this.register(new Setting("PearlNotifs", true));
        this.crash = (Setting<Boolean>)this.register(new Setting("Crash", true));
        this.popUp = (Setting<Boolean>)this.register(new Setting("PopUpVisualRange", false));
        this.burrow = (Setting<Boolean>)this.register(new Setting("Burrow", false));
        this.strength = (Setting<Boolean>)this.register(new Setting("Strength", false));
        this.strcheckrate = (Setting<Integer>)this.register(new Setting("CheckRate", 500, 0, 5000,  v -> this.strength.getValue()));
        this.weakness = (Setting<Boolean>)this.register(new Setting("Weakness", false));
        this.strongness = (Setting<Boolean>)this.register(new Setting("Strongness", false,  v -> this.weakness.getValue()));
        this.checkrate = (Setting<Integer>)this.register(new Setting("CheckRate", 500, 0, 5000,  v -> this.weakness.getValue()));
        this.thirtytwokay = (Setting<Boolean>)this.register(new Setting("32k", false));
        this.voider = (Setting<Boolean>)this.register(new Setting("Void", false));
        this.voidcheckrate = (Setting<Integer>)this.register(new Setting("CheckRate", 500, 0, 5000,  v -> this.voider.getValue()));
        this.tp = (Setting<Boolean>)this.register(new Setting("Chorus", false));
        this.rb = (Setting<Boolean>)this.register(new Setting("Rubberband", false));
        this.entities = (Setting<Boolean>)this.register(new Setting("Entities", false));
        this.Desktop = (Setting<Boolean>)this.register(new Setting("DesktopNotifs", false,  v -> this.entities.getValue()));
        this.Ghasts = (Setting<Boolean>)this.register(new Setting("Ghasts", false,  v -> this.entities.getValue()));
        this.Donkeys = (Setting<Boolean>)this.register(new Setting("Donkeys", false,  v -> this.entities.getValue()));
        this.Mules = (Setting<Boolean>)this.register(new Setting("Mules", false,  v -> this.entities.getValue()));
        this.Llamas = (Setting<Boolean>)this.register(new Setting("Llamas", false,  v -> this.entities.getValue()));
        this.Chat = (Setting<Boolean>)this.register(new Setting("Chat", false,  v -> this.entities.getValue()));
        this.Sound = (Setting<Boolean>)this.register(new Setting("Sound", false,  v -> this.entities.getValue()));
        this.totemAnnounce = new TimerUtil();
        this.image = Toolkit.getDefaultToolkit().getImage("phobos.png");
        this.icon = new TrayIcon(this.image,  "Perry Phobos");
        this.knownPlayers = new ArrayList<EntityPlayer>();
        this.setInstance();
        this.icon.setImageAutoSize(true);
        try {
            final SystemTray tray = SystemTray.getSystemTray();
            tray.add(this.icon);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    public static Notifications getInstance() {
        if (Notifications.INSTANCE == null) {
            Notifications.INSTANCE = new Notifications();
        }
        return Notifications.INSTANCE;
    }
    
    public static void displayCrash(final Exception e) {
        Command.sendMessage("§cException caught: " + e.getMessage());
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook && this.rb.getValue()) {
            Command.sendMessage(ChatFormatting.RED + "Rubberband Detected!");
        }
    }
    
    private void setInstance() {
        Notifications.INSTANCE = this;
    }
    
    private boolean is32k(final ItemStack stack) {
        if (stack.getItem() instanceof ItemSword) {
            final NBTTagList enchants = stack.getEnchantmentTagList();
            for (int i = 0; i < enchants.tagCount(); ++i) {
                if (enchants.getCompoundTagAt(i).getShort("lvl") >= 32767) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public void onLoad() {
        this.check = true;
        this.loadFile();
        this.check = false;
    }
    
    @Override
    public void onEnable() {
        this.knownPlayers = new ArrayList<EntityPlayer>();
        if (!this.check) {
            this.loadFile();
            this.flag = true;
        }
        this.ghasts.clear();
        this.donkeys.clear();
        this.mules.clear();
        this.llamas.clear();
    }
    
    @SubscribeEvent
    public void onChorus(final ChorusEvent event) {
        if (this.tp.getValue()) {
            Command.sendMessage(ChatFormatting.DARK_PURPLE + "A player teleported " + MathUtil.getDirectionFromPlayer(event.getChorusX(),  event.getChorusZ()) + "!");
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.entities.getValue() && this.Ghasts.getValue()) {
            for (final Entity entity : Notifications.mc.world.getLoadedEntityList()) {
                if (entity instanceof EntityGhast) {
                    if (this.ghasts.contains(entity)) {
                        continue;
                    }
                    if (this.Chat.getValue()) {
                        Command.sendMessage("Ghast Detected at: " + Math.round((float)entity.getPosition().getX()) + ",  " + Math.round((float)entity.getPosition().getY()) + ",  " + Math.round((float)entity.getPosition().getZ()) + ".");
                    }
                    this.ghasts.add(entity);
                    if (this.Desktop.getValue()) {
                        this.icon.displayMessage("Perry Phobos",  "I found a ghast at: " + Math.round((float)entity.getPosition().getX()) + ",  " + Math.round((float)entity.getPosition().getY()) + ",  " + Math.round((float)entity.getPosition().getZ()) + ".",  TrayIcon.MessageType.INFO);
                    }
                    if (!this.Sound.getValue()) {
                        continue;
                    }
                    Notifications.mc.player.playSound(SoundEvents.BLOCK_ANVIL_DESTROY,  1.0f,  1.0f);
                }
            }
        }
        if (this.entities.getValue() && this.Donkeys.getValue()) {
            for (final Entity entity : Notifications.mc.world.getLoadedEntityList()) {
                if (entity instanceof EntityDonkey) {
                    if (this.donkeys.contains(entity)) {
                        continue;
                    }
                    if (this.Chat.getValue()) {
                        Command.sendMessage("Donkey Detected at: " + Math.round((float)entity.getPosition().getX()) + ",  " + Math.round((float)entity.getPosition().getY()) + ",  " + Math.round((float)entity.getPosition().getZ()) + ".");
                    }
                    this.donkeys.add(entity);
                    if (this.Desktop.getValue()) {
                        this.icon.displayMessage("Perry Phobos",  "I found a donkey at: " + Math.round((float)entity.getPosition().getX()) + ",  " + Math.round((float)entity.getPosition().getY()) + ",  " + Math.round((float)entity.getPosition().getZ()) + ".",  TrayIcon.MessageType.INFO);
                    }
                    if (!this.Sound.getValue()) {
                        continue;
                    }
                    Notifications.mc.player.playSound(SoundEvents.BLOCK_ANVIL_DESTROY,  1.0f,  1.0f);
                }
            }
        }
        if (this.entities.getValue() && this.Mules.getValue()) {
            for (final Entity entity : Notifications.mc.world.getLoadedEntityList()) {
                if (entity instanceof EntityMule) {
                    if (this.mules.contains(entity)) {
                        continue;
                    }
                    if (this.Chat.getValue()) {
                        Command.sendMessage("Mule Detected at: " + Math.round((float)entity.getPosition().getX()) + ",  " + Math.round((float)entity.getPosition().getY()) + ",  " + Math.round((float)entity.getPosition().getZ()) + ".");
                    }
                    this.mules.add(entity);
                    if (this.Desktop.getValue()) {
                        this.icon.displayMessage("Perry Phobos",  "I found a mule at: " + Math.round((float)entity.getPosition().getX()) + ",  " + Math.round((float)entity.getPosition().getY()) + ",  " + Math.round((float)entity.getPosition().getZ()) + ".",  TrayIcon.MessageType.INFO);
                    }
                    if (!this.Sound.getValue()) {
                        continue;
                    }
                    Notifications.mc.player.playSound(SoundEvents.BLOCK_ANVIL_DESTROY,  1.0f,  1.0f);
                }
            }
        }
        if (this.entities.getValue() && this.Llamas.getValue()) {
            for (final Entity entity : Notifications.mc.world.getLoadedEntityList()) {
                if (entity instanceof EntityLlama) {
                    if (this.llamas.contains(entity)) {
                        continue;
                    }
                    if (this.Chat.getValue()) {
                        Command.sendMessage("Llama Detected at: " + Math.round((float)entity.getPosition().getX()) + ",  " + Math.round((float)entity.getPosition().getY()) + ",  " + Math.round((float)entity.getPosition().getZ()) + ".");
                    }
                    this.llamas.add(entity);
                    if (this.Desktop.getValue()) {
                        this.icon.displayMessage("Perry Phobos",  "I found a llama at: " + Math.round((float)entity.getPosition().getX()) + ",  " + Math.round((float)entity.getPosition().getY()) + ",  " + Math.round((float)entity.getPosition().getZ()) + ".",  TrayIcon.MessageType.INFO);
                    }
                    if (!this.Sound.getValue()) {
                        continue;
                    }
                    Notifications.mc.player.playSound(SoundEvents.BLOCK_ANVIL_DESTROY,  1.0f,  1.0f);
                }
            }
        }
        if (this.readfile.getValue()) {
            if (!this.check) {
                Command.sendMessage("Loading File...");
                this.timer.reset();
                this.loadFile();
            }
            this.check = true;
        }
        if (this.check && this.timer.passedMs(750L)) {
            this.readfile.setValue(false);
            this.check = false;
        }
        if (this.voider.getValue()) {
            if (!this.voids.passedMs(this.voidcheckrate.getValue())) {
                return;
            }
            for (final Entity entity : Notifications.mc.world.loadedEntityList) {
                if (entity instanceof EntityPlayer && entity.posY < 0.0 && !((EntityPlayer)entity).isSpectator()) {
                    final DecimalFormat format = new DecimalFormat("#");
                    Command.sendMessage(ChatFormatting.RED + entity.getName() + ChatFormatting.RESET + " Is in the void at Y " + format.format(entity.posY),  false);
                }
            }
            this.voids.reset();
        }
        if (this.visualRange.getValue()) {
            final ArrayList<EntityPlayer> tickPlayerList = new ArrayList<EntityPlayer>(Notifications.mc.world.playerEntities);
            if (tickPlayerList.size() > 0) {
                for (final EntityPlayer player : tickPlayerList) {
                    if (!player.getName().equals(Notifications.mc.player.getName())) {
                        if (this.knownPlayers.contains(player)) {
                            continue;
                        }
                        this.knownPlayers.add(player);
                        if (Phobos.friendManager.isFriend(player)) {
                            Command.sendMessage("Player §a" + player.getName() + "§r entered your visual range" + (this.coords.getValue() ? (" at (" + (int)player.posX + ",  " + (int)player.posY + ",  " + (int)player.posZ + ")!") : "!"),  (boolean)this.popUp.getValue());
                        }
                        else {
                            Command.sendMessage("Player §c" + player.getName() + "§r entered your visual range" + (this.coords.getValue() ? (" at (" + (int)player.posX + ",  " + (int)player.posY + ",  " + (int)player.posZ + ")!") : "!"),  (boolean)this.popUp.getValue());
                        }
                        if (this.VisualRangeSound.getValue()) {
                            Notifications.mc.player.playSound(SoundEvents.BLOCK_ANVIL_LAND,  1.0f,  1.0f);
                        }
                        return;
                    }
                }
            }
            if (this.knownPlayers.size() > 0) {
                for (final EntityPlayer player : this.knownPlayers) {
                    if (tickPlayerList.contains(player)) {
                        continue;
                    }
                    this.knownPlayers.remove(player);
                    if (this.leaving.getValue()) {
                        if (Phobos.friendManager.isFriend(player)) {
                            Command.sendMessage("Player §a" + player.getName() + "§r left your visual range" + (this.coords.getValue() ? (" at (" + (int)player.posX + ",  " + (int)player.posY + ",  " + (int)player.posZ + ")!") : "!"),  (boolean)this.popUp.getValue());
                        }
                        else {
                            Command.sendMessage("Player §c" + player.getName() + "§r left your visual range" + (this.coords.getValue() ? (" at (" + (int)player.posX + ",  " + (int)player.posY + ",  " + (int)player.posZ + ")!") : "!"),  (boolean)this.popUp.getValue());
                        }
                    }
                    return;
                }
            }
        }
        if (this.pearls.getValue()) {
            if (Notifications.mc.world == null || Notifications.mc.player == null) {
                return;
            }
            Entity enderPearl = null;
            for (final Entity e : Notifications.mc.world.loadedEntityList) {
                if (e instanceof EntityEnderPearl) {
                    enderPearl = e;
                    break;
                }
            }
            if (enderPearl == null) {
                this.flag = true;
                return;
            }
            EntityPlayer closestPlayer = null;
            for (final EntityPlayer entity2 : Notifications.mc.world.playerEntities) {
                if (closestPlayer == null) {
                    closestPlayer = entity2;
                }
                else {
                    if (closestPlayer.getDistance(enderPearl) <= entity2.getDistance(enderPearl)) {
                        continue;
                    }
                    closestPlayer = entity2;
                }
            }
            if (closestPlayer == Notifications.mc.player) {
                this.flag = false;
            }
            if (closestPlayer != null && this.flag) {
                String facing = enderPearl.getHorizontalFacing().toString();
                if (facing.equals("west")) {
                    facing = "east";
                }
                else if (facing.equals("east")) {
                    facing = "west";
                }
                Command.sendSilentMessage(Phobos.friendManager.isFriend(closestPlayer.getName()) ? (ChatFormatting.AQUA + closestPlayer.getName() + ChatFormatting.DARK_GRAY + " has just thrown a pearl heading " + facing + "!") : (ChatFormatting.RED + closestPlayer.getName() + ChatFormatting.DARK_GRAY + " has just thrown a pearl heading " + facing + "!"));
                this.flag = false;
            }
        }
        if (this.weakness.getValue()) {
            if (!this.weak.passedMs(this.checkrate.getValue())) {
                return;
            }
            if (Notifications.mc.player.isPotionActive((Potion)Objects.requireNonNull(Potion.getPotionFromResourceLocation("weakness"))) && !this.last) {
                Command.sendMessage(ChatFormatting.RED + "You have been weaknessed.");
                this.last = true;
            }
            if (!Notifications.mc.player.isPotionActive((Potion)Objects.requireNonNull(Potion.getPotionFromResourceLocation("weakness"))) && this.last) {
                if (this.strongness.getValue()) {
                    Command.sendMessage(ChatFormatting.GREEN + "You no longer have weakness.");
                }
                this.last = false;
            }
            this.weak.reset();
        }
        if (this.strength.getValue()) {
            for (final EntityPlayer entityPlayer : Notifications.mc.world.playerEntities) {
                if (!this.strgh.passedMs(this.strcheckrate.getValue())) {
                    return;
                }
                if (entityPlayer.equals((Object)Notifications.mc.player)) {
                    continue;
                }
                if (entityPlayer.isPotionActive(MobEffects.STRENGTH) && !this.str.contains(entityPlayer)) {
                    Command.sendMessage(entityPlayer.getDisplayNameString() + ChatFormatting.RED + " has strength");
                    this.str.add(entityPlayer);
                }
                if (!this.str.contains(entityPlayer)) {
                    continue;
                }
                if (entityPlayer.isPotionActive(MobEffects.STRENGTH)) {
                    continue;
                }
                Command.sendMessage(entityPlayer.getDisplayNameString() + ChatFormatting.GREEN + " no longer has strength");
                this.str.remove(entityPlayer);
            }
        }
    }
    
    @Override
    public void onTick() {
        if (fullNullCheck()) {
            return;
        }
        if (this.burrow.getValue()) {
            for (final EntityPlayer entityPlayer2 : (List)Notifications.mc.world.playerEntities.stream().filter(entityPlayer -> entityPlayer != Notifications.mc.player).collect(Collectors.toList())) {
                if (!this.burrowedPlayers.contains(entityPlayer2) && this.isInBurrow(entityPlayer2)) {
                    Command.sendMessage(ChatFormatting.RED + entityPlayer2.getDisplayNameString() + ChatFormatting.GREEN + " has burrowed.");
                    this.burrowedPlayers.add(entityPlayer2);
                }
            }
        }
        if (this.thirtytwokay.getValue()) {
            int once = 0;
            for (final EntityPlayer player : Notifications.mc.world.playerEntities) {
                if (player.equals((Object)Notifications.mc.player)) {
                    continue;
                }
                if (this.is32k(player.getHeldItem(EnumHand.MAIN_HAND)) && !this.sword.contains(player)) {
                    Command.sendMessage(ChatFormatting.RED + player.getDisplayNameString() + " is holding a 32k");
                    this.sword.add(player);
                }
                if (this.is32k(player.getHeldItem(EnumHand.MAIN_HAND))) {
                    if (once > 0) {
                        return;
                    }
                    ++once;
                }
                if (!this.sword.contains(player)) {
                    continue;
                }
                if (this.is32k(player.getHeldItem(EnumHand.MAIN_HAND))) {
                    continue;
                }
                Command.sendMessage(ChatFormatting.GREEN + player.getDisplayNameString() + " is no longer holding a 32k");
                this.sword.remove(player);
            }
        }
    }
    
    private boolean isInBurrow(final EntityPlayer entityPlayer) {
        final BlockPos playerPos = new BlockPos(this.getMiddlePosition(entityPlayer.posX),  entityPlayer.posY,  this.getMiddlePosition(entityPlayer.posZ));
        return Notifications.mc.world.getBlockState(playerPos).getBlock() == Blocks.OBSIDIAN || Notifications.mc.world.getBlockState(playerPos).getBlock() == Blocks.ENDER_CHEST || Notifications.mc.world.getBlockState(playerPos).getBlock() == Blocks.ANVIL;
    }
    
    private double getMiddlePosition(final double positionIn) {
        double positionFinal = (double)Math.round(positionIn);
        if (Math.round(positionIn) > positionIn) {
            positionFinal -= 0.5;
        }
        else if (Math.round(positionIn) <= positionIn) {
            positionFinal += 0.5;
        }
        return positionFinal;
    }
    
    public void loadFile() {
        final List<String> fileInput = FileManager.readTextFileAllLines("phobos/util/ModuleMessage_List.txt");
        final Iterator<String> i = fileInput.iterator();
        Notifications.modules.clear();
        while (i.hasNext()) {
            final String s = i.next();
            if (s.replaceAll("\\s",  "").isEmpty()) {
                continue;
            }
            Notifications.modules.add(s);
        }
    }
    
    @SubscribeEvent
    public void onToggleModule(final ClientEvent event) {
        if (!this.moduleMessage.getValue()) {
            return;
        }
        Module module;
        if (event.getStage() == 0 && !(module = (Module)event.getFeature()).equals(this) && (Notifications.modules.contains(module.getDisplayName()) || !this.list.getValue())) {
            int moduleNumber = 0;
            for (final char character : module.getDisplayName().toCharArray()) {
                moduleNumber += character;
                moduleNumber *= 10;
            }
            if (this.watermark.getValue()) {
                final TextComponentString textComponentString = new TextComponentString(Phobos.commandManager.getClientMessage() + " §r§c" + module.getDisplayName() + " disabled.");
                Notifications.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)textComponentString,  moduleNumber);
                if (this.bold.getValue()) {
                    final TextComponentString textComponentString2 = new TextComponentString(Phobos.commandManager.getClientMessage() + " " + ChatFormatting.BOLD + module.getDisplayName() + ChatFormatting.RED + " disabled.");
                    Notifications.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)textComponentString2,  moduleNumber);
                }
            }
            else {
                final TextComponentString textComponentString = new TextComponentString("§c" + module.getDisplayName() + " disabled.");
                Notifications.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)textComponentString,  moduleNumber);
                if (this.bold.getValue()) {
                    final TextComponentString textComponentString2 = new TextComponentString(ChatFormatting.BOLD + module.getDisplayName() + ChatFormatting.RED + " disabled.");
                    Notifications.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)textComponentString2,  moduleNumber);
                }
            }
        }
        if (event.getStage() == 1 && (Notifications.modules.contains((module = (Module)event.getFeature()).getDisplayName()) || !this.list.getValue())) {
            int moduleNumber = 0;
            for (final char character : module.getDisplayName().toCharArray()) {
                moduleNumber += character;
                moduleNumber *= 10;
            }
            if (this.watermark.getValue()) {
                final TextComponentString textComponentString = new TextComponentString(Phobos.commandManager.getClientMessage() + " §r§a" + module.getDisplayName() + " enabled.");
                Notifications.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)textComponentString,  moduleNumber);
                if (this.bold.getValue()) {
                    final TextComponentString textComponentString2 = new TextComponentString(Phobos.commandManager.getClientMessage() + " " + ChatFormatting.BOLD + module.getDisplayName() + ChatFormatting.GREEN + " enabled.");
                    Notifications.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)textComponentString2,  moduleNumber);
                }
            }
            else {
                final TextComponentString textComponentString = new TextComponentString("§a" + module.getDisplayName() + " enabled.");
                Notifications.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)textComponentString,  moduleNumber);
                if (this.bold.getValue()) {
                    final TextComponentString textComponentString2 = new TextComponentString(ChatFormatting.BOLD + module.getDisplayName() + ChatFormatting.GREEN + " enabled.");
                    Notifications.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)textComponentString2,  moduleNumber);
                }
            }
        }
    }
    
    static {
        modules = new ArrayList<String>();
        Notifications.INSTANCE = new Notifications();
    }
}
