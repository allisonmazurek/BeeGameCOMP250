import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class BeeGameGUI {

    private JPanel mainPanel;
    private JPanel BeeButtonPanel;
    private JPanel TilePanel;
    private JButton busyBeeButton;
    private JButton stingyBeeButton;
    private JButton tankyBeeButton;
    private JButton takeActionButton;
    private JButton NewSwarm;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JTextArea textAreaFood;
    JButton[][] buttons;
    int N = 10; // <-- Change to change board dimensions
    private int food = 20; // <-- Change to change starting food amount
    private RandomBees randomBees;
    private RandomTile randomTile;
    private RandomSwarm randomSwarm;
    private SwarmOfHornets swarm;
    private Tile selected;
    private Tile[][] tile;
    public static Tile nestTile;
    public static Tile hiveTile;


    public BeeGameGUI() throws IOException {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame();
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel[][] panelHolder = new JPanel[N][N];
        GridLayout layout = new GridLayout(N, N);
        layout.setHgap(5);
        layout.setVgap(0);
        TilePanel.setBackground(Color.ORANGE.darker());
        TilePanel.setLayout(layout);
        textAreaFood.setText("FOOD: " + food);

        tile = new Tile[N][N];
        buttons = new JButton[N][N];

        ArrayList<Tile> tileList = new ArrayList<>();
        ArrayList<JButton> buttonList = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                panelHolder[i][j] = new JPanel();
                panelHolder[i][j].setBackground(Color.ORANGE.darker());
                JButton b = new JButton();
                randomTile = new RandomTile();
                double d = Math.random();
                if (d <= 0.25) {
                    tile[i][j] = randomTile.nextEmptyTile();
                    System.out.println(tile[i][j].toString());
                }
                if (d > 0.25 && d < 1) {
                    tile[i][j] = randomTile.nextTile();
                    System.out.println(tile[i][j].toString());
                }

                buttons[i][j] = b;
                b.setOpaque(true);
                b.setPreferredSize(new Dimension(70, 70));
                panelHolder[i][j].add(b);
                int finalI = i;
                int finalJ = j;
                b.addActionListener(e -> setTileInfo(((JButton) e.getSource()).getName(), tile[finalI][finalJ]));
                TilePanel.add(panelHolder[i][j]);
            }
        }


        int hiveX = 0;
        int hiveY = 0;
        int nestX = N - 1;
        int nestY = N - 1;


        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                StringBuilder sb = new StringBuilder();
                sb.append("src/");
                if (i == nestX && j == nestY) {
                    nestTile = tile[i][j];
                    nestTile.buildNest();
                    tileList.add(tile[i][j]);
                    sb.append("nest");
                }
                else if (i == hiveX && j == hiveY) {
                    hiveTile = tile[i][j];
                    hiveTile.buildHive();
                    tileList.add(tile[i][j]);
                    sb.append("hive");
                }
                else if (i == j) {
                    tileList.add(tile[i][j]);
                    sb.append("path");
                }
                sb.append(".png");
                try {
                    Image tileImage = ImageIO.read(new File(sb.toString()));
                    tileImage = tileImage.getScaledInstance(70, 70, 1);
                    buttons[i][j].setIcon(new ImageIcon(tileImage));
                } catch (IOException ioException) {
//                    ioException.printStackTrace();
                }
            }
        }

        int initial = 1;
        int m = tileList.size() - 1;
        for (int k = initial; k < tileList.size() - 1; k++) {
            if (tileList.get(initial - 1).isHive()) {
                if (k == initial) {
                    tileList.get(k - 1).createPath(null, tileList.get(k));
                    tileList.get(m).createPath(tileList.get(m - 1), null);
                }
                tileList.get(k).createPath(tileList.get(k - 1), tileList.get(k + 1));
            }
            else if (tileList.get(initial - 1).isNest()) {
                if (k == initial) {
                    tileList.get(k - 1).createPath(tileList.get(k), null);
                    tileList.get(m).createPath(null, tileList.get(m - 1));
                }
                tileList.get(k).createPath(tileList.get(k + 1), tileList.get(k - 1));
            }
        }

        createUIComponents();
        frame.setSize(750, 500);
        frame.setBackground(Color.ORANGE.darker());
        frame.setVisible(true);
    }

    private void setTileInfo(String name, Tile t) {
        textAreaFood.setText("FOOD: " + food);
        textArea2.setText("");
        textArea1.setText(
                t.toString()
                        + "\n Hive: " + t.isHive()
                        + "\n Nest: " + t.isNest()
                        + "\n On Path: " + t.isOnThePath()
                        + "\n Bee: " + ((t.getBee() != null) ? t.getBee().getClass().getSimpleName() +
                                        ", Health: " + t.getBee().getHealth() : null)
                        + "\n Swarm: " + t.getNumOfHornets()
                        + "\n Toward Hive: " + t.towardTheHive()
                        + "\n Toward Nest: " + t.towardTheNest()
                        + "\n"
        );
        selected = t;
    }

    private void setErrorInfo(String name, Tile t) {
        textArea1.setText(t.toString());
    }

    private void CreateBusyBee(Tile t) {
        randomBees = new RandomBees();
        try {
            BusyBee insect = randomBees.nextBusyBee(t);
            if (food > ((BusyBee) insect).getCost()) {
                t.addInsect(insect);
                food -= ((BusyBee) insect).getCost();
                textAreaFood.setText("FOOD: " + food);
               updateTiles();
            } else textArea2.setText("Not enough food to buy BusyBee");
        } catch (Exception e) {
            System.out.println(e);
            textArea2.setText("Cannot add BusyBee to tile");
        }
    }

    private void CreateStingyBee(Tile t) {
        randomBees = new RandomBees();
        try {
            StingyBee insect = randomBees.nextStingyBee(t);
            if (food > ((StingyBee) insect).getCost()) {
                t.addInsect(insect);
                food -= ((StingyBee) insect).getCost();
                textAreaFood.setText("FOOD: " + food);
                updateTiles();
            } else textArea2.setText("Not enough food to buy StingyBee");
        } catch (Exception e) {
            System.out.println(e);
            textArea2.setText("Cannot add StingyBee to tile");
        }
    }

    private void CreateTankyBee(Tile t) {
        randomBees = new RandomBees();
        try {
            TankyBee insect = randomBees.nextTankyBee(t);
            if (food > ((TankyBee) insect).getCost()) {
                t.addInsect(insect);
                food -= ((TankyBee) insect).getCost();
                textAreaFood.setText("FOOD: " + food);
                updateTiles();
            } else textArea2.setText("Not enough food to buy TankyBee");
        } catch (Exception e) {
            System.out.println(e);
            textArea2.setText("Cannot add TankyBee to tile");
        }
    }

    private SwarmOfHornets CreateHornetSwarm() {
        randomSwarm = new RandomSwarm();
        try {
            swarm = randomSwarm.nextSwarm();
            updateTiles();
            return swarm;
        } catch (Exception e) {
            System.out.println(e);
            textArea2.setText("Cannot add Hornet Swarm to tile");
        }
        return null;
    }


    private void createUIComponents() {
        busyBeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateBusyBee(selected);
                textArea1.setText(selected.toString());
            }
        });
        stingyBeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateStingyBee(selected);
                textArea1.setText(selected.toString());
            }
        });
        tankyBeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateTankyBee(selected);
                textArea1.setText(selected.toString());
            }
        });
        NewSwarm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateHornetSwarm();
            }
        });
        takeActionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int swarmCount = 0;
                for (int i = 0; i < tile.length; i++) {
                    for (int j = 0; j < tile[i].length; j++) {
                        Tile t = tile[i][j];
                        if (t != null && t.getBee() != null) {
                            if (t.getBee() instanceof BusyBee) {
                                food += t.collectFood();
                            }
                            t.getBee().takeAction();
                        }
                        if (t.getNumOfHornets() > 0) {
                            swarmCount++;
                            for (Hornet hornet : swarm.getHornets()) {
                                hornet.takeAction();
                            }
                        }
                    }
                }
                if (swarmCount == 0)
                    swarm = null;
                updateTiles();
                textAreaFood.setText("FOOD: " + food);
            }
        });
    }

    public void updateTiles() {
        for (int i = 0; i < tile.length; i++) {
            for (int j = 0; j < tile[i].length; j++) {
                Tile t = tile[i][j];
                StringBuilder sb = new StringBuilder();
                sb.append("src/");
                if (t.isNest())
                    sb.append("nest");
                else if (t.isHive())
                    sb.append("hive");
                else if (t.isOnThePath())
                    sb.append("path");
                else
                    continue;
                if (t != null && t.getBee() != null) {
                    t.getBee().takeAction();
                    sb.append(t.getBee().getClass().getSimpleName());
                }
                if (t.getNumOfHornets() > 0) {
                    for (Hornet hornet : swarm.getHornets()) {
                    }
                    sb.append("Hornet");
                }
                sb.append(".png");
                try {
                    Image tileImage = ImageIO.read(new File(sb.toString()));
                    tileImage = tileImage.getScaledInstance(70, 70, 1);
                    buttons[i][j].setIcon(new ImageIcon(tileImage));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) throws IOException {
        BeeGameGUI program = new BeeGameGUI();
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(10, 0));
        panel1.setBackground(new Color(-10658467));
        panel1.setForeground(new Color(-10658467));
        mainPanel.add(panel1, BorderLayout.EAST);
        BeeButtonPanel = new JPanel();
        BeeButtonPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        BeeButtonPanel.setAlignmentX(0.5f);
        BeeButtonPanel.setAlignmentY(0.5f);
        BeeButtonPanel.setBackground(new Color(-10658467));
        BeeButtonPanel.setPreferredSize(new Dimension(200, 159));
        panel1.add(BeeButtonPanel, BorderLayout.NORTH);
        busyBeeButton = new JButton();
        busyBeeButton.setText(" BusyBee ");
        BeeButtonPanel.add(busyBeeButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        stingyBeeButton = new JButton();
        stingyBeeButton.setText("StingyBee");
        BeeButtonPanel.add(stingyBeeButton, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tankyBeeButton = new JButton();
        tankyBeeButton.setText("TankyBee");
        BeeButtonPanel.add(tankyBeeButton, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        NewSwarm = new JButton();
        NewSwarm.setText(" Next Swarm ");
        BeeButtonPanel.add(NewSwarm, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        BeeButtonPanel.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        textAreaFood = new JTextArea();
        panel2.add(textAreaFood, BorderLayout.CENTER);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout(0, 0));
        panel1.add(panel3, BorderLayout.CENTER);
        textArea2 = new JTextArea();
        panel3.add(textArea2, BorderLayout.CENTER);
        textArea1 = new JTextArea();
        panel3.add(textArea1, BorderLayout.NORTH);
        TilePanel = new JPanel();
        TilePanel.setLayout(new BorderLayout(0, 0));
        mainPanel.add(TilePanel, BorderLayout.CENTER);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}