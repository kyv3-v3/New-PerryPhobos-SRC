



package me.earth.phobos.manager;

import me.earth.phobos.features.*;
import me.earth.phobos.features.modules.*;
import java.awt.*;
import me.earth.phobos.features.modules.client.*;
import me.earth.phobos.features.modules.misc.*;
import me.earth.phobos.features.modules.combat.*;
import me.earth.phobos.features.modules.render.*;
import me.earth.phobos.features.modules.movement.*;
import me.earth.phobos.features.modules.player.*;
import net.minecraftforge.common.*;
import me.earth.phobos.event.events.*;
import java.util.*;
import java.util.stream.*;
import java.util.function.*;
import org.lwjgl.input.*;
import me.earth.phobos.features.gui.*;

public class ModuleManager extends Feature
{
    public ArrayList<Module> modules;
    public List<Module> sortedModules;
    public List<Module> alphabeticallySortedModules;
    public Map<Module, Color> moduleColorMap;
    
    public ModuleManager() {
        this.modules = new ArrayList<Module>();
        this.sortedModules = new ArrayList<Module>();
        this.alphabeticallySortedModules = new ArrayList<Module>();
        this.moduleColorMap = new HashMap<Module, Color>();
    }
    
    public void init() {
        this.modules.add((Module)new Offhand());
        this.modules.add((Module)new Surround());
        this.modules.add((Module)new AutoTrap());
        this.modules.add((Module)new AutoCrystal());
        this.modules.add((Module)new Criticals());
        this.modules.add((Module)new BowSpam());
        this.modules.add((Module)new Killaura());
        this.modules.add((Module)new HoleFiller());
        this.modules.add((Module)new Selftrap());
        this.modules.add((Module)new Webaura());
        this.modules.add((Module)new AutoArmor());
        this.modules.add((Module)new AntiTrap());
        this.modules.add((Module)new BedBomb());
        this.modules.add((Module)new ArmorMessage());
        this.modules.add((Module)new CrystalCrash());
        this.modules.add((Module)new Auto32k());
        this.modules.add((Module)new AntiCrystal());
        this.modules.add((Module)new AnvilAura());
        this.modules.add((Module)new GodModule());
        this.modules.add((Module)new NoteBot());
        this.modules.add((Module)new ChatModifier());
        this.modules.add((Module)new BetterPortals());
        this.modules.add((Module)new BuildHeight());
        this.modules.add((Module)new NoHandShake());
        this.modules.add((Module)new AutoRespawn());
        this.modules.add((Module)new NoRotate());
        this.modules.add((Module)new MCF());
        this.modules.add((Module)new PingSpoof());
        this.modules.add((Module)new NoSoundLag());
        this.modules.add((Module)new AutoLog());
        this.modules.add((Module)new KitDelete());
        this.modules.add((Module)new Exploits());
        this.modules.add((Module)new Spammer());
        this.modules.add((Module)new AntiVanish());
        this.modules.add((Module)new ExtraTab());
        this.modules.add((Module)new MobOwner());
        this.modules.add((Module)new Nuker());
        this.modules.add((Module)new AutoReconnect());
        this.modules.add((Module)new AntiAFK());
        this.modules.add((Module)new Tracker());
        this.modules.add((Module)new AntiPackets());
        this.modules.add((Module)new Logger());
        this.modules.add((Module)new RPC());
        this.modules.add((Module)new AutoGG());
        this.modules.add((Module)new Godmode());
        this.modules.add((Module)new Companion());
        this.modules.add((Module)new EntityControl());
        this.modules.add((Module)new ReverseStep());
        this.modules.add((Module)new Bypass());
        this.modules.add((Module)new Strafe());
        this.modules.add((Module)new Velocity());
        this.modules.add((Module)new Speed());
        this.modules.add((Module)new Step());
        this.modules.add((Module)new StepOld());
        this.modules.add((Module)new Sprint());
        this.modules.add((Module)new AntiLevitate());
        this.modules.add((Module)new Phase());
        this.modules.add((Module)new Static());
        this.modules.add((Module)new TPSpeed());
        this.modules.add((Module)new Flight());
        this.modules.add((Module)new ElytraFlight());
        this.modules.add((Module)new NoSlowDown());
        this.modules.add((Module)new HoleTP());
        this.modules.add((Module)new NoFall());
        this.modules.add((Module)new IceSpeed());
        this.modules.add((Module)new AutoWalk());
        this.modules.add((Module)new Packetfly());
        this.modules.add((Module)new LongJump());
        this.modules.add((Module)new BlockLag());
        this.modules.add((Module)new FastSwim());
        this.modules.add((Module)new StairSpeed());
        this.modules.add((Module)new BoatFly());
        this.modules.add((Module)new VanillaSpeed());
        this.modules.add((Module)new Reach());
        this.modules.add((Module)new LiquidInteract());
        this.modules.add((Module)new FakePlayer());
        this.modules.add((Module)new Timer());
        this.modules.add((Module)new FastPlace());
        this.modules.add((Module)new Freecam());
        this.modules.add((Module)new Speedmine());
        this.modules.add((Module)new SafeWalk());
        this.modules.add((Module)new Blink());
        this.modules.add((Module)new MultiTask());
        this.modules.add((Module)new BlockTweaks());
        this.modules.add((Module)new XCarry());
        this.modules.add((Module)new Replenish());
        this.modules.add((Module)new AntiHunger());
        this.modules.add((Module)new Jesus());
        this.modules.add((Module)new Scaffold());
        this.modules.add((Module)new EchestBP());
        this.modules.add((Module)new TpsSync());
        this.modules.add((Module)new MCP());
        this.modules.add((Module)new TrueDurability());
        this.modules.add((Module)new Yaw());
        this.modules.add((Module)new AntiDDoS());
        this.modules.add((Module)new StorageESP());
        this.modules.add((Module)new NoRender());
        this.modules.add((Module)new SmallShield());
        this.modules.add((Module)new Fullbright());
        this.modules.add((Module)new CameraClip());
        this.modules.add((Module)new Chams());
        this.modules.add((Module)new Skeleton());
        this.modules.add((Module)new ESP());
        this.modules.add((Module)new HoleESP());
        this.modules.add((Module)new BlockHighlight());
        this.modules.add((Module)new Trajectories());
        this.modules.add((Module)new Tracers());
        this.modules.add((Module)new LogoutSpots());
        this.modules.add((Module)new XRay());
        this.modules.add((Module)new PortalESP());
        this.modules.add((Module)new Ranges());
        this.modules.add((Module)new ArrowESP());
        this.modules.add((Module)new HandColor());
        this.modules.add((Module)new VoidESP());
        this.modules.add((Module)new Cosmetics());
        this.modules.add((Module)new CrystalModifier());
        this.modules.add((Module)new Notifications());
        this.modules.add((Module)new HUD());
        this.modules.add((Module)new ToolTips());
        this.modules.add((Module)new CustomFont());
        this.modules.add((Module)new ClickGui());
        this.modules.add((Module)new Management());
        this.modules.add((Module)new Components());
        this.modules.add((Module)new StreamerMode());
        this.modules.add((Module)new Capes());
        this.modules.add((Module)new Colors());
        this.modules.add((Module)new PingBypass());
        this.modules.add((Module)new Screens());
        this.modules.add((Module)new Media());
        this.modules.add((Module)new PhobosChat());
        this.modules.add((Module)new SelfCrystal());
        this.modules.add((Module)new PhysicsCapes());
        this.modules.add((Module)new ShoulderEntity());
        this.modules.add((Module)new Announcer());
        this.modules.add((Module)new Translator());
        this.modules.add((Module)new me.earth.phobos.features.modules.render.Cosmetics());
        this.modules.add((Module)new Trails());
        this.modules.add((Module)new Nametags());
        this.modules.add((Module)new TestNametags());
        this.modules.add((Module)new Aspect());
        this.modules.add((Module)new Anchor());
        this.modules.add((Module)new Shaders());
        this.modules.add((Module)new Animations());
        this.modules.add((Module)new ViewModel());
        this.modules.add((Module)new GlintModify());
        this.modules.add((Module)new AutoCity());
        this.modules.add((Module)new AirJump());
        this.modules.add((Module)new AirPlace());
        this.modules.add((Module)new SelfAnvil());
        this.modules.add((Module)new SilentXP());
        this.modules.add((Module)new SkyColor());
        this.modules.add((Module)new Quiver());
        this.modules.add((Module)new OffhandRewrite());
        this.modules.add((Module)new AutoMinecart());
        this.modules.add((Module)new Rubberband());
        this.modules.add((Module)new PackReload());
        this.modules.add((Module)new NoEntityTrace());
        this.modules.add((Module)new PacketMend());
        this.modules.add((Module)new Flatten());
        this.modules.add((Module)new BowAim());
        this.modules.add((Module)new ItemPhysics());
        this.modules.add((Module)new ChorusESP());
        this.modules.add((Module)new AutoBuilder());
        this.modules.add((Module)new PopChams());
        this.modules.add((Module)new PacketFlyNew());
        this.modules.add((Module)new PluginsGrabber());
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)AntiTrap.class), new Color(128, 53, 69));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)AnvilAura.class), new Color(90, 227, 96));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)ArmorMessage.class), new Color(255, 51, 51));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Auto32k.class), new Color(185, 212, 144));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)AutoArmor.class), new Color(74, 227, 206));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)AutoCrystal.class), new Color(255, 15, 43));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)AutoTrap.class), new Color(193, 49, 244));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)BedBomb.class), new Color(185, 80, 195));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)BowSpam.class), new Color(204, 191, 153));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)CrystalCrash.class), new Color(208, 66, 9));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Criticals.class), new Color(204, 151, 184));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)HoleFiller.class), new Color(166, 55, 110));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Killaura.class), new Color(255, 37, 0));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Offhand.class), new Color(185, 212, 144));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Selftrap.class), new Color(22, 127, 145));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Surround.class), new Color(100, 0, 150));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Webaura.class), new Color(11, 161, 121));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)AntiCrystal.class), new Color(255, 161, 121));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)AntiPackets.class), new Color(155, 186, 115));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)AntiVanish.class), new Color(25, 209, 135));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)AutoGG.class), new Color(240, 49, 110));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)AutoLog.class), new Color(176, 176, 176));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)AutoReconnect.class), new Color(17, 85, 153));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)BetterPortals.class), new Color(71, 214, 187));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)BuildHeight.class), new Color(64, 136, 199));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Bypass.class), new Color(194, 214, 81));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Companion.class), new Color(140, 252, 146));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)ChatModifier.class), new Color(255, 59, 216));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Exploits.class), new Color(255, 0, 0));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)ExtraTab.class), new Color(161, 113, 173));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Godmode.class), new Color(1, 35, 95));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)KitDelete.class), new Color(229, 194, 255));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Logger.class), new Color(186, 0, 109));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)MCF.class), new Color(17, 85, 255));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)MobOwner.class), new Color(255, 254, 204));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)AntiAFK.class), new Color(80, 5, 98));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)NoHandShake.class), new Color(173, 232, 139));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)NoRotate.class), new Color(69, 81, 223));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)NoSoundLag.class), new Color(255, 56, 0));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Nuker.class), new Color(152, 169, 17));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)PingSpoof.class), new Color(23, 214, 187));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)RPC.class), new Color(0, 64, 255));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Spammer.class), new Color(140, 87, 166));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)ToolTips.class), new Color(209, 125, 156));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Translator.class), new Color(74, 82, 15));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Tracker.class), new Color(0, 255, 225));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)ArrowESP.class), new Color(193, 219, 20));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)BlockHighlight.class), new Color(103, 182, 224));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)CameraClip.class), new Color(247, 169, 107));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Chams.class), new Color(34, 152, 34));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)ESP.class), new Color(255, 27, 155));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Fullbright.class), new Color(255, 164, 107));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)HandColor.class), new Color(96, 138, 92));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)HoleESP.class), new Color(95, 83, 130));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)LogoutSpots.class), new Color(2, 135, 134));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Nametags.class), new Color(98, 82, 223));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)NoRender.class), new Color(255, 164, 107));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)PortalESP.class), new Color(26, 242, 62));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Ranges.class), new Color(144, 212, 196));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Skeleton.class), new Color(219, 219, 219));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)SmallShield.class), new Color(145, 223, 187));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)StorageESP.class), new Color(97, 81, 223));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Tracers.class), new Color(255, 107, 107));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Trajectories.class), new Color(98, 18, 223));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)VoidESP.class), new Color(68, 178, 142));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)XRay.class), new Color(217, 118, 37));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)AntiLevitate.class), new Color(206, 255, 255));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)AutoWalk.class), new Color(153, 153, 170));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)ElytraFlight.class), new Color(55, 161, 201));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Flight.class), new Color(186, 164, 178));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)HoleTP.class), new Color(68, 178, 142));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)IceSpeed.class), new Color(33, 193, 247));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)LongJump.class), new Color(228, 27, 213));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)NoFall.class), new Color(61, 204, 78));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)NoSlowDown.class), new Color(61, 204, 78));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Packetfly.class), new Color(238, 59, 27));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Phase.class), new Color(186, 144, 212));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)SafeWalk.class), new Color(182, 186, 164));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Speed.class), new Color(55, 161, 196));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Sprint.class), new Color(148, 184, 142));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Static.class), new Color(86, 53, 98));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Step.class), new Color(144, 212, 203));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)StepOld.class), new Color(144, 212, 203));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Strafe.class), new Color(0, 204, 255));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)TPSpeed.class), new Color(20, 177, 142));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Velocity.class), new Color(115, 134, 140));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)ReverseStep.class), new Color(1, 134, 140));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)AntiDDoS.class), new Color(67, 191, 181));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Blink.class), new Color(144, 184, 141));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)BlockTweaks.class), new Color(89, 223, 235));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)EchestBP.class), new Color(255, 243, 30));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)FakePlayer.class), new Color(37, 192, 170));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)FastPlace.class), new Color(217, 118, 37));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Freecam.class), new Color(206, 232, 128));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Jesus.class), new Color(136, 221, 235));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)LiquidInteract.class), new Color(85, 223, 235));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)MCP.class), new Color(153, 68, 170));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)MultiTask.class), new Color(17, 223, 235));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)AntiHunger.class), new Color(86, 53, 98));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Reach.class), new Color(9, 223, 187));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Replenish.class), new Color(153, 223, 235));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Scaffold.class), new Color(152, 166, 113));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Speedmine.class), new Color(152, 166, 113));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Timer.class), new Color(255, 133, 18));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)TpsSync.class), new Color(93, 144, 153));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)TrueDurability.class), new Color(254, 161, 51));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)XCarry.class), new Color(254, 161, 51));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Yaw.class), new Color(115, 39, 141));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Capes.class), new Color(26, 135, 104));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)ClickGui.class), new Color(26, 81, 135));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Colors.class), new Color(135, 133, 26));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Components.class), new Color(135, 26, 26));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)CustomFont.class), new Color(135, 26, 88));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)HUD.class), new Color(110, 26, 135));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Management.class), new Color(26, 90, 135));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Notifications.class), new Color(170, 153, 255));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)PingBypass.class), new Color(60, 110, 175));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Media.class), new Color(138, 45, 13));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)Screens.class), new Color(165, 89, 101));
        this.moduleColorMap.put(this.getModuleByClass((Class<Module>)StreamerMode.class), new Color(0, 0, 0));
        for (final Module module : this.modules) {
            module.animation.start();
        }
    }
    
    public Module getModuleByName(final String name) {
        for (final Module module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) {
                continue;
            }
            return module;
        }
        return null;
    }
    
    public <T extends Module> T getModuleByClass(final Class<T> clazz) {
        for (final Module module : this.modules) {
            if (!clazz.isInstance(module)) {
                continue;
            }
            return (T)module;
        }
        return null;
    }
    
    public void enableModule(final Class<Module> clazz) {
        final Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.enable();
        }
    }
    
    public void disableModule(final Class<Module> clazz) {
        final Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.disable();
        }
    }
    
    public void enableModule(final String name) {
        final Module module = this.getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }
    
    public void disableModule(final String name) {
        final Module module = this.getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }
    
    public boolean isModuleEnabled(final String name) {
        final Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }
    
    public boolean isModuleEnabled(final Class<? extends Module> clazz) {
        final Module module = this.getModuleByClass(clazz);
        return module != null && module.isOn();
    }
    
    public Module getModuleByDisplayName(final String displayName) {
        for (final Module module : this.modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) {
                continue;
            }
            return module;
        }
        return null;
    }
    
    public ArrayList<Module> getEnabledModules() {
        final ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (final Module module : this.modules) {
            if (!module.isEnabled() && !module.isSliding()) {
                continue;
            }
            enabledModules.add(module);
        }
        return enabledModules;
    }
    
    public ArrayList<Module> getModulesByCategory(final Module.Category category) {
        final ArrayList<Module> modulesCategory = new ArrayList<Module>();
        final ArrayList<Module> list;
        this.modules.forEach(module -> {
            if (module.getCategory() == category) {
                list.add(module);
            }
            return;
        });
        return modulesCategory;
    }
    
    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }
    
    public void onLoad() {
        this.modules.stream().filter(Module::listening).forEach(MinecraftForge.EVENT_BUS::register);
        this.modules.forEach(Module::onLoad);
    }
    
    public void onUpdate() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }
    
    public void onTick() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }
    
    public void onRender2D(final Render2DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }
    
    public void onRender3D(final Render3DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }
    
    public void sortModules(final boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect((Collector<? super Object, ?, List<Module>>)Collectors.toList());
    }
    
    public void alphabeticallySortModules() {
        this.alphabeticallySortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing((Function<? super Object, ? extends Comparable>)Module::getDisplayName)).collect((Collector<? super Object, ?, List<Module>>)Collectors.toList());
    }
    
    public void onLogout() {
        this.modules.forEach(Module::onLogout);
    }
    
    public void onLogin() {
        this.modules.forEach(Module::onLogin);
    }
    
    public void onUnload() {
        this.modules.forEach(MinecraftForge.EVENT_BUS::unregister);
        this.modules.forEach(Module::onUnload);
    }
    
    public void onUnloadPost() {
        for (final Module module : this.modules) {
            module.enabled.setValue((Object)false);
        }
    }
    
    public void onKeyPressed(final int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.currentScreen instanceof PhobosGui) {
            return;
        }
        this.modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) {
                module.toggle();
            }
        });
    }
    
    public List<Module> getAnimationModules(final Module.Category category) {
        final ArrayList<Module> animationModules = new ArrayList<Module>();
        for (final Module module : this.getEnabledModules()) {
            if (module.getCategory() == category && !module.isDisabled() && module.isSliding()) {
                if (!module.isDrawn()) {
                    continue;
                }
                animationModules.add(module);
            }
        }
        return animationModules;
    }
}
