package com.upverasmusproject.ro.recsysrestapi.entities;

import com.upverasmusproject.ro.recsysrestapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM User m WHERE m.token = :token  ", nativeQuery = true)
    User findUserByToken(@Param("token") String token);

}
