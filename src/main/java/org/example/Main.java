package org.example;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
public class Main {
        public static void main(String[] args) {
            String directory = "pdf lerin bulunduğu dizin";
            String searchText = "aranacak metin";
            try {
                List<String> results = searchInPDFs(directory, searchText);
                for (String result : results) {
                    System.out.println("Metin bulundu: " + result);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static List<String> searchInPDFs(String directory, String searchText) throws IOException {
            List<String> resultFiles = new ArrayList<>();
            try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
                paths.filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".pdf"))
                        .forEach(path -> {
                            try {
                                if (containsText(path, searchText)) {
                                    resultFiles.add(path.toString());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
            }
            return resultFiles;
        }

        public static boolean containsText(Path pdfPath, String searchText) throws IOException {
            try (PDDocument document = PDDocument.load(new File(pdfPath.toString()))) {
                PDFTextStripper pdfStripper = new PDFTextStripper();
                String text = pdfStripper.getText(document);
                return text.toLowerCase().contains(searchText.toLowerCase());
            }
        }
    }

