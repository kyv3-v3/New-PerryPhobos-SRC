/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.block.BlockWeb
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.server.SPacketSetSlot
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package me.earth.phobos.features.modules.combat;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import me.earth.phobos.Phobos;
import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.event.events.ProcessRightClickBlockEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.client.PingBypass;
import me.earth.phobos.features.modules.combat.AutoCrystal;
import me.earth.phobos.features.setting.Bind;
import me.earth.phobos.features.setting.EnumConverter;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.mixin.mixins.accessors.IContainer;
import me.earth.phobos.mixin.mixins.accessors.ISPacketSetSlot;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.InventoryUtil;
import me.earth.phobos.util.TimerUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockWeb;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Offhand
extends Module {
    private static Offhand instance;
    private final Queue<InventoryUtil.Task> taskList = new ConcurrentLinkedQueue<InventoryUtil.Task>();
    private final TimerUtil timer = new TimerUtil();
    private final TimerUtil secondTimer = new TimerUtil();
    private final TimerUtil serverTimer = new TimerUtil();
    public Setting<Type> type = this.register(new Setting<Type>("Mode", Type.NEW));
    public Setting<Boolean> cycle = this.register(new Setting<Object>("Cycle", Boolean.valueOf(false), v -> this.type.getValue() == Type.OLD));
    public Setting<Bind> cycleKey = this.register(new Setting<Object>("Key", new Bind(-1), v -> this.cycle.getValue() != false && this.type.getValue() == Type.OLD));
    public Setting<Bind> offHandGapple = this.register(new Setting<Bind>("Gapple", new Bind(-1)));
    public Setting<Float> gappleHealth = this.register(new Setting<Float>("G-Health", Float.valueOf(13.0f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Float> gappleHoleHealth = this.register(new Setting<Float>("G-H-Health", Float.valueOf(3.5f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Bind> offHandCrystal = this.register(new Setting<Bind>("Crystal", new Bind(-1)));
    public Setting<Float> crystalHealth = this.register(new Setting<Float>("C-Health", Float.valueOf(13.0f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Float> crystalHoleHealth = this.register(new Setting<Float>("C-H-Health", Float.valueOf(3.5f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Float> cTargetDistance = this.register(new Setting<Float>("C-Distance", Float.valueOf(10.0f), Float.valueOf(1.0f), Float.valueOf(20.0f)));
    public Setting<Bind> obsidian = this.register(new Setting<Bind>("Obsidian", new Bind(-1)));
    public Setting<Float> obsidianHealth = this.register(new Setting<Float>("O-Health", Float.valueOf(13.0f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Float> obsidianHoleHealth = this.register(new Setting<Float>("O-H-Health", Float.valueOf(8.0f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Bind> webBind = this.register(new Setting<Bind>("Webs", new Bind(-1)));
    public Setting<Float> webHealth = this.register(new Setting<Float>("W-Health", Float.valueOf(13.0f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Float> webHoleHealth = this.register(new Setting<Float>("W-H-Health", Float.valueOf(8.0f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Boolean> holeCheck = this.register(new Setting<Boolean>("Hole-Check", true));
    public Setting<Boolean> crystalCheck = this.register(new Setting<Boolean>("Crystal-Check", false));
    public Setting<Boolean> gapSwap = this.register(new Setting<Boolean>("Gap-Swap", true));
    public Setting<Integer> updates = this.register(new Setting<Integer>("Updates", 1, 1, 2));
    public Setting<Boolean> cycleObby = this.register(new Setting<Object>("CycleObby", Boolean.valueOf(false), v -> this.type.getValue() == Type.OLD));
    public Setting<Boolean> cycleWebs = this.register(new Setting<Object>("CycleWebs", Boolean.valueOf(false), v -> this.type.getValue() == Type.OLD));
    public Setting<Boolean> crystalToTotem = this.register(new Setting<Object>("Crystal-Totem", Boolean.valueOf(true), v -> this.type.getValue() == Type.OLD));
    public Setting<Boolean> absorption = this.register(new Setting<Object>("Absorption", Boolean.valueOf(false), v -> this.type.getValue() == Type.OLD));
    public Setting<Boolean> autoGapple = this.register(new Setting<Object>("AutoGapple", Boolean.valueOf(false), v -> this.type.getValue() == Type.OLD));
    public Setting<Boolean> onlyWTotem = this.register(new Setting<Object>("OnlyWTotem", Boolean.valueOf(true), v -> this.autoGapple.getValue() != false && this.type.getValue() == Type.OLD));
    public Setting<Boolean> unDrawTotem = this.register(new Setting<Object>("DrawTotems", Boolean.valueOf(true), v -> this.type.getValue() == Type.OLD));
    public Setting<Boolean> noOffhandGC = this.register(new Setting<Boolean>("NoOGC", false));
    public Setting<Boolean> retardOGC = this.register(new Setting<Boolean>("RetardOGC", false));
    public Setting<Boolean> returnToCrystal = this.register(new Setting<Boolean>("RecoverySwitch", false));
    public Setting<Integer> timeout = this.register(new Setting<Integer>("Timeout", 50, 0, 500));
    public Setting<Integer> timeout2 = this.register(new Setting<Integer>("Timeout2", 50, 0, 500));
    public Setting<Integer> actions = this.register(new Setting<Object>("Actions", Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(4), v -> this.type.getValue() == Type.OLD));
    public Setting<NameMode> displayNameChange = this.register(new Setting<Object>("Name", (Object)NameMode.TOTEM, v -> this.type.getValue() == Type.OLD));
    public Setting<Boolean> guis = this.register(new Setting<Boolean>("Guis", false));
    public Setting<Integer> serverTimeOut = this.register(new Setting<Integer>("S-Timeout", 1000, 0, 5000));
    public Setting<Boolean> bedcheck = this.register(new Setting<Boolean>("BedCheck", false));
    public Mode mode = Mode.CRYSTALS;
    public Mode oldMode = Mode.CRYSTALS;
    public Mode2 currentMode = Mode2.TOTEMS;
    public int totems;
    public int crystals;
    public int gapples;
    public int obby;
    public int webs;
    public int lastTotemSlot = -1;
    public int lastGappleSlot = -1;
    public int lastCrystalSlot = -1;
    public int lastObbySlot = -1;
    public int lastWebSlot = -1;
    public boolean holdingCrystal;
    public boolean holdingTotem;
    public boolean holdingGapple;
    public boolean holdingObby;
    public boolean holdingWeb;
    public boolean didSwitchThisTick;
    private int oldSlot = -1;
    private boolean swapToTotem;
    private boolean eatingApple;
    private boolean oldSwapToTotem;
    private boolean autoGappleSwitch;
    private boolean second;
    private boolean switchedForHealthReason;

    public Offhand() {
        super("Offhand", "Allows you to switch up your Offhand.", Module.Category.COMBAT, true, false, false);
        instance = this;
    }

    public static Offhand getInstance() {
        if (instance == null) {
            instance = new Offhand();
        }
        return instance;
    }

    public void onItemFinish(ItemStack stack, EntityLivingBase base) {
        if (this.noOffhandGC.getValue().booleanValue() && base.equals((Object)Offhand.mc.field_71439_g) && stack.func_77973_b() == Offhand.mc.field_71439_g.func_184592_cb().func_77973_b()) {
            this.secondTimer.reset();
            this.second = true;
        }
    }

    @Override
    public void onTick() {
        if (Offhand.nullCheck() || this.updates.getValue() == 1) {
            return;
        }
        this.doOffhand();
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(ProcessRightClickBlockEvent event) {
        if (this.noOffhandGC.getValue().booleanValue() && event.hand == EnumHand.MAIN_HAND && event.stack.func_77973_b() == Items.field_185158_cP && Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao && Offhand.mc.field_71476_x != null && event.pos == Offhand.mc.field_71476_x.func_178782_a()) {
            event.setCanceled(true);
            Offhand.mc.field_71439_g.func_184598_c(EnumHand.OFF_HAND);
            Offhand.mc.field_71442_b.func_187101_a((EntityPlayer)Offhand.mc.field_71439_g, (World)Offhand.mc.field_71441_e, EnumHand.OFF_HAND);
        }
    }

    @Override
    public void onUpdate() {
        if (this.noOffhandGC.getValue().booleanValue() && this.retardOGC.getValue().booleanValue()) {
            if (this.timer.passedMs(this.timeout.getValue().intValue())) {
                if (Offhand.mc.field_71439_g != null && Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao && Offhand.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP && Mouse.isButtonDown((int)1)) {
                    Offhand.mc.field_71439_g.func_184598_c(EnumHand.OFF_HAND);
                    Offhand.mc.field_71474_y.field_74313_G.field_74513_e = Mouse.isButtonDown((int)1);
                }
            } else if (Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao && Offhand.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP) {
                Offhand.mc.field_71474_y.field_74313_G.field_74513_e = false;
            }
        }
        if (Offhand.nullCheck() || this.updates.getValue() == 2) {
            return;
        }
        this.doOffhand();
        if (this.secondTimer.passedMs(this.timeout2.getValue().intValue()) && this.second) {
            this.second = false;
            this.timer.reset();
        }
    }

    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            if (this.type.getValue() == Type.NEW) {
                if (this.offHandCrystal.getValue().getKey() == Keyboard.getEventKey()) {
                    if (this.mode == Mode.CRYSTALS) {
                        this.setSwapToTotem(!this.isSwapToTotem());
                    } else {
                        this.setSwapToTotem(false);
                    }
                    this.setMode(Mode.CRYSTALS);
                }
                if (this.offHandGapple.getValue().getKey() == Keyboard.getEventKey()) {
                    if (this.mode == Mode.GAPPLES) {
                        this.setSwapToTotem(!this.isSwapToTotem());
                    } else {
                        this.setSwapToTotem(false);
                    }
                    this.setMode(Mode.GAPPLES);
                }
                if (this.obsidian.getValue().getKey() == Keyboard.getEventKey()) {
                    if (this.mode == Mode.OBSIDIAN) {
                        this.setSwapToTotem(!this.isSwapToTotem());
                    } else {
                        this.setSwapToTotem(false);
                    }
                    this.setMode(Mode.OBSIDIAN);
                }
                if (this.webBind.getValue().getKey() == Keyboard.getEventKey()) {
                    if (this.mode == Mode.WEBS) {
                        this.setSwapToTotem(!this.isSwapToTotem());
                    } else {
                        this.setSwapToTotem(false);
                    }
                    this.setMode(Mode.WEBS);
                }
            } else if (this.cycle.getValue().booleanValue()) {
                if (this.cycleKey.getValue().getKey() == Keyboard.getEventKey()) {
                    Mode2 newMode = (Mode2)EnumConverter.increaseEnum(this.currentMode);
                    if (newMode == Mode2.OBSIDIAN && !this.cycleObby.getValue().booleanValue() || newMode == Mode2.WEBS && !this.cycleWebs.getValue().booleanValue()) {
                        newMode = Mode2.TOTEMS;
                    }
                    this.setMode(newMode);
                }
            } else {
                if (this.offHandCrystal.getValue().getKey() == Keyboard.getEventKey()) {
                    this.setMode(Mode2.CRYSTALS);
                }
                if (this.offHandGapple.getValue().getKey() == Keyboard.getEventKey()) {
                    this.setMode(Mode2.GAPPLES);
                }
                if (this.obsidian.getValue().getKey() == Keyboard.getEventKey()) {
                    this.setMode(Mode2.OBSIDIAN);
                }
                if (this.webBind.getValue().getKey() == Keyboard.getEventKey()) {
                    this.setMode(Mode2.WEBS);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (this.noOffhandGC.getValue().booleanValue() && !Offhand.fullNullCheck() && Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao && Offhand.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP && Offhand.mc.field_71474_y.field_74313_G.func_151470_d()) {
            if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
                CPacketPlayerTryUseItemOnBlock packet2 = (CPacketPlayerTryUseItemOnBlock)event.getPacket();
                if (packet2.func_187022_c() == EnumHand.MAIN_HAND && !AutoCrystal.placedPos.contains((Object)packet2.func_187023_a())) {
                    if (this.timer.passedMs(this.timeout.getValue().intValue())) {
                        Offhand.mc.field_71439_g.func_184598_c(EnumHand.OFF_HAND);
                        Offhand.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItem(EnumHand.OFF_HAND));
                    }
                    event.setCanceled(true);
                }
            } else if (event.getPacket() instanceof CPacketPlayerTryUseItem && ((CPacketPlayerTryUseItem)event.getPacket()).func_187028_a() == EnumHand.OFF_HAND && !this.timer.passedMs(this.timeout.getValue().intValue())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        SPacketSetSlot packet;
        if (PingBypass.getInstance().isConnected() && event.getPacket() instanceof SPacketSetSlot && (packet = (SPacketSetSlot)event.getPacket()).func_149173_d() == -1 && packet.func_149175_c() != -1) {
            ((IContainer)Offhand.mc.field_71439_g.field_71070_bA).setTransactionID((short)packet.func_149175_c());
            ((ISPacketSetSlot)packet).setWindowId(-1);
            this.serverTimer.reset();
            this.switchedForHealthReason = true;
        }
    }

    @Override
    public String getDisplayInfo() {
        if (this.type.getValue() == Type.NEW) {
            return String.valueOf(this.getStackSize());
        }
        switch (this.displayNameChange.getValue()) {
            case MODE: {
                return EnumConverter.getProperName(this.currentMode);
            }
            case TOTEM: {
                if (this.currentMode == Mode2.TOTEMS) {
                    return this.totems + "";
                }
                return EnumConverter.getProperName(this.currentMode);
            }
        }
        switch (this.currentMode) {
            case TOTEMS: {
                return this.totems + "";
            }
            case GAPPLES: {
                return this.gapples + "";
            }
        }
        return this.crystals + "";
    }

    @Override
    public String getDisplayName() {
        if (this.type.getValue() == Type.NEW) {
            if (!this.shouldTotem()) {
                switch (this.mode) {
                    case GAPPLES: {
                        return "OffhandGapple";
                    }
                    case WEBS: {
                        return "OffhandWebs";
                    }
                    case OBSIDIAN: {
                        return "OffhandObby";
                    }
                }
                return "OffhandCrystal";
            }
            return "AutoTotem" + (!this.isSwapToTotem() ? "-" + this.getModeStr() : "");
        }
        switch (this.displayNameChange.getValue()) {
            case MODE: {
                return (String)this.displayName.getValue();
            }
            case TOTEM: {
                if (this.currentMode == Mode2.TOTEMS) {
                    return "AutoTotem";
                }
                return (String)this.displayName.getValue();
            }
        }
        switch (this.currentMode) {
            case TOTEMS: {
                return "AutoTotem";
            }
            case GAPPLES: {
                return "OffhandGapple";
            }
            case WEBS: {
                return "OffhandWebs";
            }
            case OBSIDIAN: {
                return "OffhandObby";
            }
        }
        return "OffhandCrystal";
    }

    public void doOffhand() {
        if (!this.serverTimer.passedMs(this.serverTimeOut.getValue().intValue())) {
            return;
        }
        if (this.type.getValue() == Type.NEW) {
            if (Offhand.mc.field_71462_r instanceof GuiContainer && !this.guis.getValue().booleanValue() && !(Offhand.mc.field_71462_r instanceof GuiInventory)) {
                return;
            }
            if (!(!this.gapSwap.getValue().booleanValue() || this.getSlot(Mode.GAPPLES) == -1 && Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() != Items.field_151153_ao || Offhand.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151153_ao || !Offhand.mc.field_71474_y.field_74313_G.func_151470_d() || this.getSlot(Mode.GAPPLES) == -1 && Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() != Items.field_151062_by || Offhand.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151062_by || !Offhand.mc.field_71474_y.field_74313_G.func_151470_d() || this.getSlot(Mode.GAPPLES) == -1 && Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() != Items.field_151046_w || Offhand.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151046_w || !Offhand.mc.field_71474_y.field_74313_G.func_151470_d() || this.getSlot(Mode.GAPPLES) == -1 && Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() != Items.field_185161_cS || Offhand.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185161_cS || !Offhand.mc.field_71474_y.field_74313_G.func_151470_d())) {
                if ((this.getSlot(Mode.GAPPLES) != -1 || Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151079_bi) && Offhand.mc.field_71439_g.func_184614_ca().func_77973_b() != Items.field_151079_bi && Offhand.mc.field_71474_y.field_74313_G.func_151470_d()) {
                    this.setMode(Mode.GAPPLES);
                    this.eatingApple = true;
                    this.swapToTotem = false;
                } else if (this.eatingApple) {
                    this.setMode(this.oldMode);
                    this.swapToTotem = this.oldSwapToTotem;
                    this.eatingApple = false;
                } else {
                    this.oldMode = this.mode;
                    this.oldSwapToTotem = this.swapToTotem;
                }
            }
            if (!this.shouldTotem()) {
                if (Offhand.mc.field_71439_g.func_184592_cb() == ItemStack.field_190927_a || !this.isItemInOffhand()) {
                    int slot;
                    int n = slot = this.getSlot(this.mode) < 9 ? this.getSlot(this.mode) + 36 : this.getSlot(this.mode);
                    if (this.getSlot(this.mode) != -1) {
                        if (this.oldSlot != -1) {
                            Offhand.mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.field_71439_g);
                            Offhand.mc.field_71442_b.func_187098_a(0, this.oldSlot, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.field_71439_g);
                        }
                        this.oldSlot = slot;
                        Offhand.mc.field_71442_b.func_187098_a(0, slot, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.field_71439_g);
                        Offhand.mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.field_71439_g);
                        Offhand.mc.field_71442_b.func_187098_a(0, slot, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.field_71439_g);
                    }
                }
            } else if (!(this.eatingApple || Offhand.mc.field_71439_g.func_184592_cb() != ItemStack.field_190927_a && Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY)) {
                int slot;
                int n = slot = this.getTotemSlot() < 9 ? this.getTotemSlot() + 36 : this.getTotemSlot();
                if (this.getTotemSlot() != -1) {
                    Offhand.mc.field_71442_b.func_187098_a(0, slot, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.field_71439_g);
                    Offhand.mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.field_71439_g);
                    Offhand.mc.field_71442_b.func_187098_a(0, this.oldSlot, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.field_71439_g);
                    this.oldSlot = -1;
                }
            }
        } else {
            if (!this.unDrawTotem.getValue().booleanValue()) {
                this.manageDrawn();
            }
            this.didSwitchThisTick = false;
            this.holdingCrystal = Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP;
            this.holdingTotem = Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY;
            this.holdingGapple = Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao;
            this.holdingObby = InventoryUtil.isBlock(Offhand.mc.field_71439_g.func_184592_cb().func_77973_b(), BlockObsidian.class);
            this.holdingWeb = InventoryUtil.isBlock(Offhand.mc.field_71439_g.func_184592_cb().func_77973_b(), BlockWeb.class);
            this.totems = Offhand.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_190929_cY).mapToInt(ItemStack::func_190916_E).sum();
            if (this.holdingTotem) {
                this.totems += Offhand.mc.field_71439_g.field_71071_by.field_184439_c.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_190929_cY).mapToInt(ItemStack::func_190916_E).sum();
            }
            this.crystals = Offhand.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_185158_cP).mapToInt(ItemStack::func_190916_E).sum();
            if (this.holdingCrystal) {
                this.crystals += Offhand.mc.field_71439_g.field_71071_by.field_184439_c.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_185158_cP).mapToInt(ItemStack::func_190916_E).sum();
            }
            this.gapples = Offhand.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_151153_ao).mapToInt(ItemStack::func_190916_E).sum();
            if (this.holdingGapple) {
                this.gapples += Offhand.mc.field_71439_g.field_71071_by.field_184439_c.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_151153_ao).mapToInt(ItemStack::func_190916_E).sum();
            }
            if (this.currentMode == Mode2.WEBS || this.currentMode == Mode2.OBSIDIAN) {
                this.obby = Offhand.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> InventoryUtil.isBlock(itemStack.func_77973_b(), BlockObsidian.class)).mapToInt(ItemStack::func_190916_E).sum();
                if (this.holdingObby) {
                    this.obby += Offhand.mc.field_71439_g.field_71071_by.field_184439_c.stream().filter(itemStack -> InventoryUtil.isBlock(itemStack.func_77973_b(), BlockObsidian.class)).mapToInt(ItemStack::func_190916_E).sum();
                }
                this.webs = Offhand.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> InventoryUtil.isBlock(itemStack.func_77973_b(), BlockWeb.class)).mapToInt(ItemStack::func_190916_E).sum();
                if (this.holdingWeb) {
                    this.webs += Offhand.mc.field_71439_g.field_71071_by.field_184439_c.stream().filter(itemStack -> InventoryUtil.isBlock(itemStack.func_77973_b(), BlockWeb.class)).mapToInt(ItemStack::func_190916_E).sum();
                }
            }
            this.doSwitch();
        }
    }

    private void manageDrawn() {
        if (this.currentMode == Mode2.TOTEMS && ((Boolean)this.drawn.getValue()).booleanValue()) {
            this.drawn.setValue(false);
        }
        if (this.currentMode != Mode2.TOTEMS && !((Boolean)this.drawn.getValue()).booleanValue()) {
            this.drawn.setValue(true);
        }
    }

    public void doSwitch() {
        if (this.autoGapple.getValue().booleanValue()) {
            if (Offhand.mc.field_71474_y.field_74313_G.func_151470_d()) {
                if (Offhand.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSword && (!this.onlyWTotem.getValue().booleanValue() || Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY)) {
                    this.setMode(Mode.GAPPLES);
                    this.autoGappleSwitch = true;
                }
            } else if (this.autoGappleSwitch) {
                this.setMode(Mode2.TOTEMS);
                this.autoGappleSwitch = false;
            }
        }
        if (this.currentMode == Mode2.GAPPLES && ((!EntityUtil.isSafe((Entity)Offhand.mc.field_71439_g) || this.bedPlaceable()) && EntityUtil.getHealth((Entity)Offhand.mc.field_71439_g, this.absorption.getValue()) <= this.gappleHealth.getValue().floatValue() || EntityUtil.getHealth((Entity)Offhand.mc.field_71439_g, this.absorption.getValue()) <= this.gappleHoleHealth.getValue().floatValue()) || this.currentMode == Mode2.CRYSTALS && ((!EntityUtil.isSafe((Entity)Offhand.mc.field_71439_g) || this.bedPlaceable()) && EntityUtil.getHealth((Entity)Offhand.mc.field_71439_g, this.absorption.getValue()) <= this.crystalHealth.getValue().floatValue() || EntityUtil.getHealth((Entity)Offhand.mc.field_71439_g, this.absorption.getValue()) <= this.crystalHoleHealth.getValue().floatValue()) || this.currentMode == Mode2.OBSIDIAN && ((!EntityUtil.isSafe((Entity)Offhand.mc.field_71439_g) || this.bedPlaceable()) && EntityUtil.getHealth((Entity)Offhand.mc.field_71439_g, this.absorption.getValue()) <= this.obsidianHealth.getValue().floatValue() || EntityUtil.getHealth((Entity)Offhand.mc.field_71439_g, this.absorption.getValue()) <= this.obsidianHoleHealth.getValue().floatValue()) || this.currentMode == Mode2.WEBS && ((!EntityUtil.isSafe((Entity)Offhand.mc.field_71439_g) || this.bedPlaceable()) && EntityUtil.getHealth((Entity)Offhand.mc.field_71439_g, this.absorption.getValue()) <= this.webHealth.getValue().floatValue() || EntityUtil.getHealth((Entity)Offhand.mc.field_71439_g, this.absorption.getValue()) <= this.webHoleHealth.getValue().floatValue())) {
            if (this.returnToCrystal.getValue().booleanValue() && this.currentMode == Mode2.CRYSTALS) {
                this.switchedForHealthReason = true;
            }
            this.setMode(Mode2.TOTEMS);
        }
        if (this.switchedForHealthReason && (EntityUtil.isSafe((Entity)Offhand.mc.field_71439_g) && !this.bedPlaceable() && EntityUtil.getHealth((Entity)Offhand.mc.field_71439_g, this.absorption.getValue()) > this.crystalHoleHealth.getValue().floatValue() || EntityUtil.getHealth((Entity)Offhand.mc.field_71439_g, this.absorption.getValue()) > this.crystalHealth.getValue().floatValue())) {
            this.setMode(Mode2.CRYSTALS);
            this.switchedForHealthReason = false;
        }
        if (Offhand.mc.field_71462_r instanceof GuiContainer && !this.guis.getValue().booleanValue() && !(Offhand.mc.field_71462_r instanceof GuiInventory)) {
            return;
        }
        Item currentOffhandItem = Offhand.mc.field_71439_g.func_184592_cb().func_77973_b();
        switch (this.currentMode) {
            case TOTEMS: {
                if (this.totems <= 0 || this.holdingTotem) break;
                this.lastTotemSlot = InventoryUtil.findItemInventorySlot(Items.field_190929_cY, false);
                int lastSlot = this.getLastSlot(currentOffhandItem, this.lastTotemSlot);
                this.putItemInOffhand(this.lastTotemSlot, lastSlot);
                break;
            }
            case GAPPLES: {
                if (this.gapples <= 0 || this.holdingGapple) break;
                this.lastGappleSlot = InventoryUtil.findItemInventorySlot(Items.field_151153_ao, false);
                int lastSlot = this.getLastSlot(currentOffhandItem, this.lastGappleSlot);
                this.putItemInOffhand(this.lastGappleSlot, lastSlot);
                break;
            }
            case WEBS: {
                if (this.webs <= 0 || this.holdingWeb) break;
                this.lastWebSlot = InventoryUtil.findInventoryBlock(BlockWeb.class, false);
                int lastSlot = this.getLastSlot(currentOffhandItem, this.lastWebSlot);
                this.putItemInOffhand(this.lastWebSlot, lastSlot);
                break;
            }
            case OBSIDIAN: {
                if (this.obby <= 0 || this.holdingObby) break;
                this.lastObbySlot = InventoryUtil.findInventoryBlock(BlockObsidian.class, false);
                int lastSlot = this.getLastSlot(currentOffhandItem, this.lastObbySlot);
                this.putItemInOffhand(this.lastObbySlot, lastSlot);
                break;
            }
            default: {
                if (this.crystals <= 0 || this.holdingCrystal) break;
                this.lastCrystalSlot = InventoryUtil.findItemInventorySlot(Items.field_185158_cP, false);
                int lastSlot = this.getLastSlot(currentOffhandItem, this.lastCrystalSlot);
                this.putItemInOffhand(this.lastCrystalSlot, lastSlot);
            }
        }
        for (int i = 0; i < this.actions.getValue(); ++i) {
            InventoryUtil.Task task = this.taskList.poll();
            if (task == null) continue;
            task.run();
            if (!task.isSwitching()) continue;
            this.didSwitchThisTick = true;
        }
    }

    private int getLastSlot(Item item, int slotIn) {
        if (item == Items.field_185158_cP) {
            return this.lastCrystalSlot;
        }
        if (item == Items.field_151153_ao) {
            return this.lastGappleSlot;
        }
        if (item == Items.field_190929_cY) {
            return this.lastTotemSlot;
        }
        if (InventoryUtil.isBlock(item, BlockObsidian.class)) {
            return this.lastObbySlot;
        }
        if (InventoryUtil.isBlock(item, BlockWeb.class)) {
            return this.lastWebSlot;
        }
        if (item == Items.field_190931_a) {
            return -1;
        }
        return slotIn;
    }

    private void putItemInOffhand(int slotIn, int slotOut) {
        if (slotIn != -1 && this.taskList.isEmpty()) {
            this.taskList.add(new InventoryUtil.Task(slotIn));
            this.taskList.add(new InventoryUtil.Task(45));
            this.taskList.add(new InventoryUtil.Task(slotOut));
            this.taskList.add(new InventoryUtil.Task());
        }
    }

    private boolean noNearbyPlayers() {
        return this.mode == Mode.CRYSTALS && Offhand.mc.field_71441_e.field_73010_i.stream().noneMatch(e -> e != Offhand.mc.field_71439_g && !Phobos.friendManager.isFriend((EntityPlayer)e) && Offhand.mc.field_71439_g.func_70032_d((Entity)e) <= this.cTargetDistance.getValue().floatValue());
    }

    private boolean isItemInOffhand() {
        switch (this.mode) {
            case GAPPLES: {
                return Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao;
            }
            case CRYSTALS: {
                return Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP;
            }
            case OBSIDIAN: {
                return Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() instanceof ItemBlock && ((ItemBlock)Offhand.mc.field_71439_g.func_184592_cb().func_77973_b()).field_150939_a == Blocks.field_150343_Z;
            }
            case WEBS: {
                return Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() instanceof ItemBlock && ((ItemBlock)Offhand.mc.field_71439_g.func_184592_cb().func_77973_b()).field_150939_a == Blocks.field_150321_G;
            }
        }
        return false;
    }

    private boolean isHeldInMainHand() {
        switch (this.mode) {
            case GAPPLES: {
                return Offhand.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151153_ao;
            }
            case CRYSTALS: {
                return Offhand.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP;
            }
            case OBSIDIAN: {
                return Offhand.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBlock && ((ItemBlock)Offhand.mc.field_71439_g.func_184614_ca().func_77973_b()).field_150939_a == Blocks.field_150343_Z;
            }
            case WEBS: {
                return Offhand.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBlock && ((ItemBlock)Offhand.mc.field_71439_g.func_184614_ca().func_77973_b()).field_150939_a == Blocks.field_150321_G;
            }
        }
        return false;
    }

    private boolean shouldTotem() {
        if (this.isHeldInMainHand() || this.isSwapToTotem()) {
            return true;
        }
        if (this.holeCheck.getValue().booleanValue() && EntityUtil.isInHole((Entity)Offhand.mc.field_71439_g) && !this.bedPlaceable()) {
            return Offhand.mc.field_71439_g.func_110143_aJ() + Offhand.mc.field_71439_g.func_110139_bj() <= this.getHoleHealth() || Offhand.mc.field_71439_g.func_184582_a(EntityEquipmentSlot.CHEST).func_77973_b() == Items.field_185160_cR || Offhand.mc.field_71439_g.field_70143_R >= 3.0f || this.noNearbyPlayers() || this.crystalCheck.getValue() != false && this.isCrystalsAABBEmpty();
        }
        return Offhand.mc.field_71439_g.func_110143_aJ() + Offhand.mc.field_71439_g.func_110139_bj() <= this.getHealth() || Offhand.mc.field_71439_g.func_184582_a(EntityEquipmentSlot.CHEST).func_77973_b() == Items.field_185160_cR || Offhand.mc.field_71439_g.field_70143_R >= 3.0f || this.noNearbyPlayers() || this.crystalCheck.getValue() != false && this.isCrystalsAABBEmpty();
    }

    private boolean isNotEmpty(BlockPos pos) {
        return Offhand.mc.field_71441_e.func_72839_b(null, new AxisAlignedBB(pos)).stream().anyMatch(e -> e instanceof EntityEnderCrystal);
    }

    private float getHealth() {
        switch (this.mode) {
            case CRYSTALS: {
                return this.crystalHealth.getValue().floatValue();
            }
            case GAPPLES: {
                return this.gappleHealth.getValue().floatValue();
            }
            case OBSIDIAN: {
                return this.obsidianHealth.getValue().floatValue();
            }
        }
        return this.webHealth.getValue().floatValue();
    }

    private float getHoleHealth() {
        switch (this.mode) {
            case CRYSTALS: {
                return this.crystalHoleHealth.getValue().floatValue();
            }
            case GAPPLES: {
                return this.gappleHoleHealth.getValue().floatValue();
            }
            case OBSIDIAN: {
                return this.obsidianHoleHealth.getValue().floatValue();
            }
        }
        return this.webHoleHealth.getValue().floatValue();
    }

    private boolean isCrystalsAABBEmpty() {
        return this.isNotEmpty(Offhand.mc.field_71439_g.func_180425_c().func_177982_a(1, 0, 0)) || this.isNotEmpty(Offhand.mc.field_71439_g.func_180425_c().func_177982_a(-1, 0, 0)) || this.isNotEmpty(Offhand.mc.field_71439_g.func_180425_c().func_177982_a(0, 0, 1)) || this.isNotEmpty(Offhand.mc.field_71439_g.func_180425_c().func_177982_a(0, 0, -1)) || this.isNotEmpty(Offhand.mc.field_71439_g.func_180425_c());
    }

    int getStackSize() {
        int size = 0;
        if (this.shouldTotem()) {
            for (int i = 45; i > 0; --i) {
                if (Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() != Items.field_190929_cY) continue;
                size += Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_190916_E();
            }
        } else if (this.mode == Mode.OBSIDIAN) {
            for (int i = 45; i > 0; --i) {
                if (!(Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemBlock) || ((ItemBlock)Offhand.mc.field_71439_g.field_71071_by.func_70301_a((int)i).func_77973_b()).field_150939_a != Blocks.field_150343_Z) continue;
                size += Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_190916_E();
            }
        } else if (this.mode == Mode.WEBS) {
            for (int i = 45; i > 0; --i) {
                if (!(Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemBlock) || ((ItemBlock)Offhand.mc.field_71439_g.field_71071_by.func_70301_a((int)i).func_77973_b()).field_150939_a != Blocks.field_150321_G) continue;
                size += Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_190916_E();
            }
        } else {
            for (int i = 45; i > 0; --i) {
                if (Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() != (this.mode == Mode.CRYSTALS ? Items.field_185158_cP : Items.field_151153_ao)) continue;
                size += Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_190916_E();
            }
        }
        return size;
    }

    int getSlot(Mode m) {
        int slot = -1;
        if (m == Mode.OBSIDIAN) {
            for (int i = 45; i > 0; --i) {
                if (!(Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemBlock) || ((ItemBlock)Offhand.mc.field_71439_g.field_71071_by.func_70301_a((int)i).func_77973_b()).field_150939_a != Blocks.field_150343_Z) continue;
                slot = i;
                break;
            }
        } else if (m == Mode.WEBS) {
            for (int i = 45; i > 0; --i) {
                if (!(Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemBlock) || ((ItemBlock)Offhand.mc.field_71439_g.field_71071_by.func_70301_a((int)i).func_77973_b()).field_150939_a != Blocks.field_150321_G) continue;
                slot = i;
                break;
            }
        } else {
            for (int i = 45; i > 0; --i) {
                if (Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() != (m == Mode.CRYSTALS ? Items.field_185158_cP : Items.field_151153_ao)) continue;
                slot = i;
                break;
            }
        }
        return slot;
    }

    int getTotemSlot() {
        int totemSlot = -1;
        for (int i = 45; i > 0; --i) {
            if (Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() != Items.field_190929_cY) continue;
            totemSlot = i;
            break;
        }
        return totemSlot;
    }

    private String getModeStr() {
        switch (this.mode) {
            case GAPPLES: {
                return "G";
            }
            case WEBS: {
                return "W";
            }
            case OBSIDIAN: {
                return "O";
            }
        }
        return "C";
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setMode(Mode2 mode) {
        this.currentMode = this.currentMode == mode ? Mode2.TOTEMS : (this.cycle.getValue() == false && this.crystalToTotem.getValue() != false && (this.currentMode == Mode2.CRYSTALS || this.currentMode == Mode2.OBSIDIAN || this.currentMode == Mode2.WEBS) && mode == Mode2.GAPPLES ? Mode2.TOTEMS : mode);
    }

    public boolean isSwapToTotem() {
        return this.swapToTotem;
    }

    public void setSwapToTotem(boolean swapToTotem) {
        this.swapToTotem = swapToTotem;
    }

    private boolean bedPlaceable() {
        if (!this.bedcheck.getValue().booleanValue()) {
            return false;
        }
        if (Offhand.mc.field_71441_e.func_180495_p(Offhand.mc.field_71439_g.func_180425_c()).func_177230_c() != Blocks.field_150324_C && Offhand.mc.field_71441_e.func_180495_p(Offhand.mc.field_71439_g.func_180425_c()).func_177230_c() != Blocks.field_150350_a) {
            return false;
        }
        for (EnumFacing facing : EnumFacing.values()) {
            if (facing == EnumFacing.UP || facing == EnumFacing.DOWN || Offhand.mc.field_71441_e.func_180495_p(Offhand.mc.field_71439_g.func_180425_c().func_177972_a(facing)).func_177230_c() != Blocks.field_150324_C && Offhand.mc.field_71441_e.func_180495_p(Offhand.mc.field_71439_g.func_180425_c().func_177972_a(facing)).func_177230_c() != Blocks.field_150350_a) continue;
            return true;
        }
        return false;
    }

    public static enum Mode {
        CRYSTALS,
        GAPPLES,
        OBSIDIAN,
        WEBS;

    }

    public static enum Type {
        OLD,
        NEW;

    }

    public static enum Mode2 {
        TOTEMS,
        GAPPLES,
        CRYSTALS,
        OBSIDIAN,
        WEBS;

    }

    public static enum NameMode {
        MODE,
        TOTEM,
        AMOUNT;

    }
}

