<%@ page session="false" trimDirectiveWhitespaces="true"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="adopciones">
	<h2>Solicita una adopción</h2>
	<p>Para que esta mascota pase a ser parte de tu familia. Deberás
		concretarle al dueño actual los motivos por los cuales estás
		interesados en adoptarla. Por ejemplo... ¿Cómo cuidarías de tu nueva
		mascota?</p>

	<table id="mascotasTable" class="table table-green table-striped">
		<thead>
			<tr>
				<th>Mascota</th>
				<th>Tipo Mascota</th>
				<th>Raza Mascota</th>
				<th>Cumpleaños</th>
				<th>Dueño Actual</th>
				<th>Fecha puesta en adopción</th>
			</tr>
		</thead>
		<tbody>

			<tr>
				<td><c:out value="${pet}" /></td>
				<td><c:out value="${pet.type}" /></td>
				<td><c:out value="${pet.raza}" /></td>
				<td><c:out value="${pet.birthDate}" /></td>
				<td><c:out value="${pet.owner.firstName} ${pet.owner.lastName}" /></td>
				<td><c:out value="${adopcion.fechaPuestaEnAdopcion}" /></td>

			</tr>

		</tbody>
	</table>
	<form:form modelAttribute="solicitud" 
		id="add-adopcion-form">
		<div class="form-group">
			<div> 
				<input type='hidden' value='${solicitud.nuevoOwner.id}' name='nuevoOwner'>
				<input type='hidden' value='${adopcion.id}' name='adopcion'>
				<input type='hidden' value='${solicitud.fechaSolicitud}' name='fechaSolicitud' >
				<petclinic:inputField label="Solicitud de adopción" name="solicitud" />
				<button class="btn btn-default" type="submit">Solicitar	adopción</button>

			</div>
		</div>
	</form:form>
</petclinic:layout>