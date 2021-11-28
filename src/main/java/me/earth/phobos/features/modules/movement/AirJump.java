/*
 * Decompiled with CFR 0.150.
 */
package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.Module;

public class AirJump
extends Module {
    public AirJump() {
        super("AirJump", "Makes it possible to jump while ur in the air.", Module.Category.MOVEMENT, false, false, false);
    }

    @Override
    public void onUpdate() {
        AirJump.mc.field_71439_g.field_70122_E = true;
    }
}

