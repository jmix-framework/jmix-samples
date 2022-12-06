package com.company.library.screen.literaturetype;

import io.jmix.ui.screen.*;
import com.company.library.entity.LiteratureType;

@UiController("jmxrpr_LiteratureType.browse")
@UiDescriptor("literature-type-browse.xml")
@LookupComponent("literatureTypesTable")
public class LiteratureTypeBrowse extends StandardLookup<LiteratureType> {
}