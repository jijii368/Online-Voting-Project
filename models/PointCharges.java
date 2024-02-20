package ace.project.vote.Group.Project.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "pointCharges")
@Data
public class PointCharges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private int point;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "pointCharges")
    private List<UsersRequestsPoints> usersRequestsPoints;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

}
