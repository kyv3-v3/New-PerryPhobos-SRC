//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.event.events;

import me.earth.phobos.event.*;

public class PerspectiveEvent extends EventStage
{
    private float aspect;
    
    public PerspectiveEvent(final float f) {
        this.aspect = f;
    }
    
    public float getAspect() {
        return this.aspect;
    }
    
    public void setAspect(final float f) {
        this.aspect = f;
    }
}
