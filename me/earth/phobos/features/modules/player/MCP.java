//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import org.lwjgl.input.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import me.earth.phobos.util.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class MCP extends Module
{
    private final Setting<Mode> mode;
    private final Setting<Boolean> antiFriend;
    private final Setting<Boolean> skyonly;
    private final Setting<Boolean> skyonly2;
    private boolean clicked;
    
    public MCP() {
        super("MCP", "Throws a pearl.", Module.Category.PLAYER, false, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.MIDDLECLICK));
        this.antiFriend = (Setting<Boolean>)this.register(new Setting("AntiFriend", (T)true));
        this.skyonly = (Setting<Boolean>)this.register(new Setting("AboveHorizon", (T)false));
        this.skyonly2 = (Setting<Boolean>)this.register(new Setting("Skyonly", (T)false));
    }
    
    public void onEnable() {
        if (!fullNullCheck() && this.mode.getValue() == Mode.TOGGLE) {
            this.throwPearl();
            this.disable();
        }
    }
    
    public void onTick() {
        if (this.mode.getValue() == Mode.MIDDLECLICK) {
            if (Mouse.isButtonDown(2)) {
                if (!this.clicked) {
                    this.throwPearl();
                }
                this.clicked = true;
            }
            else {
                this.clicked = false;
            }
        }
    }
    
    private void throwPearl() {
        RayTraceResult result;
        if (this.antiFriend.getValue() && (result = MCP.mc.objectMouseOver) != null && result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit instanceof EntityPlayer) {
            return;
        }
        if (this.skyonly.getValue() && MCP.mc.player.rotationPitch > 0.0f) {
            return;
        }
        if (this.skyonly2.getValue() && (result = MCP.mc.objectMouseOver) != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
            return;
        }
        final int pearlSlot = InventoryUtil.findHotbarBlock(ItemEnderPearl.class);
        final boolean offhand = MCP.mc.player.getHeldItemOffhand().getItem() == Items.ENDER_PEARL;
        if (pearlSlot != -1 || offhand) {
            final int oldslot = MCP.mc.player.inventory.currentItem;
            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(pearlSlot, false);
            }
            MCP.mc.playerController.processRightClick((EntityPlayer)MCP.mc.player, (World)MCP.mc.world, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(oldslot, false);
            }
        }
    }
    
    public enum Mode
    {
        TOGGLE, 
        MIDDLECLICK;
    }
}
