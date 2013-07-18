import Alexis.B2JVA.LabyToSingleFile;
import Alexis.B2JVA.Labyrinth;
import Alexis.B2JVA.UnZipper;
import Alexis.B2JVA.Zipper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

/**
 * @User: CHEVALIER Alexis <Alexis.Chevalier@supinfo.com>
 * @Date: 09/02/13
 */

public class LabyrinthCLI {

    //Tests sur les arguments de la ligne de commande
    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                if (args.length >= 1) {
                    if (args[0].equals("generate")) { //ON VEUT GENERER DES LABYRINTHES
                        if (Integer.parseInt(args[2]) < 2 || Integer.parseInt(args[3]) < 2) {
                            System.out.println("La taille du labyrinthe doit être supérieure à 1 !");
                            return;
                        }
                        String extension = "";
                        if (args[1].equals("single")) {     //UN SEUL
                            int i = args[4].lastIndexOf('.');
                            if (i > 0) {
                                extension = args[4].substring(i + 1);
                            }
                            if (extension.equals("laby")) {
                                LabyToSingleFile fileGenerator = new LabyToSingleFile();
                                fileGenerator.toFile(args, args[4]);
                            } else {
                                System.out.println("Please use only .laby files as output file");
                            }

                        } else if (args[1].equals("multiple")) {   //PLUSIEURS
                            int i = args[5].lastIndexOf('.');
                            if (i > 0) {
                                extension = args[5].substring(i + 1);
                            }
                            if (extension.equals("zip")) {
                                Zipper Compressor = new Zipper();
                                Compressor.launch(args, args[5]);
                            } else {
                                System.out.println("Please use only .zip files as output file");
                            }
                        } else {
                            System.out.println("Bad command. Type --help for find your way in this complicated software !");
                        }

                    } else if (args[0].equals("solve")) {       //ON VEUT RESOUDRE DES LABYRINTHES
                        String extension = "";
                        int i = args[1].lastIndexOf('.');
                        if (i > 0) {
                            extension = args[1].substring(i + 1);
                        }
                        if (extension.equals("zip")) {          //ON NOUS DONNE PLUSIEURS LABYS
                            UnZipper DeZipper = new UnZipper();
                            Stack<Labyrinth> labyStack = DeZipper.launch(args[1]);
                            int a = 1;
                            while (labyStack.size() != 0) {
                                System.out.println("Labyrinthe no " + a);
                                Labyrinth templaby = labyStack.pop();
                                templaby.show();

                                System.out.println();
                                if (labyStack.size() != 0) {
                                    System.out.println("Press enter to solve the next labyrinth.");
                                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                                    try {
                                        String s = br.readLine();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                a++;
                            }
                        } else if (extension.equals("laby")) {       /// ON NOUS DONNE UN SEUL LABY
                            System.out.println("Labyrinth : ");
                            LabyToSingleFile labyGenerator = new LabyToSingleFile();
                            Labyrinth laby = labyGenerator.toLaby(args[1]);
                            laby.show();
                        } else {
                            System.out.println("Please use .zip or .laby files");
                        }
                    } else {
                        System.out.println("Bad command. Type --help for find your way in this complicated software !");
                    }

                } else if (args[0].equals("--help") || args[0].equals("-h")) {
                    help();
                } else {
                    System.out.println("Unknown command");
                }
            } else {
                help();
            }
        } catch (Exception e) {
            System.out.println("Unknown Error !");
        }
    }

    private static void help() {
        System.out.println("Usage : java -jar aMAZEing-CLI.jar [Tool] [Output/Input File or Archive]");
        System.out.println("Create one or several labyrinths");
        System.out.println("");
        System.out.println("Tools can be : ");
        System.out.println("            generate [Options] [Labyrinth Height] [Labyrinth Width] Generate a/several labyrinth(s)");
        System.out.println("            solve               Solve a/several labyrinth(s)");

        System.out.println("For the generate tool, Options can be : ");
        System.out.println("            single              Generate/Solve a single labyrinth");
        System.out.println("            multiple x          Create an archive with x labyrinths");
    }
}
