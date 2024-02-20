package ace.project.vote.Group.Project.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "admin")
@Data
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private  String name;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private  Timestamp  updatedAt;

    @OneToMany(mappedBy = "admin")
    private List<PointCharges> pointCharges;

    @OneToMany(mappedBy = "admin")
    private List<Topic> topics;

    @OneToMany(mappedBy = "admin")
    private List<Payment> payments;
}