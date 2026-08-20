package ru.netology;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {
    private final int port;
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public ChatServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public void addClient(ClientHandler client) {
        clients.add(client);
    }

    // МЕТОД Отключение клиента
    public void removeClient(ClientHandler client) {
        clients.remove(client);
        System.out.println("Клиент отключён. Осталось клиентов: " + clients.size());
    }

    // МЕТОД Проверка дубликата имени
    public boolean checkName(String name) {
        for (ClientHandler client : clients) {
            if (name.equals(client.getName())) {
                return true;
            }
        }
        return false;
    }
}
