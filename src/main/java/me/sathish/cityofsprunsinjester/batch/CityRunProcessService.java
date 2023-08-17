package me.sathish.cityofsprunsinjester.batch;

import ch.qos.logback.core.testUtil.RandomUtil;
import lombok.RequiredArgsConstructor;
import me.sathish.cityofsprunsinjester.data.RunsData;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CityRunProcessService {

    public String restCallToService(RunsData runsData) {
        RestTemplate restTemplate = new RestTemplate();
        CityDTO cityDTO = new CityDTO();
        cityDTO.setId(String.valueOf(RandomUtil.getPositiveInt()));
        cityDTO.setText(runsData.getRun_Calories());
        return restTemplate.postForObject("http://localhost:8080/cityofspruns", cityDTO, String.class);
    }
}
