package com.contact.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Forgot {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int fid;
	
	private String mail;
	private String otp;

	public Forgot(int fid, String mail, String otp) {
		super();
		this.fid = fid;
		this.mail = mail;
		this.otp = otp;
	}

	public Forgot() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	@Override
	public String toString() {
		return "Forgot [fid=" + fid + ", mail=" + mail + ", otp=" + otp + "]";
	}
	
}
