package com.smart;



import java.security.Principal;
import java.util.Random;

import javax.mail.Session;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserDao;
import com.smart.email.EmailService;
import com.smart.entities.User;
import com.smart.helper.Helper;

@Controller
public class Homecoltroller {
	@Autowired
	public UserDao userDao;
	@Autowired
	private  BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private EmailService emailService;
	@RequestMapping("home")
	public String home()
	{
		return "home";
	}
	@RequestMapping("fgverification")
	public String verify(){
		return "fgverification";
	}
	@RequestMapping("newpassword")
	public String newPass() {
		return "newpassword";
	}
	@RequestMapping("signup")
	public String signup(Model model)
	{
		model.addAttribute("user",new User());
		
		return "signup";
	}
	@RequestMapping("FG")
	public String fg()
	{
		return "FG";
	}
	@PostMapping("/new_pass")
	public String verifyPass(@RequestParam("oldpass") String oldpass,@RequestParam("newpass") String pass,Principal principal,HttpSession session) {
//		String name=principal.getName();
		System.out.print("ok1");
		System.out.print("DD");
		String name=(String)session.getAttribute("email");
		User user =userDao.getUserByUserName(name);
		if(oldpass.equals(pass))
		{
			user.setPassword(passwordEncoder.encode(pass));
			userDao.save(user);
			System.out.print("ok1");

			session.removeAttribute("email");
			System.out.print("ok1");

			return "redirect:/newpassword";
		}
		else
		{
			System.out.print("soort");
		}
		return "redirect:/FG";
	}
	@PostMapping("/verify")
	public String verfiy(@RequestParam("otp") String otp,HttpSession session)
	{
		int e=(int) session.getAttribute("otp");
		String EmailOtp=Integer.toString(e);
		if(otp.equals(EmailOtp))
		{
			session.removeAttribute("otp");
			return "redirect:/newpassword";
		}
		else
		{
			session.removeAttribute("email");
			return "redirect:/fgverification";
		}
	}
	@PostMapping("/fgpass")
	public String fgpass(@RequestParam("email") String email,HttpSession session){
	    Random random=new Random();
		int otp=random.nextInt(10000);
		session.setAttribute("otp", otp);
		String message="<h1>Otp is"+otp+"</h1>";
		String to=email;
		session.setAttribute("email",email);
		String subject="Forgetting password";
		boolean b=emailService.sendEmail(to, subject, message);
		if(b)
		return  "redirect:/fgverification";
		else
		return  "redirect:/FG";
		}
	@RequestMapping("emailverify")
	public String emailverify() {
		return "Emailverification";
	}
	@PostMapping("/emailverification")
	public String emailVefication(@RequestParam("otp") String otp,HttpSession session)
	{
		int e=(int) session.getAttribute("verifyotp");
		String otps=Integer.toString(e);
		if(otps.equals(otp))
		{
			userDao.save((User)session.getAttribute("newUser"));
		    session.setAttribute("message",new Helper("signup succefull!....", "show-toast"));
			session.removeAttribute("verifyotp");
		    return "redirect:/signup";	
		}
		else
		{
		    session.setAttribute("message",new Helper("Incoorect OTP!....", "alert-danger"));
			return "redirect:/emailverify";	
		}
	}
	@RequestMapping(value = "registration",method = RequestMethod.POST)
	public String registration(@Valid @ModelAttribute("user") User user,BindingResult result,@RequestParam(value="enabled",defaultValue = "false") boolean enable,HttpSession session)
	{
		try {
			if(result.hasErrors())
			{
				System.out.println(result.hasErrors());
				return "signup";
			}
			 Random random=new Random();
				int otp=random.nextInt(10000);
				session.setAttribute("verifyotp", otp);
				String message="<h1>Otp is"+otp+"</h1>";
				String to=user.getEmail();
				String subject="Email Verification";
				boolean b=emailService.sendEmail(to, subject, message);
				user.setRole("ROLE_USER");
				user.setPassword(passwordEncoder.encode(user.getPassword()));
                if(b)
                {
                session.setAttribute("newUser", user);
			    return "redirect:/emailverify";
                }
                else
                {
                	System.out.print("soory");
    			    return "redirect:/signup";

                }
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message",new Helper("check email", "alert-danger"));
			return "redirect:/signup";
		}
	}
    @GetMapping("/sign")
	public String customLogin()
    {
    	return "login";
    }
}

