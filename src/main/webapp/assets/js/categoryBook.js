// Category Book JavaScript functionality
let includeCategories = [];
let excludeCategories = [];

//Hàm để cập nhật label hiển thị trong input Categories
function updateCategorySelectorLabel() {
    const input = document.getElementById('categorySelectorInput');
    if (!input) return;

    if (includeCategories.length > 0 && excludeCategories.length > 0) {
        input.value = `Included: ${includeCategories.length}, Excluded: ${excludeCategories.length}`;
        input.classList.add('selected');
    } else if (includeCategories.length > 0) {
        input.value = `Include ${includeCategories.length} categories`;
        input.classList.add('selected');
    } else if (excludeCategories.length > 0) {
        input.value = `Exclude ${excludeCategories.length} categories`;
        input.classList.add('selected');
    } else {
        input.value = '';
        input.placeholder = 'Include/Exclude Categories (Click to select)';
        input.classList.remove('selected');
    }
}

// Initialize from existing values if available
function initializeCategories() {
    // Get values from JSP variables (will be set by the JSP page)
    if (typeof includeCategories_jsp !== 'undefined' && includeCategories_jsp !== '') {
        includeCategories = includeCategories_jsp.split(',').map(id => parseInt(id));
    }
    
    if (typeof excludeCategories_jsp !== 'undefined' && excludeCategories_jsp !== '') {
        excludeCategories = excludeCategories_jsp.split(',').map(id => parseInt(id));
    }
    
    // Áp dụng trạng thái cho các category đã được chọn
    includeCategories.forEach(categoryId => {
        const element = document.querySelector(`[data-id="${categoryId}"]`);
        if (element) {
            element.classList.add('include');
        }
    });
    
    // Áp dụng trạng thái cho các category bị loại trừ
    excludeCategories.forEach(categoryId => {
        const element = document.querySelector(`[data-id="${categoryId}"]`);
        if (element) {
            element.classList.add('exclude');
        }
    });
    // Cập nhật label hiển thị
    updateCategorySelectorLabel();
}

function toggleCategoryTable() {
    const table = document.getElementById('categoryTable');
    table.classList.toggle('show');
}

function toggleCategory(element, categoryId) {
    if (element.classList.contains('include')) {
        // Từ Include -> Exclude (viền đỏ)
        element.classList.remove('include');
        element.classList.add('exclude');
        
        // Remove from include array, add to exclude array
        includeCategories = includeCategories.filter(id => id !== categoryId);
        if (!excludeCategories.includes(categoryId)) {
            excludeCategories.push(categoryId);
        }
    } else if (element.classList.contains('exclude')) {
        // Từ Exclude -> Unselected (mặc định, không viền)
        element.classList.remove('exclude');
        
        // Remove from exclude array
        excludeCategories = excludeCategories.filter(id => id !== categoryId);
    } else {
        // Từ Unselected -> Include (viền xanh)
        element.classList.add('include');
        
        // Add to include array
        if (!includeCategories.includes(categoryId)) {
            includeCategories.push(categoryId);
        }
    }

    // Cập nhật label hiển thị
    updateCategorySelectorLabel();
}

function submitFilterForm() {
    // Update hidden inputs với các categories được chọn và loại trừ (form tìm kiếm chính)
    const includeInput = document.getElementById('includeCategories');
    const excludeInput = document.getElementById('excludeCategories');
    if (includeInput) includeInput.value = includeCategories.join(',');
    if (excludeInput) excludeInput.value = excludeCategories.join(',');

    // Set action type là "categories" (ưu tiên category filter)
    const actionInput = document.getElementById('searchAction');
    if (actionInput) actionInput.value = 'categories';

    toggleCategoryTable();

    // Submit form - form sẽ tự động gửi tất cả input values
    const form = document.querySelector('.search-form');
    if (form) form.submit();
}

function setSearchAction(actionType) {
    // Set action type cho title search
    const input = document.getElementById('searchAction');
    if (input) input.value = actionType;
}

// Initialize when the page loads
document.addEventListener('DOMContentLoaded', function() {
    initializeCategories();
    
    // Add event listener cho Enter key trong title input
    const titleInput = document.querySelector('input[name="title"]');
    if (titleInput) {
        titleInput.addEventListener('keypress', function(event) {
            if (event.key === 'Enter') {
                setSearchAction('title');
                // Form sẽ tự động submit vì đây là Enter trong input
            }
        });
    }

    // Auto-open Advanced Panel if any advanced filters are present
    const advPanel = document.getElementById('advanceSearchPanel');
    if (advPanel) {
        const advForm = document.querySelector('.advance-search-form');
        const authorVal = advForm?.querySelector('input[name="author"]')?.value?.trim();
        const publishYearVal = advForm?.querySelector('input[name="publishYear"]')?.value?.trim();
        const yearBeforeVal = advForm?.querySelector('input[name="yearBefore"]')?.value?.trim();
        const yearAfterVal = advForm?.querySelector('input[name="yearAfter"]')?.value?.trim();
        const priceFromVal = advForm?.querySelector('input[name="priceFrom"]')?.value?.trim();
        const priceUpToVal = advForm?.querySelector('input[name="priceUpTo"]')?.value?.trim();
        const sortByVal = advForm?.querySelector('select[name="sortBy"]')?.value?.trim();

        const hasAdv = Boolean(authorVal || publishYearVal || yearBeforeVal || yearAfterVal || priceFromVal || priceUpToVal || sortByVal)
            || includeCategories.length > 0 || excludeCategories.length > 0;
        if (hasAdv) {
            advPanel.style.display = 'block';
            setTimeout(() => advPanel.classList.add('show'), 10);
        }
    }
});

function setupPublishYearValidation() {
    const input = document.getElementById('publishYearInput');
    if (input) {
        input.addEventListener('input', function(event) {
            // Lọc bỏ các ký tự không phải số
            let value = this.value.replace(/[^0-9]/g, '');
            // Giới hạn tối đa 4 ký tự
            if (value.length > 4) {
                value = value.substring(0, 4);
            }
            this.value = value;
        });
    }
}

function syncTitleAndSubmit() {
    // 1. Lấy giá trị từ thanh tìm kiếm (form chính)
    const mainTitleValue = document.getElementById('mainTitleInput').value;
    
    // 2. Gán giá trị đó vào input ẩn trong form Advanced Search
    const advancedTitleInput = document.getElementById('advancedTitleInput');
    if (advancedTitleInput) {
        advancedTitleInput.value = mainTitleValue;
    }
    
    // 3. Đồng bộ category selections sang form Advanced Search
    const advancedForm = document.querySelector('.advance-search-form');
    if (advancedForm) {
        let inc = advancedForm.querySelector('input[name="includeCategories"]');
        let exc = advancedForm.querySelector('input[name="excludeCategories"]');
        if (!inc) {
            inc = document.createElement('input');
            inc.type = 'hidden';
            inc.name = 'includeCategories';
            advancedForm.appendChild(inc);
        }
        if (!exc) {
            exc = document.createElement('input');
            exc.type = 'hidden';
            exc.name = 'excludeCategories';
            advancedForm.appendChild(exc);
        }
        inc.value = includeCategories.join(',');
        exc.value = excludeCategories.join(',');
    }

    // 4. Đặt action cho Advanced Search
    const searchActionInput = document.getElementById('advancedAction');
    if (searchActionInput) {
        searchActionInput.value = 'filter'; // Đặt action để Servlet biết đây là filter tổng hợp
    }
}

function setupNumericValidations() {
    const yearInputs = ['publishYearInput', 'yearBeforeInput', 'yearAfterInput'];
    const priceInputs = ['priceFromInput', 'priceUpToInput'];

    yearInputs.forEach(id => {
        const input = document.getElementById(id);
        if (input) {
            input.addEventListener('input', function() {
                let value = this.value.replace(/[^0-9]/g, '');
                if (value.length > 4) {
                    value = value.substring(0, 4);
                }
                this.value = value;
            });
        }
    });

    priceInputs.forEach(id => {
        const input = document.getElementById(id);
        if (input) {
            input.addEventListener('input', function() {
                // Chỉ cho phép số
                this.value = this.value.replace(/[^0-9]/g, '');
            });
        }
    });
}

function toggleAdvanceSearch() {
  const panel = document.getElementById("advanceSearchPanel");
  if (!panel) {
    console.warn("⚠️ Không tìm thấy advanceSearchPanel trong DOM");
    return;
  }

  if (panel.style.display === "none" || panel.style.display === "") {
    panel.style.display = "block";
    setTimeout(() => panel.classList.add("show"), 10);
  } else {
    panel.classList.remove("show");
    setTimeout(() => (panel.style.display = "none"), 300);
  }
}

function resetAdvanceSearch() {
    const form = document.querySelector(".advance-search-form");
    if (form) form.reset();

    // Xóa includeCategories và excludeCategories
    includeCategories = [];
    excludeCategories = []; 
    document.querySelectorAll('.category-item').forEach(item => {
        item.classList.remove('include', 'exclude');
    });
    document.getElementById('categorySelectorInput').value = '';

    const publishYearInput = document.getElementById('publishYearInput');
    if (publishYearInput) {
        publishYearInput.value = '';
    }

    // Xóa giá trị trong input TITLE chính khi reset advanced search
    const mainTitleInput = document.getElementById('mainTitleInput');
    if (mainTitleInput) {
        mainTitleInput.value = '';
    }
    
    // Xóa giá trị trong input HIDDEN title của Advanced Search
    const advancedTitleInput = document.getElementById('advancedTitleInput');
    if (advancedTitleInput) {
        advancedTitleInput.value = '';
    }

    updateCategorySelectorLabel();

    document.getElementById('includeCategories').value = '';
    document.getElementById('excludeCategories').value = '';

      //Reset trường action type
    const searchActionInput = document.getElementById('searchAction');
    if (searchActionInput) {
        searchActionInput.value = '';
    }
    const advancedActionInput = document.getElementById('advancedAction');
    if (advancedActionInput) {
        advancedActionInput.value = '';
    }

    //Reset các trường filter trong form Advanced Search (nếu form.reset() không hiệu quả)
    const advForm = document.querySelector('.advance-search-form');
    if (advForm) {
        const authorInput = advForm.querySelector('input[name="author"]');
        const publishYearAdv = advForm.querySelector('input[name="publishYear"]');
        const sortSelect = advForm.querySelector('select[name="sortBy"]');
        if (authorInput) authorInput.value = '';
        if (publishYearAdv) publishYearAdv.value = '';
        if (sortSelect) sortSelect.value = '';
    }

    // Reset các input Năm và Giá
    const yearBeforeInput = document.getElementById('yearBeforeInput');
    if (yearBeforeInput) yearBeforeInput.value = '';

    const yearAfterInput = document.getElementById('yearAfterInput');
    if (yearAfterInput) yearAfterInput.value = '';

    const priceFromInput = document.getElementById('priceFromInput');
    if (priceFromInput) priceFromInput.value = '';

    const priceUpToInput = document.getElementById('priceUpToInput');
    if (priceUpToInput) priceUpToInput.value = '';
}

document.addEventListener('DOMContentLoaded', function() {
    initializeCategories();
    setupPublishYearValidation(); // 🟢 GỌI HÀM RÀNG BUỘC
    setupNumericValidations();
});

// ✅ Đặt sau tất cả function:
window.toggleAdvanceSearch = toggleAdvanceSearch;
window.resetAdvanceSearch = resetAdvanceSearch;
window.toggleCategory = toggleCategory; 
window.syncTitleAndSubmit = syncTitleAndSubmit; 
window.submitFilterForm = submitFilterForm;
