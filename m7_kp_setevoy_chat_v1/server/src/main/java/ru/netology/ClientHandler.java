package ru.netology;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private String clientName;
    private final ChatServer chatServer;

    public ClientHandler(Socket socket, ChatServer chatServer) {
        this.socket = socket;
        this.chatServer = chatServer;
    }

    @Override
    public void run() {
        try (
                PrintWriter outServer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader inServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            this.writer = outServer;
            this.reader = inServer;

            // Проверка дубликата имени
            while (true) {
                String name = reader.readLine();
                if (name == null) {
                    return;
                }
                if (chatServer.checkName(name)) {
                    outServer.println("Занято");
                } else {
                    this.clientName = name;
                    outServer.println("Свободно");
                    break;
                }
            }

            chatServer.addClient(this);

            while (true) {
                // Получение от клиента
                String line = reader.readLine();
                if (line == null) {
                    return;
                }

                String[] in = line.split("&", 3);
                if (in.length < 3) {
                    continue;
                }

                if (in[2].isEmpty()) {
                    continue;
                }

                String nowDateTime = in[0];
                String msg = in[2];

                if (msg.equals("/exit")) {
                    break;
                }

                // Логирование всех сообщений в server/file.log сервера
                logMessage(nowDateTime, clientName, msg, "server/file.log");

                // Отправка на клиента
                String fullMessage = nowDateTime + " " + clientName + ": " + msg;
                chatServer.broadcast(fullMessage);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            chatServer.removeClient(this);
            closeConnection();
        }
    }

    private void closeConnection() {
        if (!socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Не удалось закрыть соединение: "
                        + e.getMessage());
            }
        }
    }

    // Тестовый метод для рассылки остальным клиентам
    public synchronized void sendMessage(String message) {
        writer.println(message);
    }

    //МЕТОД 2 ВАРИАНТ Логирование всех сообщений в server/file.log
    public static synchronized void logMessage(String dataTime, String name, String msg, String pathFile) {
        byte[] outBytes = (dataTime + " " + name + ": " + msg + System.lineSeparator()).getBytes();
        try (FileOutputStream fileOutputStream = new FileOutputStream(pathFile, true)) {
            fileOutputStream.write(outBytes);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public String getName() {
        return clientName;
    }
}
