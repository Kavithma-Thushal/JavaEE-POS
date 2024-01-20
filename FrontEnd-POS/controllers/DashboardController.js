/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 6:56 PM - 1/15/2024
 **/

let baseUrl = "http://localhost:8080/javaee_pos/";

$("#txtCustomerCount").val("00");
$.ajax({
    url: baseUrl + "customer?option=CustomerCount",
    method: "GET",
    contentType: "application/json",
    dataType: "json",
    success: function (resp) {
        let num = resp.count;
        $("#txtCustomerCount").text(num);
    },
    error: function (ob, statusText, error) {
        console.log("Error : ", error);
    }
});

$("#txtItemsCount").val("00");
$.ajax({
    url: baseUrl + "item?option=itemCount",
    method: "GET",
    contentType: "application/json",
    dataType: "json",
    success: function (resp) {
        let num = resp.count;
        $("#txtItemsCount").text(num);
    },
    error: function (ob, statusText, error) {
        console.log("Error : ", error);
    }
});

$("#txtOrderCount").val("00");
$.ajax({
    url: baseUrl + "orders?option=ordersCount",
    method: "GET",
    contentType: "application/json",
    dataType: "json",
    success: function (resp) {
        let num = resp.count;
        $("#txtOrderCount").text(num);
    },
    error: function (ob, statusText, error) {
        console.log("Error : ", error);
    }
});