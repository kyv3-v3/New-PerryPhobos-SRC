/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.inventory.Slot
 *  org.lwjgl.input.Keyboard
 */
package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Bind;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.TextUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import org.lwjgl.input.Keyboard;

public class KitDelete
extends Module {
    private final Setting<Bind> deleteKey = this.register(new Setting<Bind>("Key", new Bind(-1)));
    private boolean keyDown;

    public KitDelete() {
        super("KitDelete", "Automates /deleteukit if u have too many kits.", Module.Category.MISC, false, false, false);
    }

    @Override
    public void onTick() {
        if (this.deleteKey.getValue().getKey() != -1) {
            if (KitDelete.mc.field_71462_r instanceof GuiContainer && Keyboard.isKeyDown((int)this.deleteKey.getValue().getKey())) {
                Slot slot = ((GuiContainer)KitDelete.mc.field_71462_r).getSlotUnderMouse();
                if (slot != null && !this.keyDown) {
                    KitDelete.mc.field_71439_g.func_71165_d("/deleteukit " + TextUtil.stripColor(slot.func_75211_c().func_82833_r()));
                    this.keyDown = true;
                }
            } else if (this.keyDown) {
                this.keyDown = false;
            }
        }
    }
}

