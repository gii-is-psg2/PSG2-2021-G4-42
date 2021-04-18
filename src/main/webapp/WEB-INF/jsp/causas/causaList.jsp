<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="causas">
    <h2>Causas</h2>

    <table id="ownersTable" class="table table-green table-striped">
        <thead>
        <tr>
        	<th>Nombre</th>
            <th style="width: 150px;">Organizacion</th>
            <th style="width: 200px;">Descripcion</th>
            <th>Recaudacion</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${causas}" var="causa">
            <tr>
                <td>
                    <c:out value="${causa.nombre}"/>
                </td>
                <td>
                    <c:out value="${causa.organizacion}"/>
                </td>
                <td>
                    <c:out value="${causa.descripcion}"/>
                </td>
                <td>
                    <c:out value="${causa.recaudacion}"/>
                </td>
        
                
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
