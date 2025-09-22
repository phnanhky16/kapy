package com.KapybaraWeb.kapyweb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.KapybaraWeb.kapyweb.entity.User;
import com.KapybaraWeb.kapyweb.enums.Role;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findAllByRole(Role role);

    //    List<User> findAllByRole(Set<Role> role);

    //    List<User> findAllByRoleName(String roleName);
}
