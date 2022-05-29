package persistence.mapper;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import persistence.DTO.IngredientDTO;
import persistence.DTO.RecipeDTO;

import java.util.List;

public interface IngredientMapper {
    final String getRandom = "select ingredientAmount,ingredient.IngredientLink,ingredient.IngredientName from ingredient right join requiredmaterials on ingredient.IngredientNumber = requiredmaterials.IngredientNumber right join\n" +
            "foods on requiredmaterials.foodNum = foods.foodNum where foodName = #{foodName};";
    @Select(getRandom)
    @Results(id = "resultSet", value = {
            @Result(property = "ingredientLink", column = "ingredientLink"),
            @Result(property = "ingredientName", column = "ingredientName"),
            @Result(property = "ingredientAmount", column = "ingredientAmount"),
    })
    List<IngredientDTO> selectIngredient(String foodName);


//    final String FINDID = "SELECT * FROM 사용자 WHERE ID = #{ID}";
//    @Select(FINDID)
//    @ResultMap("resultSet")
//    public List<UserDTO> selectById(String ID);
}
