import java.util.Random;

public class RandomSwarm extends Random {


    public RandomSwarm() {
    }

    public RandomSwarm(long seed) {
        super(seed);
    }

    RandomBees randomBees = new RandomBees();

    public SwarmOfHornets nextSwarm(){
        SwarmOfHornets swarm = new SwarmOfHornets();
        Hornet hornet;
        Tile t = new Tile();
        t.buildNest();
        int r = nextInt(10);
        for (int i = 0; i < r; i++){
            swarm.addHornet(randomBees.nextHornet(t));
        }
        return swarm;
    }

}
