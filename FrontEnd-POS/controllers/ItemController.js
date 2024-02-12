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
$("#txtSearchItemCode").on("keypress", function (event) {
    if (event.key === "Enter") {
        searchItem();
    }
});

/**
 * Search Item Method
 **/
function searchItem() {
    let searchItemCode = $("#txtSearchItemCode").val();
    $.ajax({
        url: baseUrl + "item?code=" + searchItemCode + "&option=searchItemCode",
        method: "GET",
        contentType: "application/json",
        dataType: "json",
        success: function (resp) {
            $("#itemTable").empty();
            let row = "<tr><td>" + resp.code + "</td><td>" + resp.description + "</td><td>" + resp.qty + "</td><td>" + resp.unitPrice + "</td></tr>";
            $("#itemTable").append(row);
            clearInputFields();
            tableListener();
        },
        error: function (error) {
            emptyMessage(JSON.parse(error.responseText).message);
            loadAllItems();
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
    $.ajax({
        url: baseUrl + "item?option=loadAllItems",
        method: "GET",
        contentType: "application/json",
        dataType: "json",
        success: function (resp) {
            $("#itemTable").empty();

            for (let i of resp.data) {
                let code = i.code;
                let description = i.description;
                let qty = i.qty;
                let unitPrice = i.unitPrice;

                let row = "<tr><td>" + code + "</td><td>" + description + "</td><td>" + qty + "</td><td>" + unitPrice + "</td></tr>";
                $("#itemTable").append(row);
            }
            clearInputFields();
            checkValidity(itemValidations);
            tableListener();
            generateItemCode();
        },
        error: function (error) {
            console.log("Load All Items Error : " + error);
        }
    });
}

/**
 * Generate Item Code Method
 **/
function generateItemCode() {
    $("#txtItemCode").val("I00-001");
    $.ajax({
        url: baseUrl + "item?option=generateItemCode",
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
            console.log("Generate Item Code Error : ", error);
        }
    });
}

/**
 * Table Listener Method
 **/
function tableListener() {
    $("#itemTable>tr").on("click", function () {
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
    loadAllItems();
});

/**
 * Clear Input Fields
 **/
function clearInputFields() {
    $("#txtItemDescription").focus();
    $("#txtItemDescription").val("");
    $("#txtItemQuantity").val("");
    $("#txtItemUnitPrice").val("");
    $("#txtSearchItemCode").val("");

    $("#btnAddItem").attr('disabled', true);
    $("#btnUpdateItem").attr('disabled', true);
    $("#btnDeleteItem").attr('disabled', true);
}

/**
 * Item Validations
 **/
let regExItemCode = /^(I00-)[0-9]{3}$/;
let regExItemDescription = /^[A-z ]{3,20}$/;
let regExItemQty = /^[0-9]{2,10}$/;
let regExItemUnitPrice = /^[0-9]{1,}[.]?[0-9]{1}$/;

let itemValidations = [];
itemValidations.push({
    reg: regExItemCode,
    field: $('#txtItemCode'),
    error: 'Item code must match the pattern I00-001'
});
itemValidations.push({
    reg: regExItemDescription,
    field: $('#txtItemDescription'),
    error: 'Item description must be between 3-20 characters'
});
itemValidations.push({
    reg: regExItemQty,
    field: $('#txtItemQuantity'),
    error: 'Item quantity must have 2-10 digits'
});
itemValidations.push({
    reg: regExItemUnitPrice,
    field: $('#txtItemUnitPrice'),
    error: 'Item price must have 2 digits with 2 decimal places'
});

/**
 * Check Item Validations
 **/
$("#txtItemCode,#txtItemDescription,#txtItemQuantity,#txtItemUnitPrice").on('keyup', function (event) {
    checkValidity(itemValidations);
});

/**
 * Disable TAB-KEY
 **/
$("#txtItemCode,#txtItemDescription,#txtItemQuantity,#txtItemUnitPrice,#txtSearchItemCode").on('keydown', function (event) {
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
    }
});
$("#txtItemDescription").on('keydown', function (event) {
    if (event.key === "Enter" && check(regExItemDescription, $("#txtItemDescription"))) {
        $("#txtItemQuantity").focus();
    }
});
$("#txtItemQuantity").on('keydown', function (event) {
    if (event.key === "Enter" && check(regExItemQty, $("#txtItemQuantity"))) {
        $("#txtItemUnitPrice").focus();
    }
});
$("#txtItemUnitPrice").on('keydown', function (event) {
    if (event.key === "Enter" && check(regExItemUnitPrice, $("#txtItemUnitPrice"))) {
        $("#btnAddItem").focus();
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