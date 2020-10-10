
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/*
 * Game board rendering class
 * Author: Lancer Guo
 */

public class GameCanvas extends Canvas {

    final static Color HIVE_COLOR = Color.yellow;
    final static Color NEST_COLOR = Color.red;
    final static Color PATH_COLOR = Color.gray;
    final static Color BUSYBEE_COLOR = Color.green;
    final static Color STINGYBEE_COLOR = Color.blue;
    final static Color TANKYBEE_COLOR = Color.cyan;

    Map _map;

    private boolean _mapGenerated = false;


    public GameCanvas(int canvasX, int canvasY, int canvasWidth, int canvasHeight) {
        //this.setBackground();

        this.setBounds(canvasX, canvasY, canvasWidth, canvasHeight);
    }

    public void registerMap(Map map) {
        _map = map;
    }

    // paint is called automatically every turn
    // update map based on map object status
    @Override
    public void paint(Graphics g) {
        // TODO Auto-generated method stub
        super.paint(g);

        if (_map == null) return;

        Tile[][] tiles = _map.getAllTiles();
        int size = _map.getTileSize();

        for (int i=0; i<tiles.length; i++) {
            for (int j=0; j<tiles[i].length; j++) {
                int x = size * i + 1;
                int y = size * j + 1;

                if (tiles[i][j] != null) {
                    if (tiles[i][j].isHive()) {
                        g.setColor(Color.black);
                        g.drawRect(x, y, size, size);
                        g.setColor(HIVE_COLOR);
                        g.fillRect(x, y, size, size);
                    }
                    else if (tiles[i][j].isNest()) {
                        g.setColor(Color.black);
                        g.drawRect(x, y, size, size);
                        g.setColor(NEST_COLOR);
                        g.fillRect(x, y, size, size);
                    }
                    else if (tiles[i][j].isOnThePath()) {
                        g.setColor(Color.black);
                        g.drawRect(x, y, size, size);
                        g.setColor(PATH_COLOR);
                        g.fillRect(x, y, size, size);
                    }

                    if (tiles[i][j].getBee() != null) {
                        HoneyBee bee = tiles[i][j].getBee();
                        if (bee instanceof BusyBee) {
                            g.setColor(Color.black);
                            g.drawRect(x, y, size, size);
                            g.setColor(BUSYBEE_COLOR);
                            g.fillRect(x, y, size, size);
                        }
                        else if (bee instanceof StingyBee) {
                            g.setColor(Color.black);
                            g.drawRect(x, y, size, size);
                            g.setColor(STINGYBEE_COLOR);
                            g.fillRect(x, y, size, size);
                        }
                        else if (bee instanceof TankyBee) {
                            g.setColor(Color.black);
                            g.drawRect(x, y, size, size);
                            g.setColor(TANKYBEE_COLOR);
                            g.fillRect(x, y, size, size);
                        }
                    }
                    if (tiles[i][j].getNumOfHornets() > 0) {
                        String string = "" + tiles[i][j].getNumOfHornets();
                        int fontSize = 20;
                        g.setColor(Color.BLACK);
                        g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
                        g.drawString(string, x + 5, y + size - 5);
                    }
                }
                else {
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, size, size);
                }
            }
        }
    }

}
