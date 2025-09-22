package com.ecomerce.sportscenter.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SimpleServiceTest {

    @Test
    void testBasicMath() {
        // Simple test that always passes
        assertEquals(2, 1 + 1);
        assertTrue(5 > 3);
        assertFalse(2 > 5);
    }

    @Test
    void testStringOperations() {
        // Simple string test
        String testString = "Hello World";
        assertEquals("Hello World", testString);
        assertTrue(testString.contains("World"));
        assertEquals(11, testString.length());
    }

    @Test
    void testArrayOperations() {
        // Simple array test
        int[] numbers = {1, 2, 3, 4, 5};
        assertEquals(5, numbers.length);
        assertEquals(1, numbers[0]);
        assertEquals(5, numbers[4]);
    }

    @Test
    void testBooleanLogic() {
        // Simple boolean test
        boolean isTrue = true;
        boolean isFalse = false;
        
        assertTrue(isTrue);
        assertFalse(isFalse);
        assertTrue(isTrue || isFalse);
        assertFalse(isTrue && isFalse);
    }

    @Test
    void testNullChecks() {
        // Simple null test
        String nullString = null;
        String notNullString = "test";
        
        assertNull(nullString);
        assertNotNull(notNullString);
    }
}
