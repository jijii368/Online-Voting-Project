package ace.project.vote.Group.Project.controllers;

import ace.project.vote.Group.Project.models.Topic;
import ace.project.vote.Group.Project.services.RoleCheckingService;
import ace.project.vote.Group.Project.services.TopicService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class PageController {

    @Autowired
    private RoleCheckingService roleCheckingService;

    @Autowired
    private TopicService topicService;

    @GetMapping({"/", "/home"})
    public ModelAndView getMenu(Model model, HttpSession session){
        List<Topic> topics = topicService.getAllTopic();
        roleCheckingService.check(session,model);
        return new ModelAndView("public/home", "topics", topics);
    }
}
