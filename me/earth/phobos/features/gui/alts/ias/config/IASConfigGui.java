//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.gui.alts.ias.config;

import net.minecraftforge.fml.client.config.*;
import net.minecraft.client.gui.*;
import me.earth.phobos.features.gui.alts.ias.*;
import net.minecraftforge.common.config.*;

public class IASConfigGui extends GuiConfig
{
    public IASConfigGui(final GuiScreen parentScreen) {
        super(parentScreen, new ConfigElement(IAS.config.getCategory("general")).getChildElements(), "ias", false, false, GuiConfig.getAbridgedConfigPath(IAS.config.toString()));
    }
}
