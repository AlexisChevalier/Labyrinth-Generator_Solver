package Alexis.B2JVA;

import java.io.*;
import java.util.Stack;
import java.util.zip.ZipInputStream;

/**
 * @User: CHEVALIER Alexis <Alexis.Chevalier@supinfo.com>
 * @Date: 09/02/13
 */

public class UnZipper {
    //Permet de dézipper et de transformer en stack de labyrinthes, utile pour la version CLI
    public Stack<Labyrinth> launch(String file) {
        Stack<Labyrinth> labyStack = new Stack<Labyrinth>();
        try (
                FileInputStream fInputStream = new FileInputStream(new File(file));
                ZipInputStream zInputStream = new ZipInputStream(fInputStream);
        ) {
            while (zInputStream.getNextEntry() != null) {
                ObjectInputStream oisCompressed = new ObjectInputStream(zInputStream);
                Labyrinth compressedLaby = (Labyrinth) oisCompressed.readObject();

                compressedLaby.resolve();
                labyStack.push(compressedLaby);
            }


        } catch (FileNotFoundException e) {
            System.out.println("File not found !");
        } catch (StreamCorruptedException e) {
            System.out.println("Corrupted file !");
        } catch (Exception e) {
            System.out.println("Unknown error !");
        }
        return labyStack;
    }

    //Permet de dézipper et de transformer en stack de labyrinthes, utile pour la version GUI (Exceptions modifiées)
    public Stack<Labyrinth> launchGUI(File file) throws Exception {
        Stack<Labyrinth> labyStack = new Stack<Labyrinth>();
        try (
                FileInputStream fInputStream = new FileInputStream(file);
                ZipInputStream zInputStream = new ZipInputStream(fInputStream);
        ) {
            while (zInputStream.getNextEntry() != null) {
                ObjectInputStream oisCompressed = new ObjectInputStream(zInputStream);
                Labyrinth compressedLaby = (Labyrinth) oisCompressed.readObject();

                compressedLaby.resolve();
                labyStack.push(compressedLaby);
            }


        } catch (InvalidClassException e) {
            throw new Exception("Le fichier est invalide");
        } catch (FileNotFoundException e) {
            throw new Exception("Le fichier est introuvable");
        } catch (StreamCorruptedException e) {
            throw new Exception("Le fichier est corrompu");
        } catch (Exception e) {
            throw new Exception("Erreur inconnue");
        }
        return labyStack;
    }
}
