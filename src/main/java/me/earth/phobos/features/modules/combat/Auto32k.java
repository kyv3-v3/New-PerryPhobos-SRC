/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockDeadBush
 *  net.minecraft.block.BlockDispenser
 *  net.minecraft.block.BlockFire
 *  net.minecraft.block.BlockHopper
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.block.BlockShulkerBox
 *  net.minecraft.block.BlockSnow
 *  net.minecraft.block.BlockTallGrass
 *  net.minecraft.client.gui.GuiHopper
 *  net.minecraft.client.gui.inventory.GuiDispenser
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityExpBottle
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.ContainerHopper
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemPickaxe
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketCloseWindow
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 *  net.minecraftforge.client.event.GuiOpenEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 *  org.lwjgl.input.Keyboard
 */
package me.earth.phobos.features.modules.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import me.earth.phobos.event.events.ClientEvent;
import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.gui.PhobosGui;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.player.Freecam;
import me.earth.phobos.features.setting.Bind;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.BlockUtil;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.InventoryUtil;
import me.earth.phobos.util.MathUtil;
import me.earth.phobos.util.RotationUtil;
import me.earth.phobos.util.TimerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class Auto32k
extends Module {
    private static Auto32k instance;
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay/Place", 25, 0, 250));
    private final Setting<Float> range = this.register(new Setting<Float>("PlaceRange", Float.valueOf(4.5f), Float.valueOf(0.0f), Float.valueOf(6.0f)));
    private final Setting<Boolean> raytrace = this.register(new Setting<Boolean>("Raytrace", false));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", false));
    private final Setting<Double> targetRange = this.register(new Setting<Double>("TargetRange", 6.0, 0.0, 20.0));
    private final Setting<Boolean> extra = this.register(new Setting<Boolean>("ExtraRotation", false));
    private final Setting<PlaceType> placeType = this.register(new Setting<PlaceType>("Place", PlaceType.CLOSE));
    private final Setting<Boolean> freecam = this.register(new Setting<Boolean>("Freecam", false));
    private final Setting<Boolean> onOtherHoppers = this.register(new Setting<Boolean>("UseHoppers", false));
    private final Setting<Boolean> checkForShulker = this.register(new Setting<Boolean>("CheckShulker", true));
    private final Setting<Integer> checkDelay = this.register(new Setting<Object>("CheckDelay", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(500), v -> this.checkForShulker.getValue()));
    private final Setting<Boolean> drop = this.register(new Setting<Boolean>("Drop", false));
    private final Setting<Boolean> mine = this.register(new Setting<Object>("Mine", Boolean.valueOf(false), v -> this.drop.getValue()));
    private final Setting<Boolean> checkStatus = this.register(new Setting<Boolean>("CheckState", true));
    private final Setting<Boolean> packet = this.register(new Setting<Boolean>("Packet", false));
    private final Setting<Boolean> superPacket = this.register(new Setting<Boolean>("DispExtra", false));
    private final Setting<Boolean> secretClose = this.register(new Setting<Boolean>("SecretClose", false));
    private final Setting<Boolean> closeGui = this.register(new Setting<Object>("CloseGui", Boolean.valueOf(false), v -> this.secretClose.getValue()));
    private final Setting<Boolean> repeatSwitch = this.register(new Setting<Boolean>("SwitchOnFail", true));
    private final Setting<Float> hopperDistance = this.register(new Setting<Float>("HopperRange", Float.valueOf(8.0f), Float.valueOf(0.0f), Float.valueOf(20.0f)));
    private final Setting<Integer> trashSlot = this.register(new Setting<Integer>("32kSlot", 0, 0, 9));
    private final Setting<Boolean> messages = this.register(new Setting<Boolean>("Messages", false));
    private final Setting<Boolean> antiHopper = this.register(new Setting<Boolean>("AntiHopper", false));
    private final TimerUtil placeTimer = new TimerUtil();
    private final TimerUtil disableTimer = new TimerUtil();
    public Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.NORMAL));
    private final Setting<Integer> delayDispenser = this.register(new Setting<Object>("Blocks/Place", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(8), v -> this.mode.getValue() != Mode.NORMAL));
    private final Setting<Integer> blocksPerPlace = this.register(new Setting<Object>("Actions/Place", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(3), v -> this.mode.getValue() == Mode.NORMAL));
    private final Setting<Boolean> preferObby = this.register(new Setting<Object>("UseObby", Boolean.valueOf(false), v -> this.mode.getValue() != Mode.NORMAL));
    private final Setting<Boolean> simulate = this.register(new Setting<Object>("Simulate", Boolean.valueOf(true), v -> this.mode.getValue() != Mode.NORMAL));
    public Setting<Boolean> autoSwitch = this.register(new Setting<Object>("AutoSwitch", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.NORMAL));
    public Setting<Boolean> withBind = this.register(new Setting<Object>("WithBind", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.NORMAL && this.autoSwitch.getValue() != false));
    public Setting<Bind> switchBind = this.register(new Setting<Object>("SwitchBind", new Bind(-1), v -> this.autoSwitch.getValue() != false && this.mode.getValue() == Mode.NORMAL && this.withBind.getValue() != false));
    public boolean switching;
    public Step currentStep = Step.PRE;
    private float yaw;
    private float pitch;
    private boolean spoof;
    private int shulkerSlot = -1;
    private int hopperSlot = -1;
    private BlockPos hopperPos;
    private EntityPlayer target;
    private int obbySlot = -1;
    private int dispenserSlot = -1;
    private int redstoneSlot = -1;
    private DispenserData finalDispenserData;
    private int actionsThisTick;
    private boolean checkedThisTick;
    private boolean shouldDisable;

    public Auto32k() {
        super("Auto32k", "Auto32ks.", Module.Category.COMBAT, true, false, false);
        instance = this;
    }

    public static Auto32k getInstance() {
        if (instance == null) {
            instance = new Auto32k();
        }
        return instance;
    }

    @Override
    public void onEnable() {
        this.checkedThisTick = false;
        this.resetFields();
        if (Auto32k.mc.field_71462_r instanceof GuiHopper) {
            this.currentStep = Step.HOPPERGUI;
        }
        if (this.mode.getValue() == Mode.NORMAL && this.autoSwitch.getValue().booleanValue() && !this.withBind.getValue().booleanValue()) {
            this.switching = true;
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (event.getStage() != 0) {
            return;
        }
        if (this.shouldDisable && this.disableTimer.passedMs(1000L)) {
            this.shouldDisable = false;
            this.disable();
            return;
        }
        this.checkedThisTick = false;
        this.actionsThisTick = 0;
        if (this.isOff() || this.mode.getValue() == Mode.NORMAL && this.autoSwitch.getValue().booleanValue() && !this.switching) {
            return;
        }
        if (this.mode.getValue() == Mode.NORMAL) {
            this.normal32k();
        } else {
            this.processDispenser32k();
        }
    }

    @SubscribeEvent
    public void onGui(GuiOpenEvent event) {
        if (Auto32k.fullNullCheck() || this.isOff()) {
            return;
        }
        if (!this.secretClose.getValue().booleanValue() && Auto32k.mc.field_71462_r instanceof GuiHopper) {
            if (this.drop.getValue().booleanValue() && Auto32k.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151048_u && this.hopperPos != null) {
                int pickaxeSlot;
                Auto32k.mc.field_71439_g.func_71040_bB(true);
                if (this.mine.getValue().booleanValue() && this.hopperPos != null && (pickaxeSlot = InventoryUtil.findHotbarBlock(ItemPickaxe.class)) != -1) {
                    InventoryUtil.switchToHotbarSlot(pickaxeSlot, false);
                    if (this.rotate.getValue().booleanValue()) {
                        this.rotateToPos(this.hopperPos.func_177984_a(), null);
                    }
                    Auto32k.mc.field_71442_b.func_180512_c(this.hopperPos.func_177984_a(), Auto32k.mc.field_71439_g.func_174811_aO());
                    Auto32k.mc.field_71442_b.func_180512_c(this.hopperPos.func_177984_a(), Auto32k.mc.field_71439_g.func_174811_aO());
                    Auto32k.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                }
            }
            this.resetFields();
            if (this.mode.getValue() != Mode.NORMAL) {
                this.disable();
                return;
            }
            if (!this.autoSwitch.getValue().booleanValue() || this.mode.getValue() == Mode.DISPENSER) {
                this.disable();
            } else if (!this.withBind.getValue().booleanValue()) {
                this.disable();
            }
        } else if (event.getGui() instanceof GuiHopper) {
            this.currentStep = Step.HOPPERGUI;
        }
    }

    @Override
    public String getDisplayInfo() {
        if (this.switching) {
            return "\u00a7aSwitch";
        }
        return null;
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (this.isOff()) {
            return;
        }
        if (Keyboard.getEventKeyState() && !(Auto32k.mc.field_71462_r instanceof PhobosGui) && this.switchBind.getValue().getKey() == Keyboard.getEventKey() && this.withBind.getValue().booleanValue()) {
            if (this.switching) {
                this.resetFields();
                this.switching = true;
            }
            this.switching = !this.switching;
        }
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        Setting setting;
        if (event.getStage() == 2 && (setting = event.getSetting()) != null && setting.getFeature().equals(this) && setting.equals(this.mode)) {
            this.resetFields();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (Auto32k.fullNullCheck() || this.isOff()) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayer) {
            if (this.spoof) {
                CPacketPlayer packet = (CPacketPlayer)event.getPacket();
                packet.field_149476_e = this.yaw;
                packet.field_149473_f = this.pitch;
                this.spoof = false;
            }
        } else if (event.getPacket() instanceof CPacketCloseWindow) {
            if (!this.secretClose.getValue().booleanValue() && Auto32k.mc.field_71462_r instanceof GuiHopper && this.hopperPos != null) {
                if (this.drop.getValue().booleanValue() && Auto32k.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151048_u) {
                    int pickaxeSlot;
                    Auto32k.mc.field_71439_g.func_71040_bB(true);
                    if (this.mine.getValue().booleanValue() && (pickaxeSlot = InventoryUtil.findHotbarBlock(ItemPickaxe.class)) != -1) {
                        InventoryUtil.switchToHotbarSlot(pickaxeSlot, false);
                        if (this.rotate.getValue().booleanValue()) {
                            this.rotateToPos(this.hopperPos.func_177984_a(), null);
                        }
                        Auto32k.mc.field_71442_b.func_180512_c(this.hopperPos.func_177984_a(), Auto32k.mc.field_71439_g.func_174811_aO());
                        Auto32k.mc.field_71442_b.func_180512_c(this.hopperPos.func_177984_a(), Auto32k.mc.field_71439_g.func_174811_aO());
                        Auto32k.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                    }
                }
                this.resetFields();
                if (!this.autoSwitch.getValue().booleanValue() || this.mode.getValue() == Mode.DISPENSER) {
                    this.disable();
                } else if (!this.withBind.getValue().booleanValue()) {
                    this.disable();
                }
            } else if (this.secretClose.getValue().booleanValue() && (!this.autoSwitch.getValue().booleanValue() || this.switching || this.mode.getValue() == Mode.DISPENSER) && this.currentStep == Step.HOPPERGUI) {
                event.setCanceled(true);
            }
        }
    }

    private void normal32k() {
        if (this.autoSwitch.getValue().booleanValue()) {
            if (this.switching) {
                this.processNormal32k();
            } else {
                this.resetFields();
            }
        } else {
            this.processNormal32k();
        }
    }

    private void processNormal32k() {
        if (this.isOff()) {
            return;
        }
        if (this.placeTimer.passedMs(this.delay.getValue().intValue())) {
            this.check();
            switch (this.currentStep) {
                case PRE: {
                    this.runPreStep();
                    if (this.currentStep == Step.PRE) break;
                }
                case HOPPER: {
                    if (this.currentStep == Step.HOPPER) {
                        this.checkState();
                        if (this.currentStep == Step.PRE) {
                            if (this.checkedThisTick) {
                                this.processNormal32k();
                            }
                            return;
                        }
                        this.runHopperStep();
                        if (this.actionsThisTick >= this.blocksPerPlace.getValue() && !this.placeTimer.passedMs(this.delay.getValue().intValue())) break;
                    }
                }
                case SHULKER: {
                    this.checkState();
                    if (this.currentStep == Step.PRE) {
                        if (this.checkedThisTick) {
                            this.processNormal32k();
                        }
                        return;
                    }
                    this.runShulkerStep();
                    if (this.actionsThisTick >= this.blocksPerPlace.getValue() && !this.placeTimer.passedMs(this.delay.getValue().intValue())) break;
                }
                case CLICKHOPPER: {
                    this.checkState();
                    if (this.currentStep == Step.PRE) {
                        if (this.checkedThisTick) {
                            this.processNormal32k();
                        }
                        return;
                    }
                    this.runClickHopper();
                }
                case HOPPERGUI: {
                    this.runHopperGuiStep();
                    break;
                }
                default: {
                    Command.sendMessage("\u00a7cThis shouldn't happen, report to 3arthqu4ke!!!");
                    Command.sendMessage("\u00a7cThis shouldn't happen, report to 3arthqu4ke!!!");
                    Command.sendMessage("\u00a7cThis shouldn't happen, report to 3arthqu4ke!!!");
                    Command.sendMessage("\u00a7cThis shouldn't happen, report to 3arthqu4ke!!!");
                    Command.sendMessage("\u00a7cThis shouldn't happen, report to 3arthqu4ke!!!");
                    this.currentStep = Step.PRE;
                }
            }
        }
    }

    private void runPreStep() {
        if (this.isOff()) {
            return;
        }
        PlaceType type = this.placeType.getValue();
        if (Freecam.getInstance().isOn() && !this.freecam.getValue().booleanValue()) {
            if (this.messages.getValue().booleanValue()) {
                Command.sendMessage("\u00a7c<Auto32k> Disable Freecam.");
            }
            if (this.autoSwitch.getValue().booleanValue()) {
                this.resetFields();
                if (!this.withBind.getValue().booleanValue()) {
                    this.disable();
                }
            } else {
                this.disable();
            }
            return;
        }
        this.hopperSlot = InventoryUtil.findHotbarBlock(BlockHopper.class);
        this.shulkerSlot = InventoryUtil.findHotbarBlock(BlockShulkerBox.class);
        if (Auto32k.mc.field_71439_g.func_184592_cb().func_77973_b() instanceof ItemBlock) {
            Block block = ((ItemBlock)Auto32k.mc.field_71439_g.func_184592_cb().func_77973_b()).func_179223_d();
            if (block instanceof BlockShulkerBox) {
                this.shulkerSlot = -2;
            } else if (block instanceof BlockHopper) {
                this.hopperSlot = -2;
            }
        }
        if (this.shulkerSlot == -1 || this.hopperSlot == -1) {
            if (this.messages.getValue().booleanValue()) {
                Command.sendMessage("\u00a7c<Auto32k> Materials not found.");
            }
            if (this.autoSwitch.getValue().booleanValue()) {
                this.resetFields();
                if (!this.withBind.getValue().booleanValue()) {
                    this.disable();
                }
            } else {
                this.disable();
            }
            return;
        }
        this.target = EntityUtil.getClosestEnemy(this.targetRange.getValue());
        if (this.target == null) {
            if (this.autoSwitch.getValue().booleanValue()) {
                if (this.switching) {
                    this.resetFields();
                    this.switching = true;
                } else {
                    this.resetFields();
                }
                return;
            }
            type = this.placeType.getValue() == PlaceType.MOUSE ? PlaceType.MOUSE : PlaceType.CLOSE;
        }
        this.hopperPos = this.findBestPos(type, this.target);
        if (this.hopperPos != null) {
            this.currentStep = Auto32k.mc.field_71441_e.func_180495_p(this.hopperPos).func_177230_c() instanceof BlockHopper ? Step.SHULKER : Step.HOPPER;
        } else {
            if (this.messages.getValue().booleanValue()) {
                Command.sendMessage("\u00a7c<Auto32k> Block not found.");
            }
            if (this.autoSwitch.getValue().booleanValue()) {
                this.resetFields();
                if (!this.withBind.getValue().booleanValue()) {
                    this.disable();
                }
            } else {
                this.disable();
            }
        }
    }

    private void runHopperStep() {
        if (this.isOff()) {
            return;
        }
        if (this.currentStep == Step.HOPPER) {
            this.runPlaceStep(this.hopperPos, this.hopperSlot);
            this.currentStep = Step.SHULKER;
        }
    }

    private void runShulkerStep() {
        if (this.isOff()) {
            return;
        }
        if (this.currentStep == Step.SHULKER) {
            this.runPlaceStep(this.hopperPos.func_177984_a(), this.shulkerSlot);
            this.currentStep = Step.CLICKHOPPER;
        }
    }

    private void runClickHopper() {
        if (this.isOff()) {
            return;
        }
        if (this.currentStep != Step.CLICKHOPPER) {
            return;
        }
        if (this.mode.getValue() == Mode.NORMAL && !(Auto32k.mc.field_71441_e.func_180495_p(this.hopperPos.func_177984_a()).func_177230_c() instanceof BlockShulkerBox) && this.checkForShulker.getValue().booleanValue()) {
            if (this.placeTimer.passedMs(this.checkDelay.getValue().intValue())) {
                this.currentStep = Step.SHULKER;
            }
            return;
        }
        this.clickBlock(this.hopperPos);
        this.currentStep = Step.HOPPERGUI;
    }

    private void runHopperGuiStep() {
        if (this.isOff()) {
            return;
        }
        if (this.currentStep != Step.HOPPERGUI) {
            return;
        }
        if (Auto32k.mc.field_71439_g.field_71070_bA instanceof ContainerHopper) {
            if (!EntityUtil.holding32k((EntityPlayer)Auto32k.mc.field_71439_g)) {
                int swordIndex = -1;
                for (int i = 0; i < 5; ++i) {
                    if (!EntityUtil.is32k(((Slot)Auto32k.mc.field_71439_g.field_71070_bA.field_75151_b.get((int)0)).field_75224_c.func_70301_a(i))) continue;
                    swordIndex = i;
                    break;
                }
                if (swordIndex == -1) {
                    return;
                }
                if (this.trashSlot.getValue() != 0) {
                    InventoryUtil.switchToHotbarSlot(this.trashSlot.getValue() - 1, false);
                } else if (this.mode.getValue() != Mode.NORMAL && this.shulkerSlot > 35 && this.shulkerSlot != 45) {
                    InventoryUtil.switchToHotbarSlot(44 - this.shulkerSlot, false);
                }
                Auto32k.mc.field_71442_b.func_187098_a(Auto32k.mc.field_71439_g.field_71070_bA.field_75152_c, swordIndex, this.trashSlot.getValue() == 0 ? Auto32k.mc.field_71439_g.field_71071_by.field_70461_c : this.trashSlot.getValue() - 1, ClickType.SWAP, (EntityPlayer)Auto32k.mc.field_71439_g);
            } else if (this.closeGui.getValue().booleanValue() && this.secretClose.getValue().booleanValue()) {
                Auto32k.mc.field_71439_g.func_71053_j();
            }
        } else if (EntityUtil.holding32k((EntityPlayer)Auto32k.mc.field_71439_g)) {
            if (this.autoSwitch.getValue().booleanValue() && this.mode.getValue() == Mode.NORMAL) {
                this.switching = false;
            } else if (!this.autoSwitch.getValue().booleanValue() || this.mode.getValue() == Mode.DISPENSER) {
                this.shouldDisable = true;
                this.disableTimer.reset();
            }
        }
    }

    private void runPlaceStep(BlockPos pos, int slot) {
        if (this.isOff()) {
            return;
        }
        EnumFacing side = EnumFacing.UP;
        if (this.antiHopper.getValue().booleanValue() && this.currentStep == Step.HOPPER) {
            boolean foundfacing = false;
            for (EnumFacing facing : EnumFacing.values()) {
                if (Auto32k.mc.field_71441_e.func_180495_p(pos.func_177972_a(facing)).func_177230_c() == Blocks.field_150438_bZ || Auto32k.mc.field_71441_e.func_180495_p(pos.func_177972_a(facing)).func_185904_a().func_76222_j()) continue;
                foundfacing = true;
                side = facing;
                break;
            }
            if (!foundfacing) {
                this.resetFields();
                return;
            }
        } else {
            side = BlockUtil.getFirstFacing(pos);
            if (side == null) {
                this.resetFields();
                return;
            }
        }
        BlockPos neighbour = pos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        Auto32k.mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
        Auto32k.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Auto32k.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
        if (this.rotate.getValue().booleanValue()) {
            if (this.blocksPerPlace.getValue() > 1) {
                float[] angle = RotationUtil.getLegitRotations(hitVec);
                if (this.extra.getValue().booleanValue()) {
                    RotationUtil.faceYawAndPitch(angle[0], angle[1]);
                }
            } else {
                this.rotateToPos(null, hitVec);
            }
        }
        InventoryUtil.switchToHotbarSlot(slot, false);
        BlockUtil.rightClickBlock(neighbour, hitVec, slot == -2 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, opposite, this.packet.getValue());
        Auto32k.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Auto32k.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
        this.placeTimer.reset();
        ++this.actionsThisTick;
    }

    private BlockPos findBestPos(PlaceType type, EntityPlayer target) {
        BlockPos pos = null;
        NonNullList positions = NonNullList.func_191196_a();
        positions.addAll((Collection)BlockUtil.getSphere(EntityUtil.getPlayerPos((EntityPlayer)Auto32k.mc.field_71439_g), this.range.getValue().floatValue(), this.range.getValue().intValue(), false, true, 0).stream().filter(this::canPlace).collect(Collectors.toList()));
        if (positions.isEmpty()) {
            return null;
        }
        switch (type) {
            case MOUSE: {
                if (Auto32k.mc.field_71476_x != null && Auto32k.mc.field_71476_x.field_72313_a == RayTraceResult.Type.BLOCK) {
                    BlockPos mousePos = Auto32k.mc.field_71476_x.func_178782_a();
                    if (!this.canPlace(mousePos)) {
                        BlockPos mousePosUp = mousePos.func_177984_a();
                        if (this.canPlace(mousePosUp)) {
                            pos = mousePosUp;
                        }
                    } else {
                        pos = mousePos;
                    }
                }
                if (pos != null) break;
            }
            case CLOSE: {
                positions.sort(Comparator.comparingDouble(pos2 -> Auto32k.mc.field_71439_g.func_174818_b(pos2)));
                pos = (BlockPos)positions.get(0);
                break;
            }
            case ENEMY: {
                positions.sort(Comparator.comparingDouble(((EntityPlayer)target)::func_174818_b));
                pos = (BlockPos)positions.get(0);
                break;
            }
            case MIDDLE: {
                ArrayList<BlockPos> toRemove = new ArrayList<BlockPos>();
                NonNullList copy = NonNullList.func_191196_a();
                copy.addAll((Collection)positions);
                for (BlockPos position : copy) {
                    double difference = Auto32k.mc.field_71439_g.func_174818_b(position) - target.func_174818_b(position);
                    if (!(difference > 1.0) && !(difference < -1.0)) continue;
                    toRemove.add(position);
                }
                copy.removeAll(toRemove);
                if (copy.isEmpty()) {
                    copy.addAll((Collection)positions);
                }
                copy.sort(Comparator.comparingDouble(pos2 -> Auto32k.mc.field_71439_g.func_174818_b(pos2)));
                pos = (BlockPos)copy.get(0);
                break;
            }
            case FAR: {
                positions.sort(Comparator.comparingDouble(pos2 -> -target.func_174818_b(pos2)));
                pos = (BlockPos)positions.get(0);
                break;
            }
            case SAFE: {
                positions.sort(Comparator.comparingInt(pos2 -> -this.safetyFactor((BlockPos)pos2)));
                pos = (BlockPos)positions.get(0);
            }
        }
        return pos;
    }

    private boolean canPlace(BlockPos pos) {
        if (pos == null) {
            return false;
        }
        BlockPos boost = pos.func_177984_a();
        if (this.isGoodMaterial(Auto32k.mc.field_71441_e.func_180495_p(pos).func_177230_c(), this.onOtherHoppers.getValue()) || this.isGoodMaterial(Auto32k.mc.field_71441_e.func_180495_p(boost).func_177230_c(), false)) {
            return false;
        }
        if (!(!this.raytrace.getValue().booleanValue() || BlockUtil.rayTracePlaceCheck(pos, this.raytrace.getValue()) && BlockUtil.rayTracePlaceCheck(pos, this.raytrace.getValue()))) {
            return false;
        }
        if (this.badEntities(pos) || this.badEntities(boost)) {
            return false;
        }
        if (this.onOtherHoppers.getValue().booleanValue() && Auto32k.mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockHopper) {
            return true;
        }
        return this.findFacing(pos);
    }

    private void check() {
        if (!(this.currentStep == Step.PRE || this.currentStep == Step.HOPPER || this.hopperPos == null || Auto32k.mc.field_71462_r instanceof GuiHopper || EntityUtil.holding32k((EntityPlayer)Auto32k.mc.field_71439_g) || !(Auto32k.mc.field_71439_g.func_174818_b(this.hopperPos) > MathUtil.square(this.hopperDistance.getValue().floatValue())) && Auto32k.mc.field_71441_e.func_180495_p(this.hopperPos).func_177230_c() == Blocks.field_150438_bZ)) {
            this.resetFields();
            if (!this.autoSwitch.getValue().booleanValue() || !this.withBind.getValue().booleanValue() || this.mode.getValue() != Mode.NORMAL) {
                this.disable();
            }
        }
    }

    private void checkState() {
        if (!this.checkStatus.getValue().booleanValue() || this.checkedThisTick || this.currentStep != Step.HOPPER && this.currentStep != Step.SHULKER && this.currentStep != Step.CLICKHOPPER) {
            this.checkedThisTick = false;
            return;
        }
        if (this.hopperPos == null || this.isGoodMaterial(Auto32k.mc.field_71441_e.func_180495_p(this.hopperPos).func_177230_c(), true) || this.isGoodMaterial(Auto32k.mc.field_71441_e.func_180495_p(this.hopperPos.func_177984_a()).func_177230_c(), false) && !(Auto32k.mc.field_71441_e.func_180495_p(this.hopperPos.func_177984_a()).func_177230_c() instanceof BlockShulkerBox) || this.badEntities(this.hopperPos) || this.badEntities(this.hopperPos.func_177984_a())) {
            if (this.autoSwitch.getValue().booleanValue() && this.mode.getValue() == Mode.NORMAL) {
                if (this.switching) {
                    this.resetFields();
                    if (this.repeatSwitch.getValue().booleanValue()) {
                        this.switching = true;
                    }
                } else {
                    this.resetFields();
                }
                if (!this.withBind.getValue().booleanValue()) {
                    this.disable();
                }
            } else {
                this.disable();
            }
            this.checkedThisTick = true;
        }
    }

    private void processDispenser32k() {
        if (this.isOff()) {
            return;
        }
        if (this.placeTimer.passedMs(this.delay.getValue().intValue())) {
            this.check();
            switch (this.currentStep) {
                case PRE: {
                    this.runDispenserPreStep();
                    if (this.currentStep == Step.PRE) break;
                }
                case HOPPER: {
                    this.runHopperStep();
                    this.currentStep = Step.DISPENSER;
                    if (this.actionsThisTick >= this.delayDispenser.getValue() && !this.placeTimer.passedMs(this.delay.getValue().intValue())) break;
                }
                case DISPENSER: {
                    boolean quickCheck;
                    this.runDispenserStep();
                    boolean bl = quickCheck = !Auto32k.mc.field_71441_e.func_180495_p(this.finalDispenserData.getHelpingPos()).func_185904_a().func_76222_j();
                    if (this.actionsThisTick >= this.delayDispenser.getValue() && !this.placeTimer.passedMs(this.delay.getValue().intValue()) || this.currentStep != Step.DISPENSER_HELPING && this.currentStep != Step.CLICK_DISPENSER || this.rotate.getValue().booleanValue() && quickCheck) break;
                }
                case DISPENSER_HELPING: {
                    this.runDispenserStep();
                    if (this.actionsThisTick >= this.delayDispenser.getValue() && !this.placeTimer.passedMs(this.delay.getValue().intValue()) || this.currentStep != Step.CLICK_DISPENSER && this.currentStep != Step.DISPENSER_HELPING || this.rotate.getValue().booleanValue()) break;
                }
                case CLICK_DISPENSER: {
                    this.clickDispenser();
                    if (this.actionsThisTick >= this.delayDispenser.getValue() && !this.placeTimer.passedMs(this.delay.getValue().intValue())) break;
                }
                case DISPENSER_GUI: {
                    this.dispenserGui();
                    if (this.currentStep == Step.DISPENSER_GUI) break;
                }
                case REDSTONE: {
                    this.placeRedstone();
                    if (this.actionsThisTick >= this.delayDispenser.getValue() && !this.placeTimer.passedMs(this.delay.getValue().intValue())) break;
                }
                case CLICKHOPPER: {
                    this.runClickHopper();
                    if (this.actionsThisTick >= this.delayDispenser.getValue() && !this.placeTimer.passedMs(this.delay.getValue().intValue())) break;
                }
                case HOPPERGUI: {
                    this.runHopperGuiStep();
                    if (this.actionsThisTick >= this.delayDispenser.getValue() && !this.placeTimer.passedMs(this.delay.getValue().intValue())) break;
                }
            }
        }
    }

    private void placeRedstone() {
        if (this.isOff()) {
            return;
        }
        if (this.badEntities(this.hopperPos.func_177984_a()) && !(Auto32k.mc.field_71441_e.func_180495_p(this.hopperPos.func_177984_a()).func_177230_c() instanceof BlockShulkerBox)) {
            return;
        }
        this.runPlaceStep(this.finalDispenserData.getRedStonePos(), this.redstoneSlot);
        this.currentStep = Step.CLICKHOPPER;
    }

    private void clickDispenser() {
        if (this.isOff()) {
            return;
        }
        this.clickBlock(this.finalDispenserData.getDispenserPos());
        this.currentStep = Step.DISPENSER_GUI;
    }

    private void dispenserGui() {
        if (this.isOff()) {
            return;
        }
        if (!(Auto32k.mc.field_71462_r instanceof GuiDispenser)) {
            return;
        }
        Auto32k.mc.field_71442_b.func_187098_a(Auto32k.mc.field_71439_g.field_71070_bA.field_75152_c, this.shulkerSlot, 0, ClickType.QUICK_MOVE, (EntityPlayer)Auto32k.mc.field_71439_g);
        Auto32k.mc.field_71439_g.func_71053_j();
        this.currentStep = Step.REDSTONE;
    }

    private void clickBlock(BlockPos pos) {
        if (this.isOff() || pos == null) {
            return;
        }
        Auto32k.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Auto32k.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
        Vec3d hitVec = new Vec3d((Vec3i)pos).func_72441_c(0.5, -0.5, 0.5);
        if (this.rotate.getValue().booleanValue()) {
            this.rotateToPos(null, hitVec);
        }
        EnumFacing facing = EnumFacing.UP;
        if (this.finalDispenserData != null && this.finalDispenserData.getDispenserPos() != null && this.finalDispenserData.getDispenserPos().equals((Object)pos) && pos.func_177956_o() > new BlockPos(Auto32k.mc.field_71439_g.func_174791_d()).func_177984_a().func_177956_o()) {
            facing = EnumFacing.DOWN;
        }
        BlockUtil.rightClickBlock(pos, hitVec, this.shulkerSlot == -2 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, facing, this.packet.getValue());
        Auto32k.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        Auto32k.mc.field_71467_ac = 4;
        ++this.actionsThisTick;
    }

    private void runDispenserStep() {
        if (this.isOff()) {
            return;
        }
        if (this.finalDispenserData == null || this.finalDispenserData.getDispenserPos() == null || this.finalDispenserData.getHelpingPos() == null) {
            this.resetFields();
            return;
        }
        if (this.currentStep != Step.DISPENSER && this.currentStep != Step.DISPENSER_HELPING) {
            return;
        }
        BlockPos dispenserPos = this.finalDispenserData.getDispenserPos();
        BlockPos helpingPos = this.finalDispenserData.getHelpingPos();
        if (Auto32k.mc.field_71441_e.func_180495_p(helpingPos).func_185904_a().func_76222_j()) {
            this.currentStep = Step.DISPENSER_HELPING;
            EnumFacing facing = EnumFacing.DOWN;
            boolean foundHelpingPos = false;
            for (EnumFacing enumFacing : EnumFacing.values()) {
                BlockPos position = helpingPos.func_177972_a(enumFacing);
                if (position.equals((Object)this.hopperPos) || position.equals((Object)this.hopperPos.func_177984_a()) || position.equals((Object)dispenserPos) || position.equals((Object)this.finalDispenserData.getRedStonePos()) || !(Auto32k.mc.field_71439_g.func_174818_b(position) <= MathUtil.square(this.range.getValue().floatValue())) || this.raytrace.getValue().booleanValue() && !BlockUtil.rayTracePlaceCheck(position, this.raytrace.getValue()) || Auto32k.mc.field_71441_e.func_180495_p(position).func_185904_a().func_76222_j()) continue;
                foundHelpingPos = true;
                facing = enumFacing;
                break;
            }
            if (!foundHelpingPos) {
                this.disable();
                return;
            }
            BlockPos neighbour = helpingPos.func_177972_a(facing);
            EnumFacing opposite = facing.func_176734_d();
            Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
            Auto32k.mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
            Auto32k.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Auto32k.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            if (this.rotate.getValue().booleanValue()) {
                if (this.blocksPerPlace.getValue() > 1) {
                    float[] angle = RotationUtil.getLegitRotations(hitVec);
                    if (this.extra.getValue().booleanValue()) {
                        RotationUtil.faceYawAndPitch(angle[0], angle[1]);
                    }
                } else {
                    this.rotateToPos(null, hitVec);
                }
            }
            int slot = this.preferObby.getValue() != false && this.obbySlot != -1 ? this.obbySlot : this.dispenserSlot;
            InventoryUtil.switchToHotbarSlot(slot, false);
            BlockUtil.rightClickBlock(neighbour, hitVec, slot == -2 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, opposite, this.packet.getValue());
            Auto32k.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Auto32k.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
            this.placeTimer.reset();
            ++this.actionsThisTick;
            return;
        }
        this.placeDispenserAgainstBlock(dispenserPos, helpingPos);
        ++this.actionsThisTick;
        this.currentStep = Step.CLICK_DISPENSER;
    }

    private void placeDispenserAgainstBlock(BlockPos dispenserPos, BlockPos helpingPos) {
        if (this.isOff()) {
            return;
        }
        EnumFacing facing = EnumFacing.DOWN;
        for (EnumFacing enumFacing : EnumFacing.values()) {
            BlockPos blockPos = dispenserPos.func_177972_a(enumFacing);
            if (!blockPos.equals((Object)helpingPos)) continue;
            facing = enumFacing;
            break;
        }
        EnumFacing opposite = facing.func_176734_d();
        Vec3d hitVec = new Vec3d((Vec3i)helpingPos).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        Auto32k.mc.field_71441_e.func_180495_p(helpingPos).func_177230_c();
        Auto32k.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Auto32k.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
        EnumFacing facings = EnumFacing.UP;
        if (this.rotate.getValue().booleanValue()) {
            if (this.blocksPerPlace.getValue() > 1) {
                float[] arrf = RotationUtil.getLegitRotations(hitVec);
                if (this.extra.getValue().booleanValue()) {
                    RotationUtil.faceYawAndPitch(arrf[0], arrf[1]);
                }
            } else {
                this.rotateToPos(null, hitVec);
            }
            new Vec3d((Vec3i)helpingPos).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        } else if (dispenserPos.func_177956_o() <= new BlockPos(Auto32k.mc.field_71439_g.func_174791_d()).func_177984_a().func_177956_o()) {
            for (EnumFacing enumFacing : EnumFacing.values()) {
                BlockPos position = this.hopperPos.func_177984_a().func_177972_a(enumFacing);
                if (!position.equals((Object)dispenserPos)) continue;
                facings = enumFacing;
                break;
            }
            float[] arrf = RotationUtil.simpleFacing(facings);
            this.yaw = arrf[0];
            this.pitch = arrf[1];
            this.spoof = true;
        } else {
            float[] arrf = RotationUtil.simpleFacing(facings);
            this.yaw = arrf[0];
            this.pitch = arrf[1];
            this.spoof = true;
        }
        Vec3d rotationVec = new Vec3d((Vec3i)helpingPos).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        float[] arrf = RotationUtil.simpleFacing(facings);
        float[] angle = RotationUtil.getLegitRotations(hitVec);
        if (this.superPacket.getValue().booleanValue()) {
            RotationUtil.faceYawAndPitch(this.rotate.getValue() == false ? arrf[0] : angle[0], this.rotate.getValue() == false ? arrf[1] : angle[1]);
        }
        InventoryUtil.switchToHotbarSlot(this.dispenserSlot, false);
        BlockUtil.rightClickBlock(helpingPos, rotationVec, this.dispenserSlot == -2 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, opposite, this.packet.getValue());
        Auto32k.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Auto32k.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
        this.placeTimer.reset();
        ++this.actionsThisTick;
        this.currentStep = Step.CLICK_DISPENSER;
    }

    private void runDispenserPreStep() {
        if (this.isOff()) {
            return;
        }
        if (Freecam.getInstance().isOn() && !this.freecam.getValue().booleanValue()) {
            if (this.messages.getValue().booleanValue()) {
                Command.sendMessage("\u00a7c<Auto32k> Disable Freecam.");
            }
            this.disable();
            return;
        }
        this.hopperSlot = InventoryUtil.findHotbarBlock(BlockHopper.class);
        this.shulkerSlot = InventoryUtil.findBlockSlotInventory(BlockShulkerBox.class, false, false);
        this.dispenserSlot = InventoryUtil.findHotbarBlock(BlockDispenser.class);
        this.redstoneSlot = InventoryUtil.findHotbarBlock(Blocks.field_150451_bX);
        this.obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        if (Auto32k.mc.field_71439_g.func_184592_cb().func_77973_b() instanceof ItemBlock) {
            Block block = ((ItemBlock)Auto32k.mc.field_71439_g.func_184592_cb().func_77973_b()).func_179223_d();
            if (block instanceof BlockHopper) {
                this.hopperSlot = -2;
            } else if (block instanceof BlockDispenser) {
                this.dispenserSlot = -2;
            } else if (block == Blocks.field_150451_bX) {
                this.redstoneSlot = -2;
            } else if (block instanceof BlockObsidian) {
                this.obbySlot = -2;
            }
        }
        if (this.shulkerSlot == -1 || this.hopperSlot == -1 || this.dispenserSlot == -1 || this.redstoneSlot == -1) {
            if (this.messages.getValue().booleanValue()) {
                Command.sendMessage("\u00a7c<Auto32k> Materials not found.");
            }
            this.disable();
            return;
        }
        this.finalDispenserData = this.findBestPos();
        if (this.finalDispenserData.isPlaceable()) {
            this.hopperPos = this.finalDispenserData.getHopperPos();
            this.currentStep = Auto32k.mc.field_71441_e.func_180495_p(this.hopperPos).func_177230_c() instanceof BlockHopper ? Step.DISPENSER : Step.HOPPER;
        } else {
            if (this.messages.getValue().booleanValue()) {
                Command.sendMessage("\u00a7c<Auto32k> Block not found.");
            }
            this.disable();
        }
    }

    private DispenserData findBestPos() {
        PlaceType type = this.placeType.getValue();
        this.target = EntityUtil.getClosestEnemy(this.targetRange.getValue());
        if (this.target == null) {
            type = this.placeType.getValue() == PlaceType.MOUSE ? PlaceType.MOUSE : PlaceType.CLOSE;
        }
        NonNullList positions = NonNullList.func_191196_a();
        positions.addAll(BlockUtil.getSphere(EntityUtil.getPlayerPos((EntityPlayer)Auto32k.mc.field_71439_g), this.range.getValue().floatValue(), this.range.getValue().intValue(), false, true, 0));
        DispenserData data = new DispenserData();
        switch (type) {
            case MOUSE: {
                BlockPos mousePos;
                if (Auto32k.mc.field_71476_x != null && Auto32k.mc.field_71476_x.field_72313_a == RayTraceResult.Type.BLOCK && (mousePos = Auto32k.mc.field_71476_x.func_178782_a()) != null && !(data = this.analyzePos(mousePos)).isPlaceable()) {
                    data = this.analyzePos(mousePos.func_177984_a());
                }
                if (data.isPlaceable()) {
                    return data;
                }
            }
            case CLOSE: {
                positions.sort(Comparator.comparingDouble(pos2 -> Auto32k.mc.field_71439_g.func_174818_b(pos2)));
                break;
            }
            case ENEMY: {
                positions.sort(Comparator.comparingDouble(((EntityPlayer)this.target)::func_174818_b));
                break;
            }
            case MIDDLE: {
                ArrayList<BlockPos> toRemove = new ArrayList<BlockPos>();
                NonNullList copy = NonNullList.func_191196_a();
                copy.addAll((Collection)positions);
                for (BlockPos position : copy) {
                    double difference = Auto32k.mc.field_71439_g.func_174818_b(position) - this.target.func_174818_b(position);
                    if (!(difference > 1.0) && !(difference < -1.0)) continue;
                    toRemove.add(position);
                }
                copy.removeAll(toRemove);
                if (copy.isEmpty()) {
                    copy.addAll((Collection)positions);
                }
                copy.sort(Comparator.comparingDouble(pos2 -> Auto32k.mc.field_71439_g.func_174818_b(pos2)));
                break;
            }
            case FAR: {
                positions.sort(Comparator.comparingDouble(pos2 -> -this.target.func_174818_b(pos2)));
                break;
            }
            case SAFE: {
                positions.sort(Comparator.comparingInt(pos2 -> -this.safetyFactor((BlockPos)pos2)));
            }
        }
        data = this.findData((NonNullList<BlockPos>)positions);
        return data;
    }

    private DispenserData findData(NonNullList<BlockPos> positions) {
        for (BlockPos position : positions) {
            DispenserData data = this.analyzePos(position);
            if (!data.isPlaceable()) continue;
            return data;
        }
        return new DispenserData();
    }

    private DispenserData analyzePos(BlockPos pos) {
        DispenserData data = new DispenserData(pos);
        if (pos == null) {
            return data;
        }
        if (this.isGoodMaterial(Auto32k.mc.field_71441_e.func_180495_p(pos).func_177230_c(), this.onOtherHoppers.getValue()) || this.isGoodMaterial(Auto32k.mc.field_71441_e.func_180495_p(pos.func_177984_a()).func_177230_c(), false)) {
            return data;
        }
        if (this.raytrace.getValue().booleanValue() && !BlockUtil.rayTracePlaceCheck(pos, this.raytrace.getValue())) {
            return data;
        }
        if (this.badEntities(pos) || this.badEntities(pos.func_177984_a())) {
            return data;
        }
        if (this.hasAdjancedRedstone(pos)) {
            return data;
        }
        if (!this.findFacing(pos)) {
            return data;
        }
        BlockPos[] otherPositions = this.checkForDispenserPos(pos);
        if (otherPositions[0] == null || otherPositions[1] == null || otherPositions[2] == null) {
            return data;
        }
        data.setDispenserPos(otherPositions[0]);
        data.setRedStonePos(otherPositions[1]);
        data.setHelpingPos(otherPositions[2]);
        data.setPlaceable();
        return data;
    }

    private boolean findFacing(BlockPos pos) {
        boolean foundFacing = false;
        for (EnumFacing facing : EnumFacing.values()) {
            if (facing == EnumFacing.UP) continue;
            if (facing == EnumFacing.DOWN && this.antiHopper.getValue().booleanValue() && Auto32k.mc.field_71441_e.func_180495_p(pos.func_177972_a(facing)).func_177230_c() == Blocks.field_150438_bZ) {
                foundFacing = false;
                break;
            }
            if (Auto32k.mc.field_71441_e.func_180495_p(pos.func_177972_a(facing)).func_185904_a().func_76222_j() || this.antiHopper.getValue().booleanValue() && Auto32k.mc.field_71441_e.func_180495_p(pos.func_177972_a(facing)).func_177230_c() == Blocks.field_150438_bZ) continue;
            foundFacing = true;
        }
        return foundFacing;
    }

    private BlockPos[] checkForDispenserPos(BlockPos posIn) {
        BlockPos[] pos;
        block12: {
            List<BlockPos> possiblePositions;
            block11: {
                pos = new BlockPos[3];
                BlockPos playerPos = new BlockPos(Auto32k.mc.field_71439_g.func_174791_d());
                if (posIn.func_177956_o() < playerPos.func_177977_b().func_177956_o()) {
                    return pos;
                }
                possiblePositions = this.getDispenserPositions(posIn);
                if (posIn.func_177956_o() < playerPos.func_177956_o()) {
                    possiblePositions.remove((Object)posIn.func_177984_a().func_177984_a());
                } else if (posIn.func_177956_o() > playerPos.func_177956_o()) {
                    possiblePositions.remove((Object)posIn.func_177976_e().func_177984_a());
                    possiblePositions.remove((Object)posIn.func_177978_c().func_177984_a());
                    possiblePositions.remove((Object)posIn.func_177968_d().func_177984_a());
                    possiblePositions.remove((Object)posIn.func_177974_f().func_177984_a());
                }
                if (!this.rotate.getValue().booleanValue() && !this.simulate.getValue().booleanValue()) break block11;
                possiblePositions.sort(Comparator.comparingDouble(pos2 -> -Auto32k.mc.field_71439_g.func_174818_b(pos2)));
                BlockPos posToCheck = possiblePositions.get(0);
                if (this.isGoodMaterial(Auto32k.mc.field_71441_e.func_180495_p(posToCheck).func_177230_c(), false)) {
                    return pos;
                }
                if (Auto32k.mc.field_71439_g.func_174818_b(posToCheck) > MathUtil.square(this.range.getValue().floatValue())) {
                    return pos;
                }
                if (this.raytrace.getValue().booleanValue() && !BlockUtil.rayTracePlaceCheck(posToCheck, this.raytrace.getValue())) {
                    return pos;
                }
                if (this.badEntities(posToCheck)) {
                    return pos;
                }
                if (this.hasAdjancedRedstone(posToCheck)) {
                    return pos;
                }
                List<BlockPos> possibleRedStonePositions = this.checkRedStone(posToCheck, posIn);
                if (possiblePositions.isEmpty()) {
                    return pos;
                }
                BlockPos[] helpingStuff = this.getHelpingPos(posToCheck, posIn, possibleRedStonePositions);
                if (helpingStuff == null || helpingStuff[0] == null || helpingStuff[1] == null) break block12;
                pos[0] = posToCheck;
                pos[1] = helpingStuff[1];
                pos[2] = helpingStuff[0];
                break block12;
            }
            possiblePositions.removeIf(position -> Auto32k.mc.field_71439_g.func_174818_b(position) > MathUtil.square(this.range.getValue().floatValue()));
            possiblePositions.removeIf(position -> this.isGoodMaterial(Auto32k.mc.field_71441_e.func_180495_p(position).func_177230_c(), false));
            possiblePositions.removeIf(position -> this.raytrace.getValue() != false && !BlockUtil.rayTracePlaceCheck(position, this.raytrace.getValue()));
            possiblePositions.removeIf(this::badEntities);
            possiblePositions.removeIf(this::hasAdjancedRedstone);
            for (BlockPos position2 : possiblePositions) {
                BlockPos[] helpingStuff;
                List<BlockPos> possibleRedStonePositions = this.checkRedStone(position2, posIn);
                if (possiblePositions.isEmpty() || (helpingStuff = this.getHelpingPos(position2, posIn, possibleRedStonePositions)) == null || helpingStuff[0] == null || helpingStuff[1] == null) continue;
                pos[0] = position2;
                pos[1] = helpingStuff[1];
                pos[2] = helpingStuff[0];
                break;
            }
        }
        return pos;
    }

    private List<BlockPos> checkRedStone(BlockPos pos, BlockPos hopperPos) {
        ArrayList<BlockPos> toCheck = new ArrayList<BlockPos>();
        for (EnumFacing facing : EnumFacing.values()) {
            toCheck.add(pos.func_177972_a(facing));
        }
        toCheck.removeIf(position -> position.equals((Object)hopperPos.func_177984_a()));
        toCheck.removeIf(position -> Auto32k.mc.field_71439_g.func_174818_b(position) > MathUtil.square(this.range.getValue().floatValue()));
        toCheck.removeIf(position -> this.isGoodMaterial(Auto32k.mc.field_71441_e.func_180495_p(position).func_177230_c(), false));
        toCheck.removeIf(position -> this.raytrace.getValue() != false && !BlockUtil.rayTracePlaceCheck(position, this.raytrace.getValue()));
        toCheck.removeIf(this::badEntities);
        toCheck.sort(Comparator.comparingDouble(pos2 -> Auto32k.mc.field_71439_g.func_174818_b(pos2)));
        return toCheck;
    }

    private boolean hasAdjancedRedstone(BlockPos pos) {
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos position = pos.func_177972_a(facing);
            if (Auto32k.mc.field_71441_e.func_180495_p(position).func_177230_c() != Blocks.field_150451_bX && Auto32k.mc.field_71441_e.func_180495_p(position).func_177230_c() != Blocks.field_150429_aA) continue;
            return true;
        }
        return false;
    }

    private List<BlockPos> getDispenserPositions(BlockPos pos) {
        ArrayList<BlockPos> list = new ArrayList<BlockPos>();
        for (EnumFacing facing : EnumFacing.values()) {
            if (facing == EnumFacing.DOWN) continue;
            list.add(pos.func_177972_a(facing).func_177984_a());
        }
        return list;
    }

    private BlockPos[] getHelpingPos(BlockPos pos, BlockPos hopperPos, List<BlockPos> redStonePositions) {
        BlockPos[] result = new BlockPos[2];
        ArrayList<BlockPos> possiblePositions = new ArrayList<BlockPos>();
        if (redStonePositions.isEmpty()) {
            return null;
        }
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos facingPos = pos.func_177972_a(facing);
            if (facingPos.equals((Object)hopperPos) || facingPos.equals((Object)hopperPos.func_177984_a())) continue;
            if (!Auto32k.mc.field_71441_e.func_180495_p(facingPos).func_185904_a().func_76222_j()) {
                if (redStonePositions.contains((Object)facingPos)) {
                    redStonePositions.remove((Object)facingPos);
                    if (redStonePositions.isEmpty()) {
                        redStonePositions.add(facingPos);
                        continue;
                    }
                    result[0] = facingPos;
                    result[1] = redStonePositions.get(0);
                    return result;
                }
                result[0] = facingPos;
                result[1] = redStonePositions.get(0);
                return result;
            }
            for (EnumFacing facing1 : EnumFacing.values()) {
                BlockPos facingPos1 = facingPos.func_177972_a(facing1);
                if (facingPos1.equals((Object)hopperPos) || facingPos1.equals((Object)hopperPos.func_177984_a()) || facingPos1.equals((Object)pos) || Auto32k.mc.field_71441_e.func_180495_p(facingPos1).func_185904_a().func_76222_j()) continue;
                if (redStonePositions.contains((Object)facingPos)) {
                    redStonePositions.remove((Object)facingPos);
                    if (redStonePositions.isEmpty()) {
                        redStonePositions.add(facingPos);
                        continue;
                    }
                    possiblePositions.add(facingPos);
                    continue;
                }
                possiblePositions.add(facingPos);
            }
        }
        possiblePositions.removeIf(position -> Auto32k.mc.field_71439_g.func_174818_b(position) > MathUtil.square(this.range.getValue().floatValue()));
        possiblePositions.sort(Comparator.comparingDouble(position -> Auto32k.mc.field_71439_g.func_174818_b(position)));
        if (!possiblePositions.isEmpty()) {
            redStonePositions.remove(possiblePositions.get(0));
            if (!redStonePositions.isEmpty()) {
                result[0] = (BlockPos)possiblePositions.get(0);
                result[1] = redStonePositions.get(0);
            }
            return result;
        }
        return null;
    }

    private void rotateToPos(BlockPos pos, Vec3d vec3d) {
        float[] angle = vec3d == null ? MathUtil.calcAngle(Auto32k.mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((double)((float)pos.func_177958_n() + 0.5f), (double)((float)pos.func_177956_o() - 0.5f), (double)((float)pos.func_177952_p() + 0.5f))) : RotationUtil.getLegitRotations(vec3d);
        this.yaw = angle[0];
        this.pitch = angle[1];
        this.spoof = true;
    }

    private boolean isGoodMaterial(Block block, boolean allowHopper) {
        return !(block instanceof BlockAir) && !(block instanceof BlockLiquid) && !(block instanceof BlockTallGrass) && !(block instanceof BlockFire) && !(block instanceof BlockDeadBush) && !(block instanceof BlockSnow) && (!allowHopper || !(block instanceof BlockHopper));
    }

    private void resetFields() {
        this.shouldDisable = false;
        this.spoof = false;
        this.switching = false;
        this.shulkerSlot = -1;
        this.hopperSlot = -1;
        this.hopperPos = null;
        this.target = null;
        this.currentStep = Step.PRE;
        this.obbySlot = -1;
        this.dispenserSlot = -1;
        this.redstoneSlot = -1;
        this.finalDispenserData = null;
        this.actionsThisTick = 0;
    }

    private boolean badEntities(BlockPos pos) {
        for (Entity entity : Auto32k.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos))) {
            if (entity instanceof EntityExpBottle || entity instanceof EntityItem || entity instanceof EntityXPOrb) continue;
            return true;
        }
        return false;
    }

    private int safetyFactor(BlockPos pos) {
        return this.safety(pos) + this.safety(pos.func_177984_a());
    }

    private int safety(BlockPos pos) {
        int safety = 0;
        for (EnumFacing facing : EnumFacing.values()) {
            if (Auto32k.mc.field_71441_e.func_180495_p(pos.func_177972_a(facing)).func_185904_a().func_76222_j()) continue;
            ++safety;
        }
        return safety;
    }

    public static class DispenserData {
        private BlockPos dispenserPos;
        private BlockPos redStonePos;
        private BlockPos hopperPos;
        private BlockPos helpingPos;

        public DispenserData() {
        }

        public DispenserData(BlockPos pos) {
            this.hopperPos = pos;
        }

        public boolean isPlaceable() {
            return this.dispenserPos != null && this.hopperPos != null && this.redStonePos != null && this.helpingPos != null;
        }

        public void setPlaceable() {
        }

        public BlockPos getDispenserPos() {
            return this.dispenserPos;
        }

        public void setDispenserPos(BlockPos dispenserPos) {
            this.dispenserPos = dispenserPos;
        }

        public BlockPos getRedStonePos() {
            return this.redStonePos;
        }

        public void setRedStonePos(BlockPos redStonePos) {
            this.redStonePos = redStonePos;
        }

        public BlockPos getHopperPos() {
            return this.hopperPos;
        }

        public BlockPos getHelpingPos() {
            return this.helpingPos;
        }

        public void setHelpingPos(BlockPos helpingPos) {
            this.helpingPos = helpingPos;
        }
    }

    public static enum PlaceType {
        MOUSE,
        CLOSE,
        ENEMY,
        MIDDLE,
        FAR,
        SAFE;

    }

    public static enum Mode {
        NORMAL,
        DISPENSER;

    }

    public static enum Step {
        PRE,
        HOPPER,
        SHULKER,
        CLICKHOPPER,
        HOPPERGUI,
        DISPENSER_HELPING,
        DISPENSER_GUI,
        DISPENSER,
        CLICK_DISPENSER,
        REDSTONE;

    }
}

