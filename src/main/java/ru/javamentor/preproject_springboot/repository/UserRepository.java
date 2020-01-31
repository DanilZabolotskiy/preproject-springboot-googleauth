package ru.javamentor.preproject_springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.javamentor.preproject_springboot.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("from User u where u.login = :login")
    User findUserByLogin(@Param("login") String login);

    @Query("from User u where u.googleID = :googleID")
    User findUserByGoogleID(@Param("googleID") String googleID);

}
