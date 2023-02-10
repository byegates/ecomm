package com.wly.ecomm.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wly.ecomm.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class UserControllerITest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    private static final String id0 = "c4e61f12-e520-451f-83c1-e74a15ecc4a8";
    private static final String id1 = "ba0500f0-17b2-4885-918e-03781b6233d2";
    private static final String id2 = "ac21fcc8-61c7-47e1-87f9-83afde40c7af";
    private static final String id3 = "c861e35f-e81a-4dec-be7e-dfd9c852a630";
    private static final String id4 = "b3f4e44a-a7dd-45ef-9243-48873e3ca28a";

    @Test @DisplayName("List all users")
    void listAllUsers() throws Exception {

        // given pre-condition or set
        // NA

        // when - action or behavior that we are going to test
        var response = mockMvc.perform(get("/users"));

        // then - verify the result or output using assert statement
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(7))) // init users from data.sql
                ;

    }

    @Test @DisplayName("Create a new user")
    @Transactional
    void createNewUser() throws Exception {

        // given pre-condition or set
        var user = getUser(Thread.currentThread().getStackTrace()[1].getMethodName());

        // when - action or behavior that we are going to test
        var response = mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        // then - verify the result or output using assert statement
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user.getLastName())))
        ;

    }

    @Test @DisplayName("Find a user with all UserDTO info returned")
    void findUserDtoById() throws Exception {

        // given pre-condition or set
        // this user id and all it's related info are created in data.sql

        // when - action or behavior that we are going to test
        var response = mockMvc.perform(get("/users/{id}", id3));

        // then - verify the result or output using assert statement
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Email", is("customer3@customer-domain.com")))
                .andExpect(jsonPath("$.['First Name']", is("Customer3")))
                .andExpect(jsonPath("$.['Last Name']", is("Customer")))
                .andExpect(jsonPath("$.['Cart Summary'].['Cart Items'].size()", is(3)))
                .andExpect(jsonPath("$.['Cart Summary'].['Cart Items'][0].['Product Name']", is("iPhone 14 256GB")))
                .andExpect(jsonPath("$.['Cart Summary'].['Cart Items'][0].['Deal Applied']", is("BOGO100")))
                .andExpect(jsonPath("$.['Cart Summary'].['Cart Items'][0].Price", is(899.0)))
                .andExpect(jsonPath("$.['Cart Summary'].['Cart Items'][0].['Price After Discount']", is(539.4)))
                .andExpect(jsonPath("$.['Cart Summary'].['Cart Items'][0].Quantity", is(5)))
                .andExpect(jsonPath("$.['Cart Summary'].['Cart Items'][0].Subtotal", is(2697.0)))
                .andExpect(jsonPath("$.['Cart Summary'].['Cart Items'][1].['Product Name']", is("iPhone 14 Plus 256GB")))
                .andExpect(jsonPath("$.['Cart Summary'].['Cart Items'][1].['Deal Applied']", is("OFF20")))
                .andExpect(jsonPath("$.['Cart Summary'].['Cart Items'][1].Price", is(999.0)))
                .andExpect(jsonPath("$.['Cart Summary'].['Cart Items'][1].['Price After Discount']", is(799.2)))
                .andExpect(jsonPath("$.['Cart Summary'].['Cart Items'][1].Quantity", is(3)))
                .andExpect(jsonPath("$.['Cart Summary'].['Cart Items'][1].Subtotal", is(2397.6)))
                .andExpect(jsonPath("$.['Cart Summary'].['Cart Items'][2].['Product Name']", is("iPhone 14 Pro 256GB")))
                .andExpect(jsonPath("$.['Cart Summary'].['Cart Items'][2].['Deal Applied']", is("NONE")))
                .andExpect(jsonPath("$.['Cart Summary'].['Cart Items'][2].Price", is(1099.0)))
                .andExpect(jsonPath("$.['Cart Summary'].['Cart Items'][2].['Price After Discount']", is(1099.0)))
                .andExpect(jsonPath("$.['Cart Summary'].['Cart Items'][2].Quantity", is(2)))
                .andExpect(jsonPath("$.['Cart Summary'].['Cart Items'][2].Subtotal", is(2198.0)))
                .andExpect(jsonPath("$.['Cart Summary'].['Total Due']", is(7292.6)))
        ;

    }

    @Test @DisplayName("Delete a user by ID")
    @Transactional
    void deleteUserById() throws Exception {

        // given pre-condition or set

        // when - action or behavior that we are going to test
        var response = mockMvc.perform(delete("/users/{id}", id3));

        // then - verify the result or output using assert statement
        response.andDo(print())
                .andExpect(status().isOk())
                ;

        mockMvc.perform(get("/users/{id}", id3))
                .andDo(print())
                .andExpect(status().isNotFound())
                ;
    }


    @Test @DisplayName("Find a user's cart by Id")
    void findCartById() throws Exception {

        // given pre-condition or set

        // when - action or behavior that we are going to test
        var response = mockMvc.perform(get("/users/{id}/cart", id2));

        // then - verify the result or output using assert statement
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$.[0].product.id", is(2000000005)))
                .andExpect(jsonPath("$.[0].product.name", is("iPhone 14 256GB")))
                .andExpect(jsonPath("$.[0].product.price", is(899.0)))
                .andExpect(jsonPath("$.[0].quantity", is(4)))
                .andExpect(jsonPath("$.[0].product.deals.size()", is(1)))
                .andExpect(jsonPath("$.[0].product.deals[0].id", is(2000000002)))
                .andExpect(jsonPath("$.[0].product.deals[0].name", is("BOGO100")))
                .andExpect(jsonPath("$.[0].product.deals[0].description", is("BUY ONE GET ONE FREE")))
                .andExpect(jsonPath("$.[1].product.id", is(2000000006)))
                .andExpect(jsonPath("$.[1].product.name", is("iPhone 14 Plus 256GB")))
                .andExpect(jsonPath("$.[1].product.price", is(999.0)))
                .andExpect(jsonPath("$.[1].quantity", is(1)))
                .andExpect(jsonPath("$.[1].product.deals.size()", is(1)))
                .andExpect(jsonPath("$.[1].product.deals[0].id", is(2000000004)))
                .andExpect(jsonPath("$.[1].product.deals[0].name", is("OFF20")))
                .andExpect(jsonPath("$.[1].product.deals[0].description", is("20% OFF")))
                ;

    }

    @Test @DisplayName("Add product to user's cart")
    @Transactional
    void addProductToCart() throws Exception {

        // given pre-condition or set
        var productId5 = 2000000005;
        var quantity = 3;

        // when - action or behavior that we are going to test
        var response = mockMvc.perform(post("/users/{userId}/cart/add/{productId}/{quantity}", id0, productId5, quantity));

        // then - verify the result or output using assert statement
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product.id", is(productId5)))
                .andExpect(jsonPath("$.product.name", is("iPhone 14 256GB")))
                .andExpect(jsonPath("$.product.price", is(899.0)))
                .andExpect(jsonPath("$.quantity", is(quantity)))
                .andExpect(jsonPath("$.product.deals.size()", is(1)))
                .andExpect(jsonPath("$.product.deals[0].id", is(2000000002)))
                .andExpect(jsonPath("$.product.deals[0].name", is("BOGO100")))
                .andExpect(jsonPath("$.product.deals[0].description", is("BUY ONE GET ONE FREE")))
        ;

    }

    @Test @DisplayName("Update quantity of existing product in cart")
    @Transactional
    void updateQuantity() throws Exception {

        // given pre-condition or set
        var quantity = 5;
        var productId = 2000000002;

        // when - action or behavior that we are going to test
        var response = mockMvc.perform(patch("/users/{userId}/cart/update/{productId}/{quantity}", id1, productId, quantity));

        // then - verify the result or output using assert statement

        // update success
        response.andDo(print())
                .andExpect(status().isOk())
        ;

        // query again for additional validations
        mockMvc.perform(get("/users/{userId}/cart", id1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$.[0].product.id", is(productId)))
                .andExpect(jsonPath("$.[0].product.name", is("iPhone 14 Plus 128GB")))
                .andExpect(jsonPath("$.[0].product.price", is(899.0)))
                .andExpect(jsonPath("$.[0].quantity", is(quantity)))
                ;

    }

    @Test @DisplayName("Remove a product from cart")
    @Transactional
    void removeProduct() throws Exception {

        // given pre-condition or set
        var productId = 2000000007;
        var productIdLeft = 2000000008;

        // when - action or behavior that we are going to test
        var response = mockMvc.perform(delete("/users/{userId}/cart/remove/{productId}", id4, productId));

        // then - verify the result or output using assert statement

        // remove action ok
        response.andDo(print())
                .andExpect(status().isOk())
        ;

        // query for additional validations
        mockMvc.perform(get("/users/{userId}/cart", id4))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$.[0].product.id", is(productIdLeft)))
        ;

    }

    @Test @DisplayName("View Receipt of for a user's all cart items")
    @Transactional
    void viewReceipt() throws Exception {

        // given pre-condition or set
        var quantity = 5;
        var productId = 2000000002;

        // when - action or behavior that we are going to test
        var response = mockMvc.perform(get("/users/{id}/receipt", id0));

        // then - verify the result or output using assert statement
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Total Due']", is(1847.85)))
                .andExpect(jsonPath("$.['Cart Items'][0].['Product Name']", is("iPhone 14 128GB")))
                .andExpect(jsonPath("$.['Cart Items'][0].['Deal Applied']", is("BOGO50")))
                .andExpect(jsonPath("$.['Cart Items'][0].['Price']", is(799.0)))
                .andExpect(jsonPath("$.['Cart Items'][0].['Price After Discount']", is(599.25)))
                .andExpect(jsonPath("$.['Cart Items'][0].['Quantity']", is(2)))
                .andExpect(jsonPath("$.['Cart Items'][0].['Subtotal']", is(1198.5)))

                .andExpect(jsonPath("$.['Cart Items'][1].['Product Name']", is("iPhone 14 Pro 128GB")))
                .andExpect(jsonPath("$.['Cart Items'][1].['Deal Applied']", is("OFF35")))
                .andExpect(jsonPath("$.['Cart Items'][1].['Price']", is(999.0)))
                .andExpect(jsonPath("$.['Cart Items'][1].['Price After Discount']", is(649.35)))
                .andExpect(jsonPath("$.['Cart Items'][1].['Quantity']", is(1)))
                .andExpect(jsonPath("$.['Cart Items'][1].['Subtotal']", is(649.35)))
        ;

    }

    User getUser(String name) {
        String email = String.format("%s@test-email.org", name).toLowerCase();
        return new User(email, name, "Last");
    }

}
