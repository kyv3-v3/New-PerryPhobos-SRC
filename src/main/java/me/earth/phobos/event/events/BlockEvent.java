



package me.earth.phobos.event.events;

import me.earth.phobos.event.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;

@Cancelable
public class BlockEvent extends EventStage
{
    public BlockPos pos;
    public EnumFacing facing;
    
    public BlockEvent(final int stage,  final BlockPos pos,  final EnumFacing facing) {
        super(stage);
        this.pos = pos;
        this.facing = facing;
    }
}
