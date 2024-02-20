package ace.project.vote.Group.Project.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDto {

    @Size(min = 1, max = 20, message = "User name must be between 1 to 20 characters!")
    private String name;

    @NotNull(message = "Email must not be empty!")
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",message = "Email is not valid!")
    private String email;

    @Size(min = 8, max = 30, message = "Password must be between 8 to 30 characters!")
    private String password;

}
