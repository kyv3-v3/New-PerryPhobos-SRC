



package me.earth.phobos.features.gui.alts.iasencrypt;

import java.io.*;
import java.nio.file.attribute.*;
import java.nio.file.*;
import me.earth.phobos.features.gui.alts.tools.alt.*;
import java.util.*;
import me.earth.phobos.features.gui.alts.tools.*;
import me.earth.phobos.features.gui.alts.ias.account.*;
import net.minecraft.client.*;

public final class Standards
{
    public static final String cfgn = ".iasx";
    public static final String pwdn = ".iasp";
    public static File IASFOLDER;
    
    public static String getPassword() {
        final File passwordFile = new File(Standards.IASFOLDER, ".iasp");
        Exception e = null;
        if (passwordFile.exists()) {
            String pass;
            try {
                final ObjectInputStream stream = new ObjectInputStream(new FileInputStream(passwordFile));
                pass = (String)stream.readObject();
                stream.close();
            }
            catch (IOException | ClassNotFoundException ex2) {
                final Exception ex;
                e = ex;
                throw new RuntimeException(e);
            }
            return pass;
        }
        final String newPass = EncryptionTools.generatePassword();
        try {
            final ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(passwordFile));
            out.writeObject(newPass);
            out.close();
        }
        catch (IOException e2) {
            throw new RuntimeException(e2);
        }
        try {
            final Path file = passwordFile.toPath();
            final DosFileAttributes attr = Files.readAttributes(file, DosFileAttributes.class, new LinkOption[0]);
            final DosFileAttributeView view = Files.getFileAttributeView(file, DosFileAttributeView.class, new LinkOption[0]);
            if (!attr.isHidden()) {
                view.setHidden(true);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return newPass;
    }
    
    public static void updateFolder() {
        final String OS = System.getProperty("os.name").toUpperCase();
        String dir;
        if (OS.contains("WIN")) {
            dir = System.getenv("AppData");
        }
        else {
            dir = System.getProperty("user.home");
            if (OS.contains("MAC")) {
                dir += "/Library/Application Support";
            }
        }
        Standards.IASFOLDER = new File(dir);
    }
    
    public static void importAccounts() {
        processData(getConfigV3());
        processData(getConfigV2());
        processData(getConfigV1(), false);
    }
    
    private static boolean hasData(final AccountData data) {
        for (final AccountData edata : AltDatabase.getInstance().getAlts()) {
            if (edata.equalsBasic(data)) {
                return true;
            }
        }
        return false;
    }
    
    private static void processData(final Config olddata) {
        processData(olddata, true);
    }
    
    private static void processData(final Config olddata, final boolean decrypt) {
        if (olddata != null) {
            for (final AccountData data : ((AltDatabase)olddata.getKey("altaccounts")).getAlts()) {
                final AccountData data2 = (AccountData)convertData(data, decrypt);
                if (!hasData(data2)) {
                    AltDatabase.getInstance().getAlts().add(data2);
                }
            }
        }
    }
    
    private static ExtendedAccountData convertData(final AccountData oldData, final boolean decrypt) {
        if (decrypt) {
            if (oldData instanceof ExtendedAccountData) {
                return new ExtendedAccountData(EncryptionTools.decodeOld(oldData.user), EncryptionTools.decodeOld(oldData.pass), oldData.alias, ((ExtendedAccountData)oldData).useCount, ((ExtendedAccountData)oldData).lastused, ((ExtendedAccountData)oldData).premium);
            }
            return new ExtendedAccountData(EncryptionTools.decodeOld(oldData.user), EncryptionTools.decodeOld(oldData.pass), oldData.alias);
        }
        else {
            if (oldData instanceof ExtendedAccountData) {
                return new ExtendedAccountData(oldData.user, oldData.pass, oldData.alias, ((ExtendedAccountData)oldData).useCount, ((ExtendedAccountData)oldData).lastused, ((ExtendedAccountData)oldData).premium);
            }
            return new ExtendedAccountData(oldData.user, oldData.pass, oldData.alias);
        }
    }
    
    private static Config getConfigV3() {
        final File f = new File(Standards.IASFOLDER, ".ias");
        Config cfg = null;
        if (f.exists()) {
            try {
                final ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f));
                cfg = (Config)stream.readObject();
                stream.close();
            }
            catch (IOException | ClassNotFoundException ex2) {
                final Exception ex;
                final Exception e = ex;
                e.printStackTrace();
            }
            f.delete();
        }
        return cfg;
    }
    
    private static Config getConfigV2() {
        final File f = new File(Minecraft.getMinecraft().gameDir, ".ias");
        Config cfg = null;
        if (f.exists()) {
            try {
                final ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f));
                cfg = (Config)stream.readObject();
                stream.close();
            }
            catch (IOException | ClassNotFoundException ex2) {
                final Exception ex;
                final Exception e = ex;
                e.printStackTrace();
            }
            f.delete();
        }
        return cfg;
    }
    
    private static Config getConfigV1() {
        final File f = new File(Minecraft.getMinecraft().gameDir, "user.cfg");
        Config cfg = null;
        if (f.exists()) {
            try {
                final ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f));
                cfg = (Config)stream.readObject();
                stream.close();
            }
            catch (IOException | ClassNotFoundException ex2) {
                final Exception ex;
                final Exception e = ex;
                e.printStackTrace();
            }
            f.delete();
        }
        return cfg;
    }
    
    static {
        Standards.IASFOLDER = Minecraft.getMinecraft().gameDir;
    }
}
