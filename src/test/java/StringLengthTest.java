import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StringLengthTest {

    @Test
    public void testStringLength() {
        String text = "Это текст длиной больше 15 символов";
        int expectedLength = 15;
        int actualLength = text.length();
        assertEquals("Длина строки должна быть больше или равна 15 символам", expectedLength, actualLength);
    }
}