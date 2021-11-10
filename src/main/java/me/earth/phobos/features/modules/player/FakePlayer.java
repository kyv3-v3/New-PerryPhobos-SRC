



package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.*;
import net.minecraft.client.entity.*;
import me.earth.phobos.features.setting.*;
import com.mojang.authlib.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import me.earth.phobos.features.modules.client.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import java.util.*;
import me.earth.phobos.*;
import net.minecraft.potion.*;

public class FakePlayer extends Module
{
    public static final String[][] phobosInfo;
    private static final String[] fitInfo;
    private static FakePlayer INSTANCE;
    private final List<EntityOtherPlayerMP> fakeEntities;
    private final Setting<Boolean> copyInv;
    public Setting<Boolean> multi;
    private final Setting<Integer> players;
    public Setting<Boolean> moving;
    public Setting<Integer> motion;
    public List<Integer> fakePlayerIdList;
    
    public FakePlayer() {
        super("FakePlayer",  "Spawns in a fake player for testing stuff.",  Module.Category.PLAYER,  true,  false,  false);
        this.fakeEntities = new ArrayList<EntityOtherPlayerMP>();
        this.copyInv = (Setting<Boolean>)this.register(new Setting("CopyInv", true));
        this.multi = (Setting<Boolean>)this.register(new Setting("Multi", false));
        this.players = (Setting<Integer>)this.register(new Setting("Players", 1, 1, 9,  v -> this.multi.getValue(),  "Amount of other players."));
        this.moving = (Setting<Boolean>)this.register(new Setting("Moving", false));
        this.motion = (Setting<Integer>)this.register(new Setting("Motion", 2, (-10), 10,  v -> this.moving.getValue()));
        this.fakePlayerIdList = new ArrayList<Integer>();
        this.setInstance();
    }
    
    public static FakePlayer getInstance() {
        if (FakePlayer.INSTANCE == null) {
            FakePlayer.INSTANCE = new FakePlayer();
        }
        return FakePlayer.INSTANCE;
    }
    
    private void setInstance() {
        FakePlayer.INSTANCE = this;
    }
    
    public void onUpdate() {
        if (this.moving.getValue()) {
            if (fullNullCheck()) {
                return;
            }
            final GameProfile profile = new GameProfile(UUID.fromString("fdee323e-7f0c-4c15-8d1c-0f277442342a"),  "Fit");
            final EntityOtherPlayerMP player = new EntityOtherPlayerMP((World)FakePlayer.mc.world,  profile);
            player.setLocationAndAngles(FakePlayer.mc.player.posX + player.motionX + this.motion.getValue(),  FakePlayer.mc.player.posY + player.motionY,  FakePlayer.mc.player.posZ + player.motionZ + this.motion.getValue(),  FakePlayer.mc.player.cameraYaw,  FakePlayer.mc.player.cameraPitch);
            player.rotationYawHead = FakePlayer.mc.player.rotationYawHead;
            if (this.copyInv.getValue()) {
                player.inventory.copyInventory(FakePlayer.mc.player.inventory);
            }
            FakePlayer.mc.world.addEntityToWorld(-69420,  (Entity)player);
        }
    }
    
    public void onLoad() {
        this.disable();
    }
    
    public void onEnable() {
        if (!this.moving.getValue()) {
            if (fullNullCheck()) {
                this.disable();
                return;
            }
            if (PingBypass.getInstance().isConnected()) {
                FakePlayer.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
                FakePlayer.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module FakePlayer set Enabled true"));
            }
            this.fakePlayerIdList = new ArrayList<Integer>();
            if (this.multi.getValue()) {
                int amount = 0;
                int entityId = -101;
                for (final String[] data : FakePlayer.phobosInfo) {
                    this.addFakePlayer(data[0],  data[1],  entityId,  Integer.parseInt(data[2]),  Integer.parseInt(data[3]));
                    if (++amount >= this.players.getValue()) {
                        return;
                    }
                    entityId -= amount;
                }
            }
            else {
                this.addFakePlayer(FakePlayer.fitInfo[0],  FakePlayer.fitInfo[1],  -100,  0,  0);
            }
        }
    }
    
    public void onDisable() {
        if (!this.moving.getValue()) {
            if (fullNullCheck()) {
                return;
            }
            if (PingBypass.getInstance().isConnected()) {
                FakePlayer.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
                FakePlayer.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module FakePlayer set Enabled false"));
            }
            for (final int id : this.fakePlayerIdList) {
                FakePlayer.mc.world.removeEntityFromWorld(id);
            }
        }
        if (this.moving.getValue()) {
            if (fullNullCheck()) {
                return;
            }
            FakePlayer.mc.world.removeEntityFromWorld(-69420);
        }
    }
    
    public void onLogout() {
        if (this.isOn()) {
            this.disable();
        }
    }
    
    private void addFakePlayer(final String uuid,  final String name,  final int entityId,  final int offsetX,  final int offsetZ) {
        if (!this.moving.getValue()) {
            final GameProfile profile = new GameProfile(UUID.fromString(uuid),  name);
            final EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP((World)FakePlayer.mc.world,  profile);
            fakePlayer.copyLocationAndAnglesFrom((Entity)FakePlayer.mc.player);
            final EntityOtherPlayerMP entityOtherPlayerMP = fakePlayer;
            entityOtherPlayerMP.posX += offsetX;
            final EntityOtherPlayerMP entityOtherPlayerMP2 = fakePlayer;
            entityOtherPlayerMP2.posZ += offsetZ;
            if (this.copyInv.getValue()) {
                for (final PotionEffect potionEffect : Phobos.potionManager.getOwnPotions()) {
                    fakePlayer.addPotionEffect(potionEffect);
                }
                fakePlayer.inventory.copyInventory(FakePlayer.mc.player.inventory);
            }
            fakePlayer.setHealth(FakePlayer.mc.player.getHealth() + FakePlayer.mc.player.getAbsorptionAmount());
            this.fakeEntities.add(fakePlayer);
            FakePlayer.mc.world.addEntityToWorld(entityId,  (Entity)fakePlayer);
            this.fakePlayerIdList.add(entityId);
        }
    }
    
    static {
        phobosInfo = new String[][] { { "8af022c8-b926-41a0-8b79-2b544ff00fcf",  "3arthqu4ke",  "3",  "0" },  { "0aa3b04f-786a-49c8-bea9-025ee0dd1e85",  "zb0b",  "-3",  "0" },  { "19bf3f1f-fe06-4c86-bea5-3dad5df89714",  "3vt",  "0",  "-3" },  { "e47d6571-99c2-415b-955e-c4bc7b55941b",  "Phobos_eu",  "0",  "3" },  { "b01f9bc1-cb7c-429a-b178-93d771f00926",  "bakpotatisen",  "6",  "0" },  { "b232930c-c28a-4e10-8c90-f152235a65c5",  "948",  "-6",  "0" },  { "ace08461-3db3-4579-98d3-390a67d5645b",  "Browswer",  "0",  "-6" },  { "5bead5b0-3bab-460d-af1d-7929950f40c2",  "fsck",  "0",  "6" },  { "78ee2bd6-64c4-45f0-96e5-0b6747ba7382",  "Fit",  "0",  "9" },  { "78ee2bd6-64c4-45f0-96e5-0b6747ba7382",  "deathcurz0",  "0",  "-9" } };
        fitInfo = new String[] { "fdee323e-7f0c-4c15-8d1c-0f277442342a",  "Fit" };
        FakePlayer.INSTANCE = new FakePlayer();
    }
}
