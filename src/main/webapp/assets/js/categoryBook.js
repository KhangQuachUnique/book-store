// Category Book JavaScript functionality
let includeCategories = [];

// Initialize from existing values if available
function initializeCategories() {
    // Get values from JSP variables (will be set by the JSP page)
    if (typeof includeCategories_jsp !== 'undefined' && includeCategories_jsp !== '') {
        includeCategories = includeCategories_jsp.split(',').map(id => parseInt(id));
    }
    
    // Áp dụng trạng thái cho các category đã được chọn
    includeCategories.forEach(categoryId => {
        const element = document.querySelector(`[data-id="${categoryId}"]`);
        if (element) {
            element.classList.add('include');
        }
    });
}

function toggleCategoryTable() {
    const table = document.getElementById('categoryTable');
    table.classList.toggle('show');
}

function toggleCategory(element, categoryId) {
    if (element.classList.contains('include')) {
        // Nếu đang được chọn (viền xanh), thì bỏ chọn (về mặc định)
        element.classList.remove('include');
        includeCategories = includeCategories.filter(id => id !== categoryId);
    } else {
        // Nếu chưa được chọn, thì chọn (viền xanh)
        element.classList.add('include');
        if (!includeCategories.includes(categoryId)) {
            includeCategories.push(categoryId);
        }
    }
}

function submitFilterForm() {
    // Update hidden input với các categories được chọn
    document.getElementById('includeCategories').value = includeCategories.join(',');
    
    // Set action type là "categories" (ưu tiên category filter)
    document.getElementById('searchAction').value = 'categories';
    
    // Submit form - form sẽ tự động gửi tất cả input values
    document.querySelector('.search-form').submit();
}

function setSearchAction(actionType) {
    // Set action type cho title search
    document.getElementById('searchAction').value = actionType;
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
});
