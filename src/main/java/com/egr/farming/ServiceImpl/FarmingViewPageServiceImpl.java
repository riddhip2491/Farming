package com.egr.farming.ServiceImpl;

import com.egr.farming.Service.FarmingViewPageService;
import com.egr.farming.model.Weather;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import sun.rmi.runtime.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Logger;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Component

@Controller
public class FarmingViewPageServiceImpl implements FarmingViewPageService {

    private static final String TITLE= "Smart Farming";

    private Weather weather;
    @Value("${openweather.apikey}")
    private String weatherApiKey;

    @Value("${openweather.url}")
    private String weatherApi;

    public static final String CLASS_NAME ="com.eg.farming.ServiceImpl.FarmingViewPageServiceImpl";
    Logger logger = Logger.getLogger(CLASS_NAME);
    String weatherDesc;
    Double currentTemp;

    public FarmingViewPageServiceImpl(){
        weather = new Weather();

    }
    @Override
    @GetMapping("/HelloWorld")
    public String helloWorld(Model model, @RequestParam (value = "name", required = false, defaultValue = "World") String name) {
        model.addAttribute("name", name);
        return "HelloWorld";
    }

    @Override
    @GetMapping("/getLocation")
    public String getIndex(Model model, @RequestParam(value="lat") String latitude, @RequestParam(value = "lon") String longitude){
        logger.info("Entering into getIndex method");
        try {
            model.addAttribute("lat", latitude);
            model.addAttribute("lon", longitude);
            //Setting latitude and longitude for future use
            weather.setLatitude(latitude);
            weather.setLongitude(longitude);
            logger.info(weatherApi + "?lat=" + latitude + "&lon=" + longitude + "&APPID=" + weatherApiKey);

            //Calling Open Weather API
            final String uri = weatherApi + "?lat=" + latitude + "&units=imperial" + "&lon=" + longitude + "&APPID=" + weatherApiKey;
            //Calling Rest Template
            getLocationObject(uri,weather);
            //Setting model attributes to be viewed in html
            model.addAttribute("temp", weather.getCurrentTemperature().toString() + "°F");
            model.addAttribute("desc", weather.getDescription());

            logger.info("Current Temperature: " + weather.getCurrentTemperature() + " Weather Description: " + weather.getDescription());
        }catch (Exception e){
            e.printStackTrace();
        }
        logger.info("Returning from getIndex");
        return "getLocation";
    }

    private Weather getLocationObject(String uri, Weather weatherObj){
        try {
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(uri, String.class);
            Object object = new JSONParser().parse(result);
            JSONObject jsonObject = (JSONObject) object;
            //Get Current temperature from main object
            JSONObject main = (JSONObject) jsonObject.get("main");
            currentTemp = (Double) main.get("temp");
            //Get Weather Descriptpion
            JSONArray weather = (JSONArray) jsonObject.get("weather");
            Iterator iterator = weather.iterator();
            while (iterator.hasNext()) {
                Object nextObj = iterator.next();
                JSONObject jsonObject1 = (JSONObject) nextObj;
                weatherDesc = (String) jsonObject1.get("description");
                logger.info("Weather Description: " + weatherDesc);

            }
            weatherObj.setCurrentTemperature(currentTemp);
            weatherObj.setDescription(weatherDesc);
            logger.info("Result: \n" + result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return weather;
    }

}
