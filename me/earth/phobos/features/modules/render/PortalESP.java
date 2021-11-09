//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.modules.render;

import me.earth.phobos.features.modules.*;
import net.minecraft.util.math.*;
import me.earth.phobos.features.setting.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.earth.phobos.event.events.*;
import java.awt.*;
import me.earth.phobos.util.*;
import java.util.*;
import net.minecraft.block.*;

public class PortalESP extends Module
{
    private final ArrayList<BlockPos> blockPosArrayList;
    private final Setting<Integer> distance;
    private final Setting<Boolean> box;
    private final Setting<Integer> boxAlpha;
    private final Setting<Boolean> outline;
    private final Setting<Float> lineWidth;
    private int cooldownTicks;
    
    public PortalESP() {
        super("PortalESP", "Draws esp over portals.", Module.Category.RENDER, true, false, false);
        this.blockPosArrayList = new ArrayList<BlockPos>();
        this.distance = (Setting<Integer>)this.register(new Setting("Distance", (T)256, (T)1, (T)256));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", (T)false));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", (T)125, (T)0, (T)255, v -> this.box.getValue()));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", (T)true));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (T)1.0f, (T)0.1f, (T)5.0f, v -> this.outline.getValue()));
    }
    
    @SubscribeEvent
    public void onTickEvent(final TickEvent.ClientTickEvent event) {
        if (PortalESP.mc.world == null) {
            return;
        }
        if (this.cooldownTicks < 1) {
            this.blockPosArrayList.clear();
            this.compileDL();
            this.cooldownTicks = 80;
        }
        --this.cooldownTicks;
    }
    
    public void onRender3D(final Render3DEvent event) {
        if (PortalESP.mc.world == null) {
            return;
        }
        for (final BlockPos pos : this.blockPosArrayList) {
            RenderUtil.drawBoxESP(pos, new Color(204, 0, 153, 255), false, new Color(204, 0, 153, 255), this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
        }
    }
    
    private void compileDL() {
        if (PortalESP.mc.world == null || PortalESP.mc.player == null) {
            return;
        }
        for (int x = (int)PortalESP.mc.player.posX - this.distance.getValue(); x <= (int)PortalESP.mc.player.posX + this.distance.getValue(); ++x) {
            for (int y = (int)PortalESP.mc.player.posY - this.distance.getValue(); y <= (int)PortalESP.mc.player.posY + this.distance.getValue(); ++y) {
                for (int z = (int)Math.max(PortalESP.mc.player.posZ - this.distance.getValue(), 1.0); z <= Math.min(PortalESP.mc.player.posZ + this.distance.getValue(), 256.0); ++z) {
                    final BlockPos pos = new BlockPos(x, y, z);
                    final Block block = PortalESP.mc.world.getBlockState(pos).getBlock();
                    if (block instanceof BlockPortal) {
                        this.blockPosArrayList.add(pos);
                    }
                }
            }
        }
    }
}
