package com.example.cardprocessing.repository;

import com.example.cardprocessing.entity.users.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {

    Roles findByName(String roleName);

    @Query(value = "select r.* from roles r inner join users_roles s on r.id = s.roles_id inner join users u on u.id = s.users_id where u.username = ?1", nativeQuery = true)
    Set<Roles> findAllByUsername(String username);
}

