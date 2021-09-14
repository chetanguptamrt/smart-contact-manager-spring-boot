package com.contact.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.contact.entity.Contact;
import com.contact.entity.User;
import com.contact.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		User user = this.userService.getUserByUsername(userName);
		model.addAttribute("user", user);
	}
	
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String dashboard(Model model) {
		model.addAttribute("title", "Dashboard | Smart Contact");
		return "normal/dashboard";
	}
	
	@RequestMapping(value = "/addContact", method = RequestMethod.GET)
	public String addContact(Model model) {
		model.addAttribute("title", "Add Contact | Smart Contact");
		model.addAttribute("contact", new Contact());
		return "normal/addContact";
	}
	
	@RequestMapping(value = "processContact", method = RequestMethod.POST)
	@ResponseBody
	public String saveContact(
			@ModelAttribute("contact") Contact contact,
			@RequestParam("image") MultipartFile file,
			Principal principal
			) {
		if(contact.getName().trim().equals("")) {
			return "noName";
		}
		
		User userByUsername = this.userService.getUserByUsername(principal.getName());
		contact.setUser(userByUsername);		
		String saveContact = this.userService.saveContact(contact, file, userByUsername);
		return saveContact;
	}

	@RequestMapping(value = "/viewContact", method = RequestMethod.GET)
	public RedirectView viewContactWithoutPageNo() {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/user/viewContact/0");
		return redirectView; 
	}
	
	@RequestMapping(value = "/viewContact/{page}", method = RequestMethod.GET)
	public String viewContact(@PathVariable("page") int pageNo, Model model, Principal principal) {
		User userByUsername = this.userService.getUserByUsername(principal.getName());
		Pageable pageable = PageRequest.of(pageNo, 10);
		Page<Contact> findContactByUser = this.userService.findContactByUser(userByUsername.getId(), pageable);
		model.addAttribute("title", "View Contact | Smart Contact");
		model.addAttribute("totalContact", findContactByUser.getTotalElements());
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("totalPage", findContactByUser.getTotalPages());
		model.addAttribute("contacts", findContactByUser.getContent());
		return "normal/viewContact";
	}

	@RequestMapping(value = "/showContact/{contactId}", method = RequestMethod.GET)
	public String showContact(@PathVariable("contactId") int cId, Model model, Principal principal) {
		String userName = principal.getName();
		User user = this.userService.getUserByUsername(userName);
		try {
			Contact contact = this.userService.getContactByUserIdAndContactId(cId, user.getId());
			model.addAttribute("contact", contact);
			if(contact==null) {
				return "redirect:/user/viewContact/0";
			}
			model.addAttribute("title", contact.getName());
		} catch (Exception e) {
			return "redirect:/user/viewContact/0";
		}
		return "normal/showContact";
	}
	
	@RequestMapping(value="/deleteContact/{contactId}", method = RequestMethod.POST)
	@ResponseBody
	public String deleteContact(@PathVariable("contactId") int cId, Principal principal) {
		String userName = principal.getName();
		User user = this.userService.getUserByUsername(userName);
		try {
			String status = this.userService.deleteContactByUserIdAndContactId(cId, user.getId());
			return status;
		} catch (Exception e) {
			return "no";
		}
	}

	@RequestMapping(value = "/editContact/{contactId}", method = RequestMethod.GET)
	public String editContact(@PathVariable int contactId, Model model, Principal principal) {
		model.addAttribute("title", "Edit Contact | Smart Contact");
		User user = this.userService.getUserByUsername(principal.getName());
		try {
			Contact contact = this.userService.getContactByUserIdAndContactId(contactId, user.getId());
			model.addAttribute("contact", contact);
		} catch (Exception e) {
			return "redirect:/user/viewContact/0";
		}
		return "normal/editContact";
	}
	
	@RequestMapping(value = "/updateContact", method = RequestMethod.POST)
	@ResponseBody
	public String updateContact(
			@ModelAttribute("contact") Contact contact,
			@RequestParam("image") MultipartFile file,
			Principal principal
			) {
		if(contact.getName().trim().equals("")) {
			return "noName";
		}
		User userByUsername = this.userService.getUserByUsername(principal.getName());
		contact.setUser(userByUsername);		
		String saveContact = this.userService.updateContact(contact, file, userByUsername);
		return saveContact;
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String viewProfile(Model model) {
		model.addAttribute("title", "Profile | Smart Contact");
		return "normal/profile";
	}
	
	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public String settings(Model model) {
		model.addAttribute("title", "Settings | Smart Contact");
		return "normal/settings";
	}
	
	@RequestMapping(value = "/changePassword", method=RequestMethod.POST)
	@ResponseBody
	public String changePassword(
			@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword,
			Principal principal
			) {
		User user = this.userService.getUserByUsername(principal.getName());
		String done = this.userService.changeUserPassword(oldPassword, newPassword, user);
		return done;
	}
	
	@RequestMapping(value = "/processProfilePhoto", method = RequestMethod.POST)
	@ResponseBody
	public String processProfilePhoto(
			@RequestParam("photo") MultipartFile file,
			Principal principal) {
		User userByUsername = this.userService.getUserByUsername(principal.getName());
		String done = this.userService.updateUserProfile(file, userByUsername);
		return done;
	}
	
	@RequestMapping(value = "/deleteAccount", method = RequestMethod.POST)
	@ResponseBody
	public String deleteAccount(Principal principal, HttpSession httpSession) {
		User user = this.userService.getUserByUsername(principal.getName());
		String done = this.userService.deleteAccount(user);
		if(done.equals(done)) {
			httpSession.invalidate();
		}
		return done;
	}
	
}
