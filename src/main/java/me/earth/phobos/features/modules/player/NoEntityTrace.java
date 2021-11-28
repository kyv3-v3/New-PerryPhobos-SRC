/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemPickaxe
 */
package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPickaxe;

public class NoEntityTrace
extends Module {
    private static NoEntityTrace INSTANCE = new NoEntityTrace();
    public Setting<Boolean> pick = this.register(new Setting<Boolean>("Pick", true));
    public Setting<Boolean> gap = this.register(new Setting<Boolean>("Gap", false));
    public Setting<Boolean> obby = this.register(new Setting<Boolean>("Obby", false));
    public boolean noTrace;

    public NoEntityTrace() {
        super("NoEntityTrace", "Mine through entities.", Module.Category.PLAYER, false, false, false);
        this.setInstance();
    }

    public static NoEntityTrace getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new NoEntityTrace();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        Item item = NoEntityTrace.mc.field_71439_g.func_184614_ca().func_77973_b();
        if (item instanceof ItemPickaxe && this.pick.getValue().booleanValue()) {
            this.noTrace = true;
            return;
        }
        if (item == Items.field_151153_ao && this.gap.getValue().booleanValue()) {
            this.noTrace = true;
            return;
        }
        if (item instanceof ItemBlock) {
            this.noTrace = ((ItemBlock)item).func_179223_d() == Blocks.field_150343_Z && this.obby.getValue() != false;
            return;
        }
        this.noTrace = false;
    }
}

