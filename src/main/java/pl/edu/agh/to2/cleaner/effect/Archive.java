package pl.edu.agh.to2.cleaner.effect;

import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Archive implements IOSideEffect {
    private final List<FileInfo> files = new ArrayList<>();
    private final String directoryPathToCreateZipInside; // CANNOT END WITH '/'

    public Archive(FileInfo file, String path) {
        files.add(file);
        this.directoryPathToCreateZipInside = path;
    }

    public Archive(List<FileInfo> files, String path) {
        this.files.addAll(files);
        this.directoryPathToCreateZipInside = path;
    }

    @Override
    public void apply() {

        try (final FileOutputStream fos = new FileOutputStream(directoryPathToCreateZipInside + "/compressed.zip")) {
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            for (FileInfo fileInfo : files) {
                File fileToZip = new File(fileInfo.toPath().toString());

                FileInputStream fis = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zipOut. putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                /*
                int read(byte[] b)
                Reads up to b.length bytes of data from this input stream
                into an array of bytes.
                 */
                // Czyli czytamy po 1024 bajty z pliku, dopoki sie da.
                while((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                fis.close();
                Files.delete(fileInfo.toPath());
            }
            zipOut.close();
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("Failed to archive the files.");
        }

    }

    @Override
    public String getLogString() {
        return null;
    }
}
