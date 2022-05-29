package persistence.DAO;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.DTO.FoodStepDTO;
import persistence.DTO.IngredientDTO;
import persistence.DTO.MemberDTO;
import persistence.mapper.FoodStepMapper;
import persistence.mapper.IngredientMapper;
import persistence.mapper.MemberMapper;

import java.util.List;

public class IngredientDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public IngredientDAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public List<IngredientDTO> selectIngredient(String foodName){
        List<IngredientDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        IngredientMapper mapper = session.getMapper(IngredientMapper.class);

        try{
            list = mapper.selectIngredient(foodName);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            session.close();
        }
        return list;
    }
}