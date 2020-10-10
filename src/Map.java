
/*
 * Map class that generates the layout
 * Author: Lancer Guo
 */

import java.util.ArrayList;

public class Map {

    private final int DEFAULT_WIDTH = 18;
    private final int DEFAULT_HEIGHT = 10;

    private int _widthInPixel;
    private int _heightInPixel;
    private int _sizePerTile;
    private int _width;
    private int _height;

    private Tile[][] tiles;
    private ArrayList<Tile> pathTiles;
    private Tile hiveTile;
    private Tile nestTile;

    // simple representation for the board
    // change the array for layout modification
    // 1 represents the bee hive
    // all path needs to follow incremental order
    private static final int[][] mapLayout = new int[][]
            {
                    new int[] { 0, 0, 0, 0,  0,  0,  0,  0,  0, 0},
                    new int[] { 0, 0, 1, 0,  0,  0,  0,  0,  0, 0},
                    new int[] { 0, 0, 2, 0,  0,  0,  0,  0,  0, 0},
                    new int[] { 0, 0, 3, 0,  0,  0,  0,  0,  0, 0},
                    new int[] { 0, 0, 4, 5,  6,  7,  8,  9,  0, 0},
                    new int[] { 0, 0, 0, 0,  0,  0,  0,  10, 0, 0},
                    new int[] { 0, 0, 0, 0,  0,  0,  0,  11, 0, 0},
                    new int[] { 0, 0, 0, 0,  0,  0,  0,  12, 0, 0},
                    new int[] { 0, 0, 0, 0,  0,  0,  0,  13, 0, 0},
                    new int[] { 0, 0, 0, 22, 21, 20, 0,  14, 0, 0},
                    new int[] { 0, 0, 0, 23, 0,  19, 0,  15, 0, 0},
                    new int[] { 0, 0, 0, 24, 0,  18, 17, 16, 0, 0},
                    new int[] { 0, 0, 0, 25, 0,  0,  0,  0,  0, 0},
                    new int[] { 0, 0, 0, 26, 27, 0,  0,  0,  0, 0},
                    new int[] { 0, 0, 0, 0,  28, 0,  0,  0,  0, 0},
                    new int[] { 0, 0, 0, 0,  29, 0,  0,  0,  0, 0},
                    new int[] { 0, 0, 0, 0,  30, 0,  0,  0,  0, 0},
                    new int[] { 0, 0, 0, 0,  0,  0,  0,  0,  0, 0}
            };

    // tile size decides map size
    public Map(int w, int h, int size) {
        _sizePerTile = size;
        _heightInPixel = h;
        _widthInPixel = w;
        _width = _widthInPixel / size;
        _height = _heightInPixel / size;

        tiles = new Tile[_width][_height];
        pathTiles = new ArrayList<Tile>();
    }

    // map size decides tile size
    public Map(int w, int h) {
        _heightInPixel = h;
        _widthInPixel = w;
        _width = DEFAULT_WIDTH;
        _height = DEFAULT_HEIGHT;
        _sizePerTile = _widthInPixel / _width;	//if not proportional then go with width by default

        tiles = new Tile[_width][_height];
        pathTiles = new ArrayList<Tile>();
        System.out.println("Width is " + mapLayout.length + " Height is " + mapLayout[0].length + " Size is " + _sizePerTile);
    }

    public boolean build() {
        int startX = -1, startY = -1;
        int index = 1;
        //find the start point (Hive tile)
        for (int i=0; i<mapLayout.length; i++) {
            for (int j=0; j<mapLayout[i].length; j++) {
                if (mapLayout[i][j] == 1) {
                    tiles[i][j] = new Tile();
                    hiveTile = tiles[i][j];
                    hiveTile.buildHive();
                    startX = i;
                    startY = j;
                    break;
                }
            }
        }

        if (startX == -1 && startY == -1) return false;
        Tile lastTile = null;
        boolean doneBuilding = false;
        boolean foundPath = false;
        while(!doneBuilding) {
            //update the index to look for
            index++;
            //search surrounding
            for (int i=-1; i<=1; i++) {
                for (int j=-1; j<=1; j++) {
                    if ((i != 0 && j != 0) || (i == 0 && j == 0)) { continue; }
                    else {
                        if (mapLayout[startX + i][startY + j] == index) {
                            //System.out.println(i + " " + j + " " + index);
                            Tile tile = tiles[startX+i][startY+j] = new Tile();
                            tiles[startX][startY].createPath(lastTile, tile);
                            lastTile = tiles[startX][startY];

                            pathTiles.add(tile);

                            startX = startX + i;
                            startY = startY + j;
                            foundPath = true;
                        }
                    }
                }
            }
            //determine end condition
            if (foundPath) foundPath = false;
            else doneBuilding = true;
        }

        pathTiles.remove(pathTiles.size() - 1);	//remove last because the last one is NestTile
        nestTile = tiles[startX][startY];
        nestTile.createPath(lastTile, null);
        nestTile.buildNest();
        return true;
    }

    // ask canvas to draw the map
    public void draw(GameCanvas canvas) {
        canvas.registerMap(this);
    }

    public Tile getHiveTile() {
        return hiveTile;
    }

    public Tile getNestTile() {
        return nestTile;
    }

    public ArrayList<Tile> getAllPath() {
        return pathTiles;
    }

    public Tile[][] getAllTiles() {
        return tiles;
    }

    public int getTileSize() {
        return _sizePerTile;
    }

    public Tile getTileAtLocation(int x, int y) {
        int _x = (int)Math.ceil((x) / _sizePerTile);
        int _y = (int)Math.ceil((y) / _sizePerTile);
        //System.out.println(_x + " " + _y + " " + _sizePerTile + " " + x + " " + y);
        return tiles[_x][_y];
    }

}
