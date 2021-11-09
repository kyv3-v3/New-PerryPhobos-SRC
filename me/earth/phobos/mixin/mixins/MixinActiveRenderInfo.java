//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.earth.phobos.util.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ ActiveRenderInfo.class })
public class MixinActiveRenderInfo
{
    @Inject(method = { "updateRenderInfo(Lnet/minecraft/entity/Entity;Z)V" }, at = { @At("HEAD") }, remap = false)
    private static void updateRenderInfo(final Entity entity, final boolean wtf, final CallbackInfo ci) {
        RenderUtil.updateModelViewProjectionMatrix();
    }
}
