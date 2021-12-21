package com.document.document.repository;

import com.document.document.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByUid(Integer id);

}
