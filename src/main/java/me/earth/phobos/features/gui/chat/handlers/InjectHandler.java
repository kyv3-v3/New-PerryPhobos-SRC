



package me.earth.phobos.features.gui.chat.handlers;

import me.earth.phobos.features.gui.chat.gui.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.common.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class InjectHandler
{
    public static GuiBetterChat chatGUI;
    
    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        InjectHandler.chatGUI = new GuiBetterChat(Minecraft.getMinecraft());
        ObfuscationReflectionHelper.setPrivateValue((Class)GuiIngame.class,  (Object)Minecraft.getMinecraft().ingameGUI,  (Object)InjectHandler.chatGUI,  "persistantChatGUI");
    }
}
