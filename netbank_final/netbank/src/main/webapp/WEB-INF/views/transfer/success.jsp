<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>NetBank — Transfer Successful</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body class="app-body">
<%@ include file="/WEB-INF/views/dashboard/navbar.jsp" %>
<div class="app-layout">
    <%@ include file="/WEB-INF/views/dashboard/sidebar.jsp" %>
    <main class="main-content">
        <div class="success-container">
            <div class="success-icon">&#10003;</div>
            <h2 class="success-title">Transfer Successful!</h2>
            <p class="success-subtitle">Your funds have been transferred securely.</p>
            <div class="success-ref">
                <span class="ref-label">Reference Number</span>
                <code class="ref-value">${referenceNumber}</code>
            </div>
            <div class="success-actions">
                <a href="${pageContext.request.contextPath}/transfer" class="btn btn-outline">New Transfer</a>
                <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">Back to Dashboard</a>
            </div>
        </div>
    </main>
</div>
</body>
</html>
