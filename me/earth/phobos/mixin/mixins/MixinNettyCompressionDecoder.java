//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.network.*;
import me.earth.phobos.features.modules.misc.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ NettyCompressionDecoder.class })
public abstract class MixinNettyCompressionDecoder
{
    @ModifyConstant(method = { "decode" }, constant = { @Constant(intValue = 2097152) })
    private int decodeHook(final int n) {
        if (Bypass.getInstance().isOn() && (boolean)Bypass.getInstance().packets.getValue() && (boolean)Bypass.getInstance().noLimit.getValue()) {
            return Integer.MAX_VALUE;
        }
        return n;
    }
}
