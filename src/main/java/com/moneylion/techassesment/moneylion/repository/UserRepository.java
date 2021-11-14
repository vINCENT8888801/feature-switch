package com.moneylion.techassesment.moneylion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moneylion.techassesment.moneylion.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public User findByEmail(String email);

}
