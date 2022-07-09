package mz.co.truetech.service;

import lombok.RequiredArgsConstructor;
import mz.co.truetech.dto.DistrictDTO;
import mz.co.truetech.dto.RoleRequest;
import mz.co.truetech.dto.projection.PDistrictDTO;
import mz.co.truetech.entity.District;
import mz.co.truetech.entity.Role;
import mz.co.truetech.exceptions.ApiRequestException;
import mz.co.truetech.repository.DistrictRepository;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DistrictService {

    private final DistrictRepository repository;
    private final Environment environment;

    public List<District> findAll() {
        return repository.findAll();
    }

    public List<PDistrictDTO> findAllByName(String name) {
        return repository.findByNameContaining(name);
    }

    public DistrictDTO save(DistrictDTO districtDTO) {
        District district = repository.save(new District(districtDTO));

       // districtDTO.getCensus().stream().map(x -> district.getCensuses().add(x)).collect(Collectors.toSet());

        return new DistrictDTO(district);
    }

    public DistrictDTO find(Long id) throws ApiRequestException {
        if (id == null) {
            throw new ApiRequestException(environment.getProperty("notfound.role"));
        }
        Optional<District> role = repository.findById(id);
        return new DistrictDTO(role.orElse(new District()));
    }
}
