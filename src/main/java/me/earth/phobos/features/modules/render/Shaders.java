/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.ResourceLocation
 */
package me.earth.phobos.features.modules.render;

import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class Shaders
extends Module {
    public Setting<Mode> shader = this.register(new Setting<Mode>("Mode", Mode.green));

    public Shaders() {
        super("Shaders", "Adds back 1.8 shaders.", Module.Category.RENDER, false, false, false);
    }

    @Override
    public void onUpdate() {
        if (OpenGlHelper.field_148824_g && mc.func_175606_aa() instanceof EntityPlayer) {
            if (Shaders.mc.field_71460_t.func_147706_e() != null) {
                Shaders.mc.field_71460_t.func_147706_e().func_148021_a();
            }
            try {
                Shaders.mc.field_71460_t.func_175069_a(new ResourceLocation("shaders/post/" + (Object)((Object)this.shader.getValue()) + ".json"));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Shaders.mc.field_71460_t.func_147706_e() != null && Shaders.mc.field_71462_r == null) {
            Shaders.mc.field_71460_t.func_147706_e().func_148021_a();
        }
    }

    @Override
    public String getDisplayInfo() {
        return this.shader.currentEnumName();
    }

    @Override
    public void onDisable() {
        if (Shaders.mc.field_71460_t.func_147706_e() != null) {
            Shaders.mc.field_71460_t.func_147706_e().func_148021_a();
        }
    }

    public static enum Mode {
        notch,
        antialias,
        art,
        bits,
        blobs,
        blobs2,
        blur,
        bumpy,
        color_convolve,
        creeper,
        deconverge,
        desaturate,
        flip,
        fxaa,
        green,
        invert,
        ntsc,
        pencil,
        phosphor,
        sobel,
        spider,
        wobble;

    }
}

