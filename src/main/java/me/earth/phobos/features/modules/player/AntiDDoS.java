



package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import java.util.*;
import java.util.concurrent.*;
import me.earth.phobos.event.events.*;
import me.earth.phobos.*;
import me.earth.phobos.features.command.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class AntiDDoS extends Module
{
    private static AntiDDoS instance;
    public final Setting<Boolean> full;
    private final Map<String, Setting> servers;
    public Setting<String> newIP;
    public Setting<Boolean> showServer;
    
    public AntiDDoS() {
        super("AntiDDoS", "Prevents DDoS attacks via multiplayer list.", Module.Category.PLAYER, false, false, true);
        this.full = (Setting<Boolean>)this.register(new Setting("Full", (T)false));
        this.servers = new ConcurrentHashMap<String, Setting>();
        this.newIP = (Setting<String>)this.register(new Setting("NewServer", (T)"Add Server...", v -> !this.full.getValue()));
        this.showServer = (Setting<Boolean>)this.register(new Setting("ShowServers", (T)false, v -> !this.full.getValue()));
        AntiDDoS.instance = this;
    }
    
    public static AntiDDoS getInstance() {
        if (AntiDDoS.instance == null) {
            AntiDDoS.instance = new AntiDDoS();
        }
        return AntiDDoS.instance;
    }
    
    @SubscribeEvent
    public void onSettingChange(final ClientEvent event) {
        if (Phobos.configManager.loadingConfig || Phobos.configManager.savingConfig) {
            return;
        }
        if (event.getStage() == 2 && event.getSetting() != null && event.getSetting().getFeature() != null && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(this.newIP) && !this.shouldntPing(this.newIP.getPlannedValue()) && !event.getSetting().getPlannedValue().equals(event.getSetting().getDefaultValue())) {
                final Setting setting = this.register(new Setting(this.newIP.getPlannedValue(), (T)true, v -> this.showServer.getValue() && !this.full.getValue()));
                this.registerServer(setting);
                Command.sendMessage("<AntiDDoS> Added new Server: " + this.newIP.getPlannedValue());
                event.setCanceled(true);
            }
            else {
                final Setting setting = event.getSetting();
                if (setting.equals(this.enabled) || setting.equals(this.drawn) || setting.equals(this.bind) || setting.equals(this.newIP) || setting.equals(this.showServer) || setting.equals(this.full)) {
                    return;
                }
                if (setting.getValue() instanceof Boolean && !setting.getPlannedValue()) {
                    this.servers.remove(setting.getName().toLowerCase());
                    this.unregister(setting);
                    event.setCanceled(true);
                }
            }
        }
    }
    
    public void registerServer(final Setting setting) {
        this.servers.put(setting.getName().toLowerCase(), setting);
    }
    
    public boolean shouldntPing(final String ip) {
        return !this.isOff() && (this.full.getValue() || this.servers.get(ip.toLowerCase()) != null);
    }
}
