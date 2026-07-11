import java.util.*;
import java.io.*;
import java.sql.*;

// ================= USER =================
class User implements Serializable {
    String username, password;
    int points = 100;

    ArrayList<String> learn = new ArrayList<>();
    ArrayList<String> teach = new ArrayList<>();

    public User(String u, String p) {
        username = u;
        password = p;
    }

    void display() {
        System.out.println("\nUser: " + username);
        System.out.println("Points: " + points);
        System.out.println("Learn: " + learn);
        System.out.println("Teach: " + teach);
    }
}

// ================= RESOURCE =================
class Resource implements Serializable {
    String username, skill, type, content;

    public Resource(String u, String s, String t, String c) {
        username = u;
        skill = s.toLowerCase();
        type = t;
        content = c;
    }

    public String toString() {
        return username + " | " + skill + " | " + type + " | " + content;
    }
}

// ================= MAIN =================
public class SkillUp {

    static final String URL = "jdbc:mysql://localhost:3306/skillup";
static final String USER = "root";
static final String PASSWORD = "bhavya";

static Connection getConnection() throws Exception {
    Class.forName("com.mysql.cj.jdbc.Driver");
    return DriverManager.getConnection(URL, USER, PASSWORD);
}
    static ArrayList<User> users = new ArrayList<>();
    static ArrayList<Resource> resources = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static User currentUser = null;

    static final int LEARN_COST = 15;
    static final int TEACH_REWARD = 30;
//create tables
static void createTables() {

    try(Connection con = getConnection()) {

        Statement st = con.createStatement();

        st.executeUpdate(
        "CREATE TABLE IF NOT EXISTS users(" +
        "username VARCHAR(50) PRIMARY KEY," +
        "password VARCHAR(100)," +
        "points INT)");

        st.executeUpdate(
        "CREATE TABLE IF NOT EXISTS learnskills(" +
        "username VARCHAR(50)," +
        "skill VARCHAR(50))");

        st.executeUpdate(
        "CREATE TABLE IF NOT EXISTS teachskills(" +
        "username VARCHAR(50)," +
        "skill VARCHAR(50))");

        st.executeUpdate(
        "CREATE TABLE IF NOT EXISTS resources(" +
        "id INT AUTO_INCREMENT PRIMARY KEY," +
        "username VARCHAR(50)," +
        "skill VARCHAR(50)," +
        "type VARCHAR(30)," +
        "content TEXT)");

    }
    catch(Exception e){
        e.printStackTrace();
    }

}
    // ================= FILE =================
static void saveData() {

    try(Connection con = getConnection()) {

        Statement st = con.createStatement();

        st.executeUpdate("DELETE FROM users");
        st.executeUpdate("DELETE FROM learnskills");
        st.executeUpdate("DELETE FROM teachskills");
        st.executeUpdate("DELETE FROM resources");

        PreparedStatement psUser =
                con.prepareStatement(
                        "INSERT INTO users VALUES(?,?,?)");

        for(User u : users){

            psUser.setString(1,u.username);
            psUser.setString(2,u.password);
            psUser.setInt(3,u.points);

            psUser.executeUpdate();

            PreparedStatement learn =
                    con.prepareStatement(
                            "INSERT INTO learnskills VALUES(?,?)");

            for(String s : u.learn){

                learn.setString(1,u.username);
                learn.setString(2,s);
                learn.executeUpdate();

            }

            PreparedStatement teach =
                    con.prepareStatement(
                            "INSERT INTO teachskills VALUES(?,?)");

            for(String s : u.teach){

                teach.setString(1,u.username);
                teach.setString(2,s);
                teach.executeUpdate();

            }

        }

        PreparedStatement psResource =
                con.prepareStatement(
                "INSERT INTO resources(username,skill,type,content) VALUES(?,?,?,?)");

        for(Resource r : resources){

            psResource.setString(1,r.username);
            psResource.setString(2,r.skill);
            psResource.setString(3,r.type);
            psResource.setString(4,r.content);

            psResource.executeUpdate();

        }

    }

    catch(Exception e){

        e.printStackTrace();

    }

}


//load Dta

static void loadData(){

    users.clear();
    resources.clear();

    try(Connection con=getConnection()){

        Statement st=con.createStatement();

        ResultSet rs=st.executeQuery("SELECT * FROM users");

        while(rs.next()){

            User u=new User(
                    rs.getString("username"),
                    rs.getString("password"));

            u.points=rs.getInt("points");

            users.add(u);

        }

        rs=st.executeQuery("SELECT * FROM learnskills");

        while(rs.next()){

            String user=rs.getString("username");
            String skill=rs.getString("skill");

            for(User u:users)
                if(u.username.equals(user))
                    u.learn.add(skill);

        }

        rs=st.executeQuery("SELECT * FROM teachskills");

        while(rs.next()){

            String user=rs.getString("username");
            String skill=rs.getString("skill");

            for(User u:users)
                if(u.username.equals(user))
                    u.teach.add(skill);

        }

        rs=st.executeQuery("SELECT * FROM resources");

        while(rs.next()){

            resources.add(new Resource(
                    rs.getString("username"),
                    rs.getString("skill"),
                    rs.getString("type"),
                    rs.getString("content")));

        }

    }

    catch(Exception e){

        System.out.println("Fresh Start!");

    }

}    // ================= SECURITY =================
    static String hashPassword(String password) {
        return Integer.toHexString(password.hashCode());
    }

    // ================= HELPERS =================
    static boolean isSkillAvailable(String skill) {
        for (Resource r : resources) {
            if (r.skill.equalsIgnoreCase(skill)) return true;
        }
        return false;
    }

    static boolean isValidLink(String link) {
        return link.startsWith("http://") || link.startsWith("https://");
    }

    // ================= AUTH =================
    static void register() {
        sc.nextLine();
        System.out.print("Username: ");
        String u = sc.nextLine();

        if (u.length() < 3) {
            System.out.println("Username too short!");
            return;
        }

        for (User user : users) {
            if (user.username.equalsIgnoreCase(u)) {
                System.out.println("Username exists!");
                return;
            }
        }

        System.out.print("Password (min 4 chars): ");
        String p = sc.nextLine();

        if (p.length() < 4) {
            System.out.println("Weak password!");
            return;
        }

        users.add(new User(u, hashPassword(p)));
        saveData();
        System.out.println("Registered!");
    }

    static void login() {
        sc.nextLine();
        System.out.print("Username: ");
        String u = sc.nextLine();

        System.out.print("Password: ");
        String p = sc.nextLine();

        for (User user : users) {
            if (user.username.equalsIgnoreCase(u) &&
                user.password.equals(hashPassword(p))) {

                currentUser = user;
                System.out.println("Login success!");
                return;
            }
        }

        System.out.println("Invalid credentials!");
    }

    // ================= LEARN =================
    static void addLearnSkill() {
        if (currentUser == null) {
            System.out.println("Login first!");
            return;
        }

        sc.nextLine();
        System.out.print("Skill to learn: ");
        String s = sc.nextLine().toLowerCase();

        if (!isSkillAvailable(s)) {
            System.out.println("Skill not available yet!");
            return;
        }

        if (currentUser.learn.contains(s)) {
            System.out.println("Already added!");
            return;
        }

        if (currentUser.points < LEARN_COST) {
            System.out.println("Not enough points!");
            return;
        }

        currentUser.learn.add(s);
        currentUser.points -= LEARN_COST;
        saveData();

        System.out.println("-15 points | Skill added!");
    }

    // ================= TEACH =================
    static void contributeSkill() {
        if (currentUser == null) {
            System.out.println("Login first!");
            return;
        }

        sc.nextLine();
        System.out.print("Skill to teach: ");
        String skill = sc.nextLine().toLowerCase();

        if (currentUser.teach.contains(skill)) {
            System.out.println("Already teaching!");
            return;
        }

        System.out.print("Type (pdf/video/link): ");
        String type = sc.next();

        sc.nextLine();
        System.out.print("Enter content: ");
        String content = sc.nextLine();

        if (type.equalsIgnoreCase("link") && !isValidLink(content)) {
            System.out.println("Invalid link!");
            return;
        }

        currentUser.teach.add(skill);
        resources.add(new Resource(currentUser.username, skill, type, content));

        currentUser.points += TEACH_REWARD;
        saveData();

        System.out.println("+30 points | Skill + Resource added!");
    }

    // ================= MATCH =================
    static void matchSkills() {
        if (currentUser == null) {
            System.out.println("Login first!");
            return;
        }

        System.out.println("\n--- Matches ---");

        for (String skill : currentUser.learn) {
            for (User u : users) {
                if (u != currentUser && u.teach.contains(skill)) {

                    System.out.println("Learn " + skill + " from " + u.username);

                    for (Resource r : resources) {
                        if (r.skill.equalsIgnoreCase(skill)
                                && r.username.equals(u.username)) {
                            System.out.println("   Resource: " + r.content);
                        }
                    }
                }
            }
        }
    }

    // ================= SEARCH =================
    static void searchBySkill() {
        sc.nextLine();
        System.out.print("Enter skill: ");
        String skill = sc.nextLine();

        boolean found = false;

        for (Resource r : resources) {
            if (r.skill.equalsIgnoreCase(skill)) {
                System.out.println(r);
                found = true;
            }
        }

        if (!found) System.out.println("No resources found!");
    }

    static void viewResources() {
        for (Resource r : resources) {
            System.out.println(r);
        }
    }

    // ================= LEADERBOARD =================
    static void leaderboard() {
        users.sort((a, b) -> b.points - a.points);

        System.out.println("\n--- Leaderboard ---");

        int rank = 1;
        for (User u : users) {
            System.out.println("#" + rank++ + " " + u.username + " : " + u.points);
        }
    }

    // ================= LOADER =================
    static class Loader extends Thread {
        public void run() {
            try {
                System.out.print("Loading");
                for (int i = 0; i < 3; i++) {
                    Thread.sleep(500);
                    System.out.print(".");
                }
                System.out.println("\nReady!\n");
            } catch (Exception e) {}
        }
    }

    // ================= MAIN =================
    public static void main(String[] args) {
    createTables();
    loadData();
        new Loader().start();

        while (true) {

            System.out.println("\n===== SkillUp Menu  =====");
            System.out.println("\n1.Register \n 2.Login");
            System.out.println("3.Add Learn Skill (-15)");
            System.out.println("4.Contribute Skill (+30)");
            System.out.println("5.Match Skills");
            System.out.println("6.View Resources");
            System.out.println("7.Search by Skill");
            System.out.println("8.Leaderboard");
            System.out.println("9.Profile");
            System.out.println("10.Exit");

            int ch = sc.nextInt();

            switch (ch) {

                case 1: register(); break;
                case 2: login(); break;
                case 3: addLearnSkill(); break;
                case 4: contributeSkill(); break;
                case 5: matchSkills(); break;
                case 6: viewResources(); break;
                case 7: searchBySkill(); break;
                case 8: leaderboard(); break;

                case 9:
                    if (currentUser != null) currentUser.display();
                    else System.out.println("Login first!");
                    break;

                case 10:
                    saveData();
                    System.out.println("Saved & Exit");
                    System.exit(0);
            }
        }
    }
}