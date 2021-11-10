



package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.util.*;
import me.earth.phobos.features.command.*;
import me.earth.phobos.event.events.*;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class NoRotate extends Module
{
    private final Setting<Integer> waitDelay;
    private final TimerUtil timer;
    private boolean cancelPackets;
    private boolean timerReset;
    
    public NoRotate() {
        super("NoRotate",  "Dangerous to use might desync you.",  Category.MISC,  true,  false,  false);
        this.waitDelay = (Setting<Integer>)this.register(new Setting("Delay", 2500, 0, 10000));
        this.timer = new TimerUtil();
        this.cancelPackets = true;
    }
    
    @Override
    public void onLogout() {
        this.cancelPackets = false;
    }
    
    @Override
    public void onLogin() {
        this.timer.reset();
        this.timerReset = true;
    }
    
    @Override
    public void onUpdate() {
        if (this.timerReset && !this.cancelPackets && this.timer.passedMs(this.waitDelay.getValue())) {
            Command.sendMessage("<NoRotate> §cThis module might desync you!");
            this.cancelPackets = true;
            this.timerReset = false;
        }
    }
    
    @Override
    public void onEnable() {
        Command.sendMessage("<NoRotate> §cThis module might desync you!");
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getStage() == 0 && this.cancelPackets && event.getPacket() instanceof SPacketPlayerPosLook) {
            final SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
            packet.yaw = NoRotate.mc.player.rotationYaw;
            packet.pitch = NoRotate.mc.player.rotationPitch;
        }
    }
}
