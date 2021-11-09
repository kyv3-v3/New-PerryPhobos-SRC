//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.network.*;
import me.earth.phobos.event.events.*;
import net.minecraft.network.play.client.*;
import net.minecraft.entity.*;
import me.earth.phobos.*;
import me.earth.phobos.util.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.earth.phobos.features.command.*;
import net.minecraft.item.*;
import java.util.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.item.*;
import net.minecraft.block.*;

public class BlockLag extends Module
{
    private static BlockLag INSTANCE;
    private final Setting<Mode> mode;
    private final Setting<Boolean> smartTp;
    private final Setting<Integer> tpMin;
    private final Setting<Integer> tpMax;
    private final Setting<Boolean> noVoid;
    private final Setting<Integer> tpHeight;
    private final Setting<Boolean> keepInside;
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> sneaking;
    private final Setting<Boolean> offground;
    private final Setting<Boolean> chat;
    private final Setting<Boolean> tpdebug;
    private BlockPos burrowPos;
    private int lastBlock;
    private int blockSlot;
    
    public BlockLag() {
        super("BlockLag", "Places a block where ur standing.", Module.Category.MOVEMENT, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.OBSIDIAN));
        this.smartTp = (Setting<Boolean>)this.register(new Setting("SmartTP", (T)true));
        this.tpMin = (Setting<Integer>)this.register(new Setting("TPMin", (T)2, (T)2, (T)10, v -> this.smartTp.getValue()));
        this.tpMax = (Setting<Integer>)this.register(new Setting("TPMax", (T)3, (T)3, (T)40, v -> this.smartTp.getValue()));
        this.noVoid = (Setting<Boolean>)this.register(new Setting("NoVoid", (T)false, v -> this.smartTp.getValue()));
        this.tpHeight = (Setting<Integer>)this.register(new Setting("TPHeight", (T)2, (T)(-100), (T)100, v -> !this.smartTp.getValue()));
        this.keepInside = (Setting<Boolean>)this.register(new Setting("Center", (T)true));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (T)false));
        this.sneaking = (Setting<Boolean>)this.register(new Setting("Sneak", (T)false));
        this.offground = (Setting<Boolean>)this.register(new Setting("Offground", (T)false));
        this.chat = (Setting<Boolean>)this.register(new Setting("Chat Msgs", (T)true));
        this.tpdebug = (Setting<Boolean>)this.register(new Setting("Debug", (T)false, v -> this.chat.getValue() && this.smartTp.getValue()));
        BlockLag.INSTANCE = this;
    }
    
    public static BlockLag getInstance() {
        if (BlockLag.INSTANCE == null) {
            BlockLag.INSTANCE = new BlockLag();
        }
        return BlockLag.INSTANCE;
    }
    
    public void onEnable() {
        this.burrowPos = new BlockPos(BlockLag.mc.player.posX, Math.ceil(BlockLag.mc.player.posY), BlockLag.mc.player.posZ);
        this.blockSlot = this.findBlockSlot();
        this.lastBlock = BlockLag.mc.player.inventory.currentItem;
        if (!this.doChecks() || this.blockSlot == -1) {
            this.disable();
            return;
        }
        if (this.keepInside.getValue()) {
            double x = BlockLag.mc.player.posX - Math.floor(BlockLag.mc.player.posX);
            double z = BlockLag.mc.player.posZ - Math.floor(BlockLag.mc.player.posZ);
            if (x <= 0.3 || x >= 0.7) {
                x = ((x > 0.5) ? 0.69 : 0.31);
            }
            if (z < 0.3 || z > 0.7) {
                z = ((z > 0.5) ? 0.69 : 0.31);
            }
            BlockLag.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Math.floor(BlockLag.mc.player.posX) + x, BlockLag.mc.player.posY, Math.floor(BlockLag.mc.player.posZ) + z, BlockLag.mc.player.onGround));
            BlockLag.mc.player.setPosition(Math.floor(BlockLag.mc.player.posX) + x, BlockLag.mc.player.posY, Math.floor(BlockLag.mc.player.posZ) + z);
        }
        BlockLag.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(BlockLag.mc.player.posX, BlockLag.mc.player.posY + 0.41999998688698, BlockLag.mc.player.posZ, !this.offground.getValue()));
        BlockLag.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(BlockLag.mc.player.posX, BlockLag.mc.player.posY + 0.7531999805211997, BlockLag.mc.player.posZ, !this.offground.getValue()));
        BlockLag.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(BlockLag.mc.player.posX, BlockLag.mc.player.posY + 1.00133597911214, BlockLag.mc.player.posZ, !this.offground.getValue()));
        BlockLag.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(BlockLag.mc.player.posX, BlockLag.mc.player.posY + 1.16610926093821, BlockLag.mc.player.posZ, !this.offground.getValue()));
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() != 0) {
            return;
        }
        if (this.sneaking.getValue() && !BlockLag.mc.player.isSneaking()) {
            BlockLag.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockLag.mc.player, CPacketEntityAction.Action.START_SNEAKING));
        }
        if (this.rotate.getValue()) {
            final float[] angle = MathUtil.calcAngle(BlockLag.mc.player.getPositionEyes(BlockLag.mc.getRenderPartialTicks()), new Vec3d((double)(this.burrowPos.getX() + 0.5f), (double)(this.burrowPos.getY() + 0.5f), (double)(this.burrowPos.getZ() + 0.5f)));
            Phobos.rotationManager.setPlayerRotations(angle[0], angle[1]);
        }
        InventoryUtil.switchToHotbarSlot(this.blockSlot, false);
        BlockUtil.placeBlock(this.burrowPos, (this.blockSlot == -2) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, false, true, this.sneaking.getValue());
        InventoryUtil.switchToHotbarSlot(this.lastBlock, false);
        BlockLag.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(BlockLag.mc.player.posX, ((boolean)this.smartTp.getValue()) ? ((double)this.adaptiveTpHeight(false)) : (this.tpHeight.getValue() + BlockLag.mc.player.posY), BlockLag.mc.player.posZ, !this.offground.getValue()));
        BlockLag.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockLag.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        this.disable();
    }
    
    private int findBlockSlot() {
        for (final Class block : this.blockList()) {
            if (block == BlockFalling.class) {
                for (int i = 0; i < 9; ++i) {
                    final ItemStack item = BlockLag.mc.player.inventory.getStackInSlot(i);
                    if (item.getItem() instanceof ItemBlock) {
                        final Block blockFalling = Block.getBlockFromItem(BlockLag.mc.player.inventory.getStackInSlot(i).getItem());
                        if (blockFalling instanceof BlockFalling) {
                            return i;
                        }
                    }
                }
            }
            else {
                final int slot = InventoryUtil.findHotbarBlock(block);
                if (slot != -1) {
                    return slot;
                }
                if (InventoryUtil.isBlock(BlockLag.mc.player.getHeldItemOffhand().getItem(), block)) {
                    return -2;
                }
                continue;
            }
        }
        if (this.chat.getValue()) {
            Command.sendMessage("<" + this.getDisplayName() + "> §cNo blocks to use.");
        }
        return -1;
    }
    
    private List<Class> blockList() {
        final List<Class> blocks = new ArrayList<Class>();
        blocks.add(this.mode.getValue().getPriorityBlock());
        for (final Mode block : Mode.values()) {
            if (this.mode.getValue() != block) {
                blocks.add(block.getPriorityBlock());
            }
        }
        return blocks;
    }
    
    private int adaptiveTpHeight(final boolean first) {
        for (int max = (BlockLag.mc.player.dimension == -1 && this.noVoid.getValue() && this.tpMax.getValue() + this.burrowPos.getY() > 127) ? Math.abs(this.burrowPos.getY() - 127) : this.tpMax.getValue(), airblock = (this.noVoid.getValue() && this.tpMax.getValue() * -1 + this.burrowPos.getY() < 0) ? (this.burrowPos.getY() * -1) : (this.tpMax.getValue() * -1); airblock < max; ++airblock) {
            if (Math.abs(airblock) >= this.tpMin.getValue() && BlockLag.mc.world.isAirBlock(this.burrowPos.offset(EnumFacing.UP, airblock)) && BlockLag.mc.world.isAirBlock(this.burrowPos.offset(EnumFacing.UP, airblock + 1))) {
                if (this.tpdebug.getValue() && this.chat.getValue() && !first) {
                    Command.sendMessage(Integer.toString(airblock));
                }
                return this.burrowPos.getY() + airblock;
            }
        }
        return 69420;
    }
    
    private boolean doChecks() {
        if (fullNullCheck()) {
            return false;
        }
        if (BlockUtil.isPositionPlaceable(this.burrowPos, false, false) < 1) {
            return false;
        }
        if (this.smartTp.getValue() && this.adaptiveTpHeight(true) == 69420) {
            if (this.chat.getValue()) {
                Command.sendMessage("<" + this.getDisplayName() + "> §cNot enough room to rubberband.");
            }
            return false;
        }
        if (!BlockLag.mc.world.isAirBlock(this.burrowPos.offset(EnumFacing.UP, 2))) {
            if (this.chat.getValue()) {
                Command.sendMessage("<" + this.getDisplayName() + "> §cNot enough room to jump.");
            }
            return false;
        }
        for (final Entity entity : BlockUtil.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(this.burrowPos))) {
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb) && !(entity instanceof EntityArrow) && !(entity instanceof EntityPlayer)) {
                if (entity instanceof EntityExpBottle) {
                    continue;
                }
                return false;
            }
        }
        return true;
    }
    
    private enum Mode
    {
        OBSIDIAN((Class)BlockObsidian.class), 
        ECHEST((Class)BlockEnderChest.class), 
        FALLING((Class)BlockFalling.class), 
        ENDROD((Class)BlockEndRod.class);
        
        private final Class priorityBlock;
        
        private Mode(final Class block) {
            this.priorityBlock = block;
        }
        
        private Class getPriorityBlock() {
            return this.priorityBlock;
        }
    }
}
