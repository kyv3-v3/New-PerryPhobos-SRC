//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.*;
import net.minecraft.potion.*;
import java.util.*;

public class AntiLevitate extends Module
{
    public AntiLevitate() {
        super("AntiLevitate", "Removes shulker levitation.", Module.Category.MOVEMENT, false, false, false);
    }
    
    public void onUpdate() {
        if (AntiLevitate.mc.player.isPotionActive((Potion)Objects.requireNonNull(Potion.getPotionFromResourceLocation("levitation")))) {
            AntiLevitate.mc.player.removeActivePotionEffect(Potion.getPotionFromResourceLocation("levitation"));
        }
    }
}
