//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.*;

public class EntityControl extends Module
{
    public static EntityControl INSTANCE;
    
    public EntityControl() {
        super("EntityControl", "Control non saddled entities.", Module.Category.MOVEMENT, false, false, false);
        EntityControl.INSTANCE = this;
    }
}
