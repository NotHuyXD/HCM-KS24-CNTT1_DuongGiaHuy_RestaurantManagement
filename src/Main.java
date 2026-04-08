import presentation.AuthMenu;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthMenu authMenu = new AuthMenu(scanner);
        authMenu.start();

        System.out.println("Đã đóng kết nối. Hẹn gặp lại!");
        scanner.close();
    }
}