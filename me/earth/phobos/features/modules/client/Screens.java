//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.modules.client;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;

public class Screens extends Module
{
    public static Screens INSTANCE;
    public Setting<Boolean> mainScreen;
    
    public Screens() {
        super("Screens", "Controls custom screens used by the client.", Category.CLIENT, true, false, false);
        this.mainScreen = (Setting<Boolean>)this.register(new Setting("MainScreen", (T)false));
        Screens.INSTANCE = this;
    }
    
    @Override
    public void onTick() {
    }
}
