import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;

public final class Main {
    public static void main(String[] args) {
        Path dataFile = Path.of(args.length > 0 ? args[0] : "data/library.tsv");
        LibraryRepository repository = new FileLibraryRepository(dataFile);
        Library library;
        try {
            boolean firstRun = !Files.exists(dataFile);
            library = repository.load();
            if (firstRun) {
                library.add(new Book("B1", "Kevade", "Oskar Luts"));
                library.add(new Book("B2", "Rehepapp", "Andrus Kivirähk"));
                library.add(new Magazine("M1", "Eesti Loodus", 10));
                repository.save(library);
            }
        } catch (IOException error) {
            System.err.println("Andmeid ei saanud avada: " + error.getMessage());
            return;
        }
        try (Scanner input = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.println("\n1 Kõik | 2 Otsi | 3 Saadaval | 4 Laenuta | 5 Tagasta | 6 Lisa raamat | 7 Lisa ajakiri | 8 Autor | 9 Eemalda | 10 Statistika | 0 Välju");
                try {
                    switch (read(input, "Valik: ")) {
                        case "1" -> show(library.search("", false));
                        case "2" -> show(library.search(read(input, "Pealkiri: "), false));
                        case "3" -> show(library.search("", true));
                        case "4" -> {
                            String id = read(input, "ID: ");
                            library.borrow(id);
                            repository.save(library);
                            System.out.println("Laenutatud " + library.findById(id).loanDays() + " päevaks.");
                        }
                        case "5" -> {
                            library.returnItem(read(input, "ID: "));
                            repository.save(library);
                            System.out.println("Tagastatud.");
                        }
                        case "6" -> {
                            library.add(new Book(read(input, "ID: "), read(input, "Pealkiri: "), read(input, "Autor: ")));
                            repository.save(library);
                            System.out.println("Raamat lisatud.");
                        }
                        case "7" -> {
                            library.add(new Magazine(read(input, "ID: "), read(input, "Pealkiri: "),
                                Integer.parseInt(read(input, "Number: "))));
                            repository.save(library);
                            System.out.println("Ajakiri lisatud.");
                        }
                        case "8" -> show(library.searchByAuthor(read(input, "Autor: ")));
                        case "9" -> {
                            library.remove(read(input, "ID: "));
                            repository.save(library);
                            System.out.println("Eemaldatud.");
                        }
                        case "10" -> {
                            Library.Statistics stats = library.statistics();
                            System.out.println("Kokku: " + stats.total() + " | saadaval: " + stats.available()
                                + " | laenutatud: " + stats.borrowed() + " | raamatud: " + stats.books() + " | ajakirjad: " + stats.magazines());
                        }
                        case "0" -> running = false;
                        default -> System.out.println("Tundmatu menüüvalik.");
                    }
                } catch (IOException error) {
                    System.err.println("Salvestamine ebaõnnestus. Viimane muudatus ei pruugi failis olla: " + error.getMessage());
                    running = false;
                } catch (EndOfInput end) {
                    running = false;
                } catch (NumberFormatException invalid) {
                    System.out.println("Viga: number peab olema täisarv.");
                } catch (IllegalArgumentException | IllegalStateException invalid) {
                    System.out.println("Viga: " + invalid.getMessage());
                }
            }
        }
        System.out.println("Head aega! Andmefail: " + dataFile.toAbsolutePath());
    }

    private static String read(Scanner input, String prompt) {
        System.out.print(prompt);
        if (!input.hasNextLine()) throw new EndOfInput();
        return input.nextLine();
    }

    private static void show(List<LibraryItem> items) {
        if (items.isEmpty()) System.out.println("Teoseid ei leitud.");
        for (LibraryItem item : items) System.out.println(item);
    }

    private static final class EndOfInput extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }
}
