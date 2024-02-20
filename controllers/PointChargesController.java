package ace.project.vote.Group.Project.controllers;
import ace.project.vote.Group.Project.dtos.PointChargesDto;
import ace.project.vote.Group.Project.models.PointCharges;
import ace.project.vote.Group.Project.services.RoleCheckingService;
import ace.project.vote.Group.Project.services.PointChargesService;
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
public class PointChargesController {

    @Autowired
    private PointChargesService pointChargesService;

    @Autowired
    private RoleCheckingService roleCheckingService;

    @GetMapping("/admin/pointCharges/create")
    public ModelAndView createPointCharges(Model model, HttpSession session){
        roleCheckingService.check(session,model);
        ModelAndView modelAndView=new ModelAndView("pointCharges/create");
        modelAndView.addObject("pointCharges",new PointChargesDto());
        return modelAndView;
    }

    @PostMapping("/admin/pointCharges/create")
    public ModelAndView createContestantForm(@Valid  @ModelAttribute("pointCharges") PointChargesDto pointCharges, BindingResult result, RedirectAttributes redirectAttributes, HttpSession session, Model model){
        ModelAndView modelAndView=new ModelAndView();
        if (result.hasErrors()){
            roleCheckingService.check(session, model);
            modelAndView.setViewName("pointCharges/create");
            modelAndView.addObject("pointCharges", pointCharges);
            return modelAndView;
        }
        pointChargesService.createPointCharges(pointCharges);
        redirectAttributes.addFlashAttribute("message", "Point Charges create successfully!");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        modelAndView.setViewName("redirect:/pointCharges/view");
        return modelAndView;
    }

    @GetMapping("/pointCharges/view")
    public String viewPointCharges( Model model, HttpSession session) {
        roleCheckingService.check(session,model);
        List<PointCharges> pointCharges = pointChargesService.getAllPointCharges();
        model.addAttribute("pointCharges", pointCharges);
        return "/pointCharges/view";
    }
    @GetMapping("/admin/pointCharges/edit/{id}")
    public String editPointCharges(@PathVariable("id") int id, Model model,HttpSession session) {
        roleCheckingService.check(session,model);
        model.addAttribute("pointCharges", pointChargesService.getById(id));
        return "/pointCharges/Edit";
    }

    @PostMapping("/admin/pointCharges/{id}")
    public String updatePointCharges(@PathVariable("id") int id, @RequestParam("point") int point, @RequestParam("amount") int amount,Model model,RedirectAttributes redirectAttributes){
        PointCharges pointCharges = pointChargesService.getById(id);
        pointCharges.setPoint(point);
        pointCharges.setAmount(amount);
        pointChargesService.updatePointCharges(pointCharges);
        redirectAttributes.addFlashAttribute("message", "Point Charges updated successfully!");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        model.addAttribute("pointCharges", pointCharges);
        return "redirect:/pointCharges/view";
    }


    @GetMapping( "/admin/pointCharges/delete/{id}")
    public String deletePointCharges (Model model,@PathVariable("id") int id){
        pointChargesService.deleteById(id);
        List<PointCharges> pointCharges = pointChargesService.getAllPointCharges();
        model.addAttribute("pointCharges", pointCharges);
        return "redirect:/pointCharges/view";
    }

    }







