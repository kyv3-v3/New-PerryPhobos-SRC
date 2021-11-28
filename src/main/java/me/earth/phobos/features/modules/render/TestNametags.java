/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemTool
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.util.text.TextFormatting
 *  org.lwjgl.opengl.GL11
 */
package me.earth.phobos.features.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import me.earth.phobos.Phobos;
import me.earth.phobos.event.events.Render3DEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.client.Colors;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.DamageUtil;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.RotationUtil;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

public class TestNametags
extends Module {
    public static TestNametags INSTANCE;
    private final Setting<Boolean> health = this.register(new Setting<Boolean>("Health", true));
    private final Setting<Boolean> armor = this.register(new Setting<Boolean>("Armor", true));
    private final Setting<Boolean> reverseArmor = this.register(new Setting<Boolean>("ReverseArmor", false));
    private final Setting<Boolean> topEnchant = this.register(new Setting<Boolean>("TopEnchant", false));
    private final Setting<Boolean> altEnchantNames = this.register(new Setting<Boolean>("AltEnchantNames", false));
    private final Setting<Float> scaling = this.register(new Setting<Float>("Size", Float.valueOf(0.3f), Float.valueOf(0.1f), Float.valueOf(20.0f)));
    private final Setting<Boolean> invisibles = this.register(new Setting<Boolean>("Invisibles", false));
    private final Setting<Boolean> ping = this.register(new Setting<Boolean>("Ping", true));
    private final Setting<Boolean> totemPops = this.register(new Setting<Boolean>("TotemPops", true));
    private final Setting<Boolean> gamemode = this.register(new Setting<Boolean>("Gamemode", false));
    private final Setting<Boolean> entityID = this.register(new Setting<Boolean>("ID", false));
    private final Setting<Boolean> rect = this.register(new Setting<Boolean>("Rectangle", true));
    private final Setting<Boolean> outline = this.register(new Setting<Boolean>("Outline", Boolean.valueOf(false), v -> this.rect.getValue()));
    private final Setting<Boolean> colorSync = this.register(new Setting<Boolean>("Sync", Boolean.valueOf(false), v -> this.outline.getValue()));
    private final Setting<Integer> redSetting = this.register(new Setting<Integer>("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue()));
    private final Setting<Integer> greenSetting = this.register(new Setting<Integer>("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue()));
    private final Setting<Integer> blueSetting = this.register(new Setting<Integer>("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue()));
    private final Setting<Integer> alphaSetting = this.register(new Setting<Integer>("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue()));
    private final Setting<Float> lineWidth = this.register(new Setting<Float>("LineWidth", Float.valueOf(1.5f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.outline.getValue()));
    private final Setting<Boolean> sneak = this.register(new Setting<Boolean>("SneakColor", false));
    private final Setting<Boolean> heldStackName = this.register(new Setting<Boolean>("StackName", false));
    private final Setting<Boolean> whiter = this.register(new Setting<Boolean>("White", false));
    private final Setting<Boolean> onlyFov = this.register(new Setting<Boolean>("OnlyFov", false));
    private final Setting<Boolean> scaleing = this.register(new Setting<Boolean>("Scale", false));
    private final Setting<Float> factor = this.register(new Setting<Float>("Factor", Float.valueOf(0.3f), Float.valueOf(0.1f), Float.valueOf(1.0f), v -> this.scaleing.getValue()));
    private final Setting<Boolean> smartScale = this.register(new Setting<Boolean>("SmartScale", Boolean.valueOf(false), v -> this.scaleing.getValue()));

    public TestNametags() {
        super("TestNametags", "Don't use.", Module.Category.RENDER, true, false, true);
        INSTANCE = this;
    }

    public static TestNametags getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TestNametags();
        }
        return INSTANCE;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (!TestNametags.fullNullCheck()) {
            for (EntityPlayer player : TestNametags.mc.field_71441_e.field_73010_i) {
                if (player == null || player.equals((Object)TestNametags.mc.field_71439_g) || !player.func_70089_S() || player.func_82150_aj() && !this.invisibles.getValue().booleanValue() || this.onlyFov.getValue().booleanValue() && !RotationUtil.isInFov((Entity)player)) continue;
                double x = this.interpolate(player.field_70142_S, player.field_70165_t, event.getPartialTicks()) - TestNametags.mc.func_175598_ae().field_78725_b;
                double y = this.interpolate(player.field_70137_T, player.field_70163_u, event.getPartialTicks()) - TestNametags.mc.func_175598_ae().field_78726_c;
                double z = this.interpolate(player.field_70136_U, player.field_70161_v, event.getPartialTicks()) - TestNametags.mc.func_175598_ae().field_78723_d;
                this.renderProperNameTag(player, x, y, z, event.getPartialTicks());
            }
        }
    }

    private void renderProperNameTag(EntityPlayer player, double x, double y, double z, float delta) {
        double tempY = y;
        tempY += player.func_70093_af() ? 0.5 : 0.7;
        Entity camera = mc.func_175606_aa();
        assert (camera != null);
        double originalPositionX = camera.field_70165_t;
        double originalPositionY = camera.field_70163_u;
        double originalPositionZ = camera.field_70161_v;
        camera.field_70165_t = this.interpolate(camera.field_70169_q, camera.field_70165_t, delta);
        camera.field_70163_u = this.interpolate(camera.field_70167_r, camera.field_70163_u, delta);
        camera.field_70161_v = this.interpolate(camera.field_70166_s, camera.field_70161_v, delta);
        String displayTag = this.getDisplayTag(player);
        double distance = camera.func_70011_f(x + TestNametags.mc.func_175598_ae().field_78730_l, y + TestNametags.mc.func_175598_ae().field_78731_m, z + TestNametags.mc.func_175598_ae().field_78728_n);
        int width = this.renderer.getStringWidth(displayTag) / 2;
        double scale = (0.0018 + (double)this.scaling.getValue().floatValue() * (distance * (double)this.factor.getValue().floatValue())) / 1000.0;
        if (distance <= 8.0 && this.smartScale.getValue().booleanValue()) {
            scale = 0.0245;
        }
        if (!this.scaleing.getValue().booleanValue()) {
            scale = (double)this.scaling.getValue().floatValue() / 100.0;
        }
        GlStateManager.func_179094_E();
        RenderHelper.func_74519_b();
        GlStateManager.func_179088_q();
        GlStateManager.func_179136_a((float)1.0f, (float)-1500000.0f);
        GlStateManager.func_179140_f();
        GlStateManager.func_179109_b((float)((float)x), (float)((float)tempY + 1.4f), (float)((float)z));
        GlStateManager.func_179114_b((float)(-TestNametags.mc.func_175598_ae().field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)TestNametags.mc.func_175598_ae().field_78732_j, (float)(TestNametags.mc.field_71474_y.field_74320_O == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
        GlStateManager.func_179139_a((double)(-scale), (double)(-scale), (double)scale);
        GlStateManager.func_179097_i();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        if (this.rect.getValue().booleanValue()) {
            this.drawRect(-width - 2, -(TestNametags.mc.field_71466_p.field_78288_b + 1), (float)width + 2.0f, 1.5f, 0x55000000);
            if (this.outline.getValue().booleanValue()) {
                int color = this.colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColorHex() : new Color(this.redSetting.getValue(), this.greenSetting.getValue(), this.blueSetting.getValue(), this.alphaSetting.getValue()).getRGB();
                this.drawOutlineRect(-width - 2, -(TestNametags.mc.field_71466_p.field_78288_b + 1), (float)width + 2.0f, 1.5f, color);
            }
        }
        GlStateManager.func_179141_d();
        ItemStack renderMainHand = player.func_184614_ca().func_77946_l();
        if (!renderMainHand.func_77962_s() || renderMainHand.func_77973_b() instanceof ItemTool || renderMainHand.func_77973_b() instanceof ItemArmor) {
            // empty if block
        }
        if (this.heldStackName.getValue().booleanValue() && !renderMainHand.field_190928_g && renderMainHand.func_77973_b() != Items.field_190931_a) {
            String stackName = renderMainHand.func_82833_r();
            int stackNameWidth = this.renderer.getStringWidth(stackName) / 2;
            GL11.glPushMatrix();
            GL11.glScalef((float)0.75f, (float)0.75f, (float)0.0f);
            this.renderer.drawStringWithShadow(stackName, -stackNameWidth, -(this.getBiggestArmorTag(player) + 20.0f), -1);
            GL11.glScalef((float)1.5f, (float)1.5f, (float)1.0f);
            GL11.glPopMatrix();
        }
        ArrayList armorInventory = new ArrayList(player.field_71071_by.field_70460_b);
        if (this.reverseArmor.getValue().booleanValue()) {
            Collections.reverse(armorInventory);
        }
        GlStateManager.func_179094_E();
        int xOffset = -8;
        for (ItemStack stack : armorInventory) {
            if (stack == null) continue;
            xOffset -= 8;
        }
        xOffset -= 8;
        ItemStack renderOffhand = player.func_184592_cb().func_77946_l();
        if (!renderOffhand.func_77962_s() || renderOffhand.func_77973_b() instanceof ItemTool || renderOffhand.func_77973_b() instanceof ItemArmor) {
            // empty if block
        }
        this.renderItemStack(player, renderOffhand, xOffset, -26, this.armor.getValue());
        xOffset += 16;
        for (ItemStack stack : armorInventory) {
            if (stack == null) continue;
            ItemStack armourStack = stack.func_77946_l();
            if (!armourStack.func_77962_s() || armourStack.func_77973_b() instanceof ItemTool || armourStack.func_77973_b() instanceof ItemArmor) {
                // empty if block
            }
            this.renderItemStack(player, armourStack, xOffset, -26, this.armor.getValue());
            xOffset += 16;
        }
        this.renderItemStack(player, renderMainHand, xOffset, -26, this.armor.getValue());
        GlStateManager.func_179121_F();
        this.renderer.drawStringWithShadow(displayTag, -width, -(this.renderer.getFontHeight() - 1), this.getDisplayColour(player));
        camera.field_70165_t = originalPositionX;
        camera.field_70163_u = originalPositionY;
        camera.field_70161_v = originalPositionZ;
        GlStateManager.func_179126_j();
        GlStateManager.func_179145_e();
        GlStateManager.func_179084_k();
        GlStateManager.func_179145_e();
        GlStateManager.func_179113_r();
        GlStateManager.func_179136_a((float)1.0f, (float)1500000.0f);
        GlStateManager.func_179121_F();
    }

    private void renderNameTag(EntityPlayer player, double x, double y, double z, float partialTicks) {
        double tempY = y + (player.func_70093_af() ? 0.5 : 0.7);
        Entity camera = mc.func_175606_aa();
        assert (camera != null);
        double originalPositionX = camera.field_70165_t;
        double originalPositionY = camera.field_70163_u;
        double originalPositionZ = camera.field_70161_v;
        camera.field_70165_t = this.interpolate(camera.field_70169_q, camera.field_70165_t, partialTicks);
        camera.field_70163_u = this.interpolate(camera.field_70167_r, camera.field_70163_u, partialTicks);
        camera.field_70161_v = this.interpolate(camera.field_70166_s, camera.field_70161_v, partialTicks);
        double distance = camera.func_70011_f(x + TestNametags.mc.func_175598_ae().field_78730_l, y + TestNametags.mc.func_175598_ae().field_78731_m, z + TestNametags.mc.func_175598_ae().field_78728_n);
        int width = TestNametags.mc.field_71466_p.func_78256_a(this.getDisplayTag(player)) / 2;
        double scale = (0.0018 + (double)this.scaling.getValue().floatValue() * distance) / 50.0;
        GlStateManager.func_179094_E();
        RenderHelper.func_74519_b();
        GlStateManager.func_179088_q();
        GlStateManager.func_179136_a((float)1.0f, (float)-1500000.0f);
        GlStateManager.func_179140_f();
        GlStateManager.func_179109_b((float)((float)x), (float)((float)tempY + 1.4f), (float)((float)z));
        GlStateManager.func_179114_b((float)(-TestNametags.mc.func_175598_ae().field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
        float thirdPersonOffset = TestNametags.mc.field_71474_y.field_74320_O == 2 ? -1.0f : 1.0f;
        GlStateManager.func_179114_b((float)TestNametags.mc.func_175598_ae().field_78732_j, (float)thirdPersonOffset, (float)0.0f, (float)0.0f);
        GlStateManager.func_179139_a((double)(-scale), (double)(-scale), (double)scale);
        GlStateManager.func_179097_i();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        this.drawRect(-width - 2, -(TestNametags.mc.field_71466_p.field_78288_b + 1), (float)width + 2.0f, 1.5f, 0x55000000);
        GlStateManager.func_179141_d();
        TestNametags.mc.field_71466_p.func_175063_a(this.getDisplayTag(player), (float)(-width), (float)(-(TestNametags.mc.field_71466_p.field_78288_b - 1)), this.getNameColor((Entity)player).getRGB());
        if (this.armor.getValue().booleanValue()) {
            GlStateManager.func_179094_E();
            double changeValue = 16.0;
            int xOffset = 0;
            xOffset = (int)((double)xOffset - changeValue / 2.0 * (double)player.field_71071_by.field_70460_b.size());
            xOffset = (int)((double)xOffset - changeValue / 2.0);
            xOffset = (int)((double)xOffset - changeValue / 2.0);
            if (!player.func_184614_ca().func_190926_b()) {
                // empty if block
            }
            xOffset = (int)((double)xOffset + changeValue);
            for (ItemStack stack : player.field_71071_by.field_70460_b) {
                if (!stack.func_190926_b()) {
                    // empty if block
                }
                xOffset = (int)((double)xOffset + changeValue);
            }
            if (!player.func_184592_cb().func_190926_b()) {
                // empty if block
            }
            GlStateManager.func_179121_F();
        }
        camera.field_70165_t = originalPositionX;
        camera.field_70163_u = originalPositionY;
        camera.field_70161_v = originalPositionZ;
        GlStateManager.func_179126_j();
        GlStateManager.func_179145_e();
        GlStateManager.func_179084_k();
        GlStateManager.func_179145_e();
        GlStateManager.func_179113_r();
        GlStateManager.func_179136_a((float)1.0f, (float)1500000.0f);
        GlStateManager.func_179121_F();
    }

    public void drawRect(float x, float y, float w, float h, int color) {
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_187441_d((float)this.lineWidth.getValue().floatValue());
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        bufferbuilder.func_181662_b((double)x, (double)h, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b((double)w, (double)h, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b((double)w, (double)y, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b((double)x, (double)y, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public void drawOutlineRect(float x, float y, float w, float h, int color) {
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_187441_d((float)this.lineWidth.getValue().floatValue());
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        bufferbuilder.func_181668_a(2, DefaultVertexFormats.field_181706_f);
        bufferbuilder.func_181662_b((double)x, (double)h, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b((double)w, (double)h, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b((double)w, (double)y, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b((double)x, (double)y, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    private Color getNameColor(Entity entity) {
        return Color.WHITE;
    }

    private void renderItemStack(EntityPlayer player, ItemStack stack, int x, int y, boolean item) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179086_m((int)256);
        RenderHelper.func_74519_b();
        TestNametags.mc.func_175599_af().field_77023_b = -150.0f;
        GlStateManager.func_179118_c();
        GlStateManager.func_179126_j();
        GlStateManager.func_179129_p();
        if (item) {
            mc.func_175599_af().func_180450_b(stack, x, y);
            mc.func_175599_af().func_175030_a(TestNametags.mc.field_71466_p, stack, x, y);
        }
        TestNametags.mc.func_175599_af().field_77023_b = 0.0f;
        RenderHelper.func_74518_a();
        GlStateManager.func_179089_o();
        GlStateManager.func_179141_d();
        GlStateManager.func_179152_a((float)0.5f, (float)0.5f, (float)0.5f);
        GlStateManager.func_179097_i();
        this.renderEnchantmentText(player, stack, x, y);
        GlStateManager.func_179126_j();
        GlStateManager.func_179152_a((float)2.0f, (float)2.0f, (float)2.0f);
        GlStateManager.func_179121_F();
    }

    private boolean shouldMoveArmor(EntityPlayer player) {
        for (ItemStack stack : player.field_71071_by.field_70460_b) {
            NBTTagList enchants = stack.func_77986_q();
            if (enchants.func_74745_c() == 0) continue;
            return true;
        }
        ItemStack renderMainHand = player.func_184614_ca().func_77946_l();
        if (renderMainHand.func_77962_s()) {
            return true;
        }
        ItemStack renderOffHand = player.func_184592_cb().func_77946_l();
        return renderMainHand.func_77962_s();
    }

    private void renderEnchantmentText(EntityPlayer player, ItemStack stack, int x, int y) {
        int enchantmentY = (int)((float)(y - 8) - (this.topEnchant.getValue() != false ? this.getBiggestArmorTag(player) - this.getEnchantHeight(stack) : 0.0f));
        if (stack.func_77973_b() == Items.field_151153_ao && stack.func_77962_s()) {
            this.renderer.drawStringWithShadow("god", x * 2, enchantmentY, -3977919);
            enchantmentY -= 8;
        }
        NBTTagList enchants = stack.func_77986_q();
        for (int index = 0; index < enchants.func_74745_c(); ++index) {
            String string;
            String encName = null;
            short id = enchants.func_150305_b(index).func_74765_d("id");
            short level = enchants.func_150305_b(index).func_74765_d("lvl");
            Enchantment enc = Enchantment.func_185262_c((int)id);
            if (enc == null) continue;
            String string2 = enc.func_190936_d() ? (this.altEnchantNames.getValue().booleanValue() ? enc.func_77316_c((int)level).substring(11).substring(0, this.altEnchantNames.getValue() != false ? 3 : 1) : (Object)TextFormatting.RED + enc.func_77316_c((int)level).substring(11).substring(0, this.altEnchantNames.getValue().booleanValue() ? (enc.func_77325_b() == 1 ? 3 : 2) : 1)) : (string = (encName = enc.func_77316_c((int)level).substring(0, this.altEnchantNames.getValue().booleanValue() ? (enc.func_77325_b() == 1 ? 3 : 2) : 1)));
            if (enc.func_77325_b() != 1) {
                encName = encName + level;
            }
            if (!this.altEnchantNames.getValue().booleanValue()) {
                encName = Objects.requireNonNull(encName).toLowerCase();
            }
            this.renderer.drawStringWithShadow(encName, x * 2, enchantmentY, -1);
            enchantmentY -= 8;
        }
        if (DamageUtil.hasDurability(stack)) {
            int percent = DamageUtil.getRoundedDamage(stack);
            String color = percent >= 60 ? "\u00a7a" : (percent >= 25 ? "\u00a7e" : "\u00a7c");
            this.renderer.drawStringWithShadow(color + percent + "%", x * 2, enchantmentY, -1);
        }
    }

    private float getEnchantHeight(ItemStack stack) {
        float enchantHeight = 0.0f;
        NBTTagList enchants = stack.func_77986_q();
        for (int index = 0; index < enchants.func_74745_c(); ++index) {
            short id = enchants.func_150305_b(index).func_74765_d("id");
            Enchantment enc = Enchantment.func_185262_c((int)id);
            if (enc == null) continue;
            enchantHeight += 8.0f;
        }
        return enchantHeight;
    }

    private float getBiggestArmorTag(EntityPlayer player) {
        ItemStack renderOffHand;
        Enchantment enc;
        short id;
        int index;
        float enchantmentY = 0.0f;
        boolean arm = false;
        for (ItemStack stack : player.field_71071_by.field_70460_b) {
            float encY = 0.0f;
            if (stack != null) {
                NBTTagList enchants = stack.func_77986_q();
                for (index = 0; index < enchants.func_74745_c(); ++index) {
                    id = enchants.func_150305_b(index).func_74765_d("id");
                    enc = Enchantment.func_185262_c((int)id);
                    if (enc == null) continue;
                    encY += 8.0f;
                    arm = true;
                }
            }
            if (!(encY > enchantmentY)) continue;
            enchantmentY = encY;
        }
        ItemStack renderMainHand = player.func_184614_ca().func_77946_l();
        if (renderMainHand.func_77962_s()) {
            float encY = 0.0f;
            NBTTagList enchants = renderMainHand.func_77986_q();
            for (int index2 = 0; index2 < enchants.func_74745_c(); ++index2) {
                id = enchants.func_150305_b(index2).func_74765_d("id");
                Enchantment enc2 = Enchantment.func_185262_c((int)id);
                if (enc2 == null) continue;
                encY += 8.0f;
                arm = true;
            }
            if (encY > enchantmentY) {
                enchantmentY = encY;
            }
        }
        if ((renderOffHand = player.func_184592_cb().func_77946_l()).func_77962_s()) {
            float encY = 0.0f;
            NBTTagList enchants = renderOffHand.func_77986_q();
            for (index = 0; index < enchants.func_74745_c(); ++index) {
                short id2 = enchants.func_150305_b(index).func_74765_d("id");
                enc = Enchantment.func_185262_c((int)id2);
                if (enc == null) continue;
                encY += 8.0f;
                arm = true;
            }
            if (encY > enchantmentY) {
                enchantmentY = encY;
            }
        }
        return (float)(arm ? 0 : 20) + enchantmentY;
    }

    private String getDisplayTag(EntityPlayer player) {
        String name = player.func_145748_c_().func_150254_d();
        if (name.contains(mc.func_110432_I().func_111285_a())) {
            name = "You";
        }
        if (!this.health.getValue().booleanValue()) {
            return name;
        }
        float health = EntityUtil.getHealth((Entity)player);
        String color = health > 18.0f ? "\u00a7a" : (health > 16.0f ? "\u00a72" : (health > 12.0f ? "\u00a7e" : (health > 8.0f ? "\u00a76" : (health > 5.0f ? "\u00a7c" : "\u00a74"))));
        String pingStr = "";
        if (this.ping.getValue().booleanValue()) {
            try {
                int responseTime = Objects.requireNonNull(mc.func_147114_u()).func_175102_a(player.func_110124_au()).func_178853_c();
                pingStr = pingStr + responseTime + "ms ";
            }
            catch (Exception responseTime) {
                // empty catch block
            }
        }
        String popStr = " ";
        if (this.totemPops.getValue().booleanValue()) {
            popStr = popStr + Phobos.totemPopManager.getTotemPopString(player);
        }
        String idString = "";
        if (this.entityID.getValue().booleanValue()) {
            idString = idString + "ID: " + player.func_145782_y() + " ";
        }
        String gameModeStr = "";
        if (this.gamemode.getValue().booleanValue()) {
            String string = player.func_184812_l_() ? gameModeStr + "[C] " : (gameModeStr = player.func_175149_v() || player.func_82150_aj() ? gameModeStr + "[I] " : gameModeStr + "[S] ");
        }
        name = Math.floor(health) == (double)health ? name + color + " " + (health > 0.0f ? Integer.valueOf((int)Math.floor(health)) : "dead") : name + color + " " + (health > 0.0f ? Integer.valueOf((int)health) : "dead");
        return pingStr + idString + gameModeStr + name + popStr;
    }

    private int getDisplayColour(EntityPlayer player) {
        int colour = -5592406;
        if (this.whiter.getValue().booleanValue()) {
            colour = -1;
        }
        if (Phobos.friendManager.isFriend(player)) {
            return -11157267;
        }
        if (player.func_82150_aj()) {
            colour = -1113785;
        } else if (player.func_70093_af() && this.sneak.getValue().booleanValue()) {
            colour = -6481515;
        }
        return colour;
    }

    private double interpolate(double previous, double current, float partialTicks) {
        return previous + (current - previous) * (double)partialTicks;
    }
}

