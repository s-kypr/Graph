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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionData that = (SessionData) o;

        if (sessionNo != that.sessionNo) return false;
        if (!username.equals(that.username)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + sessionNo;
        return result;
    }

    @Override
    public String toString() {
        return "{ username='" + username +", sessionNo=" + sessionNo +"}\n";
    }
}
