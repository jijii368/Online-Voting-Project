package ace.project.vote.Group.Project.dtos;

import jakarta.validation.constraints.Min;
import lombok.Data;



@Data
public class PointChargesDto {

    @Min(value = 1,message = "Amount should be greater than 1!!")
    private int amount;

    @Min(value = 1,message = "Point should be greater than 1!!")
    private int point;
}

