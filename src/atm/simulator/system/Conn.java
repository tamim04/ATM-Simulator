package atm.simulator.system;

import java.sql.*;

public class Conn {
    Connection c;
    Statement s;

    public Conn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql:///atmsimulatorsystem", "root", "123456");
            s = c.createStatement();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /** Runs a parameterized SELECT; values are bound with ? placeholders so user input can't change the SQL. */
    public ResultSet query(String sql, Object... params) throws SQLException {
        return prepare(sql, params).executeQuery();
    }

    /** Runs a parameterized INSERT/UPDATE/DELETE and returns the number of affected rows. */
    public int update(String sql, Object... params) throws SQLException {
        return prepare(sql, params).executeUpdate();
    }

    private PreparedStatement prepare(String sql, Object... params) throws SQLException {
        PreparedStatement ps = c.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        return ps;
    }

    /** Current balance for an account (identified by card number): deposits minus withdrawals. */
    public int getBalance(String cardnumber) throws SQLException {
        int balance = 0;
        ResultSet rs = query("SELECT type, amount FROM bank WHERE cardnumber = ?", cardnumber);
        while (rs.next()) {
            int amount;
            try {
                amount = Integer.parseInt(rs.getString("amount").trim());
            } catch (NumberFormatException e) {
                continue; // skip malformed rows saved by older versions
            }
            if (rs.getString("type").equalsIgnoreCase("Deposit")) {
                balance += amount;
            } else if (rs.getString("type").equalsIgnoreCase("Withdrawl")) {
                balance -= amount;
            }
        }
        return balance;
    }

    /** Parses a user-entered amount; returns -1 if it isn't a positive whole number. */
    public static int parseAmount(String text) {
        try {
            int amount = Integer.parseInt(text.trim());
            return amount > 0 ? amount : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
