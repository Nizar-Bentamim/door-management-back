package com.startup.doormanagement.repository;

import com.startup.doormanagement.entity.Access;
import com.startup.doormanagement.entity.Door;
import com.startup.doormanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccessRepository extends JpaRepository<Access, Long> {
    Optional<Access> findByUserAndDoor(User user, Door door);
    boolean existsByUserAndDoor(User user, Door door);
    void deleteByUserAndDoor(User user, Door door);

    List<Access> findByDoorId(Long doorId);

    List<Access> findByUserId(Long userId);
}


