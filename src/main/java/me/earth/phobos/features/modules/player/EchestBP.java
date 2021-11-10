



package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.inventory.*;

public class EchestBP extends Module
{
    private GuiScreen echestScreen;
    
    public EchestBP() {
        super("EchestBP", "Allows to open your echest later.", Module.Category.PLAYER, false, false, false);
    }
    
    public void onUpdate() {
        final Container container;
        if (EchestBP.mc.currentScreen instanceof GuiContainer && (container = ((GuiContainer)EchestBP.mc.currentScreen).inventorySlots) instanceof ContainerChest && ((ContainerChest)container).getLowerChestInventory() instanceof InventoryBasic && ((ContainerChest)container).getLowerChestInventory().getName().equalsIgnoreCase("Ender Chest")) {
            this.echestScreen = EchestBP.mc.currentScreen;
            EchestBP.mc.currentScreen = null;
        }
    }
    
    public void onDisable() {
        if (!fullNullCheck() && this.echestScreen != null) {
            EchestBP.mc.displayGuiScreen(this.echestScreen);
        }
        this.echestScreen = null;
    }
}
