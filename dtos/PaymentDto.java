package ace.project.vote.Group.Project.dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PaymentDto {

    @Size(min = 1, max = 20, message = "Name must be between 1 to 20 characters!")
    private String name;

    @Pattern(regexp = "^[0-9]{8,17}$", message = "Account number must be between 8 to 17 and must be numbers!")
    private String accountNumber;

    @Pattern(regexp = "^09[0-9]{9}$", message = "Your phone number is not valid!")
    private String phoneNumber;
}
