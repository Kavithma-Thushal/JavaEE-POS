package lk.ijse.gdse66.pos.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 1:17 PM - 1/16/2024
 **/
public class CrudUtil {
    public static <T> T execute(Connection connection, String sql, Object... params) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            statement.setObject((i + 1), params[i]);
        }
        if (sql.startsWith("SELECT")) {
            return (T) statement.executeQuery();
        } else {
            return (T) (Boolean) (statement.executeUpdate() > 0);
        }
    }
}