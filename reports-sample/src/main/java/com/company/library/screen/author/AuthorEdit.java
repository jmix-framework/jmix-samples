package com.company.library.screen.author;

import io.jmix.ui.screen.*;
import com.company.library.entity.Author;

@UiController("jmxrpr_Author.edit")
@UiDescriptor("author-edit.xml")
@EditedEntityContainer("authorDc")
public class AuthorEdit extends StandardEditor<Author> {
}