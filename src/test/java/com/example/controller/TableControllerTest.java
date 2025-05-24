package com.example.controller;

import com.example.entity.RestaurantTable;
import com.example.service.TableService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TableController.class)
public class TableControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableService tableService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllTables() throws Exception {
        Mockito.when(tableService.getAllTables()).thenReturn(List.of(
                new RestaurantTable(1L, "Table1", 4, "Indoor", true)
        ));

        mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Table1"));
    }

    @Test
    public void testCreateTable() throws Exception {
        RestaurantTable table = new RestaurantTable(1L, "Table2", 2, "Outdoor", true);

        Mockito.when(tableService.createTable(any(RestaurantTable.class))).thenReturn(table);

        mockMvc.perform(post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(table)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Table2"));
    }
}