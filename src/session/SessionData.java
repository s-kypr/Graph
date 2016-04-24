package session;

/**
 * Created by sofia on 4/17/16.
 */
public class SessionData {
    private String username;
    private int sessionNo;

    public SessionData(String username, int sessionNo) {
        this.username = username;
        this.sessionNo = sessionNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getSessionNo() {
        return sessionNo;
    }

    public void setSessionNo(int sessionNo) {
        this.sessionNo = sessionNo;
    }

    @Override
    public String toString() {
        return "SessionData{" +
                "username='" + username + '\n' +
                ", sessionNo=" + sessionNo +
                '}';
    }
}
