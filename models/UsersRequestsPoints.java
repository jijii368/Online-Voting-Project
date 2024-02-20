package ace.project.vote.Group.Project.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users_request_points")
public class UsersRequestsPoints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "point_charges_id")
    private PointCharges pointCharges;

    private boolean hasConfirmed;
}
