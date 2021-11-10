



package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.client.network.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import java.util.*;
import me.earth.phobos.util.*;

public class Spammer extends Module
{
    private static final String fileName = "phobos/util/Spammer.txt";
    private static final String defaultMessage = "gg";
    private static final List<String> spamMessages;
    private static final Random rnd;
    private final TimerUtil timer;
    private final List<String> sendPlayers;
    public Setting<Mode> mode;
    public Setting<PwordMode> type;
    public Setting<String> msgTarget;
    public Setting<DelayType> delayType;
    public Setting<Integer> delay;
    public Setting<Integer> delayDS;
    public Setting<Integer> delayMS;
    public Setting<Boolean> greentext;
    public Setting<Boolean> random;
    public Setting<Boolean> loadFile;
    
    public Spammer() {
        super("Spammer",  "Spams stuff.",  Category.MISC,  true,  false,  false);
        this.timer = new TimerUtil();
        this.sendPlayers = new ArrayList<String>();
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", Mode.PWORD));
        this.type = (Setting<PwordMode>)this.register(new Setting("Pword", PwordMode.CHAT,  v -> this.mode.getValue() == Mode.PWORD));
        this.msgTarget = (Setting<String>)this.register(new Setting("MsgTarget", "Target...",  v -> this.mode.getValue() == Mode.PWORD && this.type.getValue() == PwordMode.MSG));
        this.delayType = (Setting<DelayType>)this.register(new Setting("DelayType", DelayType.S));
        this.delay = (Setting<Integer>)this.register(new Setting("DelayS", 10, 1, 1000,  v -> this.delayType.getValue() == DelayType.S));
        this.delayDS = (Setting<Integer>)this.register(new Setting("DelayDS", 10, 1, 500,  v -> this.delayType.getValue() == DelayType.DS));
        this.delayMS = (Setting<Integer>)this.register(new Setting("DelayDS", 10, 1, 1000,  v -> this.delayType.getValue() == DelayType.MS));
        this.greentext = (Setting<Boolean>)this.register(new Setting("Greentext", false,  v -> this.mode.getValue() == Mode.FILE));
        this.random = (Setting<Boolean>)this.register(new Setting("Random", false,  v -> this.mode.getValue() == Mode.FILE));
        this.loadFile = (Setting<Boolean>)this.register(new Setting("LoadFile", false,  v -> this.mode.getValue() == Mode.FILE));
    }
    
    @Override
    public void onLoad() {
        this.readSpamFile();
        this.disable();
    }
    
    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            this.disable();
            return;
        }
        this.readSpamFile();
    }
    
    @Override
    public void onLogin() {
        this.disable();
    }
    
    @Override
    public void onLogout() {
        this.disable();
    }
    
    @Override
    public void onDisable() {
        Spammer.spamMessages.clear();
        this.timer.reset();
    }
    
    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            this.disable();
            return;
        }
        if (this.loadFile.getValue()) {
            this.readSpamFile();
            this.loadFile.setValue(false);
        }
        switch (this.delayType.getValue()) {
            case MS: {
                if (this.timer.passedMs(this.delayMS.getValue())) {
                    break;
                }
                return;
            }
            case S: {
                if (this.timer.passedS(this.delay.getValue())) {
                    break;
                }
                return;
            }
            case DS: {
                if (this.timer.passedDs(this.delayDS.getValue())) {
                    break;
                }
                return;
            }
        }
        if (this.mode.getValue() == Mode.PWORD) {
            String msg = "  \u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\n \u2588\u2588\u2588\u2592\u2588\u2592\u2588\u2592\u2588\u2588\u2588\u2592\u2588\u2588\u2588\u2592\u2588\u2588\u2588\u2592\u2588\u2588\u2588\n \u2588\u2592\u2588\u2592\u2588\u2592\u2588\u2592\u2588\u2592\u2588\u2592\u2588\u2592\u2588\u2592\u2588\u2592\u2588\u2592\u2588\u2592\u2592\n \u2588\u2588\u2588\u2592\u2588\u2588\u2588\u2592\u2588\u2592\u2588\u2592\u2588\u2588\u2588\u2592\u2588\u2592\u2588\u2592\u2588\u2588\u2588\n \u2588\u2592\u2592\u2592\u2588\u2592\u2588\u2592\u2588\u2592\u2588\u2592\u2588\u2592\u2588\u2592\u2588\u2592\u2588\u2592\u2592\u2592\u2588\n \u2588\u2592\u2592\u2592\u2588\u2592\u2588\u2592\u2588\u2588\u2588\u2592\u2588\u2588\u2588\u2592\u2588\u2588\u2588\u2592\u2588\u2588\u2588\n \u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592";
            switch (this.type.getValue()) {
                case MSG: {
                    msg = "/msg " + this.msgTarget.getValue() + msg;
                    break;
                }
                case EVERYONE: {
                    String target = null;
                    if (Spammer.mc.getConnection() == null) {
                        return;
                    }
                    Spammer.mc.getConnection().getPlayerInfoMap();
                    for (final NetworkPlayerInfo info : Spammer.mc.getConnection().getPlayerInfoMap()) {
                        if (info != null) {
                            if (info.getDisplayName() == null) {
                                continue;
                            }
                            try {
                                final String str = info.getDisplayName().getFormattedText();
                                final String name = StringUtils.stripControlCodes(str);
                                if (name.equals(Spammer.mc.player.getName()) || this.sendPlayers.contains(name)) {
                                    continue;
                                }
                                target = name;
                                this.sendPlayers.add(name);
                            }
                            catch (Exception ex) {
                                continue;
                            }
                            break;
                        }
                    }
                    if (target == null) {
                        this.sendPlayers.clear();
                        return;
                    }
                    msg = "/msg " + target + msg;
                    break;
                }
            }
            Spammer.mc.player.sendChatMessage(msg);
        }
        else if (Spammer.spamMessages.size() > 0) {
            String messageOut;
            if (this.random.getValue()) {
                final int index = Spammer.rnd.nextInt(Spammer.spamMessages.size());
                messageOut = Spammer.spamMessages.get(index);
                Spammer.spamMessages.remove(index);
            }
            else {
                messageOut = Spammer.spamMessages.get(0);
                Spammer.spamMessages.remove(0);
            }
            Spammer.spamMessages.add(messageOut);
            if (this.greentext.getValue()) {
                messageOut = "> " + messageOut;
            }
            Spammer.mc.player.connection.sendPacket((Packet)new CPacketChatMessage(messageOut.replaceAll("§",  "")));
        }
        this.timer.reset();
    }
    
    private void readSpamFile() {
        final List<String> fileInput = FileUtil.readTextFileAllLines("phobos/util/Spammer.txt");
        final Iterator<String> i = fileInput.iterator();
        Spammer.spamMessages.clear();
        while (i.hasNext()) {
            final String s = i.next();
            if (s.replaceAll("\\s",  "").isEmpty()) {
                continue;
            }
            Spammer.spamMessages.add(s);
        }
        if (Spammer.spamMessages.size() == 0) {
            Spammer.spamMessages.add("gg");
        }
    }
    
    static {
        spamMessages = new ArrayList<String>();
        rnd = new Random();
    }
    
    public enum DelayType
    {
        MS,  
        DS,  
        S;
    }
    
    public enum PwordMode
    {
        MSG,  
        EVERYONE,  
        CHAT;
    }
    
    public enum Mode
    {
        FILE,  
        PWORD;
    }
}
