public abstract class LibraryItem {
    private final String id;
    private final String title;
    private boolean borrowed;

    protected LibraryItem(String id, String title) {
        if (id == null || id.isBlank() || title == null || title.isBlank()) {
            throw new IllegalArgumentException("ID ja pealkiri peavad olema täidetud.");
        }
        this.id = id.strip();
        this.title = title.strip();
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public boolean isBorrowed() { return borrowed; }
    public abstract int loanDays();
    public abstract String details();

    public void borrow() {
        if (borrowed) throw new IllegalStateException("Teos on juba laenutatud.");
        borrowed = true;
    }

    public void returnItem() {
        if (!borrowed) throw new IllegalStateException("Teos ei ole laenutatud.");
        borrowed = false;
    }

    @Override
    public String toString() {
        return id + " | " + title + " | " + details() + " | " + loanDays()
            + " päeva | " + (borrowed ? "laenutatud" : "saadaval");
    }
}
