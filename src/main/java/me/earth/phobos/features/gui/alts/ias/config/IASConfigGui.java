



package me.earth.phobos.features.gui.alts.ias.config;

import net.minecraftforge.fml.client.config.*;
import net.minecraft.client.gui.*;
import me.earth.phobos.features.gui.alts.ias.*;
import net.minecraftforge.common.config.*;

public class IASConfigGui extends GuiConfig
{
    public IASConfigGui(final GuiScreen parentScreen) {
        super(parentScreen,  new ConfigElement(IAS.config.getCategory("general")).getChildElements(),  "ias",  false,  false,  GuiConfig.getAbridgedConfigPath(IAS.config.toString()));
    }
}
