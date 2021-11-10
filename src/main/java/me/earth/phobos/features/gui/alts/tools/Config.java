



package me.earth.phobos.features.gui.alts.tools;

import me.earth.phobos.features.gui.alts.iasencrypt.*;
import java.nio.file.attribute.*;
import java.io.*;
import java.nio.file.*;
import net.minecraft.client.*;
import me.earth.phobos.features.gui.alts.tools.alt.*;
import java.util.*;

public class Config implements Serializable
{
    public static final long serialVersionUID = -559038737L;
    private static final String configFileName = ".iasx";
    private static Config instance;
    private final ArrayList<Pair<String,  Object>> field_218893_c;
    
    private Config() {
        this.field_218893_c = new ArrayList<Pair<String,  Object>>();
        Config.instance = this;
    }
    
    public static Config getInstance() {
        return Config.instance;
    }
    
    public static void save() {
        saveToFile();
    }
    
    public static void load() {
        loadFromOld();
        readFromFile();
    }
    
    private static void readFromFile() {
        final File f = new File(Standards.IASFOLDER,  ".iasx");
        if (f.exists()) {
            try {
                final ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f));
                Config.instance = (Config)stream.readObject();
                stream.close();
            }
            catch (IOException | ClassNotFoundException ex2) {
                final Exception ex;
                final Exception e = ex;
                e.printStackTrace();
                Config.instance = new Config();
                f.delete();
            }
        }
        if (Config.instance == null) {
            Config.instance = new Config();
        }
    }
    
    private static void saveToFile() {
        try {
            final Path file = new File(Standards.IASFOLDER,  ".iasx").toPath();
            final DosFileAttributes attr = Files.readAttributes(file,  DosFileAttributes.class,  new LinkOption[0]);
            final DosFileAttributeView view = Files.getFileAttributeView(file,  DosFileAttributeView.class,  new LinkOption[0]);
            if (attr.isHidden()) {
                view.setHidden(false);
            }
        }
        catch (NoSuchFileException ex) {}
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            final ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(Standards.IASFOLDER,  ".iasx")));
            out.writeObject(Config.instance);
            out.close();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            final Path file = new File(Standards.IASFOLDER,  ".iasx").toPath();
            final DosFileAttributes attr = Files.readAttributes(file,  DosFileAttributes.class,  new LinkOption[0]);
            final DosFileAttributeView view = Files.getFileAttributeView(file,  DosFileAttributeView.class,  new LinkOption[0]);
            if (!attr.isHidden()) {
                view.setHidden(true);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void loadFromOld() {
        final File f = new File(Minecraft.getMinecraft().gameDir,  "user.cfg");
        if (f.exists()) {
            try {
                final ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f));
                Config.instance = (Config)stream.readObject();
                stream.close();
                f.delete();
                System.out.println("Loaded data from old file");
            }
            catch (IOException | ClassNotFoundException ex2) {
                final Exception ex;
                final Exception e = ex;
                e.printStackTrace();
                f.delete();
            }
        }
    }
    
    public void setKey(final Pair<String,  Object> key) {
        if (this.getKey(key.getValue1()) != null) {
            this.removeKey(key.getValue1());
        }
        this.field_218893_c.add(key);
        save();
    }
    
    public void setKey(final String key,  final AltDatabase value) {
        this.setKey(new Pair<String,  Object>(key,  value));
    }
    
    public Object getKey(final String key) {
        for (final Pair<String,  Object> aField_218893_c : this.field_218893_c) {
            if (aField_218893_c.getValue1().equals(key)) {
                return aField_218893_c.getValue2();
            }
        }
        return null;
    }
    
    private void removeKey(final String key) {
        for (int i = 0; i < this.field_218893_c.size(); ++i) {
            if (this.field_218893_c.get(i).getValue1().equals(key)) {
                this.field_218893_c.remove(i);
            }
        }
    }
}
