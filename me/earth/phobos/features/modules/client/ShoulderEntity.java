



package me.earth.phobos.features.modules.client;

import me.earth.phobos.features.modules.*;
import net.minecraft.util.*;
import net.minecraft.entity.passive.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.nbt.*;

public class ShoulderEntity extends Module
{
    private static final ResourceLocation BLACK_OCELOT_TEXTURES;
    
    public ShoulderEntity() {
        super("ShoulderEntity", "Test.", Category.CLIENT, true, false, false);
    }
    
    @Override
    public void onEnable() {
        ShoulderEntity.mc.world.addEntityToWorld(-101, (Entity)new EntityOcelot((World)ShoulderEntity.mc.world));
        final NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("id", (NBTBase)new NBTTagInt(-101));
        ShoulderEntity.mc.player.addShoulderEntity(tag);
    }
    
    @Override
    public void onDisable() {
        ShoulderEntity.mc.world.removeEntityFromWorld(-101);
    }
    
    public float interpolate(final float yaw1, final float yaw2, final float percent) {
        float rotation = (yaw1 + (yaw2 - yaw1) * percent) % 360.0f;
        if (rotation < 0.0f) {
            rotation += 360.0f;
        }
        return rotation;
    }
    
    static {
        BLACK_OCELOT_TEXTURES = new ResourceLocation("textures/entity/cat/black.png");
    }
}
