/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tron.ai;

import tron.control.AIController;

/**
 * Example AI that simply checks straight in all 4 directions and
 * determines which direction he can go furthest in an heads there,
 * hence the name TunnelVision.
 *
 * Also has the priority N->E->S->W, if there is a tie.
 * @author User
 */
public class TunnelVisionAI extends AIController {

    public int newDirection(int pid) {
        int dir = getPlayerDirection(pid);

        // Get our current location.
        int px = getPlayerX(pid);
        int py = getPlayerY(pid);

        int tempx;
        int tempy;

        int max_distance = 0;
        int current_distance = 0;
        int new_direction = 0;

        // Check all 4 directions and see which one we can go the furthest
        for(int i=0;i<4;i++) {
            // Reset the variables
            tempx = px;
            tempy = py;
            current_distance = 0;

            switch(i) {
                case NORTH:
                    tempy++;
                    break;
                case SOUTH:
                    tempy--;
                    break;
                case EAST:
                    tempx++;
                    break;
                case WEST:
                    tempx--;
                    break;
            }

            // While the location is free keep checking
            while(getCoord(tempx, tempy) == 0) {
                switch(i) {
                    case NORTH:
                        tempy++;
                        break;
                    case SOUTH:
                        tempy--;
                        break;
                    case EAST:
                        tempx++;
                        break;
                    case WEST:
                        tempx--;
                        break;
                }
                current_distance++;
            }

            // If the current direction can go further and it is not the opposite direction
            if(current_distance >  max_distance && i != getOppositeDirection(dir)) {
                max_distance = current_distance;
                new_direction = i;
            }

        }

        return new_direction;
    }

}
