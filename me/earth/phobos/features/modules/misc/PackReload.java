//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class PackReload extends Module
{
    public PackReload() {
        super("PackReload", "Automatically reloads ur pack.", Category.MISC, false, false, false);
    }
    
    @SubscribeEvent
    @Override
    public void onTick() {
        PackReload.mc.refreshResources();
        this.disable();
    }
}
