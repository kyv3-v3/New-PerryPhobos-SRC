package me.earth.phobos;
import java.io.IOException;
import me.earth.phobos.DiscordPresence;
import me.earth.phobos.features.gui.custom.GuiCustomMainScreen;
import me.earth.phobos.features.modules.client.PhobosChat;
import me.earth.phobos.features.modules.misc.RPC;
import me.earth.phobos.manager.CapeManager;
import me.earth.phobos.manager.ClassManager;
import me.earth.phobos.manager.ColorManager;
import me.earth.phobos.manager.CommandManager;
import me.earth.phobos.manager.ConfigManager;
import me.earth.phobos.manager.CosmeticsManager;
import me.earth.phobos.manager.EventManager;
import me.earth.phobos.manager.FileManager;
import me.earth.phobos.manager.FriendManager;
import me.earth.phobos.manager.HoleManager;
import me.earth.phobos.manager.InventoryManager;
import me.earth.phobos.manager.ModuleManager;
import me.earth.phobos.manager.NoStopManager;
import me.earth.phobos.manager.NotificationManager;
import me.earth.phobos.manager.PacketManager;
import me.earth.phobos.manager.PositionManager;
import me.earth.phobos.manager.PotionManager;
import me.earth.phobos.manager.ReloadManager;
import me.earth.phobos.manager.RotationManager;
import me.earth.phobos.manager.SafetyManager;
import me.earth.phobos.manager.ServerManager;
import me.earth.phobos.manager.SpeedManager;
import me.earth.phobos.manager.TextManager;
import me.earth.phobos.manager.TimerManager;
import me.earth.phobos.manager.TotemPopManager;
import me.earth.phobos.manager.WaypointManager;
import me.earth.phobos.util.Tracker;
import me.earth.phobos.util.TrackerID;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
@Mod(modid = "phobos",  name = "Phobos",  version = "1.9.0")
public class Phobos
{
    public static final String MODID = "phobos";
    public static final String MODNAME = "Phobos";
    public static final String MODVER = "1.9.0";
    public static final String NAME_UNICODE = "3\u1d00\u0280\u1d1b\u029c\u029c4\u1d04\u1d0b";
    public static final String PHOBOS_UNICODE = "\u1d18\u029c\u1d0f\u0299\u1d0f\ua731";
    public static final String CHAT_SUFFIX = " \u23d0 3\u1d00\u0280\u1d1b\u029c\u029c4\u1d04\u1d0b";
    public static final String PHOBOS_SUFFIX = " \u23d0 \u1d18\u029c\u1d0f\u0299\u1d0f\ua731";
    public static final Logger LOGGER;
    public static CapeManager capeManager;
    public static ModuleManager moduleManager;
    public static SpeedManager speedManager;
    public static PositionManager positionManager;
    public static RotationManager rotationManager;
    public static CommandManager commandManager;
    public static EventManager eventManager;
    public static ConfigManager configManager;
    public static FileManager fileManager;
    public static FriendManager friendManager;
    public static TextManager textManager;
    public static ColorManager colorManager;
    public static ServerManager serverManager;
    public static PotionManager potionManager;
    public static InventoryManager inventoryManager;
    public static TimerManager timerManager;
    public static PacketManager packetManager;
    public static ReloadManager reloadManager;
    public static TotemPopManager totemPopManager;
    public static HoleManager holeManager;
    public static NotificationManager notificationManager;
    public static SafetyManager safetyManager;
    public static GuiCustomMainScreen customMainScreen;
    public static CosmeticsManager cosmeticsManager;
    public static NoStopManager baritoneManager;
    public static WaypointManager waypointManager;
    @Mod.Instance
    public static Phobos INSTANCE;
    private static boolean unloaded;
    
    public static void load() {
        Phobos.LOGGER.info("\n\nLoading 3arthh4ck 1.9.0");
        Phobos.unloaded = false;
        if (Phobos.reloadManager != null) {
            Phobos.reloadManager.unload();
            Phobos.reloadManager = null;
        }
        Phobos.capeManager = new CapeManager();
        Phobos.baritoneManager = new NoStopManager();
        Phobos.totemPopManager = new TotemPopManager();
        Phobos.timerManager = new TimerManager();
        Phobos.packetManager = new PacketManager();
        Phobos.serverManager = new ServerManager();
        Phobos.colorManager = new ColorManager();
        Phobos.textManager = new TextManager();
        Phobos.moduleManager = new ModuleManager();
        Phobos.speedManager = new SpeedManager();
        Phobos.rotationManager = new RotationManager();
        Phobos.positionManager = new PositionManager();
        Phobos.commandManager = new CommandManager();
        Phobos.eventManager = new EventManager();
        Phobos.configManager = new ConfigManager();
        Phobos.fileManager = new FileManager();
        Phobos.friendManager = new FriendManager();
        Phobos.potionManager = new PotionManager();
        Phobos.inventoryManager = new InventoryManager();
        Phobos.holeManager = new HoleManager();
        Phobos.notificationManager = new NotificationManager();
        Phobos.safetyManager = new SafetyManager();
        Phobos.waypointManager = new WaypointManager();
        Phobos.LOGGER.info("Initialized Management");
        Phobos.moduleManager.init();
        Phobos.LOGGER.info("Modules loaded.");
        Phobos.configManager.init();
        Phobos.eventManager.init();
        Phobos.LOGGER.info("EventManager loaded.");
        Phobos.textManager.init(true);
        Phobos.moduleManager.onLoad();
        Phobos.totemPopManager.init();
        Phobos.timerManager.init();
        if (((RPC)Phobos.moduleManager.getModuleByClass((Class)RPC.class)).isEnabled()) {
            DiscordPresence.start();
        }
        Phobos.cosmeticsManager = new CosmeticsManager();
        Phobos.LOGGER.info("3arthh4ck initialized!\n");
    }
    
    public static void unload(final boolean unload) {
        Phobos.LOGGER.info("\n\nUnloading 3arthh4ck 1.9.0");
        if (unload) {
            (Phobos.reloadManager = new ReloadManager()).init((Phobos.commandManager != null) ? Phobos.commandManager.getPrefix() : ".");
        }
        if (Phobos.baritoneManager != null) {
            Phobos.baritoneManager.stop();
        }
        onUnload();
        Phobos.capeManager = null;
        Phobos.eventManager = null;
        Phobos.holeManager = null;
        Phobos.timerManager = null;
        Phobos.moduleManager = null;
        Phobos.totemPopManager = null;
        Phobos.serverManager = null;
        Phobos.colorManager = null;
        Phobos.textManager = null;
        Phobos.speedManager = null;
        Phobos.rotationManager = null;
        Phobos.positionManager = null;
        Phobos.commandManager = null;
        Phobos.configManager = null;
        Phobos.fileManager = null;
        Phobos.friendManager = null;
        Phobos.potionManager = null;
        Phobos.inventoryManager = null;
        Phobos.notificationManager = null;
        Phobos.safetyManager = null;
        Phobos.LOGGER.info("3arthh4ck unloaded!\n");
    }
    
    public static void reload() {
        unload(false);
        load();
    }
    
    public static void onUnload() {
        if (!Phobos.unloaded) {
            try {
                PhobosChat.INSTANCE.disconnect();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            Phobos.eventManager.onUnload();
            Phobos.moduleManager.onUnload();
            Phobos.configManager.saveConfig(Phobos.configManager.config.replaceFirst("phobos/",  ""));
            Phobos.moduleManager.onUnloadPost();
            Phobos.timerManager.unload();
            Phobos.unloaded = true;
        }
    }
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        Phobos.LOGGER.info("ohare is cute!!!");
        Phobos.LOGGER.info("faggot above - 3vt");
        Phobos.LOGGER.info("megyn wins again");
        Phobos.LOGGER.info("gtfo my logs - 3arth");
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        Phobos.customMainScreen = new GuiCustomMainScreen();
        Display.setTitle("3arthh4ck - v1.9.0");
        load();
    }
    
    static {
        LOGGER = LogManager.getLogger("3arthh4ck");
        Phobos.unloaded = false;
    }
}
