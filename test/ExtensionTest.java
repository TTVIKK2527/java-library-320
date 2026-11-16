public final class ExtensionTest {
    public static void main(String[] args) {
        Library library = new Library();
        library.add(new Book("B1", "Kevade", "Oskar Luts"));
        library.add(new Book("B2", "Rehepapp", "Andrus Kivirähk"));
        library.add(new Magazine("M1", "Oskar Luts", 1));
        check(library.searchByAuthor(" LUTS ").size() == 1, "Author search ignores case and whitespace");
        check(library.searchByAuthor("missing").isEmpty(), "Missing author");
        check(library.searchByAuthor("").size() == 2, "Author filter includes only books");
        try { library.searchByAuthor(null); throw new AssertionError("Null accepted"); }
        catch (IllegalArgumentException expected) { }
        System.out.println("PASS: extension tests");
    }
    private static void check(boolean condition, String message) { if (!condition) throw new AssertionError(message); }
}
