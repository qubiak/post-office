package pl.qubiak.post.office.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.qubiak.post.office.Model.Orders;
import pl.qubiak.post.office.RowMapper.OrdersRowMapper;

import java.util.Date;
import java.util.List;

@Repository
public class addOrderDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public addOrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void newOrder(String userName, String role, Date waitingTime) {
        String sql = "INSERT INTO order_list(order_id, user_name, role, waiting_time) VALUE (NULL, ?, ?, ?)";
        jdbcTemplate.update(sql, userName, role, waitingTime);
    }

    public List<Orders> ordersList() {
        String sql = "SELECT * FROM order_list";
        return jdbcTemplate.query(sql, new OrdersRowMapper());
    }

}
