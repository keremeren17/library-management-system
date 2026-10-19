import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Console-based library management program.
 */
public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static Library library = new Library();

    public static void main(String[] args) {
        try {
            library.load();
            System.out.println("Saved data loaded.");
        } catch (IOException e) {
            System.out.println("Could not load data: " + e.getMessage());
        }

        boolean running = true;
        while (running) {
            showMenu();
            int choice = readInt("Your choice: ");

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    listBooks(library.getBooks());
                    break;
                case 3:
                    searchBooks();
                    break;
                case 4:
                    removeBook();
                    break;
                case 5:
                    addMember();
                    break;
                case 6:
                    listMembers();
                    break;
                case 7:
                    lendBook();
                    break;
                case 8:
                    returnBook();
                    break;
                case 9:
                    showMemberBooks();
                    break;
                case 0:
                    save();
                    System.out.println("Exiting. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
        scanner.close();
    }

    private static void showMenu() {
        System.out.println();
        System.out.println("===== LIBRARY MANAGEMENT SYSTEM =====");
        System.out.println("1. Add book");
        System.out.println("2. List books");
        System.out.println("3. Search books");
        System.out.println("4. Remove book");
        System.out.println("5. Add member");
        System.out.println("6. List members");
        System.out.println("7. Lend book");
        System.out.println("8. Return book");
        System.out.println("9. Show a member's books");
        System.out.println("0. Save and exit");
    }

    // ---------------- Menu actions ----------------

    private static void addBook() {
        String title = readText("Title: ");
        String author = readText("Author: ");
        int year = readInt("Publication year: ");
        Book book = library.addBook(title, author, year);
        System.out.println("Added: " + book);
    }

    private static void listBooks(ArrayList<Book> list) {
        if (list.isEmpty()) {
            System.out.println("No books to show.");
            return;
        }
        for (Book b : list) {
            System.out.println(b);
        }
    }

    private static void searchBooks() {
        String keyword = readText("Search keyword (title or author): ");
        ArrayList<Book> results = library.searchBooks(keyword);
        System.out.println(results.size() + " result(s) found.");
        listBooks(results);
    }

    private static void removeBook() {
        int id = readInt("ID of the book to remove: ");
        try {
            library.removeBook(id);
            System.out.println("Book removed.");
        } catch (LibraryException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void addMember() {
        System.out.println("1. Student (max 3 books)");
        System.out.println("2. Staff (max 5 books)");
        int type = readInt("Member type: ");
        String name = readText("Full name: ");

        Member member;
        if (type == 1) {
            String studentNumber = readText("Student number: ");
            member = library.addStudent(name, studentNumber);
        } else if (type == 2) {
            String department = readText("Department: ");
            member = library.addStaff(name, department);
        } else {
            System.out.println("Invalid member type.");
            return;
        }
        System.out.println("Added: " + member);
    }

    private static void listMembers() {
        if (library.getMembers().isEmpty()) {
            System.out.println("No registered members.");
            return;
        }
        for (Member m : library.getMembers()) {
            System.out.println(m);
        }
    }

    private static void lendBook() {
        int bookId = readInt("Book ID: ");
        int memberId = readInt("Member ID: ");
        try {
            library.lendBook(bookId, memberId);
            System.out.println("Book lent successfully.");
        } catch (LibraryException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void returnBook() {
        int bookId = readInt("ID of the returned book: ");
        try {
            library.returnBook(bookId);
            System.out.println("Book returned.");
        } catch (LibraryException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void showMemberBooks() {
        int memberId = readInt("Member ID: ");
        try {
            Member member = library.findMember(memberId);
            System.out.println("Books borrowed by " + member.getName() + ":");
            listBooks(library.getBooksOfMember(memberId));
        } catch (LibraryException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void save() {
        try {
            library.save();
            System.out.println("Data saved.");
        } catch (IOException e) {
            System.out.println("Save error: " + e.getMessage());
        }
    }

    // ---------------- Helper methods ----------------

    private static String readText(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.contains(";")) {
                System.out.println("The ';' character is not allowed.");
                input = "";
            }
        } while (input.isEmpty());
        return input;
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
