//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.gui.alts.tools.alt;

import net.minecraft.client.*;
import com.mojang.authlib.yggdrasil.*;
import com.mojang.authlib.*;
import me.earth.phobos.features.gui.alts.iasencrypt.*;
import me.earth.phobos.features.gui.alts.ias.account.*;
import com.mojang.util.*;
import net.minecraft.util.*;
import me.earth.phobos.features.gui.alts.*;
import me.earth.phobos.features.gui.alts.ias.config.*;
import java.util.*;

public class AltManager
{
    private static AltManager manager;
    private final UserAuthentication auth;
    
    private AltManager() {
        final UUID uuid = UUID.randomUUID();
        final AuthenticationService authService = (AuthenticationService)new YggdrasilAuthenticationService(Minecraft.getMinecraft().getProxy(), uuid.toString());
        this.auth = authService.createUserAuthentication(Agent.MINECRAFT);
        authService.createMinecraftSessionService();
    }
    
    public static AltManager getInstance() {
        if (AltManager.manager == null) {
            AltManager.manager = new AltManager();
        }
        return AltManager.manager;
    }
    
    public Throwable setUser(final String username, final String password) {
        Throwable throwable = null;
        if (!Minecraft.getMinecraft().getSession().getUsername().equals(EncryptionTools.decode(username)) || Minecraft.getMinecraft().getSession().getToken().equals("0")) {
            if (!Minecraft.getMinecraft().getSession().getToken().equals("0")) {
                for (final AccountData data : AltDatabase.getInstance().getAlts()) {
                    if (data.alias.equals(Minecraft.getMinecraft().getSession().getUsername()) && data.user.equals(username)) {
                        throwable = (Throwable)new AlreadyLoggedInException();
                        return throwable;
                    }
                }
            }
            this.auth.logOut();
            this.auth.setUsername(EncryptionTools.decode(username));
            this.auth.setPassword(EncryptionTools.decode(password));
            try {
                this.auth.logIn();
                final Session session = new Session(this.auth.getSelectedProfile().getName(), UUIDTypeAdapter.fromUUID(this.auth.getSelectedProfile().getId()), this.auth.getAuthenticatedToken(), this.auth.getUserType().getName());
                MR.setSession(session);
                for (int i = 0; i < AltDatabase.getInstance().getAlts().size(); ++i) {
                    final AccountData data2 = AltDatabase.getInstance().getAlts().get(i);
                    if (data2.user.equals(username) && data2.pass.equals(password)) {
                        data2.alias = session.getUsername();
                    }
                }
            }
            catch (Exception e) {
                throwable = e;
            }
        }
        else if (!ConfigValues.ENABLERELOG) {
            throwable = (Throwable)new AlreadyLoggedInException();
        }
        return throwable;
    }
    
    public void setUserOffline(final String username) {
        this.auth.logOut();
        final Session session = new Session(username, username, "0", "legacy");
        try {
            MR.setSession(session);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
