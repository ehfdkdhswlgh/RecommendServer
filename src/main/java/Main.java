//import persistence.MyBatisConnectionFactory;
//import persistence.dao.*;
import persistence.DAO.CommentsDAO;
import persistence.DAO.MemberDAO;
import persistence.DAO.RecipeDAO;
import persistence.DTO.CommentsDTO;
import persistence.DTO.MemberDTO;
import persistence.DTO.RecipeDTO;
import persistence.GpsTransfer;
import persistence.MyBatisConnectionFactory;
import persistence.Weather;
import persistence.mapper.CommentsMapper;
//import service.*;

import java.util.Collections;
import java.util.List;

/**
 * 이 클래스는 DB연결 함수확인용
 */

public class Main {

    public static void main(String args[]){

        //DAO 생성
        RecipeDTO testDTO = new RecipeDTO();
        RecipeDAO recipeDAO = new RecipeDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        MemberDAO memberDAO = new MemberDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        CommentsDAO commentsDAO = new CommentsDAO(MyBatisConnectionFactory.getSqlSessionFactory());

//        GpsTransfer gpsTransfer = new GpsTransfer(36.119485, 128.3445734);
//        gpsTransfer.transfer();
//        System.out.println("결과 : " + gpsTransfer.getStringLat() + "," + gpsTransfer.getStringLon());
//
//        Weather weather = new Weather(gpsTransfer.getStringLat(), gpsTransfer.getStringLon());
//        weather.weather();
//        System.out.println(weather.getWeatherConditions() + ", " + weather.getTemperature());

//        System.out.println(memberDAO.login("111","111"));

//        List<CommentsDTO> list = commentsDAO.selectByFoodName("열무김치 맛있게 담그는법(전라도김치)");
//
//        for(int i=0; i<list.size(); i++){
//            System.out.print(list.get(i).getMemberID() + " ");
//            System.out.print(list.get(i).getContent() + " ");
//
//            System.out.println();
//        }

        System.out.println(memberDAO.selectNumber("111"));
        System.out.println(recipeDAO.selectNumber("열무김치 맛있게 담그는법(전라도김치)"));

//        commentsDAO.enrollComment(6, 1, "새로운 댓글 61");


//        List<RecipeDTO> tmp2 = recipeDAO.getRandom();
////        RecipeDTO tmp3 = tmp2.get(1);
//        System.out.println(tmp2.get(1));
//        System.out.println(tmp2.get(2));
//
//        Collections.swap(tmp2, 1, 2);
//
//        System.out.println(tmp2.get(1));
//        System.out.println(tmp2.get(2));

//        tmp3.add(tmp2.get(0));
//        tmp3.add(tmp2.get(2));
//        tmp3.add(tmp2.get(1));
//        tmp3.add(tmp2.get(3));

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