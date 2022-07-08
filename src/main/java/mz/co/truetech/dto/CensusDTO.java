package mz.co.truetech.dto;

import lombok.Data;
import mz.co.truetech.enums.Gender;
import mz.co.truetech.enums.Zone;

@Data
public class CensusDTO {

    private Long id;
    private Integer year;
    private Long amount;
    private Gender gender;
    private Zone zone;
    private Integer age;


}
