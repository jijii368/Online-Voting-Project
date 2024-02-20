package ace.project.vote.Group.Project.services;

import ace.project.vote.Group.Project.daos.ContestantDao;
import ace.project.vote.Group.Project.dtos.ContestantDto;


import ace.project.vote.Group.Project.models.Contestant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ContestantService {

    @Autowired
    private ContestantDao contestantDao;


    public Contestant createContestant(ContestantDto contestantDto, String image) {

        Contestant contestant=new Contestant();
        contestant.setName(contestantDto.getName());
        contestant.setDescription(contestantDto.getDescription());
        contestant.setImage(image);
        contestant.setTopic(contestantDto.getTopic());
        contestant.setVoteCount(0);
//       contestant.setTopic(contestant.getTopic());
//        Topic topic=(Topic) session.getAttribute("topicId");
//        contestant.setTopic(topic);
        return contestantDao.save(contestant);

    }

    public List<Contestant> getAllContestant(){
        return contestantDao.findAll();
    }

   public Contestant save(Contestant contestant) {
        return contestantDao.save(contestant);
    }

    public Contestant getContestantById(int id){
        return contestantDao.findById(id);
    }

    public void deleteById(int id) {
        contestantDao.deleteById(id);
    }


    public Contestant updateForm(Contestant exitingContestant) {
        return contestantDao.save(exitingContestant);

    }

   /* public List<Contestant> getContestantByTopic(int id) {
        return contestantDao.findByTopicId(id);
    }
*/
    public List<Contestant> getContestantsByTopicOrderedByVoteCount(int id) {
        return contestantDao.findByTopicIdOrderByVoteCountDesc(id);
    }

    public void deleteAll(int id) {
        List<Contestant> contestants=contestantDao.findByTopicIdOrderByVoteCountDesc(id);
        for (Contestant contestant:contestants) {
            contestantDao.delete(contestant);
        }
    }

    public Contestant getContestantWithHighestVoteCount(int id) {
        return  contestantDao.findFirstByTopicIdOrderByVoteCountDesc(id);
    }
}















    /*public void deleteById(int id) {
        contestantDao.deleteById(id);
    }*/

    /*public Contestant updateForm(Contestant exitingContestant) {
        return contestantDao.save(exitingContestant);
    }*/







    /*public Contestant getContestantByTopic(int id) {
        return contestantDao.findByTopicId(id);
    }*/


    /* public Contestant updateForm(Contestant contestant) {
        return contestantDao.save(contestant);
    }*/



/* public Contestant updateForm(int id, ContestantEditDto contestantEditDto, MultipartFile file) throws IOException {
        Contestant contestant=contestantDao.findById(id);
        contestant.setName(contestantEditDto.getName());
        contestant.setDescription(contestantEditDto.getDescription());
        if (!file.isEmpty()){
            byte[] bytes = file.getBytes();
            String encodedString = Base64.getEncoder().encodeToString(bytes);
            contestant.setImage(encodedString);
        }else {
            contestant.getImage();
        }

        return contestantDao.save(contestant);
    }

    public ContestantEditDto entityToDto(Contestant contestant){
        ContestantEditDto dto=new ContestantEditDto();
        dto.setName(contestant.getName());
        dto.setDescription(contestant.getDescription());
        return dto;
    }*/




    /*public ContestantDto entityToDto(Contestant contestant){
       ContestantDto contestantDto=new ContestantDto();
       contestantDto.setName(contestant.getName());
       contestantDto.setDescription(contestant.getDescription()) ;
       contestantDto.setImage(contestant.getImage());
       return contestantDto;
    }

    public Contestant dtoToEntity (ContestantDto contestantDto){
        Contestant contestant=new Contestant();
        contestant.setName(contestantDto.getName());
        contestant.setDescription(contestantDto.getDescription());
        contestant.setImage(contestantDto.getImage());
        return contestantDao.save(contestant);

    }*/

    /*String fileName= StringUtils.cleanPath(file.getOriginalFilename());
        if (fileName.contains("..")){
        System.out.println("not valid");
    }try{
        contestant.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
    } catch (IOException e) {
        e.printStackTrace();
    }


     public Page<Contestant> getAllContestantsPaginated(Pageable pageable) {
        return contestantRepository.findAll(pageable);
    }

    public Page<Contestant> getAllContestantsSortedByNamePaginated(Pageable pageable) {
        return contestantRepository.findAll(Sort.by(Sort.Direction.ASC, "name"), pageable);
    }








    */


