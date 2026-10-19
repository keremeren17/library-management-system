/**
 * Represents a book in the library.
 */
public class Book {

    private int id;
    private String title;
    private String author;
    private int year;
    private int borrowerId; // -1 means the book is on the shelf

    public Book(int id, String title, String author, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.borrowerId = -1;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    public int getBorrowerId() {
        return borrowerId;
    }

    public boolean isAvailable() {
        return borrowerId == -1;
    }

    public void lendTo(int memberId) {
        this.borrowerId = memberId;
    }

    public void returnBook() {
        this.borrowerId = -1;
    }

    @Override
    public String toString() {
        String status = isAvailable() ? "Available" : "Borrowed (Member #" + borrowerId + ")";
        return String.format("[%d] %s - %s (%d) | %s", id, title, author, year, status);
    }
}
