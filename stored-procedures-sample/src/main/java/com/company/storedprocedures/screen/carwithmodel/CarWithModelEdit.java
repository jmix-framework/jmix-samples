package com.company.storedprocedures.screen.carwithmodel;

import com.company.storedprocedures.app.CarWithModelService;
import io.jmix.core.SaveContext;
import io.jmix.ui.screen.*;
import com.company.storedprocedures.entity.CarWithModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Set;

@UiController("CarWithModel.edit")
@UiDescriptor("car-with-model-edit.xml")
@EditedEntityContainer("carWithModelDc")
public class CarWithModelEdit extends StandardEditor<CarWithModel> {

    @Autowired
    private CarWithModelService carWithModelService;

    @Install(target = Target.DATA_CONTEXT)
    private Set<Object> commitDelegate(SaveContext saveContext) {
        CarWithModel editedEntity = getEditedEntity();
        carWithModelService.save(editedEntity);
        return Collections.singleton(editedEntity);
    }
}