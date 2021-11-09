//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.gui.chat.utils;

public class AnimationTools
{
    public static float clamp(final float number, final float min, final float max) {
        return (number < min) ? min : Math.min(number, max);
    }
}
