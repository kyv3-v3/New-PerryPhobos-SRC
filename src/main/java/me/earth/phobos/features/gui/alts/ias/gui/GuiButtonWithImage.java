/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.ResourceLocation
 */
package me.earth.phobos.features.gui.alts.ias.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonWithImage
extends GuiButton {
    private static final ResourceLocation customButtonTextures = new ResourceLocation("accswitcher:textures/gui/custombutton.png");

    public GuiButtonWithImage(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.field_146125_m) {
            FontRenderer fontrenderer = mc.field_71466_p;
            mc.func_110434_K().func_110577_a(customButtonTextures);
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            this.field_146123_n = mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
            int k = this.func_146114_a(this.field_146123_n);
            GlStateManager.func_179147_l();
            GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
            GlStateManager.func_179112_b((int)770, (int)771);
            this.func_73729_b(this.field_146128_h, this.field_146129_i, 0, 46 + k * 20, this.field_146120_f / 2, this.field_146121_g);
            this.func_73729_b(this.field_146128_h + this.field_146120_f / 2, this.field_146129_i, 200 - this.field_146120_f / 2, 46 + k * 20, this.field_146120_f / 2, this.field_146121_g);
            this.func_146119_b(mc, mouseX, mouseY);
            int l = 0xE0E0E0;
            if (this.packedFGColour != 0) {
                l = this.packedFGColour;
            } else if (!this.field_146124_l) {
                l = 0xA0A0A0;
            } else if (this.field_146123_n) {
                l = 0xFFFFA0;
            }
            this.func_73732_a(fontrenderer, this.field_146126_j, this.field_146128_h + this.field_146120_f / 2, this.field_146129_i + (this.field_146121_g - 8) / 2, l);
        }
    }
}

