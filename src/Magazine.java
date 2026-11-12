public final class Magazine extends LibraryItem {
    private final int issue;

    public Magazine(String id, String title, int issue) {
        super(id, title);
        if (issue < 1) throw new IllegalArgumentException("Number peab olema positiivne.");
        this.issue = issue;
    }

    public int getIssue() { return issue; }
    @Override public int loanDays() { return 7; }
    @Override public String details() { return "Ajakiri nr " + issue; }
}
