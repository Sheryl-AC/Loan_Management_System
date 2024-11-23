package entity;

public class Homeloan extends Loan {
    private String propertyAddress;
    private int propertyValue;

    public Homeloan(int loanId, int customerId, double principalAmount, double interestRate, int loanTerm, String loanType, String loanStatus, String propertyAddress, int propertyValue) {
        super(loanId, customerId, principalAmount, interestRate, loanTerm, loanType, loanStatus);
        this.propertyAddress = propertyAddress;
        this.propertyValue = propertyValue;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public int getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(int propertyValue) {
        this.propertyValue = propertyValue;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", Property Address: " + propertyAddress +
                ", Property Value: " + propertyValue;
    }
}
