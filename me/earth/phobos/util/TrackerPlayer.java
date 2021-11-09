



package me.earth.phobos.util;

import com.google.gson.*;

public class TrackerPlayer
{
    private static final Gson gson;
    private static final Gson PRETTY_PRINTING;
    
    public String toJson(final boolean prettyPrinting) {
        return prettyPrinting ? TrackerPlayer.PRETTY_PRINTING.toJson((Object)this) : TrackerPlayer.gson.toJson((Object)this);
    }
    
    static {
        gson = new Gson();
        PRETTY_PRINTING = new GsonBuilder().setPrettyPrinting().create();
    }
}
