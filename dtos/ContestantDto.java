package ace.project.vote.Group.Project.dtos;


import ace.project.vote.Group.Project.models.Topic;
import ace.project.vote.Group.Project.models.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ContestantDto {

    @NotBlank(message = "Contestant Name must not be empty!")
    @Size(min = 1, max = 20, message = "Contestant name must be between 1 to 20 characters!")
    private String name;

    @NotBlank(message = "Description must not be empty!")
    @Size(min = 1, max = 200, message = "Description must be between 1 to 200 characters!")
    private String description;


//    private String image;

    private Topic topic;

    private int voteCount;

}
