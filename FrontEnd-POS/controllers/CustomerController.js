/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 6:56 PM - 1/15/2024
 **/

let baseUrl = "http://localhost:8080/javaee_pos/";

/**
 * Save Customer
 **/
$("#btnSaveCustomer").click(function () {
    let formData = $("#customerForm").serialize();
    $.ajax({
        url: baseUrl + "customer",
        method: "POST",
        data: formData,
        dataType: "json",
        success: function (res) {
            saveUpdateAlert("Customer", res.message);
            loadAllCustomers();
        },
        error: function (error) {
            unSuccessUpdateAlert("Customer", JSON.parse(error.responseText).message);
        }
    });
});

/**
 * Update Customer
 **/
$("#btnUpdateCustomer").click(function () {
    let cusId = $("#txtCusId").val();
    let cusName = $("#txtCusName").val();
    let cusAddress = $("#txtCusAddress").val();
    let cusSalary = $("#txtCusSalary").val();

    const customerObj = {
        id: cusId,
        name: cusName,
        address: cusAddress,
        salary: cusSalary
    };

    $.ajax({
        url: baseUrl + "customer",
        method: "PUT",
        contentType: "application/json",
        data: JSON.stringify(customerObj),
        dataType: "json",
        success: function (res) {
            saveUpdateAlert("Customer", res.message);
            loadAllCustomers();
        },
        error: function (error) {
            unSuccessUpdateAlert("Customer", JSON.parse(error.responseText).message);
        }
    });
});

/**
 * Delete Customer
 **/
$("#btnDeleteCustomer").click(function () {
    let cusId = $("#txtCusId").val();

    $.ajax({
        url: baseUrl + "customer?id=" + cusId,
        method: "DELETE",
        contentType: "application/json",
        dataType: "json",
        success: function (res) {
            saveUpdateAlert("Customer", res.message);
            loadAllCustomers();
        },
        error: function (error) {
            unSuccessUpdateAlert("Customer", JSON.parse(error.responseText).message);
        }
    });
});

/**
 * Search Customer
 **/
$("#btnSearchCustomer").click(function () {
    searchCustomer();
});
$("#txtSearchCusId").on("keypress", function (event) {
    if (event.which === 13) {
        searchCustomer();
    }
});

function searchCustomer() {
    let searchId = $("#txtSearchCusId").val();
    $.ajax({
        url: baseUrl + "customer?id=" + searchId + "&option=searchCusId",
        method: "GET",
        contentType: "application/json",
        dataType: "json",
        success: function (res) {
            $("#customerTable").empty();
            let row = "<tr><td>" + res.id + "</td><td>" + res.name + "</td><td>" + res.address + "</td><td>" + res.salary + "</td></tr>";
            $("#customerTable").append(row);
            blindClickEvents();
        },
        error: function (error) {
            emptyMassage(JSON.parse(error.responseText).message);
            loadAllCustomers();
        }
    });
}

/**
 * Get All Customers
 **/
$('#btnGetAllCustomers').click(function () {
    loadAllCustomers();
});

/**
 * Load All Customers
 **/
function loadAllCustomers() {
    $.ajax({
        url: baseUrl + "customer?option=loadAllCustomer",
        method: "GET",
        contentType: "application/json",
        dataType: "json",
        success: function (res) {
            $("#customerTable").empty();

            for (let i of res.data) {
                let id = i.id;
                let name = i.name;
                let address = i.address;
                let salary = i.salary;

                let row = "<tr><td>" + id + "</td><td>" + name + "</td><td>" + address + "</td><td>" + salary + "</td></tr>";
                $("#customerTable").append(row);
            }
            blindClickEvents();
            generateCustomerID();
            clearInputFields();
        },
        error: function (error) {
            console.log("Load All Error : " + error);
        }
    });
}

/**
 * Clear All Button
 **/
$("#btnClearAllCustomer").click(function () {
    clearInputFields();
    loadAllCustomers();
});

/**
 * Table Listener
 **/
function blindClickEvents() {
    $("#customerTable>tr").on("click", function () {
        let id = $(this).children().eq(0).text();
        let name = $(this).children().eq(1).text();
        let address = $(this).children().eq(2).text();
        let salary = $(this).children().eq(3).text();

        $("#txtCusId").val(id);
        $("#txtCusName").val(name);
        $("#txtCusAddress").val(address);
        $("#txtCusSalary").val(salary);

        $("#btnSaveCustomer").attr('disabled', true);
        $("#btnUpdateCustomer").attr('disabled', false);
        $("#btnDeleteCustomer").attr('disabled', false);
    });
}

/**
 * Generate Customer ID
 **/
function generateCustomerID() {
    $("#txtCusId").val("C00-001");
    $.ajax({
        url: baseUrl + "customer?option=CustomerIdGenerate",
        method: "GET",
        contentType: "application/json",
        dataType: "json",
        success: function (resp) {
            let id = resp.id;
            let tempId = parseInt(id.split("-")[1]);
            tempId = tempId + 1;
            if (tempId <= 9) {
                $("#txtCusId").val("C00-00" + tempId);
            } else if (tempId <= 99) {
                $("#txtCusId").val("C00-0" + tempId);
            } else {
                $("#txtCusId").val("C00-" + tempId);
            }
        },
        error: function (error) {
            console.log("Generate Customer Id Error : ", error);
        }
    });
}

/**
 * Disable Buttons
 **/
$("#btnSaveCustomer").attr('disabled', true);
$("#btnUpdateCustomer").attr('disabled', true);
$("#btnDeleteCustomer").attr('disabled', true);
$("#txtCusId").attr('disabled', true);
loadAllCustomers();

/**
 * Clear Input Fields
 **/
function clearInputFields() {
    $("#txtCusName").focus();
    $('#txtCusName').val("");
    $('#txtCusAddress').val("");
    $('#txtCusSalary').val("");
    $('#txtSearchCusId').val("");
    checkValidity(customerValidations);
}

/**
 * Disable TAB-KEY
 **/
$("#txtCusId,#txtCusName,#txtCusAddress,#txtCusSalary").on('keydown', function (event) {
    if (event.key === "Tab") {
        event.preventDefault();
    }
});

$("#txtCusId,#txtCusName,#txtCusAddress,#txtCusSalary").on('keyup', function (event) {
    checkValidity(customerValidations);
});

$("#txtCusId,#txtCusName,#txtCusAddress,#txtCusSalary").on('blur', function (event) {
    checkValidity(customerValidations);
});

/**
 * Enable ENTER-KEY
 **/
$("#txtCusId").on('keydown', function (event) {
    if (event.key === "Enter" && check(regExCusID, $("#txtCusId"))) {
        $("#txtCusName").focus();
    } else {
        focusText($("#txtCusId"));
    }
});
$("#txtCusName").on('keydown', function (event) {
    if (event.key === "Enter" && check(regExCusName, $("#txtCusName"))) {
        focusText($("#txtCusAddress"));
    }
});
$("#txtCusAddress").on('keydown', function (event) {
    if (event.key === "Enter" && check(regExCusAddress, $("#txtCusAddress"))) {
        focusText($("#txtCusSalary"));
    }
});
$("#txtCusSalary").on('keydown', function (event) {
    if (event.key === "Enter" && check(regExSalary, $("#txtCusSalary"))) {
        if (event.which === 13) {
            $('#btnSaveCustomer').focus();
        }
    }
});