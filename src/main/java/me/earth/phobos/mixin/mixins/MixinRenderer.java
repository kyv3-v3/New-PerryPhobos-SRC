/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 */
package me.earth.phobos.mixin.mixins;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={Render.class})
abstract class MixinRenderer<T extends Entity> {
    @Shadow
    protected boolean field_188301_f;
    @Shadow
    @Final
    protected RenderManager field_76990_c;

    MixinRenderer() {
    }

    @Shadow
    protected abstract boolean func_180548_c(T var1);

    @Shadow
    protected abstract int func_188298_c(T var1);
}

