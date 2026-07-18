<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>NetBank — Error</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body class="auth-body">
<div class="auth-container">
    <div class="auth-card" style="text-align:center">
        <div class="error-icon">&#9888;</div>
        <h2 class="error-heading">${not empty errorTitle ? errorTitle : 'Something went wrong'}</h2>
        <p class="error-msg">${not empty errorMessage ? errorMessage : 'An unexpected error occurred. Please try again.'}</p>
        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary" style="margin-top:1.5rem">Back to Dashboard</a>
    </div>
</div>
</body>
</html>
