package com.foodservice.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.foodservice.entity.MenuItemsEntity;
import com.foodservice.repository.MenuItemsRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class MenuItemsDAO {

    @Autowired
    private MenuItemsRepository menuItemsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // ---------------- BASIC ----------------

    public MenuItemsEntity save(MenuItemsEntity item) {
        return menuItemsRepository.save(item);
    }

    public List<MenuItemsEntity> findAll() {
        return menuItemsRepository.findAll();
    }

    public MenuItemsEntity findById(Integer id) {
        return menuItemsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    public void deleteById(Integer id) {
        menuItemsRepository.deleteById(id);
    }

    // ---------------- RELATION ----------------

    public List<MenuItemsEntity> findByRestaurantId(Integer restaurantId) {
        return menuItemsRepository.findByRestaurantRestaurantId(restaurantId);
    }

    public List<MenuItemsEntity> findItemsByOrderId(Integer orderId) {
        return menuItemsRepository.findItemsByOrderId(orderId);
    }

    // ---------------- SEARCH (DYNAMIC JPQL) ----------------

    public List<MenuItemsEntity> searchItems(String name, Double minPrice,
                                             Double maxPrice, Integer restaurantId) {

        StringBuilder jpql = new StringBuilder("SELECT m FROM MenuItemsEntity m WHERE 1=1");

        if (name != null)
            jpql.append(" AND LOWER(m.itemName) LIKE LOWER(CONCAT('%', :name, '%'))");

        if (minPrice != null)
            jpql.append(" AND m.itemPrice >= :minPrice");

        if (maxPrice != null)
            jpql.append(" AND m.itemPrice <= :maxPrice");

        if (restaurantId != null)
            jpql.append(" AND m.restaurant.restaurantId = :restaurantId");

        TypedQuery<MenuItemsEntity> query =
                entityManager.createQuery(jpql.toString(), MenuItemsEntity.class);

        if (name != null)
            query.setParameter("name", name);

        if (minPrice != null)
            query.setParameter("minPrice", minPrice);

        if (maxPrice != null)
            query.setParameter("maxPrice", maxPrice);

        if (restaurantId != null)
            query.setParameter("restaurantId", restaurantId);

        return query.getResultList();
    }

    // ---------------- BULK ----------------

    public List<MenuItemsEntity> saveAll(List<MenuItemsEntity> items) {
        return menuItemsRepository.saveAll(items);
    }

    public void deleteByRestaurant(Integer restaurantId) {
        menuItemsRepository.deleteByRestaurantId(restaurantId);
    }

    // ---------------- SORT ----------------

    public List<MenuItemsEntity> sortItems(String field, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(field).descending() :
                Sort.by(field).ascending();

        return menuItemsRepository.findAll(sort);
    }

    // ---------------- PAGINATION ----------------

    public Page<MenuItemsEntity> getPagedItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return menuItemsRepository.findAll(pageable);
    }

    // ---------------- FULL DYNAMIC FILTER ----------------

    public List<MenuItemsEntity> dynamicFilter(Map<String, Object> filters) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MenuItemsEntity> cq = cb.createQuery(MenuItemsEntity.class);
        Root<MenuItemsEntity> root = cq.from(MenuItemsEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (filters.containsKey("itemName")) {
            predicates.add(cb.like(
                    cb.lower(root.get("itemName")),
                    "%" + filters.get("itemName").toString().toLowerCase() + "%"
            ));
        }

        if (filters.containsKey("minPrice")) {
            predicates.add(cb.greaterThanOrEqualTo(
                    root.get("itemPrice"),
                    Double.valueOf(filters.get("minPrice").toString())
            ));
        }

        if (filters.containsKey("maxPrice")) {
            predicates.add(cb.lessThanOrEqualTo(
                    root.get("itemPrice"),
                    Double.valueOf(filters.get("maxPrice").toString())
            ));
        }

        if (filters.containsKey("restaurantId")) {
            predicates.add(cb.equal(
                    root.get("restaurant").get("restaurantId"),
                    filters.get("restaurantId")
            ));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getResultList();
    }
}