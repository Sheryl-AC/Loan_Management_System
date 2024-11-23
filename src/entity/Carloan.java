package entity;

public class Carloan extends Loan {
    private String carModel;
    private int carYear;

    public Carloan(int loanId, int customerId, double principalAmount, double interestRate, int loanTerm, String loanType, String loanStatus, String carModel, int carYear) {
        super(loanId, customerId, principalAmount, interestRate, loanTerm, loanType, loanStatus); // Call the Loan constructor
        this.carModel = carModel;
        this.carYear = carYear;
    }

    // Getters and setters
    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public int getCarYear() {
        return carYear;
    }

    public void setCarYear(int carYear) {
        this.carYear = carYear;
    }
}
