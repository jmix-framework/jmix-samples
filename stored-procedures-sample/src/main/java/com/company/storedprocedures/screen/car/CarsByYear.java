package com.company.storedprocedures.screen.car;

import com.company.storedprocedures.app.CarService;
import io.jmix.core.LoadContext;
import io.jmix.ui.Notifications;
import io.jmix.ui.component.HasValue;
import io.jmix.ui.component.TextField;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import com.company.storedprocedures.entity.Car;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

@UiController("CarsByYear")
@UiDescriptor("cars-by-year.xml")
@LookupComponent("carsTable")
public class CarsByYear extends StandardLookup<Car> {

    @Autowired
    private TextField<Integer> yearFilterField;
    @Autowired
    private Notifications notifications;
    @Autowired
    private CollectionLoader<Car> carsDl;
    @Autowired
    private CarService carService;

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        yearFilterField.setValue(2021);
    }

    @Subscribe("yearFilterField")
    public void onYearFilterFieldValueChange(HasValue.ValueChangeEvent<Integer> event) {
        if (event.getValue() == null || event.getValue() == 0) {
            notifications.create().withCaption("Enter a year").show();
        } else {
            carsDl.load();
        }
    }

    @Install(to = "carsDl", target = Target.DATA_LOADER)
    private List<Car> carsDlLoadDelegate(LoadContext<Car> loadContext) {
        Integer year = yearFilterField.getValue();
        if (year == null) {
            return Collections.emptyList();
        } else {
            return carService.loadCarsByYear(year);
        }
    }
}