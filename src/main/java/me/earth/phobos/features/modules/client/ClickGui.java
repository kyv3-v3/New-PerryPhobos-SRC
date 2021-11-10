



package me.earth.phobos.features.modules.client;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.client.settings.*;
import me.earth.phobos.event.events.*;
import me.earth.phobos.*;
import me.earth.phobos.features.command.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.earth.phobos.features.gui.*;
import net.minecraft.client.gui.*;
import net.minecraft.util.*;

public class ClickGui extends Module
{
    private static ClickGui INSTANCE;
    public Setting<Boolean> colorSync;
    public Setting<Boolean> outline;
    public Setting<Boolean> rainbowRolling;
    public Setting<String> prefix;
    public Setting<Boolean> blurEffect;
    public Setting<Boolean> boxing;
    public Setting<Boolean> gear;
    public Setting<Boolean> desc;
    public Setting<Boolean> bg;
    public Setting<Integer> bgtint;
    public Setting<Boolean> scroll;
    public Setting<Integer> scrollval;
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Integer> hoverAlpha;
    public Setting<Integer> alpha;
    public Setting<Boolean> customFov;
    public Setting<Float> fov;
    public Setting<Boolean> openCloseChange;
    public Setting<String> open;
    public Setting<String> close;
    public Setting<String> moduleButton;
    public Setting<Boolean> devSettings;
    public Setting<Integer> topRed;
    public Setting<Integer> topGreen;
    public Setting<Integer> topBlue;
    public Setting<Integer> topAlpha;
    
    public ClickGui() {
        super("ClickGui", "Opens the ClickGui.", Category.CLIENT, true, false, false);
        this.colorSync = (Setting<Boolean>)this.register(new Setting("Sync", (T)false));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", (T)false));
        this.rainbowRolling = (Setting<Boolean>)this.register(new Setting("RollingRainbow", (T)false, v -> this.colorSync.getValue() && Colors.INSTANCE.rainbow.getValue()));
        this.prefix = (Setting<String>)this.register((Setting)new Setting<String>("Prefix", ".").setRenderName(true));
        this.blurEffect = (Setting<Boolean>)this.register(new Setting("Blur", (T)false));
        this.boxing = (Setting<Boolean>)this.register(new Setting("Boxing", (T)true));
        this.gear = (Setting<Boolean>)this.register(new Setting("Gears", (T)false));
        this.desc = (Setting<Boolean>)this.register(new Setting("Descriptions", (T)true));
        this.bg = (Setting<Boolean>)this.register(new Setting("Background", (T)true));
        this.bgtint = (Setting<Integer>)this.register(new Setting("Tint", (T)127, (T)0, (T)127, v -> this.bg.getValue()));
        this.scroll = (Setting<Boolean>)this.register(new Setting("Scroll", (T)true));
        this.scrollval = (Setting<Integer>)this.register(new Setting("Scroll Speed", (T)10, (T)1, (T)20, v -> this.scroll.getValue()));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)255, (T)0, (T)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)0, (T)0, (T)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)0, (T)0, (T)255));
        this.hoverAlpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)180, (T)0, (T)255));
        this.alpha = (Setting<Integer>)this.register(new Setting("HoverAlpha", (T)240, (T)0, (T)255));
        this.customFov = (Setting<Boolean>)this.register(new Setting("CustomFov", (T)false));
        this.fov = (Setting<Float>)this.register(new Setting("Fov", (T)150.0f, (T)(-180.0f), (T)180.0f, v -> this.customFov.getValue()));
        this.openCloseChange = (Setting<Boolean>)this.register(new Setting("Open/Close", (T)false));
        this.open = (Setting<String>)this.register((Setting)new Setting<Object>("Open:", "", v -> this.openCloseChange.getValue()).setRenderName(true));
        this.close = (Setting<String>)this.register((Setting)new Setting<Object>("Close:", "", v -> this.openCloseChange.getValue()).setRenderName(true));
        this.moduleButton = (Setting<String>)this.register((Setting)new Setting<Object>("Buttons:", "", v -> !this.openCloseChange.getValue()).setRenderName(true));
        this.devSettings = (Setting<Boolean>)this.register(new Setting("DevSettings", (T)false));
        this.topRed = (Setting<Integer>)this.register(new Setting("TopRed", (T)255, (T)0, (T)255, v -> this.devSettings.getValue()));
        this.topGreen = (Setting<Integer>)this.register(new Setting("TopGreen", (T)0, (T)0, (T)255, v -> this.devSettings.getValue()));
        this.topBlue = (Setting<Integer>)this.register(new Setting("TopBlue", (T)0, (T)0, (T)255, v -> this.devSettings.getValue()));
        this.topAlpha = (Setting<Integer>)this.register(new Setting("TopAlpha", (T)255, (T)0, (T)255, v -> this.devSettings.getValue()));
        this.setInstance();
    }
    
    public static ClickGui getInstance() {
        if (ClickGui.INSTANCE == null) {
            ClickGui.INSTANCE = new ClickGui();
        }
        return ClickGui.INSTANCE;
    }
    
    private void setInstance() {
        ClickGui.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        if (this.customFov.getValue()) {
            ClickGui.mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, (float)this.fov.getValue());
        }
    }
    
    @SubscribeEvent
    public void onSettingChange(final ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(this.prefix)) {
                Phobos.commandManager.setPrefix(this.prefix.getPlannedValue());
                Command.sendMessage("Prefix set to §a" + Phobos.commandManager.getPrefix());
            }
            Phobos.colorManager.setColor(this.red.getPlannedValue(), this.green.getPlannedValue(), this.blue.getPlannedValue(), this.hoverAlpha.getPlannedValue());
        }
    }
    
    @Override
    public void onEnable() {
        ClickGui.mc.displayGuiScreen((GuiScreen)new PhobosGui());
        if (this.blurEffect.getValue()) {
            ClickGui.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
    }
    
    @Override
    public void onLoad() {
        if (this.colorSync.getValue()) {
            Phobos.colorManager.setColor(Colors.INSTANCE.getCurrentColor().getRed(), Colors.INSTANCE.getCurrentColor().getGreen(), Colors.INSTANCE.getCurrentColor().getBlue(), this.hoverAlpha.getValue());
        }
        else {
            Phobos.colorManager.setColor(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.hoverAlpha.getValue());
        }
        Phobos.commandManager.setPrefix(this.prefix.getValue());
    }
    
    @Override
    public void onTick() {
        if (!(ClickGui.mc.currentScreen instanceof PhobosGui)) {
            this.disable();
            if (ClickGui.mc.entityRenderer.getShaderGroup() != null) {
                ClickGui.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
        }
    }
    
    @Override
    public void onDisable() {
        if (ClickGui.mc.currentScreen instanceof PhobosGui) {
            ClickGui.mc.displayGuiScreen((GuiScreen)null);
        }
    }
    
    static {
        ClickGui.INSTANCE = new ClickGui();
    }
}
