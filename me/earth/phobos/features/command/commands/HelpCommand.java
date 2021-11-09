



package me.earth.phobos.features.command.commands;

import me.earth.phobos.features.command.*;
import me.earth.phobos.*;
import java.util.*;

public class HelpCommand extends Command
{
    public HelpCommand() {
        super("help");
    }
    
    public void execute(final String[] commands) {
        sendMessage("You can use following commands: ");
        for (final Command command : Phobos.commandManager.getCommands()) {
            sendMessage(Phobos.commandManager.getPrefix() + command.getName());
        }
    }
}
