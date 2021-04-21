<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<petclinic:layout pageName="causas">
    <h2>Causas</h2>

    <table id="ownersTable" class="table table-green table-striped">
        <thead>
        <tr>
        	<th>Nombre</th>
            <th>Organizacion</th>
            <th>Descripcion</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${causas}" var="causa">
            <tr>
                <td>
                    <a href="/causa/${causa.id}"> <c:out value="${causa.nombre}"/> </a>
                </td>
                <td>
                    <c:out value="${causa.organizacion}"/>
                </td>
                <td>
                    <c:out value="${causa.descripcion.substring(0,75)}"/>...
                </td>         
            </tr>
        </c:forEach>
        </tbody>
    </table>
    
 	<sec:authorize access="hasAuthority('owner')"> 
		<a href="/donacion/new/">
	            <button style="size: 30px">Realizar una donacion</button>
	 </a>
   	</sec:authorize>
</petclinic:layout>
