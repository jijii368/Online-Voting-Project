package ace.project.vote.Group.Project.services;

import ace.project.vote.Group.Project.daos.UserDao;
import ace.project.vote.Group.Project.dtos.UserDto;
import ace.project.vote.Group.Project.dtos.UserEditDto;
import ace.project.vote.Group.Project.models.User;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public void createUser(UserDto userDto) {
        User user=new User();
        user.setPoint(0);
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(BCrypt.withDefaults().hashToString(12,userDto.getPassword().toCharArray()));
        userDao.save(user);
    }

    public User getUserById(int id) {
        return userDao.findById(id);
    }

    public User checkUserByEmailAndPassword(String email, String password){
        User user=userDao.findByEmail(email);
        if (user.getEmail().equals(email) && BCrypt.verifyer().verify(password.toCharArray(),user.getPassword()).verified){
            return user;
        }else {
            return null;
        }
    }

    public void updateUser(int id, UserEditDto userEditDto){
        User user=userDao.findById(id);
        user.setName(userEditDto.getName());
        user.setPassword(BCrypt.withDefaults().hashToString(12,userEditDto.getPassword().toCharArray()));
        userDao.save(user);
    }

//    public void deleteUser(int id){
//        userDao.deleteById(id);
//    }

    public UserEditDto entityToDto(User user){
        UserEditDto dto=new UserEditDto();
        dto.setName(user.getName());
        dto.setPassword(user.getPassword());
        return dto;
    }

    public boolean checkEmail(String email){
        if (userDao.findByEmail(email)!=null){
            return true;
        }else {
            return false;
        }
    }

    public User updatePoint(User user) {
        return userDao.save(user);
    }


    public String pictureRotation(){
        String[] pictures={"/assets/img/team/Icons.jpg", "/assets/img/team/pic1.jpg","/assets/img/team/pic2.jpg","/assets/img/team/pic3.jpg"};
        Random ran=new Random();
        return pictures[ran.nextInt(pictures.length)];
    }


    public void updateUserPoint(User user, int point){
        user.setPoint(user.getPoint()+point);
        userDao.save(user);
    }



}
