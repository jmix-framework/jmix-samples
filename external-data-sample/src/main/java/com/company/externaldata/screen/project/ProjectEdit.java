package com.company.externaldata.screen.project;

import com.company.externaldata.entity.Project;
import io.jmix.ui.screen.*;

@UiController("Project.edit")
@UiDescriptor("project-edit.xml")
@EditedEntityContainer("projectDc")
public class ProjectEdit extends StandardEditor<Project> {
}