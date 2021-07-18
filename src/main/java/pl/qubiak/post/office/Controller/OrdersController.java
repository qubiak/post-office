package pl.qubiak.post.office.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.qubiak.post.office.Dao.OrderDao;
import pl.qubiak.post.office.Model.Orders;
import pl.qubiak.post.office.Validator;

import java.time.Instant;
import java.util.List;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import static pl.qubiak.post.office.NamesAndPins.STANDARD;
import static pl.qubiak.post.office.NamesAndPins.SUDDEN;
import static pl.qubiak.post.office.NamesAndPins.VIP;

@RestController
public class OrdersController {

    @Autowired
    public OrderDao orderDao;

    @GetMapping(value = "/addOrder")
    public ModelAndView addOrder(Model model) {
        model.addAttribute("order", new Orders());
        return new ModelAndView("addOrder");
    }

    @PostMapping(value = "/newOrder")
    public String newOrders(@RequestParam(value = "username") String username,
                            @RequestParam(value = "role") String role,
                            @RequestParam(value = "pin", required = false) String pin) {
        if (!Validator.validate(role, pin)) {
            return "You entered wrong pin";
        }
        List<Orders> activeOrders = orderDao.getActiveOrders();
        if (activeOrders.size() == 0) {
            orderDao.newOrder(username, role, Instant.now());
        } else if (role.equals(STANDARD)) {
            final Orders orders = activeOrders.get(activeOrders.size() - 1);
            Instant waitingTime = orders.getWaitingTime();
            int addProcessingTime = (orders.getRole().equals(SUDDEN) ? 40 : 20);
            final Instant newWaitintTime = waitingTime.plus(addProcessingTime, ChronoUnit.SECONDS);
            orderDao.newOrder(username, role, newWaitintTime);
        } else if (role.equals(VIP)) {
            final List<Orders> activeSuddenAndVip =
                    activeOrders.stream().filter(ord -> ord.getRole().equals(VIP) || ord.getRole().equals(SUDDEN)).collect(Collectors.toList());
            if (activeSuddenAndVip.isEmpty()) {
                orderDao.newOrder(username, role, Instant.now());
                activeOrders.forEach(o -> {
                    orderDao.updateOrderWaitingTime(o.getOrderId(), o.getWaitingTime().plus(20, ChronoUnit.SECONDS));
                });
            } else {
                addOrderAsSuddenVipInQueue(activeSuddenAndVip, username, role);
            }

        } else if (role.equals(SUDDEN)) {
            final List<Orders> activeSudden =
                    activeOrders.stream().filter(ord -> ord.getRole().equals(SUDDEN)).collect(Collectors.toList());
            if (activeSudden.isEmpty()) {
                orderDao.newOrder(username, role, Instant.now());
                activeOrders.forEach(o -> {
                    orderDao.updateOrderWaitingTime(o.getOrderId(), o.getWaitingTime().plus(20, ChronoUnit.SECONDS));
                });
            } else {
                addOrderAsSuddenVipInQueue(activeSudden, username, role);
            }
        }
        String orderId = orderDao.getOrderId(username);
        return "Your order id: " + orderId;
    }

    @GetMapping(value = "/getOrderList")
    public List<Orders> showOrders() {
        return orderDao.ordersList();
    }

    @GetMapping(value = "/getQueueNumber")
    public ModelAndView getQueueNumber(Model model) {
        return new ModelAndView("getQueueNumber");
    }

    @PostMapping(value = "/getQueueByOrderId")
    public String getQueueByOrderId(@RequestParam(value = "orderid") int orderId) {
        Orders order = orderDao.getOrderById(orderId);
        List<Orders> activeOrders = orderDao.getActiveOrders();
        if (!activeOrders.isEmpty()) {
            int placeInQueue = 0;
            for (int i = 0; i < activeOrders.size(); i++) {
                if (order.getOrderId() == activeOrders.get(i).getOrderId()) {
                    placeInQueue = i + 1;
                }
            }
            Duration between = Duration.between(Instant.now(), order.getWaitingTime());
            return String.format("You are %s in queue. Time to start processing: %s", placeInQueue, between.toSeconds());
        } else {
            return "There are no active orders";
        }
    }

    private void addOrderAsSuddenVipInQueue(List<Orders> ordersBefore, String username, String role) {
        final Orders lastSuddenOrVipInQueue = ordersBefore.get(ordersBefore.size() - 1);
        final Instant waitingTime = lastSuddenOrVipInQueue.getWaitingTime();
        int addProcessingTime = (lastSuddenOrVipInQueue.getRole().equals(SUDDEN) ? 40 : 20);
        final Instant newWaitintTime = waitingTime.plus(addProcessingTime, ChronoUnit.SECONDS);
        orderDao.newOrder(username, role, newWaitintTime);
        final List<Orders> laterOrders = orderDao.getOrdersFromTime(ZonedDateTime.ofInstant(waitingTime, ZoneId.of("UTC")));
        laterOrders.forEach(o -> {
            orderDao.updateOrderWaitingTime(o.getOrderId(), o.getWaitingTime().plus(addProcessingTime, ChronoUnit.SECONDS));
        });
    }
}
