//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.manager;

import java.util.*;
import me.earth.phobos.features.notifications.*;
import me.earth.phobos.features.modules.client.*;
import me.earth.phobos.*;

public class NotificationManager
{
    private final ArrayList<Notifications> notifications;
    
    public NotificationManager() {
        this.notifications = new ArrayList<Notifications>();
    }
    
    public void handleNotifications(int posY) {
        for (int i = 0; i < this.getNotifications().size(); ++i) {
            this.getNotifications().get(i).onDraw(posY);
            posY -= ((HUD)Phobos.moduleManager.getModuleByClass((Class)HUD.class)).renderer.getFontHeight() + 5;
        }
    }
    
    public void addNotification(final String text, final long duration) {
        this.getNotifications().add(new Notifications(text, duration));
    }
    
    public ArrayList<Notifications> getNotifications() {
        return this.notifications;
    }
}
