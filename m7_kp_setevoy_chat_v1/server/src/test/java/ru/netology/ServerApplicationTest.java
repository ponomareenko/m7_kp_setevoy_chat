package ru.netology;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerApplicationTest {
    ServerApplication serverApplication;

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
        int expectedPort = 8080;

        // Act
        ServerApplication.saveSettings(settings, pathFile);

        // Assert
        assertEquals(expectedPort, Integer.parseInt(settings.get("port")));
    }
}
