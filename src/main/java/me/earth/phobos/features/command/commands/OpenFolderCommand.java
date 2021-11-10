



package me.earth.phobos.features.command.commands;

import me.earth.phobos.features.command.*;
import java.awt.*;
import java.io.*;

public class OpenFolderCommand extends Command
{
    public OpenFolderCommand() {
        super("openfolder",  new String[0]);
    }
    
    public void execute(final String[] commands) {
        try {
            Desktop.getDesktop().open(new File("phobos/"));
            Command.sendMessage("Opened config folder!",  false);
        }
        catch (IOException e) {
            Command.sendMessage("Could not open config folder!",  false);
            e.printStackTrace();
        }
    }
}
