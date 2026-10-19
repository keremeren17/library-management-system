import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Main class that manages books and members.
 * Data is stored in books.txt and members.txt.
 */
public class Library {

    private static final String BOOKS_FILE = "books.txt";
    private static final String MEMBERS_FILE = "members.txt";

    private ArrayList<Book> books;
    private ArrayList<Member> members;
    private int nextBookId;
    private int nextMemberId;

    public Library() {
        books = new ArrayList<>();
        members = new ArrayList<>();
        nextBookId = 1;
        nextMemberId = 1;
    }

    // ---------------- Book operations ----------------

    public Book addBook(String title, String author, int year) {
        Book book = new Book(nextBookId, title, author, year);
        books.add(book);
        nextBookId++;
        return book;
    }

    public void removeBook(int bookId) throws LibraryException {
        Book book = findBook(bookId);
        if (!book.isAvailable()) {
            throw new LibraryException("A borrowed book cannot be removed.");
        }
        books.remove(book);
    }

    public Book findBook(int bookId) throws LibraryException {
        for (Book b : books) {
            if (b.getId() == bookId) {
                return b;
            }
        }
        throw new LibraryException("No book found with ID " + bookId + ".");
    }

    public ArrayList<Book> searchBooks(String keyword) {
        ArrayList<Book> results = new ArrayList<>();
        String lower = keyword.toLowerCase();
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(lower)
                    || b.getAuthor().toLowerCase().contains(lower)) {
                results.add(b);
            }
        }
        return results;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    // ---------------- Member operations ----------------

    public Member addStudent(String name, String studentNumber) {
        Member member = new Student(nextMemberId, name, studentNumber);
        members.add(member);
        nextMemberId++;
        return member;
    }

    public Member addStaff(String name, String department) {
        Member member = new Staff(nextMemberId, name, department);
        members.add(member);
        nextMemberId++;
        return member;
    }

    public Member findMember(int memberId) throws LibraryException {
        for (Member m : members) {
            if (m.getId() == memberId) {
                return m;
            }
        }
        throw new LibraryException("No member found with ID " + memberId + ".");
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    // ---------------- Borrow / Return ----------------

    public void lendBook(int bookId, int memberId) throws LibraryException {
        Book book = findBook(bookId);
        Member member = findMember(memberId);

        if (!book.isAvailable()) {
            throw new LibraryException("This book is currently borrowed by another member.");
        }
        if (!member.canBorrowMore()) {
            throw new LibraryException(member.getName()
                    + " can borrow at most " + member.getMaxBooks() + " books.");
        }

        book.lendTo(memberId);
        member.incrementBorrowed();
    }

    public void returnBook(int bookId) throws LibraryException {
        Book book = findBook(bookId);

        if (book.isAvailable()) {
            throw new LibraryException("This book is already on the shelf.");
        }

        Member member = findMember(book.getBorrowerId());
        member.decrementBorrowed();
        book.returnBook();
    }

    public ArrayList<Book> getBooksOfMember(int memberId) {
        ArrayList<Book> results = new ArrayList<>();
        for (Book b : books) {
            if (b.getBorrowerId() == memberId) {
                results.add(b);
            }
        }
        return results;
    }

    // ---------------- File operations ----------------

    public void save() throws IOException {
        // Books: id;title;author;year;borrowerId
        BufferedWriter bookWriter = new BufferedWriter(new FileWriter(BOOKS_FILE, StandardCharsets.UTF_8));
        for (Book b : books) {
            bookWriter.write(b.getId() + ";" + b.getTitle() + ";" + b.getAuthor() + ";"
                    + b.getYear() + ";" + b.getBorrowerId());
            bookWriter.newLine();
        }
        bookWriter.close();

        // Members: type;id;name;extraInfo
        BufferedWriter memberWriter = new BufferedWriter(new FileWriter(MEMBERS_FILE, StandardCharsets.UTF_8));
        for (Member m : members) {
            String extraInfo;
            if (m instanceof Student) {
                extraInfo = ((Student) m).getStudentNumber();
            } else {
                extraInfo = ((Staff) m).getDepartment();
            }
            memberWriter.write(m.getType() + ";" + m.getId() + ";" + m.getName() + ";" + extraInfo);
            memberWriter.newLine();
        }
        memberWriter.close();
    }

    public void load() throws IOException {
        books.clear();
        members.clear();

        // Members are loaded first
        File memberFile = new File(MEMBERS_FILE);
        if (memberFile.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(memberFile, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                int id = Integer.parseInt(parts[1]);
                Member member;
                if (parts[0].equals("Student")) {
                    member = new Student(id, parts[2], parts[3]);
                } else {
                    member = new Staff(id, parts[2], parts[3]);
                }
                members.add(member);
                if (id >= nextMemberId) {
                    nextMemberId = id + 1;
                }
            }
            reader.close();
        }

        // Then books are loaded and borrow info is applied to members
        File bookFile = new File(BOOKS_FILE);
        if (bookFile.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(bookFile, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                int id = Integer.parseInt(parts[0]);
                Book book = new Book(id, parts[1], parts[2], Integer.parseInt(parts[3]));
                int borrowerId = Integer.parseInt(parts[4]);
                if (borrowerId != -1) {
                    book.lendTo(borrowerId);
                    try {
                        findMember(borrowerId).incrementBorrowed();
                    } catch (LibraryException e) {
                        // If the member no longer exists, put the book back on the shelf
                        book.returnBook();
                    }
                }
                books.add(book);
                if (id >= nextBookId) {
                    nextBookId = id + 1;
                }
            }
            reader.close();
        }
    }
}
