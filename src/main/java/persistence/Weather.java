package persistence;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class Weather {
    private String nx_data;
    private String ny_data;

    private String weatherConditions;
    private String temperature;

    private int intTemp;
    private String seasonNum;
    private String weaherNum;
//    기준 : 4 - 비옴, 26 > 더움(2), 26 < 맑음(1) < 15, 쌀쌀함(3) < 15
//            3~5월이 봄(1), 6~8월이 여름(2), 9~11월이 가을(3), 12~2월이 겨울(4)

    public String getWeatherNum(){
        intTemp = Integer.parseInt(temperature);

        if(weaherNum != null && weaherNum.equals("4")){
            return weaherNum;
        }
        else {
            if (intTemp >= 26) {
                weaherNum = "2";
            } else if (15 <= intTemp && intTemp < 26) {
                weaherNum = "1";
            } else if (intTemp < 15) {
                weaherNum = "3";
            }
        }

        return weaherNum;
    }

    public String getSeasonNum(){
            return seasonNum;
    }

    public Weather(String nx_data, String ny_data) {
        this.nx_data = nx_data;
        this.ny_data = ny_data;
    }

    public String weather()
    {
        Date d = new Date();
        SimpleDateFormat f1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat f2 = new SimpleDateFormat("HH");
        SimpleDateFormat f3 = new SimpleDateFormat("MM");
        String month = f3.format(d);
        String date = f1.format(d);
        String time = Integer.parseInt(f2.format(d))-2+"00";
        String now = Integer.parseInt(f2.format(d)) + "00";

        switch (month){
            case "03", "04", "05" :
                seasonNum = "1";
                break;
            case "06", "07", "08" :
                seasonNum = "2";
                break;
            case "09", "10", "11" :
                seasonNum = "3";
                break;
            case "12", "01", "02" :
                seasonNum = "4";
                break;
        }



        if(Integer.parseInt(now) < 0100) {

            int newTime  = Integer.parseInt(time) + 2400;
            time = String.valueOf(newTime);
            int newdate = Integer.parseInt(date) - 1;
            date = String.valueOf(newdate);

        }

        if (Integer.parseInt(time) < 1000) {
            time = "0" + time;
        }

        if (Integer.parseInt(now) < 1000) {
            now = "0" + now;
        }

        String sentence = "";



        try {
            String nx = this.nx_data;
            String ny = this.ny_data;
            String baseDate = date;
            String baseTime = time;
            String pageNo = "1";
            String numOfRows = "1000";
            String serviceKey = "xH2V6uA%2Bns89rijEsbFvVBxuwuZUug%2FGbeQAaSMgfF3tjzbhfOaIBgXnR4H9ETwEF%2BClQbsQODdMSroYgaGKpw%3D%3D";
            String urlStr =
                    "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst?" + "serviceKey=" + serviceKey + "&pageNo=" + pageNo + "&numOfRows=" + numOfRows + "&dataType=JSON" + "&base_date=" + baseDate + "&base_time=" + baseTime + "&nx=" + nx + "&ny=" + ny;
            URL url = new URL(urlStr);

            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType){ }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                    }
            };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());


            BufferedReader bf;
            String line = "";
            String result = "";

            bf = new BufferedReader(new InputStreamReader(url.openStream()));

            while ((line = bf.readLine()) != null) {
                result = result.concat(line);

            }

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(result);

            JSONObject parse_response = (JSONObject) obj.get("response");
            JSONObject parse_body = (JSONObject) parse_response.get("body");
            JSONObject parse_items = (JSONObject) parse_body.get("items");

            JSONArray parse_item = (JSONArray) parse_items.get("item");
            String category;
            JSONObject weather;
            String fcstValue;
            String fcstTime;
            for (int i = 0; i < parse_item.size(); i++) {
                weather = (JSONObject) parse_item.get(i);
                fcstValue = (String) weather.get("fcstValue");
                // String nX = (String)weather.get("nx");
                // String nY = (String)weather.get("ny");
                category = (String) weather.get("category");
                fcstTime = (String) weather.get("fcstTime");


                if(fcstTime.equals(now)) {
                    if(category.equals("PTY")) {
                        if (!fcstValue.equals("0")) {
                            this.weatherConditions = "비";
                            weaherNum = "4";
                        }
                    }
                    else if(category.equals("SKY")) {
                        if(fcstValue.equals("1")) {
                            this.weatherConditions = "맑음";
                        } else {
                            this.weatherConditions = "구름많음";
                        }
                    }
                    if(category.equals("T1H")) {
                        this.temperature = fcstValue;
                    }
                }

            }

            bf.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return sentence;

    }

}