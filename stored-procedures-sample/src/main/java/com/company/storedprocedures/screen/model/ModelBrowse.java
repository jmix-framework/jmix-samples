package com.company.storedprocedures.screen.model;

import io.jmix.ui.screen.*;
import com.company.storedprocedures.entity.Model;

@UiController("Model.browse")
@UiDescriptor("model-browse.xml")
@LookupComponent("modelsTable")
public class ModelBrowse extends StandardLookup<Model> {
}