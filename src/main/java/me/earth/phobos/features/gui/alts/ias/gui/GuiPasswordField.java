/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  joptsimple.internal.Strings
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
 */
package me.earth.phobos.features.gui.alts.ias.gui;

import joptsimple.internal.Strings;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiPasswordField
extends GuiTextField {
    public GuiPasswordField(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
        super(componentId, fontrendererObj, x, y, par5Width, par6Height);
    }

    public void func_146194_f() {
        String password = this.func_146179_b();
        this.replaceText(Strings.repeat((char)'*', (int)this.func_146179_b().length()));
        super.func_146194_f();
        this.replaceText(password);
    }

    public boolean func_146201_a(char typedChar, int keyCode) {
        return !GuiScreen.func_175280_f((int)keyCode) && !GuiScreen.func_175277_d((int)keyCode) && super.func_146201_a(typedChar, keyCode);
    }

    public boolean func_146192_a(int mouseX, int mouseY, int mouseButton) {
        String password = this.func_146179_b();
        this.replaceText(Strings.repeat((char)'*', (int)this.func_146179_b().length()));
        super.func_146192_a(mouseX, mouseY, mouseButton);
        this.replaceText(password);
        return true;
    }

    private void replaceText(String newText) {
        int cursorPosition = this.func_146198_h();
        int selectionEnd = this.func_146186_n();
        this.func_146180_a(newText);
        this.func_146190_e(cursorPosition);
        this.func_146199_i(selectionEnd);
    }
}

