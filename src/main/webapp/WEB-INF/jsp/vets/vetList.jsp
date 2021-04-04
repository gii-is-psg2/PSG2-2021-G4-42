<%@ page session="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<petclinic:layout pageName="vets">
    <h2>Veterinarios</h2>

    <table id="vetsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Nombre</th>
            <th>Especialidades</th>
            <th>Editar</th>
           	<sec:authorize access="hasAuthority('admin')">
            <th></th>
            </sec:authorize>

          </tr>

        </thead>
        <tbody>
        <c:forEach items="${vets.vetList}" var="vet">
            <tr>
                <td>
                    <c:out value="${vet.firstName} ${vet.lastName}"/>
                </td>
                <td>
                    <c:forEach var="specialty" items="${vet.specialties}">
                        <c:out value="${specialty.name} "/>
                    </c:forEach>
                    <c:if test="${vet.nrOfSpecialties == 0}">ninguna</c:if>
                </td>
                <td>
                	<spring:url value="/vets/{id}/edit" var="vetUrl">
                        <spring:param name="id" value="${vet.id}"/>
                    </spring:url>
                    <a class="btn btn-default" href="${fn:escapeXml(vetUrl)}">Editar</a>
                
                </td>              
           		<sec:authorize access="hasAuthority('admin')">
                <td>
                 <spring:url value="/vets/{vetId}/delete" var="vetUrl">
                        <spring:param name="vetId" value="${vet.id}"/>
                 </spring:url>
                 <a href="${fn:escapeXml(vetUrl)}"> <span class="glyphicon glyphicon-trash"></span></a>
                </td>
                </sec:authorize>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <table class="table-buttons">
        <tr>
            <td style="padding-right: 7px">          
                <a href="<spring:url value="/vets.xml"/>">Ver como XML</a>
            </td>
            <td>
                <a href="<spring:url value="/vets/new"/>">Añadir nuevo vet</a>  
            </td>
        </tr>
    </table>
</petclinic:layout>
