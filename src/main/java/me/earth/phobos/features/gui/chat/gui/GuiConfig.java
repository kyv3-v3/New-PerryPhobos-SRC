/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiNewChat
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiUtilRenderComponents
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraft.util.text.TextFormatting
 *  net.minecraftforge.fml.client.config.GuiSlider
 */
package me.earth.phobos.features.gui.chat.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.earth.phobos.features.gui.chat.BetterChat;
import me.earth.phobos.features.gui.chat.ChatSettings;
import me.earth.phobos.features.gui.chat.handlers.InjectHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiConfig
extends GuiScreen {
    private final ChatSettings settings;
    private final List<ITextComponent> exampleChat = new ArrayList<ITextComponent>();
    private boolean dragging;
    private int chatLeft;
    private int chatRight;
    private int chatTop;
    private int chatBottom;
    private int dragStartX;
    private int dragStartY;
    private GuiButton clearButton;
    private GuiButton smoothButton;
    private GuiSlider scaleSlider;
    private GuiSlider widthSlider;

    public GuiConfig() {
        this.settings = BetterChat.getSettings();
        this.exampleChat.add((ITextComponent)new TextComponentString(I18n.func_135052_a((String)"gui.betterchat.text.example3", (Object[])new Object[0])));
        this.exampleChat.add((ITextComponent)new TextComponentString(I18n.func_135052_a((String)"gui.betterchat.text.example2", (Object[])new Object[0])));
        this.exampleChat.add((ITextComponent)new TextComponentString(I18n.func_135052_a((String)"gui.betterchat.text.example1", (Object[])new Object[0])));
    }

    public void func_73866_w_() {
        InjectHandler.chatGUI.configuring = true;
        this.clearButton = new GuiButton(0, this.field_146294_l / 2 - 120, this.field_146295_m / 2 - 50, 240, 20, this.getPropName("clear") + " " + this.getColoredBool("clear", this.settings.clear));
        this.field_146292_n.add(this.clearButton);
        this.smoothButton = new GuiButton(1, this.field_146294_l / 2 - 120, this.field_146295_m / 2 - 25, 240, 20, this.getPropName("smooth") + " " + this.getColoredBool("smooth", this.settings.smooth));
        this.field_146292_n.add(this.smoothButton);
        this.scaleSlider = new GuiSlider(3, this.field_146294_l / 2 - 120, this.field_146295_m / 2, 240, 20, this.getPropName("scale") + " ", "%", 0.0, 100.0, (double)(this.field_146297_k.field_71474_y.field_96691_E * 100.0f), false, true);
        this.field_146292_n.add(this.scaleSlider);
        this.widthSlider = new GuiSlider(4, this.field_146294_l / 2 - 120, this.field_146295_m / 2 + 25, 240, 20, this.getPropName("width") + " ", "px", 40.0, 320.0, (double)GuiNewChat.func_146233_a((float)this.field_146297_k.field_71474_y.field_96692_F), false, true);
        this.field_146292_n.add(this.widthSlider);
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 120, this.field_146295_m / 2 + 50, 240, 20, this.getPropName("reset")));
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        super.func_73863_a(mouseX, mouseY, partialTicks);
        this.func_73732_a(this.field_146297_k.field_71466_p, I18n.func_135052_a((String)"gui.betterchat.text.title", (Object[])new Object[]{(Object)TextFormatting.GREEN + TextFormatting.BOLD.toString() + "Better Chat" + (Object)TextFormatting.RESET, (Object)TextFormatting.AQUA + TextFormatting.BOLD.toString() + "LlamaLad7"}), this.field_146294_l / 2, this.field_146295_m / 2 - 75, 0xFFFFFF);
        this.func_73732_a(this.field_146297_k.field_71466_p, I18n.func_135052_a((String)"gui.betterchat.text.drag", (Object[])new Object[0]), this.field_146294_l / 2, this.field_146295_m / 2 - 63, 0xFFFFFF);
        if (this.dragging) {
            this.settings.xOffset += mouseX - this.dragStartX;
            this.settings.yOffset += mouseY - this.dragStartY;
            this.dragStartX = mouseX;
            this.dragStartY = mouseY;
        }
        this.field_146297_k.field_71474_y.field_96691_E = (float)this.scaleSlider.getValueInt() / 100.0f;
        this.field_146297_k.field_71474_y.field_96692_F = ((float)this.widthSlider.getValueInt() - 40.0f) / 280.0f;
        this.drawExampleChat();
    }

    public void drawExampleChat() {
        ArrayList lines = new ArrayList();
        int i = MathHelper.func_76141_d((float)((float)InjectHandler.chatGUI.func_146228_f() / InjectHandler.chatGUI.func_146244_h()));
        for (ITextComponent line : this.exampleChat) {
            lines.addAll(GuiUtilRenderComponents.func_178908_a((ITextComponent)line, (int)i, (FontRenderer)this.field_146297_k.field_71466_p, (boolean)false, (boolean)false));
        }
        Collections.reverse(lines);
        GlStateManager.func_179094_E();
        ScaledResolution scaledresolution = new ScaledResolution(this.field_146297_k);
        GlStateManager.func_179109_b((float)(2.0f + (float)this.settings.xOffset), (float)(8.0f + (float)this.settings.yOffset + (float)scaledresolution.func_78328_b() - 48.0f), (float)0.0f);
        float f = this.field_146297_k.field_71474_y.field_74357_r * 0.9f + 0.1f;
        float f1 = this.field_146297_k.field_71474_y.field_96691_E;
        int k = MathHelper.func_76123_f((float)((float)InjectHandler.chatGUI.func_146228_f() / f1));
        GlStateManager.func_179152_a((float)f1, (float)f1, (float)1.0f);
        int i1 = 0;
        double d0 = 1.0;
        int l1 = (int)(255.0 * d0);
        l1 = (int)((float)l1 * f);
        GlStateManager.func_179141_d();
        GlStateManager.func_179147_l();
        this.chatLeft = this.settings.xOffset;
        this.chatRight = (int)((float)this.settings.xOffset + (float)(k + 4) * f1);
        this.chatBottom = 8 + this.settings.yOffset + scaledresolution.func_78328_b() - 48;
        for (ITextComponent message : lines) {
            int j2 = -i1 * 9;
            if (!this.settings.clear) {
                GuiConfig.func_73734_a((int)-2, (int)(j2 - 9), (int)(k + 4), (int)j2, (int)(l1 / 2 << 24));
            }
            this.field_146297_k.field_71466_p.func_175063_a(message.func_150254_d(), 0.0f, (float)(j2 - 8), 0xFFFFFF + (l1 << 24));
            ++i1;
        }
        this.chatTop = (int)((float)(8 + this.settings.yOffset + scaledresolution.func_78328_b() - 48) + (float)(-i1 * 9) * f1);
        GlStateManager.func_179118_c();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }

    public void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.func_73864_a(mouseX, mouseY, mouseButton);
        if (mouseButton == 0 && mouseX >= this.chatLeft && mouseX <= this.chatRight && mouseY >= this.chatTop && mouseY <= this.chatBottom) {
            this.dragging = true;
            this.dragStartX = mouseX;
            this.dragStartY = mouseY;
        }
    }

    public void func_146286_b(int mouseX, int mouseY, int mouseButton) {
        super.func_146286_b(mouseX, mouseY, mouseButton);
        this.dragging = false;
    }

    public void func_146281_b() {
        this.settings.saveConfig();
        InjectHandler.chatGUI.configuring = false;
        this.field_146297_k.field_71474_y.func_74303_b();
    }

    protected void func_146284_a(GuiButton button) {
        switch (button.field_146127_k) {
            case 0: {
                this.settings.clear = !this.settings.clear;
                button.field_146126_j = this.getPropName("clear") + " " + this.getColoredBool("clear", this.settings.clear);
                break;
            }
            case 1: {
                this.settings.smooth = !this.settings.smooth;
                button.field_146126_j = this.getPropName("smooth") + " " + this.getColoredBool("smooth", this.settings.smooth);
                break;
            }
            case 2: {
                this.settings.resetConfig();
                this.clearButton.field_146126_j = this.getPropName("clear") + " " + this.getColoredBool("clear", this.settings.clear);
                this.smoothButton.field_146126_j = this.getPropName("smooth") + " " + this.getColoredBool("smooth", this.settings.smooth);
                this.scaleSlider.setValue((double)(this.field_146297_k.field_71474_y.field_96691_E * 100.0f));
                this.scaleSlider.updateSlider();
                this.widthSlider.setValue((double)GuiNewChat.func_146233_a((float)this.field_146297_k.field_71474_y.field_96692_F));
                this.widthSlider.updateSlider();
            }
        }
    }

    public boolean func_73868_f() {
        return false;
    }

    private String getColoredBool(String prop, boolean bool) {
        if (bool) {
            return (Object)TextFormatting.GREEN + I18n.func_135052_a((String)("gui.betterchat.text." + prop + ".enabled"), (Object[])new Object[0]);
        }
        return (Object)TextFormatting.RED + I18n.func_135052_a((String)("gui.betterchat.text." + prop + ".disabled"), (Object[])new Object[0]);
    }

    private String getPropName(String prop) {
        return I18n.func_135052_a((String)("gui.betterchat.text." + prop + ".name"), (Object[])new Object[0]);
    }
}

