package lk.ijse.gdse66.pos.bo.custom;

import lk.ijse.gdse66.pos.bo.SuperBO;
import lk.ijse.gdse66.pos.dto.ItemDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 4:18 PM - 1/16/2024
 **/
public interface ItemBO extends SuperBO {
    boolean saveItem(ItemDTO dto, Connection connection) throws SQLException, ClassNotFoundException;

    ArrayList<ItemDTO> itemSearchId(String id, Connection connection) throws SQLException, ClassNotFoundException;

    boolean updateItem(ItemDTO dto, Connection connection) throws SQLException, ClassNotFoundException;

    boolean deleteItem(String code, Connection connection) throws SQLException, ClassNotFoundException;

    ArrayList<ItemDTO> getAllItems(Connection connection) throws SQLException, ClassNotFoundException;

    String generateNewItemCode(Connection connection) throws SQLException, ClassNotFoundException;
}