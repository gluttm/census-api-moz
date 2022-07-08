package mz.co.truetech.repository;

import mz.co.truetech.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DistrictRepository extends JpaRepository<District, Long> {

    @Query("SELECT d FROM District d WHERE upper(d.name) LIKE %:name%")
    List<District> findByNameContaining(@Param("name") String name);

    Optional<District> findDistinctBySlug(String slug);
}
