/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.Session
 */
package me.earth.phobos.features.gui.alts;

import java.lang.reflect.Field;
import me.earth.phobos.features.gui.alts.tools.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class MR {
    public static void init() {
        Config.load();
    }

    public static void setSession(Session s) throws Exception {
        Class<?> mc = Minecraft.func_71410_x().getClass();
        try {
            Field session = null;
            for (Field f : mc.getDeclaredFields()) {
                if (!f.getType().isInstance((Object)s)) continue;
                session = f;
                System.out.println("Found field " + f + ", injecting...");
            }
            if (session == null) {
                throw new IllegalStateException("No field of type " + Session.class.getCanonicalName() + " declared.");
            }
            session.setAccessible(true);
            session.set((Object)Minecraft.func_71410_x(), (Object)s);
            session.setAccessible(false);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}

