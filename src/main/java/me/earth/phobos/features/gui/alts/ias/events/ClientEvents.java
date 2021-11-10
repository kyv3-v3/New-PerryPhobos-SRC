



package me.earth.phobos.features.gui.alts.ias.events;

import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.earth.phobos.features.gui.alts.tools.*;
import net.minecraft.client.*;
import me.earth.phobos.features.gui.alts.ias.gui.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.client.resources.*;
import net.minecraft.client.gui.*;
import net.minecraftforge.fml.client.event.*;
import me.earth.phobos.features.gui.alts.ias.*;

public class ClientEvents
{
    @SubscribeEvent
    public void guiEvent(final GuiScreenEvent.InitGuiEvent.Post event) {
        final GuiScreen gui = event.getGui();
        if (gui instanceof GuiMainMenu) {
            event.getButtonList().add(new GuiButtonWithImage(20,  gui.width / 2 + 104,  gui.height / 4 + 48 + 72 + 12,  20,  20,  ""));
        }
    }
    
    @SubscribeEvent
    public void onClick(final GuiScreenEvent.ActionPerformedEvent event) {
        if (event.getGui() instanceof GuiMainMenu && event.getButton().id == 20) {
            if (Config.getInstance() == null) {
                Config.load();
            }
            Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new GuiAccountSelector());
        }
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.RenderTickEvent t) {
        final GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if (screen instanceof GuiMainMenu) {
            screen.drawCenteredString(Minecraft.getMinecraft().fontRenderer,  I18n.format("ias.loggedinas",  new Object[0]) + Minecraft.getMinecraft().getSession().getUsername() + ".",  screen.width / 2,  screen.height / 4 + 48 + 72 + 12 + 22,  -3372920);
        }
        else if (screen instanceof GuiMultiplayer && Minecraft.getMinecraft().getSession().getToken().equals("0")) {
            screen.drawCenteredString(Minecraft.getMinecraft().fontRenderer,  I18n.format("ias.offlinemode",  new Object[0]),  screen.width / 2,  10,  16737380);
        }
    }
    
    @SubscribeEvent
    public void configChanged(final ConfigChangedEvent event) {
        if (event.getModID().equals("ias")) {
            IAS.syncConfig();
        }
    }
}
