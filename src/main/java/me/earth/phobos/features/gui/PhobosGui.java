



package me.earth.phobos.features.gui;

import net.minecraft.client.gui.*;
import me.earth.phobos.features.gui.components.*;
import me.earth.phobos.*;
import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.gui.components.items.buttons.*;
import me.earth.phobos.features.*;
import java.util.function.*;
import java.util.*;
import me.earth.phobos.features.gui.components.items.*;
import me.earth.phobos.features.modules.client.*;
import org.lwjgl.opengl.*;
import java.awt.*;
import me.earth.phobos.util.*;
import org.lwjgl.input.*;
import java.io.*;

public class PhobosGui extends GuiScreen
{
    private static PhobosGui INSTANCE;
    private final ArrayList<Component> components;
    
    public PhobosGui() {
        this.components = new ArrayList<Component>();
        this.setInstance();
        this.load();
    }
    
    public static PhobosGui getInstance() {
        if (PhobosGui.INSTANCE == null) {
            PhobosGui.INSTANCE = new PhobosGui();
        }
        return PhobosGui.INSTANCE;
    }
    
    public static PhobosGui getClickGui() {
        return getInstance();
    }
    
    private void setInstance() {
        PhobosGui.INSTANCE = this;
    }
    
    private void load() {
        int x = -84;
        for (final Module.Category category : Phobos.moduleManager.getCategories()) {
            final ArrayList<Component> components2 = this.components;
            final String name = category.getName();
            x += 90;
            components2.add(new Component(name, x, 4, true) {
                public void setupItems() {
                    Phobos.moduleManager.getModulesByCategory(category).forEach(module -> {
                        if (!module.hidden) {
                            this.addButton((Button)new ModuleButton(module));
                        }
                    });
                }
            });
        }
        this.components.forEach(components -> components.getItems().sort(Comparator.comparing((Function<? super E, ? extends Comparable>)Feature::getName)));
    }
    
    public void updateModule(final Module module) {
        for (final Component component : this.components) {
            for (final Item item : component.getItems()) {
                if (!(item instanceof ModuleButton)) {
                    continue;
                }
                final ModuleButton button = (ModuleButton)item;
                final Module mod = button.getModule();
                if (module == null) {
                    continue;
                }
                if (!module.equals(mod)) {
                    continue;
                }
                button.initSettings();
                break;
            }
        }
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.checkMouseWheel();
        if (ClickGui.getInstance().bg.getValue()) {
            RenderUtil.drawRect(0.0f, 0.0f, (float)Display.getWidth(), (float)Display.getHeight(), new Color(0, 0, 0, ClickGui.getInstance().bgtint.getValue()).getRGB());
        }
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int clickedButton) {
        this.components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int releaseButton) {
        this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
    }
    
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    public final ArrayList<Component> getComponents() {
        return this.components;
    }
    
    public void checkMouseWheel() {
        final int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            if (ClickGui.getInstance().scroll.getValue()) {
                this.components.forEach(component -> component.setY(component.getY() - ClickGui.getInstance().scrollval.getValue()));
            }
        }
        else if (dWheel > 0 && ClickGui.getInstance().scroll.getValue()) {
            this.components.forEach(component -> component.setY(component.getY() + ClickGui.getInstance().scrollval.getValue()));
        }
    }
    
    public int getTextOffset() {
        return -6;
    }
    
    public Component getComponentByName(final String name) {
        for (final Component component : this.components) {
            if (!component.getName().equalsIgnoreCase(name)) {
                continue;
            }
            return component;
        }
        return null;
    }
    
    public void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
    }
    
    static {
        PhobosGui.INSTANCE = new PhobosGui();
    }
}
