package com.ecommerce.app.repository;

import com.ecommerce.app.entities.Order;
import com.ecommerce.app.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

//    @Query("FROM Orders o where o.user_id = :userId")
    List<Order> findAllByUserEntity_Id(Long userId);

}
