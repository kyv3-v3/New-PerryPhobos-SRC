//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.util;

import com.google.gson.annotations.*;

public class TrackerPlayerBuilder extends TrackerPlayer
{
    String username;
    String content;
    @SerializedName("avatar_url")
    String avatarUrl;
    @SerializedName("tts")
    boolean textToSpeech;
    
    public TrackerPlayerBuilder() {
        this(null, "", null, false);
    }
    
    public TrackerPlayerBuilder(final String username, final String content, final String avatar_url, final boolean tts) {
        this.capeUsername(username);
        this.setCape(content);
        this.checkCapeUrl(avatar_url);
        this.isDev(tts);
    }
    
    public void capeUsername(final String username) {
        if (username != null) {
            this.username = username.substring(0, Math.min(31, username.length()));
        }
        else {
            this.username = null;
        }
    }
    
    public void setCape(final String content) {
        this.content = content;
    }
    
    public void checkCapeUrl(final String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
    public void isDev(final boolean textToSpeech) {
        this.textToSpeech = textToSpeech;
    }
    
    public static class Builder
    {
        private final TrackerPlayerBuilder message;
        
        public Builder() {
            this.message = new TrackerPlayerBuilder();
        }
        
        public Builder withUsername(final String username) {
            this.message.capeUsername(username);
            return this;
        }
        
        public Builder withContent(final String content) {
            this.message.setCape(content);
            return this;
        }
        
        public Builder withAvatarURL(final String avatarURL) {
            this.message.checkCapeUrl(avatarURL);
            return this;
        }
        
        public Builder withDev(final boolean tts) {
            this.message.isDev(tts);
            return this;
        }
        
        public TrackerPlayerBuilder build() {
            return this.message;
        }
    }
}
