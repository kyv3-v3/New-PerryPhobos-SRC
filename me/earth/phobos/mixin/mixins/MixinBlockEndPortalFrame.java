



package me.earth.phobos.mixin.mixins;

import net.minecraft.block.*;
import org.spongepowered.asm.mixin.*;
import net.minecraft.block.state.*;
import net.minecraft.world.*;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.earth.phobos.features.modules.movement.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ BlockEndPortalFrame.class })
public class MixinBlockEndPortalFrame
{
    @Shadow
    @Final
    protected static AxisAlignedBB AABB_BLOCK;
    
    @Inject(method = { "getBoundingBox" }, at = { @At("HEAD") }, cancellable = true)
    public void getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos, final CallbackInfoReturnable<AxisAlignedBB> info) {
        if (NoSlowDown.getInstance().isOn() && (boolean)NoSlowDown.getInstance().endPortal.getValue()) {
            info.setReturnValue((Object)new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0));
        }
    }
}
