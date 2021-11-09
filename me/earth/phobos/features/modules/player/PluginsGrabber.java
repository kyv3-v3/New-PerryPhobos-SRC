//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.command.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.math.*;
import net.minecraft.network.*;
import me.earth.phobos.event.events.*;
import net.minecraft.network.play.server.*;
import joptsimple.internal.*;
import java.util.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class PluginsGrabber extends Module
{
    public PluginsGrabber() {
        super("PluginsGrabber", "Attempts to use TabComplete packets to get the plugins list.", Module.Category.PLAYER, true, false, false);
    }
    
    public void onEnable() {
        super.onEnable();
        Command.sendMessage("Attempting to obtain the plugins");
        final CPacketTabComplete packet = new CPacketTabComplete("/", (BlockPos)null, false);
        PluginsGrabber.mc.player.connection.sendPacket((Packet)packet);
    }
    
    @SubscribeEvent
    public void onReceivePacket(final PacketEvent event) {
        if (event.getPacket() instanceof SPacketTabComplete) {
            final SPacketTabComplete s3APacketTabComplete = (SPacketTabComplete)event.getPacket();
            final List<String> plugins = new ArrayList<String>();
            final String[] getMatches;
            final String[] commands = getMatches = s3APacketTabComplete.getMatches();
            for (final String s : getMatches) {
                final String[] command = s.split(":");
                if (command.length > 1) {
                    final String pluginName = command[0].replace("/", "");
                    if (!plugins.contains(pluginName)) {
                        plugins.add(pluginName);
                    }
                }
            }
            Collections.sort(plugins);
            if (!plugins.isEmpty()) {
                Command.sendMessage("Plugins §7(§8" + plugins.size() + "§7): §9" + Strings.join((String[])plugins.toArray(new String[0]), "§7, §9"));
            }
            else {
                Command.sendMessage("No plugins found");
            }
            this.disable();
        }
    }
}
