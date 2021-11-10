



package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.*;
import java.util.*;
import net.minecraft.network.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.client.entity.*;
import net.minecraft.util.math.*;
import java.util.concurrent.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import me.earth.phobos.util.*;
import me.earth.phobos.event.events.*;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class Blink extends Module
{
    private static Blink INSTANCE;
    private final TimerUtil timer;
    private final Queue<Packet<?>> packets;
    public Setting<Boolean> cPacketPlayer;
    public Setting<Mode> autoOff;
    public Setting<Integer> timeLimit;
    public Setting<Integer> packetLimit;
    public Setting<Float> distance;
    private EntityOtherPlayerMP entity;
    private int packetsCanceled;
    private BlockPos startPos;
    
    public Blink() {
        super("Blink",  "Fakelag.",  Module.Category.PLAYER,  true,  false,  false);
        this.timer = new TimerUtil();
        this.packets = new ConcurrentLinkedQueue<Packet<?>>();
        this.cPacketPlayer = (Setting<Boolean>)this.register(new Setting("CPacketPlayer", true));
        this.autoOff = (Setting<Mode>)this.register(new Setting("AutoOff", Mode.MANUAL));
        this.timeLimit = (Setting<Integer>)this.register(new Setting("Time", 20, 1, 500,  v -> this.autoOff.getValue() == Mode.TIME));
        this.packetLimit = (Setting<Integer>)this.register(new Setting("Packets", 20, 1, 500,  v -> this.autoOff.getValue() == Mode.PACKETS));
        this.distance = (Setting<Float>)this.register(new Setting("Distance", 10.0f, 1.0f, 100.0f,  v -> this.autoOff.getValue() == Mode.DISTANCE));
        this.setInstance();
    }
    
    public static Blink getInstance() {
        if (Blink.INSTANCE == null) {
            Blink.INSTANCE = new Blink();
        }
        return Blink.INSTANCE;
    }
    
    private void setInstance() {
        Blink.INSTANCE = this;
    }
    
    public void onEnable() {
        if (!fullNullCheck()) {
            (this.entity = new EntityOtherPlayerMP((World)Blink.mc.world,  Blink.mc.session.getProfile())).copyLocationAndAnglesFrom((Entity)Blink.mc.player);
            this.entity.rotationYaw = Blink.mc.player.rotationYaw;
            this.entity.rotationYawHead = Blink.mc.player.rotationYawHead;
            this.entity.inventory.copyInventory(Blink.mc.player.inventory);
            Blink.mc.world.addEntityToWorld(6942069,  (Entity)this.entity);
            this.startPos = Blink.mc.player.getPosition();
        }
        else {
            this.disable();
        }
        this.packetsCanceled = 0;
        this.timer.reset();
    }
    
    public void onUpdate() {
        if (nullCheck() || (this.autoOff.getValue() == Mode.TIME && this.timer.passedS(this.timeLimit.getValue())) || (this.autoOff.getValue() == Mode.DISTANCE && this.startPos != null && Blink.mc.player.getDistanceSq(this.startPos) >= MathUtil.square(this.distance.getValue())) || (this.autoOff.getValue() == Mode.PACKETS && this.packetsCanceled >= this.packetLimit.getValue())) {
            this.disable();
        }
    }
    
    public void onLogout() {
        if (this.isOn()) {
            this.disable();
        }
    }
    
    @SubscribeEvent
    public void onSendPacket(final PacketEvent.Send event) {
        if (event.getStage() == 0 && Blink.mc.world != null && !Blink.mc.isSingleplayer()) {
            final Object packet = event.getPacket();
            if (this.cPacketPlayer.getValue() && packet instanceof CPacketPlayer) {
                event.setCanceled(true);
                this.packets.add((Packet<?>)packet);
                ++this.packetsCanceled;
            }
            if (!this.cPacketPlayer.getValue()) {
                if (packet instanceof CPacketChatMessage || packet instanceof CPacketKeepAlive || packet instanceof CPacketTabComplete || packet instanceof CPacketClientStatus || packet instanceof CPacketHeldItemChange || packet instanceof CPacketPlayerTryUseItem || packet instanceof CPacketPlayerTryUseItemOnBlock) {
                    return;
                }
                this.packets.add((Packet<?>)packet);
                event.setCanceled(true);
                ++this.packetsCanceled;
            }
        }
    }
    
    public void onDisable() {
        if (!fullNullCheck()) {
            Blink.mc.world.removeEntity((Entity)this.entity);
            while (!this.packets.isEmpty()) {
                Blink.mc.player.connection.sendPacket((Packet)this.packets.poll());
            }
        }
        this.startPos = null;
    }
    
    static {
        Blink.INSTANCE = new Blink();
    }
    
    public enum Mode
    {
        MANUAL,  
        TIME,  
        DISTANCE,  
        PACKETS;
    }
}
