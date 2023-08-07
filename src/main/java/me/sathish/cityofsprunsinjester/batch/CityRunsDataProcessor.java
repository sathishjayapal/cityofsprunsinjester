package me.sathish.cityofsprunsinjester.batch;

import me.sathish.cityofsprunsinjester.data.RunsData;
import me.sathish.cityofsprunsinjester.data.RunsDataInput;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CityRunsDataProcessor implements ItemProcessor<RunsDataInput, RunsData> {
    @Override
    public RunsData process(RunsDataInput item) throws Exception {
        RunsData runsData = new RunsData();
        runsData.setRun_Date(LocalDate.parse(item.getDate(), DateTimeFormatter.ISO_LOCAL_DATE));
        runsData.setRun_Time(LocalDateTime.parse(item.getTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        runsData.setRun_Calories(item.getCalories());
        runsData.setRun_Distance(Long.parseLong(item.getDistance()));
        runsData.setActivity_Type(item.getActivity_Type());
        runsData.setRun_Calories(item.getCalories());
        return runsData;
    }
}
