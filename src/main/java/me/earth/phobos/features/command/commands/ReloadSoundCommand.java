



package me.earth.phobos.features.command.commands;

import me.earth.phobos.features.command.*;
import net.minecraft.client.audio.*;
import net.minecraftforge.fml.common.*;

public class ReloadSoundCommand extends Command
{
    public ReloadSoundCommand() {
        super("reloadsound", new String[0]);
    }
    
    public void execute(final String[] commands) {
        try {
            final SoundManager sndManager = (SoundManager)ObfuscationReflectionHelper.getPrivateValue((Class)SoundHandler.class, (Object)ReloadSoundCommand.mc.getSoundHandler(), new String[] { "sndManager", "sndManager" });
            sndManager.reloadSoundSystem();
            sendMessage("§aReloaded Sound System.");
        }
        catch (Exception e) {
            System.out.println("Could not restart sound manager: " + e);
            e.printStackTrace();
            sendMessage("§cCouldn't Reload Sound System!");
        }
    }
}
