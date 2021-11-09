



package me.earth.phobos.features.modules.render;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.item.*;
import java.util.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.earth.phobos.event.events.*;
import net.minecraft.client.renderer.*;
import me.earth.phobos.util.*;
import org.lwjgl.opengl.*;

public class Trails extends Module
{
    private final Setting<Float> lineWidth;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    private final Map<Entity, List<Vec3d>> renderMap;
    
    public Trails() {
        super("Trails", "Draws trails on projectiles.", Module.Category.RENDER, true, false, false);
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (T)1.5f, (T)0.1f, (T)5.0f));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)0, (T)0, (T)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)255, (T)0, (T)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)0, (T)0, (T)255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)255, (T)0, (T)255));
        this.renderMap = new HashMap<Entity, List<Vec3d>>();
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        for (final Entity entity : Trails.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderPearl)) {
                continue;
            }
            final Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, Trails.mc.getRenderPartialTicks());
            final List<Vec3d> vectors = (this.renderMap.get(entity) != null) ? this.renderMap.get(entity) : new ArrayList<Vec3d>();
            vectors.add(new Vec3d(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z));
            this.renderMap.put(entity, vectors);
        }
    }
    
    public void onRender3D(final Render3DEvent event) {
        for (final Entity entity : Trails.mc.world.loadedEntityList) {
            if (!this.renderMap.containsKey(entity)) {
                continue;
            }
            GlStateManager.pushMatrix();
            RenderUtil.GLPre(this.lineWidth.getValue());
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GL11.glColor4f(this.red.getValue() / 255.0f, this.green.getValue() / 255.0f, this.blue.getValue() / 255.0f, this.alpha.getValue() / 255.0f);
            GL11.glLineWidth((float)this.lineWidth.getValue());
            GL11.glBegin(1);
            for (int i = 0; i < this.renderMap.get(entity).size() - 1; ++i) {
                GL11.glVertex3d(this.renderMap.get(entity).get(i).x, this.renderMap.get(entity).get(i).y, this.renderMap.get(entity).get(i).z);
                GL11.glVertex3d(this.renderMap.get(entity).get(i + 1).x, this.renderMap.get(entity).get(i + 1).y, this.renderMap.get(entity).get(i + 1).z);
            }
            GL11.glEnd();
            GlStateManager.resetColor();
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            RenderUtil.GlPost();
            GlStateManager.popMatrix();
        }
    }
}
