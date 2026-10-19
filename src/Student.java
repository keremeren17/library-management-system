public class Student extends Member {

    private String studentNumber;

    public Student(int id, String name, String studentNumber) {
        super(id, name);
        this.studentNumber = studentNumber;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    @Override
    public int getMaxBooks() {
        return 3;
    }

    @Override
    public String getType() {
        return "Student";
    }

    @Override
    public String toString() {
        return super.toString() + " | Student No: " + studentNumber;
    }
}
