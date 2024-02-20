package ace.project.vote.Group.Project.controllers;

import ace.project.vote.Group.Project.dtos.ContestantDto;


import ace.project.vote.Group.Project.models.Contestant;
import ace.project.vote.Group.Project.models.Topic;
import ace.project.vote.Group.Project.models.User;
import ace.project.vote.Group.Project.services.RoleCheckingService;
import ace.project.vote.Group.Project.services.ContestantService;
import ace.project.vote.Group.Project.services.TopicService;
import ace.project.vote.Group.Project.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
public class ContestantController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private ContestantService contestantService;

    @Autowired
    private RoleCheckingService roleCheckingService;

    @Autowired
    private UserService userService;

    @GetMapping("/admin/contestant/create")
    public ModelAndView createContestant(@RequestParam("id") int id, HttpSession session, Model model) {
        roleCheckingService.check(session, model);
        ModelAndView modelAndView = new ModelAndView("contestant/create");
        Topic topic = topicService.getById(id);
        ContestantDto dto = new ContestantDto();
        session.setAttribute("topicId", id);
        session.setAttribute("topic", topic);
        modelAndView.addObject("contestant", dto);
        return modelAndView;
    }

    @PostMapping("/admin/contestant/create")
    public String create(@Valid @ModelAttribute("contestant") ContestantDto contestant, BindingResult result,
                         RedirectAttributes redirectAttributes, HttpSession session,
                         @RequestParam("file") MultipartFile file, Model model) throws IOException {
        System.out.println("Name : : : " + contestant.getName());
        contestant.setTopic((Topic) session.getAttribute("topic"));
        redirectAttributes.addAttribute("tid", session.getAttribute("topicId"));

        if (file.isEmpty() && result.hasErrors()) {
            roleCheckingService.check(session, model);
            model.addAttribute("checkFile", "File must not be empty");
            model.addAttribute("contestant", contestant);
            return "/contestant/create";
        } else if (!file.isEmpty() && result.hasErrors()) {
            roleCheckingService.check(session, model);
            model.addAttribute("contestant", contestant);
            return "/contestant/create";
        } else if (file.isEmpty() && !result.hasErrors()) {
            roleCheckingService.check(session, model);
            model.addAttribute("checkFile", "File must not be empty");
            return "/contestant/create";
        } else {
            byte[] bytes = file.getBytes();
            String encodedString = Base64.getEncoder().encodeToString(bytes);
            model.addAttribute("fileName", encodedString);
            contestantService.createContestant(contestant, encodedString);
            redirectAttributes.addFlashAttribute("message", "Contestant create successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            return "redirect:/contestant/view/{tid}";
        }
    }


/* @GetMapping("/contestant/view/{id}")
public String viewContestant(@PathVariable("id") int id, Model model, HttpSession session) {
roleCheckingService.check(session, model);
Topic topic = topicService.getById(id);

LocalDateTime fromDate = topic.getFromDate();
LocalDateTime toDate = topic.getToDate();
LocalDate currentDate = LocalDate.now();
if ((currentDate.isEqual(ChronoLocalDate.from(fromDate)) || currentDate.isAfter(ChronoLocalDate.from(fromDate))) && (currentDate.isEqual(ChronoLocalDate.from(toDate)) || currentDate.isBefore(ChronoLocalDate.from(toDate)))) {
model.addAttribute("votingEnabled", true);
} else {
model.addAttribute("votingEnabled", false);
}

model.addAttribute("name", topic.getName());
model.addAttribute("fromDate", topic.getFromDate());
model.addAttribute("toDate", topic.getToDate());
List<Contestant> contestants = contestantService.getContestantsByTopicOrderedByVoteCount(id);
    model.addAttribute("contestants", contestants);

    *//* if (!contestants.isEmpty()) {
    Contestant winner = contestantService.getContestantWithHighestVoteCount(id);
    model.addAttribute("winner", winner);
    }*//*



    model.addAttribute("topic", id);

    return "contestant/view";
    }
    */

    @GetMapping("/contestant/view/{id}")
    public String viewContestant(@PathVariable("id") int id, Model model, HttpSession session) {
        roleCheckingService.check(session, model);
        Topic topic = topicService.getById(id);

        LocalDate fromDate = topic.getFromDate();
        LocalDate toDate = topic.getToDate();
        LocalDate currentDate = LocalDate.now();
        if ((currentDate.isEqual(ChronoLocalDate.from(fromDate)) || currentDate.isAfter(ChronoLocalDate.from(fromDate))) && (currentDate.isEqual(ChronoLocalDate.from(toDate)) ||  currentDate.isBefore(ChronoLocalDate.from(toDate)))) {
            model.addAttribute("votingEnabled", true);


        } else {
            model.addAttribute("votingEnabled", false);
            List<Contestant> contestants=contestantService.getContestantsByTopicOrderedByVoteCount(id);
            model.addAttribute("contestants",contestants);
            if (!contestants.isEmpty()  && contestantService.getContestantWithHighestVoteCount(id).getVoteCount() > 0) {
                Contestant winner = contestantService.getContestantWithHighestVoteCount(id);
                model.addAttribute("winner", winner);
            }

        }

        model.addAttribute("name", topic.getName());
        model.addAttribute("fromDate", topic.getFromDate());
        model.addAttribute("toDate", topic.getToDate());
        List<Contestant> contestants = contestantService.getContestantsByTopicOrderedByVoteCount(id);
        model.addAttribute("contestants", contestants);
        model.addAttribute("topic", id);

        return "contestant/view";
    }

    @GetMapping("/admin/contestant/edit")
    public String editContestant(@RequestParam(name = "id") int id, @RequestParam("tid") int tid,
                                 Model model, HttpSession session) {
        roleCheckingService.check(session, model);
        model.addAttribute("contestant", contestantService.getContestantById(id));
        model.addAttribute("topicId", tid);
        System.out.println(id);
        System.out.println(tid);
        return "contestant/edit";
    }

    @PostMapping("/admin/contestant")
    public String editContestantForm(@RequestParam(name = "id") int id, @RequestParam("tid") int tid,
                                     @RequestParam("name") String name, @RequestParam("description") String description, Model model,
                                     RedirectAttributes redirectAttributes, @RequestParam("file") MultipartFile file) throws IOException {
        Contestant existingContestant = contestantService.getContestantById(id);
        existingContestant.setName(name);
        existingContestant.setDescription(description);
        existingContestant.setTopic(existingContestant.getTopic());
       /* existingContestant.setVoteCount(0);*/
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            String encodedString = Base64.getEncoder().encodeToString(bytes);
            existingContestant.setImage(encodedString);
            model.addAttribute("fileName", encodedString);
        } else {
            model.addAttribute("fileName", existingContestant.getImage());
        }
        contestantService.updateForm(existingContestant);
        model.addAttribute("contestant", existingContestant);
        model.addAttribute("topicId", tid);
        redirectAttributes.addAttribute("id", tid);

        redirectAttributes.addFlashAttribute("message", "Contestant update successfully!");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/contestant/view/{id}";
    }

    @GetMapping("/admin/contestant/delete")
    public String deleteContestant(Model model, @RequestParam(name = "id") int id,
                                   @RequestParam(name = "tid") int tid, RedirectAttributes redirectAttributes) {
        contestantService.deleteById(id);
        redirectAttributes.addAttribute("id", tid);
        model.addAttribute("contestant", contestantService.getContestantsByTopicOrderedByVoteCount(tid));
        return "redirect:/contestant/view/{id}";
    }



}


















