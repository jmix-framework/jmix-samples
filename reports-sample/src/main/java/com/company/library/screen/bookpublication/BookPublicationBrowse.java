package com.company.library.screen.bookpublication;

import io.jmix.ui.screen.*;
import com.company.library.entity.BookPublication;

@UiController("jmxrpr_BookPublication.browse")
@UiDescriptor("book-publication-browse.xml")
@LookupComponent("bookPublicationsTable")
public class BookPublicationBrowse extends StandardLookup<BookPublication> {
}