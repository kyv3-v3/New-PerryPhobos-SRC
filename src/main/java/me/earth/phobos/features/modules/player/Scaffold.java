/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.MobEffects
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.Container
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.util.EnumActionResult
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.earth.phobos.features.modules.player;

import java.util.Arrays;
import java.util.List;
import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.BlockUtil;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.InventoryUtil;
import me.earth.phobos.util.MathUtil;
import me.earth.phobos.util.TimerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Scaffold
extends Module {
    private final Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.New));
    private final Setting<Boolean> swing = this.register(new Setting<Boolean>("Swing Arm", Boolean.valueOf(true), bl -> this.mode.getValue() == Mode.New));
    private final Setting<Boolean> bSwitch = this.register(new Setting<Boolean>("Switch", Boolean.valueOf(true), bl -> this.mode.getValue() == Mode.New));
    private final Setting<Boolean> center = this.register(new Setting<Boolean>("Center", Boolean.valueOf(false), bl -> this.mode.getValue() == Mode.New));
    private final Setting<Boolean> tower = this.register(new Setting<Boolean>("Tower", Boolean.valueOf(true), bl -> this.mode.getValue() == Mode.New));
    private final Setting<Boolean> keepY = this.register(new Setting<Boolean>("KeepYLevel", Boolean.valueOf(false), bl -> this.mode.getValue() == Mode.New));
    private final Setting<Boolean> sprint = this.register(new Setting<Boolean>("UseSprint", Boolean.valueOf(true), bl -> this.mode.getValue() == Mode.New));
    private final Setting<Boolean> replenishBlocks = this.register(new Setting<Boolean>("ReplenishBlocks", Boolean.valueOf(true), bl -> this.mode.getValue() == Mode.New));
    private final Setting<Boolean> down = this.register(new Setting<Boolean>("Down", Boolean.valueOf(true), bl -> this.mode.getValue() == Mode.New));
    private final Setting<Float> expand = this.register(new Setting<Float>("Expand", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(6.0f), f -> this.mode.getValue() == Mode.New));
    private final List<Block> invalid = Arrays.asList(new Block[]{Blocks.field_150381_bn, Blocks.field_150460_al, Blocks.field_150404_cg, Blocks.field_150462_ai, Blocks.field_150447_bR, Blocks.field_150486_ae, Blocks.field_150367_z, Blocks.field_150350_a, Blocks.field_150355_j, Blocks.field_150353_l, Blocks.field_150358_i, Blocks.field_150356_k, Blocks.field_150431_aC, Blocks.field_150478_aa, Blocks.field_150467_bQ, Blocks.field_150421_aI, Blocks.field_150430_aB, Blocks.field_150471_bO, Blocks.field_150442_at, Blocks.field_150323_B, Blocks.field_150456_au, Blocks.field_150445_bS, Blocks.field_150452_aw, Blocks.field_150443_bT, Blocks.field_150337_Q, Blocks.field_150338_P, Blocks.field_150327_N, Blocks.field_150328_O, Blocks.field_150467_bQ, Blocks.field_150434_aF, Blocks.field_150468_ap, Blocks.field_150477_bB});
    private final TimerUtil timerMotion = new TimerUtil();
    private final TimerUtil itemTimer = new TimerUtil();
    private final TimerUtil timer = new TimerUtil();
    public Setting<Boolean> rotation = this.register(new Setting<Boolean>("Rotate", Boolean.valueOf(false), bl -> this.mode.getValue() == Mode.Old));
    private int lastY;
    private BlockPos pos;
    private boolean teleported;

    public Scaffold() {
        super("Scaffold", "Places Blocks underneath you.", Module.Category.PLAYER, true, false, false);
    }

    public static void swap(int n, int n2) {
        Scaffold.mc.field_71442_b.func_187098_a(Scaffold.mc.field_71439_g.field_71069_bz.field_75152_c, n, 0, ClickType.PICKUP, (EntityPlayer)Scaffold.mc.field_71439_g);
        Scaffold.mc.field_71442_b.func_187098_a(Scaffold.mc.field_71439_g.field_71069_bz.field_75152_c, n2, 0, ClickType.PICKUP, (EntityPlayer)Scaffold.mc.field_71439_g);
        Scaffold.mc.field_71442_b.func_187098_a(Scaffold.mc.field_71439_g.field_71069_bz.field_75152_c, n, 0, ClickType.PICKUP, (EntityPlayer)Scaffold.mc.field_71439_g);
        Scaffold.mc.field_71442_b.func_78765_e();
    }

    public static int getItemSlot(Container container, Item item) {
        int n = 0;
        for (int i = 9; i < 45; ++i) {
            if (!container.func_75139_a(i).func_75216_d() || container.func_75139_a(i).func_75211_c().func_77973_b() != item) continue;
            n = i;
        }
        return n;
    }

    public static boolean isMoving(EntityLivingBase entityLivingBase) {
        return entityLivingBase.field_191988_bg == 0.0f && entityLivingBase.field_70702_br == 0.0f;
    }

    @Override
    public void onEnable() {
        this.timer.reset();
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayerPost(UpdateWalkingPlayerEvent updateWalkingPlayerEvent) {
        if (this.mode.getValue() == Mode.Old) {
            BlockPos blockPos;
            if (this.isOff() || Scaffold.fullNullCheck() || updateWalkingPlayerEvent.getStage() == 0) {
                return;
            }
            if (!Scaffold.mc.field_71474_y.field_74314_A.func_151470_d()) {
                this.timer.reset();
            }
            if (BlockUtil.isScaffoldPos((blockPos = EntityUtil.getPlayerPosWithEntity()).func_177982_a(0, -1, 0))) {
                if (BlockUtil.isValidBlock(blockPos.func_177982_a(0, -2, 0))) {
                    this.place(blockPos.func_177982_a(0, -1, 0), EnumFacing.UP);
                } else if (BlockUtil.isValidBlock(blockPos.func_177982_a(-1, -1, 0))) {
                    this.place(blockPos.func_177982_a(0, -1, 0), EnumFacing.EAST);
                } else if (BlockUtil.isValidBlock(blockPos.func_177982_a(1, -1, 0))) {
                    this.place(blockPos.func_177982_a(0, -1, 0), EnumFacing.WEST);
                } else if (BlockUtil.isValidBlock(blockPos.func_177982_a(0, -1, -1))) {
                    this.place(blockPos.func_177982_a(0, -1, 0), EnumFacing.SOUTH);
                } else if (BlockUtil.isValidBlock(blockPos.func_177982_a(0, -1, 1))) {
                    this.place(blockPos.func_177982_a(0, -1, 0), EnumFacing.NORTH);
                } else if (BlockUtil.isValidBlock(blockPos.func_177982_a(1, -1, 1))) {
                    if (BlockUtil.isValidBlock(blockPos.func_177982_a(0, -1, 1))) {
                        this.place(blockPos.func_177982_a(0, -1, 1), EnumFacing.NORTH);
                    }
                    this.place(blockPos.func_177982_a(1, -1, 1), EnumFacing.EAST);
                } else if (BlockUtil.isValidBlock(blockPos.func_177982_a(-1, -1, 1))) {
                    if (BlockUtil.isValidBlock(blockPos.func_177982_a(-1, -1, 0))) {
                        this.place(blockPos.func_177982_a(0, -1, 1), EnumFacing.WEST);
                    }
                    this.place(blockPos.func_177982_a(-1, -1, 1), EnumFacing.SOUTH);
                } else if (BlockUtil.isValidBlock(blockPos.func_177982_a(1, -1, 1))) {
                    if (BlockUtil.isValidBlock(blockPos.func_177982_a(0, -1, 1))) {
                        this.place(blockPos.func_177982_a(0, -1, 1), EnumFacing.SOUTH);
                    }
                    this.place(blockPos.func_177982_a(1, -1, 1), EnumFacing.WEST);
                } else if (BlockUtil.isValidBlock(blockPos.func_177982_a(1, -1, 1))) {
                    if (BlockUtil.isValidBlock(blockPos.func_177982_a(0, -1, 1))) {
                        this.place(blockPos.func_177982_a(0, -1, 1), EnumFacing.EAST);
                    }
                    this.place(blockPos.func_177982_a(1, -1, 1), EnumFacing.NORTH);
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        if (this.mode.getValue() == Mode.New) {
            if (this.down.getValue().booleanValue() && Scaffold.mc.field_71474_y.field_74311_E.func_151470_d() && !this.sprint.getValue().booleanValue()) {
                Scaffold.mc.field_71439_g.func_70031_b(false);
            }
            if (this.replenishBlocks.getValue().booleanValue() && !(Scaffold.mc.field_71439_g.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof ItemBlock) && this.getBlockCountHotbar() <= 0 && this.itemTimer.passedMs(100L)) {
                for (int i = 9; i < 45; ++i) {
                    ItemStack itemStack;
                    if (!Scaffold.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d() || !((itemStack = Scaffold.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c()).func_77973_b() instanceof ItemBlock) || this.invalid.contains((Object)Block.func_149634_a((Item)itemStack.func_77973_b())) || i >= 36) continue;
                    Scaffold.swap(Scaffold.getItemSlot(Scaffold.mc.field_71439_g.field_71069_bz, itemStack.func_77973_b()), 44);
                }
            }
            if (this.keepY.getValue().booleanValue()) {
                if (Scaffold.isMoving((EntityLivingBase)Scaffold.mc.field_71439_g) && Scaffold.mc.field_71474_y.field_74314_A.func_151470_d() || Scaffold.mc.field_71439_g.field_70124_G || Scaffold.mc.field_71439_g.field_70122_E) {
                    this.lastY = MathHelper.func_76128_c((double)Scaffold.mc.field_71439_g.field_70163_u);
                }
            } else {
                this.lastY = MathHelper.func_76128_c((double)Scaffold.mc.field_71439_g.field_70163_u);
            }
            BlockData blockData = null;
            double d = Scaffold.mc.field_71439_g.field_70165_t;
            double d2 = Scaffold.mc.field_71439_g.field_70161_v;
            double d3 = this.keepY.getValue() != false ? (double)this.lastY : Scaffold.mc.field_71439_g.field_70163_u;
            double d4 = Scaffold.mc.field_71439_g.field_71158_b.field_192832_b;
            double d5 = Scaffold.mc.field_71439_g.field_71158_b.field_78902_a;
            float f = Scaffold.mc.field_71439_g.field_70177_z;
            if (!Scaffold.mc.field_71439_g.field_70123_F) {
                double[] object2 = this.getExpandCoords(d, d2, d4, d5, f);
                d = object2[0];
                d2 = object2[1];
            }
            if (this.canPlace(Scaffold.mc.field_71441_e.func_180495_p(new BlockPos(Scaffold.mc.field_71439_g.field_70165_t, Scaffold.mc.field_71439_g.field_70163_u - (double)(Scaffold.mc.field_71474_y.field_74311_E.func_151470_d() && this.down.getValue() != false ? 2 : 1), Scaffold.mc.field_71439_g.field_70161_v)).func_177230_c())) {
                d = Scaffold.mc.field_71439_g.field_70165_t;
                d2 = Scaffold.mc.field_71439_g.field_70161_v;
            }
            BlockPos object = new BlockPos(d, d3 - 1.0, d2);
            if (Scaffold.mc.field_71474_y.field_74311_E.func_151470_d() && this.down.getValue().booleanValue()) {
                object = new BlockPos(d, d3 - 2.0, d2);
            }
            this.pos = object;
            if (Scaffold.mc.field_71441_e.func_180495_p(object).func_177230_c() == Blocks.field_150350_a) {
                blockData = this.getBlockData2(object);
            }
            if (blockData != null) {
                if (this.getBlockCountHotbar() <= 0 || !this.bSwitch.getValue().booleanValue() && !(Scaffold.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBlock)) {
                    return;
                }
                int n = Scaffold.mc.field_71439_g.field_71071_by.field_70461_c;
                if (this.bSwitch.getValue().booleanValue()) {
                    for (int i = 0; i < 9; ++i) {
                        Scaffold.mc.field_71439_g.field_71071_by.func_70301_a(i);
                        if (Scaffold.mc.field_71439_g.field_71071_by.func_70301_a(i).func_190916_E() == 0 || !(Scaffold.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemBlock) || this.invalid.contains((Object)((ItemBlock)Scaffold.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b()).func_179223_d())) continue;
                        Scaffold.mc.field_71439_g.field_71071_by.field_70461_c = i;
                        break;
                    }
                }
                if (this.mode.getValue() == Mode.New) {
                    if (Scaffold.mc.field_71474_y.field_74314_A.func_151470_d() && Scaffold.mc.field_71439_g.field_191988_bg == 0.0f && Scaffold.mc.field_71439_g.field_70702_br == 0.0f && !Scaffold.mc.field_71439_g.func_70644_a(MobEffects.field_76430_j)) {
                        if (!this.teleported && this.center.getValue().booleanValue()) {
                            this.teleported = true;
                            BlockPos blockPos = new BlockPos(Scaffold.mc.field_71439_g.field_70165_t, Scaffold.mc.field_71439_g.field_70163_u, Scaffold.mc.field_71439_g.field_70161_v);
                            Scaffold.mc.field_71439_g.func_70107_b((double)blockPos.func_177958_n() + 0.5, (double)blockPos.func_177956_o(), (double)blockPos.func_177952_p() + 0.5);
                        }
                        if (this.center.getValue().booleanValue() && !this.teleported) {
                            return;
                        }
                        Scaffold.mc.field_71439_g.field_70181_x = 0.42f;
                        Scaffold.mc.field_71439_g.field_70179_y = 0.0;
                        Scaffold.mc.field_71439_g.field_70159_w = 0.0;
                        if (!this.tower.getValue().booleanValue()) {
                            Scaffold.mc.field_71439_g.field_70181_x = -0.28;
                        }
                    } else {
                        this.timerMotion.reset();
                        if (this.teleported && this.center.getValue().booleanValue()) {
                            this.teleported = false;
                        }
                    }
                }
                if (Scaffold.mc.field_71442_b.func_187099_a(Scaffold.mc.field_71439_g, Scaffold.mc.field_71441_e, blockData.position, blockData.face, new Vec3d((double)blockData.position.func_177958_n() + Math.random(), (double)blockData.position.func_177956_o() + Math.random(), (double)blockData.position.func_177952_p() + Math.random()), EnumHand.MAIN_HAND) != EnumActionResult.FAIL) {
                    if (this.swing.getValue().booleanValue()) {
                        Scaffold.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                    } else {
                        Scaffold.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
                    }
                }
                Scaffold.mc.field_71439_g.field_71071_by.field_70461_c = n;
            }
        }
    }

    public double[] getExpandCoords(double d, double d2, double d3, double d4, float f) {
        BlockPos blockPos = new BlockPos(d, Scaffold.mc.field_71439_g.field_70163_u - (double)(Scaffold.mc.field_71474_y.field_74311_E.func_151470_d() && this.down.getValue() != false ? 2 : 1), d2);
        Block block = Scaffold.mc.field_71441_e.func_180495_p(blockPos).func_177230_c();
        double d5 = -999.0;
        double d6 = -999.0;
        double d7 = 0.0;
        double d8 = this.expand.getValue().floatValue() * 2.0f;
        while (!this.canPlace(block)) {
            double d9;
            d5 = d;
            d6 = d2;
            d7 += 1.0;
            if (d9 > d8) {
                d7 = d8;
            }
            d5 += (d3 * 0.45 * Math.cos(Math.toRadians(f + 90.0f)) + d4 * 0.45 * Math.sin(Math.toRadians(f + 90.0f))) * d7;
            d6 += (d3 * 0.45 * Math.sin(Math.toRadians(f + 90.0f)) - d4 * 0.45 * Math.cos(Math.toRadians(f + 90.0f))) * d7;
            if (d7 == d8) break;
            blockPos = new BlockPos(d5, Scaffold.mc.field_71439_g.field_70163_u - (double)(Scaffold.mc.field_71474_y.field_74311_E.func_151470_d() && this.down.getValue() != false ? 2 : 1), d6);
            block = Scaffold.mc.field_71441_e.func_180495_p(blockPos).func_177230_c();
        }
        return new double[]{d5, d6};
    }

    public boolean canPlace(Block block) {
        return (block instanceof BlockAir || block instanceof BlockLiquid) && Scaffold.mc.field_71441_e != null && Scaffold.mc.field_71439_g != null && this.pos != null && Scaffold.mc.field_71441_e.func_72839_b(null, new AxisAlignedBB(this.pos)).isEmpty();
    }

    private int getBlockCountHotbar() {
        int n = 0;
        for (int i = 36; i < 45; ++i) {
            if (!Scaffold.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d()) continue;
            ItemStack itemStack = Scaffold.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            Item item = itemStack.func_77973_b();
            if (!(itemStack.func_77973_b() instanceof ItemBlock) || this.invalid.contains((Object)((ItemBlock)item).func_179223_d())) continue;
            n += itemStack.func_190916_E();
        }
        return n;
    }

    private BlockData getBlockData2(BlockPos blockPos) {
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(blockPos.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(blockPos.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(blockPos.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(blockPos.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        BlockPos blockPos2 = blockPos.func_177982_a(-1, 0, 0);
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos2.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(blockPos2.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos2.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(blockPos2.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos2.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos2.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos2.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos2.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos2.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(blockPos2.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos2.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(blockPos2.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos blockPos3 = blockPos.func_177982_a(1, 0, 0);
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos3.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(blockPos3.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos3.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(blockPos3.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos3.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos3.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos3.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos3.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos3.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(blockPos3.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos3.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(blockPos3.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos blockPos4 = blockPos.func_177982_a(0, 0, 1);
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos4.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(blockPos4.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos4.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(blockPos4.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos4.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos4.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos4.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos4.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos4.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(blockPos4.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos4.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(blockPos4.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos blockPos5 = blockPos.func_177982_a(0, 0, -1);
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos5.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(blockPos5.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos5.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(blockPos5.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos5.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos5.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos5.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos5.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos5.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(blockPos5.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos5.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(blockPos5.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos2.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(blockPos2.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos2.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(blockPos2.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos2.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos2.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos2.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos2.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos2.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(blockPos2.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos2.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(blockPos2.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos3.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(blockPos3.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos3.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(blockPos3.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos3.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos3.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos3.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos3.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos3.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(blockPos3.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos3.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(blockPos3.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos4.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(blockPos4.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos4.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(blockPos4.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos4.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos4.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos4.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos4.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos4.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(blockPos4.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos4.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(blockPos4.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos5.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(blockPos5.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos5.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(blockPos5.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos5.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos5.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos5.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos5.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos5.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(blockPos5.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos5.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(blockPos5.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos blockPos10 = blockPos.func_177982_a(0, -1, 0);
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos10.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(blockPos10.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos10.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(blockPos10.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos10.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos10.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos10.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos10.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos10.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(blockPos10.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos10.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(blockPos10.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos blockPos11 = blockPos10.func_177982_a(1, 0, 0);
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos11.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(blockPos11.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos11.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(blockPos11.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos11.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos11.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos11.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos11.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos11.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(blockPos11.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos11.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(blockPos11.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos blockPos12 = blockPos10.func_177982_a(-1, 0, 0);
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos12.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(blockPos12.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos12.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(blockPos12.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos12.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos12.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos12.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos12.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos12.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(blockPos12.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos12.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(blockPos12.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos blockPos13 = blockPos10.func_177982_a(0, 0, 1);
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos13.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(blockPos13.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos13.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos13.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos13.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(blockPos13.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos13.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos13.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos13.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(blockPos13.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos13.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(blockPos13.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos blockPos14 = blockPos10.func_177982_a(0, 0, -1);
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos14.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(blockPos14.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos14.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(blockPos14.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos14.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos14.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos14.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(blockPos14.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos14.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(blockPos14.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos14.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(blockPos14.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        return null;
    }

    public void place(BlockPos blockPos, EnumFacing enumFacing) {
        int n;
        BlockPos blockPos2 = blockPos;
        if (enumFacing == EnumFacing.UP) {
            blockPos2 = blockPos2.func_177982_a(0, -1, 0);
        } else if (enumFacing == EnumFacing.NORTH) {
            blockPos2 = blockPos2.func_177982_a(0, 0, 1);
        } else if (enumFacing == EnumFacing.SOUTH) {
            blockPos2 = blockPos2.func_177982_a(0, 0, -1);
        } else if (enumFacing == EnumFacing.EAST) {
            blockPos2 = blockPos2.func_177982_a(-1, 0, 0);
        } else if (enumFacing == EnumFacing.WEST) {
            blockPos2 = blockPos2.func_177982_a(1, 0, 0);
        }
        int n2 = Scaffold.mc.field_71439_g.field_71071_by.field_70461_c;
        int n3 = -1;
        for (n = 0; n < 9; ++n) {
            ItemStack object = Scaffold.mc.field_71439_g.field_71071_by.func_70301_a(n);
            if (InventoryUtil.isNull(object) || !(object.func_77973_b() instanceof ItemBlock) || !Block.func_149634_a((Item)object.func_77973_b()).func_176223_P().func_185913_b()) continue;
            n3 = n;
            break;
        }
        if (n3 == -1) {
            return;
        }
        n = 0;
        if (!Scaffold.mc.field_71439_g.func_70093_af() && BlockUtil.blackList.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(blockPos2).func_177230_c())) {
            Scaffold.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Scaffold.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            n = 1;
        }
        if (!(Scaffold.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBlock)) {
            Scaffold.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(n3));
            Scaffold.mc.field_71439_g.field_71071_by.field_70461_c = n3;
            Scaffold.mc.field_71442_b.func_78765_e();
        }
        if (Scaffold.mc.field_71474_y.field_74314_A.func_151470_d()) {
            Scaffold.mc.field_71439_g.field_70159_w *= 0.3;
            Scaffold.mc.field_71439_g.field_70179_y *= 0.3;
            Scaffold.mc.field_71439_g.func_70664_aZ();
            if (this.timer.passedMs(1500L)) {
                Scaffold.mc.field_71439_g.field_70181_x = -0.28;
                this.timer.reset();
            }
        }
        if (this.rotation.getValue().booleanValue()) {
            float[] angle = MathUtil.calcAngle(Scaffold.mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((double)((float)this.pos.func_177958_n() + 0.5f), (double)((float)this.pos.func_177956_o() - 0.5f), (double)((float)this.pos.func_177952_p() + 0.5f)));
            Scaffold.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(angle[0], (float)MathHelper.func_180184_b((int)((int)angle[1]), (int)360), Scaffold.mc.field_71439_g.field_70122_E));
        }
        Scaffold.mc.field_71442_b.func_187099_a(Scaffold.mc.field_71439_g, Scaffold.mc.field_71441_e, blockPos2, enumFacing, new Vec3d(0.5, 0.5, 0.5), EnumHand.MAIN_HAND);
        Scaffold.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        Scaffold.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(n2));
        Scaffold.mc.field_71439_g.field_71071_by.field_70461_c = n2;
        Scaffold.mc.field_71442_b.func_78765_e();
        if (n != 0) {
            Scaffold.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Scaffold.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }

    private static class BlockData {
        public BlockPos position;
        public EnumFacing face;

        public BlockData(BlockPos blockPos, EnumFacing enumFacing) {
            this.position = blockPos;
            this.face = enumFacing;
        }
    }

    public static enum Mode {
        New,
        Old;

    }
}

