package SourceFiles.Animals;
import SourceFiles.*;
import java.awt.*;
import java.util.Random;

public class Turtle extends Animal {
    private boolean isMoving;

    public Turtle(World world) {
        this.world = world;
        initiative = 1;
        power = 2;
        Random rd = new Random();
        do {
            int x = rd.nextInt(world.GetWidth()) + 1;
            int y = rd.nextInt(world.GetHeight()) + 1;
            SetX(x);
            SetY(y);
        } while (world.GetElement(GetX(), GetY()) != ' ');
        identity = 'T';
        name = "Turtle";
        alive = true;
        generation = 1;
        isMoving = false;
        color = new Color(0, 150, 255);
    }

    /**
     * Konstruktor dla żółwia z zadanym położeniem i pokoleniem.
     * @param world Świat, w którym żółw istnieje.
     * @param X Początkowa pozycja X żółwia.
     * @param Y Początkowa pozycja Y żółwia.
     * @param generation Pokolenie żółwia.
     */

    public Turtle(World world, int X, int Y, int generation) {
        this.world = world;
        initiative = 1;
        power = 2;
        this.X = X;
        this.Y = Y;
        identity = 'T';
        name = "Turtle";
        alive = true;
        this.generation = generation;
        isMoving = false; // Żółw początkowo się nie porusza
        color = new Color(0, 150, 255);
    }
    @Override
    public void Action() {
        isMoving = false;
        Random rd = new Random();
        int chance = rd.nextInt(4) + 1;
        if (chance == 4) {
            super.Action();  // Żółw porusza się tylko z prawdopodobieństwem 1/4
        }
        isMoving = true;
    }
    @Override
    public void Collision(Organism organism) {
        if (organism == null) return;
        if (!isMoving) {
            // Odepchnij organizm na jego poprzednie miejsce, jeśli żółw się nie porusza
            organism.SetX(organism.GetPrevX());
            organism.SetY(organism.GetPrevY());
            String message = organism.GetName() + " was pushed back to his previous position by Turtle at X = " + GetX() + ", Y = " + GetY();
            world.AddMessage(message);
        } else {
            super.Collision(organism);
        }
        isMoving = false;
    }
    /**
     * Metoda sprawdzająca, czy żółw może się obronić.
     * @param organism Organizm, z którym żółw koliduje.
     * @return True, jeśli moc organizmu jest mniejsza lub równa 5, w przeciwnym razie False.
     */

    public boolean Defend(Organism organism) {
        return organism.GetPower() <= 5;
    }
    /**
     * Metoda tworząca nowego żółwia potomnego na podanym położeniu.
     * @param X Pozycja X nowo narodzonego żółwia.
     * @param Y Pozycja Y nowo narodzonego żółwia.
     * @return Nowy żółw potomny.
     */

    public Organism NewBorn(int X, int Y) {
        return new Turtle(world, X, Y, generation + 1);
    }

    public boolean GetIsMoving() {
        return isMoving;
    }

    /**
     * Metoda ustawiająca status poruszania się żółwia.
     * @param move Nowy status poruszania się.
     */

    public void SetIsMoving(boolean move) {
        this.isMoving = move;
    }
}