// cart.js - fetch update for cart quantity

document.addEventListener('DOMContentLoaded', function() {
    function formatVND(number) {
        return new Intl.NumberFormat('vi-VN').format(number) + ' ₫';
    }


    document.querySelectorAll('.cart-quantity-input').forEach(function(input) {
        const form = input.closest('form');
        const row = input.closest('tr');
        const cartId = form.querySelector('input[name="cartId"]').value;

        // Remove update button if exists
        const updateBtn = form.querySelector('.quantity-update-btn');
        if (updateBtn) updateBtn.remove();

        // Create custom quantity control
        const wrapper = document.createElement('div');
        wrapper.className = 'quantity-control';
        const minusBtn = document.createElement('button');
        minusBtn.type = 'button';
        minusBtn.textContent = '−';
        minusBtn.className = 'quantity-btn';
        const qtyInput = document.createElement('input');
        qtyInput.type = 'number';
        qtyInput.value = input.value;
        qtyInput.min = 1;
        qtyInput.className = 'cart-quantity-input';
        const plusBtn = document.createElement('button');
        plusBtn.type = 'button';
        plusBtn.textContent = '+';
        plusBtn.className = 'quantity-btn';
        wrapper.appendChild(minusBtn);
        wrapper.appendChild(qtyInput);
        wrapper.appendChild(plusBtn);

        // Replace the form with the custom control (prevents duplicate controls)
        form.parentNode.replaceChild(wrapper, form);

        function updateQuantity(newQty) {
            fetch('cart', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: `action=update&cartId=${cartId}&quantity=${newQty}`
            })
            .then(res => {
                if (!res.ok) throw new Error('Network error');
                return res.json();
            })
            .then(data => {
                if (data && data.success) {
                    // Update row total
                    const totalCell = row.querySelector('td:nth-child(4) span');
                    if (totalCell) totalCell.textContent = formatVND(data.itemTotal);
                    // Update summary (find by label text for robustness)
                    document.querySelectorAll('.summary-row').forEach(function(row) {
                        const label = row.querySelector('span:first-child');
                        const value = row.querySelector('span:last-child');
                        if (label && value) {
                            if (label.textContent.trim().toLowerCase() === 'subtotal') {
                                value.textContent = formatVND(data.cartTotal);
                            }
                            if (label.textContent.trim().toLowerCase() === 'total') {
                                value.textContent = formatVND(data.cartTotal + 30000);
                            }
                        }
                    });
                } else {
                    alert(data && data.message ? data.message : 'Update failed');
                }
            })
            .catch((err) => {
                alert('Error updating cart.');
                console.error(err);
            });
        }

        minusBtn.addEventListener('click', function() {
            let val = parseInt(qtyInput.value, 10);
            if (val > 1) {
                qtyInput.value = val - 1;
                updateQuantity(qtyInput.value);
            }
        });
        plusBtn.addEventListener('click', function() {
            let val = parseInt(qtyInput.value, 10);
            qtyInput.value = val + 1;
            updateQuantity(qtyInput.value);
        });
        qtyInput.addEventListener('change', function() {
            let val = parseInt(qtyInput.value, 10);
            if (isNaN(val) || val < 1) {
                qtyInput.value = 1;
            }
            updateQuantity(qtyInput.value);
        });
        form.addEventListener('submit', function(e) { e.preventDefault(); });
    });
});
