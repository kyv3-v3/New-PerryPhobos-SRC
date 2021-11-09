//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.modules.combat;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.earth.phobos.event.events.*;
import java.awt.*;
import me.earth.phobos.features.modules.client.*;
import java.util.*;
import me.earth.phobos.features.modules.player.*;
import me.earth.phobos.util.*;
import net.minecraft.block.*;
import me.earth.phobos.features.command.*;
import me.earth.phobos.*;
import net.minecraft.util.*;

public class AutoTrap extends Module
{
    public static boolean isPlacing;
    private final Setting<Boolean> server;
    private final Setting<Integer> delay;
    private final Setting<Integer> blocksPerPlace;
    private final Setting<Double> targetRange;
    private final Setting<Double> range;
    private final Setting<TargetMode> targetMode;
    private final Setting<InventoryUtil.Switch> switchMode;
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> raytrace;
    private final Setting<Pattern> pattern;
    private final Setting<Integer> extend;
    private final Setting<Boolean> antiScaffold;
    private final Setting<Boolean> antiStep;
    private final Setting<Boolean> face;
    private final Setting<Boolean> legs;
    private final Setting<Boolean> platform;
    private final Setting<Boolean> antiDrop;
    private final Setting<Double> speed;
    private final Setting<Boolean> antiSelf;
    private final Setting<Integer> eventMode;
    private final Setting<Boolean> freecam;
    private final Setting<Boolean> info;
    private final Setting<Boolean> entityCheck;
    private final Setting<Boolean> noScaffoldExtend;
    private final Setting<Boolean> disable;
    private final Setting<Boolean> packet;
    private final Setting<Boolean> airPacket;
    private final Setting<Integer> retryer;
    private final Setting<Boolean> endPortals;
    private final Setting<Boolean> render;
    public final Setting<Boolean> colorSync;
    public final Setting<Boolean> box;
    private final Setting<Integer> boxAlpha;
    public final Setting<Boolean> outline;
    public final Setting<Boolean> customOutline;
    private final Setting<Integer> cRed;
    private final Setting<Integer> cGreen;
    private final Setting<Integer> cBlue;
    private final Setting<Integer> cAlpha;
    private final Setting<Float> lineWidth;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    private final TimerUtil timer;
    private final Map<BlockPos, Integer> retries;
    private final TimerUtil retryTimer;
    private final Map<BlockPos, IBlockState> toAir;
    public EntityPlayer target;
    private boolean didPlace;
    private boolean switchedItem;
    private boolean isSneaking;
    private int lastHotbarSlot;
    private int placements;
    private boolean smartRotate;
    private BlockPos startPos;
    private List<Vec3d> currentPlaceList;
    
    public AutoTrap() {
        super("AutoTrap", "Traps other players.", Category.COMBAT, true, false, false);
        this.server = (Setting<Boolean>)this.register(new Setting("Server", (T)false));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay/Place", (T)50, (T)0, (T)250));
        this.blocksPerPlace = (Setting<Integer>)this.register(new Setting("Block/Place", (T)8, (T)1, (T)30));
        this.targetRange = (Setting<Double>)this.register(new Setting("TargetRange", (T)10.0, (T)0.0, (T)20.0));
        this.range = (Setting<Double>)this.register(new Setting("PlaceRange", (T)6.0, (T)0.0, (T)10.0));
        this.targetMode = (Setting<TargetMode>)this.register(new Setting("Target", (T)TargetMode.CLOSEST));
        this.switchMode = (Setting<InventoryUtil.Switch>)this.register(new Setting("Switch", (T)InventoryUtil.Switch.NORMAL));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (T)true));
        this.raytrace = (Setting<Boolean>)this.register(new Setting("Raytrace", (T)false));
        this.pattern = (Setting<Pattern>)this.register(new Setting("Pattern", (T)Pattern.STATIC));
        this.extend = (Setting<Integer>)this.register(new Setting("Extend", (T)4, (T)1, (T)4, v -> this.pattern.getValue() != Pattern.STATIC, "Extending the Trap."));
        this.antiScaffold = (Setting<Boolean>)this.register(new Setting("AntiScaffold", (T)false));
        this.antiStep = (Setting<Boolean>)this.register(new Setting("AntiStep", (T)false));
        this.face = (Setting<Boolean>)this.register(new Setting("Face", (T)true));
        this.legs = (Setting<Boolean>)this.register(new Setting("Legs", (T)false, v -> this.pattern.getValue() != Pattern.OPEN));
        this.platform = (Setting<Boolean>)this.register(new Setting("Platform", (T)false, v -> this.pattern.getValue() != Pattern.OPEN));
        this.antiDrop = (Setting<Boolean>)this.register(new Setting("AntiDrop", (T)false));
        this.speed = (Setting<Double>)this.register(new Setting("Speed", (T)10.0, (T)0.0, (T)30.0));
        this.antiSelf = (Setting<Boolean>)this.register(new Setting("AntiSelf", (T)false));
        this.eventMode = (Setting<Integer>)this.register(new Setting("Updates", (T)3, (T)1, (T)3));
        this.freecam = (Setting<Boolean>)this.register(new Setting("Freecam", (T)false));
        this.info = (Setting<Boolean>)this.register(new Setting("Info", (T)false));
        this.entityCheck = (Setting<Boolean>)this.register(new Setting("NoBlock", (T)true));
        this.noScaffoldExtend = (Setting<Boolean>)this.register(new Setting("NoScaffoldExtend", (T)false));
        this.disable = (Setting<Boolean>)this.register(new Setting("TSelfMove", (T)false));
        this.packet = (Setting<Boolean>)this.register(new Setting("Packet", (T)false));
        this.airPacket = (Setting<Boolean>)this.register(new Setting("AirPacket", (T)false, v -> this.packet.getValue()));
        this.retryer = (Setting<Integer>)this.register(new Setting("Retries", (T)4, (T)1, (T)15));
        this.endPortals = (Setting<Boolean>)this.register(new Setting("EndPortals", (T)false));
        this.render = (Setting<Boolean>)this.register(new Setting("Render", (T)true));
        this.colorSync = (Setting<Boolean>)this.register(new Setting("Sync", (T)false, v -> this.render.getValue()));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", (T)false, v -> this.render.getValue()));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", (T)125, (T)0, (T)255, v -> this.box.getValue() && this.render.getValue()));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", (T)true, v -> this.render.getValue()));
        this.customOutline = (Setting<Boolean>)this.register(new Setting("CustomLine", (T)false, v -> this.outline.getValue() && this.render.getValue()));
        this.cRed = (Setting<Integer>)this.register(new Setting("OL-Red", (T)255, (T)0, (T)255, v -> this.customOutline.getValue() && this.outline.getValue() && this.render.getValue()));
        this.cGreen = (Setting<Integer>)this.register(new Setting("OL-Green", (T)255, (T)0, (T)255, v -> this.customOutline.getValue() && this.outline.getValue() && this.render.getValue()));
        this.cBlue = (Setting<Integer>)this.register(new Setting("OL-Blue", (T)255, (T)0, (T)255, v -> this.customOutline.getValue() && this.outline.getValue() && this.render.getValue()));
        this.cAlpha = (Setting<Integer>)this.register(new Setting("OL-Alpha", (T)255, (T)0, (T)255, v -> this.customOutline.getValue() && this.outline.getValue() && this.render.getValue()));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (T)1.0f, (T)0.1f, (T)5.0f, v -> this.outline.getValue() && this.render.getValue()));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)0, (T)0, (T)255, v -> this.render.getValue()));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)255, (T)0, (T)255, v -> this.render.getValue()));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)0, (T)0, (T)255, v -> this.render.getValue()));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)255, (T)0, (T)255, v -> this.render.getValue()));
        this.timer = new TimerUtil();
        this.retries = new HashMap<BlockPos, Integer>();
        this.retryTimer = new TimerUtil();
        this.toAir = new HashMap<BlockPos, IBlockState>();
        this.currentPlaceList = new ArrayList<Vec3d>();
    }
    
    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            this.disable();
            return;
        }
        this.toAir.clear();
        this.startPos = EntityUtil.getRoundedBlockPos((Entity)AutoTrap.mc.player);
        this.lastHotbarSlot = AutoTrap.mc.player.inventory.currentItem;
        this.retries.clear();
        if (this.shouldServer()) {
            AutoTrap.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
            AutoTrap.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module AutoTrap set Enabled true"));
        }
    }
    
    @Override
    public void onLogout() {
        this.disable();
    }
    
    @Override
    public void onTick() {
        if (this.eventMode.getValue() == 3) {
            this.smartRotate = false;
            this.doTrap();
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && this.eventMode.getValue() == 2) {
            this.smartRotate = (this.rotate.getValue() && this.blocksPerPlace.getValue() == 1);
            this.doTrap();
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.eventMode.getValue() == 1) {
            this.smartRotate = false;
            this.doTrap();
        }
    }
    
    @Override
    public String getDisplayInfo() {
        if (this.info.getValue() && this.target != null) {
            return this.target.getName();
        }
        return null;
    }
    
    @Override
    public void onDisable() {
        if (fullNullCheck()) {
            return;
        }
        if (this.shouldServer()) {
            AutoTrap.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
            AutoTrap.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module AutoTrap set Enabled false"));
            return;
        }
        AutoTrap.isPlacing = false;
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        this.switchItem(true);
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (this.render.getValue() && this.currentPlaceList != null) {
            for (final Vec3d vec : this.currentPlaceList) {
                final BlockPos pos = new BlockPos(vec);
                if (!(AutoTrap.mc.world.getBlockState(pos).getBlock() instanceof BlockAir)) {
                    continue;
                }
                RenderUtil.drawBoxESP(pos, ((boolean)this.colorSync.getValue()) ? Colors.INSTANCE.getCurrentColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.customOutline.getValue(), new Color(this.cRed.getValue(), this.cGreen.getValue(), this.cBlue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
            }
        }
    }
    
    private boolean shouldServer() {
        return PingBypass.getInstance().isConnected() && this.server.getValue();
    }
    
    private void doTrap() {
        if (this.shouldServer() || this.check()) {
            return;
        }
        switch (this.pattern.getValue()) {
            case STATIC: {
                this.doStaticTrap();
                break;
            }
            case SMART:
            case OPEN: {
                this.doSmartTrap();
                break;
            }
        }
        if (this.packet.getValue() && this.airPacket.getValue()) {
            for (final Map.Entry<BlockPos, IBlockState> entry : this.toAir.entrySet()) {
                AutoTrap.mc.world.setBlockState((BlockPos)entry.getKey(), (IBlockState)entry.getValue());
            }
            this.toAir.clear();
        }
        if (this.didPlace) {
            this.timer.reset();
        }
    }
    
    private void doSmartTrap() {
        final List<Vec3d> placeTargets = EntityUtil.getUntrappedBlocksExtended(this.extend.getValue(), this.target, this.antiScaffold.getValue(), this.antiStep.getValue(), this.legs.getValue(), this.platform.getValue(), this.antiDrop.getValue(), this.raytrace.getValue(), this.noScaffoldExtend.getValue(), this.face.getValue());
        this.placeList(placeTargets);
        this.currentPlaceList = placeTargets;
    }
    
    private void doStaticTrap() {
        final List<Vec3d> placeTargets = EntityUtil.targets(this.target.getPositionVector(), this.antiScaffold.getValue(), this.antiStep.getValue(), this.legs.getValue(), this.platform.getValue(), this.antiDrop.getValue(), this.raytrace.getValue(), this.face.getValue());
        this.placeList(placeTargets);
        this.currentPlaceList = placeTargets;
    }
    
    private void placeList(final List<Vec3d> list) {
        list.sort((vec3d, vec3d2) -> Double.compare(AutoTrap.mc.player.getDistanceSq(vec3d2.x, vec3d2.y, vec3d2.z), AutoTrap.mc.player.getDistanceSq(vec3d.x, vec3d.y, vec3d.z)));
        list.sort(Comparator.comparingDouble(vec3d -> vec3d.y));
        for (final Vec3d vec3d3 : list) {
            final BlockPos position = new BlockPos(vec3d3);
            final int placeability = BlockUtil.isPositionPlaceable(position, this.raytrace.getValue());
            if (this.entityCheck.getValue() && placeability == 1 && (this.switchMode.getValue() == InventoryUtil.Switch.SILENT || (BlockTweaks.getINSTANCE().isOn() && BlockTweaks.getINSTANCE().noBlock.getValue())) && (this.retries.get(position) == null || this.retries.get(position) < this.retryer.getValue())) {
                this.placeBlock(position);
                this.retries.put(position, (this.retries.get(position) == null) ? 1 : (this.retries.get(position) + 1));
                this.retryTimer.reset();
            }
            else {
                if (placeability != 3) {
                    continue;
                }
                if (this.antiSelf.getValue() && MathUtil.areVec3dsAligned(AutoTrap.mc.player.getPositionVector(), vec3d3)) {
                    continue;
                }
                this.placeBlock(position);
            }
        }
    }
    
    private boolean check() {
        AutoTrap.isPlacing = false;
        this.didPlace = false;
        this.placements = 0;
        int obbySlot;
        if (this.endPortals.getValue()) {
            obbySlot = InventoryUtil.findHotbarBlock(BlockEndPortalFrame.class);
            if (obbySlot == -1) {
                obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            }
        }
        else {
            obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        }
        if (this.isOff()) {
            return true;
        }
        if (this.disable.getValue() && this.startPos != null && !this.startPos.equals((Object)EntityUtil.getRoundedBlockPos((Entity)AutoTrap.mc.player))) {
            this.disable();
            return true;
        }
        if (this.retryTimer.passedMs(2000L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        if (obbySlot == -1) {
            if (this.switchMode.getValue() != InventoryUtil.Switch.NONE) {
                if (this.info.getValue()) {
                    Command.sendMessage("<" + this.getDisplayName() + "> §cYou are out of Obsidian.");
                }
                this.disable();
            }
            return true;
        }
        if (AutoTrap.mc.player.inventory.currentItem != this.lastHotbarSlot && AutoTrap.mc.player.inventory.currentItem != obbySlot) {
            this.lastHotbarSlot = AutoTrap.mc.player.inventory.currentItem;
        }
        this.switchItem(true);
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        this.target = this.getTarget(this.targetRange.getValue(), this.targetMode.getValue() == TargetMode.UNTRAPPED);
        return this.target == null || (Phobos.moduleManager.isModuleEnabled("Freecam") && !this.freecam.getValue()) || !this.timer.passedMs(this.delay.getValue()) || (this.switchMode.getValue() == InventoryUtil.Switch.NONE && AutoTrap.mc.player.inventory.currentItem != InventoryUtil.findHotbarBlock(BlockObsidian.class));
    }
    
    private EntityPlayer getTarget(final double range, final boolean trapped) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (final EntityPlayer player : AutoTrap.mc.world.playerEntities) {
            if (!EntityUtil.isntValid((Entity)player, range) && (this.pattern.getValue() != Pattern.STATIC || !trapped || !EntityUtil.isTrapped(player, this.antiScaffold.getValue(), this.antiStep.getValue(), this.legs.getValue(), this.platform.getValue(), this.antiDrop.getValue(), this.face.getValue())) && (this.pattern.getValue() == Pattern.STATIC || !trapped || !EntityUtil.isTrappedExtended(this.extend.getValue(), player, this.antiScaffold.getValue(), this.antiStep.getValue(), this.legs.getValue(), this.platform.getValue(), this.antiDrop.getValue(), this.raytrace.getValue(), this.noScaffoldExtend.getValue(), this.face.getValue())) && (!EntityUtil.getRoundedBlockPos((Entity)AutoTrap.mc.player).equals((Object)EntityUtil.getRoundedBlockPos((Entity)player)) || !this.antiSelf.getValue())) {
                if (Phobos.speedManager.getPlayerSpeed(player) > this.speed.getValue()) {
                    continue;
                }
                if (target == null) {
                    target = player;
                    distance = AutoTrap.mc.player.getDistanceSq((Entity)player);
                }
                else {
                    if (AutoTrap.mc.player.getDistanceSq((Entity)player) >= distance) {
                        continue;
                    }
                    target = player;
                    distance = AutoTrap.mc.player.getDistanceSq((Entity)player);
                }
            }
        }
        return target;
    }
    
    private void placeBlock(final BlockPos pos) {
        if (this.placements < this.blocksPerPlace.getValue() && AutoTrap.mc.player.getDistanceSq(pos) <= MathUtil.square(this.range.getValue()) && this.switchItem(false)) {
            AutoTrap.isPlacing = true;
            if (this.airPacket.getValue() && this.packet.getValue()) {
                this.toAir.put(pos, AutoTrap.mc.world.getBlockState(pos));
            }
            this.isSneaking = (this.smartRotate ? BlockUtil.placeBlockSmartRotate(pos, EnumHand.MAIN_HAND, true, !this.airPacket.getValue() && this.packet.getValue(), this.isSneaking) : BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, this.rotate.getValue(), !this.airPacket.getValue() && this.packet.getValue(), this.isSneaking));
            this.didPlace = true;
            ++this.placements;
        }
    }
    
    private boolean switchItem(final boolean back) {
        final boolean[] value = InventoryUtil.switchItem(back, this.lastHotbarSlot, this.switchedItem, this.switchMode.getValue(), (Class)((this.endPortals.getValue() && InventoryUtil.findHotbarBlock(BlockEndPortalFrame.class) != -1) ? BlockEndPortalFrame.class : BlockObsidian.class));
        this.switchedItem = value[0];
        return value[1];
    }
    
    public enum TargetMode
    {
        CLOSEST, 
        UNTRAPPED;
    }
    
    public enum Pattern
    {
        STATIC, 
        SMART, 
        OPEN;
    }
}
