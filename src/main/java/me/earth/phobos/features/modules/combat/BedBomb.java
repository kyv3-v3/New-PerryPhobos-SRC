



package me.earth.phobos.features.modules.combat;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import com.google.common.util.concurrent.*;
import java.util.concurrent.atomic.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.earth.phobos.features.modules.client.*;
import me.earth.phobos.event.events.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.inventory.*;
import net.minecraft.network.play.client.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.*;
import me.earth.phobos.util.*;
import net.minecraft.init.*;
import me.earth.phobos.*;
import net.minecraft.block.*;
import java.util.*;
import java.util.stream.*;
import net.minecraft.block.state.*;

public class BedBomb extends Module
{
    private final Setting<Boolean> server;
    private final Setting<Boolean> place;
    private final Setting<Integer> placeDelay;
    private final Setting<Float> placeRange;
    private final Setting<Boolean> extraPacket;
    private final Setting<Boolean> packet;
    private final Setting<Boolean> explode;
    private final Setting<BreakLogic> breakMode;
    private final Setting<Integer> breakDelay;
    private final Setting<Float> breakRange;
    private final Setting<Float> minDamage;
    private final Setting<Float> range;
    private final Setting<Boolean> suicide;
    private final Setting<Boolean> removeTiles;
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> oneDot15;
    private final Setting<Logic> logic;
    private final Setting<Boolean> craft;
    private final Setting<Boolean> placeCraftingTable;
    private final Setting<Boolean> openCraftingTable;
    private final Setting<Boolean> craftTable;
    private final Setting<Float> tableRange;
    private final Setting<Integer> craftDelay;
    private final Setting<Integer> tableSlot;
    private final Setting<Boolean> sslot;
    private final TimerUtil breakTimer;
    private final TimerUtil placeTimer;
    private final TimerUtil craftTimer;
    private final AtomicDouble yaw;
    private final AtomicDouble pitch;
    private final AtomicBoolean shouldRotate;
    private final int lastCraftStage = -1;
    private EntityPlayer target;
    private boolean sendRotationPacket;
    private boolean one;
    private boolean two;
    private boolean three;
    private boolean four;
    private boolean five;
    private boolean six;
    private BlockPos maxPos;
    private boolean shouldCraft;
    private int craftStage;
    private int bedSlot;
    private BlockPos finalPos;
    private EnumFacing finalFacing;
    
    public BedBomb() {
        super("BedBomb",  "AutoPlace and Break for beds.",  Category.COMBAT,  true,  false,  false);
        this.server = (Setting<Boolean>)this.register(new Setting("Server", false));
        this.place = (Setting<Boolean>)this.register(new Setting("Place", false));
        this.placeDelay = (Setting<Integer>)this.register(new Setting("Placedelay", 50, 0, 500,  v -> this.place.getValue()));
        this.placeRange = (Setting<Float>)this.register(new Setting("PlaceRange", 6.0f, 1.0f, 10.0f,  v -> this.place.getValue()));
        this.extraPacket = (Setting<Boolean>)this.register(new Setting("InsanePacket", false,  v -> this.place.getValue()));
        this.packet = (Setting<Boolean>)this.register(new Setting("Packet", false,  v -> this.place.getValue()));
        this.explode = (Setting<Boolean>)this.register(new Setting("Break", true));
        this.breakMode = (Setting<BreakLogic>)this.register(new Setting("BreakMode", BreakLogic.ALL,  v -> this.explode.getValue()));
        this.breakDelay = (Setting<Integer>)this.register(new Setting("Breakdelay", 50, 0, 500,  v -> this.explode.getValue()));
        this.breakRange = (Setting<Float>)this.register(new Setting("BreakRange", 6.0f, 1.0f, 10.0f,  v -> this.explode.getValue()));
        this.minDamage = (Setting<Float>)this.register(new Setting("MinDamage", 5.0f, 1.0f, 36.0f,  v -> this.explode.getValue()));
        this.range = (Setting<Float>)this.register(new Setting("Range", 10.0f, 1.0f, 12.0f,  v -> this.explode.getValue()));
        this.suicide = (Setting<Boolean>)this.register(new Setting("Suicide", false,  v -> this.explode.getValue()));
        this.removeTiles = (Setting<Boolean>)this.register(new Setting("RemoveTiles", false));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", false));
        this.oneDot15 = (Setting<Boolean>)this.register(new Setting("1.15", false));
        this.logic = (Setting<Logic>)this.register(new Setting("Logic", Logic.BREAKPLACE,  v -> this.place.getValue() && this.explode.getValue()));
        this.craft = (Setting<Boolean>)this.register(new Setting("Craft", false));
        this.placeCraftingTable = (Setting<Boolean>)this.register(new Setting("PlaceTable", false,  v -> this.craft.getValue()));
        this.openCraftingTable = (Setting<Boolean>)this.register(new Setting("OpenTable", false,  v -> this.craft.getValue()));
        this.craftTable = (Setting<Boolean>)this.register(new Setting("CraftTable", false,  v -> this.craft.getValue()));
        this.tableRange = (Setting<Float>)this.register(new Setting("TableRange", 6.0f, 1.0f, 10.0f,  v -> this.craft.getValue()));
        this.craftDelay = (Setting<Integer>)this.register(new Setting("CraftDelay", 4, 1, 10,  v -> this.craft.getValue()));
        this.tableSlot = (Setting<Integer>)this.register(new Setting("TableSlot", 8, 0, 8,  v -> this.craft.getValue()));
        this.sslot = (Setting<Boolean>)this.register(new Setting("S-Slot", false));
        this.breakTimer = new TimerUtil();
        this.placeTimer = new TimerUtil();
        this.craftTimer = new TimerUtil();
        this.yaw = new AtomicDouble(-1.0);
        this.pitch = new AtomicDouble(-1.0);
        this.shouldRotate = new AtomicBoolean(false);
        this.bedSlot = -1;
    }
    
    @Override
    public void onEnable() {
        if (!fullNullCheck() && this.shouldServer()) {
            BedBomb.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
            BedBomb.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module BedBomb set Enabled true"));
        }
    }
    
    @Override
    public void onDisable() {
        if (!fullNullCheck() && this.shouldServer()) {
            BedBomb.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
            BedBomb.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module BedBomb set Enabled false"));
            if (this.sslot.getValue()) {
                BedBomb.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(BedBomb.mc.player.inventory.currentItem));
            }
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketEvent.Send event) {
        if (this.shouldRotate.get() && event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            packet.yaw = (float)this.yaw.get();
            packet.pitch = (float)this.pitch.get();
            this.shouldRotate.set(false);
        }
    }
    
    private boolean shouldServer() {
        return PingBypass.getInstance().isConnected() && this.server.getValue();
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (fullNullCheck() || (BedBomb.mc.player.dimension != -1 && BedBomb.mc.player.dimension != 1) || this.shouldServer()) {
            return;
        }
        if (event.getStage() == 0) {
            this.doBedBomb();
            if (this.shouldCraft && BedBomb.mc.currentScreen instanceof GuiCrafting) {
                final int woolSlot = InventoryUtil.findInventoryWool(false);
                final int woodSlot = InventoryUtil.findInventoryBlock(BlockPlanks.class,  true);
                if (woolSlot == -1 || woodSlot == -1) {
                    BedBomb.mc.displayGuiScreen((GuiScreen)null);
                    BedBomb.mc.currentScreen = null;
                    this.shouldCraft = false;
                    return;
                }
                if (this.craftStage > 1 && !this.one) {
                    BedBomb.mc.playerController.windowClick(((GuiContainer)BedBomb.mc.currentScreen).inventorySlots.windowId,  woolSlot,  0,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer)BedBomb.mc.currentScreen).inventorySlots.windowId,  1,  1,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer)BedBomb.mc.currentScreen).inventorySlots.windowId,  woolSlot,  0,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                    this.one = true;
                }
                else if (this.craftStage > 1 + this.craftDelay.getValue() && !this.two) {
                    BedBomb.mc.playerController.windowClick(((GuiContainer)BedBomb.mc.currentScreen).inventorySlots.windowId,  woolSlot,  0,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer)BedBomb.mc.currentScreen).inventorySlots.windowId,  2,  1,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer)BedBomb.mc.currentScreen).inventorySlots.windowId,  woolSlot,  0,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                    this.two = true;
                }
                else if (this.craftStage > 1 + this.craftDelay.getValue() * 2 && !this.three) {
                    BedBomb.mc.playerController.windowClick(((GuiContainer)BedBomb.mc.currentScreen).inventorySlots.windowId,  woolSlot,  0,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer)BedBomb.mc.currentScreen).inventorySlots.windowId,  3,  1,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer)BedBomb.mc.currentScreen).inventorySlots.windowId,  woolSlot,  0,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                    this.three = true;
                }
                else if (this.craftStage > 1 + this.craftDelay.getValue() * 3 && !this.four) {
                    BedBomb.mc.playerController.windowClick(((GuiContainer)BedBomb.mc.currentScreen).inventorySlots.windowId,  woodSlot,  0,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer)BedBomb.mc.currentScreen).inventorySlots.windowId,  4,  1,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer)BedBomb.mc.currentScreen).inventorySlots.windowId,  woodSlot,  0,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                    this.four = true;
                }
                else if (this.craftStage > 1 + this.craftDelay.getValue() * 4 && !this.five) {
                    BedBomb.mc.playerController.windowClick(((GuiContainer)BedBomb.mc.currentScreen).inventorySlots.windowId,  woodSlot,  0,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer)BedBomb.mc.currentScreen).inventorySlots.windowId,  5,  1,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer)BedBomb.mc.currentScreen).inventorySlots.windowId,  woodSlot,  0,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                    this.five = true;
                }
                else if (this.craftStage > 1 + this.craftDelay.getValue() * 5 && !this.six) {
                    BedBomb.mc.playerController.windowClick(((GuiContainer)BedBomb.mc.currentScreen).inventorySlots.windowId,  woodSlot,  0,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer)BedBomb.mc.currentScreen).inventorySlots.windowId,  6,  1,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer)BedBomb.mc.currentScreen).inventorySlots.windowId,  woodSlot,  0,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                    this.recheckBedSlots(woolSlot,  woodSlot);
                    BedBomb.mc.playerController.windowClick(((GuiContainer)BedBomb.mc.currentScreen).inventorySlots.windowId,  0,  0,  ClickType.QUICK_MOVE,  (EntityPlayer)BedBomb.mc.player);
                    this.six = true;
                    this.one = false;
                    this.two = false;
                    this.three = false;
                    this.four = false;
                    this.five = false;
                    this.six = false;
                    this.craftStage = -2;
                    this.shouldCraft = false;
                }
                ++this.craftStage;
            }
        }
        else if (event.getStage() == 1 && this.finalPos != null) {
            final Vec3d hitVec = new Vec3d((Vec3i)this.finalPos.down()).add(0.5,  0.5,  0.5).add(new Vec3d(this.finalFacing.getOpposite().getDirectionVec()).scale(0.5));
            BedBomb.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BedBomb.mc.player,  CPacketEntityAction.Action.START_SNEAKING));
            InventoryUtil.switchToHotbarSlot(this.bedSlot,  false);
            BlockUtil.rightClickBlock(this.finalPos.down(),  hitVec,  (this.bedSlot == -2) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND,  EnumFacing.UP,  this.packet.getValue());
            BedBomb.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BedBomb.mc.player,  CPacketEntityAction.Action.STOP_SNEAKING));
            this.placeTimer.reset();
            this.finalPos = null;
        }
    }
    
    public void recheckBedSlots(final int woolSlot,  final int woodSlot) {
        for (int i = 1; i <= 3; ++i) {
            if (BedBomb.mc.player.openContainer.getInventory().get(i) == ItemStack.EMPTY) {
                BedBomb.mc.playerController.windowClick(1,  woolSlot,  0,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                BedBomb.mc.playerController.windowClick(1,  i,  1,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                BedBomb.mc.playerController.windowClick(1,  woolSlot,  0,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
            }
        }
        for (int i = 4; i <= 6; ++i) {
            if (BedBomb.mc.player.openContainer.getInventory().get(i) == ItemStack.EMPTY) {
                BedBomb.mc.playerController.windowClick(1,  woodSlot,  0,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                BedBomb.mc.playerController.windowClick(1,  i,  1,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                BedBomb.mc.playerController.windowClick(1,  woodSlot,  0,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
            }
        }
    }
    
    public void incrementCraftStage() {
        if (this.craftTimer.passedMs(this.craftDelay.getValue())) {
            ++this.craftStage;
            if (this.craftStage > 9) {
                this.craftStage = 0;
            }
            this.craftTimer.reset();
        }
    }
    
    private void doBedBomb() {
        switch (this.logic.getValue()) {
            case BREAKPLACE: {
                this.mapBeds();
                this.breakBeds();
                this.placeBeds();
                break;
            }
            case PLACEBREAK: {
                this.mapBeds();
                this.placeBeds();
                this.breakBeds();
                break;
            }
        }
    }
    
    private void breakBeds() {
        if (this.explode.getValue() && this.breakTimer.passedMs(this.breakDelay.getValue())) {
            if (this.breakMode.getValue() == BreakLogic.CALC) {
                if (this.maxPos != null) {
                    final Vec3d hitVec = new Vec3d((Vec3i)this.maxPos).add(0.5,  0.5,  0.5);
                    final float[] rotations = RotationUtil.getLegitRotations(hitVec);
                    this.yaw.set((double)rotations[0]);
                    if (this.rotate.getValue()) {
                        this.shouldRotate.set(true);
                        this.pitch.set((double)rotations[1]);
                    }
                    final RayTraceResult result;
                    final EnumFacing facing = ((result = BedBomb.mc.world.rayTraceBlocks(new Vec3d(BedBomb.mc.player.posX,  BedBomb.mc.player.posY + BedBomb.mc.player.getEyeHeight(),  BedBomb.mc.player.posZ),  new Vec3d(this.maxPos.getX() + 0.5,  this.maxPos.getY() - 0.5,  this.maxPos.getZ() + 0.5))) == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
                    BlockUtil.rightClickBlock(this.maxPos,  hitVec,  EnumHand.MAIN_HAND,  facing,  true);
                    this.breakTimer.reset();
                }
            }
            else {
                for (final TileEntity entityBed : BedBomb.mc.world.loadedTileEntityList) {
                    if (entityBed instanceof TileEntityBed) {
                        if (BedBomb.mc.player.getDistanceSq(entityBed.getPos()) > MathUtil.square(this.breakRange.getValue())) {
                            continue;
                        }
                        final Vec3d hitVec2 = new Vec3d((Vec3i)entityBed.getPos()).add(0.5,  0.5,  0.5);
                        final float[] rotations2 = RotationUtil.getLegitRotations(hitVec2);
                        this.yaw.set((double)rotations2[0]);
                        if (this.rotate.getValue()) {
                            this.shouldRotate.set(true);
                            this.pitch.set((double)rotations2[1]);
                        }
                        final RayTraceResult result2;
                        final EnumFacing facing2 = ((result2 = BedBomb.mc.world.rayTraceBlocks(new Vec3d(BedBomb.mc.player.posX,  BedBomb.mc.player.posY + BedBomb.mc.player.getEyeHeight(),  BedBomb.mc.player.posZ),  new Vec3d(entityBed.getPos().getX() + 0.5,  entityBed.getPos().getY() - 0.5,  entityBed.getPos().getZ() + 0.5))) == null || result2.sideHit == null) ? EnumFacing.UP : result2.sideHit;
                        BlockUtil.rightClickBlock(entityBed.getPos(),  hitVec2,  EnumHand.MAIN_HAND,  facing2,  true);
                        this.breakTimer.reset();
                    }
                }
            }
        }
    }
    
    private void mapBeds() {
        this.maxPos = null;
        float maxDamage = 0.5f;
        if (this.removeTiles.getValue()) {
            final ArrayList<BedData> removedBlocks = new ArrayList<BedData>();
            for (final TileEntity tile : BedBomb.mc.world.loadedTileEntityList) {
                if (!(tile instanceof TileEntityBed)) {
                    continue;
                }
                final TileEntityBed bed = (TileEntityBed)tile;
                final BedData data = new BedData(tile.getPos(),  BedBomb.mc.world.getBlockState(tile.getPos()),  bed,  bed.isHeadPiece());
                removedBlocks.add(data);
            }
            for (final BedData data2 : removedBlocks) {
                BedBomb.mc.world.setBlockToAir(data2.getPos());
            }
            for (final BedData data2 : removedBlocks) {
                final BlockPos pos;
                if (data2.isHeadPiece() && BedBomb.mc.player.getDistanceSq(pos = data2.getPos()) <= MathUtil.square(this.breakRange.getValue())) {
                    final float selfDamage;
                    if ((selfDamage = DamageUtil.calculateDamage(pos,  (Entity)BedBomb.mc.player)) + 1.0 >= EntityUtil.getHealth((Entity)BedBomb.mc.player) && DamageUtil.canTakeDamage(this.suicide.getValue())) {
                        continue;
                    }
                    for (final EntityPlayer player : BedBomb.mc.world.playerEntities) {
                        final float damage;
                        if (player.getDistanceSq(pos) < MathUtil.square(this.range.getValue()) && EntityUtil.isValid((Entity)player,  this.range.getValue() + this.breakRange.getValue()) && ((damage = DamageUtil.calculateDamage(pos,  (Entity)player)) > selfDamage || (damage > this.minDamage.getValue() && !DamageUtil.canTakeDamage(this.suicide.getValue())) || damage > EntityUtil.getHealth((Entity)player))) {
                            if (damage <= maxDamage) {
                                continue;
                            }
                            maxDamage = damage;
                            this.maxPos = pos;
                        }
                    }
                }
            }
            for (final BedData data2 : removedBlocks) {
                BedBomb.mc.world.setBlockState(data2.getPos(),  data2.getState());
            }
        }
        else {
            for (final TileEntity tile2 : BedBomb.mc.world.loadedTileEntityList) {
                final TileEntityBed bed2;
                final BlockPos pos2;
                if (tile2 instanceof TileEntityBed && (bed2 = (TileEntityBed)tile2).isHeadPiece() && BedBomb.mc.player.getDistanceSq(pos2 = bed2.getPos()) <= MathUtil.square(this.breakRange.getValue())) {
                    final float selfDamage2;
                    if ((selfDamage2 = DamageUtil.calculateDamage(pos2,  (Entity)BedBomb.mc.player)) + 1.0 >= EntityUtil.getHealth((Entity)BedBomb.mc.player) && DamageUtil.canTakeDamage(this.suicide.getValue())) {
                        continue;
                    }
                    for (final EntityPlayer player : BedBomb.mc.world.playerEntities) {
                        final float damage;
                        if (player.getDistanceSq(pos2) < MathUtil.square(this.range.getValue()) && EntityUtil.isValid((Entity)player,  this.range.getValue() + this.breakRange.getValue()) && ((damage = DamageUtil.calculateDamage(pos2,  (Entity)player)) > selfDamage2 || (damage > this.minDamage.getValue() && !DamageUtil.canTakeDamage(this.suicide.getValue())) || damage > EntityUtil.getHealth((Entity)player))) {
                            if (damage <= maxDamage) {
                                continue;
                            }
                            maxDamage = damage;
                            this.maxPos = pos2;
                        }
                    }
                }
            }
        }
    }
    
    private void placeBeds() {
        if (this.place.getValue() && this.placeTimer.passedMs(this.placeDelay.getValue()) && this.maxPos == null) {
            this.bedSlot = this.findBedSlot();
            if (this.bedSlot == -1) {
                if (BedBomb.mc.player.getHeldItemOffhand().getItem() != Items.BED) {
                    if (this.craft.getValue() && !this.shouldCraft && EntityUtil.getClosestEnemy(this.placeRange.getValue()) != null) {
                        this.doBedCraft();
                    }
                    return;
                }
                this.bedSlot = -2;
            }
            this.target = EntityUtil.getClosestEnemy(this.placeRange.getValue());
            if (this.target != null) {
                final BlockPos targetPos = new BlockPos(this.target.getPositionVector());
                this.placeBed(targetPos,  true);
                if (this.craft.getValue()) {
                    this.doBedCraft();
                }
            }
        }
    }
    
    private void placeBed(final BlockPos pos,  final boolean firstCheck) {
        if (BedBomb.mc.world.getBlockState(pos).getBlock() == Blocks.BED) {
            return;
        }
        final float damage = DamageUtil.calculateDamage(pos,  (Entity)BedBomb.mc.player);
        if (damage > EntityUtil.getHealth((Entity)BedBomb.mc.player) + 0.5) {
            if (firstCheck && this.oneDot15.getValue()) {
                this.placeBed(pos.up(),  false);
            }
            return;
        }
        if (!BedBomb.mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
            if (firstCheck && this.oneDot15.getValue()) {
                this.placeBed(pos.up(),  false);
            }
            return;
        }
        final ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        final HashMap<BlockPos,  EnumFacing> facings = new HashMap<BlockPos,  EnumFacing>();
        for (final EnumFacing facing : EnumFacing.values()) {
            final BlockPos position;
            if (facing != EnumFacing.DOWN && facing != EnumFacing.UP && BedBomb.mc.player.getDistanceSq(position = pos.offset(facing)) <= MathUtil.square(this.placeRange.getValue()) && BedBomb.mc.world.getBlockState(position).getMaterial().isReplaceable()) {
                if (!BedBomb.mc.world.getBlockState(position.down()).getMaterial().isReplaceable()) {
                    positions.add(position);
                    facings.put(position,  facing.getOpposite());
                }
            }
        }
        if (positions.isEmpty()) {
            if (firstCheck && this.oneDot15.getValue()) {
                this.placeBed(pos.up(),  false);
            }
            return;
        }
        positions.sort(Comparator.comparingDouble(pos2 -> BedBomb.mc.player.getDistanceSq(pos2)));
        this.finalPos = positions.get(0);
        this.finalFacing = facings.get(this.finalPos);
        final float[] rotation = RotationUtil.simpleFacing(this.finalFacing);
        if (!this.sendRotationPacket && this.extraPacket.getValue()) {
            RotationUtil.faceYawAndPitch(rotation[0],  rotation[1]);
            this.sendRotationPacket = true;
        }
        this.yaw.set((double)rotation[0]);
        this.pitch.set((double)rotation[1]);
        this.shouldRotate.set(true);
        Phobos.rotationManager.setPlayerRotations(rotation[0],  rotation[1]);
    }
    
    @Override
    public String getDisplayInfo() {
        if (this.target != null) {
            return this.target.getName();
        }
        return null;
    }
    
    public void doBedCraft() {
        final int woolSlot = InventoryUtil.findInventoryWool(false);
        final int woodSlot = InventoryUtil.findInventoryBlock(BlockPlanks.class,  true);
        if (woolSlot == -1 || woodSlot == -1) {
            if (BedBomb.mc.currentScreen instanceof GuiCrafting) {
                BedBomb.mc.displayGuiScreen((GuiScreen)null);
                BedBomb.mc.currentScreen = null;
            }
            return;
        }
        final List targets;
        if (this.placeCraftingTable.getValue() && BlockUtil.getBlockSphere(this.tableRange.getValue() - 1.0f,  BlockWorkbench.class).size() == 0 && !(targets = BlockUtil.getSphere(EntityUtil.getPlayerPos((EntityPlayer)BedBomb.mc.player),  this.tableRange.getValue(),  this.tableRange.getValue().intValue(),  false,  true,  0).stream().filter(pos -> BlockUtil.isPositionPlaceable(pos,  false) == 3).sorted(Comparator.comparingInt(pos -> -this.safety(pos))).collect((Collector<? super Object,  ?,  List<? super Object>>)Collectors.toList())).isEmpty()) {
            final BlockPos target = targets.get(0);
            int tableSlot = InventoryUtil.findHotbarBlock(BlockWorkbench.class);
            if (tableSlot != -1) {
                BedBomb.mc.player.inventory.currentItem = tableSlot;
                BlockUtil.placeBlock(target,  EnumHand.MAIN_HAND,  this.rotate.getValue(),  true,  false);
            }
            else {
                if (this.craftTable.getValue()) {
                    this.craftTable();
                }
                if ((tableSlot = InventoryUtil.findHotbarBlock(BlockWorkbench.class)) != -1) {
                    BedBomb.mc.player.inventory.currentItem = tableSlot;
                    BlockUtil.placeBlock(target,  EnumHand.MAIN_HAND,  this.rotate.getValue(),  true,  false);
                }
            }
        }
        if (this.openCraftingTable.getValue()) {
            final List<BlockPos> tables = BlockUtil.getBlockSphere(this.tableRange.getValue(),  BlockWorkbench.class);
            tables.sort(Comparator.comparingDouble(pos -> BedBomb.mc.player.getDistanceSq(pos)));
            if (!tables.isEmpty() && !(BedBomb.mc.currentScreen instanceof GuiCrafting)) {
                final BlockPos target = tables.get(0);
                BedBomb.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BedBomb.mc.player,  CPacketEntityAction.Action.STOP_SNEAKING));
                if (BedBomb.mc.player.getDistanceSq(target) > MathUtil.square(this.breakRange.getValue())) {
                    return;
                }
                final Vec3d hitVec = new Vec3d((Vec3i)target);
                final float[] rotations = RotationUtil.getLegitRotations(hitVec);
                this.yaw.set((double)rotations[0]);
                if (this.rotate.getValue()) {
                    this.shouldRotate.set(true);
                    this.pitch.set((double)rotations[1]);
                }
                final RayTraceResult result;
                final EnumFacing facing = ((result = BedBomb.mc.world.rayTraceBlocks(new Vec3d(BedBomb.mc.player.posX,  BedBomb.mc.player.posY + BedBomb.mc.player.getEyeHeight(),  BedBomb.mc.player.posZ),  new Vec3d(target.getX() + 0.5,  target.getY() - 0.5,  target.getZ() + 0.5))) == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
                BlockUtil.rightClickBlock(target,  hitVec,  EnumHand.MAIN_HAND,  facing,  true);
                this.breakTimer.reset();
                if (BedBomb.mc.player.isSneaking()) {
                    BedBomb.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BedBomb.mc.player,  CPacketEntityAction.Action.START_SNEAKING));
                }
            }
            this.shouldCraft = (BedBomb.mc.currentScreen instanceof GuiCrafting);
            this.craftStage = 0;
            this.craftTimer.reset();
        }
    }
    
    public void craftTable() {
        final int woodSlot = InventoryUtil.findInventoryBlock(BlockPlanks.class,  true);
        if (woodSlot != -1) {
            BedBomb.mc.playerController.windowClick(0,  woodSlot,  0,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
            BedBomb.mc.playerController.windowClick(0,  1,  1,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
            BedBomb.mc.playerController.windowClick(0,  2,  1,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
            BedBomb.mc.playerController.windowClick(0,  3,  1,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
            BedBomb.mc.playerController.windowClick(0,  4,  1,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
            BedBomb.mc.playerController.windowClick(0,  0,  0,  ClickType.QUICK_MOVE,  (EntityPlayer)BedBomb.mc.player);
            final int table = InventoryUtil.findInventoryBlock(BlockWorkbench.class,  true);
            if (table != -1) {
                BedBomb.mc.playerController.windowClick(0,  table,  0,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                BedBomb.mc.playerController.windowClick(0,  (int)this.tableSlot.getValue(),  0,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
                BedBomb.mc.playerController.windowClick(0,  table,  0,  ClickType.PICKUP,  (EntityPlayer)BedBomb.mc.player);
            }
        }
    }
    
    @Override
    public void onToggle() {
        this.bedSlot = -1;
        this.sendRotationPacket = false;
        this.target = null;
        this.yaw.set(-1.0);
        this.pitch.set(-1.0);
        this.shouldRotate.set(false);
        this.shouldCraft = false;
    }
    
    private int findBedSlot() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = BedBomb.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() == Items.BED) {
                return i;
            }
        }
        return -1;
    }
    
    private int safety(final BlockPos pos) {
        int safety = 0;
        for (final EnumFacing facing : EnumFacing.values()) {
            if (!BedBomb.mc.world.getBlockState(pos.offset(facing)).getMaterial().isReplaceable()) {
                ++safety;
            }
        }
        return safety;
    }
    
    public enum BreakLogic
    {
        ALL,  
        CALC;
    }
    
    public enum Logic
    {
        BREAKPLACE,  
        PLACEBREAK;
    }
    
    public static class BedData
    {
        private final BlockPos pos;
        private final IBlockState state;
        private final boolean isHeadPiece;
        private final TileEntityBed entity;
        
        public BedData(final BlockPos pos,  final IBlockState state,  final TileEntityBed bed,  final boolean isHeadPiece) {
            this.pos = pos;
            this.state = state;
            this.entity = bed;
            this.isHeadPiece = isHeadPiece;
        }
        
        public BlockPos getPos() {
            return this.pos;
        }
        
        public IBlockState getState() {
            return this.state;
        }
        
        public boolean isHeadPiece() {
            return this.isHeadPiece;
        }
        
        public TileEntityBed getEntity() {
            return this.entity;
        }
    }
}
