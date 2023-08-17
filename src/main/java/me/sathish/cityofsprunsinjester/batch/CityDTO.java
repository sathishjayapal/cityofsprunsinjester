package me.sathish.cityofsprunsinjester.batch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDTO {

    private Date date = new Date();
    private String id;
    private String text;
}
