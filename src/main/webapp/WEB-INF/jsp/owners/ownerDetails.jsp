<%@ page session="false" trimDirectiveWhitespaces="true"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

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
			<td><b><c:out value="${owner.firstName} ${owner.lastName}" /></b></td>
		</tr>
		<tr>
			<th>Dirección</th>
			<td><c:out value="${owner.address}" /></td>
		</tr>
		<tr>
			<th>Ciudad</th>
			<td><c:out value="${owner.city}" /></td>
		</tr>
		<tr>
			<th>Teléfono</th>
			<td><c:out value="${owner.telephone}" /></td>
		</tr>
	</table>

	<spring:url value="{ownerId}/edit" var="editUrl">
		<spring:param name="ownerId" value="${owner.id}" />
	</spring:url>

	<c:if test="${showButtons}">
		<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar
			Dueño</a>
	</c:if>
	<sec:authorize access="hasAuthority('admin')">
		<spring:url value="{ownerId}/delete" var="deleteUrl">
			<spring:param name="ownerId" value="${owner.id}" />
		</spring:url>
		<a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Eliminar
			Dueño</a>
	</sec:authorize>


	<spring:url value="{ownerId}/pets/new" var="addUrl">
		<spring:param name="ownerId" value="${owner.id}" />
	</spring:url>
	<c:if test="${showButtons}">
		<a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Agregar
			nueva mascota</a>
	</c:if>

	<br />
	<br />
	<br />
	<h2>Mascotas y visitas</h2>

	<table class="table table-striped">
		<c:forEach var="pet" items="${owner.pets}">

			<tr>
				<td valign="top">
					<dl class="dl-horizontal">
						<dt>Nombre</dt>
						<dd>
							<c:out value="${pet.name}" />
						</dd>
						<dt>Cumpleaños</dt>
						<dd>
							<petclinic:localDate date="${pet.birthDate}" pattern="yyyy-MM-dd" />
						</dd>
						<dt>Tipo</dt>
						<dd>
							<c:out value="${pet.type.name}" />
						</dd>
						<dt>Raza</dt>
						<dd>
							<c:out value="${pet.raza}" />
						</dd>
					</dl>
				</td>
				<td valign="top"><c:if test="${showButtons}">
						<table class="table-condensed">
							<thead>
								<tr>
									<th>Fecha de visita</th>
									<th>Descripción</th>
								</tr>
							</thead>
							<c:forEach var="visit" items="${pet.visits}">
								<tr>
									<td><petclinic:localDate date="${visit.date}"
											pattern="yyyy-MM-dd" /></td>
									<td><c:out value="${visit.description}" /></td>
									<td><spring:url value="/visits/{visitId}/delete"
											var="visitUrl">
											<spring:param name="visitId" value="${visit.id}" />
										</spring:url> <a href="${fn:escapeXml(visitUrl)}">Cancelar visita</a></td>
								</tr>
							</c:forEach>
							<tr>
								<td><spring:url value="/owners/{ownerId}/pets/{petId}/edit"
										var="petUrl">
										<spring:param name="ownerId" value="${owner.id}" />
										<spring:param name="petId" value="${pet.id}" />
									</spring:url> <a href="${fn:escapeXml(petUrl)}">Editar mascota</a></td>
								<td><spring:url
										value="/owners/{ownerId}/pets/{petId}/delete" var="petUrl2">
										<spring:param name="ownerId" value="${owner.id}" />
										<spring:param name="petId" value="${pet.id}" />
									</spring:url> <a href="${fn:escapeXml(petUrl2)}">Borrar mascota</a></td>
								<td><spring:url value="/visits/new" var="visitUrl">
										<spring:param name="petId" value="${pet.id}" />
									</spring:url> <a href="${fn:escapeXml(visitUrl)}">Añadir visita</a></td>
							</tr>


						</table>
					</c:if> <c:if test="${showButtons}">
						<td><c:set var='mascotaEnAdopcion' value='${false}'></c:set>

							<c:forEach items='${adopciones}' var='adopcion'>
								<c:set var='mascotaEnAdopcion'
									value='${mascotaEnAdopcion||adopcion.pet eq pet}'></c:set>

							</c:forEach> <c:if test='${not mascotaEnAdopcion}'>
								<spring:url value="/owners/{ownerId}/adopciones/{petId}/new"
									var="adpUrl">
									<spring:param name="ownerId" value="${owner.id}" />
									<spring:param name="petId" value="${pet.id}" />
								</spring:url>
								<a href="${fn:escapeXml(adpUrl)}" class="btn btn-default">Dar
									en adopción</a>
							</c:if></td>
					</c:if>
			</tr>

		</c:forEach>
	</table>
	<c:if test="${showButtons}">
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
						<td>Habitación nº<c:out value="${reserva.habitacion.numero}" />
						</td>
						<td><c:out value="${reserva.fechaIni}" /></td>
						<td><c:out value="${reserva.fechaFin}" /></td>
						<td><c:out value="${reserva.pet}" /></td>
						<td><spring:url value="/reserva/delete/{reservaId}"
								var="reservaUrl">
								<spring:param name="reservaId" value="${reserva.id}" />
							</spring:url> <a href="${fn:escapeXml(reservaUrl)}">Cancelar reserva</a></td>


					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
	<h2>Adopciones</h2>
	<p>Aqui aparecerán las mascotas que tengas en adopción, y las
		personas interesadas en ellas. Puedes añadir y quitar tus mascotas de
		esta sección cuando quieras.</p>
	<table class="table table-striped">
		<c:forEach var="adopcion" items="${adopciones}">

			<tr>
				<td valign="top">
					<dl class="dl-horizontal">
						<dt>Nombre</dt>
						<dd>
							<c:out value="${adopcion.pet.name}" />
						</dd>
						<dt>Cumpleaños</dt>
						<dd>
							<petclinic:localDate date="${adopcion.pet.birthDate}"
								pattern="yyyy-MM-dd" />
						</dd>
						<dt>Tipo</dt>
						<dd>
							<c:out value="${adopcion.pet.type.name}" />
						</dd>
					</dl>
				</td>
				<c:if test="${showButtons}">
					<td valign="top"><spring:url
							value="/adopciones/delete/{adopcionId}" var="editUrl">
							<spring:param name="adopcionId" value="${adopcion.id}" />
						</spring:url> <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Dejar
							de dar en adopción</a></td>
				</c:if>
				<td valign="top"><spring:url
						value="/adopciones/interesados/{petId}" var="editUrl">
						<spring:param name="petId" value="${adopcion.pet.id}" />
					</spring:url> <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Ver
						personas interesadas</a></td>
				<c:if test="${not showButtons}">
					<td><spring:url value="/adopciones/solicitud/{adopcionId}"
							var="adopcion">
							<spring:param name="adopcionId" value="${adopcion.id}" />
						</spring:url> <a href="${fn:escapeXml(adopcion)}" class="btn btn-default">Adoptar</a>
					</td>
				</c:if>
			</tr>

		</c:forEach>
	</table>
</petclinic:layout>
