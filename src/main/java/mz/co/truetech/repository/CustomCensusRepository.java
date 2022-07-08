package mz.co.truetech.repository;

import mz.co.truetech.dto.CensusDTO;
import mz.co.truetech.entity.Census;
import mz.co.truetech.enums.Gender;
import mz.co.truetech.enums.Zone;

import java.util.List;

public interface CustomCensusRepository {
    List<Census> filterCensus(CensusDTO censusDTO);
}
