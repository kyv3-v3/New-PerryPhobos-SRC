//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.gui.alts.ias.config;

import net.minecraftforge.fml.client.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import java.util.*;

public class IASGuiFactory implements IModGuiFactory
{
    public void initialize(final Minecraft minecraftInstance) {
    }
    
    public boolean hasConfigGui() {
        return true;
    }
    
    public GuiScreen createConfigGui(final GuiScreen parentScreen) {
        return (GuiScreen)new IASConfigGui(parentScreen);
    }
    
    public Set<IModGuiFactory.RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }
}
