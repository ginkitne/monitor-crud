<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${isNew ? 'New' : 'Edit'} Monitoring Target</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<main>
    <h1>${isNew ? 'Add New Monitoring Target' : 'Edit Monitoring Target'}</h1>

    <c:if test="${not empty error}">
        <div class="flash error">${fn:escapeXml(error)}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/targets/save">

        <c:if test="${not isNew}">
            <input type="hidden" name="id" value="${target.id}">
        </c:if>

        <div class="field">
            <label for="url">Target URL <span class="required">*</span></label>
            <input type="text" id="url" name="url"
                   value="${fn:escapeXml(target.url)}"
                   placeholder="https://example.com/page" required>
        </div>

        <div class="field">
            <label for="checkInterval">Check Interval <span class="required">*</span></label>
            <select id="checkInterval" name="checkInterval" required>
                <option value="">-- Select --</option>
                <option value="hourly" ${target.checkInterval == 'hourly' ? 'selected' : ''}>Hourly</option>
                <option value="daily"  ${target.checkInterval == 'daily'  ? 'selected' : ''}>Daily</option>
                <option value="weekly" ${target.checkInterval == 'weekly' ? 'selected' : ''}>Weekly</option>
            </select>
        </div>

        <div class="field">
            <label for="cssSelector">CSS Selector (optional)</label>
            <input type="text" id="cssSelector" name="cssSelector"
                   value="${fn:escapeXml(target.cssSelector)}"
                   placeholder=".content, #main">
        </div>

        <div class="field checkbox">
            <label>
                <input type="checkbox" name="active" ${target.active ? 'checked' : ''}>
                Enable periodic checks
            </label>
        </div>

        <div class="actions">
            <a href="${pageContext.request.contextPath}/targets" class="button secondary">Cancel</a>
            <button type="submit" class="button">Save</button>
        </div>
    </form>
</main>
</body>
</html>
