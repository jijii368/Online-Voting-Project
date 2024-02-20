package ace.project.vote.Group.Project.controllers;

import ace.project.vote.Group.Project.dtos.AdminDto;
import ace.project.vote.Group.Project.dtos.AdminEditDto;

import ace.project.vote.Group.Project.models.Admin;
import ace.project.vote.Group.Project.services.AdminService;
import ace.project.vote.Group.Project.services.RoleCheckingService;
import ace.project.vote.Group.Project.services.UserRequestPointsService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserRequestPointsService userRequestPointsService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleCheckingService roleCheckingService;

    @GetMapping("/create")
    public ModelAndView createAdmin(HttpSession session, Model model){
        roleCheckingService.check(session,model);
        ModelAndView modelAndView = new ModelAndView("admin/create");
        modelAndView.addObject("admin",new AdminDto());
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView createAdmin(@Valid @ModelAttribute("admin") AdminDto admin, BindingResult result, Model model, RedirectAttributes redirectAttributes, HttpSession session) {
        ModelAndView modelAndView=new ModelAndView();
        if (result.hasErrors()){
            roleCheckingService.check(session, model);
            modelAndView.setViewName("admin/create");
            modelAndView.addObject("admin", admin);
            return modelAndView;
        }
        List<Admin> admins=adminService.getAll();
        System.out.println(admins);
        if(admins.isEmpty()){
            adminService.createAdmin(admin);
            redirectAttributes.addFlashAttribute("adminMessage", "Admin create successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            modelAndView.setViewName("redirect:/");
        }else {
            roleCheckingService.check(session, model);
            String existMessage= "Admin already exit!";
            model.addAttribute("existMessage", existMessage);
            modelAndView.setViewName("admin/create");
            modelAndView.addObject("admin", admin);
            return modelAndView;
        }
        return modelAndView;
    }

    @GetMapping("/admin/admin-profile")
    public ModelAndView adminView(HttpSession session, Model model) {
        roleCheckingService.check(session,model);
        model.addAttribute("allRequestPoints", userRequestPointsService.getAllRequestPoints());
        model.addAttribute("createdDate",adminService.getAdminById((Integer) session.getAttribute("adminId")).getCreatedAt().toString().substring(0, 11));
        model.addAttribute("updatedDate",adminService.getAdminById((Integer) session.getAttribute("adminId")).getUpdatedAt().toString().substring(0, 11));
        return new ModelAndView("admin/view", "adminProfile",adminService.getAdminById((Integer) session.getAttribute("adminId")));
    }

    @GetMapping("/admin/edit")
    public ModelAndView editAdminView(HttpSession session, Model model)  {
        roleCheckingService.check(session,model);
        return new ModelAndView("admin/edit","adminEdit", adminService.entityToDto(adminService.getAdminById((Integer) session.getAttribute("adminId"))));
    }

    @PostMapping("/admin/edit")
    public ModelAndView editAdmin(HttpSession session, @Valid @ModelAttribute("adminEdit") AdminEditDto admin, BindingResult result,RedirectAttributes redirectAttributes, Model model){
        if (result.hasErrors()){
            roleCheckingService.check(session, model);
            return new ModelAndView("admin/edit", "adminEdit", admin);
        }
        adminService.updateAdmin((Integer) session.getAttribute("adminId"), admin);
        redirectAttributes.addFlashAttribute("message", "Admin Edit successfully!");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return new ModelAndView("redirect:/admin/admin-profile");
    }

//    @GetMapping("/admin/delete")
//    public ModelAndView deleteAdmin(HttpSession session){
//        adminService.deleteAdmin((Integer) session.getAttribute("adminId"));
//        return new ModelAndView("redirect:/sign-out");
//    }
}
