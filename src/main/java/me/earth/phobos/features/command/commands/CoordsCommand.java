/*
 * Decompiled with CFR 0.150.
 */
package me.earth.phobos.features.command.commands;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.text.DecimalFormat;
import me.earth.phobos.features.command.Command;

public class CoordsCommand
extends Command {
    public CoordsCommand() {
        super("coords", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        DecimalFormat format = new DecimalFormat("#");
        StringSelection contents = new StringSelection(format.format(CoordsCommand.mc.field_71439_g.field_70165_t) + ", " + format.format(CoordsCommand.mc.field_71439_g.field_70163_u) + ", " + format.format(CoordsCommand.mc.field_71439_g.field_70161_v));
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(contents, null);
        Command.sendMessage("Saved Coordinates To Clipboard.", false);
    }
}

