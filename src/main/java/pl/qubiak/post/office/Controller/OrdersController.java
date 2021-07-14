package pl.qubiak.post.office.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.qubiak.post.office.Dao.addOrderDao;
import pl.qubiak.post.office.Model.Orders;

import java.util.List;

@RestController
public class OrdersController {

    @Autowired
    public addOrderDao addOrderDao;

    @GetMapping(value = "/addOrder")
    public ModelAndView addOrder(Model model) {
        model.addAttribute("order", new Orders());
        return new ModelAndView("addOrder");
    }

    @GetMapping(value = "/newOrdes")
    public void newOrders(Orders orders) {
        addOrderDao.newOrder(orders.getUserName(), orders.getRole(), orders.getWaitingTime());
    }

    @GetMapping(value = "/getOrderList")
    public List<Orders> showOrders() {
        return addOrderDao.ordersList();
    }
}
