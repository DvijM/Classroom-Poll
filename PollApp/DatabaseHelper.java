import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper {

    private static String getURL() {
        // This will create a file named "poll_app.db" in the same directory
        return "jdbc:sqlite:poll_app.db";
    }

    public static void initializeDatabase() {
        String createPollsTable = "CREATE TABLE IF NOT EXISTS polls ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " question TEXT NOT NULL,"
                + " option1 TEXT NOT NULL,"
                + " option2 TEXT NOT NULL,"
                + " option3 TEXT NOT NULL,"
                + " option4 TEXT NOT NULL,"
                + " isActive INTEGER DEFAULT 0"
                + ");";

        String createVotesTable = "CREATE TABLE IF NOT EXISTS votes ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " poll_id INTEGER NOT NULL,"
                + " selected_option INTEGER NOT NULL,"
                + " FOREIGN KEY (poll_id) REFERENCES polls(id)"
                + ");";

        try (Connection conn = DriverManager.getConnection(getURL());
                Statement stmt = conn.createStatement()) {
            stmt.execute(createPollsTable);
            stmt.execute(createVotesTable);
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    // Admin: Create a new poll
    public static boolean createPoll(String q, String o1, String o2, String o3, String o4) {
        String sql = "INSERT INTO polls(question, option1, option2, option3, option4) VALUES(?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(getURL());
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, q);
            pstmt.setString(2, o1);
            pstmt.setString(3, o2);
            pstmt.setString(4, o3);
            pstmt.setString(5, o4);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error creating poll: " + e.getMessage());
            return false;
        }
    }

    // Admin: Get all polls to display in a list
    public static List<Poll> getAllPolls() {
        String sql = "SELECT id, question, option1, option2, option3, option4, isActive FROM polls";
        List<Poll> polls = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(getURL());
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                polls.add(new Poll(
                        rs.getInt("id"),
                        rs.getString("question"),
                        rs.getString("option1"),
                        rs.getString("option2"),
                        rs.getString("option3"),
                        rs.getString("option4"),
                        rs.getInt("isActive") == 1));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching polls: " + e.getMessage());
        }
        return polls;
    }

    // Admin: Launch a poll (deactivates all others first)
    public static void launchPoll(int pollId) {
        String deactivateSql = "UPDATE polls SET isActive = 0";
        String activateSql = "UPDATE polls SET isActive = 1 WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(getURL())) {
            conn.setAutoCommit(false);

            try (Statement stmt = conn.createStatement();
                    PreparedStatement pstmt = conn.prepareStatement(activateSql)) {

                stmt.executeUpdate(deactivateSql); // End all polls
                pstmt.setInt(1, pollId);
                pstmt.executeUpdate(); // Launch the new one

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Error launching poll: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error with connection: " + e.getMessage());
        }
    }

    // Admin: End all active polls
    public static void endPoll() {
        String sql = "UPDATE polls SET isActive = 0";
        try (Connection conn = DriverManager.getConnection(getURL());
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error ending poll: " + e.getMessage());
        }
    }

    // Student: Get the single active poll
    public static Poll getActivePoll() {
        String sql = "SELECT * FROM polls WHERE isActive = 1 LIMIT 1";
        try (Connection conn = DriverManager.getConnection(getURL());
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return new Poll(
                        rs.getInt("id"),
                        rs.getString("question"),
                        rs.getString("option1"),
                        rs.getString("option2"),
                        rs.getString("option3"),
                        rs.getString("option4"),
                        true);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching active poll: " + e.getMessage());
        }
        return null; // No active poll
    }

    // Student: Submit a vote
    public static boolean submitVote(int pollId, int option) {
        String sql = "INSERT INTO votes(poll_id, selected_option) VALUES(?,?)";

        try (Connection conn = DriverManager.getConnection(getURL());
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pollId);
            pstmt.setInt(2, option);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error submitting vote: " + e.getMessage());
            return false;
        }
    }

    // Result: Get the vote counts for a specific poll
    public static Map<String, Integer> getVoteCounts(Poll poll) {
        Map<String, Integer> counts = new HashMap<>();
        counts.put(poll.getOption1(), 0);
        counts.put(poll.getOption2(), 0);
        counts.put(poll.getOption3(), 0);
        counts.put(poll.getOption4(), 0);

        String sql = "SELECT selected_option, COUNT(*) as count FROM votes "
                + "WHERE poll_id = ? GROUP BY selected_option";

        try (Connection conn = DriverManager.getConnection(getURL());
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, poll.getId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int optionNum = rs.getInt("selected_option");
                int count = rs.getInt("count");

                switch (optionNum) {
                    case 1:
                        counts.put(poll.getOption1(), count);
                        break;
                    case 2:
                        counts.put(poll.getOption2(), count);
                        break;
                    case 3:
                        counts.put(poll.getOption3(), count);
                        break;
                    case 4:
                        counts.put(poll.getOption4(), count);
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting vote counts: " + e.getMessage());
        }
        return counts;
    }

}
