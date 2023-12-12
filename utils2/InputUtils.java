package utils2;

import java.util.Scanner;

public class InputUtils {
static final Scanner scanner = new Scanner(System.in);
InputUtils() {}

public static int getIntInput() {
int input = -1;
while (input == -1) {
try{
input = scanner.nextInt();
} 
catch (NumberFormatException e) {
System.out.println("Invalid input. Please enter a valid integer.");
scanner.nextLine();
}
}
scanner.nextLine(); 
return input;
}
public static String getStringInput() {
return scanner.nextLine();
}
}
