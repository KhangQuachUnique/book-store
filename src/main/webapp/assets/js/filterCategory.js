// filterCategory.js
document.addEventListener('DOMContentLoaded', function() {
    // --- Add Category: kiểm tra trùng tên ---
    const nameInput = document.querySelector('form[action*="/adminn/category"] input[name="name"]');
    const addForm = document.querySelector('form[action*="/adminn/category"]');

    if (nameInput && addForm) {
        let debounceTimer;
        nameInput.addEventListener('input', function() {
            const val = nameInput.value.trim();

            // Chỉ kiểm tra khi có ký tự
            if (val.length > 0) {
                clearTimeout(debounceTimer);
                debounceTimer = setTimeout(() => {
                    let msg = document.getElementById('name-exists-msg');
                    if (!msg) {
                        msg = document.createElement('div');
                        msg.id = 'name-exists-msg';
                        nameInput.parentNode.appendChild(msg);
                    }
                    nameInput.classList.remove('is-invalid','is-valid');
                    msg.className='';

                    const localList = Array.isArray(window.EXISTING_CATEGORY_NAMES) ? window.EXISTING_CATEGORY_NAMES : null;
                    if (localList) {
                        const existsLocal = localList.some(n => n.toLowerCase() === val.toLowerCase());
                        if (existsLocal) {
                            msg.textContent = 'Tên đã tồn tại, vui lòng nhập tên khác! (local)';
                            nameInput.classList.add('is-invalid');
                            msg.classList.add('invalid-feedback');
                            return; // Không cần gọi server nữa
                        } else {
                            msg.textContent = 'Có thể sử dụng category này.';
                            nameInput.classList.add('is-valid');
                            msg.classList.add('valid-feedback');
                            return; // Đủ nhanh, bỏ qua gọi server
                        }
                    }

                    // Fallback nếu không có localList (trường hợp trang không inject)
                    const base = (typeof window !== 'undefined' && window.APP_CONTEXT) ? window.APP_CONTEXT : '';
                    const checkUrl = `${base}/adminn/category?action=checkName&name=${encodeURIComponent(val)}`;
                    fetch(checkUrl)
                        .then(res => { if(!res.ok) throw new Error('HTTP '+res.status); return res.json(); })
                        .then(data => {
                            if (data.exists) {
                                msg.textContent = 'Tên đã tồn tại, vui lòng nhập tên khác!';
                                nameInput.classList.add('is-invalid');
                                msg.classList.add('invalid-feedback');
                            } else {
                                msg.textContent = 'Có thể sử dụng category này.';
                                nameInput.classList.add('is-valid');
                                msg.classList.add('valid-feedback');
                            }
                        })
                        .catch(err => console.error('Error checking name (fallback):', err));
                }, 300);
            } else {
                // Xóa lỗi nếu người dùng xóa hết chữ
                 nameInput.classList.remove('is-invalid');
                 nameInput.classList.remove('is-valid');
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

    // --- Manage Category: lọc kết hợp theo tên và ID ---
    const searchInput = document.querySelector('input[name="keyword"]');
    const tableRows = document.querySelectorAll('table tbody tr');
    const idInput = document.querySelector('input[name="id"]');
    const tbody = document.querySelector('table tbody');

    // Hàm áp dụng bộ lọc kết hợp (AND): thỏa cả ID và Name nếu có nhập
    const applyCategoryFilters = () => {
        if (!tableRows || tableRows.length === 0) return;

        const nameVal = (searchInput ? searchInput.value.trim().toLowerCase() : '');
        const idVal = (idInput ? idInput.value.trim().toLowerCase() : '');

        tableRows.forEach(row => {
            const idCellText = row.querySelector('td:nth-child(1)')?.textContent?.toLowerCase() || '';
            const nameCellText = row.querySelector('td:nth-child(2)')?.textContent?.toLowerCase() || '';

            const matchId = idVal === '' || idCellText.includes(idVal);
            const matchName = nameVal === '' || nameCellText.includes(nameVal);

            row.style.display = (matchId && matchName) ? '' : 'none';
        });

        // Nếu đang lọc theo ID, sắp xếp các hàng đang hiển thị theo ID tăng dần
        if (idVal && tbody) {
            const visibleRows = Array.from(tableRows).filter(r => r.style.display !== 'none');
            visibleRows.sort((a, b) => {
                const aId = parseInt(a.querySelector('td:nth-child(1)')?.textContent || '0', 10);
                const bId = parseInt(b.querySelector('td:nth-child(1)')?.textContent || '0', 10);
                return aId - bId;
            });
            visibleRows.forEach(row => tbody.appendChild(row));
        }
    };

    if ((searchInput || idInput) && tableRows.length > 0) {
        if (searchInput) searchInput.addEventListener('input', applyCategoryFilters);
        if (idInput) idInput.addEventListener('input', applyCategoryFilters);
    }

    // --- Edit Category: tự động lọc combobox tên ---
    const editNameInput = document.querySelector('form[action$="/adminn/category"] input[name="name"]');
    if (editNameInput) {
        editNameInput.addEventListener('input', function() {
            const val = editNameInput.value.trim();
            if (val.length > 0) {
                const base = (typeof window !== 'undefined' && window.APP_CONTEXT) ? window.APP_CONTEXT : '';
                fetch(`${base}/adminn/category?action=listNames&keyword=${encodeURIComponent(val)}`)
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
