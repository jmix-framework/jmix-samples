package com.company.storedprocedures.screen.car;

import io.jmix.ui.screen.*;
import com.company.storedprocedures.entity.Car;

@UiController("Car.browse")
@UiDescriptor("car-browse.xml")
@LookupComponent("carsTable")
public class CarBrowse extends StandardLookup<Car> {
}