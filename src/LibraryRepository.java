import java.io.IOException;

/** Persistence boundary: the console and catalogue do not depend on a file format. */
public interface LibraryRepository {
    Library load() throws IOException;
    void save(Library library) throws IOException;
}
