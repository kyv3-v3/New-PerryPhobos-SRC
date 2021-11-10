



package me.earth.phobos.features.command.commands;

import me.earth.phobos.features.command.*;
import me.earth.phobos.*;

public class BaritoneNoStop extends Command
{
    public BaritoneNoStop() {
        super("noStop",  new String[] { "<prefix>",  "<x>",  "<y>",  "<z>" });
    }
    
    public void execute(final String[] commands) {
        if (commands.length == 5) {
            Phobos.baritoneManager.setPrefix(commands[0]);
            int x;
            int y;
            int z;
            try {
                x = Integer.parseInt(commands[1]);
                y = Integer.parseInt(commands[2]);
                z = Integer.parseInt(commands[3]);
            }
            catch (NumberFormatException e) {
                sendMessage("Invalid Input for x,  y or z!");
                Phobos.baritoneManager.stop();
                return;
            }
            Phobos.baritoneManager.start(x,  y,  z);
            return;
        }
        sendMessage("Stoping Baritone-Nostop.");
        Phobos.baritoneManager.stop();
    }
}
