package com.darksouls.rougelike.utility;


import com.darksouls.rougelike.model.VPoint;
import com.darksouls.rougelike.references.Config;
import com.darksouls.rougelike.references.Reference;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class GuiMagic {
    public static final Border frameBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);

    public static void setFrameSize(JFrame frame, int width, int height){
        int wp = Reference.WIN_WBORDER;
        int hp = Reference.WIN_HBORDER;
        frame.setSize(width + wp, height + wp);
        frame.setPreferredSize(new Dimension(width + wp, height + hp));
    }

    public static VPoint getFieldCenter(VPoint mouseCoord){
        VPoint ret = null;
        if(mouseCoord != null)
            ret = new VPoint(Math.floor(mouseCoord.x() / Config.FIELD_SIZE),
                Math.floor(mouseCoord.y() / Config.FIELD_SIZE));

        //LogHelper.writeLn("gui: " + mouseCoord.toString());
        return ret;
    }

    public static VPoint getFieldCoord(VPoint mouseCoord){
        VPoint ret = getFieldCenter(mouseCoord);
        if (ret != null)
            ret = ret.multiply(Config.FIELD_SIZE).addX(Config.FIELD_SIZE / 2).addY(Config.FIELD_SIZE / 2);
        return ret;
    }


    public static final String WIN_LOOKS = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

    public static void setWindowTheme(String set){
        try {
            UIManager.setLookAndFeel(set);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    public static void setWindowsLook(){
        if (System.getProperty("os.name").startsWith("Windows")) {
            setWindowTheme(WIN_LOOKS);
        }
    }

    public static void registerFont(URI fontPath){
        try {
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(fontPath)));
        } catch (IOException | FontFormatException e) {
            //Handle exception
            LogHelper.error("wrong path");
        }
    }
}
