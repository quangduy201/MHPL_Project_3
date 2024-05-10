package com.example.project_3.controllers;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.models.XuLy;
import com.example.project_3.payloads.requests.LoginRequest;
import com.example.project_3.payloads.requests.RegisterRequest;
import com.example.project_3.payloads.responses.ThanhVienResponse;
import com.example.project_3.services.AuthService;
import com.example.project_3.services.XuLyService;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import com.example.project_3.services.ThanhVienService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Properties;

@Controller
@RequestMapping("/")
public class AuthController {
    private final AuthService authService;
    private final ThanhVienService thanhVienService;
    private final XuLyService xuLyService;

    @Autowired
    public AuthController(AuthService authService, ThanhVienService thanhVienService, XuLyService xuLyService) {
        this.authService = authService;
        this.thanhVienService = thanhVienService;
        this.xuLyService = xuLyService;
    }


    @GetMapping({"/login", "/login/"})
    public String loginForm(Model model) {

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
        }

        List<XuLy> xuLyList = xuLyService.getViPhamKhoaTaiKhoanByMaTVAnd(thanhVienResponse.getMaTV());
        if(!xuLyList.isEmpty()) {
            XuLy xuLy = xuLyList.get(0);
            bindingResult.rejectValue("credentials", "invalid.credentials", "Thành viên này đang bị vi phạm: " + xuLy.getHinhThucXL());
            return "login/index";
        } else {
            session.setAttribute("user", thanhVienResponse);
            return "redirect:/user";
        }
//        Page<XuLy> xuLyPage = xuLyService.getViPhamByMaTV(thanhVienResponse.getMaTV());
//        if(xuLyPage.hasContent()) {
//            XuLy xuLy = xuLyPage.getContent().get(0);
//            bindingResult.rejectValue("credentials", "invalid.credentials", "Thành viên này đang bị vi phạm: " + xuLy.getHinhThucXL());
//            return "login/index";
//        }
//        session.setAttribute("user", thanhVienResponse);
//        return "redirect:/user";
    }

    @GetMapping({"/register", "/register/"})
    public String registerForm(Model model) {
        model.addAttribute("tv", new RegisterRequest());
        return "register/index";
    }

    @PostMapping({"/register", "/register/"})
    public String register(@Valid @ModelAttribute("tv") RegisterRequest tv, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            if (!tv.isXacNhanMatKhauValid()) {
                bindingResult.rejectValue("xacNhanMatKhau", "password.mismatch", "Trường này không khớp với trường mật khẩu");
                bindingResult.rejectValue("matKhau", "password.mismatch", "Trường này không khớp với trường xác nhận mật khẩu");
            }
            model.addAttribute("tv", tv);
            return "register/index";
        }

        if (thanhVienService.getThanhVienById(Long.valueOf(tv.getMaTV())) != null) {
            bindingResult.rejectValue("credentials", "invalid.credentials", "Mã thành viên đã tồn tại trong hệ thống");

            model.addAttribute("tv", tv);
            return "register/index";
        }

        if (thanhVienService.getThanhVienBySdt(tv.getSdt()) != null) {
            bindingResult.rejectValue("credentials", "invalid.credentials", "Số điện thoại đã tồn tại trong hệ thống");

            model.addAttribute("tv", tv);
            return "register/index";
        }

        if (thanhVienService.getThanhVienByEmail(tv.getEmail()) != null) {
            bindingResult.rejectValue("credentials", "invalid.credentials", "Email đã tồn tại trong hệ thống");

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
    public String adminLoginForm(Model model) {

        model.addAttribute("tv", new LoginRequest());

        return "admin/login/index";
    }

    @PostMapping({"/admin/login", "/admin/login/"})
    public String adminLogin(@Valid @ModelAttribute("tv") LoginRequest tv, BindingResult bindingResult, Model model,
                        HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("tv", tv);
            return "admin/login/index";
        }

        ThanhVienResponse thanhVienResponse = authService.adminLogin(tv.getEmail(), tv.getPassword());

        if (thanhVienResponse == null) {
            bindingResult.rejectValue("credentials", "invalid.credentials", "Email hoặc mật khẩu không đúng.");
            return "admin/login/index";
        } else {
            session.setAttribute("admin", thanhVienResponse);
            return "redirect:/admin";
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

    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("hoavt313@gmail.com", "pxjyzoqelijelurn");
            }
        };

        Session session = Session.getInstance(props, auth);

        MimeMessage message = new MimeMessage(session);

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

        message.setFrom(new InternetAddress("hoavt313@gmail.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject(subject);
        message.setContent(content, "text/html");

        Transport.send(message);
    }


    @PostMapping({"/quen-mat-khau", "/quen-mat-khau/"})
    public String forgotPasswordPost(Model model, HttpServletRequest request) {
        String email = request.getParameter("email");
        String token = generateRandomString(30);
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String resetPasswordLink = null;
        try {
            authService.updateResetPasswordToken(token, email);
            resetPasswordLink = baseUrl + "/lay-lai-mat-khau?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "Chúng tôi đã gửi một đường dẫn lấy lại mật khẩu vào email của bạn. Vui lòng kiểm tra.");

        } catch (Exception ex) {
        	ex.printStackTrace();
            model.addAttribute("error", "Email của bạn không tồn tại trong hệ thống");
        } return "/quenmatkhau/index";
    }
    @GetMapping({"/lay-lai-mat-khau", "/lay-lai-mat-khau/"})
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        ThanhVien customer = authService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (customer == null) {
            model.addAttribute("message", "Mã token không đúng");
            return "message";
        }

        return "/quenmatkhau/reset_password_form";
    }

    @PostMapping({"/lay-lai-mat-khau", "/lay-lai-mat-khau/"})
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("matKhau");

        ThanhVien customer = authService.getByResetPasswordToken(token);
        model.addAttribute("title", "Lấy lại mật khẩu");

        if (customer == null) {
            model.addAttribute("message", "Mã token không đúng");
            return "message";
        } else {
            authService.updatePassword(customer, password);

            model.addAttribute("message", "Bạn đã thay đổi mật khẩu thành công.");
        }

        return "/quenmatkhau/index";
    }

    @GetMapping({"/logout", "/logout/"})
    public String logout(Model model, HttpSession session) {
        if (session.getAttribute("user") != null) {
            session.removeAttribute("user");
        }
        return "redirect:/user";
    }

    @GetMapping({"/admin/logout", "/admin/logout/"})
    public String adminLogout(Model model, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            session.removeAttribute("admin");
        }
        return "redirect:/admin";
    }
}
