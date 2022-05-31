package persistence.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class FoodStepDTO {
    private int foodNum;
    private String step;
    private BigInteger stepNumber;
    private String foodName;

    public String toString(){
        return " 번 : " + step;
    }


}