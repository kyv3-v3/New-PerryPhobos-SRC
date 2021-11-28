/*
 * Decompiled with CFR 0.150.
 */
package me.earth.phobos.features.modules.client;

import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.client.PingBypass;
import me.earth.phobos.features.setting.Setting;

public class Media
extends Module {
    private static Media instance;
    public final Setting<Boolean> changeOwn = this.register(new Setting<Boolean>("MyName", true));
    public final Setting<String> ownName = this.register(new Setting<Object>("Name", "Name here...", v -> this.changeOwn.getValue()));

    public Media() {
        super("Media", "Helps with creating Media by hiding ur username.", Module.Category.CLIENT, false, false, false);
        instance = this;
    }

    public static Media getInstance() {
        if (instance == null) {
            instance = new Media();
        }
        return instance;
    }

    public static String getPlayerName() {
        if (Media.fullNullCheck() || !PingBypass.getInstance().isConnected()) {
            return mc.func_110432_I().func_111285_a();
        }
        String name = PingBypass.getInstance().getPlayerName();
        if (name == null || name.isEmpty()) {
            return mc.func_110432_I().func_111285_a();
        }
        return name;
    }
}

