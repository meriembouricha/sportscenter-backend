package com.ecomerce.sportscenter.repository;

import com.ecomerce.sportscenter.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
    boolean existsByUsername(String username);

    @Query("SELECT u FROM AppUser u JOIN u.roles r WHERE r = 'ADMIN'")
    List<AppUser> findAllAdmins();

}
