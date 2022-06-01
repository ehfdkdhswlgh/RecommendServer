package persistence.DAO;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.DTO.MemberDTO;
import persistence.DTO.RecipeDTO;
import persistence.mapper.MemberMapper;
import persistence.mapper.RecipeMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecipeDAO {

    private SqlSessionFactory sqlSessionFactory = null;

    public RecipeDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

//    public List<RecipeDTO> getRandom(){
//        SqlSession session = sqlSessionFactory.openSession();
//        RecipeMapper mapper = session.getMapper(RecipeMapper.class);
//        List<RecipeDTO> rnd = mapper.getRandom();
//        for(int i=0; i < rnd.size(); i++){
//            if(rnd.get(i).getYoutubeLink() == null){
//                rnd.get(i).setYoutubeLink("https://www.youtube.com/");
//            }
//        }
//        return rnd;
//    }

    public List<RecipeDTO> getRandom(String weatherNum, String seasonNum){
        SqlSession session = sqlSessionFactory.openSession();
        RecipeMapper mapper = session.getMapper(RecipeMapper.class);
        List<RecipeDTO> rnd1 = mapper.getRandomByWeatherNum(weatherNum);
        List<RecipeDTO> rnd2 = mapper.getRandomBySeasonNum(seasonNum);
        List<RecipeDTO> rnd = new ArrayList<>();

        // 안되면 이걸로 시도
        rnd.addAll(rnd1);
        rnd.addAll(rnd2);
        Collections.swap(rnd, 1, 2);

//        rnd.add(rnd1.get(0));
//        rnd.add(rnd2.get(0));
//        rnd.add(rnd1.get(1));
//        rnd.add(rnd2.get(1));

        for(int i=0; i < rnd.size(); i++){
            if(rnd.get(i).getYoutubeLink() == null){
                rnd.get(i).setYoutubeLink("https://www.youtube.com/");
            }
        }
        return rnd;
    }

    public int selectNumber(String foodName) {
        RecipeDTO recipeDTO = null;
        SqlSession session = sqlSessionFactory.openSession();
        RecipeMapper mapper = session.getMapper(RecipeMapper.class);
        try {
            recipeDTO = mapper.selectNumber(foodName);
            session.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return recipeDTO.getFoodNum();
    }

//    final DataSource ds = PooledDataSource.getDataSource();
//
//    private Connection conn;
//    PreparedStatement pstmt = null;
//    ResultSet rs = null;
//    Statement stmt = null;
//
//    public TestDAO(SqlSessionFactory sqlSessionFactory) {
//        try {
//            conn = ds.getConnection();
//        }catch(Exception e) {
//            try {
//                conn.close();
//            }catch(SQLException e1) {}
//        }
//    }
//
//    public List<TestDTO> getRandom(){
//        SqlSession session = sqlSessionFactory.openSession();
//        TestMapper mapper = session.getMapper(TestMapper.class);
//        List<TestDTO> rnd = mapper.getRandom();
//        return rnd;
//    }

}
