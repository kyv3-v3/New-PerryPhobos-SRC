/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.RenderItem
 *  net.minecraft.client.renderer.block.model.IBakedModel
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms$TransformType
 *  net.minecraft.client.renderer.entity.RenderEntityItem
 *  net.minecraft.client.renderer.texture.TextureMap
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraftforge.client.ForgeHooksClient
 *  org.lwjgl.opengl.GL11
 */
package me.earth.phobos.mixin.mixins;

import java.util.Random;
import me.earth.phobos.features.modules.render.ItemPhysics;
import me.earth.phobos.mixin.mixins.MixinRenderer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={RenderEntityItem.class})
public abstract class MixinRenderEntityItem
extends MixinRenderer<EntityItem> {
    private final Minecraft mc = Minecraft.func_71410_x();
    @Shadow
    @Final
    private RenderItem field_177080_a;
    @Shadow
    @Final
    private Random field_177079_e;
    private long tick;

    @Shadow
    protected abstract int func_177078_a(ItemStack var1);

    @Shadow
    public abstract boolean shouldSpreadItems();

    @Shadow
    public abstract boolean shouldBob();

    @Shadow
    protected abstract ResourceLocation func_110775_a(EntityItem var1);

    private double formPositive(float rotationPitch) {
        return rotationPitch > 0.0f ? (double)rotationPitch : (double)(-rotationPitch);
    }

    @Overwrite
    private int func_177077_a(EntityItem itemIn, double x, double y, double z, float p_177077_8_, IBakedModel p_177077_9_) {
        if (ItemPhysics.INSTANCE.isEnabled()) {
            ItemStack itemstack = itemIn.func_92059_d();
            itemstack.func_77973_b();
            boolean flag = p_177077_9_.func_177555_b();
            int i = this.func_177078_a(itemstack);
            float f2 = 0.0f;
            GlStateManager.func_179109_b((float)((float)x), (float)((float)y + 0.0f + 0.1f), (float)((float)z));
            float f3 = 0.0f;
            if (flag || this.mc.func_175598_ae().field_78733_k != null && this.mc.func_175598_ae().field_78733_k.field_74347_j) {
                GlStateManager.func_179114_b((float)f3, (float)0.0f, (float)1.0f, (float)0.0f);
            }
            if (!flag) {
                f3 = -0.0f * (float)(i - 1) * 0.5f;
                float f4 = -0.0f * (float)(i - 1) * 0.5f;
                float f5 = -0.046875f * (float)(i - 1) * 0.5f;
                GlStateManager.func_179109_b((float)f3, (float)f4, (float)f5);
            }
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            return i;
        }
        ItemStack itemstack = itemIn.func_92059_d();
        itemstack.func_77973_b();
        boolean flag = p_177077_9_.func_177556_c();
        int i = this.func_177078_a(itemstack);
        float f1 = this.shouldBob() ? MathHelper.func_76126_a((float)(((float)itemIn.func_174872_o() + p_177077_8_) / 10.0f + itemIn.field_70290_d)) * 0.1f + 0.1f : 0.0f;
        float f2 = p_177077_9_.func_177552_f().func_181688_b((ItemCameraTransforms.TransformType)ItemCameraTransforms.TransformType.GROUND).field_178363_d.y;
        GlStateManager.func_179109_b((float)((float)x), (float)((float)y + f1 + 0.25f * f2), (float)((float)z));
        if (flag || this.field_76990_c.field_78733_k != null) {
            float f3 = (((float)itemIn.func_174872_o() + p_177077_8_) / 20.0f + itemIn.field_70290_d) * 57.295776f;
            GlStateManager.func_179114_b((float)f3, (float)0.0f, (float)1.0f, (float)0.0f);
        }
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        return i;
    }

    @Overwrite
    public void func_76986_a(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (ItemPhysics.INSTANCE.isEnabled()) {
            double rotation = (double)(System.nanoTime() - this.tick) / 3000000.0;
            if (!this.mc.field_71415_G) {
                rotation = 0.0;
            }
            ItemStack itemstack = entity.func_92059_d();
            itemstack.func_77973_b();
            this.field_177079_e.setSeed(187L);
            this.mc.func_175598_ae().field_78724_e.func_110577_a(TextureMap.field_110575_b);
            this.mc.func_175598_ae().field_78724_e.func_110581_b(TextureMap.field_110575_b).func_174936_b(false, false);
            GlStateManager.func_179091_B();
            GlStateManager.func_179092_a((int)516, (float)0.1f);
            GlStateManager.func_179147_l();
            GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
            GlStateManager.func_179094_E();
            IBakedModel ibakedmodel = this.field_177080_a.func_175037_a().func_178089_a(itemstack);
            int i = this.func_177077_a(entity, x, y, z, partialTicks, ibakedmodel);
            BlockPos blockpos = new BlockPos((Entity)entity);
            if (entity.field_70125_A > 360.0f) {
                entity.field_70125_A = 0.0f;
            }
            if (!(Double.isNaN(entity.field_70165_t) || Double.isNaN(entity.field_70163_u) || Double.isNaN(entity.field_70161_v) || entity.field_70170_p == null)) {
                if (entity.field_70122_E) {
                    if (entity.field_70125_A != 0.0f && entity.field_70125_A != 90.0f && entity.field_70125_A != 180.0f && entity.field_70125_A != 270.0f) {
                        double d0 = this.formPositive(entity.field_70125_A);
                        double d2 = this.formPositive(entity.field_70125_A - 90.0f);
                        double d3 = this.formPositive(entity.field_70125_A - 180.0f);
                        double d4 = this.formPositive(entity.field_70125_A - 270.0f);
                        if (d0 <= d2 && d0 <= d3 && d0 <= d4) {
                            entity.field_70125_A = entity.field_70125_A < 0.0f ? (entity.field_70125_A += (float)rotation) : (entity.field_70125_A -= (float)rotation);
                        }
                        if (d2 < d0 && d2 <= d3 && d2 <= d4) {
                            entity.field_70125_A = entity.field_70125_A - 90.0f < 0.0f ? (entity.field_70125_A += (float)rotation) : (entity.field_70125_A -= (float)rotation);
                        }
                        if (d3 < d2 && d3 < d0 && d3 <= d4) {
                            entity.field_70125_A = entity.field_70125_A - 180.0f < 0.0f ? (entity.field_70125_A += (float)rotation) : (entity.field_70125_A -= (float)rotation);
                        }
                        if (d4 < d2 && d4 < d3 && d4 < d0) {
                            entity.field_70125_A = entity.field_70125_A - 270.0f < 0.0f ? (entity.field_70125_A += (float)rotation) : (entity.field_70125_A -= (float)rotation);
                        }
                    }
                } else {
                    BlockPos blockpos2 = new BlockPos((Entity)entity);
                    blockpos2.func_177982_a(0, 1, 0);
                    Material material = entity.field_70170_p.func_180495_p(blockpos2).func_185904_a();
                    Material material2 = entity.field_70170_p.func_180495_p(blockpos).func_185904_a();
                    boolean flag2 = entity.func_70055_a(Material.field_151586_h);
                    boolean flag3 = entity.func_70090_H();
                    entity.field_70125_A = flag2 | material == Material.field_151586_h | material2 == Material.field_151586_h | flag3 ? (entity.field_70125_A += (float)(rotation / 4.0)) : (entity.field_70125_A += (float)(rotation * 2.0));
                }
            }
            GL11.glRotatef((float)entity.field_70177_z, (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glRotatef((float)(entity.field_70125_A + 90.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            for (int j = 0; j < i; ++j) {
                if (ibakedmodel.func_177555_b()) {
                    GlStateManager.func_179094_E();
                    GlStateManager.func_179152_a((float)ItemPhysics.INSTANCE.Scaling.getValue().floatValue(), (float)ItemPhysics.INSTANCE.Scaling.getValue().floatValue(), (float)ItemPhysics.INSTANCE.Scaling.getValue().floatValue());
                    this.field_177080_a.func_180454_a(itemstack, ibakedmodel);
                    GlStateManager.func_179121_F();
                    continue;
                }
                GlStateManager.func_179094_E();
                if (j > 0 && this.shouldSpreadItems()) {
                    GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)(0.046875f * (float)j));
                }
                this.field_177080_a.func_180454_a(itemstack, ibakedmodel);
                if (!this.shouldSpreadItems()) {
                    GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)0.046875f);
                }
                GlStateManager.func_179121_F();
            }
            GlStateManager.func_179121_F();
            GlStateManager.func_179101_C();
            GlStateManager.func_179084_k();
            this.mc.func_175598_ae().field_78724_e.func_110577_a(TextureMap.field_110575_b);
            this.mc.func_175598_ae().field_78724_e.func_110581_b(TextureMap.field_110575_b).func_174935_a();
            return;
        }
        ItemStack itemstack = entity.func_92059_d();
        int i = itemstack.func_190926_b() ? 187 : Item.func_150891_b((Item)itemstack.func_77973_b()) + itemstack.func_77960_j();
        this.field_177079_e.setSeed(i);
        boolean flag = false;
        if (this.func_180548_c(entity)) {
            this.field_76990_c.field_78724_e.func_110581_b(this.func_110775_a(entity)).func_174936_b(false, false);
            flag = true;
        }
        GlStateManager.func_179091_B();
        GlStateManager.func_179092_a((int)516, (float)0.1f);
        GlStateManager.func_179147_l();
        RenderHelper.func_74519_b();
        GlStateManager.func_187428_a((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.func_179094_E();
        IBakedModel ibakedmodel = this.field_177080_a.func_184393_a(itemstack, entity.field_70170_p, null);
        int j = this.func_177077_a(entity, x, y, z, partialTicks, ibakedmodel);
        boolean flag1 = ibakedmodel.func_177556_c();
        if (!flag1) {
            float f3 = -0.0f * (float)(j - 1) * 0.5f;
            float f4 = -0.0f * (float)(j - 1) * 0.5f;
            float f5 = -0.09375f * (float)(j - 1) * 0.5f;
            GlStateManager.func_179109_b((float)f3, (float)f4, (float)f5);
        }
        if (this.field_188301_f) {
            GlStateManager.func_179142_g();
            GlStateManager.func_187431_e((int)this.func_188298_c(entity));
        }
        for (int k = 0; k < j; ++k) {
            IBakedModel transformedModel;
            GlStateManager.func_179094_E();
            if (flag1) {
                if (k > 0) {
                    float f7 = (this.field_177079_e.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    float f9 = (this.field_177079_e.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    float f6 = (this.field_177079_e.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    GlStateManager.func_179109_b((float)(this.shouldSpreadItems() ? f7 : 0.0f), (float)(this.shouldSpreadItems() ? f9 : 0.0f), (float)f6);
                }
                transformedModel = ForgeHooksClient.handleCameraTransforms((IBakedModel)ibakedmodel, (ItemCameraTransforms.TransformType)ItemCameraTransforms.TransformType.GROUND, (boolean)false);
                this.field_177080_a.func_180454_a(itemstack, transformedModel);
                GlStateManager.func_179121_F();
                continue;
            }
            if (k > 0) {
                float f8 = (this.field_177079_e.nextFloat() * 2.0f - 1.0f) * 0.15f * 0.5f;
                float f10 = (this.field_177079_e.nextFloat() * 2.0f - 1.0f) * 0.15f * 0.5f;
                GlStateManager.func_179109_b((float)f8, (float)f10, (float)0.0f);
            }
            transformedModel = ForgeHooksClient.handleCameraTransforms((IBakedModel)ibakedmodel, (ItemCameraTransforms.TransformType)ItemCameraTransforms.TransformType.GROUND, (boolean)false);
            this.field_177080_a.func_180454_a(itemstack, transformedModel);
            GlStateManager.func_179121_F();
            GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)0.09375f);
        }
        if (this.field_188301_f) {
            GlStateManager.func_187417_n();
            GlStateManager.func_179119_h();
        }
        GlStateManager.func_179121_F();
        GlStateManager.func_179101_C();
        GlStateManager.func_179084_k();
        this.func_180548_c(entity);
        if (flag) {
            this.field_76990_c.field_78724_e.func_110581_b(this.func_110775_a(entity)).func_174935_a();
        }
    }
}

