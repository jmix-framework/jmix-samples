package com.company.library.screen.bookpublication;

import io.jmix.ui.screen.*;
import com.company.library.entity.BookPublication;

@UiController("jmxrpr_BookPublication.edit")
@UiDescriptor("book-publication-edit.xml")
@EditedEntityContainer("bookPublicationDc")
public class BookPublicationEdit extends StandardEditor<BookPublication> {
}