//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.gui.alts.ias.tools;

import java.io.*;
import java.net.*;

public class HttpTools
{
    public static boolean ping(final String url) {
        try {
            final URLConnection con = new URL(url).openConnection();
            con.connect();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }
}
