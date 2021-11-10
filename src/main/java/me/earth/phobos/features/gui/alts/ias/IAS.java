



package me.earth.phobos.features.gui.alts.ias;

import net.minecraftforge.fml.common.*;
import net.minecraftforge.common.config.*;
import me.earth.phobos.features.gui.alts.ias.config.*;
import net.minecraft.client.resources.*;
import me.earth.phobos.features.gui.alts.iasencrypt.*;
import me.earth.phobos.features.gui.alts.*;
import net.minecraftforge.common.*;
import me.earth.phobos.features.gui.alts.ias.events.*;
import net.minecraftforge.fml.common.event.*;
import me.earth.phobos.features.gui.alts.ias.tools.*;

@Mod(modid = "ias", name = "In-Game Account Switcher", clientSideOnly = true, guiFactory = "me.earth.phobos.features.gui.alts.ias.config.IASGuiFactory", updateJSON = "https://thefireplace.bitnamiapp.com/jsons/ias.json", acceptedMinecraftVersions = "[1.11,)")
public class IAS
{
    public static Configuration config;
    private static Property CASESENSITIVE_PROPERTY;
    private static Property ENABLERELOG_PROPERTY;
    
    public static void syncConfig() {
        ConfigValues.CASESENSITIVE = IAS.CASESENSITIVE_PROPERTY.getBoolean();
        ConfigValues.ENABLERELOG = IAS.ENABLERELOG_PROPERTY.getBoolean();
        if (IAS.config.hasChanged()) {
            IAS.config.save();
        }
    }
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        (IAS.config = new Configuration(event.getSuggestedConfigurationFile())).load();
        IAS.CASESENSITIVE_PROPERTY = IAS.config.get("general", "ias.cfg.casesensitive", false, I18n.format("ias.cfg.casesensitive.tooltip", new Object[0]));
        IAS.ENABLERELOG_PROPERTY = IAS.config.get("general", "ias.cfg.enablerelog", false, I18n.format("ias.cfg.enablerelog.tooltip", new Object[0]));
        syncConfig();
        if (!event.getModMetadata().version.equals("${version}")) {
            Standards.updateFolder();
        }
        else {
            System.out.println("Dev environment detected!");
        }
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        MR.init();
        MinecraftForge.EVENT_BUS.register((Object)new ClientEvents());
        Standards.importAccounts();
    }
    
    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        SkinTools.cacheSkins();
    }
}
