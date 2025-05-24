package com.example.service;

import com.example.entity.RestaurantTable;

import com.example.exception.ResourceNotFoundException;
import com.example.repository.RestaurantTableRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableService {

    private final RestaurantTableRepository repository;

    public TableService(RestaurantTableRepository repository) {
        this.repository = repository;
    }

    public List<RestaurantTable> getAllTables() {
        return repository.findAll();
    }

    public RestaurantTable getTableById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id " + id));
    }

    public RestaurantTable createTable(RestaurantTable table) {
        return repository.save(table);
    }

    public RestaurantTable updateTable(Long id, RestaurantTable updated) {
        RestaurantTable table = getTableById(id);
        table.setName(updated.getName());
        table.setNumberOfSeats(updated.getNumberOfSeats());
        table.setArea(updated.getArea());
        table.setReservable(updated.isReservable());
        return repository.save(table);
    }

    public void deleteTable(Long id) {
        RestaurantTable table = getTableById(id);
        repository.delete(table);
    }
}