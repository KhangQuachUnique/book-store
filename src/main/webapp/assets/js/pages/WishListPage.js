import { wishListApi } from "../services/WishListService.js";
import {cartApi} from "../services/CartService.js";

function showResult(message, success = true) {
    showToast(message, success ? 'success' : 'error', 3000);
}

document.addEventListener('DOMContentLoaded', () => {
    // Add to Cart functionality
    document.querySelectorAll('.add-to-cart').forEach(button => {
        button.addEventListener('click', async () => {
            const bookId = button.getAttribute('data-book-id');

            // No pending state: do not disable or change button text
            try {
                const response = await cartApi.addToCart({
                    bookId: bookId,
                    quantity: 1
                });

                if (response.ok) {
                    const data = await response.json();
                    if (data.success) {
                        showResult(data.message || 'Book added to cart successfully!', true);
                        // Update cart icon count if you have one
                        if (typeof updateCartCount === 'function') {
                            updateCartCount();
                        }
                    } else {
                        showResult(data.message || 'Failed to add book to cart', false);
                    }
                } else if (response.status === 401) {
                    // User not logged in
                    const data = await response.json();
                    showResult(data.message || 'Please log in to add items to cart', false);
                    // Optionally redirect to login page after a delay
                    setTimeout(() => {
                        window.location.href = '/login';
                    }, 2000);
                } else {
                    await response.text(); // consume body
                    showResult('Failed to add book to cart', false);
                }
            } catch (error) {
                console.error('Add to cart error:', error);
                showResult('Network error. Please try again.', false);
            }
        });
    });


    document.querySelectorAll('.heart-icon').forEach(svg => {
        svg.addEventListener('click', async (e) => {
            svg.classList.toggle('selected');
            const bookId = svg.getAttribute('data-book-id');
            if (svg.classList.contains('selected')) {
                // Add to wishlist (do not remove DOM)
                const response = await wishListApi.addToWishList({ itemId: bookId });
                const body = await response.json();

                if (body.success) {
                    showResult("Added to wishlist", true);
                } else {
                    showResult("Failed to add to wishlist", false);
                    // Revert UI change
                    svg.classList.remove('selected');
                }
            } else {
                // Remove from wishlist (do not remove DOM)
                const response = await wishListApi.removeFromWishList({ itemId: bookId });
                const body = await response.json();

                if (body.success) {
                    showResult(response.message || "Removed from wishlist", true);
                } else {
                    showResult(response.error || "Failed to remove from wishlist", false);
                    // Revert UI change
                    svg.classList.add('selected');
                }
            }
        });
    });
});
