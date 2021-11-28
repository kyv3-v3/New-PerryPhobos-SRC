/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
 *  net.minecraft.client.resources.I18n
 *  org.lwjgl.input.Keyboard
 */
package me.earth.phobos.features.gui.alts.ias.gui;

import java.io.IOException;
import me.earth.phobos.features.gui.alts.ias.gui.GuiAccountSelector;
import me.earth.phobos.features.gui.alts.ias.gui.GuiPasswordField;
import me.earth.phobos.features.gui.alts.iasencrypt.EncryptionTools;
import me.earth.phobos.features.gui.alts.tools.alt.AccountData;
import me.earth.phobos.features.gui.alts.tools.alt.AltDatabase;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

public abstract class AbstractAccountGui
extends GuiScreen {
    private final String actionString;
    protected boolean hasUserChanged;
    private GuiTextField username;
    private GuiTextField password;
    private GuiButton complete;

    public AbstractAccountGui(String actionString) {
        this.actionString = actionString;
    }

    public void func_73866_w_() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.field_146292_n.clear();
        this.complete = new GuiButton(2, this.field_146294_l / 2 - 152, this.field_146295_m - 28, 150, 20, I18n.func_135052_a((String)this.actionString, (Object[])new Object[0]));
        this.field_146292_n.add(this.complete);
        this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 + 2, this.field_146295_m - 28, 150, 20, I18n.func_135052_a((String)"gui.cancel", (Object[])new Object[0])));
        this.username = new GuiTextField(0, this.field_146289_q, this.field_146294_l / 2 - 100, 60, 200, 20);
        this.username.func_146195_b(true);
        this.username.func_146203_f(64);
        this.password = new GuiPasswordField(1, this.field_146289_q, this.field_146294_l / 2 - 100, 90, 200, 20);
        this.password.func_146203_f(64);
        this.complete.field_146124_l = false;
    }

    public void func_73863_a(int par1, int par2, float par3) {
        this.func_146276_q_();
        this.func_73732_a(this.field_146289_q, I18n.func_135052_a((String)this.actionString, (Object[])new Object[0]), this.field_146294_l / 2, 7, -1);
        this.func_73732_a(this.field_146289_q, I18n.func_135052_a((String)"ias.username", (Object[])new Object[0]), this.field_146294_l / 2 - 130, 66, -1);
        this.func_73732_a(this.field_146289_q, I18n.func_135052_a((String)"ias.password", (Object[])new Object[0]), this.field_146294_l / 2 - 130, 96, -1);
        this.username.func_146194_f();
        this.password.func_146194_f();
        super.func_73863_a(par1, par2, par3);
    }

    protected void func_73869_a(char character, int keyIndex) {
        if (keyIndex == 1) {
            this.escape();
        } else if (keyIndex == 28) {
            if (this.username.func_146206_l()) {
                this.username.func_146195_b(false);
                this.password.func_146195_b(true);
            } else if (this.password.func_146206_l() && this.complete.field_146124_l) {
                this.complete();
                this.escape();
            }
        } else if (keyIndex == 15) {
            this.username.func_146195_b(!this.username.func_146206_l());
            this.password.func_146195_b(!this.password.func_146206_l());
        } else {
            this.username.func_146201_a(character, keyIndex);
            this.password.func_146201_a(character, keyIndex);
            if (this.username.func_146206_l()) {
                this.hasUserChanged = true;
            }
        }
    }

    public void func_73876_c() {
        this.username.func_146178_a();
        this.password.func_146178_a();
        this.complete.field_146124_l = this.canComplete();
    }

    protected void func_146284_a(GuiButton button) {
        if (button.field_146124_l) {
            if (button.field_146127_k == 2) {
                this.complete();
                this.escape();
            } else if (button.field_146127_k == 3) {
                this.escape();
            }
        }
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.func_73864_a(mouseX, mouseY, mouseButton);
        this.username.func_146192_a(mouseX, mouseY, mouseButton);
        this.password.func_146192_a(mouseX, mouseY, mouseButton);
    }

    public void func_146281_b() {
        Keyboard.enableRepeatEvents((boolean)false);
    }

    private void escape() {
        this.field_146297_k.func_147108_a((GuiScreen)new GuiAccountSelector());
    }

    public String getUsername() {
        return this.username.func_146179_b();
    }

    public void setUsername(String username) {
        this.username.func_146180_a(username);
    }

    public String getPassword() {
        return this.password.func_146179_b();
    }

    public void setPassword(String password) {
        this.password.func_146180_a(password);
    }

    protected boolean accountNotInList() {
        for (AccountData data : AltDatabase.getInstance().getAlts()) {
            if (!EncryptionTools.decode(data.user).equals(this.getUsername())) continue;
            return false;
        }
        return true;
    }

    public boolean canComplete() {
        return this.getUsername().length() > 0 && this.accountNotInList();
    }

    public abstract void complete();
}

