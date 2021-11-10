



package me.earth.phobos.features.gui.alts.ias.tools;

import me.earth.phobos.features.gui.alts.ias.legacysupport.*;

public class JavaTools
{
    private static double getJavaVersion() {
        final String version = System.getProperty("java.version");
        int pos = version.indexOf(46);
        pos = version.indexOf(46,  pos + 1);
        return Double.parseDouble(version.substring(0,  pos));
    }
    
    public static ILegacyCompat getJavaCompat() {
        if (getJavaVersion() >= 1.8) {
            return (ILegacyCompat)new NewJava();
        }
        return (ILegacyCompat)new OldJava();
    }
}
