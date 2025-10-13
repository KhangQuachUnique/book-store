// filterCategory.js
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
                            return;
                        } else {
                            msg.textContent = 'Có thể sử dụng category này.';
                            nameInput.classList.add('is-valid');
                            msg.classList.add('valid-feedback');
                            return;
                        }
                    }

                    // Fallback nếu không có localList (trường hợp trang không inject)
                    const base = (typeof window !== 'undefined' && window.APP_CONTEXT) ? window.APP_CONTEXT : '';
                    const checkUrl = `${base}/admin/category?action=checkName&name=${encodeURIComponent(val)}`;
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

    // --- Manage Category: tự động submit form khi nhập tìm kiếm ---
    const searchInput = document.querySelector('input[name="keyword"]');
    const idInput = document.querySelector('input[name="id"]');

    // Hàm debounce để tránh gửi quá nhiều request
    function debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }

    // Hàm submit form tìm kiếm
    const submitSearchForm = () => {
        const keywordValue = searchInput ? searchInput.value.trim() : '';
        const idValue = idInput ? idInput.value.trim() : '';
        
        // Tạo URL với các tham số tìm kiếm
        const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1));
        let url = `${contextPath}/admin/category`;
        
        if (keywordValue || idValue) {
            url += '?action=search';
            if (keywordValue) url += `&keyword=${encodeURIComponent(keywordValue)}`;
            if (idValue) url += `&id=${encodeURIComponent(idValue)}`;
        } else {
            url += '?action=list';
        }
        
        window.location.href = url;
    };

    // Debounced version của submitSearchForm
    const debouncedSubmit = debounce(submitSearchForm, 500);

    // Gắn sự kiện cho các ô tìm kiếm
    if (searchInput) {
        searchInput.addEventListener('input', debouncedSubmit);
    }
    
    if (idInput) {
        idInput.addEventListener('input', debouncedSubmit);
    }
});
