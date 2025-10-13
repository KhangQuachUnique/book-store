const injected = (typeof window !== 'undefined' && window.APP_CONTEXT) ? window.APP_CONTEXT : null;
const contextPath = injected ?? (window.location.pathname.split("/")[1] ? `/${window.location.pathname.split("/")[1]}` : "");
const BASE_URL = contextPath;

document.addEventListener("DOMContentLoaded", function () {
    const applyBtn = document.querySelector(".apply-btn");
    const promoInput = document.getElementById("promoCodeInput");
    const promoMessage = document.getElementById("promoMessage");
    const discountRow = document.getElementById("discountRow");
    const totalValue = document.getElementById("totalValue");

    applyBtn.addEventListener("click", async function () {
        const code = promoInput.value.trim();
        if (!code) {
            promoMessage.style.color = "#a11a2b";
            promoMessage.textContent = "Vui lòng nhập mã khuyến mãi.";
            return;
        }

        try {
            const res = await fetch(BASE_URL + "/user/payment/apply-promotion", {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: "promoCode=" + encodeURIComponent(code)
            });

            const data = await res.json();
            console.log("Response:", data);
            console.log(discountRow);


            if (data.success) {
                promoMessage.style.color = "green";
                promoMessage.textContent = data.message;

                // Cập nhật Discount + Total
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
    });
});