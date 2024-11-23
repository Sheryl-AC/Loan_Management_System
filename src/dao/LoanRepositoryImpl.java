package dao;

import entity.Loan;
import entity.Customer;
import exception.InvalidLoanException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanRepositoryImpl implements ILoanRepository {

    private Connection conn;

    public LoanRepositoryImpl() {
        try {
            this.conn = DBConnUtil.getDBConn();
            if (this.conn == null) {
                throw new SQLException("Failed to establish a database connection.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean applyLoan(Loan loan) {
        try {
            // Check if customer exists
            String customerCheckQuery = "SELECT COUNT(*) FROM Customer WHERE customer_id = ?";
            PreparedStatement checkPs = conn.prepareStatement(customerCheckQuery);
            checkPs.setInt(1, loan.getCustomerId());
            ResultSet rs = checkPs.executeQuery();
            rs.next();
            if (rs.getInt(1) == 0) {
                System.out.println("Customer ID " + loan.getCustomerId() + " does not exist.");
                return false;
            }

            // Insert the loan
            String query = "INSERT INTO Loan (customer_id, principal_amount, interest_rate, loan_term, loan_type, loan_status) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, loan.getCustomerId());
            ps.setDouble(2, loan.getPrincipalAmount());
            ps.setDouble(3, loan.getInterestRate());
            ps.setInt(4, loan.getLoanTerm());
            ps.setString(5, loan.getLoanType());
            ps.setString(6, "Pending");
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                loan.setLoanId(generatedKeys.getInt(1));
            }

            System.out.println("Loan applied successfully! Loan ID: " + loan.getLoanId());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public double calculateInterest(int loanId) throws InvalidLoanException {
        try {
            String query = "SELECT principal_amount, interest_rate, loan_term FROM Loan WHERE loan_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, loanId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double principalAmount = rs.getDouble("principal_amount");
                double interestRate = rs.getDouble("interest_rate");
                int loanTerm = rs.getInt("loan_term");
                return (principalAmount * interestRate * loanTerm) / 1200;
            } else {
                throw new InvalidLoanException("Loan not found for ID: " + loanId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InvalidLoanException("Error calculating interest for loan ID: " + loanId);
        }
    }

    @Override
    public double calculateEMI(int loanId) throws InvalidLoanException {
        try {
            String query = "SELECT principal_amount, interest_rate, loan_term FROM Loan WHERE loan_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, loanId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double principalAmount = rs.getDouble("principal_amount");
                double interestRate = rs.getDouble("interest_rate");
                int loanTerm = rs.getInt("loan_term");
                double monthlyInterestRate = interestRate / 12 / 100;
                return (principalAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, loanTerm)) /
                        (Math.pow(1 + monthlyInterestRate, loanTerm) - 1);
            } else {
                throw new InvalidLoanException("Loan not found for ID: " + loanId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InvalidLoanException("Error calculating EMI for loan ID: " + loanId);
        }
    }

    @Override
    public String loanStatus(int loanId) {
        try {
            String query = "SELECT credit_score FROM Loan INNER JOIN Customer ON Loan.customer_id = Customer.customer_id WHERE loan_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, loanId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int creditScore = rs.getInt("credit_score");
                String status = creditScore > 650 ? "Approved" : "Rejected";
                String updateQuery = "UPDATE Loan SET loan_status = ? WHERE loan_id = ?";
                PreparedStatement updatePs = conn.prepareStatement(updateQuery);
                updatePs.setString(1, status);
                updatePs.setInt(2, loanId);
                updatePs.executeUpdate();
                return status;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int loanRepayment(int loanId, double repaymentAmount) throws InvalidLoanException {
        try {
            double emi = calculateEMI(loanId);
            if (repaymentAmount < emi) {
                System.out.println("Payment is less than the EMI. Repayment rejected.");
                return -1;
            } else {
                String query = "UPDATE Loan SET principal_amount = principal_amount - ? WHERE loan_id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setDouble(1, repaymentAmount);
                ps.setInt(2, loanId);
                ps.executeUpdate();

                System.out.println("Repayment successful. Remaining balance updated.");
                return 1;
            }
        } catch (SQLException | InvalidLoanException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<Loan> getAllLoan() {
        List<Loan> loans = new ArrayList<>();
        if (this.conn == null) {
            System.err.println("Database connection is not initialized. Check DBConnUtil.");
            return loans;
        }
        try {
            String query = "SELECT * FROM Loan";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Loan loan = new Loan(0, 0, 0, 0, 0, query, query);
                loan.setLoanId(rs.getInt("loan_id"));
                loan.setPrincipalAmount(rs.getDouble("principal_amount"));
                loan.setInterestRate(rs.getDouble("interest_rate"));
                loan.setLoanTerm(rs.getInt("loan_term"));
                loan.setLoanType(rs.getString("loan_type"));
                loan.setLoanStatus(rs.getString("loan_status"));
                loans.add(loan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    @Override
    public Loan getLoanById(int loanId) throws InvalidLoanException {
        try {
            String query = "SELECT * FROM Loan WHERE loan_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, loanId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Loan loan = new Loan(loanId, loanId, loanId, loanId, loanId, query, query);
                loan.setLoanId(rs.getInt("loan_id"));
                loan.setPrincipalAmount(rs.getDouble("principal_amount"));
                loan.setInterestRate(rs.getDouble("interest_rate"));
                loan.setLoanTerm(rs.getInt("loan_term"));
                loan.setLoanType(rs.getString("loan_type"));
                loan.setLoanStatus(rs.getString("loan_status"));
                return loan;
            } else {
                throw new InvalidLoanException("Loan not found for ID: " + loanId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InvalidLoanException("Error fetching loan for ID: " + loanId);
        }
    }

	@Override
	public double calculateInterest(double principalAmount, double interestRate, int loanTerm) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double calculateEMI(double principalAmount, double interestRate, int loanTerm) {
		// TODO Auto-generated method stub
		return 0;
	}
}
