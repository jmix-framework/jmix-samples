package com.company.library.screen.librarydepartment;

import io.jmix.ui.screen.*;
import com.company.library.entity.LibraryDepartment;

@UiController("jmxrpr_LibraryDepartment.edit")
@UiDescriptor("library-department-edit.xml")
@EditedEntityContainer("libraryDepartmentDc")
public class LibraryDepartmentEdit extends StandardEditor<LibraryDepartment> {
}