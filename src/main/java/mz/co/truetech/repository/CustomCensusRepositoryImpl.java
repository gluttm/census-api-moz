package mz.co.truetech.repository;

import mz.co.truetech.dto.CensusDTO;
import mz.co.truetech.entity.Census;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class CustomCensusRepositoryImpl implements  CustomCensusRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Census> filterCensus(CensusDTO censusDTO) {
        String jpql = "SELECT c from Census c WHERE c.year = :year";
        TypedQuery<Census> query = entityManager.createQuery(jpql, Census.class)
                .setParameter("year", censusDTO.getYear());
        return query.getResultList();
    }
}
