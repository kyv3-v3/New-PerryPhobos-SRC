//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.command.commands;

import me.earth.phobos.features.command.*;

public class ClearRamCommand extends Command
{
    public ClearRamCommand() {
        super("clearram");
    }
    
    public void execute(final String[] commands) {
        System.gc();
        Command.sendMessage("Finished clearing the ram.", false);
    }
}
