package ace.project.vote.Group.Project.controllers;

import ace.project.vote.Group.Project.dtos.PaymentDto;
import ace.project.vote.Group.Project.services.RoleCheckingService;
import ace.project.vote.Group.Project.services.PaymentService;
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
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RoleCheckingService roleCheckingService;

    @GetMapping("/admin/payment/create")
    public ModelAndView paymentCreateView(HttpSession session, Model model){
        roleCheckingService.check(session,model);
        return new ModelAndView("payment/create", "payment", new PaymentDto());
    }

    @PostMapping("/admin/payment/create")
    public ModelAndView paymentCreate(@Valid @ModelAttribute("payment") PaymentDto payment, BindingResult result, RedirectAttributes redirectAttributes, HttpSession session, Model model){
        if (result.hasErrors()){
            roleCheckingService.check(session,model);
            return new ModelAndView("payment/create","payment", payment);
        }
        paymentService.createPayment(payment, (Integer) session.getAttribute("adminId"));
        redirectAttributes.addFlashAttribute("message", "Payment create successfully!");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return new ModelAndView("redirect:/admin/payment/view");
    }

    @GetMapping("/admin/payment/view")
    public ModelAndView paymentView(HttpSession session,Model model){
        roleCheckingService.check(session,model);
        return new ModelAndView("payment/view", "allPayment", paymentService.getAllPayment());
    }

    @GetMapping("/admin/payment/edit/{id}")
    public ModelAndView paymentEditView(@PathVariable("id") int id, HttpSession session, Model model){
        roleCheckingService.check(session,model);
        model.addAttribute("id", id);
        return new ModelAndView("payment/edit","payment",paymentService.entityToDto(paymentService.getPaymentById(id)));
    }

    @PostMapping("/admin/payment/edit/{id}")
    public ModelAndView paymentEdit(@PathVariable("id") int id, @Valid @ModelAttribute("payment") PaymentDto payment, BindingResult result, HttpSession session, Model model,RedirectAttributes redirectAttributes){
        if (result.hasErrors()){
            roleCheckingService.check(session, model);
            return new ModelAndView("payment/edit", "payment", payment);
        }
        paymentService.updatePayment(id, payment);
        redirectAttributes.addFlashAttribute("message", "Payment updated successfully!");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return new ModelAndView("redirect:/admin/payment/view");
    }

    @GetMapping("/admin/payment/delete/{id}")
    public ModelAndView paymentDelete(@PathVariable("id") int id){
        paymentService.deletePaymentById(id);
        return new ModelAndView("redirect:/admin/payment/view");
    }
}
