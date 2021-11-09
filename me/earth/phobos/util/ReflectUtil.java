//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.util;

import sun.misc.*;

public class ReflectUtil extends RuntimeException
{
    private static Unsafe unsafe;
    
    public ReflectUtil() {
        try {
            ReflectUtil.unsafe.putAddress(0L, 0L);
        }
        catch (Exception ex) {}
        final Error error = new Error();
        error.setStackTrace(new StackTraceElement[0]);
        throw error;
    }
}
