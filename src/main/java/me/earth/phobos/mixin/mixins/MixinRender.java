



package me.earth.phobos.mixin.mixins;

import net.minecraft.entity.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.culling.*;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.*;

@Mixin({ Render.class })
public class MixinRender<T extends Entity>
{
    @Overwrite
    public boolean shouldRender(final T livingEntity,  final ICamera camera,  final double camX,  final double camY,  final double camZ) {
        try {
            AxisAlignedBB axisalignedbb = livingEntity.getRenderBoundingBox().grow(0.5);
            if ((axisalignedbb.hasNaN() || axisalignedbb.getAverageEdgeLength() == 0.0) && livingEntity != null) {
                axisalignedbb = new AxisAlignedBB(livingEntity.posX - 2.0,  livingEntity.posY - 2.0,  livingEntity.posZ - 2.0,  livingEntity.posX + 2.0,  livingEntity.posY + 2.0,  livingEntity.posZ + 2.0);
            }
            return livingEntity.isInRangeToRender3d(camX,  camY,  camZ) && (livingEntity.ignoreFrustumCheck || camera.isBoundingBoxInFrustum(axisalignedbb));
        }
        catch (Exception ignored) {
            return false;
        }
    }
}
