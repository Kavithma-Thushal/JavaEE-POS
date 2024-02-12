/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 6:56 PM - 1/15/2024
 **/

let baseUrl = "http://localhost:8080/javaee_pos/";

loadAllItems();

/**
 * Save Item Button
 **/
$("#btnAddItem").click(function () {
    let formData = $("#itemForm").serialize();
    $.ajax({
        url: baseUrl + "item",
        method: "POST",
        data: formData,
        dataType: "json",
        success: function (resp) {
            successAlert("Item", resp.message);
            loadAllItems();
        },
        error: function (error) {
            errorAlert("Item", JSON.parse(error.responseText).message);
        }
    });
});

/**
 * Update Item Button
 **/
$("#btnUpdateItem").click(function () {
    let code = $("#txtItemCode").val();
    let description = $("#txtItemDescription").val();
    let qty = $("#txtItemQuantity").val();
    let unitPrice = $("#txtItemUnitPrice").val();

    var itemObj = {
        code: code,
        description: description,
        qty: qty,
        unitPrice: unitPrice
    }

    $.ajax({
        url: baseUrl + "item",
        method: "PUT",
        contentType: "application/json",    // Specify content type of the request body, Now server hope this type
        data: JSON.stringify(itemObj),      // This is the Actual Request
        dataType: 'json',    // Specify that you're expecting JSON data from the Server
        success: function (resp) {
            successAlert("Item", resp.message);
            loadAllItems();
        },
        error: function (error) {
            errorAlert("Item", JSON.parse(error.responseText).message);
        }
    });
});

/**
 * Delete Item Button
 **/
$("#btnDeleteItem").click(function () {
    let deleteItemCode = $("#txtItemCode").val();

    $.ajax({
        url: baseUrl + "item",
        method: "DELETE",
        contentType: "application/json",
        data: JSON.stringify({code: deleteItemCode}),
        dataType: 'json',
        success: function (resp) {
            successAlert("Item", resp.message);
            loadAllItems();
        },
        error: function (error) {
            errorAlert("Item", JSON.parse(error.responseText).message);
        }
    });
});

/**
 * Search Item Button
 **/
$('#btnSearchItem').click(function () {
    searchItem();
});
$("#ItemIdSearch").on("keypress", function (event) {
    if (event.which === 13) {
        searchItem();
    }
});

/**
 * Search Item Method
 **/
function searchItem() {
    var search = $("#ItemIdSearch").val();
    $("#ItemTable").empty();
    $.ajax({
        url: baseUrl + "item?code=" + search + "&option=searchItemCode",
        method: "GET",
        contentType: "application/json",
        dataType: "json",
        success: function (res) {
            let row = "<tr><td>" + res.code + "</td><td>" + res.description + "</td><td>" + res.qty + "</td><td>" + res.unitPrice + "</td></tr>";
            $("#ItemTable").append(row);
            blindClickEvents();
        },
        error: function (error) {
            loadAllItems();
            let message = JSON.parse(error.responseText).message;
            emptyMessage(message);
        }
    })
};

/**
 * Get All Items Button
 **/
$('#btnGetAllItems').click(function () {
    loadAllItems();
});

/**
 * Load All Items Method
 **/
function loadAllItems() {
    $("#ItemTable").empty();
    $.ajax({
        url: baseUrl + "item?option=loadAllItem",
        method: "GET",
        dataType: "json",
        success: function (res) {
            for (let i of res.data) {
                let code = i.code;
                let description = i.description;
                let qty = i.qty;
                let unitPrice = i.unitPrice;

                let row = "<tr><td>" + code + "</td><td>" + description + "</td><td>" + qty + "</td><td>" + unitPrice + "</td></tr>";
                $("#ItemTable").append(row);
            }
            blindClickEvents();
            generateItemID();
            clearInputFields("", "", "", "");
        },
        error: function (error) {
            let message = JSON.parse(error.responseText).message;
            console.log(message);
        }
    });
}

/**
 * Generate Item Code Method
 **/
function generateItemID() {
    $("#txtItemCode").val("I00-001");
    $.ajax({
        url: baseUrl + "item?option=ItemIdGenerate",
        method: "GET",
        contentType: "application/json",
        dataType: "json",
        success: function (resp) {
            let code = resp.code;
            let tempId = parseInt(code.split("-")[1]);
            tempId = tempId + 1;
            if (tempId <= 9) {
                $("#txtItemCode").val("I00-00" + tempId);
            } else if (tempId <= 99) {
                $("#txtItemCode").val("I00-0" + tempId);
            } else {
                $("#txtItemCode").val("I00-" + tempId);
            }
        },
        error: function (ob, statusText, error) {
            console.log("Error : ", error);
        }
    });
}

/**
 * Table Listener Method
 **/
function blindClickEvents() {
    $("#ItemTable>tr").on("click", function () {
        let code = $(this).children().eq(0).text();
        let description = $(this).children().eq(1).text();
        let qty = $(this).children().eq(2).text();
        let unitPrice = $(this).children().eq(3).text();

        $("#txtItemCode").val(code);
        $("#txtItemDescription").val(description);
        $("#txtItemQuantity").val(qty);
        $("#txtItemUnitPrice").val(unitPrice);

        $("#btnAddItem").attr('disabled', true);
        $("#btnUpdateItem").attr('disabled', false);
        $("#btnDeleteItem").attr('disabled', false);
    });
}

/**
 * Clear All Items Button
 **/
$("#btnClearAllItem").click(function () {
    $('#txtItemCode').val("");
    $('#txtItemDescription').val("");
    $('#txtItemUnitPrice').val("");
    $('#txtItemQuantity').val("");
    $('#ItemIdSearch').val("");
    loadAllItems();
});

/**
 * Clear Input Fields
 **/
function clearInputFields(code, description, qty, price) {
    //$("#txtItemCode").val(code);
    $("#txtItemDescription").val(description);
    $("#txtItemQuantity").val(qty);
    $("#txtItemUnitPrice").val(price);
    $("#txtItemDescription").focus();
    checkValidity(ItemsValidations);
    $("#btnAddItem").attr('disabled', true);
    $("#btnUpdateItem").attr('disabled', true);
    $("#btnDeleteItem").attr('disabled', true);
}

/**
 * Item Validations
 **/
$("#txtItemCode").focus();
const regExItemCode = /^(I00-)[0-9]{3,4}$/;
const regExItemName = /^[A-z ]{3,20}$/;
const regExItemPrice = /^[0-9]{1,10}$/;
const regExItemQtyOnHand = /^[0-9]{1,}[.]?[0-9]{1,2}$/;

let ItemsValidations = [];
ItemsValidations.push({
    reg: regExItemCode,
    field: $('#txtItemCode'),
    error: 'Item ID Pattern is Wrong : I00-001'
});
ItemsValidations.push({
    reg: regExItemName,
    field: $('#txtItemDescription'),
    error: 'Item Name Pattern is Wrong : A-z 3-20'
});
ItemsValidations.push({
    reg: regExItemPrice,
    field: $('#txtItemQuantity'),
    error: 'Item Qty Pattern is Wrong : 0-9 1-10'
});
ItemsValidations.push({
    reg: regExItemQtyOnHand,
    field: $('#txtItemUnitPrice'),
    error: 'Item Salary Pattern is Wrong : 100 or 100.00'
});

/**
 * Check Item Validations
 **/
$("#txtItemCode,#txtItemDescription,#txtItemQuantity,#txtItemUnitPrice").on('keyup', function (event) {
    checkValidity(ItemsValidations);
});

/**
 * Disable TAB-KEY
 **/
$("#txtItemCode,#txtItemDescription,#txtItemQuantity,#txtItemUnitPrice").on('keydown', function (event) {
    if (event.key === "Tab") {
        event.preventDefault();
    }
});

/**
 * Enable ENTER-KEY
 **/
$("#txtItemCode").on('keydown', function (event) {
    if (event.key === "Enter" && check(regExItemCode, $("#txtItemCode"))) {
        $("#txtItemDescription").focus();
    } else {
        focusText($("#txtItemCode"));
    }
});
$("#txtItemDescription").on('keydown', function (event) {
    if (event.key === "Enter" && check(regExItemName, $("#txtItemDescription"))) {
        focusText($("#txtItemQuantity"));
    }
});
$("#txtItemQuantity").on('keydown', function (event) {
    if (event.key === "Enter" && check(regExItemPrice, $("#txtItemQuantity"))) {
        focusText($("#txtItemUnitPrice"));
    }
});
$("#txtItemUnitPrice").on('keydown', function (event) {
    if (event.key === "Enter" && check(regExItemQtyOnHand, $("#txtItemUnitPrice"))) {
        if (event.which === 13) {
            $('#btnAddItem').focus();
        }
    }
});

/**
 * Disable/Enable Buttons
 **/
function setButtonState(value) {
    if (value > 0) {
        $("#btnAddItem").attr('disabled', true);
        $("#btnUpdateItem").attr('disabled', true);
        $("#btnDeleteItem").attr('disabled', true);
    } else {
        $("#btnAddItem").attr('disabled', false);
        $("#btnUpdateItem").attr('disabled', false);
        $("#btnDeleteItem").attr('disabled', false);
    }
}

/**
 * Disable Buttons
 **/
$("#btnAddItem").attr('disabled', true);
$("#btnUpdateItem").attr('disabled', true);
$("#btnDeleteItem").attr('disabled', true);