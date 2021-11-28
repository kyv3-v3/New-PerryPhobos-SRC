/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 */
package me.earth.phobos.util;

import java.util.Objects;
import me.earth.phobos.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class MovementUtil
implements Util {
    public static Vec3d calculateLine(Vec3d x1, Vec3d x2, double distance) {
        double length = Math.sqrt(MovementUtil.multiply(x2.field_72450_a - x1.field_72450_a) + MovementUtil.multiply(x2.field_72448_b - x1.field_72448_b) + MovementUtil.multiply(x2.field_72449_c - x1.field_72449_c));
        double unitSlopeX = (x2.field_72450_a - x1.field_72450_a) / length;
        double unitSlopeY = (x2.field_72448_b - x1.field_72448_b) / length;
        double unitSlopeZ = (x2.field_72449_c - x1.field_72449_c) / length;
        double x = x1.field_72450_a + unitSlopeX * distance;
        double y = x1.field_72448_b + unitSlopeY * distance;
        double z = x1.field_72449_c + unitSlopeZ * distance;
        return new Vec3d(x, y, z);
    }

    public static Vec2f calculateLineNoY(Vec2f x1, Vec2f x2, double distance) {
        double length = Math.sqrt(MovementUtil.multiply(x2.field_189982_i - x1.field_189982_i) + MovementUtil.multiply(x2.field_189983_j - x1.field_189983_j));
        double unitSlopeX = (double)(x2.field_189982_i - x1.field_189982_i) / length;
        double unitSlopeZ = (double)(x2.field_189983_j - x1.field_189983_j) / length;
        float x = (float)((double)x1.field_189982_i + unitSlopeX * distance);
        float z = (float)((double)x1.field_189983_j + unitSlopeZ * distance);
        return new Vec2f(x, z);
    }

    public static double multiply(double one) {
        return one * one;
    }

    public static Vec3d extrapolatePlayerPositionWithGravity(EntityPlayer player, int ticks) {
        double totalDistance = 0.0;
        double extrapolatedMotionY = player.field_70181_x;
        for (int i = 0; i < ticks; ++i) {
            totalDistance += MovementUtil.multiply(player.field_70159_w) + MovementUtil.multiply(extrapolatedMotionY) + MovementUtil.multiply(player.field_70179_y);
            extrapolatedMotionY -= 0.1;
        }
        double horizontalDistance = MovementUtil.multiply(player.field_70159_w) + MovementUtil.multiply(player.field_70179_y) * (double)ticks;
        Vec2f horizontalVec = MovementUtil.calculateLineNoY(new Vec2f((float)player.field_70142_S, (float)player.field_70136_U), new Vec2f((float)player.field_70165_t, (float)player.field_70161_v), horizontalDistance);
        double addedY = player.field_70181_x;
        double finalY = player.field_70163_u;
        Vec3d tempPos = new Vec3d((double)horizontalVec.field_189982_i, player.field_70163_u, (double)horizontalVec.field_189983_j);
        for (int i = 0; i < ticks; ++i) {
            finalY += addedY;
            addedY -= 0.1;
        }
        RayTraceResult result = MovementUtil.mc.field_71441_e.func_72933_a(player.func_174791_d(), new Vec3d(tempPos.field_72450_a, finalY, tempPos.field_72449_c));
        if (result == null || result.field_72313_a == RayTraceResult.Type.ENTITY) {
            return new Vec3d(tempPos.field_72450_a, finalY, tempPos.field_72449_c);
        }
        return result.field_72307_f;
    }

    public static double[] forward(double d) {
        float f = Minecraft.func_71410_x().field_71439_g.field_71158_b.field_192832_b;
        float f2 = Minecraft.func_71410_x().field_71439_g.field_71158_b.field_78902_a;
        float f3 = Minecraft.func_71410_x().field_71439_g.field_70126_B + (Minecraft.func_71410_x().field_71439_g.field_70177_z - Minecraft.func_71410_x().field_71439_g.field_70126_B) * Minecraft.func_71410_x().func_184121_ak();
        if (f != 0.0f) {
            if (f2 > 0.0f) {
                f3 += (float)(f > 0.0f ? -45 : 45);
            } else if (f2 < 0.0f) {
                f3 += (float)(f > 0.0f ? 45 : -45);
            }
            f2 = 0.0f;
            if (f > 0.0f) {
                f = 1.0f;
            } else if (f < 0.0f) {
                f = -1.0f;
            }
        }
        double d2 = Math.sin(Math.toRadians(f3 + 90.0f));
        double d3 = Math.cos(Math.toRadians(f3 + 90.0f));
        double d4 = (double)f * d * d3 + (double)f2 * d * d2;
        double d5 = (double)f * d * d2 - (double)f2 * d * d3;
        return new double[]{d4, d5};
    }

    public static boolean isMoving(EntityLivingBase entityLivingBase) {
        return entityLivingBase.field_191988_bg != 0.0f || entityLivingBase.field_70702_br != 0.0f;
    }

    public static void setSpeed(EntityLivingBase entityLivingBase, double d) {
        double[] dArray = MovementUtil.forward(d);
        entityLivingBase.field_70159_w = dArray[0];
        entityLivingBase.field_70179_y = dArray[1];
    }

    public static double getBaseMoveSpeed() {
        double d = 0.2873;
        if (Minecraft.func_71410_x().field_71439_g != null && Minecraft.func_71410_x().field_71439_g.func_70644_a(Objects.requireNonNull(Potion.func_188412_a((int)1)))) {
            int n = Objects.requireNonNull(Minecraft.func_71410_x().field_71439_g.func_70660_b(Objects.requireNonNull(Potion.func_188412_a((int)1)))).func_76458_c();
            d *= 1.0 + 0.2 * (double)(n + 1);
        }
        return d;
    }

    public static Vec3d extrapolatePlayerPosition(EntityPlayer player, int ticks) {
        double totalDistance = 0.0;
        double extrapolatedMotionY = player.field_70181_x;
        for (int i = 0; i < ticks; ++i) {
        }
        Vec3d lastPos = new Vec3d(player.field_70142_S, player.field_70137_T, player.field_70136_U);
        Vec3d currentPos = new Vec3d(player.field_70165_t, player.field_70163_u, player.field_70161_v);
        double distance = MovementUtil.multiply(player.field_70159_w) + MovementUtil.multiply(player.field_70181_x) + MovementUtil.multiply(player.field_70179_y);
        double extrapolatedPosY = player.field_70163_u;
        if (!player.func_189652_ae()) {
            extrapolatedPosY -= 0.1;
        }
        Vec3d tempVec = MovementUtil.calculateLine(lastPos, currentPos, distance * (double)ticks);
        Vec3d finalVec = new Vec3d(tempVec.field_72450_a, extrapolatedPosY, tempVec.field_72449_c);
        MovementUtil.mc.field_71441_e.func_72933_a(player.func_174791_d(), finalVec);
        return new Vec3d(tempVec.field_72450_a, player.field_70163_u, tempVec.field_72449_c);
    }
}

