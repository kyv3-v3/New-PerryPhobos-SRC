//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.modules.render;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.*;
import org.lwjgl.opengl.*;
import net.minecraft.entity.*;
import me.earth.phobos.util.*;
import me.earth.phobos.features.modules.client.*;
import net.minecraft.client.renderer.*;
import java.awt.*;
import net.minecraft.entity.item.*;
import java.util.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.math.*;
import org.lwjgl.util.glu.*;
import me.earth.phobos.event.events.*;
import me.earth.phobos.*;
import net.minecraft.init.*;

public class ESP extends Module
{
    private static ESP INSTANCE;
    private final Setting<Mode> mode;
    private final Setting<Boolean> colorSync;
    private final Setting<Boolean> players;
    private final Setting<Boolean> animals;
    private final Setting<Boolean> mobs;
    private final Setting<Boolean> items;
    private final Setting<Boolean> xporbs;
    private final Setting<Boolean> xpbottles;
    private final Setting<Boolean> pearl;
    private final Setting<Boolean> penis;
    private final Setting<Integer> spin;
    private final Setting<Integer> cumSize;
    private final Setting<Boolean> burrow;
    private final Setting<Boolean> name;
    private final Setting<Boolean> box;
    private final Setting<Integer> boxRed;
    private final Setting<Integer> boxGreen;
    private final Setting<Integer> boxBlue;
    private final Setting<Integer> burrowAlpha;
    private final Setting<Boolean> outline;
    private final Setting<Float> outlineWidth;
    private final Setting<Boolean> cOutline;
    private final Setting<Integer> outlineRed;
    private final Setting<Integer> outlineGreen;
    private final Setting<Integer> outlineBlue;
    private final Setting<Integer> outlineAlpha;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> boxAlpha;
    private final Setting<Integer> alpha;
    private final Setting<Float> lineWidth;
    private final Setting<Boolean> colorFriends;
    private final Setting<Boolean> self;
    private final Setting<Boolean> onTop;
    private final Setting<Boolean> invisibles;
    private final Map<EntityPlayer, BlockPos> burrowedPlayers;
    
    public ESP() {
        super("ESP", "Renders a nice ESP.", Module.Category.RENDER, false, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.OUTLINE));
        this.colorSync = (Setting<Boolean>)this.register(new Setting("Sync", (T)false));
        this.players = (Setting<Boolean>)this.register(new Setting("Players", (T)true));
        this.animals = (Setting<Boolean>)this.register(new Setting("Animals", (T)false));
        this.mobs = (Setting<Boolean>)this.register(new Setting("Mobs", (T)false));
        this.items = (Setting<Boolean>)this.register(new Setting("Items", (T)false));
        this.xporbs = (Setting<Boolean>)this.register(new Setting("XpOrbs", (T)false));
        this.xpbottles = (Setting<Boolean>)this.register(new Setting("XpBottles", (T)false));
        this.pearl = (Setting<Boolean>)this.register(new Setting("Pearls", (T)false));
        this.penis = (Setting<Boolean>)this.register(new Setting("Penis", (T)false));
        this.spin = (Setting<Integer>)this.register(new Setting("PSpin", (T)3, (T)0, (T)6, v -> this.penis.getValue()));
        this.cumSize = (Setting<Integer>)this.register(new Setting("PSize", (T)3, (T)0, (T)6, v -> this.penis.getValue()));
        this.burrow = (Setting<Boolean>)this.register(new Setting("Burrow", (T)false));
        this.name = (Setting<Boolean>)this.register(new Setting("Name", (T)true, v -> this.burrow.getValue()));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", (T)true, v -> this.burrow.getValue()));
        this.boxRed = (Setting<Integer>)this.register(new Setting("BoxRed", (T)0, (T)0, (T)255, v -> this.box.getValue() && this.burrow.getValue()));
        this.boxGreen = (Setting<Integer>)this.register(new Setting("BoxGreen", (T)0, (T)0, (T)255, v -> this.box.getValue() && this.burrow.getValue()));
        this.boxBlue = (Setting<Integer>)this.register(new Setting("BoxBlue", (T)255, (T)0, (T)255, v -> this.box.getValue() && this.burrow.getValue()));
        this.burrowAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", (T)125, (T)0, (T)255, v -> this.box.getValue() && this.burrow.getValue()));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", (T)true, v -> this.burrow.getValue()));
        this.outlineWidth = (Setting<Float>)this.register(new Setting("OutlineWidth", (T)1.0f, (T)0.0f, (T)5.0f, v -> this.outline.getValue() && this.burrow.getValue()));
        this.cOutline = (Setting<Boolean>)this.register(new Setting("CustomOutline", (T)true, v -> this.outline.getValue() && this.burrow.getValue()));
        this.outlineRed = (Setting<Integer>)this.register(new Setting("OutlineRed", (T)0, (T)0, (T)255, v -> this.outline.getValue() && this.cOutline.getValue() && this.burrow.getValue()));
        this.outlineGreen = (Setting<Integer>)this.register(new Setting("OutlineGreen", (T)0, (T)0, (T)255, v -> this.outline.getValue() && this.cOutline.getValue() && this.burrow.getValue()));
        this.outlineBlue = (Setting<Integer>)this.register(new Setting("OutlineBlue", (T)255, (T)0, (T)255, v -> this.outline.getValue() && this.cOutline.getValue() && this.burrow.getValue()));
        this.outlineAlpha = (Setting<Integer>)this.register(new Setting("OutlineAlpha", (T)255, (T)0, (T)255, v -> this.outline.getValue() && this.cOutline.getValue() && this.burrow.getValue()));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)255, (T)0, (T)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)255, (T)0, (T)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)255, (T)0, (T)255));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", (T)120, (T)0, (T)255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)255, (T)0, (T)255));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (T)2.0f, (T)0.1f, (T)5.0f));
        this.colorFriends = (Setting<Boolean>)this.register(new Setting("Friends", (T)true));
        this.self = (Setting<Boolean>)this.register(new Setting("Self", (T)true));
        this.onTop = (Setting<Boolean>)this.register(new Setting("onTop", (T)true));
        this.invisibles = (Setting<Boolean>)this.register(new Setting("Invisibles", (T)false));
        this.burrowedPlayers = new HashMap<EntityPlayer, BlockPos>();
        this.setInstance();
    }
    
    public static ESP getInstance() {
        if (ESP.INSTANCE == null) {
            ESP.INSTANCE = new ESP();
        }
        return ESP.INSTANCE;
    }
    
    private void setInstance() {
        ESP.INSTANCE = this;
    }
    
    public void onRender3D(final Render3DEvent event) {
        if (this.penis.getValue()) {
            for (final Object e : ESP.mc.world.loadedEntityList) {
                if (!(e instanceof EntityPlayer)) {
                    continue;
                }
                final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
                final EntityPlayer entityPlayer = (EntityPlayer)e;
                final double d = entityPlayer.lastTickPosX + (entityPlayer.posX - entityPlayer.lastTickPosX) * ESP.mc.timer.renderPartialTicks;
                ESP.mc.getRenderManager();
                final double d2 = d - renderManager.renderPosX;
                final double d3 = entityPlayer.lastTickPosY + (entityPlayer.posY - entityPlayer.lastTickPosY) * ESP.mc.timer.renderPartialTicks;
                ESP.mc.getRenderManager();
                final double d4 = d3 - renderManager.renderPosY;
                final double d5 = entityPlayer.lastTickPosZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) * ESP.mc.timer.renderPartialTicks;
                ESP.mc.getRenderManager();
                final double d6 = d5 - renderManager.renderPosZ;
                GL11.glPushMatrix();
                RenderHelper.disableStandardItemLighting();
                this.esp(entityPlayer, d2, d4, d6);
                RenderHelper.enableStandardItemLighting();
                GL11.glPopMatrix();
            }
        }
        if (this.burrow.getValue() && !this.burrowedPlayers.isEmpty()) {
            this.burrowedPlayers.forEach((key, value) -> {
                this.renderBurrowedBlock(value);
                if (this.name.getValue()) {
                    RenderUtil.drawText(new AxisAlignedBB(value), key.getGameProfile().getName());
                }
                return;
            });
        }
        if (this.items.getValue()) {
            int i = 0;
            for (final Entity entity : ESP.mc.world.loadedEntityList) {
                if (entity instanceof EntityItem) {
                    if (ESP.mc.player.getDistanceSq(entity) >= 2500.0) {
                        continue;
                    }
                    final Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, ESP.mc.getRenderPartialTicks());
                    final AxisAlignedBB bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interp.z);
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.renderFilledBox(bb, ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getRed() / 255.0f) : (this.red.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getGreen() / 255.0f) : (this.green.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getBlue() / 255.0f) : (this.blue.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? ((float)Colors.INSTANCE.getCurrentColor().getAlpha()) : (this.boxAlpha.getValue() / 255.0f));
                    GL11.glDisable(2848);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepth();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    RenderUtil.drawBlockOutline(bb, ((boolean)this.colorSync.getValue()) ? Colors.INSTANCE.getCurrentColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0f);
                    if (++i < 50) {
                        continue;
                    }
                    break;
                }
            }
        }
        if (this.xporbs.getValue()) {
            int i = 0;
            for (final Entity entity : ESP.mc.world.loadedEntityList) {
                if (entity instanceof EntityXPOrb) {
                    if (ESP.mc.player.getDistanceSq(entity) >= 2500.0) {
                        continue;
                    }
                    final Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, ESP.mc.getRenderPartialTicks());
                    final AxisAlignedBB bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interp.z);
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.renderFilledBox(bb, ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getRed() / 255.0f) : (this.red.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getGreen() / 255.0f) : (this.green.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getBlue() / 255.0f) : (this.blue.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getAlpha() / 255.0f) : (this.boxAlpha.getValue() / 255.0f));
                    GL11.glDisable(2848);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepth();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    RenderUtil.drawBlockOutline(bb, ((boolean)this.colorSync.getValue()) ? Colors.INSTANCE.getCurrentColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0f);
                    if (++i < 50) {
                        continue;
                    }
                    break;
                }
            }
        }
        if (this.pearl.getValue()) {
            int i = 0;
            for (final Entity entity : ESP.mc.world.loadedEntityList) {
                if (entity instanceof EntityEnderPearl) {
                    if (ESP.mc.player.getDistanceSq(entity) >= 2500.0) {
                        continue;
                    }
                    final Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, ESP.mc.getRenderPartialTicks());
                    final AxisAlignedBB bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interp.z);
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.renderFilledBox(bb, ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getRed() / 255.0f) : (this.red.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getGreen() / 255.0f) : (this.green.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getBlue() / 255.0f) : (this.blue.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getAlpha() / 255.0f) : (this.boxAlpha.getValue() / 255.0f));
                    GL11.glDisable(2848);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepth();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    RenderUtil.drawBlockOutline(bb, ((boolean)this.colorSync.getValue()) ? Colors.INSTANCE.getCurrentColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0f);
                    if (++i < 50) {
                        continue;
                    }
                    break;
                }
            }
        }
        if (this.xpbottles.getValue()) {
            int i = 0;
            for (final Entity entity : ESP.mc.world.loadedEntityList) {
                if (entity instanceof EntityExpBottle) {
                    if (ESP.mc.player.getDistanceSq(entity) >= 2500.0) {
                        continue;
                    }
                    final Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, ESP.mc.getRenderPartialTicks());
                    final AxisAlignedBB bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interp.z);
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.renderFilledBox(bb, ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getRed() / 255.0f) : (this.red.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getGreen() / 255.0f) : (this.green.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getBlue() / 255.0f) : (this.blue.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getAlpha() / 255.0f) : (this.boxAlpha.getValue() / 255.0f));
                    GL11.glDisable(2848);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepth();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    RenderUtil.drawBlockOutline(bb, ((boolean)this.colorSync.getValue()) ? Colors.INSTANCE.getCurrentColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0f);
                    if (++i < 50) {
                        continue;
                    }
                    break;
                }
            }
        }
    }
    
    public void esp(final EntityPlayer entityPlayer, final double d, final double d2, final double d3) {
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glDepthMask(true);
        GL11.glLineWidth(1.0f);
        GL11.glTranslated(d, d2, d3);
        GL11.glRotatef(-entityPlayer.rotationYaw, 0.0f, entityPlayer.height, 0.0f);
        GL11.glTranslated(-d, -d2, -d3);
        GL11.glTranslated(d, d2 + entityPlayer.height / 2.0f - 0.22499999403953552, d3);
        GL11.glColor4f(1.38f, 0.55f, 2.38f, 1.0f);
        GL11.glRotated((double)((entityPlayer.isSneaking() ? 35 : 0) + this.spin.getValue()), (double)(1.0f + this.spin.getValue()), 0.0, (double)this.cumSize.getValue());
        GL11.glTranslated(0.0, 0.0, 0.07500000298023224);
        final Cylinder cylinder = new Cylinder();
        cylinder.setDrawStyle(100013);
        cylinder.draw(0.1f, 0.11f, 0.4f, 25, 20);
        GL11.glColor4f(1.38f, 0.85f, 1.38f, 1.0f);
        GL11.glTranslated(0.0, 0.0, -0.12500000298023223);
        GL11.glTranslated(-0.09000000074505805, 0.0, 0.0);
        final Sphere sphere = new Sphere();
        sphere.setDrawStyle(100013);
        sphere.draw(0.14f, 10, 20);
        GL11.glTranslated(0.16000000149011612, 0.0, 0.0);
        final Sphere sphere2 = new Sphere();
        sphere2.setDrawStyle(100013);
        sphere2.draw(0.14f, 10, 20);
        GL11.glColor4f(1.35f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslated(-0.07000000074505806, 0.0, 0.589999952316284);
        final Sphere sphere3 = new Sphere();
        sphere3.setDrawStyle(100013);
        sphere3.draw(0.13f, 15, 20);
        GL11.glDepthMask(true);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
    }
    
    public void onRenderModel(final RenderEntityModelEvent event) {
        if (event.getStage() != 0 || event.entity == null || (event.entity.isInvisible() && !this.invisibles.getValue()) || (!this.self.getValue() && event.entity.equals((Object)ESP.mc.player)) || (!this.players.getValue() && event.entity instanceof EntityPlayer) || (!this.animals.getValue() && EntityUtil.isPassive(event.entity)) || (!this.mobs.getValue() && !EntityUtil.isPassive(event.entity) && !(event.entity instanceof EntityPlayer))) {
            return;
        }
        final Color color = this.colorSync.getValue() ? Colors.INSTANCE.getCurrentColor() : EntityUtil.getColor(event.entity, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue(), this.colorFriends.getValue());
        final boolean fancyGraphics = ESP.mc.gameSettings.fancyGraphics;
        ESP.mc.gameSettings.fancyGraphics = false;
        final float gamma = ESP.mc.gameSettings.gammaSetting;
        ESP.mc.gameSettings.gammaSetting = 10000.0f;
        if (this.onTop.getValue() && (!Chams.getInstance().isEnabled() || !Chams.getInstance().colored.getValue())) {
            event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
        }
        if (this.mode.getValue() == Mode.OUTLINE) {
            RenderUtil.renderOne(this.lineWidth.getValue());
            event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GlStateManager.glLineWidth((float)this.lineWidth.getValue());
            RenderUtil.renderTwo();
            event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GlStateManager.glLineWidth((float)this.lineWidth.getValue());
            RenderUtil.renderThree();
            RenderUtil.renderFour(color);
            event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GlStateManager.glLineWidth((float)this.lineWidth.getValue());
            RenderUtil.renderFive();
        }
        else {
            GL11.glPushMatrix();
            GL11.glPushAttrib(1048575);
            if (this.mode.getValue() == Mode.WIREFRAME) {
                GL11.glPolygonMode(1032, 6913);
            }
            else {
                GL11.glPolygonMode(1028, 6913);
            }
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glEnable(2848);
            GL11.glEnable(3042);
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
            GlStateManager.glLineWidth((float)this.lineWidth.getValue());
            event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
        if (!this.onTop.getValue() && (!Chams.getInstance().isEnabled() || !Chams.getInstance().colored.getValue())) {
            event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
        }
        try {
            ESP.mc.gameSettings.fancyGraphics = fancyGraphics;
            ESP.mc.gameSettings.gammaSetting = gamma;
        }
        catch (Exception ex) {}
        event.setCanceled(true);
    }
    
    public void onEnable() {
        this.burrowedPlayers.clear();
    }
    
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        this.burrowedPlayers.clear();
        this.getPlayers();
    }
    
    private void renderBurrowedBlock(final BlockPos blockPos) {
        RenderUtil.drawBoxESP(blockPos, new Color(this.boxRed.getValue(), this.boxGreen.getValue(), this.boxBlue.getValue(), this.burrowAlpha.getValue()), true, new Color(this.outlineRed.getValue(), this.outlineGreen.getValue(), this.outlineBlue.getValue(), this.outlineAlpha.getValue()), this.outlineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.burrowAlpha.getValue(), true);
    }
    
    private void getPlayers() {
        for (final EntityPlayer entityPlayer : ESP.mc.world.playerEntities) {
            if (entityPlayer != ESP.mc.player && !Phobos.friendManager.isFriend(entityPlayer.getName()) && EntityUtil.isLiving((Entity)entityPlayer)) {
                if (!this.isBurrowed(entityPlayer)) {
                    continue;
                }
                this.burrowedPlayers.put(entityPlayer, new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ));
            }
        }
    }
    
    private boolean isBurrowed(final EntityPlayer entityPlayer) {
        final BlockPos blockPos = new BlockPos(Math.floor(entityPlayer.posX), Math.floor(entityPlayer.posY + 0.2), Math.floor(entityPlayer.posZ));
        return ESP.mc.world.getBlockState(blockPos).getBlock() == Blocks.ENDER_CHEST || ESP.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN || ESP.mc.world.getBlockState(blockPos).getBlock() == Blocks.CHEST || ESP.mc.world.getBlockState(blockPos).getBlock() == Blocks.ANVIL || ESP.mc.world.getBlockState(blockPos).getBlock() == Blocks.END_ROD;
    }
    
    static {
        ESP.INSTANCE = new ESP();
    }
    
    public enum Mode
    {
        WIREFRAME, 
        OUTLINE;
    }
}
