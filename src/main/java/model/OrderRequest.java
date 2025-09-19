package model;

public class OrderRequest {
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isValidStatus() {
        if (status == null) return false;
        
        return status.equals(Order.STATUS_PENDING) ||
               status.equals(Order.STATUS_PAID) ||
               status.equals(Order.STATUS_SHIPPED) ||
               status.equals(Order.STATUS_COMPLETED) ||
               status.equals(Order.STATUS_CANCELLED);
    }
}