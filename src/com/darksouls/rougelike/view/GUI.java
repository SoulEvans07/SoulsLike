package com.darksouls.rougelike.view;

import com.darksouls.rougelike.model.VPoint;
import com.darksouls.rougelike.references.Config;
import com.darksouls.rougelike.references.Reference;
import com.darksouls.rougelike.utility.GuiMagic;
import com.darksouls.rougelike.utility.LogHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URISyntaxException;

// Singleton
public class GUI extends JFrame{
    private static GUI instance;

    private VPoint startSize;
    private VPoint size;

    private GUI(){}

    public static GUI getInstance(){
        if(instance == null)
            instance = new GUI();

        return instance;
    }

    private static void startUp(){
        GUI gui = GUI.getInstance();
        gui.initFrame();

        gui.setContentPane(MenuPanel.getInstance());

        gui.invalidate();
        gui.setVisible(true);
    }




    private void initFrame(){
        GuiMagic.setWindowsLook();
        try {
            GuiMagic.registerFont(this.getClass().getResource(Reference.FONTS_PATH + Reference.FONT_NAME + ".ttf").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        this.setLayout(new BorderLayout());
        this.setTitle(Reference.PROJ_NAME + (Config.DEBUG ? " - " + Reference.PROJ_VER : ""));
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new exitApp());

        // TODO: option based window resize, full window mode, etc.
        //this.setResizable(false);

        startSize = new VPoint(800, 600);
        size = new VPoint(startSize.x() + this.getInsets().right , startSize.y() + this.getInsets().top);
        GuiMagic.setFrameSize(this, size.getX(), size.getY());

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        //this.setLocationRelativeTo(null);
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }


    //--------------------------------------------------Main------------------------------------------------------------

    public static void main(String[] args){
        if(args.length > 0 && args[0].toUpperCase().equals("-debug".toUpperCase()))
            Config.DEBUG = true;
        else
            Config.DEBUG = false;
        GUI.startUp();
    }

    //---------------------------------------------Action-Handling------------------------------------------------------

    public void back2Menu(){
        //Clock.stopClock();
        this.setContentPane(MenuPanel.getInstance());
        MenuPanel.getInstance().setFocus();
        this.setVisible(true);
    }

    public void startGame(){
        this.setContentPane(GamePanel.getNewInstance());
        GamePanel.getInstance().requestFocus();
        this.setVisible(true);
        //Clock.startClock();
    }

    private static void exitGame(){
        System.exit(0);
    }

    //---------------------------------------------Inner-Classes--------------------------------------------------------

    private class exitApp extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e){
            requestFocus(false); // letiltjuk, hogy ESC-re ne hozza be a pausePanelt, am�g fent van az OptionPane
            // TODO: LATER ENABLE IT!!!
            /*int i = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?",
                    "Confirm Exit", JOptionPane.YES_NO_OPTION);
             if(i == 0) {*/
            if(true){
                GUI.exitGame();
            }
            requestFocus(); // visszaadjuk, ha nem l�pt�nk ki
        }
    }
}
