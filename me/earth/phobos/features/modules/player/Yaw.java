//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.event.events.*;
import net.minecraft.entity.*;
import java.util.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class Yaw extends Module
{
    public Setting<Boolean> lockYaw;
    public Setting<Boolean> byDirection;
    public Setting<Direction> direction;
    public Setting<Integer> yaw;
    public Setting<Boolean> lockPitch;
    public Setting<Integer> pitch;
    
    public Yaw() {
        super("Yaw", "Locks your yaw.", Module.Category.PLAYER, true, false, false);
        this.lockYaw = (Setting<Boolean>)this.register(new Setting("LockYaw", (T)false));
        this.byDirection = (Setting<Boolean>)this.register(new Setting("ByDirection", (T)false));
        this.direction = (Setting<Direction>)this.register(new Setting("Direction", (T)Direction.NORTH, v -> this.byDirection.getValue()));
        this.yaw = (Setting<Integer>)this.register(new Setting("Yaw", (T)0, (T)(-180), (T)180, v -> !this.byDirection.getValue()));
        this.lockPitch = (Setting<Boolean>)this.register(new Setting("LockPitch", (T)false));
        this.pitch = (Setting<Integer>)this.register(new Setting("Pitch", (T)0, (T)(-180), (T)180));
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (this.lockYaw.getValue()) {
            if (this.byDirection.getValue()) {
                switch (this.direction.getValue()) {
                    case NORTH: {
                        this.setYaw(180);
                        break;
                    }
                    case NE: {
                        this.setYaw(225);
                        break;
                    }
                    case EAST: {
                        this.setYaw(270);
                        break;
                    }
                    case SE: {
                        this.setYaw(315);
                        break;
                    }
                    case SOUTH: {
                        this.setYaw(0);
                        break;
                    }
                    case SW: {
                        this.setYaw(45);
                        break;
                    }
                    case WEST: {
                        this.setYaw(90);
                        break;
                    }
                    case NW: {
                        this.setYaw(135);
                        break;
                    }
                }
            }
            else {
                this.setYaw(this.yaw.getValue());
            }
        }
        if (this.lockPitch.getValue()) {
            if (Yaw.mc.player.isRiding()) {
                Objects.requireNonNull(Yaw.mc.player.getRidingEntity()).rotationPitch = this.pitch.getValue();
            }
            Yaw.mc.player.rotationPitch = this.pitch.getValue();
        }
    }
    
    private void setYaw(final int yaw) {
        if (Yaw.mc.player.isRiding()) {
            Objects.requireNonNull(Yaw.mc.player.getRidingEntity()).rotationYaw = (float)yaw;
        }
        Yaw.mc.player.rotationYaw = (float)yaw;
    }
    
    public enum Direction
    {
        NORTH, 
        NE, 
        EAST, 
        SE, 
        SOUTH, 
        SW, 
        WEST, 
        NW;
    }
}
