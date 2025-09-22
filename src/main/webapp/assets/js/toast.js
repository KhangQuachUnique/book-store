// toast.js
let toastIndex = 0;

function showToast(message, type = 'success', duration = 3000) {
    // Create toast element
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;

    // Calculate position for stacking
    const existingToasts = document.querySelectorAll('.toast');
    const topOffset = 100 + (existingToasts.length * 80);

    toast.style.top = `${topOffset}px`;

    // Toast content
    toast.innerHTML = `
        <div class="toast-content">
            <span class="toast-icon">${getToastIcon(type)}</span>
            <span class="toast-message">${message}</span>
            <button class="toast-close">&times;</button>
        </div>
    `;

    // Add to body
    document.body.appendChild(toast);

    // Show toast with animation
    requestAnimationFrame(() => {
        toast.classList.add('toast-show');
    });

    // Auto hide and reposition others
    const hideToast = () => {
        toast.classList.remove('toast-show');
        setTimeout(() => {
            if (toast.parentNode) {
                toast.parentNode.removeChild(toast);
                repositionToasts(); // Reposition remaining toasts
            }
        }, 300);
    };

    // Close button event
    toast.querySelector('.toast-close').addEventListener('click', hideToast);

    // Auto hide after duration
    if (duration > 0) {
        setTimeout(hideToast, duration);
    }

    return toast;
}

function repositionToasts() {
    const toasts = document.querySelectorAll('.toast');
    toasts.forEach((toast, index) => {
        const topOffset = 100 + (index * 80);
        toast.style.top = `${topOffset}px`;
    });
}

function getToastIcon(type) {
    const icons = {
        success: '✓',
        error: '✕',
        warning: '⚠',
        info: 'ℹ'
    };
    return icons[type] || icons.info;
}

// Make it globally available
window.showToast = showToast;
