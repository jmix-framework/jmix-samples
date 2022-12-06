package com.company.library.screen.literaturetype;

import io.jmix.ui.screen.*;
import com.company.library.entity.LiteratureType;

@UiController("jmxrpr_LiteratureType.edit")
@UiDescriptor("literature-type-edit.xml")
@EditedEntityContainer("literatureTypeDc")
public class LiteratureTypeEdit extends StandardEditor<LiteratureType> {
}