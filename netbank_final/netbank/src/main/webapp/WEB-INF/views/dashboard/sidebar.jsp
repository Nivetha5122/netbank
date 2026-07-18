<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<aside class="sidebar">
    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/dashboard"
           class="sidebar-link ${pageContext.request.requestURI.contains('dashboard') ? 'active' : ''}">
            <span class="sidebar-icon">&#9810;</span> Dashboard
        </a>
        <a href="${pageContext.request.contextPath}/transfer"
           class="sidebar-link ${pageContext.request.requestURI.contains('transfer') ? 'active' : ''}">
            <span class="sidebar-icon">&#8644;</span> Fund Transfer
        </a>
        <a href="${pageContext.request.contextPath}/bills"
           class="sidebar-link ${pageContext.request.requestURI.contains('bills') ? 'active' : ''}">
            <span class="sidebar-icon">&#128203;</span> Bill Payments
        </a>
        <a href="${pageContext.request.contextPath}/beneficiaries"
           class="sidebar-link ${pageContext.request.requestURI.contains('beneficiaries') ? 'active' : ''}">
            <span class="sidebar-icon">&#128101;</span> Beneficiaries
        </a>
        <c:if test="${not empty accounts}">
            <a href="${pageContext.request.contextPath}/statement/mini?accountNumber=${accounts[0].accountNumber}"
               class="sidebar-link ${pageContext.request.requestURI.contains('statement') ? 'active' : ''}">
                <span class="sidebar-icon">&#128196;</span> Statements
            </a>
        </c:if>
        <c:if test="${empty accounts}">
            <a href="${pageContext.request.contextPath}/statement/mini"
               class="sidebar-link ${pageContext.request.requestURI.contains('statement') ? 'active' : ''}">
                <span class="sidebar-icon">&#128196;</span> Statements
            </a>
        </c:if>
    </nav>
    <div class="sidebar-footer">
        <p class="sidebar-help">Need help?</p>
        <p class="sidebar-support">support@netbank.com</p>
    </div>
</aside>
