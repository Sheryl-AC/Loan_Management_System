package exception;

public class InvalidLoanException extends Exception {

    public InvalidLoanException() {
        super("Invalid loan details provided.");
    }
    public InvalidLoanException(String message) {
        super(message);
    }
   }
