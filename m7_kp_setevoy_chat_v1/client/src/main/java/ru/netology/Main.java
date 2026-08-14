package ru.netology;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {
    static void main(String[] args) {
        // Чтение файла настроек подключения клиента client/settings.txt
        TreeMap<String, String> settingsClient = new TreeMap<>();
        saveSettings(settingsClient, "client/settings.txt");

        try (
                Socket clientSocket = new Socket(settingsClient.get("host"), Integer.parseInt(settingsClient.get("port")));

                PrintWriter outClient = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader inClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                Scanner scanner = new Scanner(System.in);
                ) {
            System.out.println("Сервер запущен\n");
            System.out.print("Введите своё имя: ");
            String nameClient = scanner.nextLine();
            outClient.println(nameClient);
            System.out.println("Подключение к серверу выполнено\n");

            boolean repeat = true;
            while (repeat) {
                // clientSocket - Ввод смс на клиенте
                System.out.print("clientSocket - Введите СМС: ");
                String out = scanner.nextLine();
                if (out.equals("exit")) {
                    repeat = false;
                }

                // Логирование всех сообщений в client/file.log одного клиента
                logMessage(nameClient, out, "client/file.log");

                // clientSocket - Отправка смс на сервер
                outClient.println(out);

                // clientSocket - Получение смс от сервера на клиент
                String in = inClient.readLine();
                System.out.println(in);
            }
            if (!repeat) {
                System.out.println("Соединение с сервером закрыто");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // МЕТОД Чтение файла настроек подключения сервера и клиента: server/settings.txt и client/settings.txt
    public static void saveSettings(TreeMap<String, String> settings, String pathFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(pathFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int iEqual = line.indexOf('=');
                settings.put(line.substring(0, iEqual), line.substring(iEqual + 1));
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    //МЕТОД Логирование всех сообщений в client/file.log одного клиента
    public static void logMessage(String name, String msg, String pathFile) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        byte[] outBytes = ("[" + LocalDateTime.now().format(formatter) + "] " +
                name + ": " + msg + System.lineSeparator()).getBytes();
        try (FileOutputStream fileOutputStream = new FileOutputStream(pathFile, true)) {
            fileOutputStream.write(outBytes);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}