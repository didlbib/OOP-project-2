package SourceFiles.Animals;
import SourceFiles.*;
import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class Human extends Animal {
    private Turn turn;
    private int HexX, HexY;
    private boolean superPower;
    private int superPowerEndRound = -1;  // Runda, w której kończy się działanie supermocy
    public Human(World world) {
        this.world = world;
        initiative = 4;
        power = 5;
        turn = Turn.NONE;
        superPower = false;
        Random random = new Random();
        // Losuj położenie człowieka na wolnym polu na planszy
        do {
            int x = random.nextInt(world.GetWidth()) + 1;
            int y = random.nextInt(world.GetHeight()) + 1;
            if (world.GetElement(x, y) == ' ') {
                X = x;
                Y = y;
                break;
            }
        } while (true);
        HexX = X;
        HexY = Y;
        identity = 'H';
        name = "Human";
        alive = true;
        generation = 1;
        color = new Color(255, 0, 0);
    }

    /**
     * Konstruktor dla człowieka z zadanym położeniem i pokoleniem.
     * @param world Świat, w którym człowiek istnieje.
     * @param X Początkowa pozycja X człowieka.
     * @param Y Początkowa pozycja Y człowieka.
     * @param generation Pokolenie człowieka.
     */

    public Human(World world, int X, int Y, int generation) {
        this.world = world;
        initiative = 4;
        power = 5;
        this.X = X;
        this.Y = Y;
        HexX = X;
        HexY = Y;
        turn = Turn.NONE;
        superPower = false;
        identity = 'H';
        name = "Human";
        alive = true;
        this.generation = generation;
        color = new Color(255, 0, 0);
    }

    /**
     * Metoda decydująca o ruchu człowieka.
     * Człowiek porusza się w kierunku ustalonym przez wartość turn.
     */

    @Override
    public void Action() {
        // Zapamiętaj poprzednią pozycję
        SetPrevX(GetX());
        SetPrevY(GetY());
        GetWorld().SetOrganismOnPoint(GetX(), GetY(), ' ');
        if (Objects.equals(world.GetType(), "Quad")) {
            // Ruch dla świata kwadratowego
            switch (turn) {
                case LEFT -> {
                    if (GetX() - 1 >= 1) SetX(GetX() - 1);
                }
                case UP -> {
                    if (GetY() - 1 >= 1) SetY(GetY() - 1);
                }
                case RIGHT -> {
                    if (GetX() + 1 <= GetWorld().GetWidth()) SetX(GetX() + 1);
                }
                case DOWN -> {
                    if (GetY() + 1 <= GetWorld().GetHeight()) SetY(GetY() + 1);
                }
            }
        }
        else {
            // Ruch dla świata heksagonalnego
            SetX(GetHexX());
            SetY(GetHexY());
        }
        // Sprawdzamy, czy supermoc jest aktywna
        if (superPower && world.GetRound() <= superPowerEndRound) {
            // Jeśli tak, niszczymy rośliny i zwierzęta na sąsiednich polach
            DestroyOrganismsAround();
        }
    }

    private void DestroyOrganismsAround() {
        int startX = Math.max(1, GetX() - 1);
        int endX = Math.min(GetWorld().GetWidth(), GetX() + 1);
        int startY = Math.max(1, GetY() - 1);
        int endY = Math.min(GetWorld().GetHeight(), GetY() + 1);

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                Organism org = GetWorld().GetOrganism(null, x, y);
                if (org != null && org != this) {
                    org.SetAlive(false);
                    GetWorld().DeleteOrganism(org);
                    String message = org.GetName() + " został zniszczony przez " + GetName() + " na X = " + x + ", Y = " + y;
                    GetWorld().AddMessage(message);
                }
            }
        }
    }

    // Metoda zmieniająca stan supermocy (włączanie/wyłączanie) na podstawie aktualnej rundy
    public void ChangeSuperPower() {
        if (!superPower && world.GetRound() > superPowerEndRound) {
            superPower = true;
            superPowerEndRound = world.GetRound() + 5; // Supermoc działa przez 5 rund
        }
    }

    @Override
    public void Collision(Organism organism) {
        // Jeśli supermoc jest aktywna i działa, dodaj moc człowiekowi
        if (superPower && world.GetRound() <= superPowerEndRound) {
            power += 5;
            superPower = false;
            super.Collision(organism);
            return;
        }
        // Jeśli supermoc działa, zmniejsz moc człowieka
        if (world.GetRound() <= superPowerEndRound) {
            power--;
        }
        super.Collision(organism);
    }
    // Ustawienia i pobieranie kierunku ruchu człowieka
    public void SetTurn(Turn turn) {
        this.turn = turn;
    }
    public Turn GetTurn() {
        return turn;
    }
    // Ustawienia i pobieranie pozycji dla świata heksagonalnego
    public void SetHexX(int X) {
        this.HexX = X;
    }
    public int GetHexX() {
        return HexX;
    }
    public void SetHexY(int Y) {
        this.HexY = Y;
    }
    public int GetHexY() {
        return HexY;
    }

    @Override
    public Organism NewBorn(int X, int Y) {
        return new Human(world, X, Y, generation + 1);
    }
    // Metody do pobierania i ustawiania supermocy

    public boolean GetSuperPower() {
        return superPower;
    }

    public void SetSuperPower(boolean power) {
        superPower = power;
    }
    public int GetEndRound() {
        return superPowerEndRound;
    }
    public void SetEndRound(int endRound) {
        superPowerEndRound = endRound;
    }
}