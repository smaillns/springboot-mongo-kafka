package fr.test.test.compute.config.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtils {

    private static final String TEST_FILES_PATH = "src/test/resources/files/";

    private TestUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static byte[] readBytes(String fileName) throws IOException {
        return Files.readAllBytes(
                Paths.get(String.format(TEST_FILES_PATH + "%s", fileName)));
    }

}
