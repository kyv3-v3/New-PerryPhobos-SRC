



package me.earth.phobos.features.gui.alts.tools.alt;

import java.io.*;
import me.earth.phobos.features.gui.alts.iasencrypt.*;

public class AccountData implements Serializable
{
    public static final long serialVersionUID = -147985492L;
    public final String user;
    public final String pass;
    public String alias;
    
    protected AccountData(final String user,  final String pass,  final String alias) {
        this.user = EncryptionTools.encode(user);
        this.pass = EncryptionTools.encode(pass);
        this.alias = alias;
    }
    
    public boolean equalsBasic(final AccountData obj) {
        return this == obj || (obj != null && this.getClass() == obj.getClass() && this.user.equals(obj.user));
    }
}
