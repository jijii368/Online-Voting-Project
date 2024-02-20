package ace.project.vote.Group.Project.services;
import ace.project.vote.Group.Project.daos.AdminDao;
import ace.project.vote.Group.Project.dtos.AdminDto;
import ace.project.vote.Group.Project.dtos.AdminEditDto;
import ace.project.vote.Group.Project.models.Admin;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AdminService {

    @Autowired
    private AdminDao adminDao;

    public void createAdmin(AdminDto adminDto){
        Admin admin=new Admin();
        admin.setName(adminDto.getName());
        admin.setPassword(BCrypt.withDefaults().hashToString(12,adminDto.getPassword().toCharArray()));
        adminDao.save(admin);
    }

    public List<Admin> getAll(){
        return adminDao.findAll();
    }

    public Admin getAdminById(int id) {
        return adminDao.findById(id);
    }
    public Admin checkByNameAndPassword(String name, String password){
        Admin admin=adminDao.findByName(name);
        if (admin.getName().equals(name) && BCrypt.verifyer().verify(password.toCharArray(), admin.getPassword()).verified){
            return admin;
        }else {
            return null;
        }
    }

    public void updateAdmin(int id, AdminEditDto adminEditDto){
        Admin admin=adminDao.findById(id);
        admin.setName(adminEditDto.getName());
        admin.setPassword(BCrypt.withDefaults().hashToString(12,adminEditDto.getPassword().toCharArray()));
        adminDao.save(admin);
    }

//    public void deleteAdmin(int id){
//        adminDao.deleteById(id);
//    }

    public AdminEditDto entityToDto(Admin admin){
        AdminEditDto dto=new AdminEditDto();
        dto.setName(admin.getName());
        dto.setPassword(admin.getPassword());
        return dto;
    }

    public boolean checkName(String name){
        if (adminDao.findByName(name)!=null){
            return true;
        }else {
            return false;
        }
    }
}