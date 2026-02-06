package com.biblioteca.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a loan transaction in the library.
 */
public class Loan implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String loanId;
    private String userId;
    private String documentId;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private LoanStatus status;
    
    public enum LoanStatus {
        ACTIVE,
        RETURNED,
        OVERDUE
    }
    
    public Loan(String loanId, String userId, String documentId, LocalDate loanDate, LocalDate dueDate) {
        this.loanId = loanId;
        this.userId = userId;
        this.documentId = documentId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.status = LoanStatus.ACTIVE;
    }
    
    public void returnDocument() {
        this.returnDate = LocalDate.now();
        this.status = LoanStatus.RETURNED;
    }
    
    public boolean isOverdue() {
        if (status == LoanStatus.RETURNED) {
            return false;
        }
        return LocalDate.now().isAfter(dueDate);
    }
    
    public void updateStatus() {
        if (status == LoanStatus.ACTIVE && isOverdue()) {
            status = LoanStatus.OVERDUE;
        }
    }
    
    // Getters and Setters
    public String getLoanId() {
        return loanId;
    }
    
    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getDocumentId() {
        return documentId;
    }
    
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
    
    public LocalDate getLoanDate() {
        return loanDate;
    }
    
    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }
    
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    public LocalDate getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    
    public LoanStatus getStatus() {
        return status;
    }
    
    public void setStatus(LoanStatus status) {
        this.status = status;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return Objects.equals(loanId, loan.loanId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(loanId);
    }
    
    @Override
    public String toString() {
        return String.format("Loan[id=%s, user=%s, document=%s, status=%s]",
            loanId, userId, documentId, status);
    }
}
