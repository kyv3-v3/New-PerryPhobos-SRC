



package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;

public class Reach extends Module
{
    private static Reach INSTANCE;
    public Setting<Boolean> override;
    public Setting<Float> add;
    public Setting<Float> reach;
    
    public Reach() {
        super("Reach",  "Extends your block reach.",  Module.Category.PLAYER,  true,  false,  false);
        this.override = (Setting<Boolean>)this.register(new Setting("Override", false));
        this.add = (Setting<Float>)this.register(new Setting("Add", 3.0f,  v -> !this.override.getValue()));
        this.reach = (Setting<Float>)this.register(new Setting("Reach", 6.0f,  v -> this.override.getValue()));
        this.setInstance();
    }
    
    public static Reach getInstance() {
        if (Reach.INSTANCE == null) {
            Reach.INSTANCE = new Reach();
        }
        return Reach.INSTANCE;
    }
    
    private void setInstance() {
        Reach.INSTANCE = this;
    }
    
    public String getDisplayInfo() {
        return this.override.getValue() ? this.reach.getValue().toString() : this.add.getValue().toString();
    }
    
    static {
        Reach.INSTANCE = new Reach();
    }
}
