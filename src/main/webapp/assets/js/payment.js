document.addEventListener("DOMContentLoaded", function () {
    const applyBtn = document.querySelector(".apply-btn");
    const promoInput = document.getElementById("promoCodeInput");
    const promoMessage = document.getElementById("promoMessage");
    const discountRow = document.getElementById("discountRow");
    const totalValue = document.getElementById("totalValue");

    // Hàm xử lý áp dụng mã khuyến mãi
    const applyPromoCode = async () => {
        const code = promoInput.value.trim();

        // Kiểm tra nếu mã giảm giá trống
        if (!code) {
            promoMessage.style.color = "#a11a2b";
            promoMessage.textContent = "Vui lòng nhập mã khuyến mãi.";
            return;
        }

        try {
            // Gửi yêu cầu đến servlet
            const res = await fetch("/user/payment/apply-promotion", {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: "promoCode=" + encodeURIComponent(code)
            });

            const data = await res.json();
            console.log("Response:", data);

            // Kiểm tra kết quả từ máy chủ và cập nhật giao diện
            if (data.success) {
                promoMessage.style.color = "green";
                promoMessage.textContent = data.message;

                // Cập nhật Discount và Total
                discountRow.innerHTML = `
                    <div class="summary-row">
                        <span>Discount (${data.promotionCode} - ${data.discountPercent}%)</span>
                        <span>- ${data.discountAmount.toLocaleString('vi-VN')} ₫</span>
                    </div>`;

                totalValue.textContent = `${data.finalTotal.toLocaleString('vi-VN')} ₫`;
            } else {
                promoMessage.style.color = "#a11a2b";
                promoMessage.textContent = data.message;
                discountRow.innerHTML = "";
            }
        } catch (err) {
            console.error(err);
            promoMessage.style.color = "#a11a2b";
            promoMessage.textContent = "Có lỗi xảy ra khi áp dụng mã.";
        }
    };

    // Gán sự kiện click cho nút áp dụng mã
    applyBtn.addEventListener("click", applyPromoCode);
});