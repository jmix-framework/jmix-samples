package com.company.library.screen.book;

import io.jmix.ui.screen.*;
import com.company.library.entity.Book;

@UiController("jmxrpr_Book.browse")
@UiDescriptor("book-browse.xml")
@LookupComponent("booksTable")
public class BookBrowse extends StandardLookup<Book> {
}