package com.contact.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.contact.entity.Forgot;

@Repository
public interface ForgotRepository extends JpaRepository<Forgot, Integer>{

	public List<Forgot> getByMail(String email);
	
	public boolean existsByMailAndOtp(String email, String otp);
	
}
