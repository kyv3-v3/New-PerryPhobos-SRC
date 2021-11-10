



package me.earth.phobos.features.modules.client;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import java.awt.*;
import me.earth.phobos.features.command.*;
import me.earth.phobos.event.events.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.earth.phobos.*;

public class CustomFont extends Module
{
    private static CustomFont INSTANCE;
    public Setting<String> fontName;
    public Setting<Integer> fontSize;
    public Setting<Integer> fontStyle;
    public Setting<Boolean> antiAlias;
    public Setting<Boolean> fractionalMetrics;
    public Setting<Boolean> shadow;
    public Setting<Boolean> showFonts;
    public Setting<Boolean> full;
    private boolean reloadFont;
    
    public CustomFont() {
        super("CustomFont",  "CustomFont for all of the clients text. Use the font command.",  Category.CLIENT,  true,  false,  false);
        this.fontName = (Setting<String>)this.register(new Setting("FontName", "Arial",  "Name of the font."));
        this.fontSize = (Setting<Integer>)this.register(new Setting("FontSize", 18,  "Size of the font."));
        this.fontStyle = (Setting<Integer>)this.register(new Setting("FontStyle", 0,  "Style of the font."));
        this.antiAlias = (Setting<Boolean>)this.register(new Setting("AntiAlias", true,  "Smoother font."));
        this.fractionalMetrics = (Setting<Boolean>)this.register(new Setting("Metrics", true,  "Thinner font."));
        this.shadow = (Setting<Boolean>)this.register(new Setting("Shadow", true,  "Less shadow offset font."));
        this.showFonts = (Setting<Boolean>)this.register(new Setting("Fonts", false,  "Shows all fonts."));
        this.full = (Setting<Boolean>)this.register(new Setting("Full", false));
        this.setInstance();
    }
    
    public static CustomFont getInstance() {
        if (CustomFont.INSTANCE == null) {
            CustomFont.INSTANCE = new CustomFont();
        }
        return CustomFont.INSTANCE;
    }
    
    public static boolean checkFont(final String font,  final boolean message) {
        for (final String s : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
            if (!message && s.equals(font)) {
                return true;
            }
            if (message) {
                Command.sendMessage(s);
            }
        }
        return false;
    }
    
    private void setInstance() {
        CustomFont.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onSettingChange(final ClientEvent event) {
        final Setting setting;
        if (event.getStage() == 2 && (setting = event.getSetting()) != null && setting.getFeature().equals(this)) {
            if (setting.getName().equals("FontName") && !checkFont(setting.getPlannedValue().toString(),  false)) {
                Command.sendMessage("§cThat font doesnt exist.");
                event.setCanceled(true);
                return;
            }
            this.reloadFont = true;
        }
    }
    
    @Override
    public void onTick() {
        if (this.showFonts.getValue()) {
            checkFont("Hello",  true);
            Command.sendMessage("Current Font: " + this.fontName.getValue());
            this.showFonts.setValue(false);
        }
        if (this.reloadFont) {
            Phobos.textManager.init(false);
            this.reloadFont = false;
        }
    }
    
    static {
        CustomFont.INSTANCE = new CustomFont();
    }
}
