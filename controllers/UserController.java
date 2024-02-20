package ace.project.vote.Group.Project.controllers;

import ace.project.vote.Group.Project.dtos.UserDto;
import ace.project.vote.Group.Project.dtos.UserEditDto;
import ace.project.vote.Group.Project.services.RoleCheckingService;
import ace.project.vote.Group.Project.services.UserRequestPointsService;
import ace.project.vote.Group.Project.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    private UserRequestPointsService userRequestPointsService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleCheckingService roleCheckingService;

    @GetMapping("/sign-up")
    public ModelAndView createUserView(HttpSession session,Model model){
        roleCheckingService.check(session, model);
        return new ModelAndView("user/create", "user", new UserDto());
    }

    @PostMapping("/sign-up")
    public ModelAndView createUser(@Valid @ModelAttribute("user") UserDto user, BindingResult result, Model model, RedirectAttributes redirectAttributes, HttpSession session) {
        if (result.hasErrors()){
            roleCheckingService.check(session, model);
            return new ModelAndView("user/create", "user", user);
        }else {
            if (userService.checkEmail(user.getEmail())) {
                roleCheckingService.check(session, model);
                model.addAttribute("userExist", "User already exist with this email!");
                return new ModelAndView("user/create", "user", user);
            } else {
                userService.createUser(user);
                redirectAttributes.addFlashAttribute("message", "User create successfully!");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
                return new ModelAndView("redirect:/home");
            }
        }
    }

    @GetMapping("/user/user-profile")
    public ModelAndView userView(HttpSession session, Model model) {
        roleCheckingService.check(session,model);
        model.addAttribute("randomPic", userService.pictureRotation());
        model.addAttribute("requestPoints",userRequestPointsService.getRequestPointsByUser((Integer) session.getAttribute("loginUserId")));
        model.addAttribute("createdDate",userService.getUserById((Integer) session.getAttribute("loginUserId")).getCreatedAt().toString().substring(0, 11));
        return new ModelAndView("user/view", "userProfile", userService.getUserById((Integer) session.getAttribute("loginUserId")));
    }

    @GetMapping("/user/edit")
    public ModelAndView editUserView(HttpSession session, Model model) {
        roleCheckingService.check(session,model);
        return new ModelAndView("user/edit","userEdit", userService.entityToDto(userService.getUserById((Integer) session.getAttribute("loginUserId"))));
    }

    @PostMapping("/user/edit")
    public ModelAndView editUser(HttpSession session, @Valid @ModelAttribute("userEdit") UserEditDto user, BindingResult result, Model model,RedirectAttributes redirectAttributes){
        if (result.hasErrors()){
            roleCheckingService.check(session, model);
            return new ModelAndView("user/edit", "userEdit", user);
        }
        userService.updateUser((Integer) session.getAttribute("loginUserId"), user);
        redirectAttributes.addFlashAttribute("message", "User Edit successfully!");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return new ModelAndView("redirect:/user/user-profile");
    }

//    @GetMapping("/user/delete")
//    public ModelAndView deleteUser(HttpSession session){
//        userService.deleteUser((Integer) session.getAttribute("loginUserId"));
//        return new ModelAndView("redirect:/sign-out");
//    }
}
