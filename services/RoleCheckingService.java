package ace.project.vote.Group.Project.services;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class RoleCheckingService {

    public void check(HttpSession session, Model model){
        model.addAttribute("title", "Vote");
        if (session.getAttribute("loginAdmin")!=null){
            model.addAttribute("admin", true);
        }
        if (session.getAttribute("loginUser")!=null){
            model.addAttribute("user",true);
        }
        if (session.getAttribute("loginAdmin")!=null || session.getAttribute("loginUser")!=null){
            model.addAttribute("sign", true);
        }
    }
}
