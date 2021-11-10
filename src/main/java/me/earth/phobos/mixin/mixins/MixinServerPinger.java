



package me.earth.phobos.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.network.*;
import net.minecraft.client.multiplayer.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.earth.phobos.features.modules.client.*;
import me.earth.phobos.features.modules.player.*;
import me.earth.phobos.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ ServerPinger.class })
public class MixinServerPinger
{
    @Inject(method = { "ping" },  at = { @At("HEAD") },  cancellable = true)
    public void pingHook(final ServerData server,  final CallbackInfo info) {
        if (server.serverIP.equalsIgnoreCase((String)PingBypass.getInstance().ip.getValue())) {
            info.cancel();
        }
        else if (AntiDDoS.getInstance().shouldntPing(server.serverIP)) {
            Phobos.LOGGER.info("AntiDDoS preventing Ping to: " + server.serverIP);
            info.cancel();
        }
    }
    
    @Inject(method = { "tryCompatibilityPing" },  at = { @At("HEAD") },  cancellable = true)
    public void tryCompatibilityPingHook(final ServerData server,  final CallbackInfo info) {
        if (server.serverIP.equalsIgnoreCase((String)PingBypass.getInstance().ip.getValue())) {
            info.cancel();
        }
        else if (AntiDDoS.getInstance().shouldntPing(server.serverIP)) {
            Phobos.LOGGER.info("AntiDDoS preventing Compatibility Ping to: " + server.serverIP);
            info.cancel();
        }
    }
}
