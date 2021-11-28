/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.culling.ICamera
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.AxisAlignedBB
 */
package me.earth.phobos.mixin.mixins;

import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value={Render.class})
public class MixinRender<T extends Entity> {
    @Overwrite
    public boolean func_177071_a(T livingEntity, ICamera camera, double camX, double camY, double camZ) {
        try {
            AxisAlignedBB axisalignedbb = livingEntity.func_184177_bl().func_186662_g(0.5);
            if ((axisalignedbb.func_181656_b() || axisalignedbb.func_72320_b() == 0.0) && livingEntity != null) {
                axisalignedbb = new AxisAlignedBB(((Entity)livingEntity).field_70165_t - 2.0, ((Entity)livingEntity).field_70163_u - 2.0, ((Entity)livingEntity).field_70161_v - 2.0, ((Entity)livingEntity).field_70165_t + 2.0, ((Entity)livingEntity).field_70163_u + 2.0, ((Entity)livingEntity).field_70161_v + 2.0);
            }
            return livingEntity.func_145770_h(camX, camY, camZ) && (((Entity)livingEntity).field_70158_ak || camera.func_78546_a(axisalignedbb));
        }
        catch (Exception ignored) {
            return false;
        }
    }
}

