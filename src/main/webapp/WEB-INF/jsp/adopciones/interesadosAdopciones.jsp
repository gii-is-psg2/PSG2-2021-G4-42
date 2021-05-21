<%@ page session="false" trimDirectiveWhitespaces="true"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="adopciones">
	<h2>Personas Interesadas</h2>

	<table id="adopcionTable" class="table table-green table-striped">
		<thead>
			<tr>
				<th>Nuevo Dueño</th>
				<th>Solicitud</th>
				<c:if test="${showButtons}">
					<th>Aceptar</th>
					<th>Denegar</th>
				</c:if>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${solicitudes}" var="s">
				<tr>
					<td><c:out
							value="${s.nuevoOwner.firstName} ${s.nuevoOwner.lastName}" /></td>
					<td><c:out value="${s.solicitud}" /></td>
					<c:if test="${showButtons}">
						<td><spring:url
								value="/adopciones/solicitudAdopcion/{solicitudId}/aceptar"
								var="SolicitudAdopcion">
								<spring:param name="solicitudId" value="${s.id}" />
							</spring:url> <a href="${fn:escapeXml(SolicitudAdopcion)}"
							class="btn btn-default">Aceptar solicitud</a></td>
						<td><spring:url
								value="/adopciones/solicitudAdopcion/{solicitudId}/denegar"
								var="SolicitudAdopcion">
								<spring:param name="solicitudId" value="${s.id}" />
							</spring:url> <a href="${fn:escapeXml(SolicitudAdopcion)}"
							class="btn btn-default">Denegar solicitud</a></td>
					</c:if>

				</tr>
			</c:forEach>
		</tbody>
	</table>
</petclinic:layout>