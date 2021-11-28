/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ServerData
 *  net.minecraft.client.network.ServerPinger
 */
package me.earth.phobos.mixin.mixins;

import me.earth.phobos.Phobos;
import me.earth.phobos.features.modules.client.PingBypass;
import me.earth.phobos.features.modules.player.AntiDDoS;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.ServerPinger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ServerPinger.class})
public class MixinServerPinger {
    @Inject(method={"ping"}, at={@At(value="HEAD")}, cancellable=true)
    public void pingHook(ServerData server, CallbackInfo info) {
        if (server.field_78845_b.equalsIgnoreCase(PingBypass.getInstance().ip.getValue())) {
            info.cancel();
        } else if (AntiDDoS.getInstance().shouldntPing(server.field_78845_b)) {
            Phobos.LOGGER.info("AntiDDoS preventing Ping to: " + server.field_78845_b);
            info.cancel();
        }
    }

    @Inject(method={"tryCompatibilityPing"}, at={@At(value="HEAD")}, cancellable=true)
    public void tryCompatibilityPingHook(ServerData server, CallbackInfo info) {
        if (server.field_78845_b.equalsIgnoreCase(PingBypass.getInstance().ip.getValue())) {
            info.cancel();
        } else if (AntiDDoS.getInstance().shouldntPing(server.field_78845_b)) {
            Phobos.LOGGER.info("AntiDDoS preventing Compatibility Ping to: " + server.field_78845_b);
            info.cancel();
        }
    }
}

