



package me.earth.phobos.features.command.commands;

import me.earth.phobos.features.command.*;

public class ClearRamCommand extends Command
{
    public ClearRamCommand() {
        super("clearram");
    }
    
    public void execute(final String[] commands) {
        System.gc();
        Command.sendMessage("Finished clearing the ram.",  false);
    }
}
