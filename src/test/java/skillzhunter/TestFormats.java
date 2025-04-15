package skillzhunter;

import org.junit.jupiter.api.Test;
import skillzhunter.model.formatters.Formats;

import static org.junit.jupiter.api.Assertions.*;

public class TestFormats {

  @Test
  public void testValueOfMethod() {
    // Test the valueOf method
    assertEquals(Formats.JSON, Formats.valueOf("JSON"));
    assertEquals(Formats.XML, Formats.valueOf("XML"));
    assertEquals(Formats.CSV, Formats.valueOf("CSV"));
    assertEquals(Formats.PRETTY, Formats.valueOf("PRETTY"));

    // Test that invalid values throw IllegalArgumentException
    assertThrows(IllegalArgumentException.class, () -> Formats.valueOf("INVALID"));
  }
}
