import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DataSorter {
    static class Stats {
        int count = 0;
        double sum = 0;
        Double min = null, max = null;

        int minLen = Integer.MAX_VALUE;
        int maxLen = Integer.MIN_VALUE;

        void addNumber(double num) {
            count++;
            sum += num;
            min = min == null ? num : Math.min(min, num);
            max = max == null ? num : Math.max(max, num);
        }

        void addString(String s) {
            count++;
            minLen = Math.min(minLen, s.length());
            maxLen = Math.max(maxLen, s.length());
        }

        double avg() {
            return count == 0 ? 0 : sum / count;
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("нет входного файла");
            return;
        }
        List<String> inputFiles = new ArrayList<>();
        String outputDir = ".";
        String prefix = "";
        boolean append = false;
        boolean showFullStats = false;
        boolean showShortStats = false;

        for(int i = 0; i < args.length; i++){
            switch (args[i]){
                case "-o": outputDir = args[++i];
                break;
                case "-p": prefix = args[++i];
                break;
                case "-a": append = true;
                break;
                case "-f": showFullStats = true;
                break;
                case "-s": showShortStats = true;
                break;
                default: inputFiles.add(args[i]);
            }
        }

        Stats intStats = new Stats();
        Stats floatStats = new Stats();
        Stats stringStats = new Stats();

        List<String> intLines = new ArrayList<>();
        List<String> floatLines = new ArrayList<>();
        List<String> stringLines = new ArrayList<>();

        for (String fileName : inputFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String trimmed = line.trim();
                    if (trimmed.isEmpty()) continue;

                    try {
                        int value = Integer.parseInt(trimmed);
                        intLines.add(trimmed);
                        intStats.addNumber(value);
                    } catch (NumberFormatException e1) {
                        try {
                            double value = Double.parseDouble(trimmed);
                            floatLines.add(trimmed);
                            floatStats.addNumber(value);
                        } catch (NumberFormatException e2) {
                            stringLines.add(trimmed);
                            stringStats.addString(trimmed);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("ошибка при чтении файла " + fileName + ": " + e.getMessage());
            }
        }

        try {
            if (!intLines.isEmpty()) writeToFile(outputDir, prefix + "integers.txt", intLines, append);
            if (!floatLines.isEmpty()) writeToFile(outputDir, prefix + "floats.txt", floatLines, append);
            if (!stringLines.isEmpty()) writeToFile(outputDir, prefix + "strings.txt", stringLines, append);
        } catch (IOException e) {
            System.err.println("ошибка записи: " + e.getMessage());
        }

        if (showFullStats || showShortStats) {
            printStats("Integers", intStats, true, showFullStats);
            printStats("Floats", floatStats, true, showFullStats);
            printStats("Strings", stringStats, false, showFullStats);
        }
    }

    static void writeToFile(String dir, String filename, List<String> lines, boolean append) throws IOException {
        Path path = Paths.get(dir, filename);
        Files.createDirectories(path.getParent());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile(), append))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    static void printStats(String label, Stats stats, boolean isNumeric, boolean full) {
        if (stats.count == 0) return;

        if (!full) {
            System.out.println(label + ": " + stats.count);
        } else if (isNumeric) {
            System.out.printf("%s: count=%d, min=%.4f, max=%.4f, sum=%.4f, avg=%.4f%n",
                    label, stats.count, stats.min, stats.max, stats.sum, stats.avg());
        } else {
            System.out.printf("%s: count=%d, minLen=%d, maxLen=%d%n",
                    label, stats.count, stats.minLen, stats.maxLen);
        }

    }
}