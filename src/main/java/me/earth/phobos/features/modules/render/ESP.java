/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderGlobal
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderPearl
 *  net.minecraft.entity.item.EntityExpBottle
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.Cylinder
 *  org.lwjgl.util.glu.Sphere
 */
package me.earth.phobos.features.modules.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import me.earth.phobos.Phobos;
import me.earth.phobos.event.events.Render3DEvent;
import me.earth.phobos.event.events.RenderEntityModelEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.client.Colors;
import me.earth.phobos.features.modules.render.Chams;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Sphere;

public class ESP
extends Module {
    private static ESP INSTANCE = new ESP();
    private final Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.OUTLINE));
    private final Setting<Boolean> colorSync = this.register(new Setting<Boolean>("Sync", false));
    private final Setting<Boolean> players = this.register(new Setting<Boolean>("Players", true));
    private final Setting<Boolean> animals = this.register(new Setting<Boolean>("Animals", false));
    private final Setting<Boolean> mobs = this.register(new Setting<Boolean>("Mobs", false));
    private final Setting<Boolean> items = this.register(new Setting<Boolean>("Items", false));
    private final Setting<Boolean> xporbs = this.register(new Setting<Boolean>("XpOrbs", false));
    private final Setting<Boolean> xpbottles = this.register(new Setting<Boolean>("XpBottles", false));
    private final Setting<Boolean> pearl = this.register(new Setting<Boolean>("Pearls", false));
    private final Setting<Boolean> penis = this.register(new Setting<Boolean>("Penis", false));
    private final Setting<Integer> spin = this.register(new Setting<Integer>("PSpin", Integer.valueOf(3), Integer.valueOf(0), Integer.valueOf(6), v -> this.penis.getValue()));
    private final Setting<Integer> cumSize = this.register(new Setting<Integer>("PSize", Integer.valueOf(3), Integer.valueOf(0), Integer.valueOf(6), v -> this.penis.getValue()));
    private final Setting<Boolean> burrow = this.register(new Setting<Boolean>("Burrow", false));
    private final Setting<Boolean> name = this.register(new Setting<Boolean>("Name", Boolean.valueOf(true), v -> this.burrow.getValue()));
    private final Setting<Boolean> box = this.register(new Setting<Boolean>("Box", Boolean.valueOf(true), v -> this.burrow.getValue()));
    private final Setting<Integer> boxRed = this.register(new Setting<Integer>("BoxRed", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.box.getValue() != false && this.burrow.getValue() != false));
    private final Setting<Integer> boxGreen = this.register(new Setting<Integer>("BoxGreen", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.box.getValue() != false && this.burrow.getValue() != false));
    private final Setting<Integer> boxBlue = this.register(new Setting<Integer>("BoxBlue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.box.getValue() != false && this.burrow.getValue() != false));
    private final Setting<Integer> burrowAlpha = this.register(new Setting<Integer>("BoxAlpha", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> this.box.getValue() != false && this.burrow.getValue() != false));
    private final Setting<Boolean> outline = this.register(new Setting<Boolean>("Outline", Boolean.valueOf(true), v -> this.burrow.getValue()));
    private final Setting<Float> outlineWidth = this.register(new Setting<Float>("OutlineWidth", Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(5.0f), v -> this.outline.getValue() != false && this.burrow.getValue() != false));
    private final Setting<Boolean> cOutline = this.register(new Setting<Boolean>("CustomOutline", Boolean.valueOf(true), v -> this.outline.getValue() != false && this.burrow.getValue() != false));
    private final Setting<Integer> outlineRed = this.register(new Setting<Integer>("OutlineRed", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue() != false && this.cOutline.getValue() != false && this.burrow.getValue() != false));
    private final Setting<Integer> outlineGreen = this.register(new Setting<Integer>("OutlineGreen", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue() != false && this.cOutline.getValue() != false && this.burrow.getValue() != false));
    private final Setting<Integer> outlineBlue = this.register(new Setting<Integer>("OutlineBlue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue() != false && this.cOutline.getValue() != false && this.burrow.getValue() != false));
    private final Setting<Integer> outlineAlpha = this.register(new Setting<Integer>("OutlineAlpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue() != false && this.cOutline.getValue() != false && this.burrow.getValue() != false));
    private final Setting<Integer> red = this.register(new Setting<Integer>("Red", 255, 0, 255));
    private final Setting<Integer> green = this.register(new Setting<Integer>("Green", 255, 0, 255));
    private final Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 255, 0, 255));
    private final Setting<Integer> boxAlpha = this.register(new Setting<Integer>("BoxAlpha", 120, 0, 255));
    private final Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));
    private final Setting<Float> lineWidth = this.register(new Setting<Float>("LineWidth", Float.valueOf(2.0f), Float.valueOf(0.1f), Float.valueOf(5.0f)));
    private final Setting<Boolean> colorFriends = this.register(new Setting<Boolean>("Friends", true));
    private final Setting<Boolean> self = this.register(new Setting<Boolean>("Self", true));
    private final Setting<Boolean> onTop = this.register(new Setting<Boolean>("onTop", true));
    private final Setting<Boolean> invisibles = this.register(new Setting<Boolean>("Invisibles", false));
    private final Map<EntityPlayer, BlockPos> burrowedPlayers = new HashMap<EntityPlayer, BlockPos>();

    public ESP() {
        super("ESP", "Renders a nice ESP.", Module.Category.RENDER, false, false, false);
        this.setInstance();
    }

    public static ESP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ESP();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        AxisAlignedBB bb;
        Vec3d interp;
        if (this.penis.getValue().booleanValue()) {
            for (Object e : ESP.mc.field_71441_e.field_72996_f) {
                if (!(e instanceof EntityPlayer)) continue;
                RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
                EntityPlayer entityPlayer = (EntityPlayer)e;
                double d = entityPlayer.field_70142_S + (entityPlayer.field_70165_t - entityPlayer.field_70142_S) * (double)ESP.mc.field_71428_T.field_194147_b;
                mc.func_175598_ae();
                double d2 = d - renderManager.field_78725_b;
                double d3 = entityPlayer.field_70137_T + (entityPlayer.field_70163_u - entityPlayer.field_70137_T) * (double)ESP.mc.field_71428_T.field_194147_b;
                mc.func_175598_ae();
                double d4 = d3 - renderManager.field_78726_c;
                double d5 = entityPlayer.field_70136_U + (entityPlayer.field_70161_v - entityPlayer.field_70136_U) * (double)ESP.mc.field_71428_T.field_194147_b;
                mc.func_175598_ae();
                double d6 = d5 - renderManager.field_78723_d;
                GL11.glPushMatrix();
                RenderHelper.func_74518_a();
                this.esp(entityPlayer, d2, d4, d6);
                RenderHelper.func_74519_b();
                GL11.glPopMatrix();
            }
        }
        if (this.burrow.getValue().booleanValue() && !this.burrowedPlayers.isEmpty()) {
            this.burrowedPlayers.forEach((key, value) -> {
                this.renderBurrowedBlock((BlockPos)value);
                if (this.name.getValue().booleanValue()) {
                    RenderUtil.drawText(new AxisAlignedBB(value), key.func_146103_bH().getName());
                }
            });
        }
        if (this.items.getValue().booleanValue()) {
            int i = 0;
            for (Entity entity : ESP.mc.field_71441_e.field_72996_f) {
                if (!(entity instanceof EntityItem) || !(ESP.mc.field_71439_g.func_70068_e(entity) < 2500.0)) continue;
                interp = EntityUtil.getInterpolatedRenderPos(entity, mc.func_184121_ak());
                bb = new AxisAlignedBB(entity.func_174813_aQ().field_72340_a - 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72338_b - 0.0 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72339_c - 0.05 - entity.field_70161_v + interp.field_72449_c, entity.func_174813_aQ().field_72336_d + 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72337_e + 0.1 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72334_f + 0.05 - entity.field_70161_v + interp.field_72449_c);
                GlStateManager.func_179094_E();
                GlStateManager.func_179147_l();
                GlStateManager.func_179097_i();
                GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
                GlStateManager.func_179090_x();
                GlStateManager.func_179132_a((boolean)false);
                GL11.glEnable((int)2848);
                GL11.glHint((int)3154, (int)4354);
                GL11.glLineWidth((float)1.0f);
                RenderGlobal.func_189696_b((AxisAlignedBB)bb, (float)(this.colorSync.getValue() != false ? (float)Colors.INSTANCE.getCurrentColor().getRed() / 255.0f : (float)this.red.getValue().intValue() / 255.0f), (float)(this.colorSync.getValue() != false ? (float)Colors.INSTANCE.getCurrentColor().getGreen() / 255.0f : (float)this.green.getValue().intValue() / 255.0f), (float)(this.colorSync.getValue() != false ? (float)Colors.INSTANCE.getCurrentColor().getBlue() / 255.0f : (float)this.blue.getValue().intValue() / 255.0f), (float)(this.colorSync.getValue() != false ? (float)Colors.INSTANCE.getCurrentColor().getAlpha() : (float)this.boxAlpha.getValue().intValue() / 255.0f));
                GL11.glDisable((int)2848);
                GlStateManager.func_179132_a((boolean)true);
                GlStateManager.func_179126_j();
                GlStateManager.func_179098_w();
                GlStateManager.func_179084_k();
                GlStateManager.func_179121_F();
                RenderUtil.drawBlockOutline(bb, this.colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0f);
                if (++i < 50) continue;
            }
        }
        if (this.xporbs.getValue().booleanValue()) {
            int i = 0;
            for (Entity entity : ESP.mc.field_71441_e.field_72996_f) {
                if (!(entity instanceof EntityXPOrb) || !(ESP.mc.field_71439_g.func_70068_e(entity) < 2500.0)) continue;
                interp = EntityUtil.getInterpolatedRenderPos(entity, mc.func_184121_ak());
                bb = new AxisAlignedBB(entity.func_174813_aQ().field_72340_a - 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72338_b - 0.0 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72339_c - 0.05 - entity.field_70161_v + interp.field_72449_c, entity.func_174813_aQ().field_72336_d + 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72337_e + 0.1 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72334_f + 0.05 - entity.field_70161_v + interp.field_72449_c);
                GlStateManager.func_179094_E();
                GlStateManager.func_179147_l();
                GlStateManager.func_179097_i();
                GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
                GlStateManager.func_179090_x();
                GlStateManager.func_179132_a((boolean)false);
                GL11.glEnable((int)2848);
                GL11.glHint((int)3154, (int)4354);
                GL11.glLineWidth((float)1.0f);
                RenderGlobal.func_189696_b((AxisAlignedBB)bb, (float)(this.colorSync.getValue() != false ? (float)Colors.INSTANCE.getCurrentColor().getRed() / 255.0f : (float)this.red.getValue().intValue() / 255.0f), (float)(this.colorSync.getValue() != false ? (float)Colors.INSTANCE.getCurrentColor().getGreen() / 255.0f : (float)this.green.getValue().intValue() / 255.0f), (float)(this.colorSync.getValue() != false ? (float)Colors.INSTANCE.getCurrentColor().getBlue() / 255.0f : (float)this.blue.getValue().intValue() / 255.0f), (float)(this.colorSync.getValue() != false ? (float)Colors.INSTANCE.getCurrentColor().getAlpha() / 255.0f : (float)this.boxAlpha.getValue().intValue() / 255.0f));
                GL11.glDisable((int)2848);
                GlStateManager.func_179132_a((boolean)true);
                GlStateManager.func_179126_j();
                GlStateManager.func_179098_w();
                GlStateManager.func_179084_k();
                GlStateManager.func_179121_F();
                RenderUtil.drawBlockOutline(bb, this.colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0f);
                if (++i < 50) continue;
            }
        }
        if (this.pearl.getValue().booleanValue()) {
            int i = 0;
            for (Entity entity : ESP.mc.field_71441_e.field_72996_f) {
                if (!(entity instanceof EntityEnderPearl) || !(ESP.mc.field_71439_g.func_70068_e(entity) < 2500.0)) continue;
                interp = EntityUtil.getInterpolatedRenderPos(entity, mc.func_184121_ak());
                bb = new AxisAlignedBB(entity.func_174813_aQ().field_72340_a - 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72338_b - 0.0 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72339_c - 0.05 - entity.field_70161_v + interp.field_72449_c, entity.func_174813_aQ().field_72336_d + 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72337_e + 0.1 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72334_f + 0.05 - entity.field_70161_v + interp.field_72449_c);
                GlStateManager.func_179094_E();
                GlStateManager.func_179147_l();
                GlStateManager.func_179097_i();
                GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
                GlStateManager.func_179090_x();
                GlStateManager.func_179132_a((boolean)false);
                GL11.glEnable((int)2848);
                GL11.glHint((int)3154, (int)4354);
                GL11.glLineWidth((float)1.0f);
                RenderGlobal.func_189696_b((AxisAlignedBB)bb, (float)(this.colorSync.getValue() != false ? (float)Colors.INSTANCE.getCurrentColor().getRed() / 255.0f : (float)this.red.getValue().intValue() / 255.0f), (float)(this.colorSync.getValue() != false ? (float)Colors.INSTANCE.getCurrentColor().getGreen() / 255.0f : (float)this.green.getValue().intValue() / 255.0f), (float)(this.colorSync.getValue() != false ? (float)Colors.INSTANCE.getCurrentColor().getBlue() / 255.0f : (float)this.blue.getValue().intValue() / 255.0f), (float)(this.colorSync.getValue() != false ? (float)Colors.INSTANCE.getCurrentColor().getAlpha() / 255.0f : (float)this.boxAlpha.getValue().intValue() / 255.0f));
                GL11.glDisable((int)2848);
                GlStateManager.func_179132_a((boolean)true);
                GlStateManager.func_179126_j();
                GlStateManager.func_179098_w();
                GlStateManager.func_179084_k();
                GlStateManager.func_179121_F();
                RenderUtil.drawBlockOutline(bb, this.colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0f);
                if (++i < 50) continue;
            }
        }
        if (this.xpbottles.getValue().booleanValue()) {
            int i = 0;
            for (Entity entity : ESP.mc.field_71441_e.field_72996_f) {
                if (!(entity instanceof EntityExpBottle) || !(ESP.mc.field_71439_g.func_70068_e(entity) < 2500.0)) continue;
                interp = EntityUtil.getInterpolatedRenderPos(entity, mc.func_184121_ak());
                bb = new AxisAlignedBB(entity.func_174813_aQ().field_72340_a - 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72338_b - 0.0 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72339_c - 0.05 - entity.field_70161_v + interp.field_72449_c, entity.func_174813_aQ().field_72336_d + 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72337_e + 0.1 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72334_f + 0.05 - entity.field_70161_v + interp.field_72449_c);
                GlStateManager.func_179094_E();
                GlStateManager.func_179147_l();
                GlStateManager.func_179097_i();
                GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
                GlStateManager.func_179090_x();
                GlStateManager.func_179132_a((boolean)false);
                GL11.glEnable((int)2848);
                GL11.glHint((int)3154, (int)4354);
                GL11.glLineWidth((float)1.0f);
                RenderGlobal.func_189696_b((AxisAlignedBB)bb, (float)(this.colorSync.getValue() != false ? (float)Colors.INSTANCE.getCurrentColor().getRed() / 255.0f : (float)this.red.getValue().intValue() / 255.0f), (float)(this.colorSync.getValue() != false ? (float)Colors.INSTANCE.getCurrentColor().getGreen() / 255.0f : (float)this.green.getValue().intValue() / 255.0f), (float)(this.colorSync.getValue() != false ? (float)Colors.INSTANCE.getCurrentColor().getBlue() / 255.0f : (float)this.blue.getValue().intValue() / 255.0f), (float)(this.colorSync.getValue() != false ? (float)Colors.INSTANCE.getCurrentColor().getAlpha() / 255.0f : (float)this.boxAlpha.getValue().intValue() / 255.0f));
                GL11.glDisable((int)2848);
                GlStateManager.func_179132_a((boolean)true);
                GlStateManager.func_179126_j();
                GlStateManager.func_179098_w();
                GlStateManager.func_179084_k();
                GlStateManager.func_179121_F();
                RenderUtil.drawBlockOutline(bb, this.colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0f);
                if (++i < 50) continue;
            }
        }
    }

    public void esp(EntityPlayer entityPlayer, double d, double d2, double d3) {
        GL11.glDisable((int)2896);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)2848);
        GL11.glDepthMask((boolean)true);
        GL11.glLineWidth((float)1.0f);
        GL11.glTranslated((double)d, (double)d2, (double)d3);
        GL11.glRotatef((float)(-entityPlayer.field_70177_z), (float)0.0f, (float)entityPlayer.field_70131_O, (float)0.0f);
        GL11.glTranslated((double)(-d), (double)(-d2), (double)(-d3));
        GL11.glTranslated((double)d, (double)(d2 + (double)(entityPlayer.field_70131_O / 2.0f) - (double)0.225f), (double)d3);
        GL11.glColor4f((float)1.38f, (float)0.55f, (float)2.38f, (float)1.0f);
        GL11.glRotated((double)((entityPlayer.func_70093_af() ? 35 : 0) + this.spin.getValue()), (double)(1.0f + (float)this.spin.getValue().intValue()), (double)0.0, (double)this.cumSize.getValue().intValue());
        GL11.glTranslated((double)0.0, (double)0.0, (double)0.075f);
        Cylinder cylinder = new Cylinder();
        cylinder.setDrawStyle(100013);
        cylinder.draw(0.1f, 0.11f, 0.4f, 25, 20);
        GL11.glColor4f((float)1.38f, (float)0.85f, (float)1.38f, (float)1.0f);
        GL11.glTranslated((double)0.0, (double)0.0, (double)-0.12500000298023223);
        GL11.glTranslated((double)-0.09000000074505805, (double)0.0, (double)0.0);
        Sphere sphere = new Sphere();
        sphere.setDrawStyle(100013);
        sphere.draw(0.14f, 10, 20);
        GL11.glTranslated((double)0.16000000149011612, (double)0.0, (double)0.0);
        Sphere sphere2 = new Sphere();
        sphere2.setDrawStyle(100013);
        sphere2.draw(0.14f, 10, 20);
        GL11.glColor4f((float)1.35f, (float)0.0f, (float)0.0f, (float)1.0f);
        GL11.glTranslated((double)-0.07000000074505806, (double)0.0, (double)0.589999952316284);
        Sphere sphere3 = new Sphere();
        sphere3.setDrawStyle(100013);
        sphere3.draw(0.13f, 15, 20);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2896);
        GL11.glEnable((int)3553);
    }

    public void onRenderModel(RenderEntityModelEvent event) {
        if (event.getStage() != 0 || event.entity == null || event.entity.func_82150_aj() && this.invisibles.getValue() == false || this.self.getValue() == false && event.entity.equals((Object)ESP.mc.field_71439_g) || this.players.getValue() == false && event.entity instanceof EntityPlayer || this.animals.getValue() == false && EntityUtil.isPassive(event.entity) || !this.mobs.getValue().booleanValue() && !EntityUtil.isPassive(event.entity) && !(event.entity instanceof EntityPlayer)) {
            return;
        }
        Color color = this.colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColor() : EntityUtil.getColor(event.entity, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue(), this.colorFriends.getValue());
        boolean fancyGraphics = ESP.mc.field_71474_y.field_74347_j;
        ESP.mc.field_71474_y.field_74347_j = false;
        float gamma = ESP.mc.field_71474_y.field_74333_Y;
        ESP.mc.field_71474_y.field_74333_Y = 10000.0f;
        if (!(!this.onTop.getValue().booleanValue() || Chams.getInstance().isEnabled() && Chams.getInstance().colored.getValue().booleanValue())) {
            event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
        }
        if (this.mode.getValue() == Mode.OUTLINE) {
            RenderUtil.renderOne(this.lineWidth.getValue().floatValue());
            event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GlStateManager.func_187441_d((float)this.lineWidth.getValue().floatValue());
            RenderUtil.renderTwo();
            event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GlStateManager.func_187441_d((float)this.lineWidth.getValue().floatValue());
            RenderUtil.renderThree();
            RenderUtil.renderFour(color);
            event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GlStateManager.func_187441_d((float)this.lineWidth.getValue().floatValue());
            RenderUtil.renderFive();
        } else {
            GL11.glPushMatrix();
            GL11.glPushAttrib((int)1048575);
            if (this.mode.getValue() == Mode.WIREFRAME) {
                GL11.glPolygonMode((int)1032, (int)6913);
            } else {
                GL11.glPolygonMode((int)1028, (int)6913);
            }
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2896);
            GL11.glDisable((int)2929);
            GL11.glEnable((int)2848);
            GL11.glEnable((int)3042);
            GlStateManager.func_179112_b((int)770, (int)771);
            GlStateManager.func_179131_c((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            GlStateManager.func_187441_d((float)this.lineWidth.getValue().floatValue());
            event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
        if (!(this.onTop.getValue().booleanValue() || Chams.getInstance().isEnabled() && Chams.getInstance().colored.getValue().booleanValue())) {
            event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
        }
        try {
            ESP.mc.field_71474_y.field_74347_j = fancyGraphics;
            ESP.mc.field_71474_y.field_74333_Y = gamma;
        }
        catch (Exception exception) {
            // empty catch block
        }
        event.setCanceled(true);
    }

    @Override
    public void onEnable() {
        this.burrowedPlayers.clear();
    }

    @Override
    public void onUpdate() {
        if (ESP.fullNullCheck()) {
            return;
        }
        this.burrowedPlayers.clear();
        this.getPlayers();
    }

    private void renderBurrowedBlock(BlockPos blockPos) {
        RenderUtil.drawBoxESP(blockPos, new Color(this.boxRed.getValue(), this.boxGreen.getValue(), this.boxBlue.getValue(), this.burrowAlpha.getValue()), true, new Color(this.outlineRed.getValue(), this.outlineGreen.getValue(), this.outlineBlue.getValue(), this.outlineAlpha.getValue()), this.outlineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.burrowAlpha.getValue(), true);
    }

    private void getPlayers() {
        for (EntityPlayer entityPlayer : ESP.mc.field_71441_e.field_73010_i) {
            if (entityPlayer == ESP.mc.field_71439_g || Phobos.friendManager.isFriend(entityPlayer.func_70005_c_()) || !EntityUtil.isLiving((Entity)entityPlayer) || !this.isBurrowed(entityPlayer)) continue;
            this.burrowedPlayers.put(entityPlayer, new BlockPos(entityPlayer.field_70165_t, entityPlayer.field_70163_u, entityPlayer.field_70161_v));
        }
    }

    private boolean isBurrowed(EntityPlayer entityPlayer) {
        BlockPos blockPos = new BlockPos(Math.floor(entityPlayer.field_70165_t), Math.floor(entityPlayer.field_70163_u + 0.2), Math.floor(entityPlayer.field_70161_v));
        return ESP.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150477_bB || ESP.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150343_Z || ESP.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150486_ae || ESP.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150467_bQ || ESP.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_185764_cQ;
    }

    public static enum Mode {
        WIREFRAME,
        OUTLINE;

    }
}

