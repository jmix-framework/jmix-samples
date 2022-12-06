package com.company.library.screen.town;

import io.jmix.ui.screen.*;
import com.company.library.entity.Town;

@UiController("jmxrpr_Town.edit")
@UiDescriptor("town-edit.xml")
@EditedEntityContainer("townDc")
public class TownEdit extends StandardEditor<Town> {
}