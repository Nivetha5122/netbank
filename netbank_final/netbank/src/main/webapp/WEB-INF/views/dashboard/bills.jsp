<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>NetBank — Bill Payments</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body class="app-body">
<%@ include file="/WEB-INF/views/dashboard/navbar.jsp" %>
<div class="app-layout">
    <%@ include file="/WEB-INF/views/dashboard/sidebar.jsp" %>
    <main class="main-content">
        <div class="page-header">
            <h2 class="page-title">Bill Payments</h2>
            <span class="page-subtitle">Pay utilities and services</span>
        </div>

        <c:if test="${not empty success}"><div class="alert alert-success">${success}</div></c:if>
        <c:if test="${not empty error}"><div class="alert alert-error">${error}</div></c:if>

        <div class="card form-card">
            <form:form action="${pageContext.request.contextPath}/bills/pay" method="post" modelAttribute="billPaymentRequest">
                <div class="form-grid">
                    <div class="form-group">
                        <label class="form-label">Pay From Account</label>
                        <form:select path="accountNumber" cssClass="form-input">
                            <form:option value="">— Select Account —</form:option>
                            <c:forEach var="acc" items="${accounts}">
                                <form:option value="${acc.accountNumber}">
                                    ${acc.accountNumber} (Rs.<fmt:formatNumber value="${acc.balance}" pattern="#,##,##0.00"/>)
                                </form:option>
                            </c:forEach>
                        </form:select>
                        <form:errors path="accountNumber" cssClass="form-error"/>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Bill Category</label>
                        <form:select path="billType" cssClass="form-input">
                            <form:option value="">— Select Category —</form:option>
                            <c:forEach var="bt" items="${billTypes}">
                                <form:option value="${bt}">${bt}</form:option>
                            </c:forEach>
                        </form:select>
                        <form:errors path="billType" cssClass="form-error"/>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Biller / Service Provider</label>
                        <form:input path="billerName" cssClass="form-input" placeholder="e.g. BESCOM, Airtel, Jio"/>
                        <form:errors path="billerName" cssClass="form-error"/>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Consumer / Account Number</label>
                        <form:input path="consumerNumber" cssClass="form-input" placeholder="Your consumer ID"/>
                        <form:errors path="consumerNumber" cssClass="form-error"/>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Amount (Rs.)</label>
                        <form:input path="amount" type="number" cssClass="form-input" placeholder="0.00" step="0.01" min="1"/>
                        <form:errors path="amount" cssClass="form-error"/>
                    </div>
                </div>
                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-ghost">Cancel</a>
                    <button type="submit" class="btn btn-primary">Pay Bill</button>
                </div>
            </form:form>
        </div>

        <!-- Bill type quick select -->
        <div class="section-title" style="margin-top:2rem">Quick Select</div>
        <div class="bill-categories">
            <c:forEach var="bt" items="${billTypes}">
                <div class="bill-cat-card" onclick="document.getElementById('billType').value='${bt}'">
                    <span class="bill-cat-name">${bt}</span>
                </div>
            </c:forEach>
        </div>
    </main>
</div>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
