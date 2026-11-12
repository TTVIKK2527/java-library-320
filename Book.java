public final class Book extends LibraryItem {
    private final String author;

    public Book(String id, String title, String author) {
        super(id, title);
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Autor peab olema täidetud.");
        }
        this.author = author.strip();
    }

    public String getAuthor() { return author; }
    @Override public int loanDays() { return 21; }
    @Override public String details() { return "Raamat: " + author; }
}
