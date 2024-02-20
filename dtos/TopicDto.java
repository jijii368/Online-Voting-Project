package ace.project.vote.Group.Project.dtos;

import ace.project.vote.Group.Project.models.Admin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class TopicDto {

    @NotBlank(message = "Topic name must not be empty!")
    @Size(min = 1, max = 25,message = "Topic name must not between 1 to 25 characters!")
    private String name;

    @NotBlank(message = "Description must not be empty!")
    @Size(min = 1, max = 200,message = "Topic name must not between 1 to 200 characters!")
    private String description;


   /* private String image;*/
   /*@NotNull
   @Range(min = 1,max = 20,message = "PointPerVote can start between 0 to 20 number!")*/
    private int pointPerVote;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fromDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate toDate;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    private Admin admin;


}