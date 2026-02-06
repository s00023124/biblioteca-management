package com.biblioteca.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a library user.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String userId;
    private String name;
    private String email;
    private String phone;
    private LocalDate registrationDate;
    private UserType userType;
    private List<String> currentLoans;
    
    public enum UserType {
        STUDENT(5),
        TEACHER(10),
        EXTERNAL(3);
        
        private final int maxLoans;
        
        UserType(int maxLoans) {
            this.maxLoans = maxLoans;
        }
        
        public int getMaxLoans() {
            return maxLoans;
        }
    }
    
    public User(String userId, String name, String email, String phone, UserType userType) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.registrationDate = LocalDate.now();
        this.userType = userType;
        this.currentLoans = new ArrayList<>();
    }
    
    public boolean canBorrow() {
        return currentLoans.size() < userType.getMaxLoans();
    }
    
    public void addLoan(String documentId) {
        currentLoans.add(documentId);
    }
    
    public void removeLoan(String documentId) {
        currentLoans.remove(documentId);
    }
    
    // Getters and Setters
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }
    
    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    public UserType getUserType() {
        return userType;
    }
    
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    
    public List<String> getCurrentLoans() {
        return new ArrayList<>(currentLoans);
    }
    
    public void setCurrentLoans(List<String> currentLoans) {
        this.currentLoans = new ArrayList<>(currentLoans);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
    
    @Override
    public String toString() {
        return String.format("User[id=%s, name=%s, type=%s, loans=%d/%d]",
            userId, name, userType, currentLoans.size(), userType.getMaxLoans());
    }
}
