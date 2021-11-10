



package me.earth.phobos.features.gui.alts.ias.gui;

import me.earth.phobos.features.gui.alts.ias.account.*;
import me.earth.phobos.features.gui.alts.tools.alt.*;

public class GuiAddAccount extends AbstractAccountGui
{
    public GuiAddAccount() {
        super("ias.addaccount");
    }
    
    public void complete() {
        AltDatabase.getInstance().getAlts().add((AccountData)new ExtendedAccountData(this.getUsername(),  this.getPassword(),  this.getUsername()));
    }
}
