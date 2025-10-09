// Category Book JavaScript functionality
let includeCategories = [];
let excludeCategories = [];

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
}

function submitFilterForm() {
    // Update hidden inputs với các categories được chọn và loại trừ
    document.getElementById('includeCategories').value = includeCategories.join(',');
    document.getElementById('excludeCategories').value = excludeCategories.join(',');
    
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
