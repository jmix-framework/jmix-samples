package com.company.storedprocedures;

import com.company.storedprocedures.app.CarService;
import com.company.storedprocedures.entity.Car;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;

@SpringBootTest
public class CarServiceTest {

    @Autowired
    private CarService carService;

    private static final Logger log = LoggerFactory.getLogger(CarServiceTest.class);

    @Test
    public void loadCarByYear() {
        List<Car> cars = carService.loadCarsByYear(2021);
        cars.forEach(car ->
                log.info("Car: vin={}, year={}", car.getVin(), car.getYear()));
        Assert.isTrue(cars.size() > 0, "Cars collection should not be empty");
    }
}
