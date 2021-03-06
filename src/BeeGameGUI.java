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
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JTextArea textAreaFood;
    private int food = 20;
    private RandomBees randomBees;
    private RandomTile randomTile;
    private Tile selected;


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

        JPanel[][] panelHolder = new JPanel[10][10];
        GridLayout layout = new GridLayout(10, 10);
        layout.setHgap(10);
        layout.setVgap(0);
        TilePanel.setBackground(Color.ORANGE.darker());
        TilePanel.setLayout(layout);
        textAreaFood.setText("FOOD: " + food);

        Tile[][] tile = new Tile[10][10];
        JButton[][] buttons = new JButton[10][10];

        ArrayList<Tile> tileList = new ArrayList<>();
        ArrayList<JButton> buttonList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
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
                b.setPreferredSize(new Dimension(50, 50));
                panelHolder[i][j].add(b);
                int finalI = i;
                int finalJ = j;
                b.addActionListener(e -> setTileInfo(((JButton) e.getSource()).getName(), tile[finalI][finalJ]));
                TilePanel.add(panelHolder[i][j]);
            }
        }

        int nestX = 0;
        int nestY = 1;
        int hiveX = 4;
        int hiveY = 5;

        Tile nestTile = tile[0][1];
        nestTile.buildNest();
        Tile hiveTile = tile[4][5];
        hiveTile.buildHive();

        for (int i = 0; i <= 4; i++) {
            tileList.add(tile[i][nestY]);
            buttonList.add(buttons[i][nestY]);

        }
        for (int i = 1 + 1; i <= 5; i++) {
            tileList.add(tile[4][i]);
            buttonList.add(buttons[4][i]);
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < tileList.size() - 1; k++) {
                    if (tileList.contains(tile[i][j]))
                        tile[i][j].createPath(tileList.get(k), tileList.get(k + 1));
                }
            }
        }


        Image imageHive = ImageIO.read(new File("src/hive.bmp"));
        imageHive = imageHive.getScaledInstance(40, 40, 1);

        Image imageNest = ImageIO.read(new File("src/nest.bmp"));
        imageNest = imageNest.getScaledInstance(40, 40, 1);

        Image imagePath = ImageIO.read(new File("src/grey.bmp"));
        imagePath = imagePath.getScaledInstance(40, 40, 1);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (buttonList.contains(buttons[i][j]) && buttonList.get(0) == buttons[i][j]) {
                    buttons[0][1].setIcon(new ImageIcon(imageHive));
                } else if (buttonList.contains(buttons[i][j]) && buttonList.get(buttonList.size() - 1) == buttons[i][j]) {
                    buttons[4][5].setIcon(new ImageIcon(imageNest));
                } else if (buttonList.contains(buttons[i][j])) {
                    buttons[i][j].setIcon(new ImageIcon(imagePath));
                } else if (tile[i][j].isNest()) {
                    buttons[i][j].setIcon(new ImageIcon(imageNest));
                } else if (tile[i][j].isHive()) {
                    buttons[i][j].setIcon(new ImageIcon(imageHive));
                }
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
        textArea1.setText(t.toString());
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
            } else textArea2.setText("Not enough food to buy BusyBee");
        } catch (Exception e) {
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
            } else textArea2.setText("Not enough food to buy BusyBee");
        } catch (Exception e) {
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
            } else textArea2.setText("Not enough food to buy BusyBee");
        } catch (Exception e) {
            textArea2.setText("Cannot add TankyBee to tile");
        }
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
