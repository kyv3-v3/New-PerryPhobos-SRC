



package me.earth.phobos.features.modules.render;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import java.util.*;
import net.minecraftforge.fml.common.eventhandler.*;
import java.util.stream.*;
import net.minecraft.client.renderer.*;
import me.earth.phobos.event.events.*;
import me.earth.phobos.features.modules.client.*;
import java.awt.*;
import me.earth.phobos.util.*;
import org.lwjgl.opengl.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;
import com.mojang.authlib.*;
import net.minecraft.client.entity.*;
import net.minecraft.world.*;
import net.minecraft.client.model.*;
import net.minecraft.entity.*;

public class PopChams extends Module
{
    private static PopChams INSTANCE;
    private final Setting<Integer> duration;
    private final Setting<Boolean> fade;
    private final Setting<Boolean> still;
    private final Setting<Boolean> heaven;
    private final Setting<Double> ascension;
    private final Setting<Boolean> outline;
    private final Setting<Boolean> fill;
    private final Setting<Mode> mode;
    private final Setting<Float> lineWidth;
    private final Setting<Integer> oAlpha;
    private final Setting<Integer> fAlpha;
    private final Setting<Boolean> colorSync;
    private final Setting<Boolean> rainbow;
    private final Setting<Integer> oRed;
    private final Setting<Integer> oGreen;
    private final Setting<Integer> oBlue;
    private final Setting<Integer> fRed;
    private final Setting<Integer> fGreen;
    private final Setting<Integer> fBlue;
    private final Setting<Boolean> visColor;
    private final Setting<Integer> vRed;
    private final Setting<Integer> vGreen;
    private final Setting<Integer> vBlue;
    private final Setting<Integer> speed;
    private final Setting<Integer> saturation;
    private final Setting<Integer> brightness;
    private final List<PopChamContext> badPlayers;
    
    public PopChams() {
        super("PopChams", "Puts chams over ppl popping.", Module.Category.RENDER, true, false, false);
        this.duration = (Setting<Integer>)this.register(new Setting("Duration", (T)2500, (T)100, (T)10000));
        this.fade = (Setting<Boolean>)this.register(new Setting("Fade", (T)true));
        this.still = (Setting<Boolean>)this.register(new Setting("Static", (T)true));
        this.heaven = (Setting<Boolean>)this.register(new Setting("Heaven", (T)false, v -> this.still.getValue()));
        this.ascension = (Setting<Double>)this.register(new Setting("Movement", (T)1.0, (T)0.1, (T)4.0, v -> this.heaven.getValue() && this.still.getValue()));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", (T)true));
        this.fill = (Setting<Boolean>)this.register(new Setting("Fill", (T)true));
        this.mode = (Setting<Mode>)this.register(new Setting("OutlineMode", (T)Mode.WIREFRAME, v -> this.outline.getValue()));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (T)3.0f, (T)0.1f, (T)6.0f, v -> this.outline.getValue()));
        this.oAlpha = (Setting<Integer>)this.register(new Setting("OAlpha", (T)255, (T)0, (T)255, v -> this.outline.getValue()));
        this.fAlpha = (Setting<Integer>)this.register(new Setting("FAlpha", (T)50, (T)0, (T)255, v -> this.fill.getValue()));
        this.colorSync = (Setting<Boolean>)this.register(new Setting("CSync", (T)false, v -> this.fill.getValue() || this.outline.getValue()));
        this.rainbow = (Setting<Boolean>)this.register(new Setting("Rainbow", (T)false, v -> this.fill.getValue() || this.outline.getValue()));
        this.oRed = (Setting<Integer>)this.register(new Setting("ORed", (T)255, (T)0, (T)255, v -> !this.colorSync.getValue() && !this.rainbow.getValue() && this.outline.getValue()));
        this.oGreen = (Setting<Integer>)this.register(new Setting("OGreen", (T)0, (T)0, (T)255, v -> !this.colorSync.getValue() && !this.rainbow.getValue() && this.outline.getValue()));
        this.oBlue = (Setting<Integer>)this.register(new Setting("OBlue", (T)180, (T)0, (T)255, v -> !this.colorSync.getValue() && !this.rainbow.getValue() && this.outline.getValue()));
        this.fRed = (Setting<Integer>)this.register(new Setting("FRed", (T)255, (T)0, (T)255, v -> !this.colorSync.getValue() && !this.rainbow.getValue() && this.fill.getValue()));
        this.fGreen = (Setting<Integer>)this.register(new Setting("FGreen", (T)0, (T)0, (T)255, v -> !this.colorSync.getValue() && !this.rainbow.getValue() && this.fill.getValue()));
        this.fBlue = (Setting<Integer>)this.register(new Setting("FBlue", (T)180, (T)0, (T)255, v -> !this.colorSync.getValue() && !this.rainbow.getValue() && this.fill.getValue()));
        this.visColor = (Setting<Boolean>)this.register(new Setting("VColor", (T)false, v -> !this.rainbow.getValue() && !this.colorSync.getValue() && this.fill.getValue()));
        this.vRed = (Setting<Integer>)this.register(new Setting("VRed", (T)50, (T)0, (T)255, v -> !this.colorSync.getValue() && !this.rainbow.getValue() && this.visColor.getValue() && this.fill.getValue()));
        this.vGreen = (Setting<Integer>)this.register(new Setting("VGreen", (T)255, (T)0, (T)255, v -> !this.colorSync.getValue() && !this.rainbow.getValue() && this.visColor.getValue() && this.fill.getValue()));
        this.vBlue = (Setting<Integer>)this.register(new Setting("VBlue", (T)180, (T)0, (T)255, v -> !this.colorSync.getValue() && !this.rainbow.getValue() && this.visColor.getValue() && this.fill.getValue()));
        this.speed = (Setting<Integer>)this.register(new Setting("Speed", (T)40, (T)1, (T)100, v -> this.rainbow.getValue() && !this.colorSync.getValue()));
        this.saturation = (Setting<Integer>)this.register(new Setting("Saturation", (T)65, (T)0, (T)100, v -> this.rainbow.getValue() && !this.colorSync.getValue()));
        this.brightness = (Setting<Integer>)this.register(new Setting("Brightness", (T)100, (T)0, (T)100, v -> this.rainbow.getValue() && !this.colorSync.getValue()));
        this.badPlayers = new ArrayList<PopChamContext>();
        this.setInstance();
    }
    
    public static PopChams getInstance() {
        if (PopChams.INSTANCE == null) {
            PopChams.INSTANCE = new PopChams();
        }
        return PopChams.INSTANCE;
    }
    
    private void setInstance() {
        PopChams.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onTotemPop(final TotemPopEvent event) {
        if (!this.still.getValue()) {
            this.badPlayers.removeIf(player -> player.getPlayer().equals((Object)event.getEntity()));
        }
        this.badPlayers.add(new PopChamContext(event.getEntity(), event.getEntity().getPrimaryHand()));
    }
    
    public void onTick() {
        this.badPlayers.removeIf(player -> player.getTime() > this.duration.getValue());
        this.badPlayers.forEach(PopChamContext::incrementTime);
    }
    
    public void onRender3D(final Render3DEvent e) {
        if (!this.still.getValue()) {
            return;
        }
        final double[] offset;
        final float factor;
        final double ascension;
        final RenderEntityModelEvent event;
        this.badPlayers.stream().filter(player -> player.getEvent() != null).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()).forEach(player -> {
            offset = player.getOffset();
            factor = (this.duration.getValue() - player.getTime()) / (float)this.duration.getValue();
            ascension = (this.heaven.getValue() ? ((factor - 1.0f) * this.ascension.getValue()) : 0.0);
            event = player.getEvent();
            GlStateManager.pushMatrix();
            GlStateManager.translate(offset[0], offset[1] - ascension, offset[2]);
            GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(event.entity.rotationYaw - event.headYaw, 0.0f, 1.0f, 0.0f);
            this.doThing(player.getEvent(), factor);
            GlStateManager.popMatrix();
        });
    }
    
    public void onRenderModel(final RenderEntityModelEvent event) {
        if (this.still.getValue()) {
            this.badPlayers.stream().filter(player -> player.getPlayer().equals((Object)event.entity) && player.getEvent() == null).forEach(player -> player.setEvent(event));
            return;
        }
        this.badPlayers.stream().filter(player -> player.getPlayer().equals((Object)event.entity)).findFirst().ifPresent(context -> {
            this.doThing(event, (this.duration.getValue() - context.getTime()) / (float)this.duration.getValue());
            event.setCanceled(true);
        });
    }
    
    private void doThing(final RenderEntityModelEvent event, final float factor) {
        if (event == null) {
            return;
        }
        if (this.fill.getValue()) {
            final int alpha = MathUtil.clamp((int)((this.fade.getValue() ? factor : 1.0f) * this.fAlpha.getValue()), 0, 255);
            final Color fColor = this.colorSync.getValue() ? new Color(Colors.INSTANCE.getCurrentColor().getRed(), Colors.INSTANCE.getCurrentColor().getGreen(), Colors.INSTANCE.getCurrentColor().getBlue(), alpha) : (this.rainbow.getValue() ? RenderUtil.getRainbowAlpha(this.speed.getValue() * 100, 0, this.saturation.getValue() / 100.0f, this.brightness.getValue() / 100.0f, alpha) : new Color(this.fRed.getValue(), this.fGreen.getValue(), this.fBlue.getValue(), alpha));
            GL11.glPushAttrib(1048575);
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glLineWidth(1.5f);
            GL11.glEnable(2960);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glEnable(10754);
            RenderUtil.setColor(fColor);
            event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            final Color vColor = new Color(this.vRed.getValue(), this.vGreen.getValue(), this.vBlue.getValue(), alpha);
            if (this.visColor.getValue() && !this.rainbow.getValue() && !this.colorSync.getValue()) {
                RenderUtil.setColor(vColor);
            }
            event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();
        }
        if (this.outline.getValue()) {
            final boolean fancyGraphics = PopChams.mc.gameSettings.fancyGraphics;
            final float gamma = PopChams.mc.gameSettings.gammaSetting;
            PopChams.mc.gameSettings.fancyGraphics = false;
            PopChams.mc.gameSettings.gammaSetting = 10000.0f;
            final int alpha2 = MathUtil.clamp((int)((this.fade.getValue() ? factor : 1.0f) * this.oAlpha.getValue()), 0, 255);
            final Color oColor = this.colorSync.getValue() ? new Color(Colors.INSTANCE.getCurrentColor().getRed(), Colors.INSTANCE.getCurrentColor().getGreen(), Colors.INSTANCE.getCurrentColor().getBlue(), alpha2) : (this.rainbow.getValue() ? RenderUtil.getRainbowAlpha(this.speed.getValue() * 100, 0, this.saturation.getValue() / 100.0f, this.brightness.getValue() / 100.0f, alpha2) : new Color(this.oRed.getValue(), this.oGreen.getValue(), this.oBlue.getValue(), alpha2));
            if (this.mode.getValue() == Mode.OUTLINE) {
                if (!this.fill.getValue() && !this.still.getValue()) {
                    event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
                }
                RenderUtil.renderOne(this.lineWidth.getValue());
                event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
                GlStateManager.glLineWidth((float)this.lineWidth.getValue());
                RenderUtil.renderTwo();
                event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
                GlStateManager.glLineWidth((float)this.lineWidth.getValue());
                RenderUtil.renderThree();
                RenderUtil.renderFour(oColor);
                event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
                GlStateManager.glLineWidth((float)this.lineWidth.getValue());
                RenderUtil.renderFive();
            }
            else {
                if (!this.fill.getValue() && !this.still.getValue()) {
                    event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
                }
                GL11.glPushMatrix();
                GL11.glPushAttrib(1048575);
                GL11.glPolygonMode(1032, 6913);
                GL11.glDisable(3553);
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                GL11.glEnable(2848);
                GL11.glEnable(3042);
                GlStateManager.blendFunc(770, 771);
                RenderUtil.setColor(oColor);
                GlStateManager.glLineWidth((float)this.lineWidth.getValue());
                event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
                GL11.glPopAttrib();
                GL11.glPopMatrix();
            }
            try {
                PopChams.mc.gameSettings.fancyGraphics = fancyGraphics;
                PopChams.mc.gameSettings.gammaSetting = gamma;
            }
            catch (Exception ex) {}
        }
    }
    
    static {
        PopChams.INSTANCE = new PopChams();
    }
    
    public enum Mode
    {
        OUTLINE, 
        WIREFRAME;
    }
    
    static class PopChamContext
    {
        private final EntityPlayer player;
        private final EnumHandSide hand;
        private int time;
        private RenderEntityModelEvent event;
        private double[] pos;
        
        public PopChamContext(final EntityPlayer player, final EnumHandSide hand) {
            this.player = player;
            this.hand = hand.opposite();
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
        
        public void setEvent(final RenderEntityModelEvent event) {
            if (this.event != null) {
                return;
            }
            final EntityOtherPlayerMP entity = new EntityOtherPlayerMP((World)PopChams.mc.world, new GameProfile(event.entity.getUniqueID(), "Cr33pyl3mon4de"));
            entity.copyLocationAndAnglesFrom(event.entity);
            entity.rotationYaw = event.entity.rotationYaw;
            entity.setSneaking(event.entity.isSneaking());
            if (event.entity.isSneaking()) {
                final EntityOtherPlayerMP entityOtherPlayerMP = entity;
                entityOtherPlayerMP.posY -= 0.1;
            }
            entity.setPrimaryHand(this.hand);
            final ModelPlayer player = new ModelPlayer(event.scale, !entity.getSkinType().equals("default"));
            player.setModelAttributes(event.modelBase);
            player.setRotationAngles(event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale, event.entity);
            this.event = new RenderEntityModelEvent(event.getStage(), (ModelBase)player, (Entity)entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale * 0.925f);
            this.pos = new double[] { this.event.entity.posX, this.event.entity.posY, this.event.entity.posZ };
        }
        
        public double[] getOffset() {
            if (this.pos != null) {
                return new double[] { this.pos[0] - PopChams.mc.getRenderManager().viewerPosX, this.pos[1] - PopChams.mc.getRenderManager().viewerPosY + 1.41, this.pos[2] - PopChams.mc.getRenderManager().viewerPosZ };
            }
            return new double[] { 0.0, 100.0, 0.0 };
        }
    }
}
