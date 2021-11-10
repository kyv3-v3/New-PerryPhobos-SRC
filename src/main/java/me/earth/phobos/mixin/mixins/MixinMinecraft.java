



package me.earth.phobos.mixin.mixins;

import net.minecraft.client.*;
import javax.annotation.*;
import org.spongepowered.asm.mixin.*;
import org.lwjgl.input.*;
import me.earth.phobos.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import org.lwjgl.opengl.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.client.gui.*;
import me.earth.phobos.features.modules.client.*;
import me.earth.phobos.features.gui.custom.*;
import net.minecraft.crash.*;
import me.earth.phobos.features.modules.render.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.multiplayer.*;
import me.earth.phobos.features.modules.player.*;

@Mixin({ Minecraft.class })
public abstract class MixinMinecraft
{
    @Shadow
    public abstract void displayGuiScreen(@Nullable final GuiScreen p0);
    
    @Inject(method = { "runTickKeyboard" },  at = { @At(value = "FIELD",  target = "Lnet/minecraft/client/Minecraft;currentScreen:Lnet/minecraft/client/gui/GuiScreen;",  ordinal = 0) },  locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onRunTickKeyboard(final CallbackInfo ci,  final int i) {
        if (Keyboard.getEventKeyState() && Phobos.moduleManager != null) {
            Phobos.moduleManager.onKeyPressed(i);
        }
    }
    
    @Inject(method = { "getLimitFramerate" },  at = { @At("HEAD") },  cancellable = true)
    public void getLimitFramerateHook(final CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        try {
            if ((boolean)Management.getInstance().unfocusedCpu.getValue() && !Display.isActive()) {
                callbackInfoReturnable.setReturnValue(Management.getInstance().cpuFPS.getValue());
            }
        }
        catch (NullPointerException ex) {}
    }
    
    @Redirect(method = { "runGameLoop" },  at = @At(value = "INVOKE",  target = "Lorg/lwjgl/opengl/Display;sync(I)V",  remap = false))
    public void syncHook(final int maxFps) {
        if (Management.getInstance().betterFrames.getValue()) {
            Display.sync((int)Management.getInstance().betterFPS.getValue());
        }
        else {
            Display.sync(maxFps);
        }
    }
    
    @Inject(method = { "runTick()V" },  at = { @At("RETURN") })
    private void runTick(final CallbackInfo callbackInfo) {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu && Screens.INSTANCE.isEnabled() && (boolean)Screens.INSTANCE.mainScreen.getValue()) {
            Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new GuiCustomMainScreen());
        }
    }
    
    @Inject(method = { "displayGuiScreen" },  at = { @At("HEAD") })
    private void displayGuiScreen(final GuiScreen screen,  final CallbackInfo ci) {
        if (screen instanceof GuiMainMenu) {
            this.displayGuiScreen((GuiScreen)new GuiCustomMainScreen());
        }
    }
    
    @Redirect(method = { "run" },  at = @At(value = "INVOKE",  target = "Lnet/minecraft/client/Minecraft;displayCrashReport(Lnet/minecraft/crash/CrashReport;)V"))
    public void displayCrashReportHook(final Minecraft minecraft,  final CrashReport crashReport) {
        this.unload();
    }
    
    @Redirect(method = { "runTick" },  at = @At(value = "INVOKE",  target = "Lnet/minecraft/client/multiplayer/WorldClient;doVoidFogParticles(III)V"))
    public void doVoidFogParticlesHook(final WorldClient world,  final int x,  final int y,  final int z) {
        NoRender.getInstance().doVoidFogParticles(x,  y,  z);
    }
    
    @Inject(method = { "shutdown" },  at = { @At("HEAD") })
    public void shutdownHook(final CallbackInfo info) {
        this.unload();
    }
    
    private void unload() {
        System.out.println("Shutting down: saving configuration");
        Phobos.onUnload();
        System.out.println("Configuration saved.");
    }
    
    @Redirect(method = { "sendClickBlockToController" },  at = @At(value = "INVOKE",  target = "Lnet/minecraft/client/entity/EntityPlayerSP;isHandActive()Z"))
    private boolean isHandActiveWrapper(final EntityPlayerSP playerSP) {
        return !MultiTask.getInstance().isOn() && playerSP.isHandActive();
    }
    
    @Redirect(method = { "rightClickMouse" },  at = @At(value = "INVOKE",  target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;getIsHittingBlock()Z",  ordinal = 0))
    private boolean isHittingBlockHook(final PlayerControllerMP playerControllerMP) {
        return !MultiTask.getInstance().isOn() && playerControllerMP.getIsHittingBlock();
    }
    
    @Inject(method = { "middleClickMouse" },  at = { @At("HEAD") },  cancellable = true)
    public void middleClickMouse(final CallbackInfo cancel) {
        if (Phobos.moduleManager.isModuleEnabled((Class)SilentXP.class) || Phobos.moduleManager.isModuleEnabled((Class)MCP.class)) {
            cancel.cancel();
        }
    }
}
