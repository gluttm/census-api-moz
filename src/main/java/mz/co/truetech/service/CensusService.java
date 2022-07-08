package mz.co.truetech.service;

import lombok.RequiredArgsConstructor;
import mz.co.truetech.dto.CensusDTO;
import mz.co.truetech.dto.DistrictDTO;
import mz.co.truetech.entity.Census;
import mz.co.truetech.entity.District;
import mz.co.truetech.enums.Gender;
import mz.co.truetech.enums.Zone;
import mz.co.truetech.exceptions.ApiRequestException;
import mz.co.truetech.repository.CensusRepository;
import mz.co.truetech.repository.DistrictRepository;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CensusService {

    private final DistrictRepository districtRepository;
    private  final CensusRepository repository;
    private final Environment environment;

    public Page<Census> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Census> findAllBy(String district, Integer year, Gender gender, Zone zone, Integer age, Pageable pageable) throws ApiRequestException {
        Optional<District> dist = districtRepository.findDistinctBySlug(district);
        if (dist.isEmpty()) {
            throw new ApiRequestException(environment.getProperty("notfound.district"));
        }

        return repository.findAllBy(dist.get(), year, gender, zone, age, pageable);
    }

//    public List<District> findAllByName(String name) {
//        return repository.findByNameContaining(name);
//    }

    public void saveMass(Map<Integer, List<String>> data,
                              Long districtId, Integer year,
                              Zone zone, Gender gender) throws ApiRequestException {
        Optional<District> district = districtRepository.findById(districtId);

        if (district.isEmpty()) {
            throw new ApiRequestException(environment.getProperty("notfound.district"));
        }
        long women = 0;
        long men = 0;
        int age = 0;
        for (int i = 1 ; i < data.size() ; i++) {
            //int in = 0;

            try { age = (int) Double.parseDouble(data.get(i).get(0)); } catch (NumberFormatException e) { age = 0;}
            try { men = (long) Double.parseDouble(data.get(i).get(1)); } catch (NumberFormatException e) { men = 0L;}
            try { women = (long) Double.parseDouble(data.get(i).get(2)); } catch (NumberFormatException e) { women = 0L;}

            repository.save(new Census(null, year, men, Gender.MALE, zone, age, district.get()));
            repository.save(new Census(null, year, women, Gender.FAMALE, zone, age, district.get()));
        }

        //return new DistrictDTO();
    }

//    public DistrictDTO find(Long id) throws ApiRequestException {
//        if (id == null) {
//            throw new ApiRequestException(environment.getProperty("notfound.role"));
//        }
//        Optional<District> role = repository.findById(id);
//        return new DistrictDTO(role.orElse(new District()));
//    }
}
