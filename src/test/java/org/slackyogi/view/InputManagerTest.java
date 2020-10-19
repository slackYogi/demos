package org.slackyogi.view;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

class InputManagerTest {

    @DisplayName("getIntInput() Scanner received an int number and returns it.")
    @Test
    public void getIntInput_ScannerReceivedInt_ReturnsIt() {
        String testInput = "3";
        InputStream in = new ByteArrayInputStream(testInput.getBytes());
        System.setIn(in);

        int i = InputManager.getIntInput();

        Assertions.assertThat(i).isEqualTo(Integer.valueOf(testInput));
    }

    @DisplayName("getIntInput() Scanner didn't receive an int number and throws IllegalArgumentException")
    @Test
    public void getIntInput_ScannerReceivedNotInt_ThrowsIllegalArgumentException() {
        String testInput = "abc";
        InputStream in = new ByteArrayInputStream(testInput.getBytes());
        System.setIn(in);

        Assertions.assertThatThrownBy(InputManager::getIntInput)
                .isInstanceOf(IllegalArgumentException.class);
    }

}