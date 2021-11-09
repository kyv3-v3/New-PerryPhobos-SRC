



package me.earth.phobos.features.gui.alts.ias.tools;

import java.io.*;
import java.net.*;

public class HttpTools
{
    public static boolean ping(final String url) {
        try {
            final URLConnection con = new URL(url).openConnection();
            con.connect();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }
}
