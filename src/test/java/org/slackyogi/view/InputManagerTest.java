package org.slackyogi.view;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

class InputManagerTest {
    @DisplayName("getIntInput() received an int number and returns it.")
    @Test
    public void getIntInput_ReceivedInt_ReturnsIt() {
        String testInput = "3";
        int i = 0;
        try (InputStream in = new ByteArrayInputStream(testInput.getBytes())) {
            System.setIn(in);

            i = InputManager.getIntInput().get();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Assertions.assertThat(i).isEqualTo(Integer.valueOf(testInput));
    }

    @DisplayName("getIntInput() didn't receive an int number and returns Optional.empty()")
    @Test
    public void getIntInput_ReceivedNotInt_ReturnsOptionalEmpty() {
        String testInput = "abc";
        Optional<Integer> possibleInt = Optional.empty();
        try (InputStream in = new ByteArrayInputStream(testInput.getBytes())) {
            System.setIn(in);

            possibleInt = InputManager.getIntInput();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Assertions.assertThat(possibleInt).isEqualTo(Optional.empty());
    }

    @DisplayName("getStringInput() received String input and returned it.")
    @Test
    public void getStringInput_ReceivedString_ReturnsIt() {
        String testInput = "abc";
        String input = "";
        try (InputStream in = new ByteArrayInputStream(testInput.getBytes())) {
            System.setIn(in);

            input = InputManager.getStringInput();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Assertions.assertThat(input).isEqualTo(testInput);
    }

    @DisplayName("getDoubleInput() received a double number and returns it.")
    @Test
    public void getDoubleInput_ReceivedDouble_ReturnsIt() {
        String testInput = "3.3";
        double d = 0;
        try (InputStream in = new ByteArrayInputStream(testInput.getBytes())) {
            System.setIn(in);

            d = InputManager.getDoubleInput().get();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Assertions.assertThat(d).isEqualTo(Double.valueOf(testInput));
    }

    @DisplayName("getDoubleInput() didn't receive a double number and returns Optional.empty().")
    @Test
    public void getDoubleInput_ReceivedNotDouble_ReturnsOptionalEmpty() {
        String testInput = "abc";
        Optional<Double> possibleDouble = Optional.empty();
        try (InputStream in = new ByteArrayInputStream(testInput.getBytes())) {
            System.setIn(in);

            possibleDouble = InputManager.getDoubleInput();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Assertions.assertThat(possibleDouble).isEqualTo(Optional.empty());
    }

}