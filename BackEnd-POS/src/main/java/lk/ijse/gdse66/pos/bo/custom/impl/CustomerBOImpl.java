package lk.ijse.gdse66.pos.bo.custom.impl;

import lk.ijse.gdse66.pos.bo.custom.CustomerBO;
import lk.ijse.gdse66.pos.dao.DAOFactory;
import lk.ijse.gdse66.pos.dao.custom.CustomerDAO;
import lk.ijse.gdse66.pos.dto.CustomerDTO;
import lk.ijse.gdse66.pos.entity.Customer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 4:30 PM - 1/16/2024
 **/
public class CustomerBOImpl implements CustomerBO {
    private final CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);// hide the object creation logic through the factory

    @Override
    public boolean saveCustomer(CustomerDTO dto, Connection connection) throws SQLException, ClassNotFoundException {
        return customerDAO.save(new Customer(dto.getId(), dto.getName(), dto.getAddress(), dto.getSalary()), connection);
    }

    @Override
    public ArrayList<CustomerDTO> customerSearchId(String id, Connection connection) throws SQLException, ClassNotFoundException {
        ArrayList<Customer> all = customerDAO.searchId(id, connection);

        ArrayList<CustomerDTO> allCustomers = new ArrayList<>();
        for (Customer customer : all) {
            allCustomers.add(new CustomerDTO(customer.getId(), customer.getName(), customer.getAddress(), customer.getSalary()));
        }
        return allCustomers;
    }

    @Override
    public boolean updateCustomer(CustomerDTO dto, Connection connection) throws SQLException, ClassNotFoundException {
        return customerDAO.update(new Customer(dto.getId(), dto.getName(), dto.getAddress(), dto.getSalary()), connection);
    }

    @Override
    public boolean deleteCustomer(String id, Connection connection) throws SQLException, ClassNotFoundException {
        return customerDAO.delete(id, connection);
    }

    @Override
    public ArrayList<CustomerDTO> getAllCustomers(Connection connection) throws SQLException, ClassNotFoundException {
        ArrayList<Customer> all = customerDAO.getAll(connection);

        ArrayList<CustomerDTO> allCustomers = new ArrayList<>();
        for (Customer customer : all) {
            allCustomers.add(new CustomerDTO(customer.getId(), customer.getName(), customer.getAddress(), customer.getSalary()));
        }
        return allCustomers;
    }

    @Override
    public String generateNewCustomerID(Connection connection) throws SQLException, ClassNotFoundException {
        return customerDAO.generateNewID(connection);
    }
}
