//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.gui.chat;

import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraftforge.common.config.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.common.*;
import me.earth.phobos.features.gui.chat.handlers.*;
import net.minecraftforge.client.*;
import me.earth.phobos.features.gui.chat.command.*;
import net.minecraft.command.*;

@Mod(modid = "betterchat", name = "Better Chat", version = "1.5")
@SideOnly(Side.CLIENT)
public class BetterChat
{
    public static final String MODID = "betterchat";
    public static final String NAME = "Better Chat";
    public static final String VERSION = "1.5";
    private static ChatSettings settings;
    
    public static ChatSettings getSettings() {
        return BetterChat.settings;
    }
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        (BetterChat.settings = new ChatSettings(new Configuration(event.getSuggestedConfigurationFile()))).loadConfig();
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register((Object)new InjectHandler());
        ClientCommandHandler.instance.registerCommand((ICommand)new CommandConfig());
    }
}
