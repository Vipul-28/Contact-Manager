package com.smart.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.smart.entities.User;

public interface UserDao extends CrudRepository<User, Integer>{
	@Query("select u From User u WHERE u.email=:email")
	public User getUserByUserName(@Param("email") String email);
}
