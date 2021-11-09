//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.gui.chat.command;

import net.minecraft.server.*;
import net.minecraftforge.common.*;
import net.minecraft.command.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.client.*;
import me.earth.phobos.features.gui.chat.gui.*;
import net.minecraft.client.gui.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class CommandConfig extends CommandBase
{
    public String getName() {
        return "betterchat";
    }
    
    public String getUsage(final ICommandSender sender) {
        return "/betterchat";
    }
    
    public boolean checkPermission(final MinecraftServer server, final ICommandSender sender) {
        return true;
    }
    
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new GuiConfig());
    }
}
