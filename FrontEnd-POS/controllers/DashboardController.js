/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 6:56 PM - 1/15/2024
 **/

let baseUrl = "http://localhost:8080/javaee_pos/";

/**
 * Customer Count
 **/
$.ajax({
    url: baseUrl + "customer?option=customerCount",
    method: "GET",
    contentType: "application/json",
    dataType: "json",
    success: function (resp) {
        $("#txtCustomerCount").text(resp.count);
    },
    error: function (error) {
        console.log("Customer Count Error : ", error);
    }
});

/**
 * Item Count
 **/
$.ajax({
    url: baseUrl + "item?option=itemCount",
    method: "GET",
    contentType: "application/json",
    dataType: "json",
    success: function (resp) {
        $("#txtItemsCount").text(resp.count);
    },
    error: function (error) {
        console.log("Item Count Error : ", error);
    }
});

/**
 * Orders Count
 **/
$.ajax({
    url: baseUrl + "orders?option=ordersCount",
    method: "GET",
    contentType: "application/json",
    dataType: "json",
    success: function (resp) {
        $("#txtOrderCount").text(resp.count);
    },
    error: function (error) {
        console.log("Orders Count Error : ", error);
    }
});