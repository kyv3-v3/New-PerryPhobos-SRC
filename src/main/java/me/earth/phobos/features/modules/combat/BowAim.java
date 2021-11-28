/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemBow
 *  net.minecraft.util.math.Vec3d
 */
package me.earth.phobos.features.modules.combat;

import me.earth.phobos.Phobos;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.MathUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.util.math.Vec3d;

public class BowAim
extends Module {
    public BowAim() {
        super("BowAim", "Automatically aims ur bow at enemies.", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (BowAim.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBow && BowAim.mc.field_71439_g.func_184587_cr() && BowAim.mc.field_71439_g.func_184612_cw() >= 3) {
            EntityPlayer player = null;
            float tickDis = 100.0f;
            for (EntityPlayer p : BowAim.mc.field_71441_e.field_73010_i) {
                float dis;
                if (p instanceof EntityPlayerSP || Phobos.friendManager.isFriend(p.func_70005_c_()) || !((dis = p.func_70032_d((Entity)BowAim.mc.field_71439_g)) < tickDis)) continue;
                tickDis = dis;
                player = p;
            }
            if (player != null) {
                Vec3d pos = EntityUtil.getInterpolatedPos(player, mc.func_184121_ak());
                float[] angels = MathUtil.calcAngle(EntityUtil.getInterpolatedPos((Entity)BowAim.mc.field_71439_g, mc.func_184121_ak()), pos);
                BowAim.mc.field_71439_g.field_70177_z = angels[0];
                BowAim.mc.field_71439_g.field_70125_A = angels[1];
            }
        }
    }
}

