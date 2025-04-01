package com.kitchensaver.backend.Repo;

import com.kitchensaver.backend.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import com.kitchensaver.backend.model.Role;

// connection to database to get user information
public interface UserRepo extends JpaRepository<Users, Long> {

    /**
     * Method to find a user by their email.
     *
     * @param email The user's email.
     * @return The user if found, or nothing if not found.
     */
    Optional<Users> findByEmail(String email);

    /**
     * Method to find a user by their username.
     *
     * @param username The user's username.
     * @return The user if found, or nothing if not found.
     */
    Optional<Users> findByUsername(String username);

    /**
     * Method to find all users who are not of ADMIN role.
     *
     * @param role The role to exclude.
     * @return A list of users who are not of the specified role.
     */
    List<Users> findAllByRoleNot(Role role);

}
