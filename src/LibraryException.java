/**
 * Custom exception for errors in library operations.
 * Examples: book not found, book already borrowed, member limit reached.
 */
public class LibraryException extends Exception {

    public LibraryException(String message) {
        super(message);
    }
}
