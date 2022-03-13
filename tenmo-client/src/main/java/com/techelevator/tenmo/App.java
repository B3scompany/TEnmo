package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.*;

import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final UserService userService = new UserService();
    private final TransferService transferService = new TransferService();

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
        System.out.println(currentUser.getToken());
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
		// TODO Auto-generated method stub
		// accountService.getUserBalance(currentUser)
        // print stuff out
	}

	private void viewTransferHistory() { //Robert
		// TODO Auto-generated method stub
		
	}

	private void viewPendingRequests() { //Maybe
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() { //Scott
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

	private void requestBucks() { //Scott

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
