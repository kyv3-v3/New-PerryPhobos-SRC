



package me.earth.phobos.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.model.*;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.entity.monster.*;
import me.earth.phobos.features.modules.render.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ ModelBiped.class })
public class MixinModelBiped
{
    @Inject(method = { "render" },  at = { @At("HEAD") },  cancellable = true)
    public void render(final Entity entityIn,  final float limbSwing,  final float limbSwingAmount,  final float ageInTicks,  final float netHeadYaw,  final float headPitch,  final float scale,  final CallbackInfo ci) {
        if (entityIn instanceof EntityPigZombie && (boolean)NoRender.getInstance().pigmen.getValue()) {
            ci.cancel();
        }
    }
}
