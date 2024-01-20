package lk.ijse.gdse66.pos.dao.custom.impl;

import lk.ijse.gdse66.pos.dao.custom.CustomerDAO;
import lk.ijse.gdse66.pos.entity.Customer;
import lk.ijse.gdse66.pos.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 3:32 PM - 1/16/2024
 **/
public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public boolean save(Customer dto, Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(connection, "INSERT INTO Customer VALUES (?,?,?,?)", dto.getId(), dto.getName(), dto.getAddress(), dto.getSalary());
    }

    @Override
    public ArrayList<Customer> searchId(String id, Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(connection, "SELECT * FROM Customer WHERE id=?", id);
        ArrayList<Customer> allCustomer = new ArrayList<>();
        while (rst.next()) {
            allCustomer.add(new Customer(rst.getString(1), rst.getString(2), rst.getString(3), rst.getDouble(4)));
        }
        return allCustomer;
    }

    @Override
    public boolean update(Customer dto, Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(connection, "UPDATE Customer SET name= ? , address=? , salary=? WHERE id=?", dto.getName(), dto.getAddress(), dto.getSalary(), dto.getId());
    }

    @Override
    public boolean delete(String id, Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(connection, "DELETE FROM Customer WHERE id=?", id);
    }

    @Override
    public ArrayList<Customer> getAll(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute(connection, "SELECT * FROM Customer");
        ArrayList<Customer> obList = new ArrayList<>();
        while (result.next()) {
            obList.add(new Customer(result.getString(1), result.getString(2), result.getString(3), result.getDouble(4)));
        }
        return obList;
    }

    @Override
    public String generateNewID(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute(connection, "SELECT id FROM Customer ORDER BY id DESC LIMIT 1");
        if (result.next()) {
            return result.getString(1);
        } else {
            return null;
        }
    }
}