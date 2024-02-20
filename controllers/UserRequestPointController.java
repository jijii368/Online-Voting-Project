package ace.project.vote.Group.Project.controllers;

import ace.project.vote.Group.Project.dtos.UserRequestPointsDto;
import ace.project.vote.Group.Project.services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserRequestPointController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @Autowired
    private PointChargesService pointChargesService;

    @Autowired
    private UserRequestPointsService userRequestPointsService;

    @Autowired
    private RoleCheckingService roleCheckingService;

    @GetMapping("/user/pointRequest/create")
    public ModelAndView requestPointView(Model model, HttpSession session){
        roleCheckingService.check(session, model);
        model.addAttribute("payments", paymentService.getAllPayment());
        model.addAttribute("pointCharges", pointChargesService.getAllPointCharges());
        return new ModelAndView("requestPoint/create", "requestPoint", new UserRequestPointsDto());
    }

    @PostMapping("/user/pointRequest/create")
    public ModelAndView requestPoint(@ModelAttribute("requestPoint") UserRequestPointsDto userRequestPointsDto, RedirectAttributes redirectAttributes, HttpSession session, Model model){
        if ((userRequestPointsDto.getPayment()==0 && userRequestPointsDto.getPointCharges()==0) || (userRequestPointsDto.getPayment()!=0 && userRequestPointsDto.getPointCharges()==0) || (userRequestPointsDto.getPayment()==0 && userRequestPointsDto.getPointCharges()!=0)){
            model.addAttribute("payments", paymentService.getAllPayment());
            model.addAttribute("pointCharges", pointChargesService.getAllPointCharges());
            model.addAttribute("needPaymentAndPointCharges", "Need to select both Payment and Point Charges!");
            roleCheckingService.check(session,model);
            return new ModelAndView("requestPoint/create", "requestPoint", userRequestPointsDto);
        }
        System.out.println(userRequestPointsDto);
        userRequestPointsService.createUserRequestPoint(userService.getUserById((Integer) session.getAttribute("loginUserId")), paymentService.getPaymentById(userRequestPointsDto.getPayment()), pointChargesService.getById(userRequestPointsDto.getPointCharges()));
        redirectAttributes.addFlashAttribute("pointRequestMessage", "Point request create successfully!");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return new ModelAndView("redirect:/user/user-profile");
    }


    @GetMapping("/user/pointRequest/cancel/{id}")
    public ModelAndView pointCancel(@PathVariable("id") int id){
        System.out.println(id);
        userRequestPointsService.deleteUserRequestPoint(id);
        return new ModelAndView("redirect:/user/user-profile");
    }

    @GetMapping("/admin/pointRequest/accept")
    public ModelAndView pointAccept(@RequestParam("id") int id, RedirectAttributes redirectAttributes){
        System.out.println(userRequestPointsService.getById(id).isHasConfirmed());
        if (userRequestPointsService.getById(id).isHasConfirmed()){
            redirectAttributes.addFlashAttribute("accepted", "This request point is already accepted!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            return new ModelAndView("redirect:/admin/admin-profile");
        }else {
            userService.updateUserPoint(userRequestPointsService.getById(id).getUser(), userRequestPointsService.getById(id).getPointCharges().getPoint());
            userRequestPointsService.updateHasConfirmed(userRequestPointsService.getById(id));
            return new ModelAndView("redirect:/admin/admin-profile");
        }
    }

    @GetMapping("/admin/pointRequest/delete")
    public ModelAndView pointDelete(@RequestParam("id")int id,RedirectAttributes redirectAttributes){
        if (userRequestPointsService.getById(id).isHasConfirmed()){
        userRequestPointsService.deleteUserRequestPoint(id);
        }else {
            redirectAttributes.addFlashAttribute("cantDeleteWithoutAccept", "You must first need to click accept button!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        }
        return new ModelAndView("redirect:/admin/admin-profile");
    }
}
