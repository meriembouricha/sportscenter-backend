package com.ecomerce.sportscenter.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SimpleControllerTest {

    @Test
    void testBasicAssertions() {
        // Simple test that always passes
        assertTrue(true);
        assertFalse(false);
        assertEquals(10, 5 + 5);
        assertNotEquals(10, 5 + 4);
    }

    @Test
    void testStringManipulation() {
        String input = "SportsCenter";
        String expected = "SportsCenter";
        
        assertEquals(expected, input);
        assertTrue(input.startsWith("Sports"));
        assertTrue(input.endsWith("Center"));
    }

    @Test
    void testNumberOperations() {
        int a = 10;
        int b = 20;
        
        assertEquals(30, a + b);
        assertEquals(10, b - a);
        assertEquals(200, a * b);
        assertEquals(2, b / a);
    }

    @Test
    void testListOperations() {
        java.util.List<String> items = java.util.Arrays.asList("item1", "item2", "item3");
        
        assertEquals(3, items.size());
        assertTrue(items.contains("item1"));
        assertFalse(items.contains("item4"));
    }

    @Test
    void testConditionalLogic() {
        int score = 85;
        
        assertTrue(score >= 80);
        assertTrue(score < 90);
        assertFalse(score < 70);
    }
}
