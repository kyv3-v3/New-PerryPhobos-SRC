/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.earth.phobos.features.modules.movement;

import me.earth.phobos.event.events.MoveEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FastSwim
extends Module {
    public Setting<Double> waterHorizontal = this.register(new Setting<Double>("WaterHorizontal", 3.0, 1.0, 20.0));
    public Setting<Double> waterVertical = this.register(new Setting<Double>("WaterVertical", 3.0, 1.0, 20.0));
    public Setting<Double> lavaHorizontal = this.register(new Setting<Double>("LavaHorizontal", 4.0, 1.0, 20.0));
    public Setting<Double> lavaVertical = this.register(new Setting<Double>("LavaVertical", 4.0, 1.0, 20.0));

    public FastSwim() {
        super("FastSwim", "Swim faster.", Module.Category.MOVEMENT, true, false, false);
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (FastSwim.mc.field_71439_g.field_70122_E || FastSwim.mc.field_71439_g.func_184613_cA()) {
            return;
        }
        if (FastSwim.mc.field_71439_g.func_180799_ab()) {
            event.setX(event.getX() * this.lavaHorizontal.getValue());
            event.setZ(event.getZ() * this.lavaHorizontal.getValue());
            event.setY(event.getY() * this.lavaVertical.getValue());
        } else if (FastSwim.mc.field_71439_g.func_70090_H()) {
            event.setX(event.getX() * this.waterHorizontal.getValue());
            event.setZ(event.getZ() * this.waterHorizontal.getValue());
            event.setY(event.getY() * this.waterVertical.getValue());
        }
    }
}

