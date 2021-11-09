//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.*;

public class RPC extends Module
{
    public static RPC INSTANCE;
    public Setting<Boolean> catMode;
    public Setting<Boolean> showIP;
    public Setting<String> state;
    
    public RPC() {
        super("RPC", "Discord rich presence.", Category.MISC, false, false, false);
        this.catMode = (Setting<Boolean>)this.register(new Setting("CatMode", (T)false));
        this.showIP = (Setting<Boolean>)this.register(new Setting("ShowIP", (T)true, "Shows the server IP in your discord presence."));
        this.state = (Setting<String>)this.register(new Setting("State", (T)"Perry's Phobos 1.9.0", "Sets the state of the DiscordRPC."));
        RPC.INSTANCE = this;
    }
    
    @Override
    public void onEnable() {
        DiscordPresence.start();
    }
    
    @Override
    public void onDisable() {
        DiscordPresence.stop();
    }
}
