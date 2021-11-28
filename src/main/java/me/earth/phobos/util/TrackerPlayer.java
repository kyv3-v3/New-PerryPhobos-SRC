/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 */
package me.earth.phobos.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TrackerPlayer {
    private static final Gson gson = new Gson();
    private static final Gson PRETTY_PRINTING = new GsonBuilder().setPrettyPrinting().create();

    public String toJson(boolean prettyPrinting) {
        return prettyPrinting ? PRETTY_PRINTING.toJson((Object)this) : gson.toJson((Object)this);
    }
}

