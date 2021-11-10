



package me.earth.phobos.features.gui.alts;

import me.earth.phobos.features.gui.alts.tools.*;
import net.minecraft.util.*;
import net.minecraft.client.*;
import java.lang.reflect.*;

public class MR
{
    public static void init() {
        Config.load();
    }
    
    public static void setSession(final Session s) throws Exception {
        final Class<? extends Minecraft> mc = Minecraft.getMinecraft().getClass();
        try {
            Field session = null;
            for (final Field f : mc.getDeclaredFields()) {
                if (f.getType().isInstance(s)) {
                    session = f;
                    System.out.println("Found field " + f + ",  injecting...");
                }
            }
            if (session == null) {
                throw new IllegalStateException("No field of type " + Session.class.getCanonicalName() + " declared.");
            }
            session.setAccessible(true);
            session.set(Minecraft.getMinecraft(),  s);
            session.setAccessible(false);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
