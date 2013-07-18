package Alexis.B2JVA;

import java.io.Serializable;
import java.util.Random;
import java.util.Stack;

/**
 * @User: CHEVALIER Alexis <Alexis.Chevalier@supinfo.com>
 * @Date: 09/02/13
 */

public class Labyrinth implements Serializable {

    private Case caseArray[][]; //Tableau de cases du laby
    private Integer cells, cellsVisited = 0; //Nombre de cases et nombre de cases visitées
    private int startX, startY; //Coordonnées de départ
    private Integer endX = null, endY = null; //Coordonnées d'arrivée
    private Stack<Coordinates> solvedPath; //Chemin de résolution le plus court
    private boolean isSolved = false; //True si le laby a déja été résolu

    //Constructeur
    public Labyrinth(int height, int width) {
        try {
            caseArray = new Case[height][width];
            for (int a = 0; a < height; a++) {
                for (int b = 0; b < width; b++) {
                    caseArray[a][b] = new Case(a, b);
                }
            }
            cells = height * width;
            this.generate();
        } catch (Exception e) {
            System.out.println("Unknown Error !");
        }

    }

    //Génération du labyrinthe via l'algorithme Depth First Search
    void generate() {
        try {
            //Randomisation du point de départ
            Random randomGenerator = new Random();

            int firstRand = randomGenerator.nextInt(4);
            if (firstRand == 0) { //Point de départ en haut
                this.startY = 0;
                this.startX = randomGenerator.nextInt(caseArray.length);
                caseArray[startX][startY].breakTopWall();
            }
            if (firstRand == 1) { //Point de départ a droite
                this.startY = randomGenerator.nextInt(caseArray[0].length);
                this.startX = caseArray.length - 1;
                caseArray[startX][startY].breakRightWall();
            }
            if (firstRand == 2) { //Point de départ en bas
                this.startY = caseArray[0].length - 1;
                this.startX = randomGenerator.nextInt(caseArray.length);
                caseArray[startX][startY].breakBottomWall();
            }
            if (firstRand == 3) { //Point de départ a gauche
                this.startY = randomGenerator.nextInt(caseArray[0].length);
                this.startX = 0;
                caseArray[startX][startY].breakLeftWall();
            }


            caseArray[startX][startY].setState(true);
            Coordinates start = new Coordinates(startX, startY);
            Coordinates actual = start;
            //Chemin en cours
            Stack<Coordinates> cellStack = new Stack<Coordinates>();
            int longerPathSize = 0;

            //Labyrinthe en creation
            while (cellsVisited < cells) {
                if (this.getNeighboursFree(actual) != null) {    //Si on a des voisins utilisables, on continue
                    Coordinates[] neighbours = this.getNeighboursFree(actual);
                    int nbr = neighbours.length;
                    int rand = randomGenerator.nextInt(nbr);
                    Coordinates target = new Coordinates(neighbours[rand].getX(), neighbours[rand].getY());

                    caseArray[neighbours[rand].getX()][neighbours[rand].getY()].setState(true);
                    this.breakWall(actual, target);
                    cellStack.push(actual);

                    actual = new Coordinates(target.getX(), target.getY());
                    cellsVisited++;
                } else {       //Si on a pas de voisins utilisables, on revient en arriére dans la stack
                    if (cellStack.size() > 0) {
                        if (cellStack.size() > longerPathSize) {
                            longerPathSize = cellStack.size();
                            this.endX = actual.getX();
                            this.endY = actual.getY();
                        }

                        Coordinates temp = cellStack.pop();

                        actual = new Coordinates(temp.getX(), temp.getY());
                    } else {
                        break;
                    }

                }

            }
        } catch (Exception e) {
            System.out.println("Unknown Error !");
        }
    }

    //Permet de casser le mur entre la case actual et la case target
    void breakWall(Coordinates actual, Coordinates target) {
        try {
            //Porte du haut
            if ((target.getX() == actual.getX()) && (target.getY() <= actual.getY())) {
                caseArray[actual.getX()][actual.getY()].breakTopWall();
                caseArray[target.getX()][target.getY()].breakBottomWall();
            }

            //Porte de droite
            if ((target.getX() >= actual.getX()) && (target.getY() == actual.getY())) {
                caseArray[actual.getX()][actual.getY()].breakRightWall();
                caseArray[target.getX()][target.getY()].breakLeftWall();
            }

            //Porte du bas
            if ((target.getX() == actual.getX()) && (target.getY() >= actual.getY())) {
                caseArray[actual.getX()][actual.getY()].breakBottomWall();
                caseArray[target.getX()][target.getY()].breakTopWall();
            }

            //Porte de gauche
            if ((target.getX() <= actual.getX()) && (target.getY() == actual.getY())) {
                caseArray[actual.getX()][actual.getY()].breakLeftWall();
                caseArray[target.getX()][target.getY()].breakRightWall();
            }
        } catch (Exception e) {
            System.out.println("Unknown Error !");
        }
    }

    //Récupére les voisins n'ayant pas été utilisés (Pour la génération)
    Coordinates[] getNeighboursFree(Coordinates coord_check) {
        try {
            int x = coord_check.getX();
            int y = coord_check.getY();
            Coordinates coord[] = new Coordinates[4];
            int a = 0;
            if (((x >= 0) && (x < caseArray.length)) && (((y - 1) >= 0) && ((y - 1) < caseArray[0].length))) {
                if (caseArray[x][y - 1].has4Walls() && !caseArray[x][y - 1].getState()) { //Haut
                    coord[a] = new Coordinates(x, y - 1);
                    a++;
                }
            }
            if ((((x + 1) >= 0) && ((x + 1) < caseArray.length)) && ((y >= 0) && (y < caseArray[0].length))) {
                if (caseArray[x + 1][y].has4Walls() && !caseArray[x + 1][y].getState()) { //Droit
                    coord[a] = new Coordinates(x + 1, y);
                    a++;
                }
            }
            if (((x >= 0) && (x < caseArray.length)) && (((y + 1) > 0) && ((y + 1) < caseArray[0].length))) {
                if (caseArray[x][y + 1].has4Walls() && !caseArray[x][y + 1].getState()) { //Bas
                    coord[a] = new Coordinates(x, y + 1);
                    a++;
                }
            }
            if ((((x - 1) >= 0) && ((x - 1) < caseArray.length)) && ((y >= 0) && (y < caseArray[0].length))) {
                if (caseArray[x - 1][y].has4Walls() && !caseArray[x - 1][y].getState()) { //Gauche
                    coord[a] = new Coordinates(x - 1, y);
                    a++;
                }
            }

            Coordinates[] returnCoord;

            if (a != 0) {
                returnCoord = new Coordinates[a];
                for (int b = 0; b < a; b++) {
                    returnCoord[b] = new Coordinates(coord[b].getX(), coord[b].getY());
                }
            } else {
                returnCoord = null;
            }

            return returnCoord;
        } catch (Exception e) {
            System.out.println("Unknown Error !");
            return null;
        }
    }

    //Récupére les voisins n'ayant pas été visités (Pour la résolution)
    Coordinates[] getNeighboursOpen(Coordinates coord_check) {
        try {
            int x = coord_check.getX();
            int y = coord_check.getY();
            Coordinates coord[] = new Coordinates[4];
            int a = 0;

            if (((x >= 0) && (x < caseArray.length)) && (((y - 1) >= 0) && ((y - 1) < caseArray[0].length))) {
                if (!caseArray[x][y - 1].getBottomWall() && !caseArray[x][y - 1].getSolverVisited()) { //Haut
                    coord[a] = new Coordinates(x, y - 1);
                    a++;
                }
            }
            if ((((x + 1) >= 0) && ((x + 1) < caseArray.length)) && ((y >= 0) && (y < caseArray[0].length))) {
                if (!caseArray[x + 1][y].getLeftWall() && !caseArray[x + 1][y].getSolverVisited()) { //Droit
                    coord[a] = new Coordinates(x + 1, y);
                    a++;
                }
            }
            if (((x >= 0) && (x < caseArray.length)) && (((y + 1) > 0) && ((y + 1) < caseArray[0].length))) {
                if (!caseArray[x][y + 1].getTopWall() && !caseArray[x][y + 1].getSolverVisited()) { //Bas
                    coord[a] = new Coordinates(x, y + 1);
                    a++;
                }
            }
            if ((((x - 1) >= 0) && ((x - 1) < caseArray.length)) && ((y >= 0) && (y < caseArray[0].length))) {
                if (!caseArray[x - 1][y].getRightWall() && !caseArray[x - 1][y].getSolverVisited()) { //Gauche
                    coord[a] = new Coordinates(x - 1, y);
                    a++;
                }
            }

            Coordinates[] returnCoord;

            if (a != 0) {
                returnCoord = new Coordinates[a];
                for (int b = 0; b < a; b++) {
                    returnCoord[b] = new Coordinates(coord[b].getX(), coord[b].getY());
                }
            } else {
                returnCoord = null;
            }

            return returnCoord;
        } catch (Exception e) {
            System.out.println("Unknown Error !");
            return null;
        }
    }

    //Résouds le labyrinthe
    //Même principe que pour le générateur, a ceci prés qu'on recherche le chemin le plus court.
    public void resolve() {
        try {
            Stack<Coordinates> actualStack = new Stack<Coordinates>();
            Stack<Coordinates> shortestStack = new Stack<Coordinates>();

            Random randomGenerator = new Random();
            Coordinates actual = new Coordinates(startX, startY);
            cellsVisited = 0;
            while (cellsVisited < cells) {
                if (this.getNeighboursOpen(actual) != null) {
                    Coordinates[] neighbours = this.getNeighboursOpen(actual);
                    int nbr = neighbours.length;
                    int rand = randomGenerator.nextInt(nbr);
                    Coordinates target = new Coordinates(neighbours[rand].getX(), neighbours[rand].getY());
                    caseArray[neighbours[rand].getX()][neighbours[rand].getY()].setSolverVisited(true);

                    actualStack.push(actual);
                    actual = new Coordinates(target.getX(), target.getY());

                    cellsVisited++;
                } else {
                    if (actual.getX() == this.getEndX() && actual.getY() == this.getEndY()) {
                        shortestStack = (Stack<Coordinates>) actualStack.clone();
                        Coordinates temp = actualStack.pop();
                        actual = new Coordinates(temp.getX(), temp.getY());
                    } else {
                        Coordinates temp = actualStack.pop();
                        actual = new Coordinates(temp.getX(), temp.getY());
                    }
                }
            }
            solvedPath = (Stack<Coordinates>) shortestStack.clone();
            int i = shortestStack.size();
            for (int j = 0; j < i; j++) {

                Coordinates temp = shortestStack.pop();
                caseArray[temp.getX()][temp.getY()].setFinalPath(true);
            }
            this.isSolved = true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unknown Error !");
        }
    }

    //Affiche le labyrinthe (CLI uniquement)
    public void show() {
        try {
        /* Premiére ligne haut */
            System.out.print('#');
            for (int b = 0; b < caseArray.length * 2; b++) {
                if (b % 2 == 0) {
                    if (caseArray[b / 2][0].getTopWall()) {
                        System.out.print('#');
                    } else {
                        System.out.print(' ');
                    }
                } else {
                    System.out.print('#');
                }
            }
            System.out.println();

            /* Reste du labyrinthe */
            for (int a = 0; a < (caseArray[0].length * 2); a++) {
                if (a % 2 == 0) { //Pair
                    if (caseArray[0][a / 2].getLeftWall()) {
                        System.out.print('#');
                    } else {
                        System.out.print(' ');
                    }
                    for (int b = 0; b < (caseArray.length * 2); b++) {
                        if (b % 2 == 0) {
                            if ((b / 2) == this.getEndX() && (a / 2) == this.getEndY()) {
                                System.out.print('E');
                            } else if ((b / 2) == this.getStartX() && (a / 2) == this.getStartY()) {
                                System.out.print('S');
                            } else {
                                if (caseArray[b / 2][a / 2].getFinalPath()) {
                                    System.out.print('.');
                                } else {
                                    System.out.print(' ');
                                }
                            }
                        } else {
                            if (caseArray[b / 2][a / 2].getRightWall()) {
                                System.out.print('#');
                            } else {
                                System.out.print(' ');
                            }
                        }
                    }
                } else { //Impair
                    System.out.print('#');
                    for (int b = 0; b < (caseArray.length * 2); b++) {
                        if (b % 2 == 0) {
                            if (caseArray[b / 2][a / 2].getBottomWall()) {
                                System.out.print('#');
                            } else {
                                System.out.print(' ');
                            }
                        } else {
                            System.out.print('#');
                        }
                    }
                }
                System.out.println();
            }
            System.out.println();
            System.out.println();
        } catch (Exception e) {
            System.out.println("Unknown Error !");
        }
    }

    //Getters/Setters

    public Case[][] getCaseArray() {
        return caseArray;
    }

    public int getStartY() {
        return startY;
    }

    public int getStartX() {
        return startX;
    }

    public Integer getEndY() {
        return endY;
    }

    public Integer getEndX() {
        return endX;
    }

    public Stack<Coordinates> getSolvedPath() {
        return solvedPath;
    }

    public boolean isSolved() {
        return isSolved;
    }
}
