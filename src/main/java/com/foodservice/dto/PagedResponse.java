package com.foodservice.dto;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponse<T> {
    private List<T> items;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int elementsInCurrentPage;
}