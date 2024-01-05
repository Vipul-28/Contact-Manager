package com.smart;

import java.io.File;
import com.razorpay.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.smart.contactDao.ContactDao;
import com.smart.dao.UserDao;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Helper;

//@ComponentScan({"com.smart.contactDao", "com.smart.dao"})
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private  BCryptPasswordEncoder pass;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ContactDao contactDao;
//	@Autowired
//	private FileUploaderHelper fileUploaderHelper;
	@ModelAttribute
	public void addcommodata(Model model,Principal principal)
	{
		String uname=principal.getName();		
		User user=userDao.getUserByUserName(uname);
		model.addAttribute("user", user);
	}
	@RequestMapping("/index")
	public String userController(Model model,Principal principal)
	{
		return "user/user_dashboard";
	}
	@RequestMapping("/add-contact")
	public String openAddContactForm(Model model){
		model.addAttribute("tittle", "Add Contact");
		return "user/add_contact";
	}
	@RequestMapping("/view_contact")
	public String viewcontact(Model model,Principal principale){
		User user1=userDao.getUserByUserName(principale.getName());
		List<Contact> contact=contactDao.getContactByUserName(user1.getId());	
		model.addAttribute("list",contact);
		return "user/view_contact";
	}
	@RequestMapping("/{cId}/single-page")
	public String singlePageContact(@PathVariable("cId") Integer cId,Model model)
	{
		System.out.print("y");
		Optional<Contact> contact=contactDao.findById(cId);
		Contact c=contact.get();
		model.addAttribute("c", c);
		return "user/contact_profile";
	}
	@GetMapping("/{cId}/delete")
//	@Transactional
	public String delettete(@PathVariable("cId")Integer cId,HttpSession session)
	{
		contactDao.deleteById(cId);
		session.setAttribute("don", new Helper("Delete Duccesfully","alert-success"));
		return "redirect:/user/view_contact";
	}
	@RequestMapping(value="/{cId}/update",method=RequestMethod.POST)
	public String update(@ModelAttribute("contact") Contact contact,Principal principal,@RequestParam("fimages")MultipartFile file,@PathVariable("cId")Integer cId){
		try {
			System.out.print(cId);
			Contact cont=contactDao.findById(contact.getcId()).get();
			if(!file.isEmpty())
			{
				Files.copy(file.getInputStream(), Paths.get("C:\\Users\\Wizar\\Documents\\workspace-spring-tool-suite-4-4.19.1.RELEASE\\smartcontactmanager\\src\\main\\resources\\static\\img"+File.separator+file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
				
			}
			else
			{
				contact.setImage(cont.getImage());
			}

				System.out.print("yes");
				
				System.out.print("ye1s");


				System.out.print("yes11");

				User user=userDao.getUserByUserName(principal.getName());
				contact.setUser(user);
				contactDao.save(contact);
				System.out.print("22yes");

			return "user/user_dashboard";
		} catch (Exception e) {
			// TODO: handle 
//			System.out.print(cId);
			e.printStackTrace();
			return "user/user_dashboard";

		}
		
	}
	@RequestMapping("/update_pass")
	public String updatepass()
	{
		return "user/update_password";
	}
	@PostMapping("/update_password")
	public String updated_password(@RequestParam("oldpass") String oldPass,@RequestParam("newpass") String newPass,Principal p)
	{
		User user=userDao.getUserByUserName(p.getName());
		if(pass.matches(oldPass,user.getPassword()))
		{
			user.setPassword(pass.encode(newPass));
			userDao.save(user);
		}
		else
		{
			System.out.print("sorry");
		}
		return "redirect:/user/update_password";
	}
	@PostMapping("/create_order")
	@ResponseBody
	public String createOrder(@RequestBody Map<String,Object>data) throws Exception
	{ 
		int amt=Integer.parseInt(data.get("amount").toString());
		var client=new RazorpayClient("rzp_test_EceT5bUJiygAbk", "D3J1l2rannfEwkbuC1xhmi2c");
		    JSONObject ob=new JSONObject();
		    ob.put("amount", amt*100);
		    ob.put("currency", "INR");
		    ob.put("receipt", "txn_34344");
		    
		    
		  Order order=  client.Orders.create(ob);
		  System.out.print(order);
		    
		return order.toString();
	}
	@PostMapping("/addcontact")
	public String addContact(@ModelAttribute("contact") Contact contact,Principal principal,@RequestParam("fimages") MultipartFile file,BindingResult result,HttpSession session )
	{
		try {
			System.out.print(result.getObjectName());
			User user=userDao.getUserByUserName(principal.getName());
			if(file.isEmpty())
			{
				System.out.print("no");
			}
			else
			{
				contact.setImage(file.getOriginalFilename());
//				File saveFile=new ClassPathResource("static/img/").getFile();
				Files.copy(file.getInputStream(), Paths.get("C:\\Users\\Wizar\\Documents\\workspace-spring-tool-suite-4-4.19.1.RELEASE\\smartcontactmanager\\src\\main\\resources\\static\\img"+File.separator+file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
				user.getContact().add(contact);
				contact.setUser(user);
				userDao.save(user);			
			}
			System.out.print("yes");
			session.setAttribute("DONE",new Helper("Contact Added Sucessfully", "alert-success"));
			return "redirect:/user/view_contact";
		} catch (Exception e) {
			// TODO: handle exception
			session.setAttribute("DONE",new Helper("Soory SomeThing Went Wrong !......", "alert-danger"));
			e.printStackTrace();
			return "redirect:/user/view_contact";
		}
		
	}
}
