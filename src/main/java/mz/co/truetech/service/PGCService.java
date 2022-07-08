package mz.co.truetech.service;

import mz.co.truetech.dto.PGCDTO;
import mz.co.truetech.entity.PGC;
import mz.co.truetech.exceptions.ApiRequestException;
import mz.co.truetech.repository.PGCRepository;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class PGCService {

    private final PGCRepository repository;
    private final Environment environment;

    public PGCService(PGCRepository repository, Environment environment) {
        this.repository = repository;
        this.environment = environment;
    }

    public PGCDTO save(PGCDTO pgcdto) {

        return null;
    }

    public PGCDTO findById(Long id) throws ApiRequestException {

        return null;
    }

    public List<PGC> findAll() {
        return repository.findAll();
    }

}
