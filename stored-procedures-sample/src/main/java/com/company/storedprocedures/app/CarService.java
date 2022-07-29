package com.company.storedprocedures.app;

import com.company.storedprocedures.entity.Car;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

/**
 * Demonstrates how to call a database function and map the result to a JPA entity using EntityManager.
 */
@Component
public class CarService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<Car> loadCarsByYear(int year) {
        StoredProcedureQuery storedProcedureQuery =
                entityManager.createStoredProcedureQuery("get_cars_by_year", Car.class)
                        .registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN)
                        .setParameter(1, year);

        List<Car> cars = storedProcedureQuery.getResultList();
        return cars;
    }
}