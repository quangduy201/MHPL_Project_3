package com.example.project_3.controllers;


import com.example.project_3.models.ThanhVien;
import com.example.project_3.payloads.requests.LoginRequest;
import com.example.project_3.payloads.requests.RegisterRequest;
import com.example.project_3.payloads.responses.ThanhVienResponse;
import com.example.project_3.services.AuthService;
import com.example.project_3.services.CustomerServices;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.aspectj.apache.bcel.classfile.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Properties;

@Controller
@RequestMapping("/")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private CustomerServices customerService;


    @GetMapping({  "/login/", "/login"})
    public String index(Model model) {

        model.addAttribute("tv", new LoginRequest());

        return "login/index";
    }

    @PostMapping({"/login", "/login/"})
    public String login(@Valid @ModelAttribute("tv") LoginRequest tv, BindingResult bindingResult, Model model,
                        HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("tv", tv);
            return "login/index";
        }

        ThanhVienResponse thanhVienResponse = authService.login(tv.getEmail(), tv.getPassword());

        if (thanhVienResponse == null) {
            bindingResult.rejectValue("credentials", "invalid.credentials", "Email hoặc mật khẩu không đúng.");
            return "login/index";
        } else {
            session.setAttribute("user", thanhVienResponse);
            return "redirect:/user";
        }
    }

    @GetMapping({"/register", "/register/"})
    public String getForm(Model model) {
        model.addAttribute("tv", new RegisterRequest());
        return "register/index";
    }

    @PostMapping({"/register", "/register/"})
    public String registerSubmit(@Valid @ModelAttribute("tv") RegisterRequest tv, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            if (!tv.isXacNhanMatKhauValid()) {
                bindingResult.rejectValue("xacNhanMatKhau", "password.mismatch", "Trường này không khớp với trường mật khẩu");
                bindingResult.rejectValue("matKhau", "password.mismatch", "Trường này không khớp với trường xác nhận mật khẩu");
            }
            model.addAttribute("tv", tv);
            return "register/index";
        }

        if (!tv.isXacNhanMatKhauValid()) {
            bindingResult.rejectValue("xacNhanMatKhau", "password.mismatch", "Trường này không khớp với trường mật khẩu");
            bindingResult.rejectValue("matKhau", "password.mismatch", "Trường này không khớp với trường xác nhận mật khẩu");
            model.addAttribute("tv", tv);
            return "register/index";
        }

        ThanhVien thanhVien = new ThanhVien();
        thanhVien.setMaTV(Long.valueOf(tv.getMaTV()));
        thanhVien.setHoTen(tv.getHoTen());
        thanhVien.setKhoa(tv.getKhoa());
        thanhVien.setNganh(tv.getNganh());
        thanhVien.setSdt(tv.getSdt());
        thanhVien.setEmail(tv.getEmail());
        thanhVien.setPassword(tv.getMatKhau());

        authService.register(thanhVien);

        // Logic to save the user to the database
        return "redirect:/login/";
    }

    @GetMapping({"/", ""})
    public String index(Model model, HttpSession session) {
        if (session.getAttribute("user") != null) {
            // Nếu có session với attribute là "user", chuyển hướng người dùng đến trang index
            // TODO SOMETHING ELSE
            return "/user/index";
        } else {
            // Nếu không có session "user", chuyển hướng người dùng đến trang đăng nhập
            // TODO SOMETHING ELSE
            return "redirect:/login";
        }
    }

    @GetMapping({"/admin/login", "/admin/login/"})
    public String loginAdmin(Model model, HttpSession session) {
        if (session.getAttribute("user") != null) {
            // Nếu có session với attribute là "user", chuyển hướng người dùng đến trang index
            // TODO SOMETHING ELSE
            return "/admin/index";
        } else {
            // Nếu không có session "user", chuyển hướng người dùng đến trang đăng nhập
            // TODO SOMETHING ELSE
            return "redirect:/admin/login";
        }
    }

    @PostMapping({"/admin/login", "/admin/login/"})
    public String loginAdminPost(Model model, HttpSession session) {
        if (session.getAttribute("user") != null) {
            // Nếu có session với attribute là "user", chuyển hướng người dùng đến trang index
            // TODO SOMETHING ELSE
            return "/admin/index";
        } else {
            // Nếu không có session "user", chuyển hướng người dùng đến trang đăng nhập
            // TODO SOMETHING ELSE
            return "redirect:/admin/login";
        }
    }

    @GetMapping({"/quen-mat-khau", "/quen-mat-khau/"})
    public String forgotPassword(Model model, HttpSession session) {
        return "/quenmatkhau/index";
    }




    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }
    
    public void sendEmail(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException {
    	
    	Properties p = System.getProperties();
        p.put("mail.smtp.host", "smtp.gmail.com");
        p.put("mail.smtp.port", 587);
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.starttls.enable", "true");
	    Session s = Session.getDefaultInstance(p, new Authenticator() {
	        @Override
	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication("hoavt313@gmail.com", "pxjy zoqe lije lurn");
	        }
	    });
        MimeMessage mm = new MimeMessage(s);
         
        String subject = "Here's the link to reset your password";
        String content = "<html>"
                + "<body>"
                + "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>"
                + "</body>"
                + "</html>";

        mm.addRecipients(Message.RecipientType.TO, recipientEmail);
        mm.setSubject(subject);
        mm.setContent(content, "text/html");
        Transport.send(mm);
    }


    @PostMapping({"/quen-mat-khau", "/quen-mat-khau/"})
    public String forgotPasswordPost(Model model, HttpServletRequest request) {
        String email = request.getParameter("email");
        String token = generateRandomString(30);
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String resetPasswordLink = null;
        try {
            customerService.updateResetPasswordToken(token, email);
            resetPasswordLink =baseUrl + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");

        } catch (Exception ex) {
        	ex.printStackTrace();
            model.addAttribute("error", "Your email does not exist");
        } return "/quenmatkhau/index";
    }
    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        ThanhVien customer = customerService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (customer == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }

        return "/quenmatkhau/reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        ThanhVien customer = customerService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");

        if (customer == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        } else {
            customerService.updatePassword(customer, password);

            model.addAttribute("message", "You have successfully changed your password.");
        }

        return "/quenmatkhau/index";
    }





}
