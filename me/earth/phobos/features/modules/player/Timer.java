//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.util.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.*;

public class Timer extends Module
{
    private final TimerUtil timer;
    private final TimerUtil turnOffTimer;
    public Setting<Boolean> autoOff;
    public Setting<Integer> timeLimit;
    public Setting<TimerMode> mode;
    public Setting<Float> timerSpeed;
    public Setting<Float> fastSpeed;
    public Setting<Integer> fastTime;
    public Setting<Integer> slowTime;
    public Setting<Boolean> startFast;
    public float speed;
    private boolean fast;
    
    public Timer() {
        super("Timer", "Will speed up the game.", Module.Category.PLAYER, false, false, false);
        this.timer = new TimerUtil();
        this.turnOffTimer = new TimerUtil();
        this.autoOff = (Setting<Boolean>)this.register(new Setting("AutoOff", (T)false));
        this.timeLimit = (Setting<Integer>)this.register(new Setting("Limit", (T)250, (T)1, (T)2500, v -> this.autoOff.getValue()));
        this.mode = (Setting<TimerMode>)this.register(new Setting("Mode", (T)TimerMode.NORMAL));
        this.timerSpeed = (Setting<Float>)this.register(new Setting("Speed", (T)4.0f, (T)0.1f, (T)20.0f));
        this.fastSpeed = (Setting<Float>)this.register(new Setting("Fast", (T)10.0f, (T)0.1f, (T)100.0f, v -> this.mode.getValue() == TimerMode.SWITCH, "Fast Speed for switch."));
        this.fastTime = (Setting<Integer>)this.register(new Setting("FastTime", (T)20, (T)1, (T)500, v -> this.mode.getValue() == TimerMode.SWITCH, "How long you want to go fast.(ms * 10)"));
        this.slowTime = (Setting<Integer>)this.register(new Setting("SlowTime", (T)20, (T)1, (T)500, v -> this.mode.getValue() == TimerMode.SWITCH, "Recover from too fast.(ms * 10)"));
        this.startFast = (Setting<Boolean>)this.register(new Setting("StartFast", (T)false, v -> this.mode.getValue() == TimerMode.SWITCH));
        this.speed = 1.0f;
    }
    
    public void onEnable() {
        this.turnOffTimer.reset();
        this.speed = this.timerSpeed.getValue();
        if (!this.startFast.getValue()) {
            this.timer.reset();
        }
    }
    
    public void onUpdate() {
        if (this.autoOff.getValue() && this.turnOffTimer.passedMs(this.timeLimit.getValue())) {
            this.disable();
            return;
        }
        if (this.mode.getValue() == TimerMode.NORMAL) {
            this.speed = this.timerSpeed.getValue();
            return;
        }
        if (!this.fast && this.timer.passedDms(this.slowTime.getValue())) {
            this.fast = true;
            this.speed = this.fastSpeed.getValue();
            this.timer.reset();
        }
        if (this.fast && this.timer.passedDms(this.fastTime.getValue())) {
            this.fast = false;
            this.speed = this.timerSpeed.getValue();
            this.timer.reset();
        }
    }
    
    public void onDisable() {
        this.speed = 1.0f;
        Phobos.timerManager.reset();
        this.fast = false;
    }
    
    public String getDisplayInfo() {
        return this.timerSpeed.getValueAsString();
    }
    
    public enum TimerMode
    {
        NORMAL, 
        SWITCH;
    }
}
