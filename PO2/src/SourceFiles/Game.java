package SourceFiles;
import javax.swing.*;
//Jest to przydatna klasa do filtrowania plików w oknach dialogowych wyboru pliku (JFileChooser).
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
//Klasa MouseAdapter jest często wykorzystywana do nasłuchiwania zdarzeń myszy w interfejsach użytkownika.
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Objects;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import SourceFiles.Animals.*;
import SourceFiles.Plants.*;

public class Game{
    private Human player;
    private World world;
    private JPanel panel;
    private final JFrame frame;
    private JPanel sidePanel;
    private final JLabel info;
    final int squareSize = 50;

    public Game() {
        frame = new JFrame("Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel sizePanel = new JPanel(new GridLayout(5, 15));
        JLabel widthLabel = new JLabel("Width: ");
        JTextField widthField = new JTextField();
        JLabel heightLabel = new JLabel("Height: ");
        JTextField heightField = new JTextField();
        JRadioButton hexWorldButton = new JRadioButton("HexWorld");
        JRadioButton quadWorldButton = new JRadioButton("QuadWorld");
        ButtonGroup worldGroup = new ButtonGroup();
        worldGroup.add(hexWorldButton);
        worldGroup.add(quadWorldButton);
        sizePanel.add(widthLabel);
        sizePanel.add(widthField);
        sizePanel.add(heightLabel);
        sizePanel.add(heightField);
        sizePanel.add(hexWorldButton);
        sizePanel.add(quadWorldButton);
        sizePanel.setPreferredSize(new Dimension(400, 200));
        widthLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        widthField.setFont(new Font("Arial", Font.PLAIN, 16));
        heightLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        heightField.setFont(new Font("Arial", Font.PLAIN, 16));
        hexWorldButton.setFont(new Font("Arial", Font.PLAIN, 16));
        quadWorldButton.setFont(new Font("Arial", Font.PLAIN, 16));
//Tworzenie przycisku startowego

        AtomicBoolean pressed = new AtomicBoolean(false);
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            int tempW = Integer.parseInt(widthField.getText());
            int tempH = Integer.parseInt(heightField.getText());
            if (hexWorldButton.isSelected()) {
                world = new HexWorld(tempW, tempH);
            } else {
                world = new QuadWorld(tempW, tempH);
            }
            player = new Human(world);
            //Tworzy nowy panel panel o układzie GridLayout z liczbą wierszy i kolumn równą podanym wartościom
            panel = new JPanel(new GridLayout(tempH, tempW));
            panel.setPreferredSize(new Dimension(800, 600));
            //Jeśli wybrany jest HexWorld, ustawia układ panelu na null.
            //Usuwa wszystkie istniejące komponenty z okna i dodaje nowy panel.
            if (Objects.equals(world.GetType(), "Hex")) panel.setLayout(null);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(panel, BorderLayout.CENTER);
            //Wywołuje metodę AddSidePanels(), aby dodać dodatkowe panele boczne
            AddSidePanels();
            //Pakuje okno (frame.pack()) i ustawia je jako widoczne (frame.setVisible(true)).
            //Ustawia zmienną pressed na true, aby zakończyć pętlę oczekiwania.
            frame.pack();
            frame.setVisible(true);
            pressed.set(true);
        });
        //Tworzenie i dodanie panelu startowego:

        JPanel startPanel = new JPanel(new BorderLayout());
        startPanel.add(sizePanel, BorderLayout.CENTER);
        startPanel.add(startButton, BorderLayout.SOUTH);
        info = new JLabel();
        frame.getContentPane().add(startPanel);
        frame.pack();
        frame.setVisible(true);
        //Pętla oczekiwania na naciśnięcie przycisku
        while (!pressed.get()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void AddSidePanels() {
        //Tworzenie przycisków:
        JButton nextRoundButton = new JButton("Next Round");
        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");
        JButton spawnOrganismButton = new JButton("Spawn Organism");
        JButton superPowerButton = new JButton("Use Super Power");
        //Ustawianie maksymalnych rozmiarów przycisków
        nextRoundButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, nextRoundButton.getPreferredSize().height));
        saveButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, saveButton.getPreferredSize().height));
        loadButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, loadButton.getPreferredSize().height));
        spawnOrganismButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, spawnOrganismButton.getPreferredSize().height));
        superPowerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, superPowerButton.getPreferredSize().height));
        //Tworzenie panelu z przyciskami
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        //Dodawanie przycisków do panelu
        buttonsPanel.add(nextRoundButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(loadButton);
        buttonsPanel.add(spawnOrganismButton);
        buttonsPanel.add(superPowerButton);
        //Dodawanie panelu z przyciskami do głównego okna
        frame.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

        //Tworzenie panelu bocznego
        sidePanel = new JPanel();
        //Ustawianie rozmiaru i tła panelu bocznego:
        sidePanel.setPreferredSize(new Dimension(350, 600));
        sidePanel.setBackground(Color.LIGHT_GRAY);
        frame.getContentPane().add(sidePanel, BorderLayout.LINE_END);
    }

    private int GetRandomNumber() {
        Random random = new Random();
        return random.nextInt(10) + 1;
    }

    public void AddOrganismsToWorld() {
        //Dodanie gracza do świata
        world.AddOrganism(player);
        //Iteracja przez wszystkie typy organizmów
        for (Organisms_t organism : Organisms_t.values()) {
            //Generowanie losowej liczby:
            int random = GetRandomNumber();
            //Dodawanie organizmów do świata
            // Pętla, która wykonuje się random razy dla każdego typu organizmu,
            // dodając odpowiednią liczbę organizmów danego typu do świata.
            for (int j = 0; j < random; j++) {
                switch (organism) {
                    case Wolf_t -> world.AddOrganism(new Wolf(world));
                    case Turtle_t -> world.AddOrganism(new Turtle(world));
                    case Sheep_t -> world.AddOrganism(new Sheep(world));
                    case Fox_t -> world.AddOrganism(new Fox(world));
                    case Antelope_t -> world.AddOrganism(new Antelope(world));
                    case Belladonna_t -> world.AddOrganism(new Belladonna(world));
                    case Dandelion_t -> world.AddOrganism(new Dandelion(world));
                    case Grass_t -> world.AddOrganism(new Grass(world));
                    case Guarana_t -> world.AddOrganism(new Guarana(world));
                    case Heracleum_t -> world.AddOrganism(new Heracleum(world));
                    //Zależnie od wartości organism, do świata dodawany jest odpowiedni typ organizmu.
                    //Na przykład, jeśli organism to Wolf_t, do świata dodawany jest nowy obiekt klasy Wolf.
                }
            }
        }
        //Po dodaniu wszystkich organizmów do świata, są one sortowane za pomocą metody SortOrganisms()
        world.SortOrganisms();
    }

    public void Control() {
        while (true) {
            //Sprawdza typ świata gry i wywołuje odpowiednią metodę wyświetlającą.
            if (Objects.equals(world.GetType(), "Quad")) {
                PrintQuadWorld();
            }
            else {
                PrintHexWorld();
            }
            PrintInfo();
            world.ClearMessages();
            //Reakcja na naciśnięcia klawiszy
            MyKeyListener keyListener = new MyKeyListener(frame);
            //Sprawdza, czy któryś z przycisków klawiatury został naciśnięty poprzez obiekt keyListener
            // i odpowiednio reaguje na naciśnięcia
            if (keyListener.isLeftPressed()) {
                player.SetTurn(Turn.LEFT);
            }

            else if (keyListener.isDownPressed()) {
                player.SetTurn(Turn.DOWN);
            }

            else if(keyListener.isRightPressed()) {
                player.SetTurn(Turn.RIGHT);
            }

            else if (keyListener.isUpPressed()) {
                player.SetTurn(Turn.UP);
            }

            else if (keyListener.isTPressed()) {
                world.DoTurn();
                player.SetTurn(Turn.NONE);
            }

            else if(keyListener.isPPressed()) {
                player.ChangeSuperPower();
            }

            else if (keyListener.isSPressed()) {
                Save();
            }

            else if (keyListener.isLPressed()) {
                Load();
            }

            else if (keyListener.isSpawnPressed()) {
                SpawnOrganism();
            }

            else if (keyListener.isEscPressed()) {
                return;
            }
        }
    }

    private void SpawnOrganism() {

        Object[] options = {Organisms_t.Wolf_t.toString(), Organisms_t.Turtle_t.toString(),
                Organisms_t.Sheep_t.toString(), Organisms_t.Fox_t.toString(),
                Organisms_t.Antelope_t.toString(), Organisms_t.Belladonna_t.toString(),
                Organisms_t.Dandelion_t.toString(), Organisms_t.Grass_t.toString(),
                Organisms_t.Guarana_t.toString(), Organisms_t.Heracleum_t.toString()};
        int result = JOptionPane.showOptionDialog(frame, "Choose an organism", "Spawn Organism", JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        AtomicBoolean pressed = new AtomicBoolean(false);
        if (result >= 0) {
            // Pobranie wybranego organizmu na podstawie indeksu z okna dialogowego.
            Organisms_t selectedOrganism = Organisms_t.values()[result];
            panel.removeAll();

            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    double XDifference = 800 / (double)world.GetWidth();

                    if (Objects.equals(world.GetType(), "Hex")) {
                        double hexDiff = world.GetWidth() / 100.0 / 2.0;
                        XDifference /= (1 + hexDiff);
                    }
                    double YDifference = 600 / (double)world.GetHeight();

                    int X = (int)Math.ceil((double)e.getX() / XDifference);
                    int Y = (int)Math.ceil((double)e.getY() / YDifference);

                    switch (selectedOrganism) {
                        case Wolf_t -> world.AddOrganism(new Wolf(world, X, Y, 1));
                        case Turtle_t -> world.AddOrganism(new Turtle(world, X, Y, 1));
                        case Sheep_t -> world.AddOrganism(new Sheep(world, X, Y, 1));
                        case Fox_t -> world.AddOrganism(new Fox(world, X, Y, 1));
                        case Antelope_t -> world.AddOrganism(new Antelope(world, X, Y, 1));
                        case Belladonna_t -> world.AddOrganism(new Belladonna(world, X, Y, 1));
                        case Dandelion_t -> world.AddOrganism(new Dandelion(world, X, Y, 1));
                        case Grass_t -> world.AddOrganism(new Grass(world, X, Y, 1));
                        case Guarana_t -> world.AddOrganism(new Guarana(world, X, Y, 1));
                        case Heracleum_t -> world.AddOrganism(new Heracleum(world, X, Y, 1));
                    }

                    world.AddMessage(world.GetOrganism(null, X, Y).GetName() + " was born at X = " + X + ", Y = " + Y);
                    pressed.set(true);
                    panel.removeMouseListener(this);
                }
            });
        }
        while (!pressed.get()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void PrintQuadWorld() {
        panel.removeAll();
        for (int i = 1; i <= world.GetHeight(); i++) {
            for (int j = 1; j <= world.GetWidth(); j++) {
                Organism organism = world.GetOrganism(null, j, i);
                JPanel square = new JPanel();
                square.setPreferredSize(new Dimension(squareSize, squareSize));
                if (organism != null) {
                    Color color = organism.GetColor();
                    square.setBackground(color);
                    square.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (SwingUtilities.isLeftMouseButton(e)) {
                                // Utworzenie komunikatu z informacjami o organizmie.
                                String info = organism.GetName() + " at X = " + organism.GetX() + " Y = " + organism.GetY();
                                JOptionPane.showMessageDialog(panel, info);
                            }
                        }
                    });
                } else {
                    // Jeśli na danych współrzędnych nie ma organizmu, ustaw kolor tła na biały.
                    square.setBackground(Color.WHITE);
                }
                // Dodanie kwadratu do panelu.
                panel.add(square);
            }
        }
        // Ponowne zaktualizowanie wyglądu panelu.
        panel.revalidate();
        panel.repaint();
    }

    private void PrintHexWorld() {
        panel.removeAll();
        // Określenie liczby wierszy i kolumn oraz promienia heksagonu w zależności od rozmiaru panelu.
        int numRows = world.GetHeight();
        int numCols = world.GetWidth();
        int radius = Math.min(panel.getHeight() / (3 * world.GetHeight()), panel.getWidth() / (2 * world.GetWidth()));
        int hexWidth = (int) (Math.sqrt(3) * radius);
        int offset = radius + (hexWidth / 2);
        // Iteracja po wszystkich wierszach i kolumnach świata.
        for (int i = 1; i <= numRows; i++) {
            for (int j = 1; j <= numCols; j++) {
                // Pobranie organizmu z określonych współrzędnych.
                Organism organism = world.GetOrganism(null, j, i);
                // Utworzenie nowego panelu reprezentującego heksagon.
                JPanel  hex = new JPanel () {
                    @Override
                    public void paintComponent(Graphics g) {
                        // Obliczenie współrzędnych wierzchołków heksagonu.
                        super.paintComponent(g);
                        int[] xPoints = {radius, hexWidth + radius + 2, 2 * hexWidth + 4, hexWidth + radius + 2, radius, 0};
                        int[] yPoints = {0, 0, radius + 4, 2 * radius + 8, 2 * radius + 8, radius + 4};
                        // Narysowanie konturu heksagonu.
                        g.setColor(Color.BLACK);
                        g.drawPolygon(xPoints, yPoints, 6);
                        // Jeśli istnieje organizm na tych współrzędnych, wypełnij heksagon kolorem organizmu.
                        if (organism != null) {
                            g.setColor(organism.GetColor());
                            g.fillPolygon(xPoints, yPoints, 6);
                        }
                    }
                };
                // Obliczenie pozycji x i y dla heksagonu na podstawie aktualnego wiersza i kolumny.
                int x = (j - 1) * (int)(hexWidth * 1.1) + (offset * (i % 2));
                int y = (i - 1) * (radius + radius / 2);
                if (i % 2 == 0) {
                    x += (int)(radius * 2.7);
                }
                // Ustawienie pozycji i rozmiaru heksagonu oraz jego koloru na biały.
                hex.setBounds((x - offset) * 2, y * 2, (int)(hexWidth * 2.2), 3 * radius);
                hex.setBackground(Color.WHITE);
                // Dodanie nasłuchiwacza myszy, który wyświetli informacje o organizmie po lewym kliknięciu
                // lub pozwoli na przesunięcie gracza po prawym kliknięciu.

                int finalI = i;
                int finalJ = j;
                hex.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            if (organism != null) {
                                String info = organism.GetName() + " at X = " + organism.GetX() + " Y = " + organism.GetY();
                                JOptionPane.showMessageDialog(panel, info);
                            }
                        }
                        if (SwingUtilities.isRightMouseButton(e)) {
                            // Sprawdzenie czy ruch gracza jest dozwolony i wykonanie go.
                            final int[] dx = { -1, -1, 0, 1, 1, 0 };
                            final int[] dy = { -1, 0, 1, 1, 0, -1 };
                            boolean goodTurn = false;
                            for (int q = 0; q < 6; q++) {
                                int tempX = player.GetX() + dx[q];
                                int tempY = player.GetY() + dy[q];
                                if (finalJ == tempX && finalI == tempY) goodTurn = true;
                            }
                            if (finalJ >= 0 && finalJ <= world.GetWidth() && finalI >= 0 && finalI <= world.GetHeight() && goodTurn) {
                                player.SetHexX(finalJ);
                                player.SetHexY(finalI);
                                PrintInfo();
                            }
                        }
                    }
                });
                // Dodanie heksagonu do panelu.
                panel.add(hex);

            }
        }
        // Ponowne zaktualizowanie wyglądu panelu.
        panel.revalidate();
        panel.repaint();
    }


    private void PrintInfo() {
        String text = "<html><div style='padding-left:10px'>Help:<br>" +
                "Click on organism to see advanced info<br>" +
                "Choose direction for human - Arrows / Mouse<br><br>" +
                "Game status:<br>" +
                "Player/Creator:Anastasiia Kniazhevska nr. 196629<br>" +
                "Round: " + world.GetRound() + "<br>";
        // Sprawdzenie dostępności supermocy gracza i dodanie informacji do tekstu.
        if (!player.GetAlive() || world.GetRound() <= player.GetEndRound()) {
            text += "Superpower is not available<br>";
        }
        else {
            text += "Superpower is available<br>";
        }
        // Dodanie informacji o aktualnym ruchu gracza w zależności od typu świata.
        if (Objects.equals(world.GetType(), "Quad")) {
            text += "Turn :" + player.GetTurn();
        }
        else {
            text += "Turn : X = " + player.GetHexX() + ", Y = " + player.GetHexY();
        }
        // Konstruowanie tekstu zawierającego logi z gry.
        StringBuilder logsText = new StringBuilder("<br><br>Logs:<br>");
        for (int i = 0; true; i++) {
            if (Objects.equals(world.GetMessage(i), "Nothing")) break;
            logsText.append(world.GetMessage(i)).append("<br>");
        }
        // Połączenie tekstu z logami z resztą tekstu.
        text += logsText.toString();
        text += "</div></html>";

        // Ustawienie tekstu w panelu informacyjnym i jego wyglądu.
        info.setText(text);
        info.setHorizontalAlignment(JLabel.LEFT);
        info.setVerticalAlignment(JLabel.TOP);
        info.setPreferredSize(new Dimension(350, 600));
        sidePanel.add(info);
        info.revalidate();
        info.repaint();
    }


    private void Save() {

        // Wyświetlenie okna dialogowego, w którym gracz wprowadza nazwę pliku do zapisu.
        String saveName = JOptionPane.showInputDialog(panel, "Enter the name of the saved file");
        // Dodanie rozszerzenia ".save" do nazwy pliku.

        saveName += ".save";
        // Utworzenie nowego obiektu pliku na podstawie nazwy.

        File file = new File(saveName);
        try {
            // Utworzenie obiektu FileWriter dla pliku.

            FileWriter writer = new FileWriter(file);
            // Pobranie listy organizmów z świata.

            Vector<Organism> orgs = world.GetOrganisms();
            // Zapisanie informacji o rundzie, wysokości, szerokości i typie świata do pliku.
            writer.write(world.GetRound() + " ");
            writer.write(world.GetHeight() + " ");
            writer.write(world.GetWidth() + " ");
            writer.write(world.GetType() + " ");
            writer.write(System.lineSeparator());
            // Iteracja przez wszystkie organizmy i zapisanie ich informacji do pliku.
            for (Organism org : orgs) {
                writer.write(org.GetIdentity() + " ");
                writer.write(org.GetX() + " ");
                writer.write(org.GetY() + " ");
                writer.write(org.GetGeneration() + " ");
                writer.write(org.GetPower() + " ");
                // Dodatkowe informacje dla konkretnych typów organizmów.
                if (org.GetIdentity() == 'H') {
                    writer.write(((Human) org).GetSuperPower() + " ");
                    writer.write(((Human) org).GetEndRound() + " ");
                }
                if (org.GetIdentity() == 'A') {
                    writer.write(((Antelope) org).GetIsRunning() + " ");
                }
                if (org.GetIdentity() == 'T') {
                    writer.write(((Turtle) org).GetIsMoving() + " ");
                }
                writer.write(System.lineSeparator());
            }
            // Dodanie komunikatu o pomyślnym zapisaniu do logów gry.
            world.AddMessage("Save successfully");
            // Zamknięcie obiektu writer.
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Load() {
        // Utworzenie obiektu JFileChooser w bieżącym katalogu.
        JFileChooser fileChooser = new JFileChooser(".");
        // Ustawienie filtru plików dla wyboru plików z rozszerzeniem ".save".
        fileChooser.setFileFilter(new FileNameExtensionFilter("Saved Files (*.save)", "save"));
        // Wyświetlenie okna dialogowego do wyboru pliku.
        int result = fileChooser.showOpenDialog(null);
        // Sprawdzenie czy plik został wybrany.
        if (result == JFileChooser.APPROVE_OPTION) {
            // Pobranie wybranego pliku.
            File selectedFile = fileChooser.getSelectedFile();
            // Pobranie nazwy wybranego pliku.
            String saveName = selectedFile.getName();
            try {
                // Utworzenie obiektu BufferedReader dla odczytu pliku.
                BufferedReader reader = new BufferedReader(new FileReader(saveName));
                int temp_h, temp_w, round;
                // Odczytanie pierwszej linii pliku zawierającej informacje o rundzie,
                // wysokości, szerokości i typie świata.
                String line = reader.readLine();
                String[] parameters = line.split(" ");
                round = Integer.parseInt(parameters[0]);
                temp_h = Integer.parseInt(parameters[1]);
                temp_w = Integer.parseInt(parameters[2]);
                String type = parameters[3];

                // Utworzenie nowego świata na podstawie odczytanych parametrów.
                if (Objects.equals(type, "Quad")) {
                    world = new QuadWorld(temp_w, temp_h);
                    panel.setLayout(new GridLayout(temp_h, temp_w));
                }
                else {
                    world = new HexWorld(temp_w, temp_h);
                    panel.setLayout(null);
                }
                world.SetRound(round);

                // Odczytanie kolejnych linii pliku zawierających informacje o organizmach.
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split(" ");
                    char identity = tokens[0].charAt(0);
                    int X = Integer.parseInt(tokens[1]);
                    int Y = Integer.parseInt(tokens[2]);
                    int Gen = Integer.parseInt(tokens[3]);
                    int Power = Integer.parseInt(tokens[4]);

                    // Utworzenie i dodanie odpowiedniego organizmu do świata.
                    switch (identity) {
                        case 'H' -> {
                            player = new Human(world, X, Y, Gen);
                            world.AddOrganism(player);
                            player.SetPower(Power);
                            boolean SuperPower = Boolean.parseBoolean(tokens[5]);
                            player.SetSuperPower(SuperPower);
                            int endRound = Integer.parseInt(tokens[6]);
                            player.SetEndRound(endRound);
                        }
                        case 'W' -> {
                            Wolf wolf = new Wolf(world, X, Y, Gen);
                            world.AddOrganism(wolf);
                            wolf.SetPower(Power);
                        }
                        case 'A' -> {
                            Antelope antelope = new Antelope(world, X, Y, Gen);
                            world.AddOrganism(antelope);
                            antelope.SetPower(Power);
                            boolean isRunning = Boolean.parseBoolean(tokens[5]);
                            antelope.SetIsRunning(isRunning);
                        }
                        case 'F' -> {
                            Fox fox = new Fox(world, X, Y, Gen);
                            world.AddOrganism(fox);
                            fox.SetPower(Power);
                        }
                        case 'S' -> {
                            Sheep sheep = new Sheep(world, X, Y, Gen);
                            world.AddOrganism(sheep);
                            sheep.SetPower(Power);
                        }
                        case 'T' -> {
                            Turtle turtle = new Turtle(world, X, Y, Gen);
                            world.AddOrganism(turtle);
                            turtle.SetPower(Power);
                            boolean isMoving = Boolean.parseBoolean(tokens[5]);
                            turtle.SetIsMoving(isMoving);
                        }
                        case 'B' -> {
                            Belladonna belladonna = new Belladonna(world, X, Y, Gen);
                            world.AddOrganism(belladonna);
                        }
                        case 'D' -> {
                            Dandelion dandelion = new Dandelion(world, X, Y, Gen);
                            world.AddOrganism(dandelion);
                        }
                        case 'G' -> {
                            Grass grass = new Grass(world, X, Y, Gen);
                            world.AddOrganism(grass);
                        }
                        case 'U' -> {
                            Guarana guarana = new Guarana(world, X, Y, Gen);
                            world.AddOrganism(guarana);
                        }
                        case 'M' -> {
                            Heracleum heracleum = new Heracleum(world, X, Y, Gen);
                            world.AddOrganism(heracleum);
                        }
                    }
                }
                world.AddMessage("Load successfully");
                reader.close();
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
