



package me.earth.phobos.features.gui.alts.ias.gui;

import me.earth.phobos.features.gui.alts.ias.account.*;
import me.earth.phobos.features.gui.alts.tools.alt.*;
import me.earth.phobos.features.gui.alts.ias.tools.*;
import me.earth.phobos.features.gui.alts.ias.enums.*;
import me.earth.phobos.features.gui.alts.iasencrypt.*;

class GuiEditAccount extends AbstractAccountGui
{
    private final ExtendedAccountData data;
    private final int selectedIndex;
    
    public GuiEditAccount(final int index) {
        super("ias.editaccount");
        this.selectedIndex = index;
        final AccountData data = AltDatabase.getInstance().getAlts().get(index);
        if (data instanceof ExtendedAccountData) {
            this.data = (ExtendedAccountData)data;
        }
        else {
            this.data = new ExtendedAccountData(data.user,  data.pass,  data.alias,  0,  JavaTools.getJavaCompat().getDate(),  EnumBool.UNKNOWN);
        }
    }
    
    public void initGui() {
        super.initGui();
        this.setUsername(EncryptionTools.decode(this.data.user));
        this.setPassword(EncryptionTools.decode(this.data.pass));
    }
    
    public void complete() {
        AltDatabase.getInstance().getAlts().set(this.selectedIndex,  (AccountData)new ExtendedAccountData(this.getUsername(),  this.getPassword(),  this.hasUserChanged ? this.getUsername() : this.data.alias,  this.data.useCount,  this.data.lastused,  this.data.premium));
    }
}
