package ru.netology;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChatServerTest {
    ChatServer chatServer;

    @BeforeAll
    public static void beforeAll() {
        System.out.println("beforeAll");
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("afterAll");
    }

    @Test
    public void checkNameTestTrue() {
        // Arrange
        chatServer = new ChatServer(0);
        ClientHandler clientHandler = Mockito.mock(ClientHandler.class);
        Mockito.when(clientHandler.getName()).thenReturn("maria");
        chatServer.addClient(clientHandler);

        // Act
        boolean result = chatServer.checkName("maria");

        // Assert
        assertTrue(result);
    }

    @Test
    public void checkNameTestFalse() {
        // Arrange
        chatServer = new ChatServer(0);
        ClientHandler clientHandler = Mockito.mock(ClientHandler.class);
        Mockito.when(clientHandler.getName()).thenReturn("maria");
        chatServer.addClient(clientHandler);

        // Act
        boolean result = chatServer.checkName("danil");

        // Assert
        assertFalse(result);
    }

    @Test
    void removeClientTest() {
        // Arrange
        chatServer = new ChatServer(0);

        ClientHandler clientHandler = Mockito.mock(ClientHandler.class);
        Mockito.when(clientHandler.getName()).thenReturn("maria");

        chatServer.addClient(clientHandler);
        assertTrue(chatServer.checkName("maria"));

        // Act
        chatServer.removeClient(clientHandler);

        // Assert
        assertFalse(chatServer.checkName("maria"));
    }

    @Test
    public void broadcastTest() {
        // Arrange
        String msg = "[19.08.2026 17:21:17] maria: privet";
        chatServer = new ChatServer(0);
        ClientHandler clientHandler = Mockito.mock(ClientHandler.class);
        chatServer.addClient(clientHandler);

        // Act
        chatServer.broadcast(msg);

        // Assert
        Mockito.verify(clientHandler).sendMessage(msg);
    }
}
