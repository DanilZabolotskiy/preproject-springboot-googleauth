package ru.javamentor.preproject_springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.javamentor.preproject_springboot.model.Role;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("from Role r where r.roleName = :roleName")
    Role findRoleByName(@Param("roleName") String roleName);

    @Query("from Role r where r.roleName in :roleName")
    Set<Role> findRolesByName(@Param("roleName") String... roleName);

}
