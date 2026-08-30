package ru.netology;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.TreeMap;

public class ClientApplication {

    static void main(String[] args) throws InterruptedException {
        // Чтение файла настроек подключения клиента client/settings.txt
        TreeMap<String, String> settingsClient = new TreeMap<>();
        saveSettings(settingsClient, "client/settings.txt");
        int port = Integer.parseInt(settingsClient.get("port"));
        String host = settingsClient.get("host");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        try (
                Socket clientSocket = new Socket(host, port);

                PrintWriter outClient = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader inClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                Scanner scanner = new Scanner(System.in);
        ) {
            System.out.println("Сервер запущен");

            // Проверка дубликата имени
            String nameClient;
            while (true) {
                while (true) {
                    System.out.print("Введите имя: ");
                    nameClient = scanner.nextLine();
                    if (nameClient.length() > 1) {
                        break;
                    }
                }

                outClient.println(nameClient);

                String otvetName = inClient.readLine();
                if (otvetName.equals("Свободно")) {
                    break;
                } else {
                    System.out.println("Это имя занято, попробуйте другое)");
                }
            }
            String finalNameClient = nameClient;

            Thread inputSendingThread = new Thread(() -> {
                System.out.println("Подключение к серверу выполнено\n");

                while (true) {
                    // Ввод смс на клиенте
                    String msg = scanner.nextLine();
                    String nowDateTime = "[" + LocalDateTime.now().format(formatter) + "]";
                    String join = "&";
                    String out = nowDateTime + join + finalNameClient + join + msg;
                    // Отправка смс на сервер
                    outClient.println(out);
                    if (msg.equals("/exit")) {
                        break;
                    }
                }
                System.out.println("Соединение с сервером закрыто");
            });

            Thread constantReadingThread = new Thread(() -> {
                try {
                    while (true) {
                        // Получение смс от сервера на клиент
                        String in = inClient.readLine();
                        if (in == null) {
                            break;
                        }
                        // Логирование всех сообщений в client/file.log
                        logMessage(in, "client/logs/" + finalNameClient + "-file.log");
                        System.out.println(in);
                    }
                } catch (IOException exception) {
                    if (!clientSocket.isClosed()) {
                        System.out.println("Ошибка получения сообщения: " + exception.getMessage());
                    }
                }
            });

            inputSendingThread.setName("inputSendingThread");
            constantReadingThread.setName("constantReadingThread");

            inputSendingThread.start();

            constantReadingThread.start();

            inputSendingThread.join();

            clientSocket.close();

            constantReadingThread.join();

        } catch (IOException e) {
            System.out.println("Не удалось подключиться к серверу " + host + ":" + port);
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

    //МЕТОД 3 ВАРИАНТ Логирование всех сообщений в client/file.log одного клиента
    public static void logMessage(String msg, String pathFile) {
        File file = new File(pathFile);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                System.out.println(exception.getMessage());
            }
        }

        byte[] outBytes = (msg + System.lineSeparator()).getBytes();
        try (FileOutputStream fileOutputStream = new FileOutputStream(pathFile, true)) {
            fileOutputStream.write(outBytes);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}