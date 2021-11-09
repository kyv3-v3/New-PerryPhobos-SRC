



package me.earth.phobos.util;

import java.util.*;
import java.net.*;
import java.io.*;

public class CapeUtil
{
    public static List<String> readURL() {
        final List<String> s = new ArrayList<String>();
        try {
            final URL url = new URL("https://pastebin.com/raw/8jWWhpqM");
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String cape;
            while ((cape = bufferedReader.readLine()) != null) {
                s.add(cape);
            }
        }
        catch (Exception ex) {}
        return s;
    }
}
