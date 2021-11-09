//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.gui.alts.tools;

import net.minecraft.client.gui.*;

public class Tools
{
    public static void drawBorderedRect(final int x, final int y, final int x1, final int y1, final int size, final int borderColor, final int insideColor) {
        Gui.drawRect(x + size, y + size, x1 - size, y1 - size, insideColor);
        Gui.drawRect(x + size, y + size, x1, y, borderColor);
        Gui.drawRect(x, y, x + size, y1, borderColor);
        Gui.drawRect(x1, y1, x1 - size, y + size, borderColor);
        Gui.drawRect(x, y1 - size, x1, y1, borderColor);
    }
}
