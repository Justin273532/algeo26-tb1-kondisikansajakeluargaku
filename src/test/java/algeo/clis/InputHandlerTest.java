package algeo.clis;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

class InputHandlerTest {
    @Test void parsesIntegersAndDecimalDotOrComma() {
        InputHandler input = new InputHandler(new ByteArrayInputStream("12\n1.25\n2,5\n".getBytes(StandardCharsets.UTF_8)));
        assertEquals(12, input.inputInt());
        assertEquals(1.25, input.inputDouble(), 1e-12);
        assertEquals(2.5, input.inputDouble(), 1e-12);
        assertFalse(input.inputError_Int); assertFalse(input.inputError_Double);
    }
    @Test void marksMalformedNumbers() {
        InputHandler input = new InputHandler(new ByteArrayInputStream("abc\n1,2.3\n".getBytes(StandardCharsets.UTF_8)));
        input.inputInt(); assertTrue(input.inputError_Int);
        input.inputError_Int = false;
        input.inputDouble(); assertTrue(input.inputError_Double);
    }
}
