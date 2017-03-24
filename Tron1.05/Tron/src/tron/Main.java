/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tron;

import java.awt.event.KeyEvent;
import tron.control.Controller;
import tron.control.HumanController;
import tron.core.Game;
import tron.gui.Frame;
import tron.properties.TronProperties;

/**
 *
 * @author User
 */
public class Main {
    
    private static Frame frame;
    private static Game game;

    private static int delay;

    private static TronProperties options;
    private static HumanController[] human_controllers;
    private static Controller[] all_controllers;

    public static void main(String[] args) {

        options = TronProperties.getSingleton();

        delay = Integer.parseInt(options.getProperty("delay"));

        initControllers();

        Game.initialize(all_controllers);

        game = Game.getSingleton();

        frame = new Frame(human_controllers);

        mainLoop();

    }
    
    private static void mainLoop() {
        while(true) {
            if(!game.isGameOver()) {
                game.step();
            }
            frame.repaint();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
    }

    private static void initControllers() {
        int[][] player_keys = new int[][]{{KeyEvent.VK_UP, KeyEvent.VK_RIGHT,
            KeyEvent.VK_DOWN, KeyEvent.VK_LEFT}, {KeyEvent.VK_W, KeyEvent.VK_D,
            KeyEvent.VK_S, KeyEvent.VK_A}, {KeyEvent.VK_NUMPAD8, KeyEvent.VK_NUMPAD6,
            KeyEvent.VK_NUMPAD5, KeyEvent.VK_NUMPAD4}, {KeyEvent.VK_U, KeyEvent.VK_K,
            KeyEvent.VK_J, KeyEvent.VK_H}};

        int num_players = Integer.parseInt(options.getProperty("num.players"));

        int human_counter = 0;

        all_controllers = new Controller[num_players];
        human_controllers = new HumanController[num_players];

        for(int i = 0;i < num_players;i++) {
            String ext = options.getProperty("p"+(i+1)+".control");
            
            if(options.getProperty("p"+(i+1)+".control").equals("Human")) {
                human_controllers[human_counter] = new HumanController(player_keys[human_counter]);
                all_controllers[i] = human_controllers[human_counter];
                human_counter++;
            }
            else {
                try {
                    Class<?> c = Class.forName("tron.ai."+options.getProperty("p"+(i+1)+".control"));
                    all_controllers[i] = (Controller) c.newInstance();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }    
}
