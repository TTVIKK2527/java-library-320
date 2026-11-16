import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public final class FileLibraryRepository implements LibraryRepository {
    private static final String HEADER = "LIBRARY\t1";
    private final Path path;

    public FileLibraryRepository(Path path) { this.path = path.toAbsolutePath(); }

    @Override public Library load() throws IOException {
        Library library = new Library();
        if (!Files.exists(path)) return library;
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        if (lines.isEmpty() || !HEADER.equals(lines.getFirst())) throw new IOException("Vigane faili päis.");
        for (int index = 1; index < lines.size(); index++) {
            try {
                String[] fields = lines.get(index).split("\t", -1);
                if (fields.length != 5) throw new IllegalArgumentException("Vigane väljade arv.");
                String id = decode(fields[1]);
                String title = decode(fields[2]);
                String extra = decode(fields[3]);
                LibraryItem item = switch (fields[0]) {
                    case "B" -> new Book(id, title, extra);
                    case "M" -> new Magazine(id, title, Integer.parseInt(extra));
                    default -> throw new IllegalArgumentException("Tundmatu teose liik.");
                };
                if (!fields[4].equals("true") && !fields[4].equals("false")) throw new IllegalArgumentException("Vigane laenutusolek.");
                if (Boolean.parseBoolean(fields[4])) item.borrow();
                library.add(item);
            } catch (IllegalArgumentException error) {
                throw new IOException("Vigased andmed real " + (index + 1) + ". Faili ei kirjutata üle.", error);
            }
        }
        return library;
    }

    @Override public void save(Library library) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (LibraryItem item : library.search("", false)) {
            String type;
            String extra;
            if (item instanceof Book book) { type = "B"; extra = book.getAuthor(); }
            else if (item instanceof Magazine magazine) { type = "M"; extra = Integer.toString(magazine.getIssue()); }
            else throw new IOException("Salvestamiseks tundmatu teose liik.");
            lines.add(type + "\t" + encode(item.getId()) + "\t" + encode(item.getTitle()) + "\t" + encode(extra) + "\t" + item.isBorrowed());
        }
        Files.createDirectories(path.getParent());
        Path temporary = Files.createTempFile(path.getParent(), ".library-", ".tmp");
        try {
            Files.write(temporary, lines, StandardCharsets.UTF_8);
            try { Files.move(temporary, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING); }
            catch (AtomicMoveNotSupportedException unsupported) { Files.move(temporary, path, StandardCopyOption.REPLACE_EXISTING); }
        } finally { Files.deleteIfExists(temporary); }
    }

    private static String encode(String value) { return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8)); }
    private static String decode(String value) {
        byte[] bytes = Base64.getDecoder().decode(value);
        String result = new String(bytes, StandardCharsets.UTF_8);
        if (!java.util.Arrays.equals(result.getBytes(StandardCharsets.UTF_8), bytes)) throw new IllegalArgumentException("Vigane UTF-8.");
        return result;
    }
}
