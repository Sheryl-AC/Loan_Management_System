package main;

import entity.Carloan;
import entity.Homeloan;
import dao.ILoanRepository;
import dao.LoanRepositoryImpl;
import exception.InvalidLoanException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ILoanRepository loanRepository = new LoanRepositoryImpl(); 

        while (true) {
            System.out.println("Loan Management System");
            System.out.println("Operations:");          
            System.out.println("1. Apply for Loan");
            System.out.println("2. View All Loans");
            System.out.println("3. View Loan By ID");
            System.out.println("4. Repay Loan");
            System.out.println("5. Exit");
            System.out.print("Please enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1: 
                    System.out.println("Choose Loan Type: ");
                    System.out.println("1. Home Loan");
                    System.out.println("2. Car Loan");
                    System.out.print("Enter choice: ");
                    int loanTypeChoice = scanner.nextInt();
                    scanner.nextLine(); 

                    System.out.print("Enter Customer ID: ");
                    int customerId = scanner.nextInt();
                    scanner.nextLine(); 

                    System.out.print("Enter Principal Amount: ");
                    double principalAmount = scanner.nextDouble();

                    System.out.print("Enter Interest Rate: ");
                    double interestRate = scanner.nextDouble();

                    System.out.print("Enter Loan Term (months): ");
                    int loanTerm = scanner.nextInt();
                    scanner.nextLine(); 

                    System.out.print("Enter Loan Type: ");
                    String loanType = scanner.nextLine();

                    String loanStatus = "Pending";

                    if (loanTypeChoice == 1) {
                        System.out.print("Enter Property Address: ");
                        String propertyAddress = scanner.nextLine();

                        System.out.print("Enter Property Value: ");
                        int propertyValue = scanner.nextInt();

                        Homeloan homeLoan = new Homeloan(0, customerId, principalAmount, interestRate, loanTerm, loanType, loanStatus, propertyAddress, propertyValue);

                        System.out.println("Do you want to apply for the loan? (Yes/No)");
                        scanner.nextLine(); 
                        String confirmation = scanner.nextLine();
                        if (confirmation.equalsIgnoreCase("Yes")) {
                            loanRepository.applyLoan(homeLoan);
                            System.out.println("Home Loan Application Submitted.");
                        } else {
                            System.out.println("Loan Application Canceled.");
                        }
                    } else if (loanTypeChoice == 2) {
                        System.out.print("Enter Car Model: ");
                        String carModel = scanner.nextLine();

                        System.out.print("Enter Car Value: ");
                        int carValue = scanner.nextInt();

                        Carloan carLoan = new Carloan(0, customerId, principalAmount, interestRate, loanTerm, loanType, loanStatus, carModel, carValue);

                        System.out.println("Do you want to apply for the loan? (Yes/No)");
                        scanner.nextLine(); 
                        String confirmation = scanner.nextLine();
                        if (confirmation.equalsIgnoreCase("Yes")) {
                            loanRepository.applyLoan(carLoan);
                            System.out.println("Car Loan Application Submitted.");
                        } else {
                            System.out.println("Loan Application Canceled.");
                        }
                    }
                    break;

                case 2: 
                    System.out.println("Fetching all loans...");
                    loanRepository.getAllLoan().forEach(System.out::println);
                    break;

                case 3: 
                    System.out.print("Enter Loan ID: ");
                    int loanId = scanner.nextInt();
                    try {
                        System.out.println(loanRepository.getLoanById(loanId));
                    } catch (InvalidLoanException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4: 
                    System.out.print("Enter Loan ID: ");
                    loanId = scanner.nextInt();

                    System.out.print("Enter Repayment Amount: ");
                    double amount = scanner.nextDouble();
                    try {
                        loanRepository.loanRepayment(loanId, amount);
                        System.out.println("Loan Repayment Successful.");
                    } catch (InvalidLoanException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 5: 
                    System.out.println("Exiting the Loan Management System.");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
