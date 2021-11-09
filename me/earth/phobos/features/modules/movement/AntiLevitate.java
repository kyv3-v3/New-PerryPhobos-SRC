



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
