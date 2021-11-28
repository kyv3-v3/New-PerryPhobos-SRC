/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelPlayer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.EnumHandSide
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.opengl.GL11
 */
package me.earth.phobos.features.modules.render;

import com.mojang.authlib.GameProfile;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import me.earth.phobos.event.events.Render3DEvent;
import me.earth.phobos.event.events.RenderEntityModelEvent;
import me.earth.phobos.event.events.TotemPopEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.client.Colors;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.MathUtil;
import me.earth.phobos.util.RenderUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHandSide;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class PopChams
extends Module {
    private static PopChams INSTANCE = new PopChams();
    private final Setting<Integer> duration = this.register(new Setting<Integer>("Duration", 2500, 100, 10000));
    private final Setting<Boolean> fade = this.register(new Setting<Boolean>("Fade", true));
    private final Setting<Boolean> still = this.register(new Setting<Boolean>("Static", true));
    private final Setting<Boolean> heaven = this.register(new Setting<Boolean>("Heaven", Boolean.valueOf(false), v -> this.still.getValue()));
    private final Setting<Double> ascension = this.register(new Setting<Double>("Movement", Double.valueOf(1.0), Double.valueOf(0.1), Double.valueOf(4.0), v -> this.heaven.getValue() != false && this.still.getValue() != false));
    private final Setting<Boolean> outline = this.register(new Setting<Boolean>("Outline", true));
    private final Setting<Boolean> fill = this.register(new Setting<Boolean>("Fill", true));
    private final Setting<Mode> mode = this.register(new Setting<Mode>("OutlineMode", Mode.WIREFRAME, v -> this.outline.getValue()));
    private final Setting<Float> lineWidth = this.register(new Setting<Float>("LineWidth", Float.valueOf(3.0f), Float.valueOf(0.1f), Float.valueOf(6.0f), v -> this.outline.getValue()));
    private final Setting<Integer> oAlpha = this.register(new Setting<Integer>("OAlpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue()));
    private final Setting<Integer> fAlpha = this.register(new Setting<Integer>("FAlpha", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(255), v -> this.fill.getValue()));
    private final Setting<Boolean> colorSync = this.register(new Setting<Boolean>("CSync", Boolean.valueOf(false), v -> this.fill.getValue() != false || this.outline.getValue() != false));
    private final Setting<Boolean> rainbow = this.register(new Setting<Boolean>("Rainbow", Boolean.valueOf(false), v -> this.fill.getValue() != false || this.outline.getValue() != false));
    private final Setting<Integer> oRed = this.register(new Setting<Integer>("ORed", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.colorSync.getValue() == false && this.rainbow.getValue() == false && this.outline.getValue() != false));
    private final Setting<Integer> oGreen = this.register(new Setting<Integer>("OGreen", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.colorSync.getValue() == false && this.rainbow.getValue() == false && this.outline.getValue() != false));
    private final Setting<Integer> oBlue = this.register(new Setting<Integer>("OBlue", Integer.valueOf(180), Integer.valueOf(0), Integer.valueOf(255), v -> this.colorSync.getValue() == false && this.rainbow.getValue() == false && this.outline.getValue() != false));
    private final Setting<Integer> fRed = this.register(new Setting<Integer>("FRed", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.colorSync.getValue() == false && this.rainbow.getValue() == false && this.fill.getValue() != false));
    private final Setting<Integer> fGreen = this.register(new Setting<Integer>("FGreen", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.colorSync.getValue() == false && this.rainbow.getValue() == false && this.fill.getValue() != false));
    private final Setting<Integer> fBlue = this.register(new Setting<Integer>("FBlue", Integer.valueOf(180), Integer.valueOf(0), Integer.valueOf(255), v -> this.colorSync.getValue() == false && this.rainbow.getValue() == false && this.fill.getValue() != false));
    private final Setting<Boolean> visColor = this.register(new Setting<Boolean>("VColor", Boolean.valueOf(false), v -> this.rainbow.getValue() == false && this.colorSync.getValue() == false && this.fill.getValue() != false));
    private final Setting<Integer> vRed = this.register(new Setting<Integer>("VRed", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(255), v -> this.colorSync.getValue() == false && this.rainbow.getValue() == false && this.visColor.getValue() != false && this.fill.getValue() != false));
    private final Setting<Integer> vGreen = this.register(new Setting<Integer>("VGreen", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.colorSync.getValue() == false && this.rainbow.getValue() == false && this.visColor.getValue() != false && this.fill.getValue() != false));
    private final Setting<Integer> vBlue = this.register(new Setting<Integer>("VBlue", Integer.valueOf(180), Integer.valueOf(0), Integer.valueOf(255), v -> this.colorSync.getValue() == false && this.rainbow.getValue() == false && this.visColor.getValue() != false && this.fill.getValue() != false));
    private final Setting<Integer> speed = this.register(new Setting<Integer>("Speed", Integer.valueOf(40), Integer.valueOf(1), Integer.valueOf(100), v -> this.rainbow.getValue() != false && this.colorSync.getValue() == false));
    private final Setting<Integer> saturation = this.register(new Setting<Integer>("Saturation", Integer.valueOf(65), Integer.valueOf(0), Integer.valueOf(100), v -> this.rainbow.getValue() != false && this.colorSync.getValue() == false));
    private final Setting<Integer> brightness = this.register(new Setting<Integer>("Brightness", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(100), v -> this.rainbow.getValue() != false && this.colorSync.getValue() == false));
    private final List<PopChamContext> badPlayers = new ArrayList<PopChamContext>();

    public PopChams() {
        super("PopChams", "Puts chams over ppl popping.", Module.Category.RENDER, true, false, false);
        this.setInstance();
    }

    public static PopChams getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PopChams();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onTotemPop(TotemPopEvent event) {
        if (!this.still.getValue().booleanValue()) {
            this.badPlayers.removeIf(player -> player.getPlayer().equals((Object)event.getEntity()));
        }
        this.badPlayers.add(new PopChamContext(event.getEntity(), event.getEntity().func_184591_cq()));
    }

    @Override
    public void onTick() {
        this.badPlayers.removeIf(player -> player.getTime() > this.duration.getValue());
        this.badPlayers.forEach(PopChamContext::incrementTime);
    }

    @Override
    public void onRender3D(Render3DEvent e) {
        if (!this.still.getValue().booleanValue()) {
            return;
        }
        this.badPlayers.stream().filter(player -> player.getEvent() != null).collect(Collectors.toList()).forEach(player -> {
            double[] offset = player.getOffset();
            float factor = (float)(this.duration.getValue() - player.getTime()) / (float)this.duration.getValue().intValue();
            double ascension = this.heaven.getValue() != false ? (double)(factor - 1.0f) * this.ascension.getValue() : 0.0;
            RenderEntityModelEvent event = player.getEvent();
            GlStateManager.func_179094_E();
            GlStateManager.func_179137_b((double)offset[0], (double)(offset[1] - ascension), (double)offset[2]);
            GlStateManager.func_179114_b((float)180.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            GlStateManager.func_179114_b((float)(event.entity.field_70177_z - event.headYaw), (float)0.0f, (float)1.0f, (float)0.0f);
            this.doThing(player.getEvent(), factor);
            GlStateManager.func_179121_F();
        });
    }

    public void onRenderModel(RenderEntityModelEvent event) {
        if (this.still.getValue().booleanValue()) {
            this.badPlayers.stream().filter(player -> player.getPlayer().equals((Object)event.entity) && player.getEvent() == null).forEach(player -> player.setEvent(event));
            return;
        }
        this.badPlayers.stream().filter(player -> player.getPlayer().equals((Object)event.entity)).findFirst().ifPresent(context -> {
            this.doThing(event, (float)(this.duration.getValue() - context.getTime()) / (float)this.duration.getValue().intValue());
            event.setCanceled(true);
        });
    }

    private void doThing(RenderEntityModelEvent event, float factor) {
        if (event == null) {
            return;
        }
        if (this.fill.getValue().booleanValue()) {
            int alpha = MathUtil.clamp((int)((this.fade.getValue() != false ? factor : 1.0f) * (float)this.fAlpha.getValue().intValue()), 0, 255);
            Color fColor = this.colorSync.getValue() != false ? new Color(Colors.INSTANCE.getCurrentColor().getRed(), Colors.INSTANCE.getCurrentColor().getGreen(), Colors.INSTANCE.getCurrentColor().getBlue(), alpha) : (this.rainbow.getValue() != false ? RenderUtil.getRainbowAlpha(this.speed.getValue() * 100, 0, (float)this.saturation.getValue().intValue() / 100.0f, (float)this.brightness.getValue().intValue() / 100.0f, alpha) : new Color(this.fRed.getValue(), this.fGreen.getValue(), this.fBlue.getValue(), alpha));
            GL11.glPushAttrib((int)1048575);
            GL11.glDisable((int)3008);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2896);
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glLineWidth((float)1.5f);
            GL11.glEnable((int)2960);
            GL11.glDisable((int)2929);
            GL11.glDepthMask((boolean)false);
            GL11.glEnable((int)10754);
            RenderUtil.setColor(fColor);
            event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GL11.glEnable((int)2929);
            GL11.glDepthMask((boolean)true);
            Color vColor = new Color(this.vRed.getValue(), this.vGreen.getValue(), this.vBlue.getValue(), alpha);
            if (this.visColor.getValue().booleanValue() && !this.rainbow.getValue().booleanValue() && !this.colorSync.getValue().booleanValue()) {
                RenderUtil.setColor(vColor);
            }
            event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GL11.glEnable((int)3042);
            GL11.glEnable((int)2896);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)3008);
            GL11.glPopAttrib();
        }
        if (this.outline.getValue().booleanValue()) {
            Color oColor;
            boolean fancyGraphics = PopChams.mc.field_71474_y.field_74347_j;
            float gamma = PopChams.mc.field_71474_y.field_74333_Y;
            PopChams.mc.field_71474_y.field_74347_j = false;
            PopChams.mc.field_71474_y.field_74333_Y = 10000.0f;
            int alpha = MathUtil.clamp((int)((this.fade.getValue() != false ? factor : 1.0f) * (float)this.oAlpha.getValue().intValue()), 0, 255);
            Color color = this.colorSync.getValue() != false ? new Color(Colors.INSTANCE.getCurrentColor().getRed(), Colors.INSTANCE.getCurrentColor().getGreen(), Colors.INSTANCE.getCurrentColor().getBlue(), alpha) : (oColor = this.rainbow.getValue() != false ? RenderUtil.getRainbowAlpha(this.speed.getValue() * 100, 0, (float)this.saturation.getValue().intValue() / 100.0f, (float)this.brightness.getValue().intValue() / 100.0f, alpha) : new Color(this.oRed.getValue(), this.oGreen.getValue(), this.oBlue.getValue(), alpha));
            if (this.mode.getValue() == Mode.OUTLINE) {
                if (!this.fill.getValue().booleanValue() && !this.still.getValue().booleanValue()) {
                    event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
                }
                RenderUtil.renderOne(this.lineWidth.getValue().floatValue());
                event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
                GlStateManager.func_187441_d((float)this.lineWidth.getValue().floatValue());
                RenderUtil.renderTwo();
                event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
                GlStateManager.func_187441_d((float)this.lineWidth.getValue().floatValue());
                RenderUtil.renderThree();
                RenderUtil.renderFour(oColor);
                event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
                GlStateManager.func_187441_d((float)this.lineWidth.getValue().floatValue());
                RenderUtil.renderFive();
            } else {
                if (!this.fill.getValue().booleanValue() && !this.still.getValue().booleanValue()) {
                    event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
                }
                GL11.glPushMatrix();
                GL11.glPushAttrib((int)1048575);
                GL11.glPolygonMode((int)1032, (int)6913);
                GL11.glDisable((int)3553);
                GL11.glDisable((int)2896);
                GL11.glDisable((int)2929);
                GL11.glEnable((int)2848);
                GL11.glEnable((int)3042);
                GlStateManager.func_179112_b((int)770, (int)771);
                RenderUtil.setColor(oColor);
                GlStateManager.func_187441_d((float)this.lineWidth.getValue().floatValue());
                event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
                GL11.glPopAttrib();
                GL11.glPopMatrix();
            }
            try {
                PopChams.mc.field_71474_y.field_74347_j = fancyGraphics;
                PopChams.mc.field_71474_y.field_74333_Y = gamma;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    static class PopChamContext {
        private final EntityPlayer player;
        private final EnumHandSide hand;
        private int time;
        private RenderEntityModelEvent event;
        private double[] pos;

        public PopChamContext(EntityPlayer player, EnumHandSide hand) {
            this.player = player;
            this.hand = hand.func_188468_a();
        }

        public EntityPlayer getPlayer() {
            return this.player;
        }

        public int getTime() {
            return this.time;
        }

        public void incrementTime() {
            this.time += 50;
        }

        public RenderEntityModelEvent getEvent() {
            return this.event;
        }

        public void setEvent(RenderEntityModelEvent event) {
            if (this.event != null) {
                return;
            }
            EntityOtherPlayerMP entity = new EntityOtherPlayerMP((World)PopChams.mc.field_71441_e, new GameProfile(event.entity.func_110124_au(), "Cr33pyl3mon4de"));
            entity.func_82149_j(event.entity);
            entity.field_70177_z = event.entity.field_70177_z;
            entity.func_70095_a(event.entity.func_70093_af());
            if (event.entity.func_70093_af()) {
                entity.field_70163_u -= 0.1;
            }
            entity.func_184819_a(this.hand);
            ModelPlayer player = new ModelPlayer(event.scale, !entity.func_175154_l().equals("default"));
            player.func_178686_a(event.modelBase);
            player.func_78087_a(event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale, event.entity);
            this.event = new RenderEntityModelEvent(event.getStage(), (ModelBase)player, (Entity)entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale * 0.925f);
            this.pos = new double[]{this.event.entity.field_70165_t, this.event.entity.field_70163_u, this.event.entity.field_70161_v};
        }

        public double[] getOffset() {
            if (this.pos != null) {
                return new double[]{this.pos[0] - PopChams.mc.func_175598_ae().field_78730_l, this.pos[1] - PopChams.mc.func_175598_ae().field_78731_m + 1.41, this.pos[2] - PopChams.mc.func_175598_ae().field_78728_n};
            }
            return new double[]{0.0, 100.0, 0.0};
        }
    }

    public static enum Mode {
        OUTLINE,
        WIREFRAME;

    }
}

