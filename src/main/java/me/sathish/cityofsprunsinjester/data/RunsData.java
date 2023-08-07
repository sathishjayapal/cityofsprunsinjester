package me.sathish.cityofsprunsinjester.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document("runsdata")
@RequiredArgsConstructor
@Data
public class RunsData {
    @Id
    private String id;
    private String activity_Type;
    private LocalDate run_Date;
    private long run_Distance;
    private String run_Calories;
    private LocalDateTime run_Time;
    private long total_Reps;
}
