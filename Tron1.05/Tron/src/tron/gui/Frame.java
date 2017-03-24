/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tron.gui;

import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.JFrame;
import tron.control.HumanController;

/**
 *
 * @author User
 */
public class Frame extends JFrame {

    private Panel panel;

    public Frame(HumanController[] controllers) {

        panel = new Panel();

        for(int i=0;i<controllers.length;i++) {
            if(controllers[i]!=null) {
                this.addKeyListener(controllers[i]);
            }
        }

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Tron");

        Container c = this.getContentPane();
        c.setLayout(new GridLayout(1,1));

        this.setVisible(true);

        c.add(panel);

        this.pack();
    }
}
