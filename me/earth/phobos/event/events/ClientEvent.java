//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.event.events;

import me.earth.phobos.event.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.earth.phobos.features.*;
import me.earth.phobos.features.setting.*;

@Cancelable
public class ClientEvent extends EventStage
{
    private Feature feature;
    private Setting setting;
    
    public ClientEvent(final int stage, final Feature feature) {
        super(stage);
        this.feature = feature;
    }
    
    public ClientEvent(final Setting setting) {
        super(2);
        this.setting = setting;
    }
    
    public Feature getFeature() {
        return this.feature;
    }
    
    public Setting getSetting() {
        return this.setting;
    }
}
