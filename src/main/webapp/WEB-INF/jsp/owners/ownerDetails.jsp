<%@ page session="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="owners">
<style>
.isDisabled {
  color: currentColor;
  cursor: not-allowed;
  opacity: 0.5;
  text-decoration: none;
}
</style>

    <h2>Información del dueño</h2>


    <table class="table table-striped">
        <tr>
            <th>Nombre</th>
            <td><b><c:out value="${owner.firstName} ${owner.lastName}"/></b></td>
        </tr>
        <tr>
            <th>Dirección</th>
            <td><c:out value="${owner.address}"/></td>
        </tr>
        <tr>
            <th>Ciudad</th>
            <td><c:out value="${owner.city}"/></td>
        </tr>
        <tr>
            <th>Teléfono</th>
            <td><c:out value="${owner.telephone}"/></td>
        </tr>
    </table>

    <spring:url value="{ownerId}/edit" var="editUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>

    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar Dueño</a>
   
    <sec:authorize access="hasAuthority('admin')"> 
    <spring:url value="{ownerId}/delete" var="deleteUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Eliminar Dueño</a>
    </sec:authorize>


    <spring:url value="{ownerId}/pets/new" var="addUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Agregar nueva mascota</a>

    <br/>
    <br/>
    <br/>
    <h2>Mascotas y visitas</h2>

    <table class="table table-striped">
        <c:forEach var="pet" items="${owner.pets}">

            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt>Nombre</dt>
                        <dd><c:out value="${pet.name}"/></dd>
                        <dt>Cumpleaños</dt>
                        <dd><petclinic:localDate date="${pet.birthDate}" pattern="yyyy-MM-dd"/></dd>
                        <dt>Tipo</dt>
                        <dd><c:out value="${pet.type.name}"/></dd>
                    </dl>
                </td>
                <td valign="top">
                    <table class="table-condensed">
                        <thead>
                        <tr>
                            <th>Fecha de visita</th>
                            <th>Descripción</th>
                        </tr>
                        </thead>
                        <c:forEach var="visit" items="${pet.visits}">
                            <tr>
                                <td><petclinic:localDate date="${visit.date}" pattern="yyyy-MM-dd"/></td>
                                <td><c:out value="${visit.description}"/></td>
                                <td>
                                <spring:url value="/visits/{visitId}/delete" var="visitUrl">
                                    <spring:param name="visitId" value="${visit.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(visitUrl)}">Cancelar visita</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/edit" var="petUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(petUrl)}">Editar mascota</a>
                            </td>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/delete" var="petUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(petUrl)}">Borrar mascota</a>
                            </td>
                            <td>
                                <spring:url value="/visits/new" var="visitUrl">
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(visitUrl)}">Añadir visita</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>

        </c:forEach>
    </table>
     <h2>Reservas</h2>
    <table id="reservasTable" class="table table-green table-striped">
    	<thead>
        <tr>
        	<th>Habitación</th>
            <th>Fecha inicio</th>
            <th>Fecha fin</th>
            <th>Mascota</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${reservas}" var="reserva">
            <tr>
                <td>
                    Habitación nº<c:out value="${reserva.habitacion.numero}"/>
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
                <td>
                    <spring:url value="/reserva/delete/{reservaId}" var="reservaUrl">
                      	<spring:param name="reservaId" value="${reserva.id}"/>
                  	</spring:url>
                	<a href="${fn:escapeXml(reservaUrl)}">Cancelar reserva</a>
                </td>
        
                
            </tr>
        </c:forEach>
        </tbody>
    </table>

</petclinic:layout>
