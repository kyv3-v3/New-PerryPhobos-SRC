/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemStack
 */
package me.earth.phobos.features.modules.player;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.combat.Auto32k;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.InventoryUtil;
import me.earth.phobos.util.TimerUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class Replenish
extends Module {
    private final Setting<Integer> threshold = this.register(new Setting<Integer>("Threshold", 0, 0, 63));
    private final Setting<Integer> replenishments = this.register(new Setting<Integer>("RUpdates", 0, 0, 1000));
    private final Setting<Integer> updates = this.register(new Setting<Integer>("HBUpdates", 100, 0, 1000));
    private final Setting<Integer> actions = this.register(new Setting<Integer>("Actions", 2, 1, 30));
    private final Setting<Boolean> pauseInv = this.register(new Setting<Boolean>("PauseInv", true));
    private final Setting<Boolean> putBack = this.register(new Setting<Boolean>("PutBack", true));
    private final TimerUtil timer = new TimerUtil();
    private final TimerUtil replenishTimer = new TimerUtil();
    private final Queue<InventoryUtil.Task> taskList = new ConcurrentLinkedQueue<InventoryUtil.Task>();
    private Map<Integer, ItemStack> hotbar = new ConcurrentHashMap<Integer, ItemStack>();

    public Replenish() {
        super("Replenish", "Replenishes your hotbar.", Module.Category.PLAYER, false, false, false);
    }

    @Override
    public void onUpdate() {
        if (Auto32k.getInstance().isOn() && (!Auto32k.getInstance().autoSwitch.getValue().booleanValue() || Auto32k.getInstance().switching)) {
            return;
        }
        if (Replenish.mc.field_71462_r instanceof GuiContainer && (!(Replenish.mc.field_71462_r instanceof GuiInventory) || this.pauseInv.getValue().booleanValue())) {
            return;
        }
        if (this.timer.passedMs(this.updates.getValue().intValue())) {
            this.mapHotbar();
        }
        if (this.replenishTimer.passedMs(this.replenishments.getValue().intValue())) {
            for (int i = 0; i < this.actions.getValue(); ++i) {
                InventoryUtil.Task task = this.taskList.poll();
                if (task == null) continue;
                task.run();
            }
            this.replenishTimer.reset();
        }
    }

    @Override
    public void onDisable() {
        this.hotbar.clear();
    }

    @Override
    public void onLogout() {
        this.onDisable();
    }

    private void mapHotbar() {
        ConcurrentHashMap<Integer, ItemStack> map = new ConcurrentHashMap<Integer, ItemStack>();
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = Replenish.mc.field_71439_g.field_71071_by.func_70301_a(i);
            map.put(i, stack);
        }
        if (this.hotbar.isEmpty()) {
            this.hotbar = map;
            return;
        }
        ConcurrentHashMap<Integer, Integer> fromTo = new ConcurrentHashMap<Integer, Integer>();
        for (Map.Entry<Integer, ItemStack> entry : map.entrySet()) {
            int replenishSlot;
            ItemStack stack = entry.getValue();
            Integer slotKey = entry.getKey();
            if (slotKey == null || stack == null || !stack.field_190928_g && stack.func_77973_b() != Items.field_190931_a && (stack.field_77994_a > this.threshold.getValue() || stack.field_77994_a >= stack.func_77976_d())) continue;
            ItemStack previousStack = entry.getValue();
            if (stack.field_190928_g || stack.func_77973_b() != Items.field_190931_a) {
                previousStack = this.hotbar.get(slotKey);
            }
            if (previousStack == null || previousStack.field_190928_g || previousStack.func_77973_b() == Items.field_190931_a || (replenishSlot = this.getReplenishSlot(previousStack)) == -1) continue;
            fromTo.put(replenishSlot, InventoryUtil.convertHotbarToInv(slotKey));
        }
        if (!fromTo.isEmpty()) {
            for (Map.Entry<Integer, Object> entry : fromTo.entrySet()) {
                this.taskList.add(new InventoryUtil.Task(entry.getKey()));
                this.taskList.add(new InventoryUtil.Task((Integer)entry.getValue()));
                this.taskList.add(new InventoryUtil.Task(entry.getKey()));
                this.taskList.add(new InventoryUtil.Task());
            }
        }
        this.hotbar = map;
    }

    private int getReplenishSlot(ItemStack stack) {
        AtomicInteger slot = new AtomicInteger();
        slot.set(-1);
        for (Map.Entry<Integer, ItemStack> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (entry.getKey() >= 36 || !InventoryUtil.areStacksCompatible(stack, entry.getValue())) continue;
            slot.set(entry.getKey());
            return slot.get();
        }
        return slot.get();
    }
}

