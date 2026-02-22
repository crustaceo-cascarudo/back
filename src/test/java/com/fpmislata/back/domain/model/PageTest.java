package com.fpmislata.back.domain.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageTest {

    @Test
    void constructor_withValidParams_shouldCreatePage() {
        List<String> data = List.of("a", "b");
        Page<String> page = new Page<>(data, 1, 5, 10);

        assertEquals(data, page.data());
        assertEquals(1, page.pageNumber());
        assertEquals(5, page.pageSize());
        assertEquals(10, page.totalElements());
        assertEquals(2, page.totalPages());
    }

    @Test
    void constructor_withPageNumberLessThanOne_shouldThrowException() {
        assertThrows(RuntimeException.class, () -> new Page<>(List.of("a"), 0, 5, 10));
    }

    @Test
    void constructor_withPageSizeZero_shouldThrowException() {
        assertThrows(RuntimeException.class, () -> new Page<>(List.of("a"), 1, 0, 10));
    }

    @Test
    void constructor_withNegativePageSize_shouldThrowException() {
        assertThrows(RuntimeException.class, () -> new Page<>(List.of("a"), 1, -1, 10));
    }

    @Test
    void constructor_withDataSizeGreaterThanPageSize_shouldThrowException() {
        assertThrows(RuntimeException.class, () -> new Page<>(List.of("a", "b", "c"), 1, 2, 10));
    }

    @Test
    void totalPages_shouldRoundUp() {
        Page<String> page = new Page<>(List.of("a"), 1, 3, 10);
        assertEquals(4, page.totalPages());
    }

    @Test
    void totalPages_withExactDivision_shouldNotRoundUp() {
        Page<String> page = new Page<>(List.of("a", "b"), 1, 2, 10);
        assertEquals(5, page.totalPages());
    }
}
