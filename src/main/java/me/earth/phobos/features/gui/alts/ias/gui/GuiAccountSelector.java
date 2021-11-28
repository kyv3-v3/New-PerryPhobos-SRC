/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiSlot
 *  net.minecraft.client.gui.GuiTextField
 *  net.minecraft.client.resources.I18n
 *  org.apache.commons.lang3.StringUtils
 *  org.lwjgl.input.Keyboard
 */
package me.earth.phobos.features.gui.alts.ias.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import me.earth.phobos.features.gui.alts.ias.account.AlreadyLoggedInException;
import me.earth.phobos.features.gui.alts.ias.account.ExtendedAccountData;
import me.earth.phobos.features.gui.alts.ias.config.ConfigValues;
import me.earth.phobos.features.gui.alts.ias.enums.EnumBool;
import me.earth.phobos.features.gui.alts.ias.gui.GuiAddAccount;
import me.earth.phobos.features.gui.alts.ias.gui.GuiEditAccount;
import me.earth.phobos.features.gui.alts.ias.tools.HttpTools;
import me.earth.phobos.features.gui.alts.ias.tools.JavaTools;
import me.earth.phobos.features.gui.alts.ias.tools.SkinTools;
import me.earth.phobos.features.gui.alts.iasencrypt.EncryptionTools;
import me.earth.phobos.features.gui.alts.tools.Config;
import me.earth.phobos.features.gui.alts.tools.Tools;
import me.earth.phobos.features.gui.alts.tools.alt.AccountData;
import me.earth.phobos.features.gui.alts.tools.alt.AltDatabase;
import me.earth.phobos.features.gui.alts.tools.alt.AltManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

public class GuiAccountSelector
extends GuiScreen {
    private int selectedAccountIndex;
    private int prevIndex;
    private Throwable loginfailed;
    private ArrayList<ExtendedAccountData> queriedaccounts = this.convertData();
    private List accountsgui;
    private GuiButton login;
    private GuiButton loginoffline;
    private GuiButton delete;
    private GuiButton edit;
    private GuiButton reloadskins;
    private String query;
    private GuiTextField search;

    public void func_73866_w_() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.accountsgui = new List(this.field_146297_k);
        this.accountsgui.func_148134_d(5, 6);
        this.query = I18n.func_135052_a((String)"ias.search", (Object[])new Object[0]);
        this.field_146292_n.clear();
        this.reloadskins = new GuiButton(8, this.field_146294_l / 2 - 154 - 10, this.field_146295_m - 76 - 8, 120, 20, I18n.func_135052_a((String)"ias.reloadskins", (Object[])new Object[0]));
        this.field_146292_n.add(this.reloadskins);
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 + 4 + 40, this.field_146295_m - 52, 120, 20, I18n.func_135052_a((String)"ias.addaccount", (Object[])new Object[0])));
        this.login = new GuiButton(1, this.field_146294_l / 2 - 154 - 10, this.field_146295_m - 52, 120, 20, I18n.func_135052_a((String)"ias.login", (Object[])new Object[0]));
        this.field_146292_n.add(this.login);
        this.edit = new GuiButton(7, this.field_146294_l / 2 - 40, this.field_146295_m - 52, 80, 20, I18n.func_135052_a((String)"ias.edit", (Object[])new Object[0]));
        this.field_146292_n.add(this.edit);
        this.loginoffline = new GuiButton(2, this.field_146294_l / 2 - 154 - 10, this.field_146295_m - 28, 110, 20, I18n.func_135052_a((String)"ias.login", (Object[])new Object[0]) + " " + I18n.func_135052_a((String)"ias.offline", (Object[])new Object[0]));
        this.field_146292_n.add(this.loginoffline);
        this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 + 4 + 50, this.field_146295_m - 28, 110, 20, I18n.func_135052_a((String)"gui.cancel", (Object[])new Object[0])));
        this.delete = new GuiButton(4, this.field_146294_l / 2 - 50, this.field_146295_m - 28, 100, 20, I18n.func_135052_a((String)"ias.delete", (Object[])new Object[0]));
        this.field_146292_n.add(this.delete);
        this.search = new GuiTextField(8, this.field_146289_q, this.field_146294_l / 2 - 80, 14, 160, 16);
        this.search.func_146180_a(this.query);
        this.updateButtons();
        if (!this.queriedaccounts.isEmpty()) {
            SkinTools.buildSkin(this.queriedaccounts.get((int)this.selectedAccountIndex).alias);
        }
    }

    public void func_146274_d() throws IOException {
        super.func_146274_d();
        this.accountsgui.func_178039_p();
    }

    public void func_73876_c() {
        this.search.func_146178_a();
        this.updateText();
        this.updateButtons();
        if (this.prevIndex != this.selectedAccountIndex) {
            this.updateShownSkin();
            this.prevIndex = this.selectedAccountIndex;
        }
    }

    private void updateShownSkin() {
        if (!this.queriedaccounts.isEmpty()) {
            SkinTools.buildSkin(this.queriedaccounts.get((int)this.selectedAccountIndex).alias);
        }
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.func_73864_a(mouseX, mouseY, mouseButton);
        boolean flag = this.search.func_146206_l();
        this.search.func_146192_a(mouseX, mouseY, mouseButton);
        if (!flag && this.search.func_146206_l()) {
            this.query = "";
            this.updateText();
            this.updateQueried();
        }
    }

    private void updateText() {
        this.search.func_146180_a(this.query);
    }

    public void func_146281_b() {
        Keyboard.enableRepeatEvents((boolean)false);
        Config.save();
    }

    public void func_73863_a(int par1, int par2, float par3) {
        this.accountsgui.func_148128_a(par1, par2, par3);
        this.func_73732_a(this.field_146289_q, I18n.func_135052_a((String)"ias.selectaccount", (Object[])new Object[0]), this.field_146294_l / 2, 4, -1);
        if (this.loginfailed != null) {
            this.func_73732_a(this.field_146289_q, this.loginfailed.getLocalizedMessage(), this.field_146294_l / 2, this.field_146295_m - 62, 0xFF6464);
        }
        this.search.func_146194_f();
        super.func_73863_a(par1, par2, par3);
        if (!this.queriedaccounts.isEmpty()) {
            SkinTools.javDrawSkin(8, this.field_146295_m / 2 - 64 - 16, 64, 128);
            Tools.drawBorderedRect(this.field_146294_l - 8 - 64, this.field_146295_m / 2 - 64 - 16, this.field_146294_l - 8, this.field_146295_m / 2 + 64 - 16, 2, -5855578, -13421773);
            if (this.queriedaccounts.get((int)this.selectedAccountIndex).premium == EnumBool.TRUE) {
                this.func_73731_b(this.field_146289_q, I18n.func_135052_a((String)"ias.premium", (Object[])new Object[0]), this.field_146294_l - 8 - 61, this.field_146295_m / 2 - 64 - 13, 0x64FF64);
            } else if (this.queriedaccounts.get((int)this.selectedAccountIndex).premium == EnumBool.FALSE) {
                this.func_73731_b(this.field_146289_q, I18n.func_135052_a((String)"ias.notpremium", (Object[])new Object[0]), this.field_146294_l - 8 - 61, this.field_146295_m / 2 - 64 - 13, 0xFF6464);
            }
            this.func_73731_b(this.field_146289_q, I18n.func_135052_a((String)"ias.timesused", (Object[])new Object[0]), this.field_146294_l - 8 - 61, this.field_146295_m / 2 - 64 - 15 + 12, -1);
            this.func_73731_b(this.field_146289_q, String.valueOf(this.queriedaccounts.get((int)this.selectedAccountIndex).useCount), this.field_146294_l - 8 - 61, this.field_146295_m / 2 - 64 - 15 + 21, -1);
            if (this.queriedaccounts.get((int)this.selectedAccountIndex).useCount > 0) {
                this.func_73731_b(this.field_146289_q, I18n.func_135052_a((String)"ias.lastused", (Object[])new Object[0]), this.field_146294_l - 8 - 61, this.field_146295_m / 2 - 64 - 15 + 30, -1);
                this.func_73731_b(this.field_146289_q, JavaTools.getJavaCompat().getFormattedDate(), this.field_146294_l - 8 - 61, this.field_146295_m / 2 - 64 - 15 + 39, -1);
            }
        }
    }

    protected void func_146284_a(GuiButton button) {
        if (button.field_146124_l) {
            if (button.field_146127_k == 3) {
                this.escape();
            } else if (button.field_146127_k == 0) {
                this.add();
            } else if (button.field_146127_k == 4) {
                this.delete();
            } else if (button.field_146127_k == 1) {
                this.login(this.selectedAccountIndex);
            } else if (button.field_146127_k == 2) {
                this.logino(this.selectedAccountIndex);
            } else if (button.field_146127_k == 7) {
                this.edit();
            } else if (button.field_146127_k == 8) {
                this.reloadSkins();
            } else {
                this.accountsgui.func_148147_a(button);
            }
        }
    }

    private void reloadSkins() {
        Config.save();
        SkinTools.cacheSkins();
        this.updateShownSkin();
    }

    private void escape() {
        this.field_146297_k.func_147108_a(null);
    }

    private void delete() {
        AltDatabase.getInstance().getAlts().remove(this.getCurrentAsEditable());
        if (this.selectedAccountIndex > 0) {
            --this.selectedAccountIndex;
        }
        this.updateQueried();
        this.updateButtons();
    }

    private void add() {
        this.field_146297_k.func_147108_a((GuiScreen)new GuiAddAccount());
    }

    private void logino(int selected) {
        ExtendedAccountData data = this.queriedaccounts.get(selected);
        AltManager.getInstance().setUserOffline(data.alias);
        this.loginfailed = null;
        Minecraft.func_71410_x().func_147108_a(null);
        ExtendedAccountData current = this.getCurrentAsEditable();
        ++Objects.requireNonNull(current).useCount;
        current.lastused = JavaTools.getJavaCompat().getDate();
    }

    private void login(int selected) {
        ExtendedAccountData data = this.queriedaccounts.get(selected);
        this.loginfailed = AltManager.getInstance().setUser(data.user, data.pass);
        if (this.loginfailed == null) {
            Minecraft.func_71410_x().func_147108_a(null);
            ExtendedAccountData current = this.getCurrentAsEditable();
            Objects.requireNonNull(current).premium = EnumBool.TRUE;
            ++current.useCount;
            current.lastused = JavaTools.getJavaCompat().getDate();
        } else if (this.loginfailed instanceof AlreadyLoggedInException) {
            Objects.requireNonNull(this.getCurrentAsEditable()).lastused = JavaTools.getJavaCompat().getDate();
        } else if (HttpTools.ping("https://minecraft.net")) {
            Objects.requireNonNull(this.getCurrentAsEditable()).premium = EnumBool.FALSE;
        }
    }

    private void edit() {
        this.field_146297_k.func_147108_a((GuiScreen)new GuiEditAccount(this.selectedAccountIndex));
    }

    private void updateQueried() {
        this.queriedaccounts = this.convertData();
        if (!this.query.equals(I18n.func_135052_a((String)"ias.search", (Object[])new Object[0])) && !this.query.equals("")) {
            for (int i = 0; i < this.queriedaccounts.size(); ++i) {
                if (!this.queriedaccounts.get((int)i).alias.contains(this.query) && ConfigValues.CASESENSITIVE) {
                    this.queriedaccounts.remove(i);
                    --i;
                    continue;
                }
                if (this.queriedaccounts.get((int)i).alias.toLowerCase().contains(this.query.toLowerCase()) || ConfigValues.CASESENSITIVE) continue;
                this.queriedaccounts.remove(i);
                --i;
            }
        }
        if (!this.queriedaccounts.isEmpty()) {
            while (this.selectedAccountIndex >= this.queriedaccounts.size()) {
                --this.selectedAccountIndex;
            }
        }
    }

    protected void func_73869_a(char character, int keyIndex) {
        if (keyIndex == 200 && !this.queriedaccounts.isEmpty()) {
            if (this.selectedAccountIndex > 0) {
                --this.selectedAccountIndex;
            }
        } else if (keyIndex == 208 && !this.queriedaccounts.isEmpty()) {
            if (this.selectedAccountIndex < this.queriedaccounts.size() - 1) {
                ++this.selectedAccountIndex;
            }
        } else if (keyIndex == 1) {
            this.escape();
        } else if (keyIndex == 211 && this.delete.field_146124_l) {
            this.delete();
        } else if (character == '+') {
            this.add();
        } else if (character == '/' && this.edit.field_146124_l) {
            this.edit();
        } else if (!this.search.func_146206_l() && keyIndex == 19) {
            this.reloadSkins();
        } else if (keyIndex == 28 && !this.search.func_146206_l() && (this.login.field_146124_l || this.loginoffline.field_146124_l)) {
            if ((Keyboard.isKeyDown((int)54) || Keyboard.isKeyDown((int)42)) && this.loginoffline.field_146124_l) {
                this.logino(this.selectedAccountIndex);
            } else if (this.login.field_146124_l) {
                this.login(this.selectedAccountIndex);
            }
        } else if (keyIndex == 14) {
            if (this.search.func_146206_l() && this.query.length() > 0) {
                this.query = this.query.substring(0, this.query.length() - 1);
                this.updateText();
                this.updateQueried();
            }
        } else if (keyIndex == 63) {
            this.reloadSkins();
        } else if (character != '\u0000' && this.search.func_146206_l()) {
            if (keyIndex == 28) {
                this.search.func_146195_b(false);
                this.updateText();
                this.updateQueried();
                return;
            }
            this.query = this.query + character;
            this.updateText();
            this.updateQueried();
        }
    }

    private ArrayList<ExtendedAccountData> convertData() {
        ArrayList tmp = (ArrayList)AltDatabase.getInstance().getAlts().clone();
        ArrayList<ExtendedAccountData> converted = new ArrayList<ExtendedAccountData>();
        int index = 0;
        for (AccountData data : tmp) {
            if (data instanceof ExtendedAccountData) {
                converted.add((ExtendedAccountData)data);
            } else {
                converted.add(new ExtendedAccountData(EncryptionTools.decode(data.user), EncryptionTools.decode(data.pass), data.alias));
                AltDatabase.getInstance().getAlts().set(index, new ExtendedAccountData(EncryptionTools.decode(data.user), EncryptionTools.decode(data.pass), data.alias));
            }
            ++index;
        }
        return converted;
    }

    private ArrayList<AccountData> getAccountList() {
        return AltDatabase.getInstance().getAlts();
    }

    private ExtendedAccountData getCurrentAsEditable() {
        for (AccountData dat : this.getAccountList()) {
            if (!(dat instanceof ExtendedAccountData) || !dat.equals(this.queriedaccounts.get(this.selectedAccountIndex))) continue;
            return (ExtendedAccountData)dat;
        }
        return null;
    }

    private void updateButtons() {
        this.login.field_146124_l = !this.queriedaccounts.isEmpty() && !EncryptionTools.decode(this.queriedaccounts.get((int)this.selectedAccountIndex).pass).equals("");
        this.loginoffline.field_146124_l = !this.queriedaccounts.isEmpty();
        this.delete.field_146124_l = !this.queriedaccounts.isEmpty();
        this.edit.field_146124_l = !this.queriedaccounts.isEmpty();
        this.reloadskins.field_146124_l = !AltDatabase.getInstance().getAlts().isEmpty();
    }

    class List
    extends GuiSlot {
        public List(Minecraft mcIn) {
            super(mcIn, GuiAccountSelector.this.field_146294_l, GuiAccountSelector.this.field_146295_m, 32, GuiAccountSelector.this.field_146295_m - 64, 14);
        }

        protected int func_148127_b() {
            return GuiAccountSelector.this.queriedaccounts.size();
        }

        protected void func_148144_a(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
            GuiAccountSelector.this.selectedAccountIndex = slotIndex;
            GuiAccountSelector.this.updateButtons();
            if (isDoubleClick && ((GuiAccountSelector)GuiAccountSelector.this).login.field_146124_l) {
                GuiAccountSelector.this.login(slotIndex);
            }
        }

        protected boolean func_148131_a(int slotIndex) {
            return slotIndex == GuiAccountSelector.this.selectedAccountIndex;
        }

        protected int func_148138_e() {
            return GuiAccountSelector.this.queriedaccounts.size() * 14;
        }

        protected void func_148123_a() {
            GuiAccountSelector.this.func_146276_q_();
        }

        protected void func_192637_a(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_) {
            ExtendedAccountData data = (ExtendedAccountData)GuiAccountSelector.this.queriedaccounts.get(p_192637_1_);
            String s = data.alias;
            if (StringUtils.isEmpty((CharSequence)s)) {
                s = I18n.func_135052_a((String)"ias.alt", (Object[])new Object[0]) + " " + (p_192637_1_ + 1);
            }
            int color = 0xFFFFFF;
            if (Minecraft.func_71410_x().func_110432_I().func_111285_a().equals(data.alias)) {
                color = 65280;
            }
            GuiAccountSelector.this.func_73731_b(GuiAccountSelector.this.field_146289_q, s, p_192637_2_ + 2, p_192637_3_ + 1, color);
        }
    }
}

