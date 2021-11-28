/*
 * Decompiled with CFR 0.150.
 */
package me.earth.phobos.features.modules.movement;

import me.earth.phobos.Phobos;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;

public class ReverseStep
extends Module {
    private final Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.NEW));
    int delay;

    public ReverseStep() {
        super("ReverseStep", "Makes u fall faster.", Module.Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (this.delay > 0) {
            --this.delay;
        }
        if (this.mode.getValue() == Mode.NEW) {
            if (ReverseStep.mc.field_71439_g.field_70181_x > (double)-0.06f) {
                this.delay = 10;
            }
            if (ReverseStep.mc.field_71439_g.field_70143_R > 0.0f && ReverseStep.mc.field_71439_g.field_70143_R < 1.0f && this.delay == 0 && !Phobos.moduleManager.isModuleEnabled("Strafe") && !ReverseStep.mc.field_71439_g.func_70090_H() && !ReverseStep.mc.field_71439_g.func_180799_ab()) {
                ReverseStep.mc.field_71439_g.field_70181_x = -3.9200038147008747;
            }
        }
        if (!(this.mode.getValue() != Mode.OLD || ReverseStep.mc.field_71439_g == null || ReverseStep.mc.field_71441_e == null || !ReverseStep.mc.field_71439_g.field_70122_E || ReverseStep.mc.field_71439_g.func_70093_af() || ReverseStep.mc.field_71439_g.func_70090_H() || ReverseStep.mc.field_71439_g.field_70128_L || ReverseStep.mc.field_71439_g.func_180799_ab() || ReverseStep.mc.field_71439_g.func_70617_f_() || ReverseStep.mc.field_71439_g.field_70145_X || ReverseStep.mc.field_71474_y.field_74311_E.func_151470_d() || ReverseStep.mc.field_71474_y.field_74314_A.func_151470_d() || Phobos.moduleManager.isModuleEnabled("Strafe") || !ReverseStep.mc.field_71439_g.field_70122_E)) {
            ReverseStep.mc.field_71439_g.field_70181_x -= 1.0;
        }
    }

    private static enum Mode {
        OLD,
        NEW;

    }
}

