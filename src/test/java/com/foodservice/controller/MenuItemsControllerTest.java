package com.foodservice.controller;

import com.foodservice.dto.MenuItemWithRating;
import com.foodservice.dto.MenuItems;
import com.foodservice.dto.PagedResponse;
import com.foodservice.services.MenuItemsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MenuItemsControllerTest {

    @Mock
    private MenuItemsService menuItemsService;

    @InjectMocks
    private MenuItemsController menuItemsController;

    private MenuItems sampleItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleItem = new MenuItems(1, "Burger", "Cheese burger", 199.0, 10);
    }

    @Test
    void createItem_returnsCreated() {
        when(menuItemsService.saveItem(sampleItem)).thenReturn("Menu item inserted");

        ResponseEntity<String> response = menuItemsController.createItem(sampleItem);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Menu item inserted", response.getBody());
        verify(menuItemsService).saveItem(sampleItem);
    }

    

    @Test
    void getAllItems_returnsOk() {
        when(menuItemsService.getAllItems()).thenReturn(List.of(sampleItem));

        ResponseEntity<List<MenuItems>> response = menuItemsController.getAllItems();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(menuItemsService).getAllItems();
    }

    @Test
    void getItemById_returnsOk() {
        when(menuItemsService.getItemById(1)).thenReturn(sampleItem);

        ResponseEntity<MenuItems> response = menuItemsController.getItemById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getItemId());
        verify(menuItemsService).getItemById(1);
    }

    @Test
    void updateItem_returnsOk() {
        when(menuItemsService.updateItem(1, sampleItem)).thenReturn("Menu item updated");

        ResponseEntity<String> response = menuItemsController.updateItem(1, sampleItem);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Menu item updated", response.getBody());
        verify(menuItemsService).updateItem(1, sampleItem);
    }

    

    @Test
    void deleteItem_returnsOk() {
        when(menuItemsService.deleteItem(1)).thenReturn("Menu item deleted");

        ResponseEntity<String> response = menuItemsController.deleteItem(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Menu item deleted", response.getBody());
        verify(menuItemsService).deleteItem(1);
    }

    

    @Test
    void getByRestaurant_returnsOk() {
        when(menuItemsService.getByRestaurantId(10)).thenReturn(List.of(sampleItem));

        ResponseEntity<List<MenuItems>> response = menuItemsController.getByRestaurant(10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(menuItemsService).getByRestaurantId(10);
    }

    

    @Test
    void getByName_returnsOk() {
        when(menuItemsService.getByName("Burger")).thenReturn(List.of(sampleItem));

        ResponseEntity<List<MenuItems>> response = menuItemsController.getByName("Burger");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(menuItemsService).getByName("Burger");
    }

    @Test
    void getByPriceRange_returnsOk() {
        when(menuItemsService.getByPriceRange(100.0, 300.0)).thenReturn(List.of(sampleItem));

        ResponseEntity<List<MenuItems>> response = menuItemsController.getByPriceRange(100.0, 300.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(menuItemsService).getByPriceRange(100.0, 300.0);
    }

    @Test
    void getByRestaurantAndPrice_returnsOk() {
        when(menuItemsService.getByRestaurantAndPrice(10, 100.0, 300.0)).thenReturn(List.of(sampleItem));

        ResponseEntity<List<MenuItems>> response =
                menuItemsController.getByRestaurantAndPrice(10, 100.0, 300.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(menuItemsService).getByRestaurantAndPrice(10, 100.0, 300.0);
    }

    

    

    @Test
    void getTopSellingItems_returnsOk() {
        when(menuItemsService.getTopSellingItems()).thenReturn(List.of(sampleItem));

        ResponseEntity<List<MenuItems>> response = menuItemsController.getTopSellingItems();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(menuItemsService).getTopSellingItems();
    }

    @Test
    void getItemsByRating_returnsOk() {
        MenuItemWithRating withRating =
                new MenuItemWithRating(1, "Burger", "Cheese burger", 199.0, 10, 4);
        when(menuItemsService.getItemsByRating(4)).thenReturn(List.of(withRating));

        ResponseEntity<List<MenuItemWithRating>> response = menuItemsController.getItemsByRating(4);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(4, response.getBody().get(0).getRating());
        verify(menuItemsService).getItemsByRating(4);
    }

    @Test
    void getItemsByRestaurantAndRating_returnsOk() {
        MenuItemWithRating withRating =
                new MenuItemWithRating(1, "Burger", "Cheese burger", 199.0, 10, 4);
        when(menuItemsService.getItemsByRestaurantAndRating(10, 4)).thenReturn(List.of(withRating));

        ResponseEntity<List<MenuItemWithRating>> response =
                menuItemsController.getItemsByRestaurantAndRating(10, 4);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(4, response.getBody().get(0).getRating());
        verify(menuItemsService).getItemsByRestaurantAndRating(10, 4);
    }

    @Test
    void sortItems_returnsOk() {
        when(menuItemsService.sortItems("itemPrice", "asc")).thenReturn(List.of(sampleItem));

        ResponseEntity<List<MenuItems>> response = menuItemsController.sortItems("itemPrice", "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(menuItemsService).sortItems("itemPrice", "asc");
    }

    @Test
    void getPagedItems_returnsOk() {
        PagedResponse<MenuItems> paged =
                new PagedResponse<>(List.of(sampleItem), 0, 1, 1L, 1);
        when(menuItemsService.getPagedItems(0, 10)).thenReturn(paged);

        ResponseEntity<PagedResponse<MenuItems>> response = menuItemsController.getPagedItems(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getItems().size());
        verify(menuItemsService).getPagedItems(0, 10);
    }
}
