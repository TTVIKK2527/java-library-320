import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class PersistenceTest {
    public static void main(String[] args) throws IOException {
        Path directory = Files.createTempDirectory("library-test-");
        Path file = directory.resolve("catalogue.tsv");
        try {
            LibraryRepository repository = new FileLibraryRepository(file);
            check(repository.load().search("", false).isEmpty(), "Missing file is empty");
            Library library = new Library();
            library.add(new Book("B1", "Õun\tja\nöö", "Mari Õis"));
            library.add(new Magazine("M1", "Loodus", 12));
            library.borrow("B1");
            repository.save(library);
            Library restored = new FileLibraryRepository(file).load();
            check(restored.search("", false).size() == 2, "All records persist across repository instances");
            check(restored.findById("B1").isBorrowed(), "Loan state persists");
            check(restored.findById("B1").getTitle().equals("Õun\tja\nöö"), "Unicode and delimiters round trip");
            check(restored.findById("M1") instanceof Magazine m && m.getIssue() == 12, "Subtype data persists");
            restored.returnItem("B1");
            repository.save(restored);
            check(!repository.load().findById("B1").isBorrowed(), "Later state replaces old snapshot");
            for (String bad : new String[]{"", "LIBRARY\t99\n", "LIBRARY\t1\nB\tx\ty\tz\ttrue\n", "LIBRARY\t1\nB\tQjE=\tVGl0bGU=\tQXV0aG9y\tmaybe\n"}) {
                Files.writeString(file, bad);
                try { repository.load(); throw new AssertionError("Corrupt file accepted"); }
                catch (IOException expected) { check(Files.readString(file).equals(bad), "Corrupt source is untouched"); }
            }
            System.out.println("PASS: persistence tests");
        } finally {
            Files.deleteIfExists(file);
            Files.deleteIfExists(directory);
        }
    }

    private static void check(boolean condition, String message) { if (!condition) throw new AssertionError(message); }
}
