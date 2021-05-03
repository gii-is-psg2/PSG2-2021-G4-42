<%@ page session="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">
    <h2>
        Dar a tu mascota en adopción
    </h2>
    <p>Si das tu mascota en adopción, recibirás solicitudes de adopción por parte de otros dueños, acepta una de ellas para que tu mascota tenga una nueva familia.</p>
   
        <table id="mascotasTable" class="table table-green table-striped">
		<thead>
			<tr>
				<th>Mascota</th>
				<th>Tipo Mascota</th>
				<th>Cumpleaños </th>
				<th>Dueño Actual</th>
				<th>Fecha puesta en adopción</th>
			</tr>
		</thead>
		<tbody>

				<tr>
					<td><c:out value="${pet}" /></td>
					<td><c:out value="${pet.type}" /></td>
					<td><c:out value="${pet.birthDate}" /></td>
					<td><c:out value="${owner.firstName} ${owner.lastName}" /></td>
					<td><c:out value="${fecha}" /></td>
					
				</tr>

		</tbody>
	</table>
       <form:form modelAttribute="owner" class="form-horizontal" id="add-adopcion-form">
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
 		
                        <button class="btn btn-default" type="submit">Confirmar </button>
                    
            </div>
        </div>
    </form:form>
</petclinic:layout>