package com.ecomerce.sportscenter.repository;

import com.ecomerce.sportscenter.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<Product> findByQunatityLessThanEqual(int threshold);

    @Query("SELECT p FROM Product p where p.name LIKE %:keyword%")
    List<Product> searchByName(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p WHERE p.brand.id = :brandId")
    List<Product> searchByBrand(@Param("brandId") Integer brandId);
    @Query("SELECT p FROM Product p WHERE p.type.id = :typeId")
    List<Product> searchByType(@Param("typeId") Integer typeId);
    @Query("SELECT p FROM Product p WHERE p.brand.id = :brandId AND p.type.id = :typeId")
    List<Product> searchByBrandAndType(@Param("brandId") Integer brandId, @Param("typeId") Integer typeId);

    @Query("SELECT p FROM Product p WHERE p.brand.id = :brandId AND p.type.id = :typeId AND p.name LIKE %:keyword%")
    List<Product> searchByBrandTypeAndName(@Param("brandId") Integer brandId, @Param("typeId") Integer typeId, @Param("keyword") String keyword);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.qunatity = p.qunatity + :amount WHERE p.id = :productId")
    void incrementProductQuantity(@Param("productId") Integer productId, @Param("amount") Integer amount);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.qunatity = p.qunatity - :amount WHERE p.id = :productId AND p.qunatity >= :amount")
    void decrementProductQuantity(@Param("productId") Integer productId, @Param("amount") Integer amount);

    @Query("SELECT p FROM Product p WHERE p.id BETWEEN :start AND :end")
    List<Product> findAllByIdBetween(@Param("start") Integer start, @Param("end") Integer end);

}