package proj;

public class Caller {
	private long number;
	private String name;

	public Caller(long number, String name) { // Constructor for a caller.
		this.number = number;
		this.name = name;
	}

	public long getNumber() { // Returns callers number. Runtime: O(1)
		return number;
	}

	public String getName() { // Returns callers name. Runtime: O(1)
		return name;
	}

	public String toString() { // Returns caller as a string. Runtime: O(1)
		return String.format("%s | %d", name, number);
	}
}
