



package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.*;

public class AirJump extends Module
{
    public AirJump() {
        super("AirJump",  "Makes it possible to jump while ur in the air.",  Module.Category.MOVEMENT,  false,  false,  false);
    }
    
    public void onUpdate() {
        AirJump.mc.player.onGround = true;
    }
}
