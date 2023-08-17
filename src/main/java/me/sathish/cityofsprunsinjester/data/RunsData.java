package me.sathish.cityofsprunsinjester.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Data
public class RunsData {
    @Id
    private String id;
    private String activity_Type;
    private LocalDate run_Date;
    private String run_Distance;
    private String run_Calories;
    private String run_Time;
    private String total_Reps;
}
