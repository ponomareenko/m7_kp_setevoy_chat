package ru.netology;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TreeMap;

public class Main {
    static void main(String[] args) {
        // Чтение файла настроек подключения сервера server/settings.txt
        TreeMap<String, String> settingsServer = new TreeMap<>();
        saveSettings(settingsServer, "server/settings.txt");
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(settingsServer.get("port")))) {
            try (
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter outServer = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader inServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ) {
                String nameServer = inServer.readLine();

                boolean repeat = true;
                while (repeat) {
                    // serverSocket
                    String in = inServer.readLine();
                    if (in.equals("exit")) {
                        repeat = false;
                    }

                    // Логирование всех сообщений в server/file.log сервера
                    logMessage(nameServer, in, "server/file.log");

                    System.out.println("serverSocket - Получил СМС: " + in);

                    outServer.println("Смс для всех клиентов: " + in);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
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

    //МЕТОД Логирование всех сообщений в client/file.log одного клиента и в server/file.log сервера
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