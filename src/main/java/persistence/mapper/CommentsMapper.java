package persistence.mapper;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import persistence.DTO.CommentsDTO;
import persistence.DTO.MemberDTO;
import persistence.DTO.RecipeDTO;

import java.util.List;

public interface CommentsMapper {
    final String getAll = "SELECT * from Comments;";
    @Select(getAll)
    @Results(id = "resultSet", value = {
            @Result(property = "commentsNumber", column = "commentsNumber"),
            @Result(property = "memberNumber", column = "memberNumber"),
            @Result(property = "foodNum", column = "foodNum"),
            @Result(property = "content", column = "content"),

            @Result(property = "memberID", column = "memberID"),
    })
    List<CommentsDTO> getAll();

    final String selectByFoodName = "SELECT memberID, content from Comments join Foods on Foods.foodNum = Comments.foodNum join member on comments.MemberNumber = member.MemberNumber where foodName = #{foodName};";
    @Select(selectByFoodName)
    @ResultMap("resultSet")
    List<CommentsDTO> selectByFoodName(String foodName);

    //list로 바꾸기
    final String INSERT = "INSERT INTO Comments (memberNumber, foodNum, content) VALUES (#{memberNumber}, #{foodNum}, #{content})";
    @Select(INSERT)
    @ResultMap("resultSet")
    void enrollComment(CommentsDTO commentsDTO);


}
