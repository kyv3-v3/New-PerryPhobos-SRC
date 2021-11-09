//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.mixin.mixins;

import net.minecraft.entity.item.*;
import org.spongepowered.asm.mixin.*;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.earth.phobos.features.modules.movement.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.client.*;
import net.minecraft.util.math.*;

@Mixin({ EntityBoat.class })
public abstract class MixinEntityBoat
{
    @Shadow
    public abstract double getMountedYOffset();
    
    @Inject(method = { "applyOrientationToEntity" }, at = { @At("HEAD") }, cancellable = true)
    public void applyOrientationToEntity(final Entity entity, final CallbackInfo ci) {
        if (BoatFly.INSTANCE.isEnabled()) {
            ci.cancel();
        }
    }
    
    @Inject(method = { "controlBoat" }, at = { @At("HEAD") }, cancellable = true)
    public void controlBoat(final CallbackInfo ci) {
        if (BoatFly.INSTANCE.isEnabled()) {
            ci.cancel();
        }
    }
    
    @Inject(method = { "updatePassenger" }, at = { @At("HEAD") }, cancellable = true)
    public void updatePassenger(final Entity passenger, final CallbackInfo ci) {
        if (BoatFly.INSTANCE.isEnabled() && passenger == Minecraft.getMinecraft().player) {
            ci.cancel();
            final float f = 0.0f;
            final float f2 = (float)((((Entity)this).isDead ? 0.009999999776482582 : this.getMountedYOffset()) + passenger.getYOffset());
            final Vec3d vec3d = new Vec3d((double)f, 0.0, 0.0).rotateYaw(-(((Entity)this).rotationYaw * 0.017453292f - 1.5707964f));
            passenger.setPosition(((Entity)this).posX + vec3d.x, ((Entity)this).posY + f2, ((Entity)this).posZ + vec3d.z);
        }
    }
}
