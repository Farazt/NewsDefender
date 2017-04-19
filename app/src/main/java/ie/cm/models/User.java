package ie.cm.models;

/**
 * Created by ftahir on 16/04/17.
 */

public class User {
    private String fbID;
    private String fbName;
    private String email;
    private String gender;
    private String birthday;

    public User(){

    }

    public User(String fbID,String name,String email,String gender,String birthday) {
        this.fbID = fbID;
        this.fbName=name;
        this.email=email;
        this.gender=gender;
        this.birthday=birthday;
    }

    public String getFbID() {
        return fbID;
    }

    public String getFbName() {
        return fbName;
    }
    public void setFbName(String fbName) {
        this.fbName = fbName;
    }

    public void setFbID(String fbID) {
        this.fbID = fbID;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }
}
