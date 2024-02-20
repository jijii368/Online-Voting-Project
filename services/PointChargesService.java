package ace.project.vote.Group.Project.services;

import ace.project.vote.Group.Project.daos.PointChargesDao;
import ace.project.vote.Group.Project.dtos.PointChargesDto;
import ace.project.vote.Group.Project.models.PointCharges;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PointChargesService {

    @Autowired
    private PointChargesDao pointChargesDao;

    public PointCharges createPointCharges(PointChargesDto pointChargesDto) {
        PointCharges pointCharges = new PointCharges();
        pointCharges.setPoint(pointChargesDto.getPoint());
        pointCharges.setAmount(pointChargesDto.getAmount());
        return pointChargesDao.save(pointCharges);
    }


    public List<PointCharges> getAllPointCharges() {
        return pointChargesDao.findAll();
    }

    public PointCharges getById(int id) {
        return pointChargesDao.findById(id);


    }

    public void deleteById(int id) {
        pointChargesDao.deleteById(id);
    }

    public PointCharges save(PointCharges pointCharges) {
        return pointChargesDao.save(pointCharges);
    }

    public PointCharges updatePointCharges(PointCharges pointCharges) {
        return pointChargesDao.save(pointCharges);
    }

}





