package ace.project.vote.Group.Project.controllers;


import ace.project.vote.Group.Project.models.Contestant;
import ace.project.vote.Group.Project.models.PointCharges;
import ace.project.vote.Group.Project.models.Topic;
import ace.project.vote.Group.Project.models.User;
import ace.project.vote.Group.Project.services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;


@Controller
public class VoteController {

    @Autowired
    private UserService userService;

    @Autowired
    private ContestantService contestantService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private RoleCheckingService roleCheckingService;


    @GetMapping("/user/contestant/vote")
    public String votePage(
            @RequestParam(name = "id") int id,
            @RequestParam("tid") int tid,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        roleCheckingService.check(session, model);
        int loginUserId = (int) session.getAttribute("loginUserId");
        Topic topic = topicService.getById(tid);
        LocalDate fromDate = topic.getFromDate();
        LocalDate toDate = topic.getToDate();
        LocalDate currentDate = LocalDate.now();


        if ((currentDate.isEqual(ChronoLocalDate.from(fromDate)) || currentDate.isAfter(ChronoLocalDate.from(fromDate))) && (currentDate.isEqual(ChronoLocalDate.from(toDate)) || currentDate.isBefore(ChronoLocalDate.from(toDate)))){
            User user = userService.getUserById(loginUserId);

            if ( user.getPoint() > topic.getPointPerVote() || user.getPoint() == topic.getPointPerVote()) {
                user.setPoint(user.getPoint() - topic.getPointPerVote());
                userService.updatePoint(user);
                model.addAttribute("user", user);

                Contestant contestant = contestantService.getContestantById(id);
                if (contestant != null) {
                    contestant.setVoteCount(contestant.getVoteCount() + 1);
                    contestantService.save(contestant);
                    redirectAttributes.addFlashAttribute("successMessage", "Vote successfully!");
                }
            } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Insufficient points.You need to buy more points to vote.");
          if(user.getPoint() == 0){
              redirectAttributes.addFlashAttribute("currentPoint",true);
          }else {
              redirectAttributes.addFlashAttribute("currentPoints",user.getPoint());
          }


            }
        }

        model.addAttribute("topicId", tid);
        redirectAttributes.addAttribute("id", tid);
        return "redirect:/contestant/view/{id}";
    }

}





























