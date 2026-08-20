package ru.netology;

import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientHandlerTest {
    ClientHandler clientHandler;

    @BeforeAll
    public static void beforeAll() {
        System.out.println("beforeAll");
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("afterAll");
    }

    @Test
    public void logMessageTest() {
        System.out.println("logMessageTest");

        // Arrange
        String pathFile = "src/test/java/ru/netology/fileTest.log";
        String expected = "[19.08.2026 17:21:17] maria: privet[19.08.2026 17:25:17] danil: hello";

        // Act
        ClientHandler.logMessage("[19.08.2026 17:21:17]", "maria", "privet", pathFile);
        ClientHandler.logMessage("[19.08.2026 17:25:17]", "danil", "hello", pathFile);

        // Assert
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(pathFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

        assertEquals(expected, String.valueOf(stringBuilder));
    }
}
