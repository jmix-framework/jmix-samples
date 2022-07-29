package com.company.storedprocedures.screen.carwithmodel;

import com.company.storedprocedures.app.CarWithModelService;
import com.company.storedprocedures.entity.Car;
import io.jmix.core.LoadContext;
import io.jmix.ui.Notifications;
import io.jmix.ui.component.HasValue;
import io.jmix.ui.component.TextField;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import com.company.storedprocedures.entity.CarWithModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

@UiController("CarWithModel.browse")
@UiDescriptor("car-with-model-browse.xml")
@LookupComponent("carWithModelsTable")
public class CarWithModelBrowse extends StandardLookup<CarWithModel> {

    @Autowired
    private TextField<Integer> yearFilterField;
    @Autowired
    private Notifications notifications;
    @Autowired
    private CollectionLoader<CarWithModel> carWithModelsDl;
    @Autowired
    private CarWithModelService carWithModelService;

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        yearFilterField.setValue(2021);
    }

    @Subscribe("yearFilterField")
    public void onYearFilterFieldValueChange(HasValue.ValueChangeEvent<Integer> event) {
        if (event.getValue() == null || event.getValue() == 0) {
            notifications.create().withCaption("Enter a year").show();
        } else {
            carWithModelsDl.load();
        }
    }

    @Install(to = "carWithModelsDl", target = Target.DATA_LOADER)
    private List<CarWithModel> carWithModelsDlLoadDelegate(LoadContext<CarWithModel> loadContext) {
        Integer year = yearFilterField.getValue();
        if (year == null) {
            return Collections.emptyList();
        } else {
            return carWithModelService.loadCarWithModelByYear(year);
        }
    }
}