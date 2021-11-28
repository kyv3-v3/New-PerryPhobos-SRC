/*
 * Decompiled with CFR 0.150.
 */
package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.MathUtil;

public class VanillaSpeed
extends Module {
    public Setting<Double> speed = this.register(new Setting<Double>("Speed", 1.0, 1.0, 20.0));

    public VanillaSpeed() {
        super("VanillaSpeed", "Speed for vanilla ac's (old ec.me).", Module.Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (VanillaSpeed.mc.field_71439_g == null || VanillaSpeed.mc.field_71441_e == null) {
            return;
        }
        double[] calc = MathUtil.directionSpeed(this.speed.getValue() / 10.0);
        VanillaSpeed.mc.field_71439_g.field_70159_w = calc[0];
        VanillaSpeed.mc.field_71439_g.field_70179_y = calc[1];
    }
}

