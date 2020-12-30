package student;

public class Student {

    private String studentID;
    private String firstName;
    private String lastName;
    private String email;
    private String displayName;
    private String qrCode;
    private byte[] idPic;

    public Student(){

    }

    public Student(String studentID, String firstName, String lastName, String email, String displayName, String qrCode, byte[] idPic){
        this.studentID = studentID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.displayName = displayName;
        this.qrCode = qrCode;
        this.idPic = idPic;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public byte[] getIdPic() {
        return idPic;
    }

    public void setIdPic(byte[] idPic) {
        this.idPic = idPic;
    }

    @Override
    public String toString(){
        return email + " " + firstName + " " + lastName;
    }
}
