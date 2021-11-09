



package me.earth.phobos.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.multiplayer.*;
import me.earth.phobos.features.modules.client.*;
import me.earth.phobos.mixin.mixins.accessors.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ ServerAddress.class })
public abstract class MixinServerAddress
{
    @Redirect(method = { "fromString" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ServerAddress;getServerAddress(Ljava/lang/String;)[Ljava/lang/String;"))
    private static String[] getServerAddressHook(final String ip) {
        final int port;
        if (ip.equals(PingBypass.getInstance().ip.getValue()) && (port = PingBypass.getInstance().getPort()) != -1) {
            return new String[] { (String)PingBypass.getInstance().ip.getValue(), Integer.toString(port) };
        }
        return IServerAddress.getServerAddress(ip);
    }
}
