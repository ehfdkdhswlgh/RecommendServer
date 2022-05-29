package persistence.mapper;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import persistence.DTO.FoodStepDTO;
import persistence.DTO.MemberDTO;
import persistence.DTO.RecipeDTO;

import java.util.List;

public interface FoodStepMapper {
    final String selectAll = "SELECT * from foodstep ";
    @Select(selectAll)
    @Results(id = "resultSet", value = {
            @Result(property = "step", column = "step"),
            @Result(property = "stepNumber", column = "stepNumber"),
            @Result(property = "foodNum", column = "foodNum"),
//            @Result(property = "foodName", column = "foodName")

    })
    List<FoodStepDTO> selectAll();


    final String selectFoodStep = "select stepnumber,step from foodstep join foods on foods.foodNum = foodstep.foodNum where foodName = #{foodName}";
    @Select(selectFoodStep)
    @ResultMap("resultSet")
     List<FoodStepDTO> selectFoodStep(String foodName);


//    final String FINDID = "SELECT * FROM 사용자 WHERE ID = #{ID}";
//    @Select(FINDID)
//    @ResultMap("resultSet")
//    public List<UserDTO> selectById(String ID);

}
