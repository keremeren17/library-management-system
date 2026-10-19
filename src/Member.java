/**
 * Abstract class holding the common properties of library members.
 * Student and Staff extend this class.
 */
public abstract class Member {

    private int id;
    private String name;
    private int borrowedCount;

    public Member(int id, String name) {
        this.id = id;
        this.name = name;
        this.borrowedCount = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBorrowedCount() {
        return borrowedCount;
    }

    public void incrementBorrowed() {
        borrowedCount++;
    }

    public void decrementBorrowed() {
        if (borrowedCount > 0) {
            borrowedCount--;
        }
    }

    public boolean canBorrowMore() {
        return borrowedCount < getMaxBooks();
    }

    // Each member type defines its own borrowing limit
    public abstract int getMaxBooks();

    // Used to tell member types apart when saving to a file
    public abstract String getType();

    @Override
    public String toString() {
        return String.format("[%d] %s (%s) | Books borrowed: %d/%d",
                id, name, getType(), borrowedCount, getMaxBooks());
    }
}
