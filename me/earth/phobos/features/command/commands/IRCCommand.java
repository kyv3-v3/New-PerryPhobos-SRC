//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.command.commands;

import me.earth.phobos.features.command.*;
import me.earth.phobos.features.modules.client.*;
import java.io.*;

public class IRCCommand extends Command
{
    public IRCCommand() {
        super("irc");
    }
    
    public void execute(final String[] commands) {
        if (commands.length == 1) {
            sendMessage(PhobosChat.INSTANCE.status ? "§aIRC is connected." : "§cIRC is not connected.");
        }
        else if (commands.length == 2) {
            if (commands[0].equalsIgnoreCase("connect")) {
                sendMessage("§aConnecting to the PhobosClient PhobosChat...");
                try {
                    PhobosChat.INSTANCE.connect();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (commands[0].equalsIgnoreCase("disconnect")) {
                sendMessage("§aDisconnecting from the PhobosClient PhobosChat...");
                try {
                    PhobosChat.INSTANCE.disconnect();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (commands[0].equalsIgnoreCase("friendall")) {
                sendMessage("§aFriending all...");
                try {
                    PhobosChat.INSTANCE.friendAll();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (commands[0].equalsIgnoreCase("list")) {
                sendMessage("§aListing PhobosClient Users...");
                try {
                    PhobosChat.INSTANCE.list();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (commands.length >= 3) {
            if (commands[0].equalsIgnoreCase("say")) {
                sendMessage("§aSending message to the PhobosClient chat server...");
                final StringBuilder builder = new StringBuilder();
                for (int i = 1; i < commands.length - 1; ++i) {
                    builder.append(commands[i]).append(" ");
                }
                final String message = builder.toString();
                try {
                    PhobosChat.say(message);
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            else if (commands[0].equalsIgnoreCase("cockt")) {
                sendMessage("§acockkk");
                try {
                    PhobosChat.cockt(Integer.parseInt(commands[1]));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
