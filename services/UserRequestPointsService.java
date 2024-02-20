package ace.project.vote.Group.Project.services;

import ace.project.vote.Group.Project.daos.UserRequestPointsDao;
import ace.project.vote.Group.Project.models.Payment;
import ace.project.vote.Group.Project.models.PointCharges;
import ace.project.vote.Group.Project.models.User;
import ace.project.vote.Group.Project.models.UsersRequestsPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRequestPointsService {

    @Autowired
    private UserRequestPointsDao userRequestPointsDao;

    public void createUserRequestPoint(User user, Payment payment, PointCharges pointCharges){
        UsersRequestsPoints usersRequestsPoints=new UsersRequestsPoints();
        usersRequestsPoints.setUser(user);
        usersRequestsPoints.setPayment(payment);
        usersRequestsPoints.setPointCharges(pointCharges);
        userRequestPointsDao.save(usersRequestsPoints);
    }

    public void updateHasConfirmed(UsersRequestsPoints usersRequestsPoints){
        usersRequestsPoints.setHasConfirmed(true);
        userRequestPointsDao.save(usersRequestsPoints);
    }

    public List<UsersRequestsPoints> getAllRequestPoints(){
        return userRequestPointsDao.findAll();
    }

    public List<UsersRequestsPoints> getRequestPointsByUser(int id){
        return userRequestPointsDao.findByUserId(id);
    }

    public void deleteUserRequestPoint(int id){
        userRequestPointsDao.deleteById(id);
    }

    public UsersRequestsPoints getById(int id){
        return userRequestPointsDao.findById(id);
    }
}
