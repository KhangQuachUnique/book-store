<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="admin-container">
    <div class="admin-header" style="display:flex;justify-content:space-between;align-items:center;margin-bottom:16px;">
        <h2 style="margin:0;"><c:choose><c:when test="${mode == 'edit'}">Edit Promotion</c:when><c:otherwise>New Promotion</c:otherwise></c:choose></h2>
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/promotions">Back</a>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-error">${error}</div>
    </c:if>

    <div class="card" style="padding:16px;">
        <form action="${pageContext.request.contextPath}/admin/promotions" method="post" class="form-grid" style="display:grid;grid-template-columns:1fr 1fr;gap:16px;align-items:start;">
            <c:if test="${mode == 'edit'}">
                <input type="hidden" name="action" value="update"/>
                <input type="hidden" name="id" value="${promotion.id}"/>
            </c:if>
            <c:if test="${mode != 'edit'}">
                <input type="hidden" name="action" value="create"/>
            </c:if>

            <div class="form-group">
                <label for="code">Code</label>
                <input type="text" id="code" name="code" class="input" placeholder="e.g. BOOK20"
                       value="${not empty backfill_code ? backfill_code : (promotion.code)}"
                       required />
            </div>

            <div class="form-group">
                <label for="discount">Discount (%)</label>
                <input type="number" id="discount" name="discount" class="input" step="0.1" min="0.1" max="100"
                       value="${not empty backfill_discount ? backfill_discount : promotion.discount}"
                       required />
            </div>

            <div class="form-group" style="grid-column: 1 / -1;">
                <label for="expireAt">Expire At</label>
                <input type="datetime-local" id="expireAt" name="expireAt" class="input"
                       value="${backfill_expireAt}" />
                <c:if test="${empty backfill_expireAt && promotion != null}">
                    <script>
                        (function(){
                            try {
                                var ms = ${promotion.expireAtDate.time};
                                if (ms) {
                                    var d = new Date(ms);
                                    var pad = n => String(n).padStart(2,'0');
                                    var v = d.getFullYear()+"-"+pad(d.getMonth()+1)+"-"+pad(d.getDate())+"T"+pad(d.getHours())+":"+pad(d.getMinutes());
                                    document.getElementById('expireAt').value = v;
                                }
                            } catch(e) {}
                        })();
                    </script>
                </c:if>
            </div>

            <div style="grid-column: 1 / -1; display:flex; gap:8px; justify-content:flex-end;">
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/promotions">Cancel</a>
                <button type="submit" class="btn btn-primary"><c:choose><c:when test="${mode == 'edit'}">Update</c:when><c:otherwise>Create</c:otherwise></c:choose></button>
            </div>
        </form>
    </div>
</div>

