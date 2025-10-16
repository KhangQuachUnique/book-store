package service;

import dao.CartDAO;
import model.Cart;
import model.CartItem;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CartService {

    private static final long DEFAULT_SHIPPING_FEE = 30_000L;

    private final CartDAO cartDAO;

    public CartService() {
        this.cartDAO = new CartDAO();
    }

    public CartService(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    public Cart getCart(Long userId) {
        if (userId == null) {
            return null;
        }

        Cart cart = cartDAO.getCartWithItems(userId);

        if (cart == null) {
            Cart emptyCart = new Cart();
            emptyCart.setItems(Collections.emptyList());
            return emptyCart;
        }

        if (cart.getItems() == null) {
            cart.setItems(Collections.emptyList());
        }

        return cart;
    }

    public List<CartItem> getItems(Long userId) {
        Cart cart = getCart(userId);
        if (cart == null || cart.getItems() == null) {
            return Collections.emptyList();
        }
        return cart.getItems();
    }

    public Cart addToCart(Long userId, Long bookId, int quantity) {
        if (userId == null || bookId == null) {
            throw new IllegalArgumentException("User ID and Book ID are required");
        }
        cartDAO.addToCart(userId, bookId.intValue(), Math.max(1, quantity));
        return cartDAO.getCartWithItems(userId);
    }

    public CartUpdateResult updateItemQuantity(Long userId, Long cartItemId, int quantity) {
        if (userId == null || cartItemId == null) {
            throw new IllegalArgumentException("User ID and Cart Item ID are required");
        }

        cartDAO.updateCartQuantity(userId, cartItemId, Math.max(1, quantity));
        Cart cart = cartDAO.getCartWithItems(userId);
        List<CartItem> items = cart != null && cart.getItems() != null ? cart.getItems() : Collections.emptyList();

        double subtotal = calculateSubtotal(items);
        double itemTotal = items.stream()
                .filter(item -> Objects.equals(item.getId(), cartItemId))
                .mapToDouble(CartItem::getSubtotal)
                .findFirst()
                .orElse(0D);

        return new CartUpdateResult(itemTotal, subtotal);
    }

    public void removeItem(Long userId, Long cartItemId) {
        if (userId == null || cartItemId == null) {
            throw new IllegalArgumentException("User ID and Cart Item ID are required");
        }
        cartDAO.removeFromCart(userId, cartItemId);
    }

    public void clearCart(Long userId) {
        if (userId == null) {
            return;
        }
        cartDAO.clearCart(userId);
    }

    /**
     * Đếm tổng số lượng items trong giỏ hàng
     *
     * @param userId ID của user
     * @return Tổng số lượng items
     */
    public int getItemCount(Long userId) {
        if (userId == null) {
            return 0;
        }
        return cartDAO.countItems(userId);
    }

    public double calculateSubtotal(Cart cart) {
        if (cart == null || cart.getItems() == null) {
            return 0D;
        }
        return calculateSubtotal(cart.getItems());
    }

    public double calculateSubtotal(List<CartItem> items) {
        if (items == null) {
            return 0D;
        }
        return items.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }

    public long getShippingFee() {
        return DEFAULT_SHIPPING_FEE;
    }

    public double calculateTotalWithShipping(List<CartItem> items) {
        return calculateSubtotal(items) + getShippingFee();
    }

    public record CartUpdateResult(double itemTotal, double cartTotal) {
    }

}
