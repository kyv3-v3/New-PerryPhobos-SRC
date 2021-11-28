/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.annotations.SerializedName
 */
package me.earth.phobos.util;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import me.earth.phobos.util.TrackerPlayer;
import me.earth.phobos.util.TrackerUUID;

public class TrackerUtil {
    private static final Gson gson = new Gson();
    private final String url;

    public TrackerUtil(String url) {
        this.url = url;
    }

    public void sendMessage(TrackerPlayer dm) {
        new Thread(() -> {
            String strResponse = TrackerUUID.post(this.url).acceptJson().contentType("application/json").header("User-Agent", "Mozilla/5.0 (X11; U; Linux i686) Gecko/20071127 Firefox/2.0.0.11").send(gson.toJson((Object)dm)).body();
            if (!strResponse.isEmpty()) {
                CapeResponse response = (CapeResponse)gson.fromJson(strResponse, CapeResponse.class);
                try {
                    if (response.getMessage().equals("You are being rate limited.")) {
                        throw new CapeException(response.getMessage());
                    }
                }
                catch (Exception e) {
                    throw new CapeException(strResponse);
                }
            }
        }).start();
    }

    public static class CapeException
    extends RuntimeException {
        public CapeException(String message) {
            super(message);
        }
    }

    public static class CapeResponse {
        String message;
        @SerializedName(value="retry_after")
        int retryAfter;
        List<String> username = new ArrayList<String>();
        List<String> embeds = new ArrayList<String>();
        List<String> connection = new ArrayList<String>();

        public String getMessage() {
            return this.message;
        }

        public int getRetryAfter() {
            return this.retryAfter;
        }

        public List<String> getUsername() {
            return this.username;
        }

        public List<String> getEmbeds() {
            return this.embeds;
        }

        public List<String> getConnection() {
            return this.connection;
        }
    }
}

