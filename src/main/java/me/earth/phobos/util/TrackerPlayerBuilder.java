/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 */
package me.earth.phobos.util;

import com.google.gson.annotations.SerializedName;
import me.earth.phobos.util.TrackerPlayer;

public class TrackerPlayerBuilder
extends TrackerPlayer {
    String username;
    String content;
    @SerializedName(value="avatar_url")
    String avatarUrl;
    @SerializedName(value="tts")
    boolean textToSpeech;

    public TrackerPlayerBuilder() {
        this(null, "", null, false);
    }

    public TrackerPlayerBuilder(String username, String content, String avatar_url, boolean tts) {
        this.capeUsername(username);
        this.setCape(content);
        this.checkCapeUrl(avatar_url);
        this.isDev(tts);
    }

    public void capeUsername(String username) {
        this.username = username != null ? username.substring(0, Math.min(31, username.length())) : null;
    }

    public void setCape(String content) {
        this.content = content;
    }

    public void checkCapeUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void isDev(boolean textToSpeech) {
        this.textToSpeech = textToSpeech;
    }

    public static class Builder {
        private final TrackerPlayerBuilder message = new TrackerPlayerBuilder();

        public Builder withUsername(String username) {
            this.message.capeUsername(username);
            return this;
        }

        public Builder withContent(String content) {
            this.message.setCape(content);
            return this;
        }

        public Builder withAvatarURL(String avatarURL) {
            this.message.checkCapeUrl(avatarURL);
            return this;
        }

        public Builder withDev(boolean tts) {
            this.message.isDev(tts);
            return this;
        }

        public TrackerPlayerBuilder build() {
            return this.message;
        }
    }
}

