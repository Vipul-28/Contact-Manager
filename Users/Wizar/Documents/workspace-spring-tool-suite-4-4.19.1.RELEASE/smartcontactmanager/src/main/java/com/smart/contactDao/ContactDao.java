package com.smart.contactDao;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Contact;

public interface ContactDao extends JpaRepository<Contact,Integer> {
	@Query("from Contact as c where c.user.id=:userId")
	public List<Contact> getContactByUserName(@Param("userId") int userId);

	@Query("select c From Contact c WHERE c.phone=:phone")
	public Contact getSingleContactByUserName(@Param("phone") String phone);
}
