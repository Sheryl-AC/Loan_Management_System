package dao;

import entity.Loan;
import exception.InvalidLoanException;
import java.util.List;

public interface ILoanRepository {
    boolean applyLoan(Loan loan);
    double calculateInterest(int loanId) throws InvalidLoanException;
    double calculateEMI(int loanId) throws InvalidLoanException;
    double calculateInterest(double principalAmount, double interestRate, int loanTerm);
    double calculateEMI(double principalAmount, double interestRate, int loanTerm);
    String loanStatus(int loanId);
    int loanRepayment(int loanId, double repaymentAmount) throws InvalidLoanException;
    List<Loan> getAllLoan();
    Loan getLoanById(int loanId) throws InvalidLoanException;
}
