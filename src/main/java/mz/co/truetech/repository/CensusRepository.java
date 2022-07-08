package mz.co.truetech.repository;

import mz.co.truetech.entity.Census;
import mz.co.truetech.entity.District;
import mz.co.truetech.enums.Gender;
import mz.co.truetech.enums.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CensusRepository extends JpaRepository<Census, Long> {

    @Query("SELECT c FROM Census c WHERE c.district = :district AND (:year is null or c.year = :year) " +
            "AND (:gender is null or c.gender = :gender) " +
            "AND (:zone is null or c.zone = :zone) " +
            "AND (:age is null or c.age = :age)")
    Page<Census> findAllBy(
            @Param("district") District district,
            @Param("year") Integer year,
            @Param("gender") Gender gender,
            @Param("zone") Zone zone,
            @Param("age") Integer age,
            Pageable pageable);

}
