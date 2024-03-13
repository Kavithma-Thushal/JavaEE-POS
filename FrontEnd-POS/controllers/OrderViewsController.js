/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 6:56 PM - 1/15/2024
 **/

let baseUrl = "http://localhost:8080/javaee_pos/";

/**
 * Load All Orders
 **/
$.ajax({
    url: baseUrl + "orders?option=LoadOrders",
    method: "GET",
    dataType: "json",
    success: function (res) {
        $("#tblOrder").empty();

        for (let i of res.data) {
            let orderId = i.orderId;
            let date = i.date;
            let cusId = i.cusId;

            let row = "<tr><td>" + orderId + "</td><td>" + date + "</td><td>" + cusId + "</td></tr>";
            $("#tblOrder").append(row);
        }
    },
    error: function (error) {
        console.log("Load All Orders Error : " + JSON.parse(error.responseText).message);
    }
});

/**
 * Load All OrderDetails
 **/
$.ajax({
    url: baseUrl + "orders?option=LoadOrderDetails",
    method: "GET",
    dataType: "json",
    success: function (res) {
        $("#tblOrderDetails").empty();

        for (let i of res.data) {
            let OrderId = i.OrderId;
            let code = i.code;
            let qty = i.qty;
            let unitPrice = i.unitPrice;

            let row = "<tr><td>" + OrderId + "</td><td>" + code + "</td><td>" + qty + "</td><td>" + unitPrice + "</td></tr>";
            $("#tblOrderDetails").append(row);
        }
    },
    error: function (error) {
        console.log("Load All OrdersDetails Error : " + JSON.parse(error.responseText).message);
    }
});