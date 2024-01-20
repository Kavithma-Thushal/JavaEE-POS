package lk.ijse.gdse66.pos.dao.custom.impl;

import lk.ijse.gdse66.pos.dao.custom.ItemDAO;
import lk.ijse.gdse66.pos.entity.Item;
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
public class ItemDAOImpl implements ItemDAO {
    @Override
    public boolean save(Item dto, Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(connection, "INSERT INTO Item VALUES (?,?,?,?)", dto.getCode(), dto.getDescription(), dto.getQty(), dto.getUnitPrice());
    }

    @Override
    public ArrayList<Item> searchId(String code, Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute(connection, "SELECT * FROM Item WHERE code=?", code);
        ArrayList<Item> allItems = new ArrayList<>();
        while (result.next()) {
            allItems.add(new Item(result.getString(1), result.getString(2), result.getInt(3), result.getDouble(4)));
        }
        return allItems;
    }

    @Override
    public boolean update(Item dto, Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(connection, "UPDATE Item SET description= ? , qty=? , unitPrice=? WHERE code=?", dto.getDescription(), dto.getQty(), dto.getUnitPrice(), dto.getCode());
    }

    @Override
    public boolean delete(String code, Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(connection, "DELETE FROM Item WHERE code=?", code);
    }

    @Override
    public ArrayList<Item> getAll(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute(connection, "SELECT * FROM Item");
        ArrayList<Item> obList = new ArrayList<>();
        while (result.next()) {
            obList.add(new Item(result.getString(1), result.getString(2), result.getInt(3), result.getDouble(4)));
        }
        return obList;
    }

    @Override
    public String generateNewID(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute(connection, "SELECT code FROM Item ORDER BY code DESC LIMIT 1");
        if (result.next()) {
            return result.getString(1);
        } else {
            return null;
        }
    }
}