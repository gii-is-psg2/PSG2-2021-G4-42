<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ page session="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>

<petclinic:layout pageName = "causas">
	<form:form action="/causa/new" modelAttribute="causa" class="form-horizontal">
		<petclinic:inputField label="Nombre de la causa" name="nombre"></petclinic:inputField>
		<petclinic:inputField label="Nombre de la organización" name="organizacion"></petclinic:inputField>
		<petclinic:textarea label="Descripción" name="descripcion"></petclinic:textarea>
		<petclinic:inputField label="Establece la recaudación objetivo de la causa"
								 name="recaudacionObjetivo"></petclinic:inputField>
		<div>
			<button class="btn btn-default" type="submit">Crear causa</button>
		</div>
	</form:form>
</petclinic:layout>