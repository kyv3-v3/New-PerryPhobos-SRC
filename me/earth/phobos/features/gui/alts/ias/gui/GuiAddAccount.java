//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.gui.alts.ias.gui;

import me.earth.phobos.features.gui.alts.ias.account.*;
import me.earth.phobos.features.gui.alts.tools.alt.*;

public class GuiAddAccount extends AbstractAccountGui
{
    public GuiAddAccount() {
        super("ias.addaccount");
    }
    
    public void complete() {
        AltDatabase.getInstance().getAlts().add((AccountData)new ExtendedAccountData(this.getUsername(), this.getPassword(), this.getUsername()));
    }
}
