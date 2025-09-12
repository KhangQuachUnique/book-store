document.addEventListener('DOMContentLoaded', function() {
    const sidebarToggle = document.getElementById('sidebarToggle');
    const hamburgerMenu = document.getElementById('hamburgerMenu');
    const sidebar = document.querySelector('.sidebar');
    const mainContent = document.querySelector('.main-content');

    // Function to toggle sidebar
    function toggleSidebar() {
        sidebar.classList.toggle('collapsed');
        mainContent.classList.toggle('expanded');

        // Store the sidebar state in localStorage
        const isSidebarCollapsed = sidebar.classList.contains('collapsed');
        localStorage.setItem('sidebarCollapsed', isSidebarCollapsed);
    }

    // Add click event to sidebar toggle
    sidebarToggle.addEventListener('click', toggleSidebar);

    // Add click event to hamburger menu
    hamburgerMenu.addEventListener('click', toggleSidebar);

    // Check localStorage on page load to restore sidebar state
    const isSidebarCollapsed = localStorage.getItem('sidebarCollapsed') === 'true';
    if (isSidebarCollapsed) {
        sidebar.classList.add('collapsed');
        mainContent.classList.add('expanded');
    }
});