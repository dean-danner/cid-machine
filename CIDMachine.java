import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CIDMachine {
	static List<Caller> callers = new ArrayList<Caller>();
	static List<Caller> blocked = new ArrayList<Caller>();

	public static String inputCmd(Scanner in) { // Prints out recurring information and takes input for a command. Runtime: O(1)
		String cmd = "";

		System.out.println("Press (R) to receive a call\nPress (D) to delete a call\nPress (S) to show previous calls\n"
				+ "Press (P) to purge a call\nPress (F) to find a number\nPress (B) to block a call"); // Prints out menu.
		if (callers.size() > 0)
			System.out.println("Previous caller: " + callers.get(callers.size() - 1).toString()); // Prints out the previous caller.
		else
			System.out.println("No calls found.");

		if (in.hasNextLine())
			cmd = in.nextLine(); // Saves the command the user types.

		return cmd;
	}

	public static void receiveCall(Scanner in) { // Prompts and saves the user input for name and number and checks for blocked and unknown callers. Runtime: O(n^2)
		String numberStr = "";
		String name = "";

		do {
			System.out.println("Enter phone number: ");
			if (in.hasNextLine())
				numberStr = in.nextLine(); // Saves the number as a string to allow regex checking.
		} while (numberStr.equals("") || !numberStr.matches("[0-9]+") || numberStr.length() != 10); // Keeps prompting until the user enters a valid phone number.
		long number = Long.parseLong(numberStr); // Converts the number to a long value.

		do {
			System.out.println("Enter caller ID: ");
			if (in.hasNextLine())
				name = in.nextLine(); // Saves the caller id the user enters.
			
			if (name.equals("Unknown Caller")) { // Checks if the caller is an unknown caller, if so they are blocked.
				System.out.println("Unknown Caller blocked.");
				break;
			} else {
				boolean b = false;
				for (Caller c : blocked) {
					if (c.getNumber() == number) { // Checks if the number entered is on the block list.
						b = true;
						System.out.println("Caller is blocked.");
					}
				}
				if (!b) { // If the caller is not blocked, they are added to the callers list.
					callers.add(new Caller(number, name));
					System.out.println("Caller added.");
				}
			}
		} while (name.equals("") || !name.matches("[a-zA-Z]+")); // Keep prompting until the user enters a valid name.
	}

	public static void deleteCall(Scanner in) { // Deletes the previous caller from history. Runtime: O(n)
		String answer = "";

		if (callers.size() > 0) { // Makes sure there is a user to delete.
			do {
				System.out.println("Are you sure you want to delete the last call? (Y/N): ");
				if (in.hasNextLine())
					answer = in.nextLine(); // Saves the users input as answer.
				if (answer.equals("Y")) { // If the user enters "Y" the previous caller is deleted.
					callers.remove(callers.size() - 1);
					System.out.println("Previous caller deleted.");
					break;
				}
				if (answer.equals("N")) // If the user enters "N", returns to the main menu.
					break;
			} while (answer.equals("") || !answer.equals("Y") || !answer.equals("N")); // Keep asking until the user enters a valid answer.
		} else
			System.out.println("Call list is empty.");
	}

	public static void showPrevious(Scanner in) { // Shows the previous users in the history up to the requested amount. Runtime: O(n)
		String kStr = "";

		if (callers.size() > 0) { // Makes sure there are users to show.
			do {
				System.out.println("How many previous calls do you want to show?: ");
				if (in.hasNextLine())
					kStr = in.nextLine(); // Saves number of previous users as a string.
			} while (kStr.equals("") || !kStr.matches("[0-9]+"));
			int k = Integer.parseInt(kStr); // Converts string to a int.

			if (k >= callers.size()) { // If the number entered is greater than the length, prints all previous callers.
				k = 0;
				System.out.println("No more calls.");
			} else {
				System.out.println("Previous " + k + " callers:");
				k = callers.size() - k; // Makes sure the loop starts at the back of the list.
				for (int i = callers.size() - 1; i >= k; i--) // Loops through the callers and prints the previous amount.
					System.out.println(callers.get(i).toString());
			}
		} else
			System.out.println("Call list is empty.");
	}

	public static void purgeCallers() { // Deletes all duplicates of the previous caller. Runtime: O(n)
		List<Caller> purge = new ArrayList<Caller>(); // List for storing the to-be deleted users.
		boolean purged = false;

		for (Caller c : callers) { // Loops through the list and determines whether there are any duplicates, if so they are added to the purge list.
			if (c.getNumber() == callers.get(callers.size() - 1).getNumber() && c != callers.get(callers.size() - 1)) {
				purge.add(c);
				purged = true;
			}
		}

		if (purged) { // If there are duplicates they are removed from the callers list.
			callers.removeAll(purge);
			System.out.println("Callers purged.");
		} else
			System.out.println("No Callers purged.");
	}

	public static void findNumber(Scanner in) { // Returns the phone number for the name entered. Runtime: O(n)
		String name = "";
		boolean found = false;

		if (callers.size() > 0) { // Makes sure there are callers to find.
			do {
				System.out.println("Enter a name you want to find: ");
				if (in.hasNextLine())
					name = in.nextLine(); // Saves requested name.
			} while (name.equals("") || !name.matches("[a-zA-Z]+")); // Keep asking until a valid name is entered.

			for (Caller c : callers) { // If the caller is found, the number is returned.
				if (c.getName().equals(name)) {
					found = true;
					System.out.println("Number: " + c.getNumber());
				}
			}
			if (!found)
				System.out.println("Caller not found."); // If the caller is not found, returns a message.
		} else
			System.out.println("Call list is empty.");
	}

	public static void blockCaller() { // Adds the previous caller to the blocked list. Runtime: O(1)
		if (callers.size() > 0) { // Makes sure there is a previous user.
			blocked.add(callers.get(callers.size() - 1)); // Adds user to blocked list.
			callers.remove(callers.get(callers.size() - 1)); // Removes user from caller list.
			System.out.println("Previous caller blocked.");
		} else
			System.out.println("Call list is empty.");
	}

	public static void main(String[] args) { // Puts everything together.
		Scanner in = new Scanner(System.in);
		while (true) { // Keep asking for an input.
			String cmd = inputCmd(in);

			if (cmd.equals("R")) // If certain commands are entered, redirects to certain methods.
				receiveCall(in);
			else if (cmd.equals("D"))
				deleteCall(in);
			else if (cmd.equals("S"))
				showPrevious(in);
			else if (cmd.equals("P"))
				purgeCallers();
			else if (cmd.equals("F"))
				findNumber(in);
			else if (cmd.equals("B"))
				blockCaller();
			else if (cmd.equals("Q"))
				break;
		}
	}
}
