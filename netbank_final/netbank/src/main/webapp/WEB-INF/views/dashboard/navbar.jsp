<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav class="navbar">
    <div class="navbar-brand">
        <div class="brand-logo-sm">NB</div>
        <span class="brand-name-sm">NetBank</span>
    </div>
    <div class="navbar-right">
        <span class="nav-user">&#128100; ${user.fullName}</span>
        <form action="${pageContext.request.contextPath}/auth/logout" method="post" style="display:inline">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-sm btn-ghost">Sign Out</button>
        </form>
    </div>
</nav>
