package com.egr.farming.Service;

import com.egr.farming.model.Weather;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface FarmingViewPageService {

    public String getIndex(Model model, String latitude, String longitude);
    public String helloWorld(Model model, String name);
    //private Weather getLocationObject(String uri);
}
