package mz.co.truetech.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import mz.co.truetech.enums.Gender;
import mz.co.truetech.enums.Zone;

import javax.persistence.*;

@Entity @Table(name="censuses")
@NoArgsConstructor @AllArgsConstructor
public class Census {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;
    @Getter @Setter
    private Integer year;
    @Getter @Setter
    private Long amount;
    @Getter @Setter
    private Gender gender;
    @Getter @Setter
    private Zone zone;
    @Getter @Setter
    private Integer age;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    @JsonBackReference
    public District getDistrict() {
        return this.district;
    }
}
