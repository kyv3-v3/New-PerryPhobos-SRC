



package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class PackReload extends Module
{
    public PackReload() {
        super("PackReload",  "Automatically reloads ur pack.",  Category.MISC,  false,  false,  false);
    }
    
    @SubscribeEvent
    @Override
    public void onTick() {
        PackReload.mc.refreshResources();
        this.disable();
    }
}
