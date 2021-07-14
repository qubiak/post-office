package pl.qubiak.post.office.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Orders {

    @Id
    @GeneratedValue
    private Long orderId;
    @Column(name = "user_name")
    private String userName;
    private String role;
    @Column(name = "waiting_time")
    private Date waitingTime;

    public Orders() {
    }

    public Orders(Long orderId, String userName, String role, Date waitingTime) {
        this.orderId = orderId;
        this.userName = userName;
        this.role = role;
        this.waitingTime = waitingTime;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public Date getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(Date waitingTime) {
        this.waitingTime = waitingTime;
    }
}