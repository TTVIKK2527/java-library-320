import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public final class Library {
    private final HashMap<String, LibraryItem> items = new HashMap<>();

    public void add(LibraryItem item) {
        if (item == null) throw new IllegalArgumentException("Teos puudub.");
        if (items.putIfAbsent(item.getId(), item) != null) {
            throw new IllegalArgumentException("Selle ID-ga teos on juba olemas.");
        }
    }

    public LibraryItem findById(String id) {
        LibraryItem item = items.get(id == null ? null : id.strip());
        if (item == null) throw new IllegalArgumentException("Sellise ID-ga teost ei ole.");
        return item;
    }

    public List<LibraryItem> search(String query, boolean availableOnly) {
        if (query == null) throw new IllegalArgumentException("Otsing puudub.");
        String normalized = query.strip().toLowerCase(Locale.ROOT);
        ArrayList<LibraryItem> result = new ArrayList<>();
        for (LibraryItem item : items.values()) {
            if (item.getTitle().toLowerCase(Locale.ROOT).contains(normalized)
                    && (!availableOnly || !item.isBorrowed())) {
                result.add(item);
            }
        }
        result.sort(Comparator.comparing(LibraryItem::getTitle,
            String.CASE_INSENSITIVE_ORDER).thenComparing(LibraryItem::getId));
        return result;
    }

    public void borrow(String id) { findById(id).borrow(); }
    public void returnItem(String id) { findById(id).returnItem(); }

    public void remove(String id) {
        LibraryItem item = findById(id);
        if (item.isBorrowed()) throw new IllegalStateException("Laenutatud teost ei saa eemaldada.");
        items.remove(item.getId());
    }

    public List<LibraryItem> searchByAuthor(String query) {
        if (query == null) throw new IllegalArgumentException("Autor puudub.");
        String normalized = query.strip().toLowerCase(Locale.ROOT);
        return search("", false).stream()
            .filter(item -> item instanceof Book book && book.getAuthor().toLowerCase(Locale.ROOT).contains(normalized))
            .toList();
    }
}
