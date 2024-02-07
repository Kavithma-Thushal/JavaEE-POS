/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 6:56 PM - 1/15/2024
 **/

let baseUrl = "http://localhost:8080/javaee_pos/";

loadAllCustomers();

/**
 * Save Customer Button
 **/
$("#btnSaveCustomer").click(function () {
    let formData = $("#customerForm").serialize();
    $.ajax({
        url: baseUrl + "customer",
        method: "POST",
        data: formData,
        dataType: 'json',
        success: function (resp) {
            successAlert("Customer", resp.message);
            loadAllCustomers();
        },
        error: function (error) {
            errorAlert("Customer", JSON.parse(error.responseText).message);
        }
    });
});

/**
 * Update Customer Button
 **/
$("#btnUpdateCustomer").click(function () {
    let cusId = $("#txtCusId").val();
    let cusName = $("#txtCusName").val();
    let cusAddress = $("#txtCusAddress").val();
    let cusSalary = $("#txtCusSalary").val();

    let customerObj = {
        id: cusId,
        name: cusName,
        address: cusAddress,
        salary: cusSalary
    };

    $.ajax({
        url: baseUrl + "customer",
        method: "PUT",
        contentType: "application/json",        // Specify content type of the request body, Now server hope this type
        data: JSON.stringify(customerObj),      // This is the Actual Request
        dataType: 'json',    // Specify that you're expecting JSON data from the Server
        success: function (resp) {
            successAlert("Customer", resp.message);
            loadAllCustomers();
        },
        error: function (error) {
            errorAlert("Customer", JSON.parse(error.responseText).message);
        }
    });
});

/**
 * Delete Customer Button
 **/
$("#btnDeleteCustomer").click(function () {
    let deleteCusId = $("#txtCusId").val();

    $.ajax({
        url: baseUrl + "customer",
        method: "DELETE",
        contentType: "application/json",
        data: JSON.stringify({id: deleteCusId}),
        dataType: 'json',
        success: function (resp) {
            successAlert("Customer", resp.message);
            loadAllCustomers();
        },
        error: function (error) {
            errorAlert("Customer", JSON.parse(error.responseText).message);
        }
    });
});

/**
 * Search Customer Button
 **/
$("#btnSearchCustomer").click(function () {
    searchCustomer();
});
$("#txtSearchCusId").on("keypress", function (event) {
    if (event.key === "Enter") {
        searchCustomer();
    }
});

/**
 * Search Customer Method
 **/
function searchCustomer() {
    let searchCusId = $("#txtSearchCusId").val();
    $.ajax({
        url: baseUrl + "customer?id=" + searchCusId + "&option=searchCusId",
        method: "GET",
        contentType: "application/json",
        dataType: 'json',
        success: function (resp) {
            $("#customerTable").empty();
            let row = "<tr><td>" + resp.id + "</td><td>" + resp.name + "</td><td>" + resp.address + "</td><td>" + resp.salary + "</td></tr>";
            $("#customerTable").append(row);
            tableListener();
        },
        error: function (error) {
            emptyMessage(JSON.parse(error.responseText).message);
            loadAllCustomers();
        }
    });
}

/**
 * Get All Customers Button
 **/
$('#btnGetAllCustomers').click(function () {
    loadAllCustomers();
});

/**
 * Load All Customers Method
 **/
function loadAllCustomers() {
    $.ajax({
        url: baseUrl + "customer?option=loadAllCustomers",
        method: "GET",
        contentType: "application/json",
        dataType: "json",
        success: function (resp) {
            $("#customerTable").empty();

            for (let i of resp.data) {
                let id = i.id;
                let name = i.name;
                let address = i.address;
                let salary = i.salary;

                let row = "<tr><td>" + id + "</td><td>" + name + "</td><td>" + address + "</td><td>" + salary + "</td></tr>";
                $("#customerTable").append(row);
            }
            clearInputFields();
            tableListener();
            generateCustomerId();
        },
        error: function (error) {
            console.log("Load All Customers Error : " + error);
        }
    });
}

/**
 * Generate CustomerId Method
 **/
function generateCustomerId() {
    $("#txtCusId").val("C00-001");
    $.ajax({
        url: baseUrl + "customer?option=generateCusId",
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
 * Table Listener Method
 **/
function tableListener() {
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
 * Clear All Customers Button
 **/
$("#btnClearAllCustomer").click(function () {
    loadAllCustomers();
});

/**
 * Clear Input Fields
 **/
function clearInputFields() {
    $("#txtCusName").focus();
    $('#txtCusName').val("");
    $('#txtCusAddress').val("");
    $('#txtCusSalary').val("");
    $('#txtSearchCusId').val("");

    $("#btnSaveCustomer").attr('disabled', true);
    $("#btnUpdateCustomer").attr('disabled', true);
    $("#btnDeleteCustomer").attr('disabled', true);
}

/**
 * Customer Validations
 **/
const regExCusID = /^(C00-)[0-9]{3,4}$/;
const regExCusName = /^[A-z ]{3,20}$/;
const regExCusAddress = /^[A-z0-9/ ]{4,30}$/;
const regExSalary = /^[0-9]{1,}[.]?[0-9]{1,2}$/;

let customerValidations = [];
customerValidations.push({
    reg: regExCusID,
    field: $('#txtCusId'),
    error: 'Customer ID Pattern is Wrong : C00-001'
});
customerValidations.push({
    reg: regExCusName,
    field: $('#txtCusName'),
    error: 'Customer Name Pattern is Wrong : A-z 3-20'
});
customerValidations.push({
    reg: regExCusAddress,
    field: $('#txtCusAddress'),
    error: 'Customer Address Pattern is Wrong : A-z 0-9 ,/'
});
customerValidations.push({
    reg: regExSalary,
    field: $('#txtCusSalary'),
    error: 'Customer Salary Pattern is Wrong : 0-9{1,}.0-9{1,2}'
});

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

/**
 * Disable/Enable Buttons
 **/
function setButtonState(value) {
    if (value > 0) {
        $("#btnSaveCustomer").attr('disabled', true);
        $("#btnUpdateCustomer").attr('disabled', true);
        $("#btnDeleteCustomer").attr('disabled', true);
    } else {
        $("#btnSaveCustomer").attr('disabled', false);
        $("#btnUpdateCustomer").attr('disabled', false);
        $("#btnDeleteCustomer").attr('disabled', false);
    }
}