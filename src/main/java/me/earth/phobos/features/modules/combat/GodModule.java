/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemEndCrystal
 *  net.minecraft.item.ItemExpBottle
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.network.play.server.SPacketSpawnExperienceOrb
 *  net.minecraft.network.play.server.SPacketSpawnGlobalEntity
 *  net.minecraft.network.play.server.SPacketSpawnMob
 *  net.minecraft.network.play.server.SPacketSpawnObject
 *  net.minecraft.network.play.server.SPacketSpawnPainting
 *  net.minecraft.network.play.server.SPacketSpawnPlayer
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.earth.phobos.features.modules.combat;

import java.util.concurrent.TimeUnit;
import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.features.Feature;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.BlockUtil;
import me.earth.phobos.util.MathUtil;
import me.earth.phobos.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GodModule
extends Module {
    public Setting rotations = this.register(new Setting<Integer>("Spoofs", 1, 1, 20));
    public Setting rotate = this.register(new Setting<Boolean>("Rotate", false));
    public Setting render = this.register(new Setting<Boolean>("Render", false));
    public Setting antiIllegal = this.register(new Setting<Boolean>("AntiIllegal", true));
    public Setting checkPos = this.register(new Setting<Boolean>("CheckPos", false));
    public Setting oneDot15 = this.register(new Setting<Boolean>("1.15", false));
    public Setting entitycheck = this.register(new Setting<Boolean>("EntityCheck", false));
    public Setting attacks = this.register(new Setting<Integer>("Attacks", 1, 1, 10));
    public Setting offset = this.register(new Setting<Integer>("Offset", 0, 0, 2));
    public Setting delay = this.register(new Setting<Integer>("Delay", 0, 0, 250));
    private float yaw;
    private float pitch;
    private boolean rotating;
    private int rotationPacketsSpoofed;
    private int highestID = -100000;

    public GodModule() {
        super("GodModule", "Predicts the entity id to make ur ca insane (Only works on a few servers).", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onToggle() {
        this.resetFields();
        if (GodModule.mc.field_71441_e != null) {
            this.updateEntityID();
        }
    }

    @Override
    public void onUpdate() {
        if (((Boolean)this.render.getValue()).booleanValue()) {
            for (Entity entity : GodModule.mc.field_71441_e.field_72996_f) {
                if (entity instanceof EntityItem) {
                    return;
                }
                if (entity instanceof EntityPlayer) {
                    return;
                }
                if (!(entity instanceof EntityEnderCrystal)) continue;
                entity.func_96094_a(String.valueOf(entity.field_145783_c));
                entity.func_174805_g(true);
            }
        }
    }

    @Override
    public void onLogout() {
        this.resetFields();
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onSendPacket(PacketEvent.Send event) {
        CPacketPlayerTryUseItemOnBlock packet;
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            packet = (CPacketPlayerTryUseItemOnBlock)event.getPacket();
            if (GodModule.mc.field_71439_g.func_184586_b(packet.field_187027_c).func_77973_b() instanceof ItemEndCrystal) {
                if (((Boolean)this.checkPos.getValue()).booleanValue() && !BlockUtil.canPlaceCrystal(packet.field_179725_b, (Boolean)this.entitycheck.getValue(), (Boolean)this.oneDot15.getValue(), false) || this.checkPlayers()) {
                    return;
                }
                this.updateEntityID();
                for (int i = 1 - (Integer)this.offset.getValue(); i <= (Integer)this.attacks.getValue(); ++i) {
                    this.attackID(packet.field_179725_b, this.highestID + i);
                }
            }
        }
        if (event.getStage() == 0 && this.rotating && ((Boolean)this.rotate.getValue()).booleanValue() && event.getPacket() instanceof CPacketPlayer) {
            packet = (CPacketPlayer)event.getPacket();
            packet.field_149476_e = this.yaw;
            packet.field_149473_f = this.pitch;
            ++this.rotationPacketsSpoofed;
            if (this.rotationPacketsSpoofed >= (Integer)this.rotations.getValue()) {
                this.rotating = false;
                this.rotationPacketsSpoofed = 0;
            }
        }
    }

    private void attackID(BlockPos pos, int id) {
        Entity entity = GodModule.mc.field_71441_e.func_73045_a(id);
        if (entity instanceof EntityItem) {
            return;
        }
        if (entity instanceof EntityPlayer) {
            return;
        }
        if (entity == null || entity instanceof EntityEnderCrystal) {
            AttackThread attackThread = new AttackThread(id, pos, (Integer)this.delay.getValue(), this);
            if ((Integer)this.delay.getValue() == 0) {
                attackThread.run();
            } else {
                attackThread.start();
            }
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSpawnObject) {
            this.checkID(((SPacketSpawnObject)event.getPacket()).func_149001_c());
        } else if (event.getPacket() instanceof SPacketSpawnExperienceOrb) {
            this.checkID(((SPacketSpawnExperienceOrb)event.getPacket()).func_148985_c());
        } else if (event.getPacket() instanceof SPacketSpawnPlayer) {
            this.checkID(((SPacketSpawnPlayer)event.getPacket()).func_148943_d());
        } else if (event.getPacket() instanceof SPacketSpawnGlobalEntity) {
            this.checkID(((SPacketSpawnGlobalEntity)event.getPacket()).func_149052_c());
        } else if (event.getPacket() instanceof SPacketSpawnPainting) {
            this.checkID(((SPacketSpawnPainting)event.getPacket()).func_148965_c());
        } else if (event.getPacket() instanceof SPacketSpawnMob) {
            this.checkID(((SPacketSpawnMob)event.getPacket()).func_149024_d());
        }
    }

    private void checkID(int id) {
        if (id > this.highestID) {
            this.highestID = id;
        }
    }

    public void updateEntityID() {
        for (Entity entity : GodModule.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityItem) {
                return;
            }
            if (entity instanceof EntityPlayer) {
                return;
            }
            if (entity.func_145782_y() <= this.highestID) continue;
            this.highestID = entity.func_145782_y();
        }
    }

    private boolean checkPlayers() {
        if (((Boolean)this.antiIllegal.getValue()).booleanValue()) {
            for (EntityPlayer player : GodModule.mc.field_71441_e.field_73010_i) {
                if (player.func_110143_aJ() > 0.0f && !this.checkItem(player.func_184614_ca()) && !this.checkItem(player.func_184592_cb())) continue;
                return false;
            }
        }
        return true;
    }

    private boolean checkItem(ItemStack stack) {
        return stack.func_77973_b() instanceof ItemBow || stack.func_77973_b() instanceof ItemExpBottle || stack.func_77973_b() == Items.field_151007_F;
    }

    public void rotateTo(BlockPos pos) {
        float[] angle = MathUtil.calcAngle(GodModule.mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((Vec3i)pos));
        this.yaw = angle[0];
        this.pitch = angle[1];
        this.rotating = true;
    }

    private void resetFields() {
        this.rotating = false;
        this.highestID = -1000000;
    }

    public static class AttackThread
    extends Thread {
        private final BlockPos pos;
        private final int id;
        private final int delay;
        private final GodModule godModule;

        public AttackThread(int idIn, BlockPos posIn, int delayIn, GodModule godModuleIn) {
            this.id = idIn;
            this.pos = posIn;
            this.delay = delayIn;
            this.godModule = godModuleIn;
        }

        @Override
        public void run() {
            try {
                if (this.delay != 1) {
                    TimeUnit.MILLISECONDS.sleep(this.delay);
                }
                Util.mc.func_152344_a(() -> {
                    if (!Feature.fullNullCheck()) {
                        CPacketUseEntity attack = new CPacketUseEntity();
                        attack.field_149567_a = this.id;
                        attack.field_149566_b = CPacketUseEntity.Action.ATTACK;
                        this.godModule.rotateTo(this.pos.func_177984_a());
                        Util.mc.field_71439_g.field_71174_a.func_147297_a((Packet)attack);
                        Util.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
                    }
                });
            }
            catch (InterruptedException var2) {
                var2.printStackTrace();
            }
        }
    }
}

