



package me.earth.phobos.manager;

import me.earth.phobos.util.*;
import me.earth.phobos.features.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.*;
import me.earth.phobos.features.modules.render.*;
import me.earth.phobos.features.modules.player.*;
import java.util.stream.*;
import java.nio.file.attribute.*;
import java.nio.file.*;
import java.util.*;
import com.google.gson.*;
import java.io.*;
import me.earth.phobos.features.modules.*;

public class ConfigManager implements Util
{
    public ArrayList<Feature> features;
    public String config;
    public boolean loadingConfig;
    public boolean savingConfig;
    
    public ConfigManager() {
        this.features = new ArrayList<Feature>();
        this.config = "phobos/config/";
    }
    
    public static void setValueFromJson(final Feature feature,  final Setting setting,  final JsonElement element) {
        final String type = setting.getType();
        switch (type) {
            case "Boolean": {
                setting.setValue((Object)element.getAsBoolean());
                break;
            }
            case "Double": {
                setting.setValue((Object)element.getAsDouble());
                break;
            }
            case "Float": {
                setting.setValue((Object)element.getAsFloat());
                break;
            }
            case "Integer": {
                setting.setValue((Object)element.getAsInt());
                break;
            }
            case "String": {
                final String str = element.getAsString();
                setting.setValue((Object)str.replace("_",  " "));
                break;
            }
            case "Bind": {
                setting.setValue((Object)new Bind.BindConverter().doBackward(element));
                break;
            }
            case "Enum": {
                try {
                    final EnumConverter converter = new EnumConverter((Class)((Enum)setting.getValue()).getClass());
                    final Enum value = converter.doBackward(element);
                    setting.setValue((value == null) ? setting.getDefaultValue() : value);
                }
                catch (Exception ex) {}
                break;
            }
            default: {
                Phobos.LOGGER.error("Unknown Setting type for: " + feature.getName() + " : " + setting.getName());
                break;
            }
        }
    }
    
    private static void loadFile(final JsonObject input,  final Feature feature) {
        for (final Map.Entry<String,  JsonElement> entry : input.entrySet()) {
            final String settingName = entry.getKey();
            final JsonElement element = entry.getValue();
            if (feature instanceof FriendManager) {
                try {
                    Phobos.friendManager.addFriend(new FriendManager.Friend(element.getAsString(),  UUID.fromString(settingName)));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                boolean settingFound = false;
                for (final Setting setting : feature.getSettings()) {
                    if (!settingName.equals(setting.getName())) {
                        continue;
                    }
                    try {
                        setValueFromJson(feature,  setting,  element);
                    }
                    catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    settingFound = true;
                }
                if (settingFound) {
                    continue;
                }
            }
            if (feature instanceof XRay) {
                feature.register(new Setting(settingName,  (Object)true,  v -> (boolean)((XRay)feature).showBlocks.getValue()));
            }
            else {
                if (!(feature instanceof AntiDDoS)) {
                    continue;
                }
                final AntiDDoS antiDDoS = (AntiDDoS)feature;
                final AntiDDoS antiDDoS2;
                final Setting<?> setting2 = (Setting<?>)feature.register(new Setting(settingName,  (Object)true,  v -> (boolean)antiDDoS2.showServer.getValue() && !(boolean)antiDDoS2.full.getValue()));
                antiDDoS.registerServer((Setting)setting2);
            }
        }
    }
    
    public void loadConfig(final String name) {
        this.loadingConfig = true;
        final List<File> files = Arrays.stream((Object[])Objects.requireNonNull((T[])new File("phobos").listFiles())).filter(File::isDirectory).collect((Collector<? super Object,  ?,  List<File>>)Collectors.toList());
        this.config = (files.contains(new File("phobos/" + name + "/")) ? ("phobos/" + name + "/") : "phobos/config/");
        Phobos.friendManager.onLoad();
        for (final Feature feature : this.features) {
            try {
                this.loadSettings(feature);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.saveCurrentConfig();
        this.loadingConfig = false;
    }
    
    public void saveConfig(final String name) {
        this.savingConfig = true;
        this.config = "phobos/" + name + "/";
        final File path = new File(this.config);
        if (!path.exists()) {
            path.mkdir();
        }
        Phobos.friendManager.saveFriends();
        for (final Feature feature : this.features) {
            try {
                this.saveSettings(feature);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.saveCurrentConfig();
        this.savingConfig = false;
    }
    
    public void saveCurrentConfig() {
        final File currentConfig = new File("phobos/currentconfig.txt");
        try {
            if (currentConfig.exists()) {
                final FileWriter writer = new FileWriter(currentConfig);
                final String tempConfig = this.config.replaceAll("/",  "");
                writer.write(tempConfig.replaceAll("phobos",  ""));
                writer.close();
            }
            else {
                currentConfig.createNewFile();
                final FileWriter writer = new FileWriter(currentConfig);
                final String tempConfig = this.config.replaceAll("/",  "");
                writer.write(tempConfig.replaceAll("phobos",  ""));
                writer.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String loadCurrentConfig() {
        final File currentConfig = new File("phobos/currentconfig.txt");
        String name = "config";
        try {
            if (currentConfig.exists()) {
                final Scanner reader = new Scanner(currentConfig);
                while (reader.hasNextLine()) {
                    name = reader.nextLine();
                }
                reader.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }
    
    public void resetConfig(final boolean saveConfig,  final String name) {
        for (final Feature feature : this.features) {
            feature.reset();
        }
        if (saveConfig) {
            this.saveConfig(name);
        }
    }
    
    public void saveSettings(final Feature feature) throws IOException {
        new JsonObject();
        final File directory = new File(this.config + this.getDirectory(feature));
        if (!directory.exists()) {
            directory.mkdir();
        }
        final Path outputFile;
        if (!Files.exists(outputFile = Paths.get(this.config + this.getDirectory(feature) + feature.getName() + ".json",  new String[0]),  new LinkOption[0])) {
            Files.createFile(outputFile,  (FileAttribute<?>[])new FileAttribute[0]);
        }
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final String json = gson.toJson((JsonElement)this.writeSettings(feature));
        final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputFile,  new OpenOption[0])));
        writer.write(json);
        writer.close();
    }
    
    public void init() {
        this.features.addAll((Collection<? extends Feature>)Phobos.moduleManager.modules);
        this.features.add(Phobos.friendManager);
        final String name = this.loadCurrentConfig();
        this.loadConfig(name);
        Phobos.LOGGER.info("Config loaded.");
    }
    
    private void loadSettings(final Feature feature) throws IOException {
        final String featureName = this.config + this.getDirectory(feature) + feature.getName() + ".json";
        final Path featurePath = Paths.get(featureName,  new String[0]);
        if (!Files.exists(featurePath,  new LinkOption[0])) {
            return;
        }
        this.loadPath(featurePath,  feature);
    }
    
    private void loadPath(final Path path,  final Feature feature) throws IOException {
        final InputStream stream = Files.newInputStream(path,  new OpenOption[0]);
        try {
            loadFile(new JsonParser().parse((Reader)new InputStreamReader(stream)).getAsJsonObject(),  feature);
        }
        catch (IllegalStateException e) {
            Phobos.LOGGER.error("Bad Config File for: " + feature.getName() + ". Resetting...");
            loadFile(new JsonObject(),  feature);
        }
        stream.close();
    }
    
    public JsonObject writeSettings(final Feature feature) {
        final JsonObject object = new JsonObject();
        final JsonParser jp = new JsonParser();
        for (final Setting setting : feature.getSettings()) {
            if (setting.isEnumSetting()) {
                final EnumConverter converter = new EnumConverter((Class)((Enum)setting.getValue()).getClass());
                object.add(setting.getName(),  converter.doForward((Enum)setting.getValue()));
            }
            else {
                if (setting.isStringSetting()) {
                    final String str = (String)setting.getValue();
                    setting.setValue((Object)str.replace(" ",  "_"));
                }
                try {
                    object.add(setting.getName(),  jp.parse(setting.getValueAsString()));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return object;
    }
    
    public String getDirectory(final Feature feature) {
        String directory = "";
        if (feature instanceof Module) {
            directory = directory + ((Module)feature).getCategory().getName() + "/";
        }
        return directory;
    }
}
