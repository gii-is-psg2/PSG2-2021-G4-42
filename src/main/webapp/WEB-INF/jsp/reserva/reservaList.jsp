<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="reserva">
    <h2>Reservas</h2>

    <table id="ownersTable" class="table table-green table-striped">
        <thead>
        <tr>
        	<th>Habitación</th>
            <th style="width: 150px;">Fecha inicio</th>
            <th style="width: 200px;">Fecha fin</th>
            <th>Mascota</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${reservas}" var="reserva">
            <tr>
                <td>
                    <c:out value="${reserva.habitacion.numero}"/>
                </td>
                <td>
                    <c:out value="${reserva.fechaIni}"/>
                </td>
                <td>
                    <c:out value="${reserva.fechaFin}"/>
                </td>
                <td>
                    <c:out value="${reserva.pet}"/>
                </td>
        
                
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
