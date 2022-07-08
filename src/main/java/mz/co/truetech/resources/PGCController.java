package mz.co.truetech.resources;

import mz.co.truetech.dto.PGCDTO;
import mz.co.truetech.entity.PGC;
import mz.co.truetech.exceptions.ApiRequestException;
import mz.co.truetech.service.PGCService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/pgc")
@ApiIgnore
public class PGCController {
    private final PGCService pgcService;

    public PGCController(PGCService pgcService) {
        this.pgcService = pgcService;
    }

    @GetMapping
    public ResponseEntity<List<PGC>> findAll() {
        List<PGC> dto = pgcService.findAll();
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<PGCDTO> save(@RequestBody PGCDTO pgcdto) {
        PGCDTO savedDTo = pgcService.save(pgcdto);
        return new ResponseEntity<>(savedDTo, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PGCDTO> findById(@PathVariable Long id) throws ApiRequestException {
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
}
