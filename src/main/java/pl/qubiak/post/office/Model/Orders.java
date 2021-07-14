package pl.qubiak.post.office.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Orders {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "user_name")
    private String userName;
    private String role;
    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "waiting_time")
    private Date waitingTime;


    public Orders() {
    }

    public Orders(Long id, String userName, String role, Long orderId, Date waitingTime) {
        this.id = id;
        this.userName = userName;
        this.role = role;
        this.orderId = orderId;
        this.waitingTime = waitingTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Date getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(Date waitingTime) {
        this.waitingTime = waitingTime;
    }
}
