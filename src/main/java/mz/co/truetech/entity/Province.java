package mz.co.truetech.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mz.co.truetech.dto.ProvinceDTO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "provinces")
@NoArgsConstructor
public class Province {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;
    @Column(unique = true) @Getter @Setter
    private String name;
    @Getter @Setter @Column(unique = true)
    private String slug;

    @OneToMany(mappedBy = "province", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private final Set<District> districts = new HashSet<>();

    public Province (Long id, String name, String slug) {
        this.id = id;
        this.name = name;
        this.slug = slug;
    }

    public Province (ProvinceDTO provinceDTO) {
        this.id = provinceDTO.getId();
        this.name = provinceDTO.getName();
//        provinceDTO
//                .getDistricts()
//                .stream()
//                .map(x -> this.districts.add(x)).collect(Collectors.toSet());
    }
    @JsonManagedReference
    public Set<District> getDistricts() {
        return districts;
    }
}
