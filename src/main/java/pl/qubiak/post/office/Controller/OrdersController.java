package pl.qubiak.post.office.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.qubiak.post.office.Dao.addOrderDao;
import pl.qubiak.post.office.Model.Orders;
import pl.qubiak.post.office.Validator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class OrdersController {

    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @Autowired
    public addOrderDao addOrderDao;

    @GetMapping(value = "/addOrder")
    public ModelAndView addOrder(Model model) {
        model.addAttribute("order", new Orders());
        return new ModelAndView("addOrder");
    }

    @GetMapping(value = "/newOrder")
    public String newOrders(@RequestParam(value = "username") String username,
                            @RequestParam(value = "role") String role,
                            @RequestParam(value = "pin", required = false) String pin) {
        if (!Validator.validate(role, pin)) {
            return "You entered wrong pin";
        }
        addOrderDao.newOrder(username, role, new Date(System.currentTimeMillis()));
        String orderId = addOrderDao.getOrderId(username);
        return "Your order id: " + orderId;
    }

    @GetMapping(value = "/getOrderList")
    public List<Orders> showOrders() {
        return addOrderDao.ordersList();
    }

    @GetMapping(value = "/getQueueNumber")
    public ModelAndView getQueueNumber(Model model) {
        return new ModelAndView("getQueueNumber");
    }

    @PostMapping(value = "/getQueueByOrderId")
    public List<Orders> getQueueByOrderId(@RequestParam(value = "orderid") int orderId) {
        return addOrderDao.getOrderById(orderId);
    }
}
