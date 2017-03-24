/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tron.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import tron.core.Game;

/**
 *
 * @author User
 */
public class HumanController implements Controller, KeyListener {
    private int recentDirection;

    private int[] keys;
    
    public HumanController(int[] keys) {
        recentDirection = -1;
        this.keys = keys;
    }

    public int newDirection(int pid) {
        if(recentDirection == -1) {
            recentDirection = Game.getSingleton().getPlayer(pid).getDirection();
        }

        return recentDirection;
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == keys[0]) {
            recentDirection = Game.NORTH;
        }
        else if(e.getKeyCode() == keys[1]) {
            recentDirection = Game.EAST;
        }
        else if(e.getKeyCode() == keys[2]) {
            recentDirection = Game.SOUTH;
        }
        else if(e.getKeyCode() == keys[3]) {
            recentDirection = Game.WEST;
        }
    }

    public void keyTyped(KeyEvent e) { }

    public void keyReleased(KeyEvent e) { }

}
