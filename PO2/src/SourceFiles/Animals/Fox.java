package SourceFiles.Animals;
import SourceFiles.*;
import java.awt.*;
import java.util.Random;

public class Fox extends Animal {
    public Fox(World world) {
        this.world = world;
        initiative = 7;
        power = 3;
        Random random = new Random();
        // Losuj położenie lisa na wolnym polu na planszy
        do {
            int x = random.nextInt(world.GetWidth()) + 1;
            int y = random.nextInt(world.GetHeight()) + 1;
            this.X = x;
            this.Y = y;
        } while (world.GetElement(X, Y) != ' ');
        identity = 'F';
        name = "Fox";
        alive = true;
        generation = 1;
        color = new Color(255, 100, 50);
    }

    public Fox(World world, int X, int Y, int generation) {
        this.world = world;
        initiative = 7;
        power = 3;
        this.X = X;
        this.Y = Y;
        identity = 'F';
        name = "Fox";
        alive = true;
        this.generation = generation;
        color = new Color(255, 100, 50);
    }

    @Override
    public void Action() {
        super.Action();
        Organism org = world.GetOrganism(this, X, Y);

        if (org == null) return;
        // Jeśli moc innego organizmu jest większa niż lisa, lis wraca na poprzednie pole
        if (org.GetPower() > power) {
            X = prev_X;
            Y = prev_Y;
        }
    }
    /**
     * Metoda tworząca nowego lisa potomnego na podanym położeniu.
     * @param X Pozycja X nowo narodzonego lisa.
     * @param Y Pozycja Y nowo narodzonego lisa.
     * @return Nowy lis potomny.
     */

    @Override
    public Organism NewBorn(int X, int Y) {
        return new Fox(world, X, Y, generation + 1);
    }
}