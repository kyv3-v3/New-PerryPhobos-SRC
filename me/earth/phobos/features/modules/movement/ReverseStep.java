



package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.*;
import net.minecraft.client.entity.*;

public class ReverseStep extends Module
{
    private final Setting<Mode> mode;
    int delay;
    
    public ReverseStep() {
        super("ReverseStep", "Makes u fall faster.", Module.Category.MOVEMENT, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.NEW));
    }
    
    public void onUpdate() {
        if (this.delay > 0) {
            --this.delay;
        }
        if (this.mode.getValue() == Mode.NEW) {
            if (ReverseStep.mc.player.motionY > -0.05999999865889549) {
                this.delay = 10;
            }
            if (ReverseStep.mc.player.fallDistance > 0.0f && ReverseStep.mc.player.fallDistance < 1.0f && this.delay == 0 && !Phobos.moduleManager.isModuleEnabled("Strafe") && !ReverseStep.mc.player.isInWater() && !ReverseStep.mc.player.isInLava()) {
                ReverseStep.mc.player.motionY = -3.9200038147008747;
            }
        }
        if (this.mode.getValue() == Mode.OLD && ReverseStep.mc.player != null && ReverseStep.mc.world != null && ReverseStep.mc.player.onGround && !ReverseStep.mc.player.isSneaking() && !ReverseStep.mc.player.isInWater() && !ReverseStep.mc.player.isDead && !ReverseStep.mc.player.isInLava() && !ReverseStep.mc.player.isOnLadder() && !ReverseStep.mc.player.noClip && !ReverseStep.mc.gameSettings.keyBindSneak.isKeyDown() && !ReverseStep.mc.gameSettings.keyBindJump.isKeyDown() && !Phobos.moduleManager.isModuleEnabled("Strafe") && ReverseStep.mc.player.onGround) {
            final EntityPlayerSP player = ReverseStep.mc.player;
            --player.motionY;
        }
    }
    
    private enum Mode
    {
        OLD, 
        NEW;
    }
}
