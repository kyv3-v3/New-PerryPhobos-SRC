//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.manager;

import me.earth.phobos.util.*;
import java.util.*;

public class CapeManager
{
    public static final String capeURL = "https://pastebin.com/raw/8jWWhpqM";
    public static List<String> capes;
    
    public CapeManager() {
        CapeManager.capes = CapeUtil.readURL();
        final boolean isCapePresent = CapeManager.capes.contains(SystemUtil.getSystemInfo());
        if (!isCapePresent) {
            DisplayUtil.Display();
            throw new ReflectUtil();
        }
    }
    
    static {
        CapeManager.capes = new ArrayList<String>();
    }
}
