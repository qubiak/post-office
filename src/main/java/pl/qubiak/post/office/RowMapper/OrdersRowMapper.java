package pl.qubiak.post.office.RowMapper;

import org.springframework.jdbc.core.RowMapper;
import pl.qubiak.post.office.Model.Orders;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrdersRowMapper implements RowMapper {

    @Override
    public Orders mapRow(ResultSet resultSet, int i) throws SQLException {

        Orders orders = new Orders();
        orders.setOrderId(resultSet.getLong("order_id"));
        orders.setUserName(resultSet.getString("user_name"));
        orders.setRole(resultSet.getString("role"));
        orders.setWaitingTime(resultSet.getTimestamp("waiting_time").toInstant());
        return orders;
    }
}
