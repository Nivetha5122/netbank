<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>NetBank — Fund Transfer</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body class="app-body">
<%@ include file="/WEB-INF/views/dashboard/navbar.jsp" %>
<div class="app-layout">
    <%@ include file="/WEB-INF/views/dashboard/sidebar.jsp" %>
    <main class="main-content">
        <div class="page-header">
            <h2 class="page-title">Fund Transfer</h2>
            <span class="page-subtitle">Transfer money securely</span>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <div class="card form-card">
            <form:form action="${pageContext.request.contextPath}/transfer" method="post"
                       modelAttribute="transferRequest">
                <div class="form-grid">
                    <div class="form-group">
                        <label class="form-label">From Account</label>
                        <form:select path="sourceAccountNumber" cssClass="form-input">
                            <form:option value="">— Select Account —</form:option>
                            <c:forEach var="acc" items="${accounts}">
                                <form:option value="${acc.accountNumber}">
                                    ${acc.accountNumber} (₹<fmt:formatNumber value="${acc.balance}" pattern="#,##,##0.00"/>)
                                </form:option>
                            </c:forEach>
                        </form:select>
                        <form:errors path="sourceAccountNumber" cssClass="form-error"/>
                    </div>

                    <div class="form-group">
                        <label class="form-label">To Account Number</label>
                        <form:input path="destinationAccountNumber" cssClass="form-input"
                                    placeholder="Recipient account number" list="beneficiary-list"/>
                        <datalist id="beneficiary-list">
                            <c:forEach var="b" items="${beneficiaries}">
                                <option value="${b.accountNumber}">${b.name} — ${b.bankName}</option>
                            </c:forEach>
                        </datalist>
                        <form:errors path="destinationAccountNumber" cssClass="form-error"/>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Amount (₹)</label>
                        <form:input path="amount" type="number" cssClass="form-input"
                                    placeholder="0.00" step="0.01" min="1"/>
                        <form:errors path="amount" cssClass="form-error"/>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Remarks (optional)</label>
                        <form:input path="description" cssClass="form-input" placeholder="e.g. Rent, Tuition fee"/>
                        <form:errors path="description" cssClass="form-error"/>
                    </div>
                </div>

                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-ghost">Cancel</a>
                    <button type="submit" class="btn btn-primary">Transfer Now</button>
                </div>
            </form:form>
        </div>

        <!-- Beneficiary quick-select -->
        <c:if test="${not empty beneficiaries}">
            <div class="section-title" style="margin-top:2rem">Saved Beneficiaries</div>
            <div class="beneficiary-chips">
                <c:forEach var="b" items="${beneficiaries}">
                    <div class="beneficiary-chip" onclick="fillBeneficiary('${b.accountNumber}')">
                        <span class="chip-name">${b.name}</span>
                        <span class="chip-acc">${b.accountNumber}</span>
                    </div>
                </c:forEach>
            </div>
        </c:if>
    </main>
</div>
<script>
    function fillBeneficiary(accNum) {
        document.getElementById('destinationAccountNumber').value = accNum;
    }
</script>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
