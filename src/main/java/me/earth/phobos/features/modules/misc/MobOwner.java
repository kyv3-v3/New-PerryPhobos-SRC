/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.passive.AbstractHorse
 *  net.minecraft.entity.passive.EntityTameable
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.earth.phobos.features.modules.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.util.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;

public class MobOwner
extends Module {
    private final Map<Entity, String> owners = new HashMap<Entity, String>();
    private final Map<Entity, UUID> toLookUp = new ConcurrentHashMap<Entity, UUID>();
    private final List<Entity> lookedUp = new ArrayList<Entity>();

    public MobOwner() {
        super("MobOwner", "Shows you who owns mobs.", Module.Category.MISC, false, false, false);
    }

    @Override
    public void onUpdate() {
        if (MobOwner.fullNullCheck()) {
            return;
        }
        if (PlayerUtil.timer.passedS(5.0)) {
            for (Map.Entry<Object, UUID> entry : this.toLookUp.entrySet()) {
                Entity entity = (Entity)entry.getKey();
                UUID uuid = entry.getValue();
                if (uuid != null) {
                    EntityPlayer owner = MobOwner.mc.field_71441_e.func_152378_a(uuid);
                    if (owner == null) {
                        try {
                            String name = PlayerUtil.getNameFromUUID(uuid);
                            if (name != null) {
                                this.owners.put(entity, name);
                                this.lookedUp.add(entity);
                            }
                        }
                        catch (Exception e) {
                            this.lookedUp.add(entity);
                            this.toLookUp.remove(entry);
                        }
                        PlayerUtil.timer.reset();
                        break;
                    }
                    this.owners.put(entity, owner.func_70005_c_());
                    this.lookedUp.add(entity);
                    continue;
                }
                this.lookedUp.add(entity);
                this.toLookUp.remove(entry);
            }
        }
        for (Entity entity : MobOwner.mc.field_71441_e.func_72910_y()) {
            AbstractHorse tameableEntity2;
            if (entity.func_174833_aM()) continue;
            if (entity instanceof EntityTameable) {
                EntityTameable tameableEntity = (EntityTameable)entity;
                if (!tameableEntity.func_70909_n() || tameableEntity.func_184753_b() == null) continue;
                if (this.owners.get((Object)tameableEntity) != null) {
                    tameableEntity.func_174805_g(true);
                    tameableEntity.func_96094_a(this.owners.get((Object)tameableEntity));
                    continue;
                }
                if (this.lookedUp.contains((Object)entity)) continue;
                this.toLookUp.put((Entity)tameableEntity, tameableEntity.func_184753_b());
                continue;
            }
            if (!(entity instanceof AbstractHorse) || !(tameableEntity2 = (AbstractHorse)entity).func_110248_bS() || tameableEntity2.func_184780_dh() == null) continue;
            if (this.owners.get((Object)tameableEntity2) != null) {
                tameableEntity2.func_174805_g(true);
                tameableEntity2.func_96094_a(this.owners.get((Object)tameableEntity2));
                continue;
            }
            if (this.lookedUp.contains((Object)entity)) continue;
            this.toLookUp.put((Entity)tameableEntity2, tameableEntity2.func_184780_dh());
        }
    }

    @Override
    public void onDisable() {
        for (Entity entity : MobOwner.mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityTameable) && !(entity instanceof AbstractHorse)) continue;
            try {
                entity.func_174805_g(false);
            }
            catch (Exception exception) {}
        }
    }
}

