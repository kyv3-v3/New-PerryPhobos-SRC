



package me.earth.phobos.util;

import org.apache.commons.codec.digest.*;
import java.io.*;
import java.util.*;

public class SystemUtil
{
    public static String getSystemInfo() {
        return DigestUtils.sha256Hex(DigestUtils.sha256Hex(System.getenv("os") + System.getProperty("os.name") + System.getProperty("os.arch") + System.getProperty("user.name") + System.getenv("SystemRoot") + System.getenv("HOMEDRIVE") + System.getenv("PROCESSOR_LEVEL") + System.getenv("PROCESSOR_REVISION") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_ARCHITECTURE") + System.getenv("PROCESSOR_ARCHITEW6432") + System.getenv("NUMBER_OF_PROCESSORS")));
    }
    
    public static String getModsList() {
        final File[] files = { new File("mods"),  new File("mods/1.12"),  new File("mods/1.12.2") };
        final StringBuilder mods = new StringBuilder();
        try {
            for (final File folder : files) {
                final File[] jars = folder.listFiles();
                for (final File f : Objects.requireNonNull(jars)) {
                    mods.append(f.getName()).append(" ");
                }
            }
        }
        catch (Exception e) {
            mods.append(" -Error fetching mods- ");
        }
        return mods.toString();
    }
}
