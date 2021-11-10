



package me.earth.phobos.features.modules.render;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;

public class ItemPhysics extends Module
{
    public static ItemPhysics INSTANCE;
    public final Setting<Float> Scaling;
    
    public ItemPhysics() {
        super("ItemPhysics",  "Apply physics to items.",  Module.Category.RENDER,  false,  false,  false);
        this.Scaling = (Setting<Float>)this.register(new Setting("Scaling", 0.5f, 0.0f, 10.0f));
        this.setInstance();
    }
    
    public static ItemPhysics getInstance() {
        if (ItemPhysics.INSTANCE == null) {
            ItemPhysics.INSTANCE = new ItemPhysics();
        }
        return ItemPhysics.INSTANCE;
    }
    
    private void setInstance() {
        ItemPhysics.INSTANCE = this;
    }
    
    static {
        ItemPhysics.INSTANCE = new ItemPhysics();
    }
}
