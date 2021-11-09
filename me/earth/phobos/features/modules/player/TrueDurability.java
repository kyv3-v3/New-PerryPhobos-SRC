//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.*;

public class TrueDurability extends Module
{
    private static TrueDurability instance;
    
    public TrueDurability() {
        super("TrueDurability", "Shows True Durability of items.", Module.Category.PLAYER, false, false, false);
        TrueDurability.instance = this;
    }
    
    public static TrueDurability getInstance() {
        if (TrueDurability.instance == null) {
            TrueDurability.instance = new TrueDurability();
        }
        return TrueDurability.instance;
    }
}
