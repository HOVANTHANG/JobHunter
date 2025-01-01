package com.example.Jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Jobhunter.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
