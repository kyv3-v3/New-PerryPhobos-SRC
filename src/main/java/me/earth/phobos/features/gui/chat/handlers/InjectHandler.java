/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiIngame
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.ObfuscationReflectionHelper
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package me.earth.phobos.features.gui.chat.handlers;

import me.earth.phobos.features.gui.chat.gui.GuiBetterChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class InjectHandler {
    public static GuiBetterChat chatGUI;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        chatGUI = new GuiBetterChat(Minecraft.func_71410_x());
        ObfuscationReflectionHelper.setPrivateValue(GuiIngame.class, (Object)Minecraft.func_71410_x().field_71456_v, (Object)((Object)chatGUI), (String)"field_73840_e");
    }
}

