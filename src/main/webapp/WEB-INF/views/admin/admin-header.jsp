<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="/BookieCake/admin">BookieCake Admin</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#adminNavbar">
            <span class="navbar-toggler-icon"></span>
        </button>
        
        <div class="collapse navbar-collapse" id="adminNavbar">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link" href="/BookieCake/admin/orders">Orders</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/BookieCake/admin/books">Books</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/BookieCake/admin/categories">Categories</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/BookieCake/admin/users">Users</a>
                </li>
            </ul>
            
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" href="/BookieCake">Back to Site</a>
                </li>
                <li class="nav-item">
                    <form action="/BookieCake/logout" method="post" class="d-inline">
                        <button type="submit" class="btn btn-link nav-link">Logout</button>
                    </form>
                </li>
            </ul>
        </div>
    </div>
</nav>