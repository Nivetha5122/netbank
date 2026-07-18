<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>NetBank — Beneficiaries</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body class="app-body">
<%@ include file="/WEB-INF/views/dashboard/navbar.jsp" %>
<div class="app-layout">
    <%@ include file="/WEB-INF/views/dashboard/sidebar.jsp" %>
    <main class="main-content">
        <div class="page-header">
            <h2 class="page-title">Beneficiaries</h2>
            <span class="page-subtitle">Manage your saved payees</span>
        </div>

        <c:if test="${not empty success}"><div class="alert alert-success">${success}</div></c:if>
        <c:if test="${not empty error}"><div class="alert alert-error">${error}</div></c:if>

        <!-- Add Beneficiary -->
        <div class="card form-card">
            <h3 class="card-title">Add New Beneficiary</h3>
            <form action="${pageContext.request.contextPath}/beneficiaries/add" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <div class="form-grid">
                    <div class="form-group">
                        <label class="form-label">Beneficiary Name</label>
                        <input type="text" name="name" class="form-input" placeholder="Full name" required/>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Account Number</label>
                        <input type="text" name="accountNumber" class="form-input" placeholder="Account number" required/>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Bank Name</label>
                        <input type="text" name="bankName" class="form-input" placeholder="e.g. SBI, HDFC, NetBank" required/>
                    </div>
                    <div class="form-group">
                        <label class="form-label">IFSC Code (optional)</label>
                        <input type="text" name="ifscCode" class="form-input" placeholder="e.g. SBIN0001234"/>
                    </div>
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Add Beneficiary</button>
                </div>
            </form>
        </div>

        <!-- Beneficiary List -->
        <div class="section-title" style="margin-top:2rem">Saved Beneficiaries</div>
        <div class="card">
            <c:choose>
                <c:when test="${empty beneficiaries}">
                    <p class="empty-state">No beneficiaries added yet.</p>
                </c:when>
                <c:otherwise>
                    <table class="data-table">
                        <thead>
                            <tr><th>Name</th><th>Account Number</th><th>Bank</th><th>IFSC</th><th>Added On</th><th>Action</th></tr>
                        </thead>
                        <tbody>
                            <c:forEach var="b" items="${beneficiaries}">
                                <tr>
                                    <td>${b.name}</td>
                                    <td><code class="ref-code">${b.accountNumber}</code></td>
                                    <td>${b.bankName}</td>
                                    <td>${b.ifscCode}</td>
                                    <td>${b.addedAt}</td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/beneficiaries/delete/${b.id}" method="post" style="display:inline">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                            <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Remove this beneficiary?')">Remove</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </main>
</div>
</body>
</html>
