//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.*;

public class StairSpeed extends Module
{
    public StairSpeed() {
        super("StairSpeed", "Makes u automatically jump up stairs to go faster.", Module.Category.MOVEMENT, true, false, false);
    }
    
    public void onUpdate() {
        if (StairSpeed.mc.player.onGround && StairSpeed.mc.player.posY - Math.floor(StairSpeed.mc.player.posY) > 0.0 && StairSpeed.mc.player.moveForward != 0.0f) {
            StairSpeed.mc.player.jump();
        }
    }
}
