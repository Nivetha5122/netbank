<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>NetBank — Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body class="app-body">

<%@ include file="/WEB-INF/views/dashboard/navbar.jsp" %>

<div class="app-layout">
    <%@ include file="/WEB-INF/views/dashboard/sidebar.jsp" %>

    <main class="main-content">
        <div class="page-header">
            <h2 class="page-title">Welcome, ${user.fullName}</h2>
            <span class="page-subtitle">Here's your financial overview</span>
        </div>

        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <!-- Account Cards -->
        <div class="accounts-grid">
            <c:forEach var="account" items="${accounts}">
                <div class="account-card">
                    <div class="account-card-header">
                        <span class="account-type-badge">${account.accountType}</span>
                        <span class="account-status ${account.status == 'ACTIVE' ? 'status-active' : 'status-inactive'}">${account.status}</span>
                    </div>
                    <div class="account-number">${account.accountNumber}</div>
                    <div class="account-balance">
                        <span class="balance-label">Available Balance</span>
                        <span class="balance-amount">
                            ₹<fmt:formatNumber value="${account.balance}" pattern="#,##,##0.00"/>
                        </span>
                    </div>
                    <div class="account-actions">
                        <a href="${pageContext.request.contextPath}/statement/mini?accountNumber=${account.accountNumber}" class="btn btn-sm btn-outline">Mini Statement</a>
                        <a href="${pageContext.request.contextPath}/statement/full?accountNumber=${account.accountNumber}" class="btn btn-sm btn-outline">Full Statement</a>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- Quick Actions -->
        <div class="section-title">Quick Actions</div>
        <div class="quick-actions">
            <a href="${pageContext.request.contextPath}/transfer" class="action-card">
                <div class="action-icon action-icon-blue">&#8644;</div>
                <span>Fund Transfer</span>
            </a>
            <a href="${pageContext.request.contextPath}/bills" class="action-card">
                <div class="action-icon action-icon-green">&#128203;</div>
                <span>Pay Bills</span>
            </a>
            <a href="${pageContext.request.contextPath}/beneficiaries" class="action-card">
                <div class="action-icon action-icon-purple">&#128101;</div>
                <span>Beneficiaries</span>
            </a>
            <c:if test="${not empty accounts}">
                <a href="${pageContext.request.contextPath}/statement/full?accountNumber=${accounts[0].accountNumber}" class="action-card">
                    <div class="action-icon action-icon-orange">&#128196;</div>
                    <span>Statement</span>
                </a>
            </c:if>
        </div>

        <!-- Recent Transactions -->
        <div class="section-title" style="margin-top:2rem">Recent Transactions</div>
        <div class="card">
            <c:choose>
                <c:when test="${empty recentTransactions}">
                    <p class="empty-state">No transactions yet. Start by making a transfer.</p>
                </c:when>
                <c:otherwise>
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Reference</th>
                                <th>Type</th>
                                <th>Description</th>
                                <th>Amount</th>
                                <th>Date</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="txn" items="${recentTransactions}">
                                <tr>
                                    <td><code class="ref-code">${txn.referenceNumber}</code></td>
                                    <td><span class="badge badge-${txn.type == 'CREDIT' ? 'green' : 'red'}">${txn.type}</span></td>
                                    <td>${txn.description}</td>
                                    <td class="amount ${txn.type == 'CREDIT' ? 'credit' : 'debit'}">
                                        ₹<fmt:formatNumber value="${txn.amount}" pattern="#,##,##0.00"/>
                                    </td>
                                    <td><fmt:formatDate value="${txn.createdAt}" pattern="dd MMM yyyy HH:mm" type="both"/></td>
                                    <td><span class="badge badge-green">${txn.status}</span></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </main>
</div>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
