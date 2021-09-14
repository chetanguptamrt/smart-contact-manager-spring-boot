package com.contact.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.contact.entity.User;
import com.contact.helper.Message;
import com.contact.repository.UserRepository;
import com.contact.service.ForgotPasswordService;

@Controller
public class MainController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder; 
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ForgotPasswordService forgotPasswordService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		model.addAttribute("title", "Home | Smart Contact");
		return "home";
	}

	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public String about(Model model) {
		model.addAttribute("title", "About | Smart Contact");
		return "about";
	}
	
	@RequestMapping(value = "/signUp", method = RequestMethod.GET)
	public String signUp(Model model) {
		model.addAttribute("title", "Sign up | Smart Contact");
		model.addAttribute("user", new User());
		return "signUp";
	}
	
	@RequestMapping(value = "/doRegistration", method = RequestMethod.POST)
	public String registerUser(
			@Valid @ModelAttribute("user") User user, BindingResult bindingResult,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
			Model model,
			HttpSession session
			) {
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Please provide valid details!","alert-danger"));
			return "signUp";
		}
		
		try {
			if(!agreement) {
				model.addAttribute("user", user);
				session.setAttribute("message", new Message("Please check terms and conditions!","alert-danger"));
			} else {
				if(this.userRepository.existsByEmail(user.getEmail())) {
					model.addAttribute("user", user);
					session.setAttribute("message", new Message("User email already registered","alert-danger"));
				} else {
					user.setPassword(this.passwordEncoder.encode(user.getPassword()));
					user.setEnabled(true);
					user.setRole("ROLE_USER");
					user.setImageUrl("default.jpg");
					this.userRepository.save(user);
					model.addAttribute("user", new User());
					session.setAttribute("message", new Message("User registration successfully done!","alert-success"));	
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong!","alert-danger"));
		}
		
		return "signUp";
	}

	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public String login(Model model) {
		model.addAttribute("title", "Login | Smart Contact");
		return "signin";
	}
	
	@RequestMapping(value = "/forgot", method = RequestMethod.GET)
	public String forgotPassword(Model model) {
		model.addAttribute("title", "Forgot Password | Smart Contact");
		return "forgot";
	}
	
	@RequestMapping(value = "/forgotOTP", method = RequestMethod.POST)
	@ResponseBody
	public String forgotOTP(@RequestParam("email") String email) {
		String sendOtpToEmail = this.forgotPasswordService.sendOtpToEmail(email);
		return sendOtpToEmail;
	}
	
	@RequestMapping(value = "/forgotPasswordConfirm", method = RequestMethod.POST)
	@ResponseBody
	public String forgotPasswordConfirm(
			@RequestParam("oemail") String email,
			@RequestParam("otp") String otp,
			@RequestParam("password") String password) {
		System.out.println(email+otp);
		String done = this.forgotPasswordService.forgotPasswordConfirm(email, otp, this.passwordEncoder.encode(password));
		return done;
	}
	
}
