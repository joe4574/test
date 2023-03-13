package demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WalletDao {
    // 获取指定用户的钱包余额
    public float getBalance(int userId) {
        float balance = 0;
        try(Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM wallet WHERE user_id = ?")) {
            stmt.setInt(1, userId);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    balance = rs.getFloat("balance");
                } else { // 该用户未创建钱包
                    createWallet(userId);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    // 用户充值
    public boolean deposit(int userId, float amount) {
        boolean success = false;
        try(Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE wallet SET balance = balance + ? WHERE user_id = ?")) {
            conn.setAutoCommit(false);
            stmt.setFloat(1, amount);
            stmt.setInt(2, userId);
            
            int rows = stmt.executeUpdate();
            if(rows == 1) {
                saveWalletTransaction(
                    userId, amount, TransactionType.DEPOSIT, 0);
                conn.commit();
                success = true;
            } else {
                conn.rollback();
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    // 用户消费
    public boolean purchase(int userId, float amount, int orderId) {
        boolean success = false;
        try(Connection conn = getConnection()) {
            float balance = getBalance(userId);
            if(balance < amount) { // 余额不足
                return false;
            }
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE wallet SET balance = balance - ? WHERE user_id = ?");
            stmt.setFloat(1, amount);
            stmt.setInt(2, userId);
            conn.setAutoCommit(false);
            int rows = stmt.executeUpdate();
            if(rows == 1) {
                saveWalletTransaction(
                    userId, -amount, TransactionType.PURCHASE, orderId);
                conn.commit();
                success = true;
            } else {
                conn.rollback();
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    // 用户退款
    public boolean refund(int userId, float amount, int orderId) {
        boolean success = false;
        try(Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(

"UPDATE wallet SET balance = balance + ? WHERE user_id = ?");
            stmt.setFloat(1, amount);
            stmt.setInt(2, userId);
            conn.setAutoCommit(false);
            int rows = stmt.executeUpdate();
            if(rows == 1) {
                saveWalletTransaction(
                    userId, amount, TransactionType.REFUND, orderId);
                conn.commit();
                success = true;
            } else {
                conn.rollback();
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    // 保存交易明细
    private void saveWalletTransaction(
        int userId, float amount, TransactionType type, int referenceId) {
        try(Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO wallet_transaction (user_id, amount, type, reference_id) VALUES (?, ?, ?, ?)")) {
            stmt.setInt(1, userId);
            stmt.setFloat(2, amount);
            stmt.setString(3, type.name());
            stmt.setInt(4, referenceId);
            stmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // 创建用户钱包
    private boolean createWallet(int userId) {
        boolean success = false;
        try(Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO wallet (user_id) VALUES (?)")) {
            stmt.setInt(1, userId);
            int rows = stmt.executeUpdate();
            if(rows == 1) {
                success = true;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    
    private final static String URL = "jdbc:mysql://127.0.0.1:3306/test";
    private final static String USER_NAME = "root";
    private final static String PASSWORD = "root";
    public Connection getConnection() throws SQLException {
        // 获取数据库连接并返回
    	Connection conn = null;
        try {
            // 加载数据库驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 获取数据库连接
            conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
