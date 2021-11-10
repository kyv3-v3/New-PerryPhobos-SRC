



package me.earth.phobos.features.modules.render;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.event.events.*;
import net.minecraft.client.renderer.*;
import java.awt.*;
import org.lwjgl.opengl.*;
import me.earth.phobos.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import me.earth.phobos.util.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.math.*;
import java.util.*;

public class Ranges extends Module
{
    private final Setting<Boolean> hitSpheres;
    private final Setting<Boolean> circle;
    private final Setting<Boolean> ownSphere;
    private final Setting<Boolean> raytrace;
    private final Setting<Float> lineWidth;
    private final Setting<Double> radius;
    
    public Ranges() {
        super("Ranges", "Draws a circle around the player.", Module.Category.RENDER, false, false, false);
        this.hitSpheres = (Setting<Boolean>)this.register(new Setting("HitSpheres", (T)false));
        this.circle = (Setting<Boolean>)this.register(new Setting("Circle", (T)true));
        this.ownSphere = (Setting<Boolean>)this.register(new Setting("OwnSphere", (T)false, v -> this.hitSpheres.getValue()));
        this.raytrace = (Setting<Boolean>)this.register(new Setting("RayTrace", (T)false, v -> this.circle.getValue()));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (T)1.5f, (T)0.1f, (T)5.0f));
        this.radius = (Setting<Double>)this.register(new Setting("Radius", (T)4.5, (T)0.1, (T)8.0));
    }
    
    public void onUpdate() {
    }
    
    public void onRender3D(final Render3DEvent event) {
        if (this.circle.getValue()) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.enableDepth();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            final RenderManager renderManager = Ranges.mc.getRenderManager();
            float hue = System.currentTimeMillis() % 7200L / 7200.0f;
            Color color = new Color(Color.HSBtoRGB(hue, 1.0f, 1.0f));
            final ArrayList<Vec3d> hVectors = new ArrayList<Vec3d>();
            final double x = Ranges.mc.player.lastTickPosX + (Ranges.mc.player.posX - Ranges.mc.player.lastTickPosX) * event.getPartialTicks() - renderManager.renderPosX;
            final double y = Ranges.mc.player.lastTickPosY + (Ranges.mc.player.posY - Ranges.mc.player.lastTickPosY) * event.getPartialTicks() - renderManager.renderPosY;
            final double z = Ranges.mc.player.lastTickPosZ + (Ranges.mc.player.posZ - Ranges.mc.player.lastTickPosZ) * event.getPartialTicks() - renderManager.renderPosZ;
            GL11.glLineWidth((float)this.lineWidth.getValue());
            GL11.glBegin(1);
            for (int i = 0; i <= 360; ++i) {
                final Vec3d vec = new Vec3d(x + Math.sin(i * 3.141592653589793 / 180.0) * this.radius.getValue(), y + 0.1, z + Math.cos(i * 3.141592653589793 / 180.0) * this.radius.getValue());
                final RayTraceResult result = Ranges.mc.world.rayTraceBlocks(new Vec3d(Ranges.mc.player.posX, Ranges.mc.player.posY + Ranges.mc.player.getEyeHeight(), Ranges.mc.player.posZ), vec, false, false, true);
                if (result != null && this.raytrace.getValue()) {
                    Phobos.LOGGER.info("raytrace was not null");
                    hVectors.add(result.hitVec);
                }
                else {
                    hVectors.add(vec);
                }
            }
            for (int j = 0; j < hVectors.size() - 1; ++j) {
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
                GL11.glVertex3d(hVectors.get(j).x, hVectors.get(j).y, hVectors.get(j).z);
                GL11.glVertex3d(hVectors.get(j + 1).x, hVectors.get(j + 1).y, hVectors.get(j + 1).z);
                color = new Color(Color.HSBtoRGB(hue += 0.0027777778f, 1.0f, 1.0f));
            }
            GL11.glEnd();
            GlStateManager.resetColor();
            GlStateManager.disableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
        if (this.hitSpheres.getValue()) {
            for (final EntityPlayer player : Ranges.mc.world.playerEntities) {
                if (player != null) {
                    if (player.equals((Object)Ranges.mc.player) && !this.ownSphere.getValue()) {
                        continue;
                    }
                    final Vec3d interpolated = EntityUtil.interpolateEntity((Entity)player, event.getPartialTicks());
                    if (Phobos.friendManager.isFriend(player.getName())) {
                        GL11.glColor4f(0.15f, 0.15f, 1.0f, 1.0f);
                    }
                    else if (Ranges.mc.player.getDistance((Entity)player) >= 64.0f) {
                        GL11.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
                    }
                    else {
                        GL11.glColor4f(1.0f, Ranges.mc.player.getDistance((Entity)player) / 150.0f, 0.0f, 1.0f);
                    }
                    RenderUtil.drawSphere(interpolated.x, interpolated.y, interpolated.z, this.radius.getValue().floatValue(), 20, 15);
                }
            }
        }
    }
}
