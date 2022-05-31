package persistence.mapper;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import persistence.DTO.MemberDTO;
import persistence.DTO.RecipeDTO;

import java.util.List;

public interface RecipeMapper {
//    SELECT foodName, imgLink, youtubeLink from foods where weatherNum = #{weatherNum} order by rand() limit 2
//    SELECT foodName, imgLink, youtubeLink from foods where seasonNum = #{seasonNum} order by rand() limit 2
    final String getRandom = "SELECT foodName, imgLink, youtubeLink from foods order by rand() limit 4;";
    @Select(getRandom)
    @Results(id = "resultSet", value = {
            @Result(property = "foodNum", column = "foodNum"),
            @Result(property = "foodName", column = "foodName"),
            @Result(property = "imgLink", column = "imgLink"),
            @Result(property = "youtubeLink", column = "youtubeLink"),
            @Result(property = "weatherNum", column = "weatherNum"),
            @Result(property = "seasonNum", column = "seasonNum"),
    })
    List<RecipeDTO> getRandom();

    final String selectNumber = "SELECT foodNum from foods WHERE foodName = #{foodName};";
    @Select(selectNumber)
    @ResultMap("resultSet")
    RecipeDTO selectNumber(String foodName);

    final String getRandomByWeatherNum = "SELECT foodName, imgLink, youtubeLink from foods where weatherNum = #{weatherNum} order by rand() limit 2;";
    @Select(getRandomByWeatherNum)
    @ResultMap("resultSet")
    List<RecipeDTO> getRandomByWeatherNum(int weatherNum);

    final String getRandomBySeasonNum = "SELECT foodName, imgLink, youtubeLink from foods where seasonNum = #{seasonNum} order by rand() limit 2;";
    @Select(getRandomBySeasonNum)
    @ResultMap("resultSet")
    List<RecipeDTO> getRandomBySeasonNum(int seasonNum);


//    final String FINDID = "SELECT * FROM 사용자 WHERE ID = #{ID}";
//    @Select(FINDID)
//    @ResultMap("resultSet")
//    public List<UserDTO> selectById(String ID);
}
