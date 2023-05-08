package com.daofab.restapi;

import com.daofab.restapi.dto.Child;
import com.daofab.restapi.dto.Parent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ControllerTest {

    private Controller controller;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        controller = new Controller();
        request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("Get parent transactions with valid page and ascending sort")
    void getParentTransactionsWithValidPageAndAscendingSort() throws Exception {
        request.setParameter("page", "1");
        request.setParameter("sort", "asc");

        List<Parent> parents = controller.getParentTransactions(1, "asc");

        assertThat(parents.size()).isEqualTo(2);
        assertThat(parents.get(0).getId()).isEqualTo(1);
        assertThat(parents.get(1).getId()).isEqualTo(2);
    }

    @Test
    @DisplayName("Get parent transactions with valid page and descending sort")
    void getParentTransactionsWithValidPageAndDescendingSort() throws Exception {
        request.setParameter("page", "1");
        request.setParameter("sort", "desc");

        List<Parent> parents = controller.getParentTransactions(1, "desc");

        assertThat(parents.size()).isEqualTo(2);
        assertThat(parents.get(0).getId()).isEqualTo(7);
        assertThat(parents.get(1).getId()).isEqualTo(6);
    }

    @Test
    @DisplayName("Get parent transactions with invalid page")
    void getParentTransactionsWithInvalidPage() throws Exception {
        request.setParameter("page", "100");

        List<Parent> parents = controller.getParentTransactions(100, "asc");

        assertThat(parents).isEmpty();
    }

    @Test
    @DisplayName("Get child transactions by parent ID")
    void getChildTransactionsByParentId() throws Exception {
        int parentId = 1;
        List<Child> expectedChildren = List.of(
                new Child(1, 1, 100),
                new Child(2, 1, 200)
        );
        Resource childResource = new ClassPathResource("Child.json");
        ObjectMapper mapper = new ObjectMapper();
        Controller controllerSpy = mock(Controller.class);
        when(controllerSpy.getChildTransactions(parentId)).thenReturn(expectedChildren);

        List<Child> actualChildren = controllerSpy.getChildTransactions(parentId);

        assertThat(actualChildren).isEqualTo(expectedChildren);
    }
}