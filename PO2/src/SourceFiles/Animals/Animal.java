package SourceFiles.Animals;

import java.util.Objects;
import java.util.Random;
import SourceFiles.*;
public class Animal extends Organism {
    @Override
    public void Action() {
        // Zapisanie poprzedniej pozycji organizmu
        SetPrevX(GetX());
        SetPrevY(GetY());
        GetWorld().SetOrganismOnPoint(GetX(), GetY(), ' ');
        Random random = new Random();
        // Sprawdzenie typu środowiska
        if (Objects.equals(world.GetType(), "Quad")) {
            // Losowanie kierunku
            Turn[] number = Turn.values();
            Turn turn = number[random.nextInt(number.length - 1)];
            // Przesunięcie organizmu w odpowiedni kierunek
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
            // Losowanie kierunku dla środowiska hexagonalnego
            int turn = random.nextInt(6);
            int x = GetX() + dx[turn];
            int y = GetY() + dy[turn];
            // Sprawdzenie czy nowa pozycja jest w granicach planszy
            if (x >= 1 && x <= world.GetWidth() && y >= 1 && y <= world.GetHeight()) {
                SetX(x);
                SetY(y);
            }
        }
    }

    @Override
    public void Collision(Organism organism) {
        if (organism == null)
            return;
        if (GetIdentity() == organism.GetIdentity()) {
            // Rozmnażanie się organizmu tego samego gatunku
            if (GetGeneration() != organism.GetGeneration())
                return;
            // Przywrócenie organizmu na poprzednią pozycję w przypadku braku miejsca na nowego potomka
            SetX(GetPrevX());
            SetY(GetPrevY());
            if (Objects.equals(world.GetType(), "Quad")) {
                // Szukanie pustego miejsca w sąsiedztwie dla nowego potomka w środowisku Quad
                for (int i = GetX() - 1; i <= GetX() + 1; i++) {
                    for (int j = GetY() - 1; j <= GetY() + 1; j++) {
                        if (GetWorld().GetElement(i, j) == ' ') {
                            GetWorld().AddOrganism(NewBorn(i, j));
                            String message = "New " + GetName() + " was born at X = " + j + ", Y = " + i;
                            world.AddMessage(message);
                            return;
                        }
                    }
                }
            }
            else {
                for (int i = 0; i < 6; i++) {
                    int x = GetX() + dx[i];
                    int y = GetY() + dy[i];
                    if (GetWorld().GetElement(x, y) == ' ') {
                        GetWorld().AddOrganism(NewBorn(x, y));
                        String message = "New " + GetName() + " was born at X = " + x + ", Y = " + y;
                        world.AddMessage(message);
                        return;
                    }
                }
            }
        } else {
            if (organism.IsEatenDebuff(GetMe())) {
                organism.Collision(GetMe());
                return;
            }
            if (GetPower() >= organism.GetPower()) {
                // Walka z organizmem o mniejszej sile
                if (organism.Defend(GetMe())) {
                    organism.Collision(GetMe());
                    return;
                }
                organism.SetAlive(false);
                organism.GetWorld().DeleteOrganism(organism);
                String message = organism.GetName() + " was killed by " + GetName() + " at X = " + GetX() + ", Y = " + GetY();
                world.AddMessage(message);
            } else {
                // Ucieczka przed silniejszym organizmem
                if (RunAway(organism)) {
                    GetMe().Collision(organism);
                    return;
                }
                GetWorld().DeleteOrganism(GetMe());
                this.SetAlive(false);
                String message = GetName() + " was killed by " + organism.GetName() + " at X = " + GetX() + ", Y = " + GetY();
                world.AddMessage(message);
            }
        }
    }

    @Override
    public Organism NewBorn(int X, int Y){
        return null;
    }
}
