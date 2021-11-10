



package me.earth.phobos.features.modules.combat;

import me.earth.phobos.features.modules.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.entity.*;
import me.earth.phobos.*;
import net.minecraft.entity.*;
import me.earth.phobos.util.*;
import java.util.*;
import net.minecraft.util.math.*;

public class BowAim extends Module
{
    public BowAim() {
        super("BowAim",  "Automatically aims ur bow at enemies.",  Category.COMBAT,  true,  false,  false);
    }
    
    @Override
    public void onUpdate() {
        if (BowAim.mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && BowAim.mc.player.isHandActive() && BowAim.mc.player.getItemInUseMaxCount() >= 3) {
            EntityPlayer player = null;
            float tickDis = 100.0f;
            for (final EntityPlayer p : BowAim.mc.world.playerEntities) {
                if (!(p instanceof EntityPlayerSP)) {
                    if (Phobos.friendManager.isFriend(p.getName())) {
                        continue;
                    }
                    final float dis = p.getDistance((Entity)BowAim.mc.player);
                    if (dis >= tickDis) {
                        continue;
                    }
                    tickDis = dis;
                    player = p;
                }
            }
            if (player != null) {
                final Vec3d pos = EntityUtil.getInterpolatedPos((Entity)player,  BowAim.mc.getRenderPartialTicks());
                final float[] angels = MathUtil.calcAngle(EntityUtil.getInterpolatedPos((Entity)BowAim.mc.player,  BowAim.mc.getRenderPartialTicks()),  pos);
                BowAim.mc.player.rotationYaw = angels[0];
                BowAim.mc.player.rotationPitch = angels[1];
            }
        }
    }
}
