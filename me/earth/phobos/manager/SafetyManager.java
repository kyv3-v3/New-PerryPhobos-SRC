//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.manager;

import me.earth.phobos.features.*;
import java.util.concurrent.atomic.*;
import me.earth.phobos.features.modules.combat.*;
import me.earth.phobos.features.modules.client.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import me.earth.phobos.util.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.player.*;
import java.util.*;
import java.util.concurrent.*;

public class SafetyManager extends Feature implements Runnable
{
    private final TimerUtil syncTimer;
    private final AtomicBoolean SAFE;
    private ScheduledExecutorService service;
    
    public SafetyManager() {
        this.syncTimer = new TimerUtil();
        this.SAFE = new AtomicBoolean(false);
    }
    
    public void run() {
        if (AutoCrystal.getInstance().isOff() || AutoCrystal.getInstance().threadMode.getValue() == AutoCrystal.ThreadMode.NONE) {
            this.doSafetyCheck();
        }
    }
    
    public void doSafetyCheck() {
        if (!fullNullCheck()) {
            boolean safe = true;
            final EntityPlayer closest = Management.getInstance().safety.getValue() ? EntityUtil.getClosestEnemy(18.0) : null;
            if ((boolean)Management.getInstance().safety.getValue() && closest == null) {
                this.SAFE.set(true);
                return;
            }
            final ArrayList<Entity> crystals = new ArrayList<Entity>(SafetyManager.mc.world.loadedEntityList);
            for (final Entity crystal : crystals) {
                if (crystal instanceof EntityEnderCrystal && DamageUtil.calculateDamage(crystal, (Entity)SafetyManager.mc.player) > 4.0) {
                    if (closest != null && closest.getDistanceSq(crystal) >= 40.0) {
                        continue;
                    }
                    safe = false;
                    break;
                }
            }
            if (safe) {
                for (final BlockPos pos : BlockUtil.possiblePlacePositions(4.0f, false, (boolean)Management.getInstance().oneDot15.getValue(), false)) {
                    if (DamageUtil.calculateDamage(pos, (Entity)SafetyManager.mc.player) > 4.0) {
                        if (closest != null && closest.getDistanceSq(pos) >= 40.0) {
                            continue;
                        }
                        safe = false;
                        break;
                    }
                }
            }
            this.SAFE.set(safe);
        }
    }
    
    public void onUpdate() {
        this.run();
    }
    
    public String getSafetyString() {
        if (this.SAFE.get()) {
            return "§aSecure";
        }
        return "§cUnsafe";
    }
    
    public boolean isSafe() {
        return this.SAFE.get();
    }
    
    public ScheduledExecutorService getService() {
        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(this, 0L, (int)Management.getInstance().safetyCheck.getValue(), TimeUnit.MILLISECONDS);
        return service;
    }
}
