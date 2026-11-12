import java.util.List;

public final class LibraryTest {
    private static int checks;

    public static void main(String[] args) {
        Library library = new Library();
        LibraryItem book = new Book("B1", "Kevade", "Oskar Luts");
        LibraryItem magazine = new Magazine("M1", "Eesti Loodus", 10);
        library.add(book);
        library.add(magazine);
        check(book.loanDays() == 21 && magazine.loanDays() == 7, "Polymorphic periods");
        check(library.findById(" B1 ") == book, "ID lookup");
        check(library.search(" KEVA ", false).equals(List.of(book)), "Case-insensitive search");
        check(library.search("", false).equals(List.of(magazine, book)), "Sorted results");
        check(library.search("missing", false).isEmpty(), "No match");
        library.borrow("B1");
        check(book.isBorrowed(), "Borrow state");
        check(library.search("", true).equals(List.of(magazine)), "Availability filter");
        expect(IllegalStateException.class, () -> library.borrow("B1"));
        library.returnItem("B1");
        check(!book.isBorrowed(), "Return state");
        expect(IllegalStateException.class, () -> library.returnItem("B1"));
        expect(IllegalArgumentException.class, () -> library.borrow("unknown"));
        expect(IllegalArgumentException.class, () -> library.add(new Book("B1", "Other", "Author")));
        check(library.findById("B1") == book, "Duplicate leaves original intact");
        expect(IllegalArgumentException.class, () -> new Book(" ", "Title", "Author"));
        expect(IllegalArgumentException.class, () -> new Book("X", null, "Author"));
        expect(IllegalArgumentException.class, () -> new Book("X", "Title", " "));
        expect(IllegalArgumentException.class, () -> new Magazine("X", "Title", 0));
        expect(IllegalArgumentException.class, () -> library.add(null));
        library.search("", false).clear();
        check(library.search("", false).size() == 2, "Result list cannot clear catalog");
        System.out.println("PASS: " + checks + " checks");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
        checks++;
    }

    private static void expect(Class<? extends RuntimeException> type, Runnable action) {
        try { action.run(); }
        catch (RuntimeException error) {
            if (!type.isInstance(error)) throw error;
            checks++;
            return;
        }
        throw new AssertionError("Expected " + type.getSimpleName());
    }
}
