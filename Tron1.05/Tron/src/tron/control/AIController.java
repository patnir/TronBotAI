/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tron.control;

import tron.core.Game;
import tron.properties.TronProperties;

/**
 * A class used as a callback for computer controlled players.
 *
 * newDirection(int pid) is called every step by the game for each
 * player, where pid is the id of the given player, to determine
 * the direction that they will go next.
 * @author User
 */
public abstract class AIController implements Controller {

    private static Game game = Game.getSingleton();
    private static int step_time = -1;

    /**
     * North or higher in the y plane.
     */
    protected final static int NORTH = 0;

    /**
     * East or higher in the x plane.
     */
    protected final static int EAST = 1;

    /**
     * South or lower in the y plane.
     */
    protected final static int SOUTH = 2;

    /**
     * West or lower in the x plane.
     */
    protected final static int WEST = 3;

    protected AIController() {
        if(step_time == -1) {
            step_time = Integer.parseInt(TronProperties.getSingleton().getProperty("step.time"));
        }
    }

    /**
     * Get the pid (player id) at the specific
     * coordinate if it is occupied and 0 otherwise.
     * If you specify a coordinate outside the
     * board -1 will be returned.
     * @param x x location of the coordinate.
     * @param y y location of the coordinate.
     * @return The value at the coordinate.
     */
    protected final static int getCoord(int x, int y){
        return game.getCoord(x, y);
    }

    /**
     * Returns the height of the board.
     * @return The height.
     */
    protected final static int getBoardWidth() {
        return game.getBoardWidth();
    }

    /**
     * Returns the height of the board.
     * @return The height.
     */
    protected final static int getBoardHeight() {
        return game.getBoardHeight();
    }

    /**
     * Get the x coordinate of a given players
     * location, or head.
     * @param pid The id of the player.
     * @return The x coordinate.
     */
    protected final static int getPlayerX(int pid) {
        return game.getPlayer(pid).getX();
    }

    /**
     * Get the y coordinate of a given players
     * location, or head.
     * @param pid The id of the player.
     * @return The y coordinate.
     */
    protected final static int getPlayerY(int pid) {
        return game.getPlayer(pid).getY();
    }

    /**
     * Returns an array of the pids of the
     * players still alive.
     * @return The players still alive.
     */
    protected final static int[] getRemainingPids() {
        return game.getRemainingPids();
    }

    /**
     * Return the direction of the given player.
     * @param pid The pid of the player.
     * @return The direction.
     */
    protected final static int getPlayerDirection(int pid) {
        return game.getPlayer(pid).getDirection();
    }

    /**
     * Return whether a given player has lost.
     * @param pid The pid of the player.
     * @return Whether the player has lost.
     */
    protected final static boolean hasPlayerLost(int pid) {
        return game.getPlayer(pid).hasLost();
    }

    /**
     * Returns the milliseconds an AI has to move.
     * @return The step time.
     */
    protected final static int getStepTime() {
        return step_time;
    }

    /**
     * Return an integer representing the direction opposite
     * of the given direction.
     * @param dir The input direction.
     * @return The opposite direction.
     */
    protected final static int getOppositeDirection(int dir) {
        if(dir % 2 == 1) {
            return 4 - dir;
        }
        else {
            return 2 - dir;
        }
    }

    /**
     * Return an integer representing the direction to the right
     * of the given direction.
     * @param dir The input direction.
     * @return The direction to the right.
     */
    protected final static int getRightDirection(int dir) {
        return (dir + 1) % 4;
    }

    /**
     * Return an integer representing the direction to the left
     * of the given direction.
     * @param dir The input direction.
     * @return The direction to the left.
     */
    protected final static int getLeftDirection(int dir) {
        return (dir + 3) % 4;
    }
}
