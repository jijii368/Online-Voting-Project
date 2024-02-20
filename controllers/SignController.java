package ace.project.vote.Group.Project.controllers;

import ace.project.vote.Group.Project.dtos.AdminLoginDto;
import ace.project.vote.Group.Project.dtos.LoginFormDto;
import ace.project.vote.Group.Project.models.Admin;
import ace.project.vote.Group.Project.models.User;
import ace.project.vote.Group.Project.services.AdminService;
import ace.project.vote.Group.Project.services.RoleCheckingService;
import ace.project.vote.Group.Project.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SignController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleCheckingService roleCheckingService;

    @GetMapping("/sign-in")
    public ModelAndView loginUserView(Model model, HttpSession session){
        roleCheckingService.check(session,model);
        return new ModelAndView("public/userlogin","loginForm",new LoginFormDto());
    }

    @PostMapping("/sign-in")
    public ModelAndView loginUser(@ModelAttribute("loginForm") LoginFormDto loginForm, HttpSession session, Model model){
        if (loginForm.getEmail().isEmpty()){
            roleCheckingService.check(session,model);
            model.addAttribute("emailNull", "Email must not be empty!");
            return new ModelAndView("public/userlogin", "loginForm", loginForm);
        }
        if (userService.checkEmail(loginForm.getEmail())){
            User checkUser=userService.checkUserByEmailAndPassword(loginForm.getEmail(),loginForm.getPassword());
            if (checkUser!=null){
                session.setAttribute("loginUser",checkUser.getName());
                session.setAttribute("loginUserId", checkUser.getId());
                return new ModelAndView("redirect:/user/user-profile");
            }else {
                roleCheckingService.check(session,model);
                model.addAttribute("notValidPassword", "Incorrect Password!");
                return new ModelAndView("public/userlogin", "loginForm", loginForm);
            }
        }else {
            roleCheckingService.check(session,model);
            model.addAttribute("notValidEmail", "Incorrect Email!");
            return new ModelAndView("public/userlogin", "loginForm", loginForm);
        }
    }

    @GetMapping("/this/is/the/way-lol")
    public ModelAndView loginAdminView(HttpSession session, Model model){
        roleCheckingService.check(session,model);
        return new ModelAndView("admin/login", "loginAdmin", new AdminLoginDto());
    }

    @PostMapping("/this/is/the/way-lol")
    public ModelAndView loginAdmin(@ModelAttribute("loginAdmin") AdminLoginDto admin, HttpSession session, Model model){
        if (admin.getName().isEmpty()){
            roleCheckingService.check(session,model);
            model.addAttribute("nameNull", "Admin Name must not be empty!");
            return new ModelAndView("admin/login", "loginAdmin", admin);
        }
        if (adminService.checkName(admin.getName())){
            Admin checkAdmin=adminService.checkByNameAndPassword(admin.getName(), admin.getPassword());
            if (checkAdmin!=null){
                session.setAttribute("loginAdmin", checkAdmin.getName());
                session.setAttribute("adminId", checkAdmin.getId());
                return new ModelAndView("redirect:/admin/admin-profile");
            }else {
                roleCheckingService.check(session,model);
                model.addAttribute("adminPasswordNull", "Incorrect admin password!");
                return new ModelAndView("admin/login", "loginAdmin", admin);
            }
        }else {
            roleCheckingService.check(session, model);
            model.addAttribute("adminNameNull", "Incorrect admin name!");
            return new ModelAndView("admin/login", "loginAdmin", admin);
        }
    }

    @GetMapping("/sign-out")
    public ModelAndView logout(HttpServletRequest request){
        request.getSession().invalidate();
        return new ModelAndView("redirect:/home");
    }
}
