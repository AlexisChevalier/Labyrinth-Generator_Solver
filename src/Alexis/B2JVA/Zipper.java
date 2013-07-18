package Alexis.B2JVA;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @User: CHEVALIER Alexis <Alexis.Chevalier@supinfo.com>
 * @Date: 09/02/13
 */

public class Zipper {

    public void launch(String[] args, String outArchive) {
        try (
                FileOutputStream fOutStream = new FileOutputStream(new File(outArchive), false);
                ZipOutputStream zOutStream = new ZipOutputStream(fOutStream);
        ) {
            for (int a = 1; a <= Integer.parseInt(args[2]); a++) {
                Labyrinth laby = new Labyrinth(Integer.parseInt(args[3]), Integer.parseInt(args[4]));

                zOutStream.putNextEntry(new ZipEntry("Labyrinth" + a + ".laby"));

                ObjectOutputStream oOutStream = new ObjectOutputStream(zOutStream);
                oOutStream.writeObject(laby);
                oOutStream.flush();
                zOutStream.flush();
                zOutStream.closeEntry();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file path !");
        } catch (Exception e) {
            System.out.println("Unknown error !");
        }
    }
}