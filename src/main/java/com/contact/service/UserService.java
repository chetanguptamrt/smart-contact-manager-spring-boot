package com.contact.service;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.contact.entity.Contact;
import com.contact.entity.User;
import com.contact.repository.ContactRepository;
import com.contact.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder; 
	
	//return user object
	public User getUserByUsername(String username) {
		return this.userRepository.getUserByUsername(username);
	}
	
	// return fileError, done, invalidFile
	public String saveContact(Contact contact, MultipartFile file, User userByUsername) {
		if (!file.isEmpty()) {
			if(!file.getContentType().equals("image/jpeg")){
				return "invalidFile";
			}
		}
		contact.setImageUrl("default.jpg");
		Contact save = this.contactRepository.save(contact);
		if (!file.isEmpty()) {
			if(file.getContentType().equals("image/jpeg")){
				try {
					Files.copy(file.getInputStream(),
							Paths.get(new ClassPathResource("static").getFile().getAbsolutePath()+
									java.io.File.separator+"img"+
									java.io.File.separator+"profile"+
									java.io.File.separator+userByUsername.getName()+save.getCid()+".jpg"), 
							StandardCopyOption.REPLACE_EXISTING);
					contact.setImageUrl(userByUsername.getName()+save.getCid()+".jpg");
					this.contactRepository.save(contact);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					this.contactRepository.delete(save);
					return "fileError";
				}
			}
		}
		return "done";
	}

	//return pages for view contacts
	public Page<Contact> findContactByUser(int userId, Pageable pageable){
		return this.contactRepository.findContactByUser(userId, pageable);
	}
	
	// get contact by user id and contact id 
	public Contact getContactByUserIdAndContactId(int cId, int userId) {
		Contact contact = this.contactRepository.getById(cId);
		if(contact.getUser().getId()!=userId) {
			return null;
		}
		return contact;
	}

	//delete contact by user id and contact id 
	public String deleteContactByUserIdAndContactId(int cId, int userId) {
		Contact contact = this.contactRepository.getById(cId);
		if(contact.getUser().getId()==userId) {
			String imageUrl = contact.getImageUrl();
			if(!imageUrl.equals("default.jpg")) {
				try {
					File file = new File(new ClassPathResource("static").getFile().getAbsolutePath()+
							java.io.File.separator+"img"+
							java.io.File.separator+"profile"+
							java.io.File.separator+ imageUrl);
					file.delete();
				} catch (IOException e) {
				}
			}
			this.contactRepository.delete(contact); 
			return "done";
		}
		return "no";
	}
		
	// return fileError, done, invalidFile
	public String updateContact(Contact contact, MultipartFile file, User userByUsername) {
		if (!file.isEmpty()) {
			if(!file.getContentType().equals("image/jpeg")){
				return "invalidFile";
			}
		}
		if(contact.getImageUrl()==null) {
			contact.setImageUrl(this.contactRepository.getById(contact.getCid()).getImageUrl());
		}
		Contact save = this.contactRepository.save(contact);
		if (!file.isEmpty()) {
			if(file.getContentType().equals("image/jpeg")){
				try {
					// now save new image
					Files.copy(file.getInputStream(),
							Paths.get(new ClassPathResource("static").getFile().getAbsolutePath()+
									java.io.File.separator+"img"+
									java.io.File.separator+"profile"+
									java.io.File.separator+userByUsername.getName()+save.getCid()+".jpg"), 
							StandardCopyOption.REPLACE_EXISTING);
					contact.setImageUrl(userByUsername.getName()+save.getCid()+".jpg");
					this.contactRepository.save(contact);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "fileError";
				}
			}
		}
		return "done";
	}

	public List<Contact> findByNameContainingAndUser(String keywords, User user){
		return this.contactRepository.findByNameContainingAndUser(keywords, user);
	}
	
	public String changeUserPassword(String oldPassword, String newPassword, User user) {
		
		if(this.passwordEncoder.matches(oldPassword, user.getPassword())) {
			user.setPassword(this.passwordEncoder.encode(newPassword));
			this.userRepository.save(user);
			return "done";
		} else { 
			return "notMatch";
		}
	}

	public String updateUserProfile(MultipartFile file, User userByUsername) {
		if (!file.isEmpty()) {
			if(file.getContentType().equals("image/jpeg")){
				try {
					Files.copy(file.getInputStream(),
							Paths.get(new ClassPathResource("static").getFile().getAbsolutePath()+
									java.io.File.separator+"img"+
									java.io.File.separator+"user"+
									java.io.File.separator+userByUsername.getName()+userByUsername.getId()+".jpg"), 
							StandardCopyOption.REPLACE_EXISTING);
					userByUsername.setImageUrl(userByUsername.getName()+userByUsername.getId()+".jpg");
					this.userRepository.save(userByUsername);
				} catch (IOException e) {
					e.printStackTrace();
					return "fileError";
				}
			} else {
				return "invalidFile";
			}
		} else {
			return "no";
		}
		return "done";
	}

	
	public String deleteAccount(User user) {
		List<Contact> list = this.contactRepository.findContactByUserId(user.getId());
		//delete all contact image
		list.forEach(contact -> {
			String imageUrl = contact.getImageUrl();
			if(!imageUrl.equals("default.jpg")) {
				try {
					File file = new File(new ClassPathResource("static").getFile().getAbsolutePath()+
							java.io.File.separator+"img"+
							java.io.File.separator+"profile"+
							java.io.File.separator+ imageUrl);
					file.delete();
				} catch (IOException e) {
				}
			}
		});
		//delete all contact
		this.contactRepository.deleteAll(list);
		//delete profile image
		String imageUrl = user.getImageUrl();
		if(!imageUrl.equals("default.jpg")) {
			try {
				File file = new File(new ClassPathResource("static").getFile().getAbsolutePath()+
						java.io.File.separator+"img"+
						java.io.File.separator+"user"+
						java.io.File.separator+ imageUrl);
				file.delete();
			} catch (IOException e) {
			}
		}
		this.userRepository.delete(user);
		return "done";
	}
		
}
