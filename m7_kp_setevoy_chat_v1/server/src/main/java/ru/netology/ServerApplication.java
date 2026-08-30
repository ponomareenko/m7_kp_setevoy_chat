package ru.netology;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

public class ServerApplication {
    static void main(String[] args) {
        // Чтение файла настроек подключения сервера server/settings.txt
        TreeMap<String, String> settingsServer = new TreeMap<>();
        saveSettings(settingsServer, "server/settings.txt");

        String portValue = settingsServer.get("port");

        if (portValue == null || portValue.isBlank()) {
            System.out.println("В настройках не указан порт");
            return;
        }

        int port;

        try {
            port = Integer.parseInt(portValue);
        } catch (NumberFormatException exception) {
            System.out.println("Порт должен быть целым числом");
            return;
        }

        ChatServer chatServer = new ChatServer(port);
        chatServer.start();
    }

    // МЕТОД Чтение файла настроек подключения сервера и клиента: server/settings.txt и client/settings.txt
    public static void saveSettings(TreeMap<String, String> settings, String pathFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(pathFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("=")) {
                    int iEqual = line.indexOf('=');
                    settings.put(line.substring(0, iEqual), line.substring(iEqual + 1));
                }
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
