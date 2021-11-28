/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.command.CommandBase
 *  net.minecraft.command.CommandException
 *  net.minecraft.command.ICommandSender
 *  net.minecraft.server.MinecraftServer
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package me.earth.phobos.features.gui.chat.command;

import me.earth.phobos.features.gui.chat.gui.GuiConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CommandConfig
extends CommandBase {
    public String func_71517_b() {
        return "betterchat";
    }

    public String func_71518_a(ICommandSender sender) {
        return "/betterchat";
    }

    public boolean func_184882_a(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiConfig());
    }
}

