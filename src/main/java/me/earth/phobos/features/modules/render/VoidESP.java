package me.earth.phobos.features.modules.render;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import me.earth.phobos.event.events.Render3DEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.BlockUtil;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.RenderUtil;
import me.earth.phobos.util.RotationUtil;
import me.earth.phobos.util.TimerUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class VoidESP extends Module
{
    private final Setting<Float> radius;
    private final TimerUtil timer;
    private final Setting<Integer> updates;
    private final Setting<Integer> voidCap;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    public Setting<Boolean> air;
    public Setting<Boolean> noEnd;
    public Setting<Boolean> box;
    private final Setting<Integer> boxAlpha;
    public Setting<Boolean> outline;
    private final Setting<Float> lineWidth;
    public Setting<Boolean> colorSync;
    public Setting<Double> height;
    public Setting<Boolean> customOutline;
    private final Setting<Integer> cRed;
    private final Setting<Integer> cGreen;
    private final Setting<Integer> cBlue;
    private final Setting<Integer> cAlpha;
    private List<BlockPos> voidHoles;
    
    public VoidESP() {
        super("VoidEsp",  "Esps the void.",  Module.Category.RENDER,  true,  false,  false);
        this.radius = (Setting<Float>)this.register(new Setting("Radius", 8.0f, 0.0f, 50.0f));
        this.timer = new TimerUtil();
        this.updates = (Setting<Integer>)this.register(new Setting("Updates", 500, 0, 1000));
        this.voidCap = (Setting<Integer>)this.register(new Setting("VoidCap", 500, 0, 1000));
        this.red = (Setting<Integer>)this.register(new Setting("Red", 0, 0, 255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", 255, 0, 255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", 0, 0, 255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", 255, 0, 255));
        this.air = (Setting<Boolean>)this.register(new Setting("OnlyAir", true));
        this.noEnd = (Setting<Boolean>)this.register(new Setting("NoEnd", true));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", true));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", 125, 0, 255,  v -> this.box.getValue()));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", true));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", 1.0f, 0.1f, 5.0f,  v -> this.outline.getValue()));
        this.colorSync = (Setting<Boolean>)this.register(new Setting("Sync", false));
        this.height = (Setting<Double>)this.register(new Setting("Height", 0.0, (-2.0), 2.0));
        this.customOutline = (Setting<Boolean>)this.register(new Setting("CustomLine", false,  v -> this.outline.getValue()));
        this.cRed = (Setting<Integer>)this.register(new Setting("OL-Red", 0, 0, 255,  v -> this.customOutline.getValue() && this.outline.getValue()));
        this.cGreen = (Setting<Integer>)this.register(new Setting("OL-Green", 0, 0, 255,  v -> this.customOutline.getValue() && this.outline.getValue()));
        this.cBlue = (Setting<Integer>)this.register(new Setting("OL-Blue", 255, 0, 255,  v -> this.customOutline.getValue() && this.outline.getValue()));
        this.cAlpha = (Setting<Integer>)this.register(new Setting("OL-Alpha", 255, 0, 255,  v -> this.customOutline.getValue() && this.outline.getValue()));
        this.voidHoles = new CopyOnWriteArrayList<BlockPos>();
    }
    
    public void onToggle() {
        this.timer.reset();
    }
    
    public void onLogin() {
        this.timer.reset();
    }
    
    public void onTick() {
        if (!fullNullCheck() && (!this.noEnd.getValue() || VoidESP.mc.player.dimension != 1) && this.timer.passedMs(this.updates.getValue())) {
            this.voidHoles.clear();
            this.voidHoles = this.findVoidHoles();
            if (this.voidHoles.size() > this.voidCap.getValue()) {
                this.voidHoles.clear();
            }
            this.timer.reset();
        }
    }
    
    public void onRender3D(final Render3DEvent event) {
        if (fullNullCheck() || (this.noEnd.getValue() && VoidESP.mc.player.dimension == 1)) {
            return;
        }
        for (final BlockPos pos : this.voidHoles) {
            if (RotationUtil.isInFov(pos)) {
                continue;
            }
            RenderUtil.drawBoxESP(pos,  new Color(this.red.getValue(),  this.green.getValue(),  this.blue.getValue(),  this.alpha.getValue()),  this.customOutline.getValue(),  new Color(this.cRed.getValue(),  this.cGreen.getValue(),  this.cBlue.getValue(),  this.cAlpha.getValue()),  this.lineWidth.getValue(),  this.outline.getValue(),  this.box.getValue(),  this.boxAlpha.getValue(),  true,  this.height.getValue(),  false,  false,  false,  false,  0);
        }
    }
    
    private List<BlockPos> findVoidHoles() {
        final BlockPos playerPos = EntityUtil.getPlayerPos((EntityPlayer)VoidESP.mc.player);
        return BlockUtil.getDisc(playerPos.add(0,  -playerPos.getY(),  0),  this.radius.getValue()).stream().filter((Predicate<? super Object>)this::isVoid).collect((Collector<? super Object,  ?,  List<BlockPos>>)Collectors.toList());
    }
    
    private boolean isVoid(final BlockPos pos) {
        return (VoidESP.mc.world.getBlockState(pos).getBlock() == Blocks.AIR || (!this.air.getValue() && VoidESP.mc.world.getBlockState(pos).getBlock() != Blocks.BEDROCK)) && pos.getY() < 1 && pos.getY() >= 0;
    }
}
