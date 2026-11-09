<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Monitoring Targets - Website Change Detector</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<main>
    <h1>Monitoring Targets</h1>

    <c:if test="${not empty flash}">
        <div class="flash">${fn:escapeXml(flash)}</div>
    </c:if>

    <p>
        <a href="${pageContext.request.contextPath}/targets/new" class="button">
            + Add Target
        </a>
    </p>

    <c:choose>
        <c:when test="${empty targets}">
            <p class="empty">No monitoring targets configured yet.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>URL</th>
                    <th>Interval</th>
                    <th>CSS Selector</th>
                    <th>Status</th>
                    <th>Created</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="t" items="${targets}">
                    <tr>
                        <td>${t.id}</td>
                        <td class="url">${fn:escapeXml(t.url)}</td>
                        <td>${fn:escapeXml(t.checkInterval)}</td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty t.cssSelector}">
                                    <code>${fn:escapeXml(t.cssSelector)}</code>
                                </c:when>
                                <c:otherwise>
                                    <em>full page</em>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${t.active}">
                                    <span class="badge active">Active</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge inactive">Inactive</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>${t.createdAt}</td>
                        <td class="actions">
                            <a href="${pageContext.request.contextPath}/targets/edit?id=${t.id}">Edit</a>
                            <form method="post"
                                  action="${pageContext.request.contextPath}/targets/delete"
                                  onsubmit="return confirm('Delete this target?');"
                                  class="inline">
                                <input type="hidden" name="id" value="${t.id}">
                                <button type="submit" class="link-button">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
