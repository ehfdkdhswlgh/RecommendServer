package persistence.DAO;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.DTO.CommentsDTO;
import persistence.DTO.MemberDTO;
import persistence.DTO.RecipeDTO;
import persistence.mapper.CommentsMapper;
import persistence.mapper.MemberMapper;
import persistence.mapper.RecipeMapper;

import java.util.List;

public class CommentsDAO {
        private SqlSessionFactory sqlSessionFactory = null;

        public CommentsDAO(SqlSessionFactory sqlSessionFactory){
            this.sqlSessionFactory = sqlSessionFactory;
        }


        public List<CommentsDTO> getAll(){
            SqlSession session = sqlSessionFactory.openSession();
            CommentsMapper mapper = session.getMapper(CommentsMapper.class);
            List<CommentsDTO> list = mapper.getAll();
            return list;
        }

    public List<CommentsDTO> selectByFoodName(String foodName){
        SqlSession session = sqlSessionFactory.openSession();
        CommentsMapper mapper = session.getMapper(CommentsMapper.class);
        List<CommentsDTO> list = mapper.selectByFoodName(foodName);
        return list;
    }

    // 타입 void로?
    public void enrollComment(int memberNumber, int foodNum, String comments) {
            CommentsDTO commentsDTO = new CommentsDTO(memberNumber, foodNum, comments);
            SqlSession session = sqlSessionFactory.openSession();
            CommentsMapper mapper = session.getMapper(CommentsMapper.class);
            try {
                mapper.enrollComment(commentsDTO);
                session.commit();
            }catch (Exception e){
                e.printStackTrace();
                session.rollback();
            } finally {
                session.close();
            }
        }

}

