import {wishListApi} from "../services/WishListService.js";
import {cartApi} from "../services/CartService.js";

function showResult(message, success = true) {
    showToast(message, success ? 'success' : 'error', 3000);
}

// Function to update cart count in header (if exists)
function updateCartCount() {
    // Look for cart count element in header/navigation
    const cartCountElement = document.querySelector('.cart-count, .cart-badge, [data-cart-count]');
    if (cartCountElement) {
        // Get current count and increment
        const currentCount = parseInt(cartCountElement.textContent) || 0;
        cartCountElement.textContent = currentCount + 1;
        
        // Add animation effect
        cartCountElement.style.transform = 'scale(1.2)';
        setTimeout(() => {
            cartCountElement.style.transform = 'scale(1)';
        }, 200);
    }
}

document.addEventListener('DOMContentLoaded', function () {
    // Add to Cart functionality
    const addToCartBtn = document.querySelector('.add-to-cart');
    if (addToCartBtn) {
        addToCartBtn.addEventListener('click', async function () {
            const bookId = this.getAttribute('data-book-id');
            const button = this;

            // Disable button to prevent double clicks
            button.disabled = true;
            button.textContent = 'Adding...';

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
                        updateCartCount();
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
                    const errorText = await response.text();
                    showResult('Failed to add book to cart', false);
                }
            } catch (error) {
                console.error('Add to cart error:', error);
                showResult('Network error. Please try again.', false);
            } finally {
                // Re-enable button
                button.disabled = false;
                button.innerHTML = `
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640"><!--!Font Awesome Free v7.0.1 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2025 Fonticons, Inc.--><path d="M24 48C10.7 48 0 58.7 0 72C0 85.3 10.7 96 24 96L69.3 96C73.2 96 76.5 98.8 77.2 102.6L129.3 388.9C135.5 423.1 165.3 448 200.1 448L456 448C469.3 448 480 437.3 480 424C480 410.7 469.3 400 456 400L200.1 400C188.5 400 178.6 391.7 176.5 380.3L171.4 352L475 352C505.8 352 532.2 330.1 537.9 299.8L568.9 133.9C572.6 114.2 557.5 96 537.4 96L124.7 96L124.3 94C119.5 67.4 96.3 48 69.2 48L24 48zM208 576C234.5 576 256 554.5 256 528C256 501.5 234.5 480 208 480C181.5 480 160 501.5 160 528C160 554.5 181.5 576 208 576zM432 576C458.5 576 480 554.5 480 528C480 501.5 458.5 480 432 480C405.5 480 384 501.5 384 528C384 554.5 405.5 576 432 576z"/></svg>
                    Thêm vào giỏ hàng
                `;
            }
        });
    }

    // Add to Wishlist functionality
    const addToWishlistBtn = document.querySelector('.add-to-wishlist');
    if (addToWishlistBtn) {
        addToWishlistBtn.addEventListener('click', async function () {
            const bookId = this.getAttribute('data-book-id');
            const response = await wishListApi.addToWishList({itemId: bookId});
            const body = await response.json();

            if (body.success) {
                showResult(body.message, true);
            } else {
                showResult(body.message, false);
            }
        });
    }
});