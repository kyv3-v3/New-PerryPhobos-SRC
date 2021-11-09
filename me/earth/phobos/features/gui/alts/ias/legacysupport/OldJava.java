//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.gui.alts.ias.legacysupport;

import net.minecraft.client.resources.*;

public class OldJava implements ILegacyCompat
{
    public int[] getDate() {
        return new int[3];
    }
    
    public String getFormattedDate() {
        return I18n.format("ias.updatejava", new Object[0]);
    }
}
