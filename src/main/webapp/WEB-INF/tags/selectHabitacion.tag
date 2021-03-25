<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="name" required="true" rtexprvalue="true"
              description="Name of corresponding property in bean object" %>
<%@ attribute name="label" required="true" rtexprvalue="true"
              description="Label appears in red color if input is considered as invalid after submission" %>
<%@ attribute name="items" required="true" rtexprvalue="true" type="java.util.List"
              description="" %>
<%@ attribute name="fechaIni" required="true" rtexprvalue="true" type="java.time.LocalDate"
              description="" %>
<%@ attribute name="fechaFin" required="true" rtexprvalue="true" type="java.time.LocalDate"
              description="" %>

<spring:bind path="${name}">
    <c:set var="cssGroup" value="form-group ${status.error ? 'has-error' : '' }"/>
    <c:set var="valid" value="${not status.error and not empty status.actualValue}"/>
    <div class="${cssGroup}">
        <label class="col-sm-2 control-label">${label}</label>

        <div class="col-sm-10">
            	<form:select class="form-control" path="${name}" size="5">
            	<c:forEach var="item" items="${items}">
            		<c:if test="${item.estaOcupada(fechaIni,fechaFin)}">
            			<form:option value="${item}" disabled="true">Habitación nº ${item.numero} - Ocupada</form:option>
            		</c:if>
            		<c:if test="${not item.estaOcupada(fechaIni,fechaFin)}">
            			<form:option value="${item}"/>
            		</c:if>
            	</c:forEach>
            	</form:select>
            <c:if test="${status.error}">
                <span class="form-control-feedback" aria-hidden="true"></span>
                <span class="help-inline">${status.errorMessage}</span>
            </c:if>
        </div>
    </div>
</spring:bind>