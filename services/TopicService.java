package ace.project.vote.Group.Project.services;

import ace.project.vote.Group.Project.daos.TopicDao;
import ace.project.vote.Group.Project.dtos.TopicDto;
import ace.project.vote.Group.Project.models.Admin;
import ace.project.vote.Group.Project.models.Topic;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TopicService {

    @Autowired
    private TopicDao topicDao;


    public Topic createTopic(TopicDto topicDto, String image, Admin admin) {


        System.out.println(topicDto.getName());
        System.out.println(topicDto.getDescription());

        System.out.println(topicDto.getPointPerVote());
        Topic topic = new Topic();
        topic.setName(topicDto.getName());
        topic.setDescription(topicDto.getDescription());
        /*topic.setImage(topicDto.getImage());*/
        topic.setImage(image);
        topic.setPointPerVote(topicDto.getPointPerVote());
        topic.setFromDate(topicDto.getFromDate());
        topic.setToDate(topicDto.getToDate());
        topic.setAdmin(admin);
        return topicDao.save(topic);
    }

    public List<Topic>getAllTopic(){
        return topicDao.findAll();
    }

    public List<Topic> getAllTopic(String field) {
        return topicDao.findAll(Sort.by(Sort.Direction.ASC, field));
    }

    public Topic getById(int id) {
        return topicDao.findById(id);


    }

    public void deleteById(int id) {
        topicDao.deleteById(id);
    }

    public Topic save(Topic topic) {
        return topicDao.save(topic);
    }

    public Topic updateTopic(Topic topic) {
        return topicDao.save(topic);
    }

    public Topic getByName(String name) {
        return  topicDao.findByName(name);
    }

    public Topic getByIdOrName(int id, String name) {
        return topicDao.findByIdOrName(id, name);
    }

    public boolean existsByName(String name) {
        return  topicDao.existsByName(name);

    }

}



















