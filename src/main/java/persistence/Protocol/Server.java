package persistence.Protocol;

import persistence.DAO.*;
import persistence.DTO.CommentsDTO;
import persistence.DTO.FoodStepDTO;
import persistence.DTO.IngredientDTO;
import persistence.DTO.RecipeDTO;
import persistence.GpsTransfer;
import persistence.MyBatisConnectionFactory;
import persistence.Weather;

import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static persistence.Protocol.Protocol.*;

//import static persistence.Protocol.Protocol.TYPE_REQUEST;

public class Server {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Socket conn = null;
        ServerSocket sSocket = null;


        try {
//            임시로 3010 포트 사용중
            sSocket = new ServerSocket(3000, 10);
            //* nullexception 발생시  - > 데이터베이스 접속하기 + 포트번호 수정하기
            System.out.println("클라이언트 접속 대기중...");


            while (true) {

                conn = sSocket.accept();
                System.out.println("클라이언트 " + conn.getInetAddress().getHostName() + " 가 접속하였습니다.");
                new handler(conn).start();
            }
        } catch (IOException e) {
            System.err.println("IOException");
        }

        try {
            sSocket.close();
        } catch (IOException ioException) {
            System.err.println("Unable to close. IOexception");
        }

    }


    static class handler extends Thread {

        private String loginId;
        private String foodName;

        private String weatherNum;
        private String seasonNum;

        private Socket conn;


        handler(Socket conn) {
            this.conn = conn;
        }


        public void run() {

            Protocol recommendFood = new Protocol(TYPE_REQUEST, CODE_RECOMMENDFOOD);

            RecipeDAO recipeDAO = new RecipeDAO(MyBatisConnectionFactory.getSqlSessionFactory());
            MemberDAO memberDAO = new MemberDAO(MyBatisConnectionFactory.getSqlSessionFactory());
            FoodStepDAO foodStepDAO = new FoodStepDAO(MyBatisConnectionFactory.getSqlSessionFactory());
            IngredientDAO ingredientDAO = new IngredientDAO(MyBatisConnectionFactory.getSqlSessionFactory());
            CommentsDAO commentsDAO = new CommentsDAO(MyBatisConnectionFactory.getSqlSessionFactory());

            try {
                OutputStream os = conn.getOutputStream();
                BufferedOutputStream bos = new BufferedOutputStream(os);
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);

                String id = "";
                String password = "";
                String data = "";



                boolean isEnd = false;

                while (!isEnd) {

                    byte[] buf = recommendFood.getPacket();
                    is.read(buf);
                    int protocolType = buf[0];
                    int protocolCode = buf[1];

                    recommendFood.setPacket(protocolType, protocolCode, buf); // 패킷 타입을 Protocol 객체의 packet 멤버변수에 buf를 복사

                    switch (protocolCode) {
                        case CODE_RECOMMENDFOOD:
                            switch (protocolType) {
                                case TYPE_REQUEST:
                                    System.out.println("요리추천패킷정상수신");
                                    Protocol proto = new Protocol(TYPE_RESPONSE, CODE_RECOMMENDFOOD);
                                    String latitude = null, longitude = null; // 위도 경도

                                    int pos = 2;

                                    byte[] tmpArr = Arrays.copyOfRange(buf, pos, pos + 4);

                                    int latitudeLength = Protocol.byteArrayToInt(tmpArr);
                                    pos +=4;

                                    byte[] latitudeArr = Arrays.copyOfRange(buf, pos, pos + latitudeLength);
                                    try {
                                        latitude = new String(latitudeArr, "UTF-8");//추출 이름 String 변환해 저장
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    pos += latitudeLength;

                                    int longitudeLength = Protocol.byteArrayToInt(Arrays.copyOfRange(buf, pos, pos + 4));
                                    pos +=4;

                                    byte[] longitudeArr = Arrays.copyOfRange(buf, pos, pos + longitudeLength);
                                    try {
                                        longitude = new String(longitudeArr, "UTF-8");//추출 이름 String 변환해 저장
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
//                                    pos += longitudeLength;
                                    pos = 2;

                                    System.out.println("위도 : " + latitude + ", 경도 : " + longitude);
                                    GpsTransfer gpsTransfer = new GpsTransfer(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                    gpsTransfer.transfer(); // 격자좌표계로 변환
                                    Weather weather = new Weather(gpsTransfer.getStringLat(), gpsTransfer.getStringLon());
                                    weather.weather();

                                    weatherNum = weather.getWeatherNum();
                                    seasonNum = weather.getSeasonNum();
                                    
                                    byte[] sendBuf = proto.getPacket();//실제 최종 보낼 패킷


                                    //temp += testDAO.getRandom();
                                    String foodName;
                                    String foodURLName;
                                    String youtubeLinkName;
                                    String weatherConditions;
                                    String temperature;
                                    int foodNameLength;
                                    int foodURLNameLength;
                                    int youtubeLinkLength;
                                    int weatherConditionsLength;
                                    int temperatureLength;

//                                    List<RecipeDTO> tmp = recipeDAO.getRandom();

                                    List<RecipeDTO> tmp = recipeDAO.getRandom(weatherNum, seasonNum);
                                    Collections.swap(tmp, 1, 2);

                                    weatherConditions = weather.getWeatherConditions();
                                    System.out.println(weatherConditionsLength = weatherConditions.getBytes().length);
                                    temperature = weather.getTemperature();
                                    System.out.println(temperatureLength = temperature.getBytes().length);

                                    byte[] temp1 = proto.intToByteArray(weatherConditionsLength); //날씨 상태 길이
                                    System.arraycopy(temp1, 0, sendBuf, pos, 4);
                                    pos += 4; // 4증가

                                    byte[] temp2 = weatherConditions.getBytes(); //날씨 상태 실제 데이터
                                    System.arraycopy(temp2, 0, sendBuf, pos, temp2.length);
                                    pos += temp2.length;

                                    byte[] temp3 = proto.intToByteArray(temperatureLength); // 기온 길이
                                    System.arraycopy(temp3, 0, sendBuf, pos, 4);
                                    pos += 4; // 4증가

                                    byte[] temp4 = temperature.getBytes(); //기온 실제 데이터
                                    System.arraycopy(temp4, 0, sendBuf, pos, temp4.length);
                                    pos += temp4.length;



                                    for(int i = 0; i < tmp.size(); i++){



                                        foodName = tmp.get(i).getFoodName();
                                        System.out.println(foodNameLength = foodName.getBytes().length);
                                        foodURLName = tmp.get(i).getImgLink();
                                        System.out.println(foodURLNameLength = foodURLName.getBytes().length);
                                        youtubeLinkName = tmp.get(i).getYoutubeLink();
                                        System.out.println(youtubeLinkLength = youtubeLinkName.getBytes().length);


                                        byte[] temp5 = proto.intToByteArray(foodNameLength); // 이름 길이
                                        System.arraycopy(temp5, 0, sendBuf, pos, 4);
                                        pos += 4; // 4증가

                                        byte[] temp6 = foodName.getBytes(); //이름 실제 데이터
                                        System.arraycopy(temp6, 0, sendBuf, pos, temp6.length);
                                        pos += temp6.length;

                                        byte[] temp7 = proto.intToByteArray(foodURLNameLength); //URL 길이
                                        System.arraycopy(temp7, 0, sendBuf, pos, 4);
                                        pos += 4;

                                        byte[] temp8 = foodURLName.getBytes(); //URL 실제 데이터
                                        System.arraycopy(temp8, 0, sendBuf, pos, temp8.length);
                                        pos += temp8.length;

                                        byte[] temp9 = proto.intToByteArray(youtubeLinkLength); //youbeLink 길이
                                        System.arraycopy(temp9, 0, sendBuf, pos, 4);
                                        pos += 4;

                                        byte[] temp10 = youtubeLinkName.getBytes(); //URL 실제 데이터
                                        System.arraycopy(temp10, 0, sendBuf, pos, temp10.length);
                                        pos += temp10.length;

                                    }

                                    bos.write(sendBuf);
                                    bos.flush();

                                    break;
                            }
                            break;

                        case CODE_RESET_RECOMMENDFOOD:
                            switch (protocolType) {
                                case TYPE_REQUEST:
                                    System.out.println("새로그침 정상수신");
                                    Protocol proto = new Protocol(TYPE_RESPONSE, CODE_RESET_RECOMMENDFOOD);

                                    int pos = 2;

                                    byte[] sendBuf = proto.getPacket();

                                    String foodName;
                                    String foodURLName;
                                    String youtubeLinkName;
                                    int foodNameLength;
                                    int foodURLNameLength;
                                    int youtubeLinkLength;

                                    List<RecipeDTO> tmp = recipeDAO.getRandom(weatherNum, seasonNum);
                                    Collections.swap(tmp, 1, 2);

                                    for(int i = 0; i < tmp.size(); i++){

                                        foodName = tmp.get(i).getFoodName();
                                        System.out.println(foodNameLength = foodName.getBytes().length);
                                        foodURLName = tmp.get(i).getImgLink();
                                        System.out.println(foodURLNameLength = foodURLName.getBytes().length);
                                        youtubeLinkName = tmp.get(i).getYoutubeLink();
                                        System.out.println(youtubeLinkLength = youtubeLinkName.getBytes().length);


                                        byte[] temp5 = proto.intToByteArray(foodNameLength); // 이름 길이
                                        System.arraycopy(temp5, 0, sendBuf, pos, 4);
                                        pos += 4; // 4증가

                                        byte[] temp6 = foodName.getBytes(); //이름 실제 데이터
                                        System.arraycopy(temp6, 0, sendBuf, pos, temp6.length);
                                        pos += temp6.length;

                                        byte[] temp7 = proto.intToByteArray(foodURLNameLength); //URL 길이
                                        System.arraycopy(temp7, 0, sendBuf, pos, 4);
                                        pos += 4;

                                        byte[] temp8 = foodURLName.getBytes(); //URL 실제 데이터
                                        System.arraycopy(temp8, 0, sendBuf, pos, temp8.length);
                                        pos += temp8.length;

                                        byte[] temp9 = proto.intToByteArray(youtubeLinkLength); //youbeLink 길이
                                        System.arraycopy(temp9, 0, sendBuf, pos, 4);
                                        pos += 4;

                                        byte[] temp10 = youtubeLinkName.getBytes(); //URL 실제 데이터
                                        System.arraycopy(temp10, 0, sendBuf, pos, temp10.length);
                                        pos += temp10.length;

                                    }

                                    bos.write(sendBuf);
                                    bos.flush();


                                    break;
                            }
                            break;


                        case CODE_LOGIN:
                            switch (protocolType) {
                                case TYPE_REQUEST:
                                    System.out.println("로그인요청 정상수신");
                                    Protocol proto = null;

                                    int type = buf[0]; //타입
                                    System.out.println(type);
                                    int code = buf[1]; //코드
                                    System.out.println(code);

                                    loginId = null;
                                    String loginPassword = null;
                                    int pos = 2;


                                    byte[] tmp = Arrays.copyOfRange(buf, pos, pos + 4);
                                    int loginIdLength = Protocol.byteArrayToInt(tmp);
                                    pos +=4;

                                    byte[] loginIdArr = Arrays.copyOfRange(buf, pos, pos + loginIdLength);
                                    try {
                                            loginId = new String(loginIdArr, "UTF-8");//추출 이름 String 변환해 저장
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                    pos += loginIdLength;

                                    int loginPasswordLength = Protocol.byteArrayToInt(Arrays.copyOfRange(buf, pos, pos + 4));
                                    pos +=4;

                                    byte[] loginPasswordArr = Arrays.copyOfRange(buf, pos, pos + loginPasswordLength);
                                    try {
                                        loginPassword = new String(loginPasswordArr, "UTF-8");//추출 이름 String 변환해 저장
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    pos += loginPasswordLength;

                                    boolean loginSuccess = memberDAO.login(loginId, loginPassword);
                                    if(loginSuccess)
                                        proto = new Protocol(TYPE_RESPONSE, CODE_LOGIN);
                                    else
                                        proto = new Protocol(TYPE_RESPONSE_ERROR, CODE_LOGIN);

                                    bos.write(proto.getPacket());
                                    bos.flush();
                                    break;
                            }
                            break;

                        case CODE_SIGNUP:
                            switch (protocolType) {
                                case TYPE_REQUEST:
                                    System.out.println("회원가입요청 정상수신");
                                    Protocol proto = null;

                                    int type = buf[0]; //타입
                                    System.out.println(type);
                                    int code = buf[1]; //코드
                                    System.out.println(code);

                                    String signUpId = null, signUpPassword = null;
                                    int pos = 2;


                                    byte[] tmp = Arrays.copyOfRange(buf, pos, pos + 4);
                                    int signUpIdLength = Protocol.byteArrayToInt(tmp);
                                    pos +=4;

                                    byte[] signUpIdArr = Arrays.copyOfRange(buf, pos, pos + signUpIdLength);
                                    try {
                                        signUpId = new String(signUpIdArr, "UTF-8");//추출 이름 String 변환해 저장
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    pos += signUpIdLength;

                                    int signUpPasswordLength = Protocol.byteArrayToInt(Arrays.copyOfRange(buf, pos, pos + 4));
                                    pos +=4;

                                    byte[] signUpPasswordArr = Arrays.copyOfRange(buf, pos, pos + signUpPasswordLength);
                                    try {
                                        signUpPassword = new String(signUpPasswordArr, "UTF-8");//추출 이름 String 변환해 저장
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    pos += signUpPasswordLength;

                                    boolean signUpSuccess = memberDAO.signUp(signUpId, signUpPassword);
                                    if(signUpSuccess)
                                        proto = new Protocol(TYPE_RESPONSE, CODE_SIGNUP);
                                    else
                                        proto = new Protocol(TYPE_RESPONSE_ERROR, CODE_SIGNUP);

                                    bos.write(proto.getPacket());
                                    bos.flush();
                                    break;
                            }
                            break;
                        case CODE_DETAIL_FOOD_INFO:
                            switch (protocolType) {
                                case TYPE_REQUEST:
                                    System.out.println("세부정보요청 정상수신");
                                    Protocol proto = new Protocol(TYPE_RESPONSE, CODE_DETAIL_FOOD_INFO);

                                    byte[] sendBuf = proto.getPacket();

                                    int type = buf[0]; //타입
                                    System.out.println(type);
                                    int code = buf[1]; //코드
                                    System.out.println(code);

                                    foodName = null;
                                    int pos = 2;


                                    byte[] tmp = Arrays.copyOfRange(buf, pos, pos + 4);
                                    int foodNameLength = Protocol.byteArrayToInt(tmp);
                                    pos +=4;

                                    byte[] foodNameArr = Arrays.copyOfRange(buf, pos, pos + foodNameLength);
                                    try {
                                        foodName = new String(foodNameArr, "UTF-8");//추출 이름 String 변환해 저장
                                        System.out.println(foodName);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
//                                    pos += foodNameLength;
                                    pos = 2;

                                    List<FoodStepDTO> foodStepList = foodStepDAO.selectFoodStep(foodName);
                                    List<CommentsDTO> commentsList = commentsDAO.selectByFoodName(foodName);
                                    List<IngredientDTO> ingredientList = ingredientDAO.selectIngredient(foodName);


                                    byte[] temp1 = proto.intToByteArray(foodStepList.size()); // '요리순서 수'의 길이
                                    System.arraycopy(temp1, 0, sendBuf, pos, 4);
                                    pos += 4; // 4증가

                                    byte[] temp2 = proto.intToByteArray(commentsList.size()); // '댓글 수'의 길이
                                    System.arraycopy(temp2, 0, sendBuf, pos, 4);
                                    pos += 4; // 4증가

                                    byte[] temp3 = proto.intToByteArray(ingredientList.size()); // '재료 수'의 길이
                                    System.arraycopy(temp3, 0, sendBuf, pos, 4);
                                    pos += 4; // 4증가

                                    for(int i=0; i<foodStepList.size(); i++){
                                        String foodStep = i+1 + "" + foodStepList.get(i).toString();
                                        int foodStepLength;
                                        System.out.println(foodStepLength = foodStep.getBytes().length);

                                        byte[] temp5 = proto.intToByteArray(foodStepLength); // 이름 길이
                                        System.arraycopy(temp5, 0, sendBuf, pos, 4);
                                        pos += 4; // 4증가

                                        byte[] temp6 = foodStep.getBytes(); //이름 실제 데이터
                                        System.arraycopy(temp6, 0, sendBuf, pos, temp6.length);
                                        pos += temp6.length;
                                        System.out.println("foodStep : " + foodStep);
                                    }

                                    for(int i=0; i<commentsList.size(); i++){
                                        String comments = commentsList.get(i).toString();
                                        int commentsLength;
                                        System.out.println(commentsLength = comments.getBytes().length);

                                        byte[] temp5 = proto.intToByteArray(commentsLength); // 이름 길이
                                        System.arraycopy(temp5, 0, sendBuf, pos, 4);
                                        pos += 4; // 4증가

                                        byte[] temp6 = comments.getBytes(); //이름 실제 데이터
                                        System.arraycopy(temp6, 0, sendBuf, pos, temp6.length);
                                        pos += temp6.length;
                                        System.out.println("comments : " + comments);
                                    }

                                    for(int i=0; i<ingredientList.size(); i++){
                                        String ingredient = ingredientList.get(i).getIngredientName();
                                        int ingredientLength;
                                        System.out.println(ingredientLength = ingredient.getBytes().length);
                                        String ingredientLink = ingredientList.get(i).getIngredientLink();
                                        int ingredientLinkLength;
                                        System.out.println(ingredientLinkLength = ingredientLink.getBytes().length);

                                        byte[] temp5 = proto.intToByteArray(ingredientLength); // 이름 길이
                                        System.arraycopy(temp5, 0, sendBuf, pos, 4);
                                        pos += 4; // 4증가

                                        byte[] temp6 = ingredient.getBytes(); //이름 실제 데이터
                                        System.arraycopy(temp6, 0, sendBuf, pos, temp6.length);
                                        pos += temp6.length;

                                        byte[] temp7 = proto.intToByteArray(ingredientLinkLength); // 이름 길이
                                        System.arraycopy(temp7, 0, sendBuf, pos, 4);
                                        pos += 4; // 4증가

                                        byte[] temp8 = ingredientLink.getBytes(); //이름 실제 데이터
                                        System.arraycopy(temp8, 0, sendBuf, pos, temp8.length);
                                        pos += temp8.length;
                                        System.out.println("ingredient : " + ingredient);
                                        System.out.println("ingredientLink : " + ingredientLink);
                                    }


                                    bos.write(proto.getPacket());
                                    bos.flush();
                                    break;
                            }
                            break;
                        case CODE_COMMENT_LEAVE:
                            switch (protocolType) {
                                case TYPE_REQUEST:
                                    System.out.println("댓글등록 정상수신");
                                    Protocol proto = null;

                                    int type = buf[0]; //타입
                                    System.out.println(type);
                                    int code = buf[1]; //코드
                                    System.out.println(code);

                                    String comment = null;
                                    int pos = 2;


                                    byte[] tmp = Arrays.copyOfRange(buf, pos, pos + 4);
                                    int commentLength = Protocol.byteArrayToInt(tmp);
                                    pos +=4;

                                    byte[] signUpIdArr = Arrays.copyOfRange(buf, pos, pos + commentLength);
                                    try {
                                        comment = new String(signUpIdArr, "UTF-8");//추출 이름 String 변환해 저장
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    pos += commentLength;

                                    int memberNumber = memberDAO.selectNumber(loginId);
                                    int foodNum = recipeDAO.selectNumber(foodName);

                                    commentsDAO.enrollComment(memberNumber, foodNum, comment);

//                                    proto = new Protocol(TYPE_RESPONSE, CODE_COMMENT_LEAVE);
//
//                                    bos.write(proto.getPacket());
//                                    bos.flush();
                                    break;
                            }
                                break;
                    }

                }
                conn.close();

            } catch (IOException e) {
                System.out.println("IOException on socket : " + e);
                e.printStackTrace();
            }

        }


    }
}






