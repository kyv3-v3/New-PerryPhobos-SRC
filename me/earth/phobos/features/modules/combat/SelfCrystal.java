//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.modules.combat;

import me.earth.phobos.features.modules.*;
import net.minecraft.entity.player.*;

public class SelfCrystal extends Module
{
    public SelfCrystal() {
        super("SelfCrystal", "Makes CA target urself.", Category.COMBAT, true, false, false);
    }
    
    @Override
    public void onTick() {
        if (AutoCrystal.getInstance().isEnabled()) {
            AutoCrystal.target = (EntityPlayer)SelfCrystal.mc.player;
        }
    }
}
