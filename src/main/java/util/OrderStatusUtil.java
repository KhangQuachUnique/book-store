package util;

import model.OrderStatus;
import java.util.ArrayList;
import java.util.List;

public class OrderStatusUtil {

    public static List<String> getAllStatuses() {
        List<String> statuses = new ArrayList<>();
        statuses.add("ALL"); // mục "Tất cả"
        for (OrderStatus status : OrderStatus.values()) {
            statuses.add(status.name());
        }
        return statuses;
    }
}
