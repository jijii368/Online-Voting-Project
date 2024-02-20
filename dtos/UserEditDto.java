package ace.project.vote.Group.Project.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserEditDto {

    @Size(min = 1, max = 20, message = "User name must be between 1 to 20 characters!")
    private String name;


    @Size(min = 8, max = 30, message = "Password must be between 8 to 30 characters!")
    private String  password;
}
