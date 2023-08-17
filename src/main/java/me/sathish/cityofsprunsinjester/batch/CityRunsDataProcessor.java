package me.sathish.cityofsprunsinjester.batch;

import me.sathish.cityofsprunsinjester.data.RunsData;
import me.sathish.cityofsprunsinjester.data.RunsDataInput;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CityRunsDataProcessor implements ItemProcessor<RunsDataInput, RunsData> {
    static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Override
    public RunsData process(RunsDataInput item) throws Exception {
        RunsData runsData = new RunsData();
        runsData.setRun_Date(LocalDate.from(LocalDateTime.parse(item.getDate(),format)));
        runsData.setRun_Time(item.getTime());
        runsData.setRun_Calories(item.getCalories());
        runsData.setRun_Distance(item.getDistance());
        runsData.setActivity_Type(item.getActivity_Type());
        runsData.setRun_Calories(item.getCalories());
        return runsData;
    }
}
