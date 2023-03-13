package demo;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WalletService {
    private WalletDao walletDao;

    public WalletService(WalletDao walletDao) {
        this.walletDao = walletDao;
    }

    // 查询用户钱包余额
    public float getBalance(int userId) {
        return walletDao.getBalance(userId);
    }

    // 用户消费
    public boolean purchase(int userId, float amount, int orderId) {
        return walletDao.purchase(userId, amount, orderId);
    }

    // 用户退款
    public boolean refund(int userId, float amount, int orderId) {
        return walletDao.refund(userId, amount, orderId);
    }

    // 用户充值
    public boolean deposit(int userId, float amount) {
        return walletDao.deposit(userId, amount);
    }

    // 查询钱包余额和交易明细
    public Wallet getWalletDetails(int userId) {
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setBalance(walletDao.getBalance(userId));
        // 查询最近十条交易明细
        try(Connection conn = walletDao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM wallet_transaction WHERE user_id = ? ORDER BY created_at DESC LIMIT 10")) {
            stmt.setInt(1, userId);
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    WalletTransaction tx = new WalletTransaction();
                    tx.setUserId(rs.getInt("user_id"));
                    tx.setAmount(rs.getFloat("amount"));
                    tx.setType(TransactionType.valueOf(rs.getString("type")));
                    tx.setReferenceId(rs.getInt("reference_id"));
                    tx.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    wallet.getTransactions().add(tx);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return wallet;
    }
}