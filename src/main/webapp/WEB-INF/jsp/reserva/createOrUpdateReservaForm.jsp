<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">
    <jsp:body>
        <h2>
            <c:if test="${reserva['new']}">Nueva </c:if> Reserva
        </h2>
        <form:form action="new/fechas" modelAttribute="reserva" class="form-horizontal">
            <div class="form-group">
            	<label class="col-sm-4 control-label">Desde </label>
            	<div class="col-sm-2">
            	<form:input readonly="true" autocomplete="off" class="form-control" path="fechaIni" />
            	</div>
                <label class="col-sm-1 control-label"> Hasta </label>
                <div class="col-sm-2">
                <form:input readonly="true" autocomplete="off" class="form-control col-4" path="fechaFin" />
                </div>
                <div class="col-sm-offset-2 col-sm-1">
                <a class="btn btn-default col-sm-12" href="/reserva/new"><span class="glyphicon glyphicon-pencil"></span></a>
                </div>
            </div>
            <div class="col-sm-12" style="height:5vh; border-top: 1px solid black"></div>
            <div class="form-group has-feedback col-sm-4">
                <petclinic:select label="Mascota" name="pet" items="${pets}"/>
            </div>
            <div class="form-group has-feedback col-sm-8">
                <petclinic:selectField label="Habitacion" name="pet" names="${pets}" size="5"/>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-10 col-sm-2">
                	<button class="btn btn-default col-sm-12" type="submit">Reservar</button>
                </div>
            </div>
        </form:form>
        <c:if test="${!pet['new']}">
        </c:if>
    </jsp:body>
</petclinic:layout>
