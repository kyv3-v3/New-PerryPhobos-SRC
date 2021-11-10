



package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.util.*;
import net.minecraft.entity.player.*;
import me.earth.phobos.features.command.*;
import me.earth.phobos.event.events.*;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.network.play.server.*;
import java.text.*;
import java.util.*;
import me.earth.phobos.features.modules.client.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;

public class ChatModifier extends Module
{
    private static ChatModifier INSTANCE;
    private final TimerUtil timer;
    public Setting<Suffix> suffix;
    public Setting<String> customSuffix;
    public Setting<Boolean> clean;
    public Setting<Boolean> infinite;
    public Setting<Boolean> autoQMain;
    public Setting<Boolean> qNotification;
    public Setting<Integer> qDelay;
    public Setting<TextUtil.Color> timeStamps;
    public Setting<Boolean> rainbowTimeStamps;
    public Setting<TextUtil.Color> bracket;
    public Setting<Boolean> space;
    public Setting<Boolean> all;
    public Setting<Boolean> shrug;
    public Setting<Boolean> disability;
    
    public ChatModifier() {
        super("Chat",  "Modifies your chat.",  Category.MISC,  true,  false,  false);
        this.timer = new TimerUtil();
        this.suffix = (Setting<Suffix>)this.register(new Setting("Suffix", Suffix.NONE,  "Your Suffix."));
        this.customSuffix = (Setting<String>)this.register(new Setting("", " | Perrys Phobos",  v -> this.suffix.getValue() == Suffix.CUSTOM));
        this.clean = (Setting<Boolean>)this.register(new Setting("CleanChat", false,  "Cleans your chat"));
        this.infinite = (Setting<Boolean>)this.register(new Setting("Infinite", false,  "Makes your chat infinite."));
        this.autoQMain = (Setting<Boolean>)this.register(new Setting("AutoQMain", false,  "Spams AutoQMain"));
        this.qNotification = (Setting<Boolean>)this.register(new Setting("QNotification", false,  v -> this.autoQMain.getValue()));
        this.qDelay = (Setting<Integer>)this.register(new Setting("QDelay", 9, 1, 90,  v -> this.autoQMain.getValue()));
        this.timeStamps = (Setting<TextUtil.Color>)this.register(new Setting("Time", TextUtil.Color.NONE));
        this.rainbowTimeStamps = (Setting<Boolean>)this.register(new Setting("RainbowTimeStamps", false,  v -> this.timeStamps.getValue() != TextUtil.Color.NONE));
        this.bracket = (Setting<TextUtil.Color>)this.register(new Setting("Bracket", TextUtil.Color.WHITE,  v -> this.timeStamps.getValue() != TextUtil.Color.NONE));
        this.space = (Setting<Boolean>)this.register(new Setting("Space", true,  v -> this.timeStamps.getValue() != TextUtil.Color.NONE));
        this.all = (Setting<Boolean>)this.register(new Setting("All", false,  v -> this.timeStamps.getValue() != TextUtil.Color.NONE));
        this.shrug = (Setting<Boolean>)this.register(new Setting("Shrug", false));
        this.disability = (Setting<Boolean>)this.register(new Setting("Disability", false));
        this.setInstance();
    }
    
    public static ChatModifier getInstance() {
        if (ChatModifier.INSTANCE == null) {
            ChatModifier.INSTANCE = new ChatModifier();
        }
        return ChatModifier.INSTANCE;
    }
    
    private void setInstance() {
        ChatModifier.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        if (this.shrug.getValue()) {
            ChatModifier.mc.player.sendChatMessage(TextUtil.shrug);
            this.shrug.setValue(false);
        }
        if (this.disability.getValue()) {
            ChatModifier.mc.player.sendChatMessage(TextUtil.disability);
            this.disability.setValue(false);
        }
        if (this.autoQMain.getValue()) {
            if (!this.shouldSendMessage((EntityPlayer)ChatModifier.mc.player)) {
                return;
            }
            if (this.qNotification.getValue()) {
                Command.sendMessage("<AutoQueueMain> Sending message: /queue main");
            }
            ChatModifier.mc.player.sendChatMessage("/queue main");
            this.timer.reset();
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketChatMessage) {
            final CPacketChatMessage packet = (CPacketChatMessage)event.getPacket();
            String s = packet.getMessage();
            if (s.startsWith("/")) {
                return;
            }
            switch (this.suffix.getValue()) {
                case EARTH: {
                    s += " \u23d0 3\u1d00\u0280\u1d1b\u029c\u029c4\u1d04\u1d0b";
                    break;
                }
                case PERRYPHOBOS: {
                    s += " \u23d0 \u1d29\u1d07\u0280\u0280\u1203\ua731 \u1d29\u029c\u1d0f\u0299\u1d0f\ua731";
                    break;
                }
                case PHOBOS: {
                    s += " \u23d0 \u1d18\u029c\u1d0f\u0299\u1d0f\ua731";
                    break;
                }
                case INSANE: {
                    s += " | \u028c\u0433\u1d07\u0455+ « \u1d0b\u1d00\u1d0d\u026a \u0299\u029f\u1d1c\u1d07 \u1d0f\u0274 \u1d1b\u1d0f\u1d18 » » \u02e2\u207f\u1d52\u02b7\u23d0 \u041d\u03b5\u13ae\u043d\u15e9\u03b5\u0455\u01ad\u03c5\u0455 » \u0299\u1d00\u1d04\u1d0b\u1d05\u1d0f\u1d0f\u0280\u1d07\u1d05 | \u1d0d\u1d07\u1d0f\u1d21 » \u1d1c\u0274\u026a\u1d04\u1d0f\u0280\u0274\u0262\u1d0f\u1d05.\u0262\u0262  \ua731\u1d07\u1d18\u1d18\u1d1c\u1d0b\u1d1c | \u029c\u1d1c\u1d22\u1d1c\u0274\u026a\u0262\u0280\u1d07\u1d07\u0274.\u0262\u0262tm » \u0299\u1d00\u1d04\u1d0b\u1d04\u029f\u026a\u1d07\u0274\u1d1btm » \u0274\u1d0f\u1d1c \u029f\u1d07\u1d00\u1d0b \u23d0 \u0493\u1d0f\u0280\u0262\u1d07\u0280\u1d00\u1d1b \u2661 | \u04e8B\u039bM\u039b \u1103\u1102I\u03a3\u041f\u01ac - \u1d07\u029f\u1d07\u1d0d\u1d07\u0274\u1d1b\u1d00\u0280\ua731.\u1d04\u1d0f\u1d0d \u300b\u1d0f\ua731\u026a\u0280\u026a\ua731 | W\u00d4\u00d4K\u00cf\u00ca \u00c7L\u00ee\u00eb\u00d1Ttm {\u0280\u1d00\u026a\u1d0f\u0274\u1d0b\u1d07\u1d0b} \u30c3 \uff32\uff10\uff10\uff34\uff5c \u1d20\u1d0f\u026a\u1d05 \u1d04\u029f\u026a\u1d07\u0274\u1d1b \uff5c \u1d0b\u1d00\u1d0d\u026a \uff5c \u02b3\u1d1c\u02e2\u02b0\u1d07\u02b3\u02b0\u1d00\u1d9c\u1d4f \uff5c \u24cc\u24cc\u24ba \uff5c \uff49\uff4d\uff50\uff41\uff43\uff54\uff5c \ua730\u1d1c\u1d1b\u1d1c\u0280\u1d07tm \u24d6\u24de\u24db\u24d3 \uff5c \u5342\u4e02\u3129\u51e0\u5342\u1d04\u029f\u026a\u1d07\u0274\u1d1b \uff5c\ua118\ua182\ua493\ua04d\ua35f\ua45b\ua2eb\ua1d3 \uff5c \u02b3\u1d00\uff54\u1d04\u029f\u026a\u1d07\u0274\u1d1b \uff5c \u1d00\u1d18\u1d0f\u029f\u029f\u028f\u1d0f\u0274 \uff5c \u1d0b\u1d00\u1d0d\u026a \u0299\u029f\u1d1c\u1d07 \u2758 \u1d3e\u1d3c\u1d3e\u1d2e\u1d3c\u1d2e \u1d9c\u1d38\u1d35\u1d31\u1d3a\u1d40 \u23d0 \u0262\u1d00\u028f \u23d0  c l i e n t |  B a L l C l i E n T» \u028c\u0433\u1d07\u0455+ « \u1d0b\u1d00\u1d0d\u026a \u0299\u029f\u1d1c\u1d07 \u1d0f\u0274 \u1d1b\u1d0f\u1d18 » » \u02e2\u207f\u1d52\u02b7\u23d0 \u041d\u03b5\u13ae\u043d\u15e9\u03b5\u0455\u01ad\u03c5\u0455 » \u0299\u1d00\u1d04\u1d0b\u1d05\u1d0f\u1d0f\u0280\u1d07\u1d05 | \u1d0d\u1d07\u1d0f\u1d21 » \u1d1c\u0274\u026a\u1d04\u1d0f\u0280\u0274\u0262\u1d0f\u1d05.\u0262\u0262  \ua731\u1d07\u1d18\u1d18\u1d1c\u1d0b\u1d1c | \u029c\u1d1c\u1d22\u1d1c\u0274\u026a\u0262\u0280\u1d07\u1d07\u0274.\u0262\u0262tm » \u0299\u1d00\u1d04\u1d0b\u1d04\u029f\u026a\u1d07\u0274\u1d1btm » \u0274\u1d0f\u1d1c \u029f\u1d07\u1d00\u1d0b  \u23d0 \u0493\u1d0f\u0280\u0262\u1d07\u0280\u1d00\u1d1b \u2661 | \u04e8B\u039bM\u039b \u1103\u1102I\u03a3\u041f\u01ac - \u1d07\u029f\u1d07\u1d0d\u1d07\u0274\u1d1b\u1d00\u0280\ua731.\u1d04\u1d0f\u1d0d \u300b\u1d0f\ua731\u026a\u0280\u026a\ua731 | W\u00d4\u00d4K\u00cf\u00ca \u00c7L\u00ee\u00eb\u00d1Ttm {\u0280\u1d00\u026a\u1d0f\u0274\u1d0b\u1d07\u1d0b} \u30c3 \uff32\uff10\uff10\uff34\uff5c \u1d20\u1d0f\u026a\u1d05 \u1d04\u029f\u026a\u1d07\u0274\u1d1b \uff5c \u1d0b\u1d00\u1d0d\u026a \uff5c \u02b3\u1d1c\u02e2\u02b0\u1d07\u02b3\u02b0\u1d00\u1d9c\u1d4f \uff5c \u24cc\u24cc\u24ba \uff5c \uff49\uff4d\uff50\uff41\uff43\uff54\uff5c \ua730\u1d1c\u1d1b\u1d1c\u0280\u1d07tm \u24d6\u24de\u24db\u24d3 \uff5c \u5342\u4e02\u3129\u51e0\u5342\u1d04\u029f\u026a\u1d07\u0274\u1d1b \uff5c\ua118\ua182\ua493\ua04d\ua35f\ua45b\ua2eb\ua1d3 \uff5c \u02b3\u1d00\uff54\u1d04\u029f\u026a\u1d07\u0274\u1d1b \uff5c \u1d00\u1d18\u1d0f\u029f\u029f\u028f\u1d0f\u0274 \uff5c \u1d0b\u1d00\u1d0d\u026a \u0299\u029f\u1d1c\u1d07 \u2758 \u1d3e\u1d3c\u1d3e\u1d2e\u1d3c\u1d2e \u1d9c\u1d38\u1d35\u1d31\u1d3a\u1d40 \u23d0 \u0262\u1d00\u028f \u23d0  c l i e n t |  B a L l C l i E n T» \u028c\u0433\u1d07\u0455+ « \u1d0b\u1d00\u1d0d\u026a \u0299\u029f\u1d1c\u1d07 \u1d0f\u0274 \u1d1b\u1d0f\u1d18 » » \u02e2\u207f\u1d52\u02b7\u23d0 \u041d\u03b5\u13ae\u043d\u15e9\u03b5\u0455\u01ad\u03c5\u0455 » \u0299\u1d00\u1d04\u1d0b\u1d05\u1d0f\u1d0f\u0280\u1d07\u1d05 | \u1d0d\u1d07\u1d0f\u1d21 » \u1d1c\u0274\u026a\u1d04\u1d0f\u0280\u0274\u0262\u1d0f\u1d05.\u0262\u0262  \ua731\u1d07\u1d18\u1d18\u1d1c\u1d0b\u1d1c | \u029c\u1d1c\u1d22\u1d1c\u0274\u026a\u0262\u0280\u1d07\u1d07\u0274.\u0262\u0262tm » \u0299\u1d00\u1d04\u1d0b\u1d04\u029f\u026a\u1d07\u0274\u1d1btm » \u0274\u1d0f\u1d1c \u029f\u1d07\u1d00\u1d0b  \u23d0 \u0493\u1d0f\u0280\u0262\u1d07\u0280\u1d00\u1d1b \u2661 | \u04e8B\u039bM\u039b \u1103\u1102I\u03a3\u041f\u01ac - \u1d07\u029f\u1d07\u1d0d\u1d07\u0274\u1d1b\u1d00\u0280\ua731.\u1d04\u1d0f\u1d0d \u300b\u1d0f\ua731\u026a\u0280\u026a\ua731 | W\u00d4\u00d4K\u00cf\u00ca \u00c7L\u00ee\u00eb\u00d1Ttm {\u0280\u1d00\u026a\u1d0f\u0274\u1d0b\u1d07\u1d0b} \u30c3 \uff32\uff10\uff10\uff34\uff5c \u1d20\u1d0f\u026a\u1d05 \u1d04\u029f\u026a\u1d07\u0274\u1d1b \uff5c \u1d0b\u1d00\u1d0d\u026a \uff5c \u02b3\u1d1c\u02e2\u02b0\u1d07\u02b3\u02b0\u1d00\u1d9c\u1d4f \uff5c \u24cc\u24cc\u24ba \uff5c \uff49\uff4d\uff50\uff41\uff43\uff54\uff5c \ua730\u1d1c\u1d1b\u1d1c\u0280\u1d07tm \u24d6\u24de\u24db\u24d3 \uff5c \u5342\u4e02\u3129\u51e0\u5342\u1d04\u029f\u026a\u1d07\u0274\u1d1b \uff5c\ua118\ua182\ua493\ua04d\ua35f\ua45b\ua2eb\ua1d3 \uff5c \u02b3\u1d00\uff54\u1d04\u029f\u026a\u1d07\u0274\u1d1b \uff5c \u1d00\u1d18\u1d0f\u029f\u029f\u028f\u1d0f\u0274 \uff5c \u1d0b\u1d00\u1d0d\u026a \u0299\u029f\u1d1c\u1d07 \u2758 \u1d3e\u1d3c\u1d3e\u1d2e\u1d3c\u1d2e \u1d9c\u1d38\u1d35\u1d31\u1d3a\u1d40 \u23d0 \u0262\u1d00\u028f \u23d0  c l i e n t |  B a L l C l i E n T» \u028c\u0433\u1d07\u0455+ « \u1d0b\u1d00\u1d0d\u026a \u0299\u029f\u1d1c\u1d07 \u1d0f\u0274 \u1d1b\u1d0f\u1d18 » » \u02e2\u207f\u1d52\u02b7\u23d0 \u041d\u03b5\u13ae\u043d\u15e9\u03b5\u0455\u01ad\u03c5\u0455 » \u0299\u1d00\u1d04\u1d0b\u1d05\u1d0f\u1d0f\u0280\u1d07\u1d05 | \u1d0d\u1d07\u1d0f\u1d21 » \u1d1c\u0274\u026a\u1d04\u1d0f\u0280\u0274\u0262\u1d0f\u1d05.\u0262\u0262  \ua731\u1d07\u1d18\u1d18\u1d1c\u1d0b\u1d1c | \u029c\u1d1c\u1d22\u1d1c\u0274\u026a\u0262\u0280\u1d07\u1d07\u0274.\u0262\u0262tm » \u0299\u1d00\u1d04\u1d0b\u1d04\u029f\u026a\u1d07\u0274\u1d1btm » \u0274\u1d0f\u1d1c \u029f\u1d07\u1d00\u1d0b  \u23d0 \u0493\u1d0f\u0280\u0262\u1d07\u0280\u1d00\u1d1b \u2661 | \u04e8B\u039bM\u039b \u1103\u1102I\u03a3\u041f\u01ac - \u1d07\u029f\u1d07\u1d0d\u1d07\u0274\u1d1b\u1d00\u0280\ua731.\u1d04\u1d0f\u1d0d \u300b\u1d0f\ua731\u026a\u0280\u026a\ua731 | W\u00d4\u00d4K\u00cf\u00ca \u00c7L\u00ee\u00eb\u00d1Ttm {\u0280\u1d00\u026a\u1d0f\u0274\u1d0b\u1d07\u1d0b} \u30c3 \uff32\uff10\uff10\uff34\uff5c \u1d20\u1d0f\u026a\u1d05 \u1d04\u029f\u026a\u1d07\u0274\u1d1b \uff5c \u1d0b\u1d00\u1d0d\u026a \uff5c \u02b3\u1d1c\u02e2\u02b0\u1d07\u02b3\u02b0\u1d00\u1d9c\u1d4f \uff5c \u24cc\u24cc\u24ba \uff5c \uff49\uff4d\uff50\uff41\uff43\uff54\uff5c \ua730\u1d1c\u1d1b\u1d1c\u0280\u1d07tm \u24d6\u24de\u24db\u24d3 \uff5c \u5342\u4e02\u3129\u51e0\u5342\u1d04\u029f\u026a\u1d07\u0274\u1d1b \uff5c\ua118\ua182\ua493\ua04d\ua35f\ua45b\ua2eb\ua1d3 \uff5c \u02b3\u1d00\uff54\u1d04\u029f\u026a\u1d07\u0274\u1d1b \uff5c \u1d00\u1d18\u1d0f\u029f\u029f\u028f\u1d0f\u0274 \uff5c \u1d0b\u1d00\u1d0d\u026a \u0299\u029f\u1d1c\u1d07 \u2758 \u1d3e\u1d3c\u1d3e\u1d2e\u1d3c\u1d2e \u1d9c\u1d38\u1d35\u1d31\u1d3a\u1d40 \u23d0 \u0262\u1d00\u028f \u23d0  c l i e n t |  B a L l C l i E n T | Phobos1.5.4.eu | popbobhack | .grabcoords | faxhax";
                    break;
                }
                case CUSTOM: {
                    s += this.customSuffix.getValue();
                    break;
                }
            }
            if (s.length() >= 256) {
                s = s.substring(0,  256);
            }
            packet.message = s;
        }
    }
    
    @SubscribeEvent
    public void onChatPacketReceive(final PacketEvent.Receive event) {
        if (event.getStage() == 0) {
            event.getPacket();
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getStage() == 0 && this.timeStamps.getValue() != TextUtil.Color.NONE && event.getPacket() instanceof SPacketChat) {
            if (!((SPacketChat)event.getPacket()).isSystem()) {
                return;
            }
            final String originalMessage = ((SPacketChat)event.getPacket()).chatComponent.getFormattedText();
            final String message = this.getTimeString(originalMessage) + originalMessage;
            ((SPacketChat)event.getPacket()).chatComponent = (ITextComponent)new TextComponentString(message);
        }
    }
    
    public String getTimeString(final String message) {
        final String date = new SimpleDateFormat("k:mm").format(new Date());
        if (this.rainbowTimeStamps.getValue()) {
            final String timeString = "<" + date + ">" + (this.space.getValue() ? " " : "");
            final StringBuilder builder = new StringBuilder(timeString);
            builder.insert(0,  "§+");
            if (!message.contains(Management.getInstance().getRainbowCommandMessage())) {
                builder.append("§r");
            }
            return builder.toString();
        }
        return ((this.bracket.getValue() == TextUtil.Color.NONE) ? "" : TextUtil.coloredString("<",  this.bracket.getValue())) + TextUtil.coloredString(date,  this.timeStamps.getValue()) + ((this.bracket.getValue() == TextUtil.Color.NONE) ? "" : TextUtil.coloredString(">",  this.bracket.getValue())) + (this.space.getValue() ? " " : "") + "§r";
    }
    
    private boolean shouldSendMessage(final EntityPlayer player) {
        return player.dimension == 1 && this.timer.passedS(this.qDelay.getValue()) && player.getPosition().equals((Object)new Vec3i(0,  240,  0));
    }
    
    static {
        ChatModifier.INSTANCE = new ChatModifier();
    }
    
    public enum Suffix
    {
        NONE,  
        PHOBOS,  
        EARTH,  
        PERRYPHOBOS,  
        CUSTOM,  
        INSANE;
    }
}
