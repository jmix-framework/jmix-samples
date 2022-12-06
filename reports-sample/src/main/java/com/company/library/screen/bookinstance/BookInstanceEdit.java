package com.company.library.screen.bookinstance;

import io.jmix.ui.screen.*;
import com.company.library.entity.BookInstance;

@UiController("jmxrpr_BookInstance.edit")
@UiDescriptor("book-instance-edit.xml")
@EditedEntityContainer("bookInstanceDc")
public class BookInstanceEdit extends StandardEditor<BookInstance> {
}