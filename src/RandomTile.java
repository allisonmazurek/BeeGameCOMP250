import java.util.Random;

public class RandomTile extends Random {

    public RandomTile() {
        super();
    }

    public RandomTile(long seed) {
        super(seed);
    }

    public Tile nextEmptyTile() {
        return new Tile();
    }

    public Tile nextTile() {
        Tile t = new Tile();
        int food = food();
        if (food > 0) t.storeFood(food);

//        boolean hive = isHive(); //TODO: check if both a hive and nest is built on the same tile. CANNOT HAPPEN!
//        boolean nest = isNest();
//
//        if (hive) t.buildHive();
//        if (nest) t.buildNest();
        //TODO: check can a bee be added on a tile with a nest
        return t;
    }

    private int food() {
        double d = Math.random();
        int food = nextInt(6);
        if (Math.random() < 0.7) return food;
        else return 0;
    }

    private boolean isHive() {
        double d = Math.random();
        if (d < 0.25) return nextBoolean();
        return false;
    }

    private boolean isNest() {
        double d = Math.random();
        if (d < 0.25) return nextBoolean();
        return false;
    }
}
