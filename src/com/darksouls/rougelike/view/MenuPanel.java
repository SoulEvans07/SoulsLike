package com.darksouls.rougelike.view;

import com.darksouls.rougelike.model.VPoint;
import com.darksouls.rougelike.references.Reference;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Singleton
public class MenuPanel extends JPanel {
    private static MenuPanel instance;

    private JButton playBtn;

    private JPanel mainPanel;
    private JPanel statusBar;
    private JLabel timeStamp;

    private MenuPanel(){}

    public static MenuPanel getInstance() {
        if (instance == null) {
            instance = new MenuPanel();
            instance.init();
        }

        return instance;
    }

    public void setFocus(){
        playBtn.requestFocus();
    }


    private void init(){
        setLayout(null);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPanel = new JPanel();
        statusBar = new JPanel();
        statusBar.setLayout(new BorderLayout());

        playBtn = new JButton("Play");
        playBtn.setPreferredSize(new Dimension(GUI.getInstance().getWidth() / 3, GUI.getInstance().getHeight() / 20));
        playBtn.addActionListener(new playAction());

        timeStamp = new JLabel(Reference.TIME_STAMP);

        mainPanel.add(playBtn);
        statusBar.add(timeStamp, BorderLayout.PAGE_END);

        this.add(mainPanel);
        this.add(statusBar);
    }

    public void windowUpdate(VPoint size){
    }

    private class playAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            GUI.getInstance().startGame();
        }
    }
}
