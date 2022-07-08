package mz.co.truetech.service;

import lombok.RequiredArgsConstructor;
import mz.co.truetech.dto.ProvinceDTO;
import mz.co.truetech.entity.Province;
import mz.co.truetech.repository.ProvinceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service @Transactional
public class ProvinceService {

    private final ProvinceRepository repository;

    public ProvinceDTO save(ProvinceDTO provinceDTO) {
        Province province = repository.save(new Province(provinceDTO));

        return new ProvinceDTO(province);
    }
}
