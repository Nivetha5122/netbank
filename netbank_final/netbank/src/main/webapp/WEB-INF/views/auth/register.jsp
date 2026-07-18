<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>NetBank — Open Account</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body class="auth-body">
<div class="auth-container">
    <div class="auth-card" style="max-width:480px">
        <div class="auth-brand">
            <div class="brand-logo">NB</div>
            <h1 class="brand-name">Open Account</h1>
            <p class="brand-tagline">Start banking with NetBank</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <form:form action="${pageContext.request.contextPath}/auth/register" method="post"
                   modelAttribute="registerRequest" class="auth-form">
            <div class="form-group">
                <label class="form-label">Full Name</label>
                <form:input path="fullName" cssClass="form-input" placeholder="Your full name"/>
                <form:errors path="fullName" cssClass="form-error"/>
            </div>
            <div class="form-group">
                <label class="form-label">Email Address</label>
                <form:input path="email" type="email" cssClass="form-input" placeholder="you@email.com"/>
                <form:errors path="email" cssClass="form-error"/>
            </div>
            <div class="form-group">
                <label class="form-label">Phone Number</label>
                <form:input path="phone" cssClass="form-input" placeholder="10-digit mobile number"/>
                <form:errors path="phone" cssClass="form-error"/>
            </div>
            <div class="form-group">
                <label class="form-label">Account Type</label>
                <form:select path="accountType" cssClass="form-input">
                    <form:option value="SAVINGS">Savings Account</form:option>
                    <form:option value="CURRENT">Current Account</form:option>
                    <form:option value="SALARY">Salary Account</form:option>
                </form:select>
            </div>
            <div class="form-group">
                <label class="form-label">Password</label>
                <form:input path="password" type="password" cssClass="form-input" placeholder="Min. 8 characters"/>
                <form:errors path="password" cssClass="form-error"/>
            </div>
            <div class="form-group">
                <label class="form-label">Confirm Password</label>
                <form:input path="confirmPassword" type="password" cssClass="form-input" placeholder="Repeat password"/>
                <form:errors path="confirmPassword" cssClass="form-error"/>
            </div>
            <button type="submit" class="btn btn-primary btn-full">Open Account</button>
        </form:form>

        <p class="auth-footer">
            Already have an account?
            <a href="${pageContext.request.contextPath}/auth/login">Sign In</a>
        </p>
    </div>
</div>
</body>
</html>
