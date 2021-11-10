



package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.util.*;
import net.minecraft.item.*;
import java.util.concurrent.*;
import me.earth.phobos.features.modules.combat.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.init.*;
import java.util.*;
import java.util.concurrent.atomic.*;

public class Replenish extends Module
{
    private final Setting<Integer> threshold;
    private final Setting<Integer> replenishments;
    private final Setting<Integer> updates;
    private final Setting<Integer> actions;
    private final Setting<Boolean> pauseInv;
    private final Setting<Boolean> putBack;
    private final TimerUtil timer;
    private final TimerUtil replenishTimer;
    private final Queue<InventoryUtil.Task> taskList;
    private Map<Integer,  ItemStack> hotbar;
    
    public Replenish() {
        super("Replenish",  "Replenishes your hotbar.",  Module.Category.PLAYER,  false,  false,  false);
        this.threshold = (Setting<Integer>)this.register(new Setting("Threshold", 0, 0, 63));
        this.replenishments = (Setting<Integer>)this.register(new Setting("RUpdates", 0, 0, 1000));
        this.updates = (Setting<Integer>)this.register(new Setting("HBUpdates", 100, 0, 1000));
        this.actions = (Setting<Integer>)this.register(new Setting("Actions", 2, 1, 30));
        this.pauseInv = (Setting<Boolean>)this.register(new Setting("PauseInv", true));
        this.putBack = (Setting<Boolean>)this.register(new Setting("PutBack", true));
        this.timer = new TimerUtil();
        this.replenishTimer = new TimerUtil();
        this.taskList = new ConcurrentLinkedQueue<InventoryUtil.Task>();
        this.hotbar = new ConcurrentHashMap<Integer,  ItemStack>();
    }
    
    public void onUpdate() {
        if (Auto32k.getInstance().isOn() && (!Auto32k.getInstance().autoSwitch.getValue() || Auto32k.getInstance().switching)) {
            return;
        }
        if (Replenish.mc.currentScreen instanceof GuiContainer && (!(Replenish.mc.currentScreen instanceof GuiInventory) || this.pauseInv.getValue())) {
            return;
        }
        if (this.timer.passedMs(this.updates.getValue())) {
            this.mapHotbar();
        }
        if (this.replenishTimer.passedMs(this.replenishments.getValue())) {
            for (int i = 0; i < this.actions.getValue(); ++i) {
                final InventoryUtil.Task task = this.taskList.poll();
                if (task != null) {
                    task.run();
                }
            }
            this.replenishTimer.reset();
        }
    }
    
    public void onDisable() {
        this.hotbar.clear();
    }
    
    public void onLogout() {
        this.onDisable();
    }
    
    private void mapHotbar() {
        final ConcurrentHashMap<Integer,  ItemStack> map = new ConcurrentHashMap<Integer,  ItemStack>();
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Replenish.mc.player.inventory.getStackInSlot(i);
            map.put(i,  stack);
        }
        if (this.hotbar.isEmpty()) {
            this.hotbar = map;
            return;
        }
        final ConcurrentHashMap<Integer,  Integer> fromTo = new ConcurrentHashMap<Integer,  Integer>();
        for (final Map.Entry<Integer,  ItemStack> hotbarItem : map.entrySet()) {
            final ItemStack stack2 = hotbarItem.getValue();
            final Integer slotKey = hotbarItem.getKey();
            if (slotKey != null && stack2 != null) {
                if (!stack2.isEmpty && stack2.getItem() != Items.AIR) {
                    if (stack2.stackSize > this.threshold.getValue()) {
                        continue;
                    }
                    if (stack2.stackSize >= stack2.getMaxStackSize()) {
                        continue;
                    }
                }
                ItemStack previousStack = hotbarItem.getValue();
                if (stack2.isEmpty || stack2.getItem() != Items.AIR) {
                    previousStack = this.hotbar.get(slotKey);
                }
                if (previousStack == null || previousStack.isEmpty || previousStack.getItem() == Items.AIR) {
                    continue;
                }
                final int replenishSlot;
                if ((replenishSlot = this.getReplenishSlot(previousStack)) == -1) {
                    continue;
                }
                fromTo.put(replenishSlot,  InventoryUtil.convertHotbarToInv(slotKey));
            }
        }
        if (!fromTo.isEmpty()) {
            for (final Map.Entry<Integer,  Integer> slotMove : fromTo.entrySet()) {
                this.taskList.add(new InventoryUtil.Task(slotMove.getKey()));
                this.taskList.add(new InventoryUtil.Task(slotMove.getValue()));
                this.taskList.add(new InventoryUtil.Task(slotMove.getKey()));
                this.taskList.add(new InventoryUtil.Task());
            }
        }
        this.hotbar = map;
    }
    
    private int getReplenishSlot(final ItemStack stack) {
        final AtomicInteger slot = new AtomicInteger();
        slot.set(-1);
        for (final Map.Entry<Integer,  ItemStack> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (entry.getKey() < 36) {
                if (!InventoryUtil.areStacksCompatible(stack,  entry.getValue())) {
                    continue;
                }
                slot.set(entry.getKey());
                return slot.get();
            }
        }
        return slot.get();
    }
}
