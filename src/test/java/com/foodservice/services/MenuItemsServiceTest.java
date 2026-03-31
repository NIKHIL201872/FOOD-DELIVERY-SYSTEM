package com.foodservice.services;

import com.foodservice.DAO.MenuItemsDAO;
import com.foodservice.dto.MenuItemWithRating;
import com.foodservice.dto.MenuItems;
import com.foodservice.dto.PagedResponse;
import com.foodservice.entity.MenuItemsEntity;
import com.foodservice.entity.RestaurantsEntity;
import com.foodservice.exception.MenuItemNotFoundException;
import com.foodservice.exception.ResourceConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MenuItemsServiceTest {

    @Mock
    private MenuItemsDAO menuItemsDAO;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private MenuItemsService service;

    private MenuItems dto;
    private MenuItemsEntity entity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dto = new MenuItems(1, "Burger", "Cheese Burger", 199.0, 10);

        RestaurantsEntity restaurant = new RestaurantsEntity();
        restaurant.setRestaurantId(10);

        entity = new MenuItemsEntity();
        entity.setItemId(1);
        entity.setItemName("Burger");
        entity.setItemDescription("Cheese Burger");
        entity.setItemPrice(new BigDecimal("199.00"));
        entity.setRestaurant(restaurant);
    }

    @Test
    void saveItem_returnsSuccessMessage() {
        when(menuItemsDAO.existsById(1)).thenReturn(false);
        when(mapper.map(dto, MenuItemsEntity.class)).thenReturn(entity);
        when(menuItemsDAO.save(any(MenuItemsEntity.class))).thenReturn(entity);

        String result = service.saveItem(dto);

        assertEquals("Menu item inserted successfully with ID: 1", result);
        verify(menuItemsDAO).save(any(MenuItemsEntity.class));
    }

    @Test
    void saveItem_throwsConflictWhenIdExists() {
        when(menuItemsDAO.existsById(1)).thenReturn(true);

        assertThrows(ResourceConflictException.class, () -> service.saveItem(dto));
        verify(menuItemsDAO, never()).save(any());
    }

    @Test
    void getItemById_returnsDto() {
        when(menuItemsDAO.findById(1)).thenReturn(entity);
        when(mapper.map(entity, MenuItems.class)).thenReturn(new MenuItems(1, "Burger", "Cheese Burger", 199.0, null));

        MenuItems result = service.getItemById(1);

        assertEquals(1, result.getItemId());
        assertEquals(10, result.getRestaurantId());
    }

    @Test
    void getItemById_throwsBadRequestForInvalidId() {
        assertThrows(MenuItemNotFoundException.class, () -> service.getItemById(0));
    }

    @Test
    void updateItem_returnsSuccessMessage() {
        when(menuItemsDAO.findById(1)).thenReturn(entity);
        when(menuItemsDAO.save(entity)).thenReturn(entity);

        String result = service.updateItem(1, dto);

        assertEquals("Menu item updated successfully for ID: 1", result);
        assertEquals("Burger", entity.getItemName());
        assertEquals(new BigDecimal("199.0"), entity.getItemPrice());
    }

    @Test
    void updatePrice_returnsSuccessMessage() {
        when(menuItemsDAO.findById(1)).thenReturn(entity);
        when(menuItemsDAO.save(entity)).thenReturn(entity);

        String result = service.updatePrice(1, 250.0);

        assertEquals("Menu item price updated successfully for ID: 1", result);
        assertEquals(new BigDecimal("250.0"), entity.getItemPrice());
    }

    @Test
    void deleteItem_returnsSuccessMessage() {
        String result = service.deleteItem(1);

        assertEquals("Menu item deleted successfully for ID: 1", result);
        verify(menuItemsDAO).deleteById(1);
    }

    @Test
    void getByRestaurantId_returnsList() {
        when(menuItemsDAO.findByRestaurantId(10)).thenReturn(List.of(entity));
        when(mapper.map(entity, MenuItems.class)).thenReturn(new MenuItems(1, "Burger", "Cheese Burger", 199.0, null));

        List<MenuItems> result = service.getByRestaurantId(10);

        assertEquals(1, result.size());
        assertEquals(10, result.get(0).getRestaurantId());
    }

    @Test
    void getByPriceRange_returnsList() {
        when(menuItemsDAO.findByPriceRange(100.0, 300.0)).thenReturn(List.of(entity));
        when(mapper.map(entity, MenuItems.class)).thenReturn(new MenuItems(1, "Burger", "Cheese Burger", 199.0, null));

        List<MenuItems> result = service.getByPriceRange(100.0, 300.0);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getItemId());
    }

    @Test
    void saveAll_returnsSuccessMessage() {
        when(mapper.map(dto, MenuItemsEntity.class)).thenReturn(entity);
        when(menuItemsDAO.saveAll(List.of(entity))).thenReturn(List.of(entity));

        String result = service.saveAll(List.of(dto));

        assertEquals("1 menu item(s) inserted successfully", result);
        verify(menuItemsDAO).saveAll(List.of(entity));
    }

    @Test
    void sortItems_returnsSortedList() {
        when(menuItemsDAO.sortItems("itemPrice", "asc")).thenReturn(List.of(entity));
        when(mapper.map(entity, MenuItems.class)).thenReturn(new MenuItems(1, "Burger", "Cheese Burger", 199.0, null));

        List<MenuItems> result = service.sortItems("itemPrice", "asc");

        assertEquals(1, result.size());
        verify(menuItemsDAO).sortItems("itemPrice", "asc");
    }

    @Test
    void getPagedItems_returnsPagedResponse() {
        Page<MenuItemsEntity> page = new PageImpl<>(List.of(entity));
        when(menuItemsDAO.getPagedItems(0, 10)).thenReturn(page);
        when(mapper.map(entity, MenuItems.class)).thenReturn(new MenuItems(1, "Burger", "Cheese Burger", 199.0, null));

        PagedResponse<MenuItems> result = service.getPagedItems(0, 10);

        assertEquals(1, result.getItems().size());
        assertEquals(0, result.getCurrentPage());
        assertEquals(1, result.getElementsInCurrentPage());
    }

    @Test
    void getByName_returnsList() {
        when(menuItemsDAO.findByName("Burger")).thenReturn(List.of(entity));
        when(mapper.map(entity, MenuItems.class)).thenReturn(new MenuItems(1, "Burger", "Cheese Burger", 199.0, null));

        List<MenuItems> result = service.getByName("Burger");

        assertEquals(1, result.size());
        assertEquals("Burger", result.get(0).getItemName());
        verify(menuItemsDAO).findByName("Burger");
    }

    @Test
    void getItemsByCustomer_returnsList() {
        when(menuItemsDAO.findItemsByCustomer(101)).thenReturn(List.of(entity));
        when(mapper.map(entity, MenuItems.class)).thenReturn(new MenuItems(1, "Burger", "Cheese Burger", 199.0, null));

        List<MenuItems> result = service.getItemsByCustomer(101);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getItemId());
        verify(menuItemsDAO).findItemsByCustomer(101);
    }

    @Test
    void getItemsByOrderDate_returnsList() {
        when(menuItemsDAO.findItemsByOrderDate("2026-03-30")).thenReturn(List.of(entity));
        when(mapper.map(entity, MenuItems.class)).thenReturn(new MenuItems(1, "Burger", "Cheese Burger", 199.0, null));

        List<MenuItems> result = service.getItemsByOrderDate("2026-03-30");

        assertEquals(1, result.size());
        assertEquals("Burger", result.get(0).getItemName());
        verify(menuItemsDAO).findItemsByOrderDate("2026-03-30");
    }

    @Test
    void getItemsByRating_returnsList() {
        when(menuItemsDAO.findItemsByRating(4)).thenReturn(List.of(entity));
        when(mapper.map(entity, MenuItems.class)).thenReturn(new MenuItems(1, "Burger", "Cheese Burger", 199.0, 10));

        List<MenuItemWithRating> result = service.getItemsByRating(4);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getItemId());
        assertEquals(4, result.get(0).getRating());
        assertEquals(10, result.get(0).getRestaurantId());
        verify(menuItemsDAO).findItemsByRating(4);
    }
}
