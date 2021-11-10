



package me.earth.phobos.features.command.commands;

import me.earth.phobos.features.command.*;
import me.earth.phobos.*;
import java.util.*;

public class FriendCommand extends Command
{
    public FriendCommand() {
        super("friend", new String[] { "<add/del/name/clear>", "<name>" });
    }
    
    public void execute(final String[] commands) {
        if (commands.length == 1) {
            if (Phobos.friendManager.getFriends().isEmpty()) {
                sendMessage("You currently don't have any friends added.");
            }
            else {
                sendMessage("Friends: ");
                for (final Map.Entry<String, UUID> entry : Phobos.friendManager.getFriends().entrySet()) {
                    sendMessage((String)entry.getKey());
                }
            }
            return;
        }
        if (commands.length == 2) {
            if ("reset".equals(commands[0])) {
                Phobos.friendManager.onLoad();
                sendMessage("Friends got reset.");
            }
            else {
                sendMessage(commands[0] + (Phobos.friendManager.isFriend(commands[0]) ? " is friended." : " isnt friended."));
            }
            return;
        }
        if (commands.length >= 2) {
            final String s = commands[0];
            switch (s) {
                case "add": {
                    Phobos.friendManager.addFriend(commands[1]);
                    sendMessage("§b" + commands[1] + " has been friended");
                    break;
                }
                case "del": {
                    Phobos.friendManager.removeFriend(commands[1]);
                    sendMessage("§c" + commands[1] + " has been unfriended");
                    break;
                }
                default: {
                    sendMessage("§cBad Command, try: friend <add/del/name> <name>.");
                    break;
                }
            }
        }
    }
}
