//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.event.events.*;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.common.eventhandler.*;
import java.net.*;
import com.google.gson.*;
import java.io.*;
import me.earth.phobos.util.*;
import net.minecraft.network.*;

public class Translator extends Module
{
    private final Setting<Language> sourceLanguage;
    private final Setting<Language> targetLanguage;
    public Translate translate;
    
    public Translator() {
        super("Translator", "Translates text to a different language.", Category.MISC, true, false, false);
        this.sourceLanguage = (Setting<Language>)this.register(new Setting("SourceLanguage", (T)Language.English));
        this.targetLanguage = (Setting<Language>)this.register(new Setting("TargetLanguage", (T)Language.Spanish));
    }
    
    @SubscribeEvent
    public void onSendPacket(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            event.setCanceled(true);
            (this.translate = new Translate(((CPacketChatMessage)event.getPacket()).getMessage(), this.sourceLanguage.getValue(), this.targetLanguage.getValue())).start();
        }
    }
    
    private JsonObject request(final String URL2) throws IOException {
        final URL url = new URL(URL2);
        final URLConnection urlConn = url.openConnection();
        urlConn.addRequestProperty("User-Agent", "Mozilla");
        final InputStream inStream = urlConn.getInputStream();
        final JsonParser jp = new JsonParser();
        final JsonElement root = jp.parse((Reader)new InputStreamReader((InputStream)urlConn.getContent()));
        inStream.close();
        return root.getAsJsonObject();
    }
    
    public enum Language
    {
        Azerbaijan("az"), 
        Albanian("sq"), 
        Amharic("am"), 
        English("en"), 
        Arabic("ar"), 
        Armenian("hy"), 
        Afrikaans("af"), 
        Basque("eu"), 
        Bashkir("ba"), 
        Belarusian("be"), 
        Bengali("bn"), 
        Burmese("my"), 
        Bulgarian("bg"), 
        Bosnian("bs"), 
        Welsh("cy"), 
        Hungarian("hu"), 
        Vietnamese("vi"), 
        Haitian("ht"), 
        Galician("gl"), 
        Dutch("nl"), 
        HillMari("mrj"), 
        Greek("el"), 
        Georgian("ka"), 
        Gujarati("gu"), 
        Danish("da"), 
        Hebrew("he"), 
        Yiddish("yi"), 
        Indonesian("id"), 
        Irish("ga"), 
        Italian("it"), 
        Icelandic("is"), 
        Spanish("es"), 
        Kazakh("kk"), 
        Kannada("kn"), 
        Catalan("ca"), 
        Kyrgyz("ky"), 
        Chinese("zh"), 
        Korean("ko"), 
        Xhosa("xh"), 
        Khmer("km"), 
        Laotian("lo"), 
        Latin("la"), 
        Latvian("lv"), 
        Lithuanian("lt"), 
        Luxembourgish("lb"), 
        Malagasy("mg"), 
        Malay("ms"), 
        Malayalam("ml"), 
        Maltese("mt"), 
        Macedonian("mk"), 
        Maori("mi"), 
        Marathi("mr"), 
        Mari("mhr"), 
        Mongolian("mn"), 
        German("de"), 
        Nepali("ne"), 
        Norwegian("no"), 
        Russian("ru"), 
        Punjabi("pa"), 
        Papiamento("pap"), 
        Persian("fa"), 
        Polish("pl"), 
        Portuguese("pt"), 
        Romanian("ro"), 
        Cebuano("ceb"), 
        Serbian("sr"), 
        Sinhala("si"), 
        Slovakian("sk"), 
        Slovenian("sl"), 
        Swahili("sw"), 
        Sundanese("su"), 
        Tajik("tg"), 
        Thai("th"), 
        Tagalog("tl"), 
        Tamil("ta"), 
        Tatar("tt"), 
        Telugu("te"), 
        Turkish("tr"), 
        Udmurt("udm"), 
        Uzbek("uz"), 
        Ukrainian("uk"), 
        Urdu("ur"), 
        Finnish("fi"), 
        French("fr"), 
        Hindi("hi"), 
        Croatian("hr"), 
        Czech("cs"), 
        Swedish("sv"), 
        Scottish("gd"), 
        Estonian("et"), 
        Esperanto("eo"), 
        Javanese("jv"), 
        Japanese("ja");
        
        private final String code;
        
        private Language(final String code) {
            this.code = code;
        }
        
        public static Language getByCode(final String code) {
            for (final Language language : values()) {
                if (language.code.equals(code)) {
                    return language;
                }
            }
            return null;
        }
        
        public String getCode() {
            return this.code;
        }
    }
    
    public static class Translate extends Thread
    {
        public String message;
        public Language sourceLang;
        public Language lang;
        public String finalMessage;
        Thread thread;
        
        public Translate(final String message, final Language sourceLang, final Language lang) {
            super("Translate");
            this.message = message;
            this.sourceLang = sourceLang;
            this.lang = lang;
        }
        
        @Override
        public void run() {
            try {
                this.finalMessage = this.request("https://translate.yandex.net/api/v1.5/tr.json/detect?key=trnsl.1.1.20200318T071948Z.11cbd84c59782bd3.3361732cd2545a6ceda7899bdeaf6d90fe83dc8f&text=" + this.message.replace(" ", "%20") + "&lang=" + this.sourceLang.getCode() + "-" + this.lang.getCode()).get("text").getAsString();
                Util.mc.player.connection.sendPacket((Packet)new CPacketChatMessage(this.finalMessage));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public void start() {
            if (this.thread == null) {
                (this.thread = new Thread(this, "Translate")).start();
            }
        }
        
        private JsonObject request(final String URL2) throws IOException {
            final URL url = new URL(URL2);
            final URLConnection urlConn = url.openConnection();
            urlConn.addRequestProperty("User-Agent", "Mozilla");
            final InputStream inStream = urlConn.getInputStream();
            final JsonParser jp = new JsonParser();
            final JsonElement root = jp.parse((Reader)new InputStreamReader((InputStream)urlConn.getContent()));
            inStream.close();
            return root.getAsJsonObject();
        }
    }
}
