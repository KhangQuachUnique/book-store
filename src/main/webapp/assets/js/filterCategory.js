// filterCategory.js
// Tự động kiểm tra trùng tên khi thêm mới, tự động lọc khi gõ trên manageCategory

document.addEventListener('DOMContentLoaded', function() {
    // --- Add Category: kiểm tra trùng tên ---
    const nameInput = document.querySelector('form[action*="/admin/category"] input[name="name"]');
    const addForm = document.querySelector('form[action*="/admin/category"]');

    if (nameInput && addForm) {
        let debounceTimer;
        nameInput.addEventListener('input', function() {
            const val = nameInput.value.trim();

            // Chỉ kiểm tra khi có ký tự
            if (val.length > 0) {
                clearTimeout(debounceTimer);
                debounceTimer = setTimeout(() => {
                    const base = (typeof window !== 'undefined' && window.APP_CONTEXT) ? window.APP_CONTEXT : '';
                    const checkUrl = `${base}/admin/category?action=checkName&name=${encodeURIComponent(val)}`;

                    fetch(checkUrl)
                        .then(res => res.json())
                        .then(data => {
                            let msg = document.getElementById('name-exists-msg');
                            if (!msg) {
                                msg = document.createElement('div');
                                msg.id = 'name-exists-msg';
                                msg.className = 'invalid-feedback'; // Dùng class chuẩn của Bootstrap 5
                                nameInput.parentNode.appendChild(msg);
                            }

                            // Hiển thị hoặc ẩn thông báo lỗi
                            if (data.exists) {
                                msg.textContent = 'Tên này đã tồn tại, vui lòng nhập tên khác!';
                                nameInput.classList.add('is-invalid');
                            } else {
                                msg.textContent = '';
                                nameInput.classList.remove('is-invalid');
                            }
                        })
                        .catch(error => console.error('Error checking name:', error)); // Thêm catch để debug
                }, 300);
            } else {
                // Xóa lỗi nếu người dùng xóa hết chữ
                 nameInput.classList.remove('is-invalid');
                 let msg = document.getElementById('name-exists-msg');
                 if (msg) msg.textContent = '';
            }
        });

        // Chặn submit form nếu đang có lỗi
        addForm.addEventListener('submit', function(e) {
            // Kiểm tra class 'is-invalid' thay vì textContent để chắc chắn hơn
            if (nameInput.classList.contains('is-invalid')) {
                e.preventDefault(); // Ngăn form gửi đi
                alert('Tên category đã tồn tại, vui lòng sửa lại trước khi lưu!');
                nameInput.focus();
            }
        });
    }

    // --- Manage Category: tự động lọc bảng khi gõ ---
    const searchInput = document.querySelector('input[name="keyword"]');
    const tableRows = document.querySelectorAll('table tbody tr');
    if (searchInput && tableRows.length > 0) {
        // Ẩn nút Search nếu có
        const btn = searchInput.parentNode.querySelector('button');
        if (btn) btn.style.display = 'none';
        searchInput.addEventListener('input', function() {
            const val = searchInput.value.trim().toLowerCase();
            tableRows.forEach(row => {
                const nameCell = row.querySelector('td:nth-child(2)');
                if (nameCell && nameCell.textContent.toLowerCase().includes(val)) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        });
    }

    // --- Edit Category: tự động lọc combobox tên ---
    // Nếu muốn dùng combobox, cần chuyển input[name="name"] thành <select> hoặc dùng thư viện autocomplete
    // Nếu chỉ muốn gợi ý, có thể fetch danh sách tên và hiển thị gợi ý khi gõ
    // (code mẫu cho autocomplete đơn giản)
    const editNameInput = document.querySelector('form[action$="/admin/category"] input[name="name"]');
    if (editNameInput) {
        editNameInput.addEventListener('input', function() {
            const val = editNameInput.value.trim();
            if (val.length > 0) {
                fetch(`/admin/category?action=listNames&keyword=${encodeURIComponent(val)}`)
                    .then(res => res.json())
                    .then(data => {
                        let datalist = document.getElementById('category-names');
                        if (!datalist) {
                            datalist = document.createElement('datalist');
                            datalist.id = 'category-names';
                            editNameInput.setAttribute('list', 'category-names');
                            editNameInput.parentNode.appendChild(datalist);
                        }
                        datalist.innerHTML = '';
                        data.names.forEach(name => {
                            const option = document.createElement('option');
                            option.value = name;
                            datalist.appendChild(option);
                        });
                    });
            }
        });
    }
});
