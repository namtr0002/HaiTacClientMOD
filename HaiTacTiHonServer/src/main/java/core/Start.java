package core;

import java.util.Scanner;

public class Start {
    public static void main(String[] args) {
        Log.banner();
        ServerManager.gI().init();
        Scanner scanner = new Scanner(System.in);
        PanelManager.gI().openUI();
        while (ServerManager.gI().running) {
            try {
                if (scanner != null && scanner.hasNextLine()) {
                    String inputString = scanner.nextLine().trim();
                    if (!inputString.isEmpty()) {
                        if (inputString.equalsIgnoreCase("panel") || inputString.equalsIgnoreCase("gui")) {
                            PanelManager.gI().openUI();
                            System.out.println("[ADMIN] Đã mở giao diện Admin Panel GUI!");
                        } else {
                            String result = PanelManager.gI().executeAdminCommand(inputString);
                            System.out.println(result);
                        }
                    }
                } else {
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    break;
                }
            }
        }
    }
}

