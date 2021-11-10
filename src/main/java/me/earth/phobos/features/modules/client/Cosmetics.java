



package me.earth.phobos.features.modules.client;

import me.earth.phobos.features.modules.*;
import net.minecraft.util.*;
import net.minecraftforge.client.event.*;
import me.earth.phobos.*;
import me.earth.phobos.util.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.math.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.model.*;
import net.minecraft.entity.player.*;
import java.util.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.entity.*;

public class Cosmetics extends Module
{
    public static Cosmetics INSTANCE;
    public final ModelCloutGoggles cloutGoggles;
    public final ModelSquidFlag flag;
    public final TopHatModel hatModel;
    public final GlassesModel glassesModel;
    public final SantaHatModel santaHatModel;
    public final ModelSquidLauncher squidLauncher;
    private final HatGlassesModel hatGlassesModel;
    private final ResourceLocation hatTexture;
    private final ResourceLocation glassesTexture;
    private final ResourceLocation santaHatTexture;
    private final ResourceLocation squidTexture;
    private final ResourceLocation cloutGoggleTexture;
    private final ResourceLocation squidLauncherTexture;
    
    public Cosmetics() {
        super("Cosmetics",  "Shows Cosmetics on phobos users.",  Category.CLIENT,  true,  false,  false);
        this.cloutGoggles = new ModelCloutGoggles();
        this.flag = new ModelSquidFlag();
        this.hatModel = new TopHatModel();
        this.glassesModel = new GlassesModel();
        this.santaHatModel = new SantaHatModel();
        this.squidLauncher = new ModelSquidLauncher();
        this.hatGlassesModel = new HatGlassesModel();
        this.hatTexture = new ResourceLocation("textures/tophat.png");
        this.glassesTexture = new ResourceLocation("textures/sunglasses.png");
        this.santaHatTexture = new ResourceLocation("textures/santahat.png");
        this.squidTexture = new ResourceLocation("textures/squid.png");
        this.cloutGoggleTexture = new ResourceLocation("textures/cloutgoggles.png");
        this.squidLauncherTexture = new ResourceLocation("textures/squidlauncher.png");
        Cosmetics.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onRenderPlayer(final RenderPlayerEvent.Post event) {
        if (Phobos.cosmeticsManager.hasCosmetics(event.getEntityPlayer()) || EntityUtil.isFakePlayer(event.getEntityPlayer())) {
            return;
        }
        for (final ModelBase model : Phobos.cosmeticsManager.getRenderModels(event.getEntityPlayer())) {
            GlStateManager.pushMatrix();
            Cosmetics.mc.getRenderManager();
            GlStateManager.translate(event.getX(),  event.getY(),  event.getZ());
            final double scale = 1.0;
            final double rotate = this.interpolate(event.getEntityPlayer().prevRotationYawHead,  event.getEntityPlayer().rotationYawHead,  event.getPartialRenderTick());
            final double rotate2 = this.interpolate(event.getEntityPlayer().prevRotationPitch,  event.getEntityPlayer().rotationPitch,  event.getPartialRenderTick());
            final double rotate3 = event.getEntityPlayer().isSneaking() ? 22.0 : 0.0;
            final float limbSwingAmount = this.interpolate(event.getEntityPlayer().prevLimbSwingAmount,  event.getEntityPlayer().limbSwingAmount,  event.getPartialRenderTick());
            final float rotate4 = MathHelper.cos(event.getEntityPlayer().limbSwing * 0.6662f + 3.1415927f) * 1.4f * limbSwingAmount;
            GL11.glScaled(-scale,  -scale,  scale);
            GL11.glTranslated(0.0,  -(event.getEntityPlayer().height - (event.getEntityPlayer().isSneaking() ? 0.25 : 0.0) - 0.38) / scale,  0.0);
            GL11.glRotated(180.0 + rotate,  0.0,  1.0,  0.0);
            if (!(model instanceof ModelSquidLauncher)) {
                GL11.glRotated(rotate2,  1.0,  0.0,  0.0);
            }
            if (model instanceof ModelSquidLauncher) {
                GL11.glRotated(rotate3,  1.0,  0.0,  0.0);
            }
            GlStateManager.translate(0.0,  -0.45,  0.0);
            if (model instanceof ModelSquidLauncher) {
                GlStateManager.translate(0.15,  1.3,  0.0);
                for (final ModelRenderer renderer : this.squidLauncher.boxList) {
                    renderer.rotateAngleX = rotate4;
                }
            }
            if (model instanceof TopHatModel) {
                Cosmetics.mc.getTextureManager().bindTexture(this.hatTexture);
                this.hatModel.render(event.getEntity(),  0.0f,  0.0f,  -0.1f,  0.0f,  0.0f,  0.0625f);
                Cosmetics.mc.getTextureManager().deleteTexture(this.hatTexture);
            }
            else if (model instanceof GlassesModel) {
                if (event.getEntityPlayer().isWearing(EnumPlayerModelParts.HAT)) {
                    Cosmetics.mc.getTextureManager().bindTexture(this.glassesTexture);
                    this.hatGlassesModel.render(event.getEntity(),  0.0f,  0.0f,  -0.1f,  0.0f,  0.0f,  0.0625f);
                    Cosmetics.mc.getTextureManager().deleteTexture(this.glassesTexture);
                }
                else {
                    Cosmetics.mc.getTextureManager().bindTexture(this.glassesTexture);
                    this.glassesModel.render(event.getEntity(),  0.0f,  0.0f,  -0.1f,  0.0f,  0.0f,  0.0625f);
                    Cosmetics.mc.getTextureManager().deleteTexture(this.glassesTexture);
                }
            }
            else if (model instanceof SantaHatModel) {
                Cosmetics.mc.getTextureManager().bindTexture(this.santaHatTexture);
                this.santaHatModel.render(event.getEntity(),  0.0f,  0.0f,  -0.1f,  0.0f,  0.0f,  0.0625f);
                Cosmetics.mc.getTextureManager().deleteTexture(this.santaHatTexture);
            }
            else if (model instanceof ModelCloutGoggles) {
                Cosmetics.mc.getTextureManager().bindTexture(this.cloutGoggleTexture);
                this.cloutGoggles.render(event.getEntity(),  0.0f,  0.0f,  -0.1f,  0.0f,  0.0f,  0.0625f);
                Cosmetics.mc.getTextureManager().deleteTexture(this.cloutGoggleTexture);
            }
            else if (model instanceof ModelSquidFlag) {
                Cosmetics.mc.getTextureManager().bindTexture(this.squidTexture);
                this.flag.render(event.getEntity(),  0.0f,  0.0f,  -0.1f,  0.0f,  0.0f,  0.0625f);
                Cosmetics.mc.getTextureManager().deleteTexture(this.squidTexture);
            }
            else if (model instanceof ModelSquidLauncher) {
                Cosmetics.mc.getTextureManager().bindTexture(this.squidLauncherTexture);
                this.squidLauncher.render(event.getEntity(),  0.0f,  0.0f,  -0.1f,  0.0f,  0.0f,  0.0325f);
                Cosmetics.mc.getTextureManager().deleteTexture(this.squidLauncherTexture);
            }
            GlStateManager.popMatrix();
        }
    }
    
    public float interpolate(final float yaw1,  final float yaw2,  final float percent) {
        float rotation = (yaw1 + (yaw2 - yaw1) * percent) % 360.0f;
        if (rotation < 0.0f) {
            rotation += 360.0f;
        }
        return rotation;
    }
    
    public static class ModelCloutGoggles extends ModelBase
    {
        public ModelRenderer leftGlass;
        public ModelRenderer topLeftFrame;
        public ModelRenderer bottomLeftFrame;
        public ModelRenderer leftLeftFrame;
        public ModelRenderer rightLeftFrame;
        public ModelRenderer rightGlass;
        public ModelRenderer topRightFrame;
        public ModelRenderer bottomLeftFrame_1;
        public ModelRenderer leftRightFrame;
        public ModelRenderer rightRightFrame;
        public ModelRenderer leftEar;
        public ModelRenderer rightEar;
        
        public ModelCloutGoggles() {
            this.textureWidth = 64;
            this.textureHeight = 32;
            (this.rightLeftFrame = new ModelRenderer((ModelBase)this,  18,  0)).setRotationPoint(-3.0f,  3.0f,  -4.0f);
            this.rightLeftFrame.addBox(0.0f,  2.0f,  0.0f,  2,  1,  1,  0.0f);
            (this.bottomLeftFrame_1 = new ModelRenderer((ModelBase)this,  26,  5)).setRotationPoint(-3.0f,  3.0f,  -4.0f);
            this.bottomLeftFrame_1.addBox(4.0f,  2.0f,  0.0f,  2,  1,  1,  0.0f);
            (this.leftLeftFrame = new ModelRenderer((ModelBase)this,  10,  5)).setRotationPoint(-3.0f,  3.0f,  -4.0f);
            this.leftLeftFrame.addBox(2.0f,  0.0f,  0.0f,  1,  2,  1,  0.0f);
            (this.rightGlass = new ModelRenderer((ModelBase)this,  18,  5)).setRotationPoint(-3.0f,  3.0f,  -4.0f);
            this.rightGlass.addBox(4.0f,  0.0f,  0.0f,  2,  2,  1,  0.0f);
            (this.rightRightFrame = new ModelRenderer((ModelBase)this,  10,  11)).setRotationPoint(3.0f,  3.0f,  -4.0f);
            this.rightRightFrame.addBox(0.0f,  0.0f,  0.0f,  1,  2,  1,  0.0f);
            (this.leftEar = new ModelRenderer((ModelBase)this,  18,  11)).setRotationPoint(-3.0f,  3.0f,  -4.0f);
            this.leftEar.addBox(-1.2f,  0.0f,  0.0f,  1,  1,  3,  0.0f);
            (this.topRightFrame = new ModelRenderer((ModelBase)this,  26,  0)).setRotationPoint(1.0f,  3.0f,  -4.0f);
            this.topRightFrame.addBox(0.0f,  -1.0f,  0.0f,  2,  1,  1,  0.0f);
            (this.topLeftFrame = new ModelRenderer((ModelBase)this,  0,  5)).setRotationPoint(-3.0f,  3.0f,  -4.0f);
            this.topLeftFrame.addBox(-1.0f,  0.0f,  0.0f,  1,  2,  1,  0.0f);
            (this.rightEar = new ModelRenderer((ModelBase)this,  28,  11)).setRotationPoint(-3.0f,  3.0f,  -4.0f);
            this.rightEar.addBox(6.2f,  0.0f,  0.0f,  1,  1,  3,  0.0f);
            (this.leftGlass = new ModelRenderer((ModelBase)this,  0,  0)).setRotationPoint(-3.0f,  3.0f,  -4.0f);
            this.leftGlass.addBox(0.0f,  0.0f,  0.0f,  2,  2,  1,  0.0f);
            (this.bottomLeftFrame = new ModelRenderer((ModelBase)this,  10,  0)).setRotationPoint(-3.0f,  3.0f,  -4.0f);
            this.bottomLeftFrame.addBox(0.0f,  -1.0f,  0.0f,  2,  1,  1,  0.0f);
            (this.leftRightFrame = new ModelRenderer((ModelBase)this,  0,  11)).setRotationPoint(-3.0f,  3.0f,  -4.0f);
            this.leftRightFrame.addBox(3.0f,  0.0f,  0.0f,  1,  2,  1,  0.0f);
        }
        
        public void render(final Entity entity,  final float f,  final float f1,  final float f2,  final float f3,  final float f4,  final float f5) {
            this.rightLeftFrame.render(f5);
            this.bottomLeftFrame_1.render(f5);
            this.leftLeftFrame.render(f5);
            this.rightGlass.render(f5);
            this.rightRightFrame.render(f5);
            this.leftEar.render(f5);
            this.topRightFrame.render(f5);
            this.topLeftFrame.render(f5);
            this.rightEar.render(f5);
            this.leftGlass.render(f5);
            this.bottomLeftFrame.render(f5);
            this.leftRightFrame.render(f5);
        }
        
        public void setRotateAngle(final ModelRenderer modelRenderer,  final float x,  final float y,  final float z) {
            modelRenderer.rotateAngleX = x;
            modelRenderer.rotateAngleY = y;
            modelRenderer.rotateAngleZ = z;
        }
    }
    
    public static class ModelSquidLauncher extends ModelBase
    {
        public ModelRenderer barrel;
        public ModelRenderer squid;
        public ModelRenderer secondBarrel;
        public ModelRenderer barrelSide1;
        public ModelRenderer barrelSide2;
        public ModelRenderer barrelSide3;
        public ModelRenderer barrelSide4;
        public ModelRenderer stock;
        public ModelRenderer stockEnd;
        public ModelRenderer trigger;
        
        public ModelSquidLauncher() {
            this.textureWidth = 64;
            this.textureHeight = 32;
            (this.barrelSide4 = new ModelRenderer((ModelBase)this,  0,  0)).setRotationPoint(0.5f,  0.0f,  0.0f);
            this.barrelSide4.addBox(0.0f,  -2.0f,  0.2f,  4,  5,  1,  0.0f);
            this.setRotateAngle(this.barrelSide4,  0.091106184f,  0.0f,  0.0f);
            (this.stock = new ModelRenderer((ModelBase)this,  0,  24)).setRotationPoint(0.0f,  0.0f,  0.0f);
            this.stock.addBox(1.5f,  3.0f,  1.5f,  2,  4,  2,  0.0f);
            (this.squid = new ModelRenderer((ModelBase)this,  0,  16)).setRotationPoint(0.0f,  0.0f,  0.0f);
            this.squid.addBox(1.2f,  -11.5f,  0.8f,  3,  4,  3,  0.0f);
            this.setRotateAngle(this.squid,  0.0f,  -0.091106184f,  0.0f);
            (this.barrelSide2 = new ModelRenderer((ModelBase)this,  18,  14)).setRotationPoint(0.0f,  0.0f,  0.0f);
            this.barrelSide2.addBox(3.8f,  -2.5f,  0.5f,  1,  5,  4,  0.0f);
            this.setRotateAngle(this.barrelSide2,  0.0f,  0.0f,  0.091106184f);
            (this.secondBarrel = new ModelRenderer((ModelBase)this,  32,  14)).setRotationPoint(0.0f,  0.0f,  0.0f);
            this.secondBarrel.addBox(0.5f,  -2.0f,  0.5f,  4,  5,  4,  0.0f);
            (this.stockEnd = new ModelRenderer((ModelBase)this,  18,  26)).setRotationPoint(0.0f,  0.0f,  0.0f);
            this.stockEnd.addBox(2.0f,  7.0f,  1.5f,  1,  1,  4,  0.0f);
            (this.barrelSide1 = new ModelRenderer((ModelBase)this,  18,  14)).setRotationPoint(0.0f,  0.0f,  0.0f);
            this.barrelSide1.addBox(0.2f,  -2.0f,  0.5f,  1,  5,  4,  0.0f);
            this.setRotateAngle(this.barrelSide1,  0.0f,  0.0f,  -0.091106184f);
            (this.barrelSide3 = new ModelRenderer((ModelBase)this,  0,  0)).setRotationPoint(0.0f,  0.0f,  0.0f);
            this.barrelSide3.addBox(0.5f,  -2.5f,  3.8f,  4,  5,  1,  0.0f);
            this.setRotateAngle(this.barrelSide3,  -0.091106184f,  0.0f,  0.0f);
            (this.trigger = new ModelRenderer((ModelBase)this,  40,  0)).setRotationPoint(0.0f,  0.0f,  0.0f);
            this.trigger.addBox(12.0f,  6.6f,  5.4f,  1,  1,  1,  0.0f);
            (this.barrel = new ModelRenderer((ModelBase)this,  18,  0)).setRotationPoint(0.0f,  0.0f,  0.0f);
            this.barrel.addBox(0.0f,  -8.0f,  0.0f,  5,  6,  5,  0.0f);
            this.boxList.add(this.barrel);
            this.boxList.add(this.squid);
            this.boxList.add(this.secondBarrel);
            this.boxList.add(this.barrelSide1);
            this.boxList.add(this.barrelSide2);
            this.boxList.add(this.barrelSide3);
            this.boxList.add(this.barrelSide4);
            this.boxList.add(this.stock);
            this.boxList.add(this.stockEnd);
            this.boxList.add(this.trigger);
        }
        
        public void render(final Entity entity,  final float f,  final float f1,  final float f2,  final float f3,  final float f4,  final float f5) {
            this.stock.render(f5);
            this.barrelSide1.render(f5);
            this.stockEnd.render(f5);
            this.secondBarrel.render(f5);
            this.barrelSide3.render(f5);
            this.squid.render(f5);
            this.barrelSide4.render(f5);
            this.barrel.render(f5);
            this.barrelSide2.render(f5);
            GlStateManager.pushMatrix();
            GlStateManager.translate(this.trigger.offsetX,  this.trigger.offsetY,  this.trigger.offsetZ);
            GlStateManager.translate(this.trigger.rotationPointX * f5,  this.trigger.rotationPointY * f5,  this.trigger.rotationPointZ * f5);
            GlStateManager.scale(0.2,  1.0,  0.8);
            GlStateManager.translate(-this.trigger.offsetX,  -this.trigger.offsetY,  -this.trigger.offsetZ);
            GlStateManager.translate(-this.trigger.rotationPointX * f5,  -this.trigger.rotationPointY * f5,  -this.trigger.rotationPointZ * f5);
            this.trigger.render(f5);
            GlStateManager.popMatrix();
        }
        
        public void setRotateAngle(final ModelRenderer modelRenderer,  final float x,  final float y,  final float z) {
            modelRenderer.rotateAngleX = x;
            modelRenderer.rotateAngleY = y;
            modelRenderer.rotateAngleZ = z;
        }
    }
    
    public static class ModelSquidFlag extends ModelBase
    {
        public ModelRenderer flag;
        
        public ModelSquidFlag() {
            this.textureWidth = 64;
            this.textureHeight = 32;
            (this.flag = new ModelRenderer((ModelBase)this,  0,  0)).setRotationPoint(0.0f,  0.0f,  0.0f);
            this.flag.addBox(-5.0f,  -16.0f,  0.0f,  10,  16,  1,  0.0f);
        }
        
        public void render(final Entity entity,  final float f,  final float f1,  final float f2,  final float f3,  final float f4,  final float f5) {
            this.flag.render(f5);
        }
        
        public void setRotateAngle(final ModelRenderer modelRenderer,  final float x,  final float y,  final float z) {
            modelRenderer.rotateAngleX = x;
            modelRenderer.rotateAngleY = y;
            modelRenderer.rotateAngleZ = z;
        }
    }
    
    public static class SantaHatModel extends ModelBase
    {
        public ModelRenderer baseLayer;
        public ModelRenderer baseRedLayer;
        public ModelRenderer midRedLayer;
        public ModelRenderer topRedLayer;
        public ModelRenderer lastRedLayer;
        public ModelRenderer realFinalLastLayer;
        public ModelRenderer whiteLayer;
        
        public SantaHatModel() {
            this.textureWidth = 64;
            this.textureHeight = 32;
            (this.topRedLayer = new ModelRenderer((ModelBase)this,  46,  0)).setRotationPoint(0.5f,  -8.4f,  -1.5f);
            this.topRedLayer.addBox(0.0f,  0.0f,  0.0f,  3,  2,  3,  0.0f);
            this.setRotateAngle(this.topRedLayer,  0.0f,  0.0f,  0.5009095f);
            (this.baseLayer = new ModelRenderer((ModelBase)this,  0,  0)).setRotationPoint(-4.0f,  -1.0f,  -4.0f);
            this.baseLayer.addBox(0.0f,  0.0f,  0.0f,  8,  2,  8,  0.0f);
            (this.midRedLayer = new ModelRenderer((ModelBase)this,  28,  0)).setRotationPoint(-1.2f,  -6.8f,  -2.0f);
            this.midRedLayer.addBox(0.0f,  0.0f,  0.0f,  4,  3,  4,  0.0f);
            this.setRotateAngle(this.midRedLayer,  0.0f,  0.0f,  0.22759093f);
            (this.realFinalLastLayer = new ModelRenderer((ModelBase)this,  46,  8)).setRotationPoint(4.0f,  -10.4f,  0.0f);
            this.realFinalLastLayer.addBox(0.0f,  0.0f,  0.0f,  1,  3,  1,  0.0f);
            this.setRotateAngle(this.realFinalLastLayer,  0.0f,  0.0f,  1.0016445f);
            (this.lastRedLayer = new ModelRenderer((ModelBase)this,  34,  8)).setRotationPoint(2.0f,  -9.4f,  0.0f);
            this.lastRedLayer.addBox(0.0f,  0.0f,  0.0f,  2,  2,  2,  0.0f);
            this.setRotateAngle(this.lastRedLayer,  0.0f,  0.0f,  0.8196066f);
            (this.whiteLayer = new ModelRenderer((ModelBase)this,  0,  22)).setRotationPoint(4.1f,  -9.7f,  -0.5f);
            this.whiteLayer.addBox(0.0f,  0.0f,  0.0f,  2,  2,  2,  0.0f);
            this.setRotateAngle(this.whiteLayer,  -0.091106184f,  0.0f,  0.18203785f);
            (this.baseRedLayer = new ModelRenderer((ModelBase)this,  0,  11)).setRotationPoint(-3.0f,  -4.0f,  -3.0f);
            this.baseRedLayer.addBox(0.0f,  0.0f,  0.0f,  6,  3,  6,  0.0f);
            this.setRotateAngle(this.baseRedLayer,  0.0f,  0.0f,  0.045553092f);
        }
        
        public void render(final Entity entity,  final float f,  final float f1,  final float f2,  final float f3,  final float f4,  final float f5) {
            this.topRedLayer.render(f5);
            this.baseLayer.render(f5);
            this.midRedLayer.render(f5);
            this.realFinalLastLayer.render(f5);
            this.lastRedLayer.render(f5);
            this.whiteLayer.render(f5);
            this.baseRedLayer.render(f5);
        }
        
        public void setRotateAngle(final ModelRenderer modelRenderer,  final float x,  final float y,  final float z) {
            modelRenderer.rotateAngleX = x;
            modelRenderer.rotateAngleY = y;
            modelRenderer.rotateAngleZ = z;
        }
    }
    
    public static class HatGlassesModel extends ModelBase
    {
        public ModelRenderer firstLeftFrame;
        public ModelRenderer firstRightFrame;
        public ModelRenderer centerBar;
        public ModelRenderer farLeftBar;
        public ModelRenderer farRightBar;
        public ModelRenderer leftEar;
        public ModelRenderer rightEar;
        
        public HatGlassesModel() {
            this.textureWidth = 64;
            this.textureHeight = 64;
            (this.farLeftBar = new ModelRenderer((ModelBase)this,  0,  13)).setRotationPoint(-4.0f,  3.5f,  -5.0f);
            this.farLeftBar.addBox(0.0f,  0.0f,  0.0f,  1,  1,  1,  0.0f);
            (this.rightEar = new ModelRenderer((ModelBase)this,  10,  0)).setRotationPoint(3.2f,  3.5f,  -5.0f);
            this.rightEar.addBox(0.0f,  0.0f,  0.0f,  1,  1,  3,  0.0f);
            (this.centerBar = new ModelRenderer((ModelBase)this,  0,  9)).setRotationPoint(-1.0f,  3.5f,  -5.0f);
            this.centerBar.addBox(0.0f,  0.0f,  0.0f,  2,  1,  1,  0.0f);
            (this.firstLeftFrame = new ModelRenderer((ModelBase)this,  0,  0)).setRotationPoint(-3.0f,  3.0f,  -5.0f);
            this.firstLeftFrame.addBox(0.0f,  0.0f,  0.0f,  2,  2,  1,  0.0f);
            (this.firstRightFrame = new ModelRenderer((ModelBase)this,  0,  5)).setRotationPoint(1.0f,  3.0f,  -5.0f);
            this.firstRightFrame.addBox(0.0f,  0.0f,  0.0f,  2,  2,  1,  0.0f);
            (this.leftEar = new ModelRenderer((ModelBase)this,  20,  0)).setRotationPoint(-4.2f,  3.5f,  -5.0f);
            this.leftEar.addBox(0.0f,  0.0f,  0.0f,  1,  1,  3,  0.0f);
            (this.farRightBar = new ModelRenderer((ModelBase)this,  0,  17)).setRotationPoint(3.0f,  3.5f,  -5.0f);
            this.farRightBar.addBox(0.0f,  0.0f,  0.0f,  1,  1,  1,  0.0f);
        }
        
        public void render(final Entity entity,  final float f,  final float f1,  final float f2,  final float f3,  final float f4,  final float f5) {
            this.farLeftBar.render(f5);
            this.rightEar.render(f5);
            this.centerBar.render(f5);
            this.firstLeftFrame.render(f5);
            this.firstRightFrame.render(f5);
            this.leftEar.render(f5);
            this.farRightBar.render(f5);
        }
        
        public void setRotateAngle(final ModelRenderer modelRenderer,  final float x,  final float y,  final float z) {
            modelRenderer.rotateAngleX = x;
            modelRenderer.rotateAngleY = y;
            modelRenderer.rotateAngleZ = z;
        }
    }
    
    public static class GlassesModel extends ModelBase
    {
        public ModelRenderer firstLeftFrame;
        public ModelRenderer firstRightFrame;
        public ModelRenderer centerBar;
        public ModelRenderer farLeftBar;
        public ModelRenderer farRightBar;
        public ModelRenderer leftEar;
        public ModelRenderer rightEar;
        
        public GlassesModel() {
            this.textureWidth = 64;
            this.textureHeight = 64;
            (this.farLeftBar = new ModelRenderer((ModelBase)this,  0,  13)).setRotationPoint(-4.0f,  3.5f,  -4.0f);
            this.farLeftBar.addBox(0.0f,  0.0f,  0.0f,  1,  1,  1,  0.0f);
            (this.rightEar = new ModelRenderer((ModelBase)this,  10,  0)).setRotationPoint(3.2f,  3.5f,  -4.0f);
            this.rightEar.addBox(0.0f,  0.0f,  0.0f,  1,  1,  3,  0.0f);
            (this.centerBar = new ModelRenderer((ModelBase)this,  0,  9)).setRotationPoint(-1.0f,  3.5f,  -4.0f);
            this.centerBar.addBox(0.0f,  0.0f,  0.0f,  2,  1,  1,  0.0f);
            (this.firstLeftFrame = new ModelRenderer((ModelBase)this,  0,  0)).setRotationPoint(-3.0f,  3.0f,  -4.0f);
            this.firstLeftFrame.addBox(0.0f,  0.0f,  0.0f,  2,  2,  1,  0.0f);
            (this.firstRightFrame = new ModelRenderer((ModelBase)this,  0,  5)).setRotationPoint(1.0f,  3.0f,  -4.0f);
            this.firstRightFrame.addBox(0.0f,  0.0f,  0.0f,  2,  2,  1,  0.0f);
            (this.leftEar = new ModelRenderer((ModelBase)this,  20,  0)).setRotationPoint(-4.2f,  3.5f,  -4.0f);
            this.leftEar.addBox(0.0f,  0.0f,  0.0f,  1,  1,  3,  0.0f);
            (this.farRightBar = new ModelRenderer((ModelBase)this,  0,  17)).setRotationPoint(3.0f,  3.5f,  -4.0f);
            this.farRightBar.addBox(0.0f,  0.0f,  0.0f,  1,  1,  1,  0.0f);
        }
        
        public void render(final Entity entity,  final float f,  final float f1,  final float f2,  final float f3,  final float f4,  final float f5) {
            this.farLeftBar.render(f5);
            this.rightEar.render(f5);
            this.centerBar.render(f5);
            this.firstLeftFrame.render(f5);
            this.firstRightFrame.render(f5);
            this.leftEar.render(f5);
            this.farRightBar.render(f5);
        }
        
        public void setRotateAngle(final ModelRenderer modelRenderer,  final float x,  final float y,  final float z) {
            modelRenderer.rotateAngleX = x;
            modelRenderer.rotateAngleY = y;
            modelRenderer.rotateAngleZ = z;
        }
    }
    
    public static class TopHatModel extends ModelBase
    {
        public ModelRenderer bottom;
        public ModelRenderer top;
        
        public TopHatModel() {
            this.textureWidth = 64;
            this.textureHeight = 32;
            (this.top = new ModelRenderer((ModelBase)this,  0,  10)).addBox(0.0f,  0.0f,  0.0f,  4,  10,  4,  0.0f);
            this.top.setRotationPoint(-2.0f,  -11.0f,  -2.0f);
            (this.bottom = new ModelRenderer((ModelBase)this,  0,  0)).addBox(0.0f,  0.0f,  0.0f,  8,  1,  8,  0.0f);
            this.bottom.setRotationPoint(-4.0f,  -1.0f,  -4.0f);
        }
        
        public void render(final Entity entity,  final float f,  final float f1,  final float f2,  final float f3,  final float f4,  final float f5) {
            this.top.render(f5);
            this.bottom.render(f5);
        }
        
        public void setRotateAngle(final ModelRenderer modelRenderer,  final float x,  final float y,  final float z) {
            modelRenderer.rotateAngleX = x;
            modelRenderer.rotateAngleY = y;
            modelRenderer.rotateAngleZ = z;
        }
    }
}
