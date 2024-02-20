package ace.project.vote.Group.Project.services;

import ace.project.vote.Group.Project.daos.PaymentDao;
import ace.project.vote.Group.Project.dtos.PaymentDto;
import ace.project.vote.Group.Project.models.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    PaymentDao paymentDao;

    @Autowired
    AdminService adminService;

    public void createPayment(PaymentDto paymentDto, int adminId){
        Payment payment=new Payment();
        payment.setName(paymentDto.getName());
        payment.setAccountNumber(paymentDto.getAccountNumber());
        payment.setPhoneNumber(paymentDto.getPhoneNumber());
        payment.setAdmin(adminService.getAdminById(adminId));
        paymentDao.save(payment);
    }

    public List<Payment> getAllPayment(){

        return paymentDao.findAll();
    }

    public Payment getPaymentById(int id){

        return paymentDao.findById(id);
    }

    public void deletePaymentById(int id){

        paymentDao.deleteById(id);
    }

    public void updatePayment(int id, PaymentDto paymentDto){
        Payment payment=paymentDao.findById(id);
        payment.setName(paymentDto.getName());
        payment.setAccountNumber(paymentDto.getAccountNumber());
        payment.setPhoneNumber(paymentDto.getPhoneNumber());
        paymentDao.save(payment);
    }

    public PaymentDto entityToDto (Payment payment){
        PaymentDto dto=new PaymentDto();
        dto.setName(payment.getName());
        dto.setAccountNumber(payment.getAccountNumber());
        dto.setPhoneNumber(payment.getPhoneNumber());
        return dto;
    }
}
