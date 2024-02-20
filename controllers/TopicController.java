package ace.project.vote.Group.Project.controllers;

import ace.project.vote.Group.Project.dtos.TopicDto;
import ace.project.vote.Group.Project.models.Admin;
import ace.project.vote.Group.Project.models.Topic;
import ace.project.vote.Group.Project.services.AdminService;
import ace.project.vote.Group.Project.services.ContestantService;
import ace.project.vote.Group.Project.services.RoleCheckingService;
import ace.project.vote.Group.Project.services.TopicService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class TopicController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private RoleCheckingService roleCheckingService;

    @Autowired
    private ContestantService contestantService;

    @Autowired
    private AdminService adminService;

    @GetMapping("/admin/topic/create")
    public ModelAndView createTopicView(HttpSession session, Model model) {
        roleCheckingService.check(session,model);
        ModelAndView modelAndView = new ModelAndView("topic/create");
        modelAndView.addObject("topic", new TopicDto());
        return modelAndView;
    }

    @PostMapping("/admin/topic/create")
    public String createTopic(@Valid @ModelAttribute("topic") TopicDto topic, BindingResult result,
                              @RequestParam("file") MultipartFile file, Model model, RedirectAttributes redirectAttributes , HttpSession session) throws IOException {
        System.out.println("Name : : : " + topic.getName());

        int id = (int) session.getAttribute("adminId");
        Admin admin = adminService.getAdminById(id);
        System.out.println("Admin ID"+admin.getName());

        if (file.isEmpty() && result.hasErrors()) {
            roleCheckingService.check(session, model);
            model.addAttribute("checkFile", "File must not be empty");
            return "/topic/create";
        } else if (!file.isEmpty() && result.hasErrors()) {
            roleCheckingService.check(session, model);
            return "/topic/create";
        } else if (file.isEmpty() && !result.hasErrors()) {
            roleCheckingService.check(session, model);
            model.addAttribute("checkFile", "File must not be empty");
            return "/topic/create";
        } else {
            byte[] bytes = file.getBytes();
            String encodedString = Base64.getEncoder().encodeToString(bytes);
            model.addAttribute("file", encodedString);
            if (topicService.existsByName(topic.getName())) {
                roleCheckingService.check(session, model);
                model.addAttribute("error", "A topic with the name '" + topic.getName() + "' already exists.");

                return "/topic/create";
            }

            topicService.createTopic(topic, encodedString, admin);
            redirectAttributes.addFlashAttribute("message", "Topic create successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            return "redirect:/topic";
        }
    }



    @GetMapping("/topic")
    public String viewTopic(Model model, HttpSession session) {
        roleCheckingService.check(session,model);
        List<Topic> topics = topicService.getAllTopic();
        model.addAttribute("topics", topics);
        return "/topic/view";
    }


    @GetMapping("/topic/search")
    public String topicSearch(HttpSession session, Model model) {
        roleCheckingService.check(session, model);
        return "/topic/view";
    }
    @PostMapping("/topic/search")
    public String searchTopic(@RequestParam(value = "id", required = false) String topicId, @RequestParam(value = "name", required = false) String name, Model model,HttpSession httpSession) {
        roleCheckingService.check(httpSession,model);
        if ((topicId == null || topicId.isBlank()) && (name == null || name.isBlank())) {
            List<Topic> topics = topicService.getAllTopic();
            model.addAttribute("topics", topics);
            model.addAttribute("error", "Please provide ID or name");
        }
        if (topicId != null && !topicId.isBlank() && name != null && !name.isBlank()) {
            int id = Integer.parseInt(topicId);
            Topic topics = topicService.getByIdOrName(id, name);
            if (topics != null) {
                model.addAttribute("topics", topics);
            } else {
                List<Topic> topic = topicService.getAllTopic();
                model.addAttribute("topic", topic);
                model.addAttribute("error", "Topic not found!");
            }
        }
        if (topicId != null && !topicId.isBlank()) {
            try {
                int id = Integer.parseInt(topicId);
                Optional<Topic> topic = Optional.ofNullable(topicService.getById(id));

                if (topic.isPresent()) {
                    model.addAttribute("topics", topic.get());
                } else {
                    model.addAttribute("error", "Topic not found!");
                    List<Topic> topics = topicService.getAllTopic();
                    model.addAttribute("topics", topics);
                }

            } catch (NumberFormatException e) {
                model.addAttribute("error", "Invalid ID format!");
                List<Topic> topics = topicService.getAllTopic();
                model.addAttribute("topics", topics);
            }
        }
        if (name != null && !name.isBlank()) {
            System.out.println("nsmr" + name);
            Topic topics1 = topicService.getByName(name);
            if (topics1 != null) {
                System.out.println(topics1.getName());
                model.addAttribute("topics", topics1);
            } else {
                model.addAttribute("error", "Topic not found!");
                List<Topic> topics2 = topicService.getAllTopic();
                model.addAttribute("topics", topics2);
            }
        }

        return "/topic/view";
    }


    @GetMapping("/admin/topic/edit/{id}")
    public String editTopic(@PathVariable("id") int id, Model model,HttpSession session) {
        roleCheckingService.check(session,model);
        model.addAttribute("topic", topicService.getById(id));
        return "/topic/edit";
    }

    @PostMapping("/admin/topic/{id}")
    public String updateTopic(@PathVariable("id") int id, @RequestParam("name") String name, @RequestParam("description") String description, @RequestParam("pointPerVote") int pointPerVote, Model model, @RequestParam("file") MultipartFile file,RedirectAttributes redirectAttributes) throws IOException {
        Topic topic = topicService.getById(id);
        topic.setName(name);
        topic.setDescription(description);
        topic.setPointPerVote(pointPerVote);

        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            String encodedString = Base64.getEncoder().encodeToString(bytes);
            topic.setImage(encodedString);
            model.addAttribute("fileName", encodedString);
        } else {
            model.addAttribute("fileName", topic.getImage());
        }
        topicService.updateTopic(topic);
        redirectAttributes.addFlashAttribute("message", "Topic updated successfully!");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        model.addAttribute("topic", topic);
        return "redirect:/topic";
    }



    @GetMapping("/admin/topic/delete/{id}")
    public String deleteTopic (Model model,@PathVariable("id") int id){
        contestantService.deleteAll(id);
        topicService.deleteById(id);
        List<Topic> topics = topicService.getAllTopic();
        model.addAttribute("topics", topics);
        return "redirect:/topic";
    }

    @GetMapping("/topics/sort")
    public String getAllTopics(@RequestParam(required = false, defaultValue = "id") String field, Model model, HttpSession session) {
        roleCheckingService.check(session,model);
        List<Topic> topics = topicService.getAllTopic(field);
        model.addAttribute("topics", topics);
        return "/topic/view";
    }
}