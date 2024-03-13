/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 6:56 PM - 1/15/2024
 **/

let baseUrl = "http://localhost:8080/javaee_pos/";

clearAll();

/**
 * Load All Customers to Combo-Box
 **/
$.ajax({
    url: baseUrl + "customer?option=loadAllCustomers",
    method: "GET",
    dataType: "json",
    success: function (res) {
        $("#cmbCustomerId").empty();

        for (let i of res.data) {
            let id = i.id;
            $("#cmbCustomerId").append(`<option>${id}</option>`);
        }
    },
    error: function (error) {
        console.log(JSON.parse("Load All Customers to Combo-Box Error : " + error.responseText).message);
    }
});

/**
 * Customer Searching Combo-Box
 **/
$("#cmbCustomerId").click(function () {
    let search = $("#cmbCustomerId").val();
    $.ajax({
        url: baseUrl + "customer?id=" + search + "&option=searchCusId",
        method: "GET",
        contentType: "application/json",
        dataType: "json",
        success: function (res) {
            $("#customerName").val(res.name);
            $("#customerAddress").val(res.address);
            $("#customerSalary").val(res.salary);
        },
        error: function (error) {
            console.log(JSON.parse("Customer Combo-Box Searching Error : " + error.responseText).message);
        }
    })
});

/**
 * Load All Items to Combo-Box
 **/
$.ajax({
    url: baseUrl + "item?option=loadAllItems",
    method: "GET",
    dataType: "json",
    success: function (res) {
        $("#cmbItemCode").empty();

        for (let i of res.data) {
            let code = i.code;
            $("#cmbItemCode").append(`<option>${code}</option>`);
        }
    },
    error: function (error) {
        console.log(JSON.parse("Load All Items to Combo-Box Error : " + error.responseText).message);
    }
});

/**
 * Item Searching Combo-Box
 **/
$("#cmbItemCode").click(function () {
    let search = $("#cmbItemCode").val();
    $.ajax({
        url: baseUrl + "item?code=" + search + "&option=searchItemCode",
        method: "GET",
        contentType: "application/json",
        dataType: "json",
        success: function (res) {
            $("#itemName").val(res.description);
            $("#itemPrice").val(res.unitPrice);
            $("#qtyOnHand").val(res.qty);
        },
        error: function (error) {
            console.log(JSON.parse("Item Combo-Box Searching Error : " + error.responseText).message);
        }
    })
});

/**
 * Generate OrderId Method
 **/
function generateOrderId() {
    $("#orderId").val("ODI-001");
    $.ajax({
        url: baseUrl + "orders?option=OrderIdGenerate",
        method: "GET",
        contentType: "application/json",
        dataType: "json",
        success: function (resp) {
            let orderId = resp.orderId;
            let tempId = parseInt(orderId.split("-")[1]);
            tempId = tempId + 1;
            if (tempId <= 9) {
                $("#orderId").val("ODI-00" + tempId);
            } else if (tempId <= 99) {
                $("#orderId").val("ODI-0" + tempId);
            } else {
                $("#orderId").val("ODI-" + tempId);
            }
            setDate();
        },
        error: function (ob, statusText, error) {
            console.log("Generate Order Id Error : ", error);
        }
    });
}

/**
 * Set Date
 **/
function setDate() {
    let date = new Date();
    let day = ("0" + date.getDate()).slice(-2);
    let month = ("0" + (date.getMonth() + 1)).slice(-2);
    let today = date.getFullYear() + "-" + (month) + "-" + (day);
    $('#orderDate').val(today);
}

/**
 * Order Details
 **/
let itemCode = 0;
let itemName = 0;
let itemPrice = 0;
let itemQty = 0;
let itemOrderQty = 0;
let total = 0;
let discount = 0;
let subTotal = 0;
let tableRow = [];

/**
 * Load Cart-Table-Details
 **/
function loadCartTableDetail() {
    itemCode = $("#cmbItemCode").val();
    itemName = $("#itemName").val();
    itemPrice = $("#itemPrice").val();
    itemQty = $("#qtyOnHand").val();
    itemOrderQty = $("#buyQty").val();

    let total = itemPrice * itemOrderQty;
    let row = `<tr><td>${itemCode}</td><td>${itemName}</td><td>${itemPrice}</td><td>${itemOrderQty}</td><td>${total}</td></tr>`;

    $("#tblAddToCart").append(row);
}

/**
 * Reduce Quantity
 **/
function reduceQty(orderQty) {
    let minQty = parseInt(orderQty);
    let reduceQty = parseInt($("#qtyOnHand").val());
    reduceQty = reduceQty - minQty;
    $("#qtyOnHand").val(reduceQty);
}

/**
 * Calculate Total
 **/
function calcTotal(amount) {
    total += amount;
    $("#txtTotal").val(total);
}

/**
 * Manage Available Qty
 **/
function manageQtyOnHand(preQty, nowQty) {
    var preQty = parseInt(preQty);
    var nowQty = parseInt(nowQty);
    let avaQty = parseInt($("#qtyOnHand").val());

    avaQty = avaQty + preQty;
    avaQty = avaQty - nowQty;

    $("#qtyOnHand").val(avaQty);
}

/**
 * Manage Total
 **/
function manageTotal(preTotal, nowTotal) {
    total -= preTotal;
    total += nowTotal;

    $("#txtTotal").val(total);
}

/**
 * Check QtyOnHand
 **/
$(document).on("change keyup blur", "#buyQty", function () {
    let qtyOnHand = $("#qtyOnHand").val();
    let buyQty = $("#buyQty").val();
    let buyOnHand = qtyOnHand - buyQty;
    if (buyOnHand < 0) {
        $("#lblCheckQty").parent().children('strong').text("Empty On Stock : " + qtyOnHand);
        $("#btnAddToCart").attr('disabled', true);
    } else {
        $("#lblCheckQty").parent().children('strong').text("");
        $("#btnAddToCart").attr('disabled', false);
    }
});

/**
 * Enter Discount and Sub Total
 **/
$(document).on("change keyup blur", "#txtDiscount", function () {
    discount = $("#txtDiscount").val();
    discount = (total / 100) * discount;
    subTotal = total - discount;
    $("#txtSubTotal").val(subTotal);
});

/**
 * Enter Cash and Balance
 **/
$(document).on("change keyup blur", "#txtCash", function () {
    let cash = $("#txtCash").val();
    let balance = cash - subTotal;
    $("#txtBalance").val(balance);
    if (balance < 0) {
        $("#lblCheckSubtotal").parent().children('strong').text(balance + " : Please enter valid balance");
        $("#btnPurchase").attr('disabled', true);
    } else {
        $("#lblCheckSubtotal").parent().children('strong').text("");
        $("#btnPurchase").attr('disabled', false);
    }
});

/**
 * Add To Cart Button
 **/
$("#btnAddToCart").on("click", function () {

    let duplicate = false;
    for (let i = 0; i < $("#tblAddToCart tr").length; i++) {
        if ($("#cmbItemCode option:selected").text() === $("#tblAddToCart tr").children(':nth-child(1)')[i].innerText) {
            duplicate = true;
        }
    }

    if (duplicate !== true) {
        loadCartTableDetail();
        reduceQty($("#buyQty").val());
        calcTotal($("#buyQty").val() * $("#itemPrice").val());
        $('#cmbItemCode,#itemName,#itemPrice,#qtyOnHand,#buyQty').val("");
        $("#btnAddToCart").attr('disabled', true);

    } else if (duplicate === true) {
        manageQtyOnHand(tableRow.children(':nth-child(4)').text(), $("#buyQty").val());
        $(tableRow).children(':nth-child(4)').text($("#buyQty").val());

        manageTotal(tableRow.children(':nth-child(5)').text(), $("#buyQty").val() * $("#itemPrice").val());
        $(tableRow).children(':nth-child(5)').text($("#buyQty").val() * $("#itemPrice").val());
    }

    /**
     * Table Listener
     **/
    $("#tblAddToCart>tr").click('click', function () {
        let itemCode = $(this).children(":eq(0)").text();
        let itemName = $(this).children(":eq(1)").text();
        let unitPrice = $(this).children(":eq(2)").text();
        let qty = $(this).children(":eq(3)").text();

        $("#cmbItemCode").val(itemCode);
        $("#itemName").val(itemName);
        $("#itemPrice").val(unitPrice);
        $("#buyQty").val(qty);
    });
});

/**
 * Purchase Order Button
 **/
$("#btnPurchase").click(function () {
    let orderDetails = [];
    for (let i = 0; i < $("#tblAddToCart tr").length; i++) {
        let detailOb = {
            orderId: $("#orderId").val(),
            itemId: $("#tblAddToCart tr").children(':nth-child(1)')[i].innerText,
            qty: $("#tblAddToCart tr").children(':nth-child(4)')[i].innerText,
            unitPrice: $("#tblAddToCart tr").children(':nth-child(5)')[i].innerText
        }
        orderDetails.push(detailOb);
    }
    let orderId = $("#orderId").val();
    let customerId = $("#cmbCustomerId option:selected").text();
    let date = $("#orderDate").val();

    let orderOb = {
        "orderId": orderId,
        "date": date,
        "customerId": customerId,
        "detail": orderDetails
    }

    $.ajax({
        url: baseUrl + "orders",
        method: "POST",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(orderOb),
        success: function (res) {
            successAlert("Order", res.message);
            generateOrderId();

        },
        error: function (error) {
            errorAlert("Order", JSON.parse(error.responseText).message);
        }
    });

    $.ajax({
        url: baseUrl + "orders",
        method: "PUT",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(orderOb)
    });

    clearAll();
    total = 0;
});

/**
 * Remove Row
 **/
$("#tblAddToCart").dblclick(function () {
    Swal.fire({
        title: 'Do you want to delete the selected row?',
        showDenyButton: true,
        confirmButtonText: 'Yes',
        denyButtonText: 'No',
        customClass: {
            actions: 'my-actions',
            denyButton: 'order-1',
            confirmButton: 'order-2'
        }
    }).then((result) => {
        if (result.isConfirmed) {
            $(this).children('tr').eq(0).remove();
        }
    })
});

/**
 * Clear All Button
 **/
$("#btnClearAll").click(function () {
    clearAll();
});

/**
 * Clear All Method
 **/
function clearAll() {
    $('#cmbCustomerId,#customerName,#customerAddress,#customerSalary,#cmbItemCode,#itemName,#itemPrice,#qtyOnHand,#buyQty,#txtTotal,#txtDiscount,#txtSubTotal,#txtCash,#txtBalance').val("");
    $("#tblAddToCart").empty();
    $("#btnAddToCart").attr('disabled', true);
    $("#btnPurchase").attr('disabled', true);
    generateOrderId();
}