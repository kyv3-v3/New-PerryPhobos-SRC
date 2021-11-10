



package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraftforge.fml.common.gameevent.*;
import org.lwjgl.input.*;
import me.earth.phobos.features.gui.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.player.*;
import me.earth.phobos.*;
import me.earth.phobos.features.command.*;
import me.earth.phobos.features.modules.client.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.entity.*;

public class MCF extends Module
{
    private final Setting<Boolean> middleClick;
    private final Setting<Boolean> keyboard;
    private final Setting<Boolean> server;
    private final Setting<Boolean> sendmsg;
    private final Setting<Bind> key;
    private boolean clicked;
    
    public MCF() {
        super("MCF",  "Middleclick Friends.",  Category.MISC,  true,  false,  false);
        this.middleClick = (Setting<Boolean>)this.register(new Setting("MiddleClick", true));
        this.keyboard = (Setting<Boolean>)this.register(new Setting("Keyboard", false));
        this.server = (Setting<Boolean>)this.register(new Setting("Server", false));
        this.sendmsg = (Setting<Boolean>)this.register(new Setting("Send Msgs", false));
        this.key = (Setting<Bind>)this.register(new Setting("KeyBind", new Bind(-1),  v -> this.keyboard.getValue()));
    }
    
    @Override
    public void onUpdate() {
        if (Mouse.isButtonDown(2)) {
            if (!this.clicked && this.middleClick.getValue() && MCF.mc.currentScreen == null) {
                this.onClick();
            }
            this.clicked = true;
        }
        else {
            this.clicked = false;
        }
    }
    
    @SubscribeEvent(priority = EventPriority.NORMAL,  receiveCanceled = true)
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (this.keyboard.getValue() && Keyboard.getEventKeyState() && !(MCF.mc.currentScreen instanceof PhobosGui) && this.key.getValue().getKey() == Keyboard.getEventKey()) {
            this.onClick();
        }
    }
    
    private void onClick() {
        final RayTraceResult result = MCF.mc.objectMouseOver;
        final Entity entity;
        if (result != null && result.typeOfHit == RayTraceResult.Type.ENTITY && (entity = result.entityHit) instanceof EntityPlayer) {
            if (Phobos.friendManager.isFriend(entity.getName())) {
                Phobos.friendManager.removeFriend(entity.getName());
                Command.sendMessage("§c" + entity.getName() + "§r unfriended.");
                if (this.sendmsg.getValue()) {
                    MCF.mc.player.sendChatMessage("/msg " + entity.getName() + " I have unadded u on Perry Phobos.");
                }
                if (this.server.getValue() && PingBypass.getInstance().isConnected()) {
                    MCF.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
                    MCF.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "friend del " + entity.getName()));
                }
            }
            else {
                Phobos.friendManager.addFriend(entity.getName());
                Command.sendMessage("§b" + entity.getName() + "§r friended.");
                if (this.sendmsg.getValue()) {
                    MCF.mc.player.sendChatMessage("/msg " + entity.getName() + " I have added u on Perry Phobos.");
                }
                if (this.server.getValue() && PingBypass.getInstance().isConnected()) {
                    MCF.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
                    MCF.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "friend add " + entity.getName()));
                }
            }
        }
        this.clicked = true;
    }
}
