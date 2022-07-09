package mz.co.truetech.dto.projection;

import lombok.Data;
import mz.co.truetech.entity.District;

@Data
public class PDistrictDTO {
    private Long id;
    private String name;
    private String slug;

    public PDistrictDTO(District district) {
        this.id = district.getId();
        this.name = district.getName();
        this.slug = district.getSlug();
    }
}
