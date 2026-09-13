package com.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.ListAssert;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Small set of AssertJ-based assertion helpers with consistent logging.
 * Prefer Playwright web-first assertions
 * ({@code com.microsoft.playwright.assertions.PlaywrightAssertions}) for
 * element state, and use these helpers for plain values and collections.
 */
@Slf4j
public final class AssertionHelper {

    private AssertionHelper() {
    }

    /** Assert a string is not null or empty. */
    public static AbstractStringAssert<?> assertNotEmpty(String actual, String fieldName) {
        log.debug("Asserting field '{}' is not empty", fieldName);
        return assertThat(actual)
                .as("Field '%s' should not be null or empty", fieldName)
                .isNotNull()
                .isNotEmpty();
    }

    /** Assert two strings are equal, ignoring case. */
    public static void assertEqualsIgnoreCase(String actual, String expected, String fieldName) {
        assertThat(actual)
                .as("Field '%s' should equal '%s' (ignoring case)", fieldName, expected)
                .isNotNull()
                .isEqualToIgnoringCase(expected);
        log.debug("Field '{}' matches expected value (case-insensitive)", fieldName);
    }

    /** Assert a string contains a substring. */
    public static void assertContains(String actual, String substring, String context) {
        assertThat(actual)
                .as("Expected '%s' to contain '%s'", context, substring)
                .isNotNull()
                .contains(substring);
        log.debug("String contains expected substring in {}", context);
    }

    /** Assert a list is not empty. */
    public static <T> ListAssert<T> assertNotEmpty(List<T> actual, String listName) {
        log.debug("Asserting list '{}' is not empty", listName);
        return assertThat(actual)
                .as("List '%s' should not be empty", listName)
                .isNotEmpty();
    }

    /** Assert a list has the expected size. */
    public static <T> void assertSize(List<T> actual, int expectedSize, String listName) {
        assertThat(actual)
                .as("List '%s' should have size %d", listName, expectedSize)
                .hasSize(expectedSize);
        log.debug("List '{}' has expected size {}", listName, expectedSize);
    }

    /** Assert a list contains an item. */
    public static <T> void assertContains(List<T> actual, T item, String listName) {
        assertThat(actual)
                .as("List '%s' should contain item: %s", listName, item)
                .contains(item);
        log.debug("List '{}' contains expected item", listName);
    }

    /** Assert an object is not null. */
    public static <T> void assertNotNull(T actual, String objectName) {
        assertThat(actual)
                .as("Object '%s' should not be null", objectName)
                .isNotNull();
        log.debug("Object '{}' is not null", objectName);
    }

    /** Assert an object is null. */
    public static <T> void assertNull(T actual, String objectName) {
        assertThat(actual)
                .as("Object '%s' should be null", objectName)
                .isNull();
        log.debug("Object '{}' is null", objectName);
    }

    /** Assert a condition is true. */
    public static void assertTrue(boolean condition, String message) {
        assertThat(condition).as(message).isTrue();
        log.debug("Assertion passed: {}", message);
    }

    /** Assert a condition is false. */
    public static void assertFalse(boolean condition, String message) {
        assertThat(condition).as(message).isFalse();
        log.debug("Assertion passed: {}", message);
    }
}
