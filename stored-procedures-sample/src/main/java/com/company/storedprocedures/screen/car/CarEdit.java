package com.company.storedprocedures.screen.car;

import io.jmix.ui.screen.*;
import com.company.storedprocedures.entity.Car;

@UiController("Car.edit")
@UiDescriptor("car-edit.xml")
@EditedEntityContainer("carDc")
public class CarEdit extends StandardEditor<Car> {
}