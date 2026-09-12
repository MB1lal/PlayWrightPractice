package com.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.ListAssert;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

/**
 * Custom assertion helpers using AssertJ for cleaner, more readable test assertions.
 * Provides fluent API with better error messages.
 */
@Slf4j
public class AssertionHelper {
    
    /**
     * Assert HTTP status code with descriptive message
     */
    public static AbstractIntegerAssert<?> assertStatusCode(int actualStatus, String context) {
        log.info("Asserting status code for context: {}", context);
        return assertThat(actualStatus)
                .as("HTTP Status Code for: " + context);
    }
    
    /**
     * Assert HTTP status is success (200-299)
     */
    public static void assertSuccessStatus(int actualStatus, String context) {
        assertThat(actualStatus)
                .as("Expected success status (200-299) for: " + context)
                .isBetween(200, 299);
        log.debug("Status code {} is in success range for {}", actualStatus, context);
    }
    
    /**
     * Assert HTTP status is 200 OK
     */
    public static void assertOkStatus(int actualStatus, String context) {
        assertStatusCode(actualStatus, context).isEqualTo(200);
        log.debug("Status code is 200 OK for {}", context);
    }
    
    /**
     * Assert HTTP status is 201 Created
     */
    public static void assertCreatedStatus(int actualStatus, String context) {
        assertStatusCode(actualStatus, context).isEqualTo(201);
        log.debug("Status code is 201 Created for {}", context);
    }
    
    /**
     * Assert HTTP status is 400 Bad Request
     */
    public static void assertBadRequestStatus(int actualStatus, String context) {
        assertStatusCode(actualStatus, context).isEqualTo(400);
        log.debug("Status code is 400 Bad Request for {}", context);
    }
    
    /**
     * Assert HTTP status is 401 Unauthorized
     */
    public static void assertUnauthorizedStatus(int actualStatus, String context) {
        assertStatusCode(actualStatus, context).isEqualTo(401);
        log.debug("Status code is 401 Unauthorized for {}", context);
    }
    
    /**
     * Assert HTTP status is 404 Not Found
     */
    public static void assertNotFoundStatus(int actualStatus, String context) {
        assertStatusCode(actualStatus, context).isEqualTo(404);
        log.debug("Status code is 404 Not Found for {}", context);
    }
    
    /**
     * Assert HTTP status is 500 Internal Server Error
     */
    public static void assertServerErrorStatus(int actualStatus, String context) {
        assertStatusCode(actualStatus, context).isEqualTo(500);
        log.debug("Status code is 500 Server Error for {}", context);
    }
    
    /**
     * Assert string is not null or empty
     */
    public static AbstractStringAssert<?> assertNotEmpty(String actual, String fieldName) {
        log.debug("Asserting that field '{}' is not empty", fieldName);
        return assertThat(actual)
                .as("Field '" + fieldName + "' should not be null or empty")
                .isNotNull()
                .isNotEmpty();
    }
    
    /**
     * Assert string matches expected value (case-insensitive)
     */
    public static void assertEqualsIgnoreCase(String actual, String expected, String fieldName) {
        assertThat(actual)
                .as("Field '" + fieldName + "' should equal '" + expected + "' (ignoring case)")
                .isNotNull()
                .isEqualToIgnoringCase(expected);
        log.debug("Field '{}' matches expected value (case-insensitive)", fieldName);
    }
    
    /**
     * Assert string contains substring
     */
    public static void assertContains(String actual, String substring, String context) {
        assertThat(actual)
                .as("Expected '" + context + "' to contain '" + substring + "'")
                .isNotNull()
                .contains(substring);
        log.debug("String contains expected substring in {}", context);
    }
    
    /**
     * Assert list is not empty
     */
    public static <T> ListAssert<T> assertNotEmpty(List<T> actual, String listName) {
        log.debug("Asserting that list '{}' is not empty", listName);
        return assertThat(actual)
                .as("List '" + listName + "' should not be empty")
                .isNotEmpty();
    }
    
    /**
     * Assert list has expected size
     */
    public static <T> void assertSize(List<T> actual, int expectedSize, String listName) {
        assertThat(actual)
                .as("List '" + listName + "' should have size " + expectedSize)
                .hasSize(expectedSize);
        log.debug("List '{}' has expected size: {}", listName, expectedSize);
    }
    
    /**
     * Assert list contains item
     */
    public static <T> void assertContains(List<T> actual, T item, String listName) {
        assertThat(actual)
                .as("List '" + listName + "' should contain item: " + item)
                .contains(item);
        log.debug("List '{}' contains expected item", listName);
    }
    
    /**
     * Assert object is not null
     */
    public static <T> void assertNotNull(T actual, String objectName) {
        assertThat(actual)
                .as("Object '" + objectName + "' should not be null")
                .isNotNull();
        log.debug("Object '{}' is not null", objectName);
    }
    
    /**
     * Assert object is null
     */
    public static <T> void assertNull(T actual, String objectName) {
        assertThat(actual)
                .as("Object '" + objectName + "' should be null")
                .isNull();
        log.debug("Object '{}' is null", objectName);
    }
    
    /**
     * Assert condition is true
     */
    public static void assertTrue(boolean condition, String message) {
        assertThat(condition)
                .as(message)
                .isTrue();
        log.debug("Assertion passed: {}", message);
    }
    
    /**
     * Assert condition is false
     */
    public static void assertFalse(boolean condition, String message) {
        assertThat(condition)
                .as(message)
                .isFalse();
        log.debug("Assertion passed: {}", message);
    }
}
