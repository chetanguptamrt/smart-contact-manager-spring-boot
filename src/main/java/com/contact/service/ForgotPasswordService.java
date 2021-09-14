package com.contact.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.contact.entity.Forgot;
import com.contact.entity.Mail;
import com.contact.entity.User;
import com.contact.repository.ForgotRepository;
import com.contact.repository.UserRepository;

@Service
public class ForgotPasswordService {

	@Autowired
    private JavaMailSender emailSender;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired 
	private ForgotRepository forgotRepository;
	
	private String sendMail(Mail mailDetail) {
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom("noreply@gmail.com");
        message.setTo(mailDetail.getToMail()); 
        message.setSubject(mailDetail.getSubject()); 
        message.setText(mailDetail.getBody());
        emailSender.send(message);
		return "done";
	}

	public String sendOtpToEmail(String email) {
		if(this.userRepository.existsByEmail(email)) {
			List<Forgot> list = this.forgotRepository.getByMail(email);
			this.forgotRepository.deleteAll(list);
			String otp = String.valueOf(this.getOTP());
			Forgot forgot = new Forgot();
			forgot.setMail(email);
			forgot.setOtp(otp);
			this.forgotRepository.save(forgot);
			Mail mail = new Mail();
			mail.setToMail(email);
			mail.setSubject("Forgot Password");
			mail.setBody("Hello,\n\n"
                    + "Your verification code is : "+otp+"\n"
                    + "If you are having any issue with your account, please don't hesitate to contact us.\n\n"
                    + "Thanks!\n"
                    + "Smart Contact");
			this.sendMail(mail);
			return "done";
		} else {
			return "noUser";
		}
	}

    private int getOTP(){
       return (int)(Math.random()*(999999-111111+1)+111111);
    }

	public String forgotPasswordConfirm(String email, String otp, String encode) {
		if(this.forgotRepository.existsByMailAndOtp(email, otp)) {
			List<Forgot> list = this.forgotRepository.getByMail(email);
			this.forgotRepository.deleteAll(list);
			User user = this.userRepository.getUserByUsername(email);
			user.setPassword(encode);
			this.userRepository.save(user);
			return "done";
		} else { 
			return "invalid";
		}
	}

}
