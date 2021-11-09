



package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.util.math.*;
import me.earth.phobos.*;
import net.minecraft.client.entity.*;

public class Static extends Module
{
    private final Setting<Mode> mode;
    private final Setting<Boolean> disabler;
    private final Setting<Boolean> ySpeed;
    private final Setting<Float> speed;
    private final Setting<Float> height;
    
    public Static() {
        super("Static", "Stops any movement. Glitches you up.", Module.Category.MOVEMENT, false, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.ROOF));
        this.disabler = (Setting<Boolean>)this.register(new Setting("Disable", (T)true, v -> this.mode.getValue() == Mode.ROOF));
        this.ySpeed = (Setting<Boolean>)this.register(new Setting("YSpeed", (T)false, v -> this.mode.getValue() == Mode.STATIC));
        this.speed = (Setting<Float>)this.register(new Setting("Speed", (T)0.1f, (T)0.0f, (T)10.0f, v -> this.ySpeed.getValue() && this.mode.getValue() == Mode.STATIC));
        this.height = (Setting<Float>)this.register(new Setting("Height", (T)3.0f, (T)0.0f, (T)256.0f, v -> this.mode.getValue() == Mode.NOVOID));
    }
    
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        switch (this.mode.getValue()) {
            case STATIC: {
                Static.mc.player.capabilities.isFlying = false;
                Static.mc.player.motionX = 0.0;
                Static.mc.player.motionY = 0.0;
                Static.mc.player.motionZ = 0.0;
                if (!this.ySpeed.getValue()) {
                    break;
                }
                Static.mc.player.jumpMovementFactor = this.speed.getValue();
                if (Static.mc.gameSettings.keyBindJump.isKeyDown()) {
                    final EntityPlayerSP player = Static.mc.player;
                    player.motionY += this.speed.getValue();
                }
                if (!Static.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    break;
                }
                final EntityPlayerSP player2 = Static.mc.player;
                player2.motionY -= this.speed.getValue();
                break;
            }
            case ROOF: {
                Static.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Static.mc.player.posX, 10000.0, Static.mc.player.posZ, Static.mc.player.onGround));
                if (!this.disabler.getValue()) {
                    break;
                }
                this.disable();
                break;
            }
            case NOVOID: {
                if (Static.mc.player.noClip) {
                    break;
                }
                if (Static.mc.player.posY > this.height.getValue()) {
                    break;
                }
                final RayTraceResult trace = Static.mc.world.rayTraceBlocks(Static.mc.player.getPositionVector(), new Vec3d(Static.mc.player.posX, 0.0, Static.mc.player.posZ), false, false, false);
                if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK) {
                    return;
                }
                if (Phobos.moduleManager.isModuleEnabled((Class<? extends Module>)Phase.class) || Phobos.moduleManager.isModuleEnabled((Class<? extends Module>)Flight.class)) {
                    return;
                }
                Static.mc.player.setVelocity(0.0, 0.0, 0.0);
                if (Static.mc.player.getRidingEntity() == null) {
                    break;
                }
                Static.mc.player.getRidingEntity().setVelocity(0.0, 0.0, 0.0);
                break;
            }
        }
    }
    
    public String getDisplayInfo() {
        if (this.mode.getValue() == Mode.ROOF) {
            return "Roof";
        }
        if (this.mode.getValue() == Mode.NOVOID) {
            return "NoVoid";
        }
        return null;
    }
    
    public enum Mode
    {
        STATIC, 
        ROOF, 
        NOVOID;
    }
}
