package pl.qubiak.post.office.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.qubiak.post.office.Model.Orders;
import pl.qubiak.post.office.RowMapper.OrdersRowMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Repository
public class OrderDao {

    private JdbcTemplate jdbcTemplate;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

    @Autowired
    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void newOrder(String userName, String role, Instant waitingTime) {
        final ZonedDateTime newWaitingTime = ZonedDateTime.ofInstant(waitingTime, ZoneId.of("UTC"));
        String sql = "INSERT INTO order_list(order_id, user_name, role, waiting_time) VALUE (NULL, ?, ?, ?)";
        jdbcTemplate.update(sql, userName, role, newWaitingTime);
    }

    public List<Orders> ordersList() {
        String sql = "SELECT * FROM order_list";
        return jdbcTemplate.query(sql, new OrdersRowMapper());
    }

    public String getOrderId(String username) {
        String sql = "SELECT order_id FROM order_list WHERE user_name = ?";
        return jdbcTemplate.queryForObject(sql, String.class, username);
    }

    public Orders getOrderById(int orderId) {
        String sql = "SELECT * FROM order_list WHERE order_id = ?";
        List<Orders> orders = jdbcTemplate.query(sql, new OrdersRowMapper(), orderId);
        return orders.get(0);
    }

    public List<Orders> getActiveOrders() {
        ZonedDateTime activeOrdersTime = ZonedDateTime.ofInstant(Instant.now().minus(20, ChronoUnit.SECONDS), ZoneId.of("UTC"));
        return getOrdersFromTime(activeOrdersTime);
    }

    public List<Orders> getOrdersFromTime(ZonedDateTime time) {
        final String formatTime = time.format(formatter);
        String sql = "SELECT * FROM order_list WHERE waiting_time > ? ORDER BY waiting_time ASC";
        return jdbcTemplate.query(sql, new OrdersRowMapper(), formatTime);
    }

    public void updateOrderWaitingTime(long orderId, Instant newTime) {
        final ZonedDateTime newWaitingTime = ZonedDateTime.ofInstant(newTime, ZoneId.of("UTC"));
        String sql = "UPDATE order_list SET waiting_time = ? WHERE order_id = ?";
        jdbcTemplate.update(sql, newWaitingTime, orderId);
    }

}

