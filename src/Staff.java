public class Staff extends Member {

    private String department;

    public Staff(int id, String name, String department) {
        super(id, name);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public int getMaxBooks() {
        return 5;
    }

    @Override
    public String getType() {
        return "Staff";
    }

    @Override
    public String toString() {
        return super.toString() + " | Department: " + department;
    }
}
