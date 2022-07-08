package mz.co.truetech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import mz.co.truetech.entity.Census;
import mz.co.truetech.entity.District;
import mz.co.truetech.entity.Province;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class DistrictDTO {

    private Long id;
    private String name;
    private String slug;
    private Province province;

    private final Set<Census> census = new HashSet<>();

    public DistrictDTO(Long id, String name, String slug, Province province) {
        this.id = id;
        this.name = name;
        this.province = province;
        this.slug = slug;
    }

    public DistrictDTO(District district) {
        this.id = district.getId();
        this.name = district.getName();
        this.slug = district.getSlug();
        district.getCensuses().stream().map(x -> this.census.add(x)).collect(Collectors.toSet());
    }
}
