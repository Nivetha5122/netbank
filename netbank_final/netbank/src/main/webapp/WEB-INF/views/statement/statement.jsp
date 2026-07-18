<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>NetBank — Statement</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body class="app-body">
<%@ include file="/WEB-INF/views/dashboard/navbar.jsp" %>
<div class="app-layout">
    <%@ include file="/WEB-INF/views/dashboard/sidebar.jsp" %>
    <main class="main-content">
        <div class="page-header">
            <div>
                <h2 class="page-title">${isMini ? 'Mini Statement' : 'Full Statement'}</h2>
                <span class="page-subtitle">Account: ${account.accountNumber}</span>
            </div>
            <div class="page-header-actions">
                <c:if test="${isMini}">
                    <a href="${pageContext.request.contextPath}/statement/full?accountNumber=${account.accountNumber}" class="btn btn-outline">Full Statement</a>
                </c:if>
                <c:if test="${not isMini}">
                    <a href="${pageContext.request.contextPath}/statement/mini?accountNumber=${account.accountNumber}" class="btn btn-outline">Mini Statement</a>
                </c:if>
            </div>
        </div>

        <div class="account-summary-bar">
            <div class="summary-item">
                <span class="summary-label">Account Number</span>
                <span class="summary-value">${account.accountNumber}</span>
            </div>
            <div class="summary-item">
                <span class="summary-label">Type</span>
                <span class="summary-value">${account.accountType}</span>
            </div>
            <div class="summary-item">
                <span class="summary-label">Balance</span>
                <span class="summary-value balance-highlight">Rs.<fmt:formatNumber value="${account.balance}" pattern="#,##,##0.00"/></span>
            </div>
        </div>

        <div class="account-switcher">
            <c:forEach var="acc" items="${accounts}">
                <a href="${pageContext.request.contextPath}/statement/${isMini ? 'mini' : 'full'}?accountNumber=${acc.accountNumber}"
                   class="acc-switch-btn ${acc.accountNumber == account.accountNumber ? 'active' : ''}">
                    ${acc.accountNumber}
                </a>
            </c:forEach>
        </div>

        <div class="card">
            <c:choose>
                <c:when test="${isMini}">
                    <c:choose>
                        <c:when test="${empty transactions}">
                            <p class="empty-state">No transactions found.</p>
                        </c:when>
                        <c:otherwise>
                            <table class="data-table">
                                <thead><tr><th>Reference</th><th>Type</th><th>Description</th><th>Amount</th><th>Balance After</th><th>Date</th></tr></thead>
                                <tbody>
                                <c:forEach var="txn" items="${transactions}">
                                    <tr>
                                        <td><code class="ref-code">${txn.referenceNumber}</code></td>
                                        <td><span class="badge badge-${txn.type == 'CREDIT' ? 'green' : 'red'}">${txn.type}</span></td>
                                        <td>${txn.description}</td>
                                        <td class="amount ${txn.type == 'CREDIT' ? 'credit' : 'debit'}">Rs.<fmt:formatNumber value="${txn.amount}" pattern="#,##,##0.00"/></td>
                                        <td>Rs.<fmt:formatNumber value="${txn.balanceAfter}" pattern="#,##,##0.00"/></td>
                                        <td>${txn.createdAt}</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <c:choose>
                        <c:when test="${transactionPage.totalElements == 0}">
                            <p class="empty-state">No transactions found.</p>
                        </c:when>
                        <c:otherwise>
                            <div class="table-meta">Showing ${transactionPage.numberOfElements} of ${transactionPage.totalElements} transactions</div>
                            <table class="data-table">
                                <thead><tr><th>Reference</th><th>Type</th><th>Description</th><th>Amount</th><th>Balance After</th><th>Status</th><th>Date</th></tr></thead>
                                <tbody>
                                <c:forEach var="txn" items="${transactionPage.content}">
                                    <tr>
                                        <td><code class="ref-code">${txn.referenceNumber}</code></td>
                                        <td><span class="badge badge-${txn.type == 'CREDIT' ? 'green' : 'red'}">${txn.type}</span></td>
                                        <td>${txn.description}</td>
                                        <td class="amount ${txn.type == 'CREDIT' ? 'credit' : 'debit'}">Rs.<fmt:formatNumber value="${txn.amount}" pattern="#,##,##0.00"/></td>
                                        <td>Rs.<fmt:formatNumber value="${txn.balanceAfter}" pattern="#,##,##0.00"/></td>
                                        <td><span class="badge badge-green">${txn.status}</span></td>
                                        <td>${txn.createdAt}</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                            <div class="pagination">
                                <c:if test="${currentPage > 0}">
                                    <a href="?accountNumber=${account.accountNumber}&page=${currentPage - 1}" class="page-btn">Prev</a>
                                </c:if>
                                <c:forEach begin="0" end="${transactionPage.totalPages - 1}" var="i">
                                    <a href="?accountNumber=${account.accountNumber}&page=${i}" class="page-btn ${i == currentPage ? 'active' : ''}">${i + 1}</a>
                                </c:forEach>
                                <c:if test="${currentPage < transactionPage.totalPages - 1}">
                                    <a href="?accountNumber=${account.accountNumber}&page=${currentPage + 1}" class="page-btn">Next</a>
                                </c:if>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>
        </div>
    </main>
</div>
</body>
</html>
