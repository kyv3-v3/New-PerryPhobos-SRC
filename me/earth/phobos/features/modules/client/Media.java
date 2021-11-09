



package me.earth.phobos.features.modules.client;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;

public class Media extends Module
{
    private static Media instance;
    public final Setting<Boolean> changeOwn;
    public final Setting<String> ownName;
    
    public Media() {
        super("Media", "Helps with creating Media by hiding ur username.", Category.CLIENT, false, false, false);
        this.changeOwn = (Setting<Boolean>)this.register(new Setting("MyName", (T)true));
        this.ownName = (Setting<String>)this.register(new Setting("Name", (T)"Name here...", v -> this.changeOwn.getValue()));
        Media.instance = this;
    }
    
    public static Media getInstance() {
        if (Media.instance == null) {
            Media.instance = new Media();
        }
        return Media.instance;
    }
    
    public static String getPlayerName() {
        if (fullNullCheck() || !PingBypass.getInstance().isConnected()) {
            return Media.mc.getSession().getUsername();
        }
        final String name = PingBypass.getInstance().getPlayerName();
        if (name == null || name.isEmpty()) {
            return Media.mc.getSession().getUsername();
        }
        return name;
    }
}
