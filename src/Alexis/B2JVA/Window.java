package Alexis.B2JVA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * @User: CHEVALIER Alexis <Alexis.Chevalier@supinfo.com>
 * @Date: 09/02/13
 */

public class Window extends JFrame {

    //Déclaration des éléments de la GUI et des valeurs utilisées
    private final JPanel content = new JPanel();
    private CardLayout cl = new CardLayout();
    private JPanel NavigatorPanel = new JPanel();
    private JLabel NavigatorLabel = new JLabel("", SwingConstants.CENTER);
    //Text inputs
    private JTextField InputLabyHeight = new JTextField();
    private JTextField InputLabyNbr = new JTextField();
    private JTextField InputLabyWidth = new JTextField();
    //File choosers
    private JFileChooser openFile = new JFileChooser();
    private JFileChooser selectFolder = new JFileChooser();
    //LabyrinthArray
    private ArrayList<Labyrinth> labyList;
    private int actualElement;

    //Constructeur
    public Window() {
        //Propriétés du JFrame
        this.setTitle("aMAZEing");
        this.setSize(650, 600);
        this.setMinimumSize(new Dimension(650, 600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(true);

        //Rélgages des INPUTS
        InputLabyHeight.setPreferredSize(new Dimension(50, 25));
        InputLabyNbr.setPreferredSize(new Dimension(50, 25));
        InputLabyWidth.setPreferredSize(new Dimension(50, 25));

        selectFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);


        /* CREATION DES LABELS */
        JLabel loaderLabel = new JLabel("Charger un fichier > ", SwingConstants.LEFT);
        loaderLabel.setText("Charger un fichier : ");
        JLabel creatorLabel = new JLabel("Créer des labyrinthes >", SwingConstants.LEFT);
        creatorLabel.setText("Créer des labyrinthes :");
        /*  */

        //Définition de l'action du bouton LOAD
        JButton loadLaby = new JButton("Charger !");
        loadLaby.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    int returnVal = openFile.showOpenDialog(Window.this);

                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = openFile.getSelectedFile();
                        labyList = LoadFile(file);
                        if (labyList.size() == 1) {
                            NavigatorPanel.setVisible(false);
                        } else {
                            NavigatorPanel.setVisible(true);
                        }
                        int i;

                        content.removeAll();
                        for (i = 0; i < labyList.size(); i++) {
                            content.add(newLabyPanel(labyList.get(i)));
                        }
                        cl.next(content);
                        actualElement = 1;
                        NavigatorLabel.setText("Labyrinthe " + actualElement);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(getParent(), e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //Définition de l'action du bouton CREATE
        //Dispose de beaucoup de tests pour la sécurité des entrées utilisateur
        JButton createLaby = new JButton("Créer !");
        createLaby.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    if (!InputLabyNbr.getText().isEmpty() &&
                            !InputLabyWidth.getText().isEmpty() &&
                            !InputLabyHeight.getText().isEmpty()
                            ) {
                        if (Pattern.matches("^\\d*$", InputLabyNbr.getText()) &&
                                Pattern.matches("^\\d*$", InputLabyWidth.getText()) &&
                                Pattern.matches("^\\d*$", InputLabyHeight.getText())
                                ) {

                            if (Integer.parseInt(InputLabyNbr.getText()) > 0 &&
                                    Integer.parseInt(InputLabyWidth.getText()) > 0 &&
                                    Integer.parseInt(InputLabyHeight.getText()) > 0
                                    ) {
                                if (Integer.parseInt(InputLabyWidth.getText()) < 2 ||
                                        Integer.parseInt(InputLabyHeight.getText()) < 2
                                        ) {
                                    JOptionPane.showMessageDialog(getParent(), "La taille du labyrinthe doit être supérieure à 1 !", "Erreur", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                                int returnVal = selectFolder.showOpenDialog(Window.this);

                                if (returnVal == JFileChooser.APPROVE_OPTION) {
                                    if (Integer.parseInt(InputLabyNbr.getText()) == 1) {
                                        LabyToSingleFile fileOutPut = new LabyToSingleFile();
                                        String[] args = new String[4];
                                        args[0] = null;
                                        args[1] = null;
                                        args[2] = InputLabyHeight.getText();
                                        args[3] = InputLabyWidth.getText();

                                        String file = selectFolder.getSelectedFile().toString() + "/labyrinth.laby";

                                        fileOutPut.toFile(args, file);
                                        JOptionPane.showMessageDialog(getParent(), "Le labyrinthe a été crée !", "Information", JOptionPane.INFORMATION_MESSAGE);
                                    } else {
                                        Zipper Compressor = new Zipper();
                                        String[] args = new String[5];
                                        args[0] = null;
                                        args[1] = null;
                                        args[2] = InputLabyNbr.getText();
                                        args[3] = InputLabyHeight.getText();
                                        args[4] = InputLabyWidth.getText();

                                        String file = selectFolder.getSelectedFile().toString() + "/labyrinths.zip";
                                        Compressor.launch(args, file);
                                        JOptionPane.showMessageDialog(getParent(), "L'archive a été crée !", "Information", JOptionPane.INFORMATION_MESSAGE);
                                    }
                                }
                            } else {
                                JOptionPane.showMessageDialog(getParent(), "Aucune des valeur ne peut être nulle !", "Erreur", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(getParent(), "Les valeurs doivent être numériques", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(getParent(), "Vous devez saisir toutes les valeurs !", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(getParent(), e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //Définition de l'action du bouton NEXT
        JButton nextLaby = new JButton("Labyrinthe suivant");
        nextLaby.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                cl.next(content);
                if (actualElement < labyList.size()) {
                    actualElement++;
                } else {
                    actualElement = 1;
                }
                NavigatorLabel.setText("Labyrinthe " + actualElement);
            }
        });


        //Définition de l'action du bouton PREV
        JButton prevLaby = new JButton("Labyrinthe précédent");
        prevLaby.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                cl.previous(content);
                if (actualElement > 1) {
                    actualElement--;
                } else {
                    actualElement = labyList.size();
                }
                NavigatorLabel.setText("Labyrinthe " + actualElement);
            }
        });

        /* PANEL DE CHARGEMENT */
        JPanel loaderPanel = new JPanel();
        loaderPanel.add(loadLaby, SwingConstants.CENTER);
        loaderPanel.add(loaderLabel, SwingConstants.CENTER);

        /*  PANEL DE NAVIGATION  */
        NavigatorPanel.add(nextLaby, SwingConstants.CENTER);
        NavigatorPanel.add(NavigatorLabel, SwingConstants.CENTER);
        NavigatorPanel.add(prevLaby, SwingConstants.CENTER);

        /* PANEL DE CREATION */
        JPanel creatorPanel = new JPanel();
        creatorPanel.add(createLaby, SwingConstants.CENTER);
        creatorPanel.add(InputLabyNbr, SwingConstants.CENTER);
        JLabel labyNbr = new JLabel("Nombre : ", SwingConstants.CENTER);
        creatorPanel.add(labyNbr, SwingConstants.CENTER);
        creatorPanel.add(InputLabyHeight, SwingConstants.CENTER);
        JLabel labyWidth = new JLabel("Largeur :", SwingConstants.CENTER);
        creatorPanel.add(labyWidth, SwingConstants.CENTER);
        creatorPanel.add(InputLabyWidth, SwingConstants.CENTER);
        JLabel labyHeight = new JLabel("Hauteur : ", SwingConstants.CENTER);
        creatorPanel.add(labyHeight, SwingConstants.CENTER);
        creatorPanel.add(creatorLabel, SwingConstants.CENTER);

        //On définit le layout
        content.setLayout(cl);
        //On ajoute les cartes à la pile avec un nom pour les retrouver

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(creatorPanel, BorderLayout.NORTH);
        northPanel.add(loaderPanel, BorderLayout.CENTER);
        northPanel.add(NavigatorPanel, BorderLayout.SOUTH);
        NavigatorPanel.setVisible(false);
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(content, BorderLayout.CENTER);

        this.getContentPane().add(northPanel, BorderLayout.NORTH);
        this.getContentPane().add(southPanel, BorderLayout.CENTER);

        this.setVisible(true);
    }

    //Retourne une jscrollpane avec le labyrinthe dessiné
    JScrollPane newLabyPanel(final Labyrinth laby) {
        //final Labyrinth laby = new Labyrinth(20,20);
        JPanel panel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;


                //Draw solution
                if (!laby.isSolved()) {
                    laby.resolve();
                }

                Stack<Coordinates> tempStack = (Stack<Coordinates>) laby.getSolvedPath().clone();
                while (tempStack.size() != 0) {
                    Coordinates temp = tempStack.pop();
                    g2.setColor(Color.orange);
                    g2.fillOval((temp.getX() * 20) + 5, (temp.getY() * 20) + 5, 10, 10);
                }
                tempStack.empty();

                //Start Point
                g2.setColor(Color.green);
                g2.fillOval(laby.getStartX() * 20, laby.getStartY() * 20, 20, 20);
                //End Point
                g2.setColor(Color.red);
                g2.fillOval(laby.getEndX() * 20, laby.getEndY() * 20, 20, 20);

                //Drawing Labyrinth
                g2.setColor(Color.black);
                for (int a = 0; a < laby.getCaseArray().length; a++) {
                    for (int b = 0; b < laby.getCaseArray()[0].length; b++) {
                        if (laby.getCaseArray()[a][b].getTopWall())
                            g2.drawLine((a) * 20, (b) * 20, (a + 1) * 20, (b) * 20); //Top
                        if (laby.getCaseArray()[a][b].getLeftWall())
                            g2.drawLine((a) * 20, (b) * 20, (a) * 20, (b + 1) * 20); //Left
                        if (laby.getCaseArray()[a][b].getBottomWall())
                            g2.drawLine((a + 1) * 20, (b + 1) * 20, (a) * 20, (b + 1) * 20); //Bottom
                        if (laby.getCaseArray()[a][b].getRightWall())
                            g2.drawLine((a + 1) * 20, (b + 1) * 20, (a + 1) * 20, (b) * 20); //Right
                    }
                }
            }
        };
        panel.setPreferredSize(new Dimension((laby.getCaseArray().length * 20) + 1, (laby.getCaseArray()[0].length * 20) + 1));
        return new JScrollPane(panel);
    }

    //Charge les labyrinthes depuis un fichier
    ArrayList<Labyrinth> LoadFile(File file) throws Exception {
        try {
            ArrayList<Labyrinth> labyList = new ArrayList<Labyrinth>();
            String extension = "";
            int i = file.getName().lastIndexOf('.');
            if (i > 0) {
                extension = file.getName().substring(i + 1);
            }
            if (extension.equals("zip")) {
                UnZipper DeZipper = new UnZipper();
                Stack<Labyrinth> labyStack = DeZipper.launchGUI(file);
                int a = 1;
                while (labyStack.size() != 0) {
                    labyList.add(labyStack.pop());
                }
                return labyList;
            } else if (extension.equals("laby")) {
                LabyToSingleFile labyGenerator = new LabyToSingleFile();
                Labyrinth laby = labyGenerator.toLaby(file.getAbsolutePath());
                labyList.add(laby);
                return labyList;
            } else {
                throw new Exception("Vous ne pouvez utiliser que des fichiers .zip ou .laby");
            }
        } catch (Exception e) {
            throw e;
        }
    }
}