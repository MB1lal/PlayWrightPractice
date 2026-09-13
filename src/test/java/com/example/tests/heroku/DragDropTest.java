package com.example.tests.herokuapp;

import com.example.support.BaseUiTest;
import com.example.ui.pages.herokuapp.DragDropPage;
import com.example.ui.pages.herokuapp.HerokuHomePage;
import com.example.utils.AssertionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Ports {@code drag_drop_tests.cy.js}: column A swaps with column B.
 */
@Tag("ui")
@DisplayName("Drag and drop")
class DragDropTest extends BaseUiTest {

    private DragDropPage dragDrop;

    @BeforeEach
    void navigateToPage() {
        HerokuHomePage home = new HerokuHomePage(browser.getPage(), context);
        home.open(config.getHerokuUrl());
        home.goTo("Drag and Drop");
        dragDrop = new DragDropPage(browser.getPage(), context);
    }

    @Test
    @DisplayName("Column A drops onto column B and they swap")
    void columnASwapsWithB() {
        dragDrop.dragAToB();

        assertThat(dragDrop.columnHeader("column-a")).isEqualTo("B");
        assertThat(dragDrop.columnHeader("column-b")).isEqualTo("A");
    }
}
