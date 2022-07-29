package com.company.storedprocedures.screen.model;

import io.jmix.ui.screen.*;
import com.company.storedprocedures.entity.Model;

@UiController("Model.edit")
@UiDescriptor("model-edit.xml")
@EditedEntityContainer("modelDc")
public class ModelEdit extends StandardEditor<Model> {
}