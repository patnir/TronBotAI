/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tron.core;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import tron.control.Controller;
import tron.properties.TronProperties;

/**
 *
 * @author User
 */
public class Game {

    public final static int NORTH = 0;
    public final static int EAST = 1;
    public final static int SOUTH = 2;
    public final static int WEST = 3;

    private static int Num_Players;

    private static int Height;
    private static int Width;

    private static Game singleton;

    private Player[] players;
    private int[][] board;

    private int num_remaining;
    private boolean game_over;

    private TronProperties options;

    private Game() { }

    public static Game getSingleton() {
        if(singleton == null)
            singleton = new Game();
        return singleton;
    }

    public static void initialize(Controller[] controllers) {
        int startX;
        int startY;

        int tempX;
        int tempY;
        boolean valid;

        int direction;
        Color color;
        TronProperties options = TronProperties.getSingleton();
 
        Num_Players = Integer.parseInt(options.getProperty("num.players"));
        
        Width = Integer.parseInt(options.getProperty("board.width"));
        Height = Integer.parseInt(options.getProperty("board.height"));

        // Force initiation
        getSingleton();

        singleton.num_remaining = Num_Players;
        singleton.game_over = false;

        singleton.options = options;

        singleton.players = new Player[Num_Players];
        singleton.board = new int[Width][Height];

        for(int i=0;i<Num_Players;i++) {
            
            if(options.getProperty("position").equalsIgnoreCase("fixed")) {
                startX = Integer.parseInt(options.getProperty("p"+(i+1)+".x")) - 1;
                startY = Integer.parseInt(options.getProperty("p"+(i+1)+".y")) - 1;
                direction = Integer.parseInt(options.getProperty("p"+(i+1)+".dir"));
            } else {
                do {
                    valid = true;

                    tempX = (int) (Math.random() * Width);
                    tempY = (int) (Math.random() * Height);

                    direction = (int) (Math.random() * 4);

                    for(int p=0;p<i;p++) {
                        if(singleton.players[p].getX() == tempX &&
                                singleton.players[p].getY() == tempY) {
                            valid = false;
                        }
                    }

                } while(!valid);

                startX = tempX;
                startY = tempY;
            }

            color  = new Color(Integer.parseInt(options.getProperty("p"+(i+1)+".colour"), 16));

            singleton.players[i] = new Player(startX, startY, color, direction, controllers[i]);
            singleton.board[singleton.players[i].getX()][singleton.players[i].getY()] = singleton.players[i].getId();
        }
    }

    public void step() {
        Map<Integer, Integer> newCords = new HashMap<Integer, Integer>();
        Set<Integer> losers = new HashSet<Integer>();

        for(int i=0;i<Num_Players;i++) {
            if(!players[i].computeMove()) {
                losers.add(i);
            }
        }

        for(int i=0;i<Num_Players;i++) {
            if(!players[i].hasLost())
            {
                players[i].step();
                if(players[i].getX()<0 || players[i].getX()>=Width ||
                        players[i].getY()<0 || players[i].getY()>=Height) {
                    losers.add(i);
                }
                else if(newCords.containsKey(players[i].getX()*100 + players[i].getY())) {
                    losers.add(i);
                    losers.add(newCords.get(players[i].getX()*100 + players[i].getY()));
                }
                else if(board[players[i].getX()][players[i].getY()] != 0) {
                    losers.add(i);
                }
                else {
                    newCords.put(players[i].getX()*100 + players[i].getY(), i);
                    board[players[i].getX()][players[i].getY()] = players[i].getId();
                }
            }
        }

        num_remaining -= (losers.size() - 1);

        for(int i:losers) {
            players[i].gameOver(num_remaining);
        }

        num_remaining--;

        if((num_remaining == 1 && Num_Players != 1) || num_remaining == 0) {
            int[] winners = getRemainingPids();

            for(int i=0;i<winners.length;i++) {
                players[winners[i] - 1].gameOver(1);
            }

            game_over = true;
        }
    }

    public int getBoardWidth() {
        return board.length;
    }

    public int getBoardHeight() {
        return board[0].length;
    }

    public int getCoord(int x, int y) {
        if(x < 0 || x >= Width || y < 0 || y >= Height) {
            return -1;
        }
        return board[x][y];
    }

    public Player getPlayer(int pid) {
        return players[pid-1];
    }

    public boolean isGameOver() {
        return game_over;
    }

    public int[] getRemainingPids() {
        int[] remaining_pids = new int[num_remaining];
        int counter = 0;

        for(int i = 0;i < Num_Players;i++) {
            if(!players[i].hasLost()) {
                remaining_pids[counter++] = i+1;
            }
        }

        return remaining_pids;
    }

    public int[] getDisplayPids() {
        final int NUM_SCREENS = 4;

        int[] pids = new int[NUM_SCREENS];
        int counter = 0;

        int[] pids_left = getRemainingPids();

        if(pids_left.length >= NUM_SCREENS) {
            for(int i=0;i<pids_left.length && counter < NUM_SCREENS;i++) {
                if(options.getProperty("p"+pids_left[i]+".control").equalsIgnoreCase("Human")) {
                    pids[counter++] = pids_left[i];
                }
            }

            for(int i=0;i<pids_left.length && counter < NUM_SCREENS;i++) {
                if(!options.getProperty("p"+pids_left[i]+".control").equalsIgnoreCase("Human")) {
                    pids[counter++] = pids_left[i];
                }
            }
        }
        else {
            for(int i=0;i<pids_left.length && counter < NUM_SCREENS;i++) {
                pids[counter++] = pids_left[i];
            }

            for(int i=0;i<Num_Players && counter < NUM_SCREENS;i++) {
                if(players[i].hasLost()) {
                    pids[counter++] = i+1;
                }
            }
        }

        return pids;
    }

    public int[] getPlayersInPosition(int position) {
        int counter = 0;
        int[] res;

        for(int i = 0;i<Num_Players;i++) {
            if(players[i].getPosition() == position) {
                counter++;
            }
        }

        res = new int[counter];
        counter = 0;


        for(int i = 0;i<Num_Players;i++) {
            if(players[i].getPosition() == position) {
                res[counter++] = i + 1; //player pid
            }
        }

        return res;
    }
}