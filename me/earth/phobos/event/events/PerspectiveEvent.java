



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
