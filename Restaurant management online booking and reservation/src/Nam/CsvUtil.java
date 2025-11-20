package Nam;

import Quan.Quan1.Customer;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CsvUtil {
    public static List<String[]> readAll(String path) throws IOException {
        List<String[]> out = new ArrayList<>();
        Path p = Paths.get(path);
        if (!Files.exists(p)) return out;
        try (BufferedReader br = Files.newBufferedReader(p)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                out.add(parseLine(line));
            }
        }
        return out;
    }

    public static void writeAll(String path, List<String[]> rows) throws IOException {
        Path p = Paths.get(path);
        Files.createDirectories(p.getParent() == null ? p : p.getParent());
        try (BufferedWriter bw = Files.newBufferedWriter(p)) {
            for (String[] r : rows) {
                bw.write(String.join(",", escape(r)));
                bw.newLine();
            }
        }
    }

    private static String[] parseLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuote = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuote && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    cur.append('"'); i++;
                } else inQuote = !inQuote;
            } else if (c == ',' && !inQuote) {
                fields.add(cur.toString());
                cur.setLength(0);
            } else cur.append(c);
        }
        fields.add(cur.toString());
        return fields.toArray(new String[0]);
    }

    private static String[] escape(String[] arr) {
        String[] out = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            String s = arr[i] == null ? "" : arr[i];
            if (s.contains(",") || s.contains("\"")) {
                s = "\"" + s.replace("\"", "\"\"") + "\"";
            }
            out[i] = s;
        }
        return out;
    }
}


