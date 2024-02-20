package ace.project.vote.Group.Project.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AdminDto {


    @Size(min = 1, max = 20, message = " name must be between 1 to 20 characters!")
    private String name;

    @NotNull(message = "Password must not be empty!")
    @Size(min = 8, max = 30, message = "Password must be between 8 to 30 characters!")
    private String password;
}