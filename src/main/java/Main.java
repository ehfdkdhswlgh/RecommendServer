//import persistence.MyBatisConnectionFactory;
//import persistence.dao.*;
import persistence.DAO.FoodStepDAO;
import persistence.DAO.IngredientDAO;
import persistence.DAO.MemberDAO;
import persistence.DAO.RecipeDAO;
import persistence.DTO.FoodStepDTO;
import persistence.DTO.IngredientDTO;
import persistence.DTO.MemberDTO;
import persistence.DTO.RecipeDTO;
import persistence.GpsTransfer;
import persistence.MyBatisConnectionFactory;
import persistence.Weather;
//import service.*;

import java.util.List;

/**
 * 이 클래스는 DB연결 함수확인용
 */

public class Main {

    public static void main(String args[]){

/*
        //DAO 생성
        RecipeDTO testDTO = new RecipeDTO();
//        RecipeDAO test = new RecipeDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        MemberDAO memberDAO = new MemberDAO(MyBatisConnectionFactory.getSqlSessionFactory());

//        GpsTransfer gpsTransfer = new GpsTransfer(36.119485, 128.3445734);
//        gpsTransfer.transfer();
//        System.out.println("결과 : " + gpsTransfer.getStringLat() + "," + gpsTransfer.getStringLon());
//
//        Weather weather = new Weather(gpsTransfer.getStringLat(), gpsTransfer.getStringLon());
//        weather.weather();
//        System.out.println(weather.getWeatherConditions() + ", " + weather.getTemperature());

        System.out.println(memberDAO.login("111","111"));
*/
/*
        RecipeDAO test = new RecipeDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        List<RecipeDTO> tmp2 = test.getRandom();

        System.out.println(tmp2.size());*/

/*        FoodStepDAO foodStepDAO = new FoodStepDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        List<FoodStepDTO> tmp = foodStepDAO.selectFoodStep("김치");
        String step;*/


        IngredientDAO ingredientDAO = new IngredientDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        List<IngredientDTO> tmp = ingredientDAO.selectIngredient("김치볶음밥");
        System.out.println("link1 : " + tmp.get(0).getIngredientLink());
        System.out.println("link2 : " + tmp.get(1).getIngredientLink());
        System.out.println("link1 : " + tmp.get(0).getIngredientName());
        System.out.println("link2 : " + tmp.get(1).getIngredientName());
   /*     for(int i = 0; i<tmp.size(); i++) {
            step = tmp.get(i).getStep();
            System.out.println(step);
        }*/

//        List<RecipeDTO> arr;
//        String s = "";
//        s += test.getRandom();
//        System.out.println(s);
//        System.out.println(memberDAO.idExist("1233"));

//        System.out.println(memberDAO.passwordExist("15123"));


//        System.out.println(memberDAO.login("123", "111"));
//        System.out.println(memberDAO.insertMember("1233", "15123"));
        //추가


    }

}