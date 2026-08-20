package ru.netology;

import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientApplicationTest {
    ClientApplication clientApplication;

    @BeforeAll
    public static void beforeAll() {
        System.out.println("beforeAll");
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("afterAll");
    }

    @Test
    public void saveSettingsTest() {
        System.out.println("saveSettingsTest");

        // Arrange
        TreeMap<String, String> settings = new TreeMap<>();
        String pathFile = "src/test/java/ru/netology/settingsTest.txt";
        String expectedLocalhost = "localhost";
        int expectedPort = 8080;

        // Act
        ClientApplication.saveSettings(settings, pathFile);

        // Assert
        assertEquals(expectedPort, Integer.parseInt(settings.get("port")));
        assertEquals(expectedLocalhost, settings.get("host"));
    }

    @Test
    public void logMessageTest() {
        System.out.println("logMessageTest");

        // Arrange
        String path = "src/test/java/ru/netology/fileTest.log";
        String expected = "[19.08.2026 17:21:17] maria: privet[19.08.2026 17:25:17] danil: hello";

        // Act
        ClientApplication.logMessage("[19.08.2026 17:21:17] maria: privet", path);
        ClientApplication.logMessage("[19.08.2026 17:25:17] danil: hello", path);

        // Assert
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
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
