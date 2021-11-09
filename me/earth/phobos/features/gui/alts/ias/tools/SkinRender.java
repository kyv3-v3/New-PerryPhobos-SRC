//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\utente\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package me.earth.phobos.features.gui.alts.ias.tools;

import net.minecraft.client.renderer.texture.*;
import net.minecraft.util.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import net.minecraft.client.gui.*;

public class SkinRender
{
    private final File file;
    private final TextureManager textureManager;
    private DynamicTexture previewTexture;
    private ResourceLocation resourceLocation;
    
    public SkinRender(final TextureManager textureManager, final File file) {
        this.textureManager = textureManager;
        this.file = file;
    }
    
    private boolean loadPreview() {
        try {
            final BufferedImage image = ImageIO.read(this.file);
            this.previewTexture = new DynamicTexture(image);
            this.resourceLocation = this.textureManager.getDynamicTextureLocation("ias", this.previewTexture);
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void drawImage(final int xPos, final int yPos, final int width, final int height) {
        if (this.previewTexture == null) {
            final boolean successful = this.loadPreview();
            if (!successful) {
                System.out.println("Failure to load preview.");
                return;
            }
        }
        this.previewTexture.updateDynamicTexture();
        this.textureManager.bindTexture(this.resourceLocation);
        Gui.drawModalRectWithCustomSizedTexture(xPos, yPos, 0.0f, 0.0f, width, height, 64.0f, 128.0f);
    }
}
