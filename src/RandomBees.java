import java.util.Random;

public class RandomBees extends Random {

    public RandomBees() {
        super();
    }

    public RandomBees(long seed) {
        super(seed);
    }

    public Insect nextInsect(Tile input) {
        int choice = nextInt(4);
        Insect insect = null;
        if (choice == 0) {
            insect = nextStingyBee(input);
            input.addInsect(insect);
            return insect;
        } else if (choice == 1) {
            insect = nextBusyBee(input);
            input.addInsect(insect);
            return insect;
        } else if (choice == 2) {
            insect = nextTankyBee(input);
            input.addInsect(insect);
            return insect;
        } else if (choice == 3) {
            insect = nextHornet(input);
            return insect;
        }
        return null;
    }

    public StingyBee nextStingyBee(Tile input) {
        return new StingyBee(input, next(10));
    }

    public BusyBee nextBusyBee(Tile input) {
        return new BusyBee(input);
    }

    public TankyBee nextTankyBee(Tile input) {
        return new TankyBee(input, nextInt(10), nextInt(40)); //TODO: Check negatives
    }

    public Hornet nextHornet(Tile input) {
        return new Hornet(input, nextInt(25), nextInt(15)); //TODO: Check negatives
    }

}

