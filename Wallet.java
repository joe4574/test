package demo;

import java.time.LocalDateTime;

public class Wallet {
    private int id;
    private int userId;
    private float balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // getter and setter methods
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Wallet getTransactions() {
		// TODO Auto-generated method stub
		return null;
	}
	public void add(WalletTransaction tx) {
		// TODO Auto-generated method stub
		
	}

	
    
}