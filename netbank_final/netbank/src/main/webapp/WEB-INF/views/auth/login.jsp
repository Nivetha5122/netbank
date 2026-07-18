<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>NetBank — Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body class="auth-body">
<div class="auth-container">
    <div class="auth-card">
        <div class="auth-brand">
            <div class="brand-logo">NB</div>
            <h1 class="brand-name">NetBank</h1>
            <p class="brand-tagline">Secure Internet Banking</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>
        <c:if test="${not empty message}">
            <div class="alert alert-success">${message}</div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/auth/login" method="post" class="auth-form">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div class="form-group">
                <label class="form-label">Email Address</label>
                <input type="email" name="email" class="form-input" placeholder="you@email.com" required autofocus/>
            </div>
            <div class="form-group">
                <label class="form-label">Password</label>
                <input type="password" name="password" class="form-input" placeholder="••••••••" required/>
            </div>
            <button type="submit" class="btn btn-primary btn-full">Sign In</button>
        </form>

        <p class="auth-footer">
            Don't have an account?
            <a href="${pageContext.request.contextPath}/auth/register">Open Account</a>
        </p>
    </div>
</div>
</body>
</html>
