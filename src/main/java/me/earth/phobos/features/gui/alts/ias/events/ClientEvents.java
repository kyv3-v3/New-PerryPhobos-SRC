/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiMainMenu
 *  net.minecraft.client.gui.GuiMultiplayer
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.resources.I18n
 *  net.minecraftforge.client.event.GuiScreenEvent$ActionPerformedEvent
 *  net.minecraftforge.client.event.GuiScreenEvent$InitGuiEvent$Post
 *  net.minecraftforge.fml.client.event.ConfigChangedEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$RenderTickEvent
 */
package me.earth.phobos.features.gui.alts.ias.events;

import me.earth.phobos.features.gui.alts.ias.IAS;
import me.earth.phobos.features.gui.alts.ias.gui.GuiAccountSelector;
import me.earth.phobos.features.gui.alts.ias.gui.GuiButtonWithImage;
import me.earth.phobos.features.gui.alts.tools.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClientEvents {
    @SubscribeEvent
    public void guiEvent(GuiScreenEvent.InitGuiEvent.Post event) {
        GuiScreen gui = event.getGui();
        if (gui instanceof GuiMainMenu) {
            event.getButtonList().add(new GuiButtonWithImage(20, gui.field_146294_l / 2 + 104, gui.field_146295_m / 4 + 48 + 72 + 12, 20, 20, ""));
        }
    }

    @SubscribeEvent
    public void onClick(GuiScreenEvent.ActionPerformedEvent event) {
        if (event.getGui() instanceof GuiMainMenu && event.getButton().field_146127_k == 20) {
            if (Config.getInstance() == null) {
                Config.load();
            }
            Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiAccountSelector());
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent t) {
        GuiScreen screen = Minecraft.func_71410_x().field_71462_r;
        if (screen instanceof GuiMainMenu) {
            screen.func_73732_a(Minecraft.func_71410_x().field_71466_p, I18n.func_135052_a((String)"ias.loggedinas", (Object[])new Object[0]) + Minecraft.func_71410_x().func_110432_I().func_111285_a() + ".", screen.field_146294_l / 2, screen.field_146295_m / 4 + 48 + 72 + 12 + 22, -3372920);
        } else if (screen instanceof GuiMultiplayer && Minecraft.func_71410_x().func_110432_I().func_148254_d().equals("0")) {
            screen.func_73732_a(Minecraft.func_71410_x().field_71466_p, I18n.func_135052_a((String)"ias.offlinemode", (Object[])new Object[0]), screen.field_146294_l / 2, 10, 0xFF6464);
        }
    }

    @SubscribeEvent
    public void configChanged(ConfigChangedEvent event) {
        if (event.getModID().equals("ias")) {
            IAS.syncConfig();
        }
    }
}

