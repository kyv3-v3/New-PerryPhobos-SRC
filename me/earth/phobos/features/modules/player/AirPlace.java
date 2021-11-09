//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.util.*;
import me.earth.phobos.util.*;
import net.minecraft.util.math.*;

public class AirPlace extends Module
{
    private final Setting<Mode> mode;
    
    public AirPlace() {
        super("AirPlace", "Place blocks in the air for 1.13+ servers.", Module.Category.PLAYER, false, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.UP));
    }
    
    public void onEnable() {
        switch (this.mode.getValue()) {
            case UP: {
                final BlockPos pos = Util.mc.player.getPosition().add(0, 1, 0);
                BlockUtil.placeBlock(pos, EnumFacing.UP, false);
                this.disable();
            }
            case DOWN: {
                final BlockPos pos = Util.mc.player.getPosition().add(0, 0, 0);
                BlockUtil.placeBlock(pos, EnumFacing.DOWN, false);
                this.disable();
                break;
            }
        }
    }
    
    public enum Mode
    {
        UP, 
        DOWN;
    }
}
