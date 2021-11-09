//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.gui.alts.tools;

import java.io.*;

public class Pair<V1, V2> implements Serializable
{
    private static final long serialVersionUID = 2586850598481149380L;
    private final V1 obj1;
    private final V2 obj2;
    
    public Pair(final V1 obj1, final V2 obj2) {
        this.obj1 = obj1;
        this.obj2 = obj2;
    }
    
    public V1 getValue1() {
        return this.obj1;
    }
    
    public V2 getValue2() {
        return this.obj2;
    }
    
    @Override
    public String toString() {
        return Pair.class.getName() + "@" + Integer.toHexString(this.hashCode()) + " [" + this.obj1.toString() + ", " + this.obj2.toString() + "]";
    }
}
