package Alexis.B2JVA;

import java.io.*;

/**
 * @User: CHEVALIER Alexis <Alexis.Chevalier@supinfo.com>
 * @Date: 09/02/13
 */
public class LabyToSingleFile {

    //Transforme un labyrinthe en fichier .laby
    public void toFile(String[] args, String outfile) {
        try (
                FileOutputStream fOutStream = new FileOutputStream(new File(outfile), false);
        ) {
            Labyrinth laby = new Labyrinth(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
            ObjectOutputStream oOutStream = new ObjectOutputStream(fOutStream);
            oOutStream.writeObject(laby);
            oOutStream.flush();
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file path !");
        } catch (Exception e) {
            System.out.println("Unknown error !");
        }
    }

    //Transforme un fichier .laby en labyrinthe
    public Labyrinth toLaby(String file) {
        try (
                FileInputStream fInputStream = new FileInputStream(new File(file));
        ) {
            ObjectInputStream oisCompressed = new ObjectInputStream(fInputStream);
            Labyrinth laby = (Labyrinth) oisCompressed.readObject();
            laby.resolve();
            return laby;
        } catch (FileNotFoundException e) {
            System.out.println("File not found !");
            return null;
        } catch (StreamCorruptedException e) {
            System.out.println("Corrupted file !");
            return null;
        } catch (Exception e) {
            System.out.println("Unknown error !");
            return null;
        }
    }
}
