package lk.ijse.gdse66.pos.bo.custom;

import lk.ijse.gdse66.pos.bo.SuperBO;
import lk.ijse.gdse66.pos.dto.CustomerDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 4:18 PM - 1/16/2024
 **/
public interface CustomerBO extends SuperBO {
    boolean saveCustomer(CustomerDTO dto, Connection connection) throws SQLException, ClassNotFoundException;

    ArrayList<CustomerDTO> customerSearchId(String id, Connection connection) throws SQLException, ClassNotFoundException;

    boolean updateCustomer(CustomerDTO dto, Connection connection) throws SQLException, ClassNotFoundException;

    boolean deleteCustomer(String id, Connection connection) throws SQLException, ClassNotFoundException;

    ArrayList<CustomerDTO> getAllCustomers(Connection connection) throws SQLException, ClassNotFoundException;

    String generateNewCustomerID(Connection connection) throws SQLException, ClassNotFoundException;
}
