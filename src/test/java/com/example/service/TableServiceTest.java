package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.entity.RestaurantTable;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.RestaurantTableRepository;


@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private RestaurantTableRepository repository;

    @InjectMocks
    private TableService service;

    @Test
    void testGetAllTables() {
        List<RestaurantTable> mockTables = Arrays.asList(
                new RestaurantTable(1L, "Table1", 4, "Indoor", true),
                new RestaurantTable(2L, "Table2", 2, "Outdoor", false)
        );
        when(repository.findAll()).thenReturn(mockTables);

        List<RestaurantTable> result = service.getAllTables();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetTableById_Found() {
        RestaurantTable table = new RestaurantTable(1L, "Table1", 4, "Indoor", true);
        when(repository.findById(1L)).thenReturn(Optional.of(table));

        RestaurantTable result = service.getTableById(1L);

        assertEquals("Table1", result.getName());
        verify(repository).findById(1L);
    }

    @Test
    void testGetTableById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getTableById(1L));
    }

    @Test
    void testCreateTable() {
        RestaurantTable table = new RestaurantTable(null, "Table1", 4, "Indoor", true);
        RestaurantTable saved = new RestaurantTable(1L, "Table1", 4, "Indoor", true);
        when(repository.save(table)).thenReturn(saved);

        RestaurantTable result = service.createTable(table);

        assertEquals(1L, result.getId());
        assertEquals("Table1", result.getName());
        verify(repository).save(table);
    }

    @Test
    void testUpdateTable_Success() {
        RestaurantTable existing = new RestaurantTable(1L, "Old", 2, "Outdoor", false);
        RestaurantTable updated = new RestaurantTable(null, "New", 4, "Indoor", true);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(RestaurantTable.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RestaurantTable result = service.updateTable(1L, updated);

        assertEquals("New", result.getName());
        assertEquals(4, result.getNumberOfSeats());
        assertEquals("Indoor", result.getArea());
        assertTrue(result.isReservable());
    }

    @Test
    void testUpdateTable_NotFound() {
        RestaurantTable updated = new RestaurantTable(null, "New", 4, "Indoor", true);
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.updateTable(1L, updated));
    }

    @Test
    void testDeleteTable_Success() {
        RestaurantTable table = new RestaurantTable(1L, "Table1", 4, "Indoor", true);
        when(repository.findById(1L)).thenReturn(Optional.of(table));

        service.deleteTable(1L);

        verify(repository).delete(table);
    }

    @Test
    void testDeleteTable_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.deleteTable(1L));
    }
}