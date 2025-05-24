package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.ResponseEntity;

import com.example.entity.RestaurantTable;
import com.example.service.TableService;

public class TableControllerTest {


    private TableService tableService;
    private TableController controller;

    @BeforeEach
    void setUp() {
        tableService = mock(TableService.class);
        controller = new TableController(tableService);
    }

    @Test
    void testGetAllTables() {
        RestaurantTable table1 = new RestaurantTable(1L, "A1", 4, "Indoor", true);
        RestaurantTable table2 = new RestaurantTable(2L, "B2", 6, "Outdoor", false);

        when(tableService.getAllTables()).thenReturn(Arrays.asList(table1, table2));

        ResponseEntity<List<RestaurantTable>> response = controller.getAllTables();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("A1", response.getBody().get(0).getName());
        assertEquals("Outdoor", response.getBody().get(1).getArea());
    }

    @Test
    void testGetTableById() {
        RestaurantTable table = new RestaurantTable(1L, "A1", 4, "Indoor", true);

        when(tableService.getTableById(1L)).thenReturn(table);

        ResponseEntity<RestaurantTable> response = controller.getTable(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("A1", response.getBody().getName());
        assertTrue(response.getBody().isReservable());
    }

    @Test
    void testCreateTable() {
        RestaurantTable newTable = new RestaurantTable(null, "C3", 2, "Rooftop", true);
        RestaurantTable savedTable = new RestaurantTable(3L, "C3", 2, "Rooftop", true);

        when(tableService.createTable(newTable)).thenReturn(savedTable);

        ResponseEntity<RestaurantTable> response = controller.createTable(newTable);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(3L, response.getBody().getId());
        assertEquals("C3", response.getBody().getName());
        assertEquals("Rooftop", response.getBody().getArea());
    }

    @Test
    void testUpdateTable() {
        RestaurantTable updated = new RestaurantTable(1L, "UpdatedName", 5, "Patio", false);

        when(tableService.updateTable(eq(1L), any(RestaurantTable.class))).thenReturn(updated);

        ResponseEntity<RestaurantTable> response = controller.updateTable(1L, updated);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("UpdatedName", response.getBody().getName());
        assertEquals("Patio", response.getBody().getArea());
        assertFalse(response.getBody().isReservable());
    }

    @Test
    void testDeleteTable() {
        doNothing().when(tableService).deleteTable(1L);

        ResponseEntity<Void> response = controller.deleteTable(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(tableService, times(1)).deleteTable(1L);
    }
}
