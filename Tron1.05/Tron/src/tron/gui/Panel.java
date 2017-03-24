/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tron.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import tron.core.Game;
import tron.properties.TronProperties;

/**
 *
 * @author User
 */
public class Panel extends JPanel{

    private final static int MINIMUM_BLOCK_SIZE = 5;

    private BufferedImage img;

    private int pixel_width;
    private int pixel_height;

    private int board_width;
    private int board_height;

    private int displayed_board_width;
    private int displayed_board_height;

    private int block_size;

    private int num_players;

    private boolean relative;

    private Game game;

    public Panel() {
        TronProperties options = TronProperties.getSingleton();

        pixel_width = Integer.parseInt(options.getProperty("px.width"));
        pixel_height = Integer.parseInt(options.getProperty("px.height"));

        board_width = Integer.parseInt(options.getProperty("board.width"));
        board_height = Integer.parseInt(options.getProperty("board.height"));

        num_players  = Integer.parseInt(options.getProperty("num.players"));

        game = Game.getSingleton();

        block_size = Math.min(pixel_width/board_width, pixel_height/board_height);

        if(block_size >= MINIMUM_BLOCK_SIZE) {
            pixel_width = block_size*board_width;
            pixel_height = block_size*board_height;
            relative = false;
        }
        else {
            block_size = MINIMUM_BLOCK_SIZE;
            
            displayed_board_width = pixel_width/block_size;
            displayed_board_height = pixel_height/block_size;

            relative = true;
        }

        
        img = new BufferedImage(pixel_width, pixel_height, BufferedImage.TYPE_INT_ARGB);
        this.setPreferredSize(new Dimension(pixel_width, pixel_height));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        createImage(img.getGraphics());
        g.drawImage(img, 0, 0, this);
    }

    private void createImage(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, pixel_width, pixel_height);

        if(relative) {
            if(num_players == 1) {
                drawRelativePlayer(g, 1, 0, 0, displayed_board_width, displayed_board_height);
            }
            else if(num_players == 2) {
                drawRelativePlayer(g, 1, 0, 0, displayed_board_width/2 - 1, displayed_board_height);
                drawRelativePlayer(g, 2, displayed_board_width/2 + 1, 0, displayed_board_width, displayed_board_height);
            }
            else if(num_players == 3) {
                drawRelativePlayer(g, 1, 0, 0, displayed_board_width/2 - 1, displayed_board_height/2 - 1);
                drawRelativePlayer(g, 2, displayed_board_width/2 + 1, 0, displayed_board_width, displayed_board_height/2 - 1);
                drawRelativePlayer(g, 3, 0, displayed_board_height/2 + 1, displayed_board_width/2 - 1, displayed_board_height);
            }
            else {
                int[] pids = game.getDisplayPids();

                drawRelativePlayer(g, pids[0], 0, 0, displayed_board_width/2 - 1, displayed_board_height/2 - 1);
                drawRelativePlayer(g, pids[1], displayed_board_width/2 + 1, 0, displayed_board_width, displayed_board_height/2 - 1);
                drawRelativePlayer(g, pids[2], 0, displayed_board_height/2 + 1, displayed_board_width/2 - 1, displayed_board_height);
                drawRelativePlayer(g, pids[3], displayed_board_width/2 + 1, displayed_board_height/2 + 1, displayed_board_width, displayed_board_height);
            }
        }
        else {
            for(int y = 0;y < board_height;y++) {
                for(int x = 0;x < board_width;x++) {
                    if(game.getCoord(x, y) == 0) {
                        g.setColor(Color.BLACK);
                    } else {
                        g.setColor(game.getPlayer(game.getCoord(x, y)).getColor());
                    }

                    g.fillRect(x * block_size, (board_height - y -1) * block_size,
                            (int)Math.round(block_size*0.8),
                            (int)Math.round(block_size*0.8));
                }
            }
            if(game.isGameOver()) {
                String display;

                int[] winners = game.getPlayersInPosition(1);

                if(winners.length == 1) { 
                    display = game.getPlayer(winners[0]).getName() + "(" +
                            winners[0] + ") Wins!";
                }
                else {
                    display = game.getPlayer(winners[0]).getName() + "(" +
                            winners[0] + ")";
                    for(int i=1;i<winners.length;i++) {
                        display += " and " + game.getPlayer(winners[1]).getName() + "(" +
                            winners[1] + ")";
                    }
                    display += " Tied!";
                }

                g.setColor(Color.WHITE);
                g.setFont(new Font(null, Font.BOLD, block_size * 4));
                g.drawString(display, (board_width/2 - display.length())*block_size,
                            board_height/2*block_size);
            }
        }
    }

    private void drawRelativePlayer(Graphics g, int pid, int startX, int startY, int endX, int endY) {
        int pX = game.getPlayer(pid).getX();
        int pY = game.getPlayer(pid).getY();
        int boardX;
        int boardY;
        int tile;

        for(int y = startY;y < endY;y++) {
            for(int x = startX;x < endX;x++) {
                boardX = pX + x - (endX - startX)/2 - startX;
                boardY = pY + y - (endY - startY)/2 - startY;

                if(!(boardX >= board_width || boardX < 0 ||
                        boardY >= board_height || boardY < 0)) {
                    tile = game.getCoord(boardX, boardY);

                    if(tile == 0) {
                        g.setColor(Color.BLACK);
                    } else {
                        g.setColor(game.getPlayer(tile).getColor());
                    }

                    g.fillRect(x * block_size, (endY - y  + startY - 1) * block_size,
                            (int)Math.round(block_size*0.8),
                            (int)Math.round(block_size*0.8));
                }
            }
        }

        if(game.getPlayer(pid).hasLost()) {
            String suffix;

            switch(game.getPlayer(pid).getPosition()) {
                case 1:
                    suffix = "st";
                    break;
                case 2:
                    suffix = "nd";
                    break;
                case 3:
                    suffix = "rd";
                    break;
                default:
                    suffix = "th";
            }

            g.setColor(Color.WHITE);

            g.setFont(new Font(null, Font.BOLD, block_size * 4));

            String display = "Player " + pid +": " + game.getPlayer(pid).getPosition() + suffix;

            g.drawString(display, ((endX-startX)/2 + startX - display.length())*block_size,
                    ((endY-startY)/2 + startY)*block_size);
        }

    }

    
}
