



package me.earth.phobos.features.modules.combat;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.item.*;
import java.util.*;
import me.earth.phobos.event.events.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.network.play.server.*;
import net.minecraft.item.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import java.util.concurrent.*;
import me.earth.phobos.util.*;
import me.earth.phobos.features.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;

public class GodModule extends Module
{
    public Setting rotations;
    public Setting rotate;
    public Setting render;
    public Setting antiIllegal;
    public Setting checkPos;
    public Setting oneDot15;
    public Setting entitycheck;
    public Setting attacks;
    public Setting offset;
    public Setting delay;
    private float yaw;
    private float pitch;
    private boolean rotating;
    private int rotationPacketsSpoofed;
    private int highestID;
    
    public GodModule() {
        super("GodModule",  "Predicts the entity id to make ur ca insane (Only works on a few servers).",  Category.COMBAT,  true,  false,  false);
        this.rotations = this.register(new Setting("Spoofs", 1, 1, 20));
        this.rotate = this.register(new Setting("Rotate", false));
        this.render = this.register(new Setting("Render", false));
        this.antiIllegal = this.register(new Setting("AntiIllegal", true));
        this.checkPos = this.register(new Setting("CheckPos", false));
        this.oneDot15 = this.register(new Setting("1.15", false));
        this.entitycheck = this.register(new Setting("EntityCheck", false));
        this.attacks = this.register(new Setting("Attacks", 1, 1, 10));
        this.offset = this.register(new Setting("Offset", 0, 0, 2));
        this.delay = this.register(new Setting("Delay", 0, 0, 250));
        this.highestID = -100000;
    }
    
    @Override
    public void onToggle() {
        this.resetFields();
        if (GodModule.mc.world != null) {
            this.updateEntityID();
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.render.getValue()) {
            for (final Entity entity : GodModule.mc.world.loadedEntityList) {
                if (entity instanceof EntityItem) {
                    return;
                }
                if (entity instanceof EntityPlayer) {
                    return;
                }
                if (!(entity instanceof EntityEnderCrystal)) {
                    continue;
                }
                entity.setCustomNameTag(String.valueOf(entity.entityId));
                entity.setAlwaysRenderNameTag(true);
            }
        }
    }
    
    @Override
    public void onLogout() {
        this.resetFields();
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onSendPacket(final PacketEvent.Send event) {
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            final CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock)event.getPacket();
            if (GodModule.mc.player.getHeldItem(packet.hand).getItem() instanceof ItemEndCrystal) {
                if ((this.checkPos.getValue() && !BlockUtil.canPlaceCrystal(packet.position,  this.entitycheck.getValue(),  this.oneDot15.getValue(),  false)) || this.checkPlayers()) {
                    return;
                }
                this.updateEntityID();
                for (int i = 1 - this.offset.getValue(); i <= this.attacks.getValue(); ++i) {
                    this.attackID(packet.position,  this.highestID + i);
                }
            }
        }
        if (event.getStage() == 0 && this.rotating && this.rotate.getValue() && event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packet2 = (CPacketPlayer)event.getPacket();
            packet2.yaw = this.yaw;
            packet2.pitch = this.pitch;
            ++this.rotationPacketsSpoofed;
            if (this.rotationPacketsSpoofed >= this.rotations.getValue()) {
                this.rotating = false;
                this.rotationPacketsSpoofed = 0;
            }
        }
    }
    
    private void attackID(final BlockPos pos,  final int id) {
        final Entity entity = GodModule.mc.world.getEntityByID(id);
        if (entity instanceof EntityItem) {
            return;
        }
        if (entity instanceof EntityPlayer) {
            return;
        }
        if (entity == null || entity instanceof EntityEnderCrystal) {
            final AttackThread attackThread = new AttackThread(id,  pos,  this.delay.getValue(),  this);
            if (this.delay.getValue() == 0) {
                attackThread.run();
            }
            else {
                attackThread.start();
            }
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSpawnObject) {
            this.checkID(((SPacketSpawnObject)event.getPacket()).getEntityID());
        }
        else if (event.getPacket() instanceof SPacketSpawnExperienceOrb) {
            this.checkID(((SPacketSpawnExperienceOrb)event.getPacket()).getEntityID());
        }
        else if (event.getPacket() instanceof SPacketSpawnPlayer) {
            this.checkID(((SPacketSpawnPlayer)event.getPacket()).getEntityID());
        }
        else if (event.getPacket() instanceof SPacketSpawnGlobalEntity) {
            this.checkID(((SPacketSpawnGlobalEntity)event.getPacket()).getEntityId());
        }
        else if (event.getPacket() instanceof SPacketSpawnPainting) {
            this.checkID(((SPacketSpawnPainting)event.getPacket()).getEntityID());
        }
        else if (event.getPacket() instanceof SPacketSpawnMob) {
            this.checkID(((SPacketSpawnMob)event.getPacket()).getEntityID());
        }
    }
    
    private void checkID(final int id) {
        if (id > this.highestID) {
            this.highestID = id;
        }
    }
    
    public void updateEntityID() {
        for (final Entity entity : GodModule.mc.world.loadedEntityList) {
            if (entity instanceof EntityItem) {
                return;
            }
            if (entity instanceof EntityPlayer) {
                return;
            }
            if (entity.getEntityId() <= this.highestID) {
                continue;
            }
            this.highestID = entity.getEntityId();
        }
    }
    
    private boolean checkPlayers() {
        if (this.antiIllegal.getValue()) {
            for (final EntityPlayer player : GodModule.mc.world.playerEntities) {
                if (player.getHealth() > 0.0f && !this.checkItem(player.getHeldItemMainhand()) && !this.checkItem(player.getHeldItemOffhand())) {
                    continue;
                }
                return false;
            }
        }
        return true;
    }
    
    private boolean checkItem(final ItemStack stack) {
        return stack.getItem() instanceof ItemBow || stack.getItem() instanceof ItemExpBottle || stack.getItem() == Items.STRING;
    }
    
    public void rotateTo(final BlockPos pos) {
        final float[] angle = MathUtil.calcAngle(GodModule.mc.player.getPositionEyes(GodModule.mc.getRenderPartialTicks()),  new Vec3d((Vec3i)pos));
        this.yaw = angle[0];
        this.pitch = angle[1];
        this.rotating = true;
    }
    
    private void resetFields() {
        this.rotating = false;
        this.highestID = -1000000;
    }
    
    public static class AttackThread extends Thread
    {
        private final BlockPos pos;
        private final int id;
        private final int delay;
        private final GodModule godModule;
        
        public AttackThread(final int idIn,  final BlockPos posIn,  final int delayIn,  final GodModule godModuleIn) {
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
                CPacketUseEntity attack;
                Util.mc.addScheduledTask(() -> {
                    if (!Feature.fullNullCheck()) {
                        attack = new CPacketUseEntity();
                        attack.entityId = this.id;
                        attack.action = CPacketUseEntity.Action.ATTACK;
                        this.godModule.rotateTo(this.pos.up());
                        Util.mc.player.connection.sendPacket((Packet)attack);
                        Util.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
                    }
                });
            }
            catch (InterruptedException var2) {
                var2.printStackTrace();
            }
        }
    }
}
