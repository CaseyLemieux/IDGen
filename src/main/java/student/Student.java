package student;

public class Student {

    private String studentID;
    private String userName;
    private String email;
    private String displayName;
    private String qrCode;

    public Student(){

    }

    public Student(String studentID, String userName, String email, String displayName, String qrCode){
        this.studentID = studentID;
        this.userName = userName;
        this.email = email;
        this.displayName = displayName;
        this.qrCode = qrCode;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
