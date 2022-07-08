package mz.co.truetech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import mz.co.truetech.entity.District;
import mz.co.truetech.entity.Province;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data @AllArgsConstructor
public class ProvinceDTO {
    private Long id;
    private String name;
    private String slug;

    public ProvinceDTO(Province province) {
        this.id = province.getId();
        this.name = province.getName();
        this.slug = province.getSlug();
//        province
//                .getDistricts()
//                .stream()
//                .map(x -> this.districts.add(x)).collect(Collectors.toSet());
    }
}
