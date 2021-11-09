//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.command.commands;

import me.earth.phobos.features.command.*;
import java.io.*;
import java.util.stream.*;
import me.earth.phobos.*;
import java.util.*;

public class ConfigCommand extends Command
{
    public ConfigCommand() {
        super("config", new String[] { "<save/load>" });
    }
    
    public void execute(final String[] commands) {
        if (commands.length == 1) {
            sendMessage("You`ll find the config files in your gameProfile directory under phobos/config");
            return;
        }
        if (commands.length == 2) {
            if ("list".equals(commands[0])) {
                String configs = "Configs: ";
                final File file = new File("phobos/");
                final List<File> directories = Arrays.stream((Object[])Objects.requireNonNull((T[])file.listFiles())).filter(File::isDirectory).filter(f -> !f.getName().equals("util")).collect((Collector<? super Object, ?, List<File>>)Collectors.toList());
                final StringBuilder builder = new StringBuilder(configs);
                for (final File file2 : directories) {
                    builder.append(file2.getName()).append(", ");
                }
                configs = builder.toString();
                sendMessage("§a" + configs);
            }
            else {
                sendMessage("§cNot a valid command... Possible usage: <list>");
            }
        }
        if (commands.length >= 3) {
            final String s = commands[0];
            switch (s) {
                case "save": {
                    Phobos.configManager.saveConfig(commands[1]);
                    sendMessage("§aConfig has been saved.");
                    break;
                }
                case "load": {
                    Phobos.moduleManager.onUnload();
                    Phobos.configManager.loadConfig(commands[1]);
                    Phobos.moduleManager.onLoad();
                    sendMessage("§aConfig has been loaded.");
                    break;
                }
                default: {
                    sendMessage("§cNot a valid command... Possible usage: <save/load>");
                    break;
                }
            }
        }
    }
}
