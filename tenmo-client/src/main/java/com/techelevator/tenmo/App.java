package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.*;
import io.cucumber.java.bs.A;

import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final UserService userService = new UserService();
    private final TransferService transferService = new TransferService();
    private final AccountService accountService = new AccountService();

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() { //Robert

        System.out.println("Your current balance is: $" + accountService.getCurrentBalance(currentUser));
	}

	private void viewTransferHistory() { //Robert
        List<Transfer> transferHistory = transferService.transferHistory(currentUser);
        consoleService.printTransferHistory();

        for (Transfer transfer : transferHistory) {
            if(transfer.getTransferStatus().equalsIgnoreCase("Approved") || transfer.getTransferStatus().equalsIgnoreCase("Pending")) {
                double amountTransferred = transfer.getAmount();
                System.out.println();
                System.out.println(transfer.getTransferId() + "      " +
                        transferService.sendOrReceive(transfer, currentUser) + "       $" +
                        amountTransferred);
            }
        }
        System.out.println("----------");
        int transferSelection = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
        for(Transfer transfer : transferHistory){
            if(transferSelection == transfer.getTransferId()){
               transfer = transferService.getTransferDetails(transferSelection);
                consoleService.printTransferDetails(transfer);
            }
        }

    }

	private void viewPendingRequests() {
        List<Transfer> pendingTransferHistory = transferService.transferHistory(currentUser);
        consoleService.printTransferHistory();

        for (Transfer transfer : pendingTransferHistory) {
            if (transfer.getTransferStatus().equalsIgnoreCase("Pending")) {
                double amountTransferred = transfer.getAmount();
                System.out.println();
                System.out.println(transfer.getTransferId() + "      " +
                        transferService.sendOrReceive(transfer, currentUser) + "       $" +
                        amountTransferred);
            }
        }
        int transferSelection = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");

        for(Transfer transfer : pendingTransferHistory){
            if(transfer.getTransferId() == transferSelection){
                consoleService.approveOrRejectTransfer();
               int approvalSelection = consoleService.promptForInt("Please choose an option: ");
               if(approvalSelection == 1 && transfer.getAmount() <= accountService.getCurrentBalance(currentUser)){
                   transferService.approve(transfer.getTransferId(), currentUser);
               }
               else if(approvalSelection == 1 && transfer.getAmount() > accountService.getCurrentBalance(currentUser)){
                   System.out.println("Error Completing Transfer");
               }
               else if(approvalSelection == 2){
                   transferService.reject(transfer.getTransferId(), currentUser);
               }


            }
        }
		
	}

	private void sendBucks() {
        List<User> userList = userService.getAllUsersExceptCurrentUser(currentUser);
		User recipientUser = chooseCounterparty("Please select a user to send bucks to: ");

        if(recipientUser != null){
            double amountToSend = chooseAmountToSend();
            Transfer transfer = transferService.transferOf(currentUser.getUser(), recipientUser, amountToSend, "Send", "Approved");
            Transfer completed = transferService.submitTransfer(transfer, currentUser);
            if(completed != null){
                consoleService.printMessage("Transfer completed -- Transfer Id - " + completed.getTransferId());
            } else {
                consoleService.printMessage("Error completing transfer.");
            }
        }
	}

	private void requestBucks() {

        User fromUser = chooseCounterparty("Please select a user to request bucks from:");

        if(fromUser != null){
            double amountToRequest = chooseAmountToRequest();
            Transfer transfer = transferService.transferOf(fromUser, currentUser.getUser(), amountToRequest, "Request", "Pending");
            Transfer completed = transferService.submitTransfer(transfer, currentUser);
            if(completed != null){
                consoleService.printMessage("Transfer requested -- Transfer Id - " + completed.getTransferId());
            } else {
                consoleService.printMessage("Error completing transfer.");
            }
        }
	}

    private User chooseCounterparty(String promptMessage){

        List<User> userList = userService.getAllUsersExceptCurrentUser(currentUser);
        int recipientSelection = -1;

        while(recipientSelection < 0 || recipientSelection > userList.size()) {
            consoleService.printUserList(userList);
            recipientSelection = consoleService.promptForInt(promptMessage);
        }

        if(recipientSelection == 0) { return null; }

        return userList.get(recipientSelection-1);

    }

    private double chooseAmountToSend(){
        return consoleService.promptForBigDecimal("Please enter an amount to send: ").doubleValue();
    }

    private double chooseAmountToRequest(){
        return consoleService.promptForBigDecimal("Please enter an amount to request: ").doubleValue();
    }

}
