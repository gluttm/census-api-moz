package mz.co.truetech.repository;

import mz.co.truetech.entity.PGC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PGCRepository extends JpaRepository<PGC, Long> {
}
