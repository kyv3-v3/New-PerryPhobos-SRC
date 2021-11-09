//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.event.events;

import me.earth.phobos.event.*;
import net.minecraft.util.math.*;

public class BlockBreakingEvent extends EventStage
{
    public BlockPos pos;
    public int breakingID;
    public int breakStage;
    
    public BlockBreakingEvent(final BlockPos pos, final int breakingID, final int breakStage) {
        this.pos = pos;
        this.breakingID = breakingID;
        this.breakStage = breakStage;
    }
}
