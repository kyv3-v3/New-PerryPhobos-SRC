



package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.client.*;
import net.minecraft.entity.*;
import net.minecraft.client.network.*;
import java.util.*;
import net.minecraft.network.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.earth.phobos.event.events.*;
import me.earth.phobos.*;
import net.minecraft.network.play.client.*;

public class Godmode extends Module
{
    private final Setting<Boolean> remount;
    public Minecraft mc;
    public Entity entity;
    
    public Godmode() {
        super("Godmode",  "Makes u able to be in godmode via riding entities and can dupe with it.",  Module.Category.PLAYER,  true,  false,  false);
        this.remount = (Setting<Boolean>)this.register(new Setting("Remount", false));
        this.mc = Minecraft.getMinecraft();
    }
    
    public void onEnable() {
        super.onEnable();
        if (this.mc.world != null && this.mc.player.getRidingEntity() != null) {
            this.entity = this.mc.player.getRidingEntity();
            this.mc.renderGlobal.loadRenderers();
            this.hideEntity();
            this.mc.player.setPosition((double)Minecraft.getMinecraft().player.getPosition().getX(),  (double)(Minecraft.getMinecraft().player.getPosition().getY() - 1),  (double)Minecraft.getMinecraft().player.getPosition().getZ());
        }
        if (this.mc.world != null && this.remount.getValue()) {
            this.remount.setValue(false);
        }
    }
    
    public void onDisable() {
        super.onDisable();
        if (this.remount.getValue()) {
            this.remount.setValue(false);
        }
        this.mc.player.dismountRidingEntity();
        Objects.requireNonNull(this.mc.getConnection()).sendPacket((Packet)new CPacketEntityAction((Entity)this.mc.player,  CPacketEntityAction.Action.START_SNEAKING));
        this.mc.getConnection().sendPacket((Packet)new CPacketEntityAction((Entity)this.mc.player,  CPacketEntityAction.Action.STOP_SNEAKING));
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer.Position || event.getPacket() instanceof CPacketPlayer.PositionRotation) {
            event.setCanceled(true);
        }
    }
    
    private void hideEntity() {
        if (this.mc.player.getRidingEntity() != null) {
            this.mc.player.dismountRidingEntity();
            this.mc.world.removeEntity(this.entity);
        }
    }
    
    private void showEntity(final Entity entity2) {
        entity2.isDead = false;
        this.mc.world.loadedEntityList.add(entity2);
        this.mc.player.startRiding(entity2,  true);
    }
    
    @SubscribeEvent
    public void onPlayerWalkingUpdate(final UpdateWalkingPlayerEvent event) {
        if (this.entity == null) {
            return;
        }
        if (event.getStage() == 0) {
            if (this.remount.getValue() && Objects.requireNonNull((Godmode)Phobos.moduleManager.getModuleByClass((Class<T>)Godmode.class)).isEnabled()) {
                this.showEntity(this.entity);
            }
            this.entity.setPositionAndRotation(Minecraft.getMinecraft().player.posX,  Minecraft.getMinecraft().player.posY,  Minecraft.getMinecraft().player.posZ,  Minecraft.getMinecraft().player.rotationYaw,  Minecraft.getMinecraft().player.rotationPitch);
            this.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(this.mc.player.rotationYaw,  this.mc.player.rotationPitch,  true));
            this.mc.player.connection.sendPacket((Packet)new CPacketInput(this.mc.player.movementInput.moveForward,  this.mc.player.movementInput.moveStrafe,  false,  false));
            this.mc.player.connection.sendPacket((Packet)new CPacketVehicleMove(this.entity));
        }
    }
}
