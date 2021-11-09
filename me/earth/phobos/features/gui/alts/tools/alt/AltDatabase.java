//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.gui.alts.tools.alt;

import java.io.*;
import java.util.*;
import me.earth.phobos.features.gui.alts.tools.*;

public class AltDatabase implements Serializable
{
    public static final long serialVersionUID = -1585600597L;
    private static AltDatabase instance;
    private final ArrayList<AccountData> altList;
    
    private AltDatabase() {
        this.altList = new ArrayList<AccountData>();
    }
    
    private static void loadFromConfig() {
        if (AltDatabase.instance == null) {
            AltDatabase.instance = (AltDatabase)Config.getInstance().getKey("altaccounts");
        }
    }
    
    private static void saveToConfig() {
        Config.getInstance().setKey("altaccounts", AltDatabase.instance);
    }
    
    public static AltDatabase getInstance() {
        loadFromConfig();
        if (AltDatabase.instance == null) {
            AltDatabase.instance = new AltDatabase();
            saveToConfig();
        }
        return AltDatabase.instance;
    }
    
    public ArrayList<AccountData> getAlts() {
        return this.altList;
    }
}
