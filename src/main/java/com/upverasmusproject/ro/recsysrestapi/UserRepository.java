package com.upverasmusproject.ro.recsysrestapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM User m WHERE m.token = :token  ", nativeQuery = true)
    public User findUserByToken(@Param("token") String token);

}
