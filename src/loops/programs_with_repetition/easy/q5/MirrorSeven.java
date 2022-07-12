package loops.programs_with_repetition.easy.q5;

import java.util.Scanner;

public class MirrorSeven {
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    while (true) {
      System.out.println("Enter an integer: ");
      int n = input.nextInt();
      int reverse = 0;
      int original = n;
      while (n > 0) {
        reverse = (reverse * 10) + (n % 10);
        n = n / 10;
      }
      if (reverse == original && original % 7 == 0) {
        System.out.print("Entered a palindrome that is divisible by 7. Ending Program.");
        break;
      }
    }
  }
}
