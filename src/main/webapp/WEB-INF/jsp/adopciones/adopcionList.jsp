<%@ page session="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="adopciones">
    <h2>Adopciones</h2>

    <table id="adopcionTable" class="table table-green table-striped">
        <thead>
        <tr>
            <th>Antiguo Dueño</th>
            <th>Mascota</th>
            <th>Tipo Mascota</th>
            <th>Cumple Mascota</th>
            <th>Puesta en adopcion</th>
            <th>Adoptar</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${adopciones}" var="adopcion">
            <tr>
                <td>
                    <spring:url value="/owners/{ownerId}" var="ownerUrl">
                        <spring:param name="ownerId" value="${adopcion.pet.owner.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(ownerUrl)}"><c:out value="${adopcion.pet.owner.firstName} ${adopcion.pet.owner.lastName}"/></a>
                </td>
                <td>
                    <c:out value="${adopcion.pet.name}"/>
                </td>
                <td>
                    <c:out value="${adopcion.pet.type}"/>
                </td>
                <td>
                    <c:out value="${adopcion.pet.birthDate}"/>
                </td>
                <td>
                    <c:out value="${adopcion.fechaPuestaEnAdopcion}"/>
                </td>
                <td>
                    <spring:url value="/adopciones/solicitud/{adopcionId}" var="adopcion">
                    <spring:param name="adopcionId" value="${adopcion.id}"/>
                	</spring:url>
                    <a href="${fn:escapeXml(adopcion)}" class="btn btn-default">Adoptar</a>
                </td>
            
                
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>