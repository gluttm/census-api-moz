package mz.co.truetech.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mz.co.truetech.dto.DistrictDTO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="districts") @NoArgsConstructor @AllArgsConstructor

public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;
    @Column(unique = true) @Getter @Setter
    private String name;
    @Getter @Setter @Column(unique = true)
    private String slug;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;

    @OneToMany(mappedBy = "district", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Census> censuses = new HashSet<>();

    public District(DistrictDTO districtDTO) {
        this.id = districtDTO.getId();
        this.name = districtDTO.getName();
        this.slug = districtDTO.getSlug();
        this.province = districtDTO.getProvince();
        //districtDTO.getCensus().stream().map(x -> this.censuses.add(x)).collect(Collectors.toSet());
    }

    @JsonManagedReference
    public Set<Census> getCensuses() {
        return this.censuses;
    }
    @JsonBackReference
    public Province getProvince() {
        return province;
    }
}
