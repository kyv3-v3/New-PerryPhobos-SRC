



package me.earth.phobos.manager;

import net.minecraft.launchwrapper.*;
import me.earth.phobos.util.*;

public class ClassManager
{
    public ClassManager() {
        try {
            if (Launch.classLoader.findClass("cat.yoink.dumper.Main") != null || Launch.classLoader.findClass("me.crystallinqq.dumper") != null || Launch.classLoader.findClass("tech.mmmax.dumper") != null || Launch.classLoader.findClass("fuck.you.multihryack") != null) {
                throw new ReflectUtil();
            }
        }
        catch (ClassNotFoundException ex) {}
    }
}
