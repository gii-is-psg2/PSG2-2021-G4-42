<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ page session="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>

<petclinic:layout pageName = "crearCausas">
	<form:form action="/causa/new" modelAttribute="causa" class="form-horizontal">
		<div>
			<h3> Detalles de la Causa </h3>
			<p>Nombre de la causa</p>
			<form:input size="" type="text" path="nombre"/>
			<p>Nombre de la organizacion</p>
			<form:input type="text" path="organizacion"/>
			<p>Descripción</p>
			<form:textarea rows="10" cols="50" path="descripcion"/>
		</div>
		<br>
		<br>
		<div>
			<h3>Establece la recaudacion objetivo de esta causa</h3>
			<form:input type="number" path="recaudacionObjetivo"/>
		</div>
		<br>
		<div>
			<button class="btn btn-default" type="submit">Crear causa</button>
		</div>
	</form:form>
</petclinic:layout>