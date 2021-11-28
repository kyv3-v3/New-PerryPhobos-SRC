/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityBoat
 *  net.minecraft.util.math.Vec3d
 */
package me.earth.phobos.mixin.mixins;

import me.earth.phobos.features.modules.movement.BoatFly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={EntityBoat.class})
public abstract class MixinEntityBoat {
    @Shadow
    public abstract double func_70042_X();

    @Inject(method={"applyOrientationToEntity"}, at={@At(value="HEAD")}, cancellable=true)
    public void applyOrientationToEntity(Entity entity, CallbackInfo ci) {
        if (BoatFly.INSTANCE.isEnabled()) {
            ci.cancel();
        }
    }

    @Inject(method={"controlBoat"}, at={@At(value="HEAD")}, cancellable=true)
    public void controlBoat(CallbackInfo ci) {
        if (BoatFly.INSTANCE.isEnabled()) {
            ci.cancel();
        }
    }

    @Inject(method={"updatePassenger"}, at={@At(value="HEAD")}, cancellable=true)
    public void updatePassenger(Entity passenger, CallbackInfo ci) {
        if (BoatFly.INSTANCE.isEnabled() && passenger == Minecraft.func_71410_x().field_71439_g) {
            ci.cancel();
            float f = 0.0f;
            float f1 = (float)((((Entity)this).field_70128_L ? (double)0.01f : this.func_70042_X()) + passenger.func_70033_W());
            Vec3d vec3d = new Vec3d((double)f, 0.0, 0.0).func_178785_b(-(((Entity)this).field_70177_z * ((float)Math.PI / 180) - 1.5707964f));
            passenger.func_70107_b(((Entity)this).field_70165_t + vec3d.field_72450_a, ((Entity)this).field_70163_u + (double)f1, ((Entity)this).field_70161_v + vec3d.field_72449_c);
        }
    }
}

