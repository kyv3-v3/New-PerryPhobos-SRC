//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class AutoWalk extends Module
{
    public AutoWalk() {
        super("AutoWalk", "Automatically walks forward.", Module.Category.MOVEMENT, true, false, false);
    }
    
    @SubscribeEvent
    public void onUpdateInput(final InputUpdateEvent event) {
        event.getMovementInput().moveForward = 1.0f;
    }
}
