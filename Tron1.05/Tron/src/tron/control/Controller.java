/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tron.control;

/**
 *
 * @author User
 */
public interface Controller {

    /**
     * Returns the direction the new player should go.
     * @param pid The id of the player.
     * @return The new direction.
     */
    public int newDirection(int pid);

}
