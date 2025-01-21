package pl.edu.agh.to2.cleaner.service;

import org.springframework.stereotype.Service;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ActionLogService {
    private final String logFilePath = "./logs/actions.log";

    public ActionLogService() {
    }

    public List<String> lastNLogs(int n) {
        List<String> lines = new ArrayList<>();

        try (RandomAccessFile file = new RandomAccessFile(logFilePath, "r")) {
            long fileLength = file.length();
            long pointer = fileLength - 1;
            StringBuilder line = new StringBuilder();
            int linesRead = 0;

            while (pointer >= 0 && linesRead < n) {
                file.seek(pointer);
                char c = (char) file.readByte();

                if (c == '\n') {
                    if (!line.isEmpty()) {
                        lines.add(line.reverse().toString());
                        line.setLength(0);
                        linesRead++;
                    }
                } else {
                    line.append(c);
                }
                pointer--;
            }

            // Add the first line if the pointer reaches the start of the file
            if (pointer < 0 && !line.isEmpty()) {
                lines.add(line.reverse().toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Reverse the list to maintain correct order
        Collections.reverse(lines);
        return lines;
    }
}
