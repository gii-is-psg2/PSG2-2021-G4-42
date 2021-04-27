<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="donacion">

	<style>
		.not-show{
			display: none;
		}
	</style>

	<h2>Participa en nuestra causa con esta donación</h2>
	<br>
	<form:form modelAttribute="donacion" class="form_horizontal" id="add-donacion-form">
		<div class="form-group">
		<div class="form-group">
			<h1><c:out value="${causa.nombre}"></c:out></h1>
			<p><c:out value="${causa.descripcion}"></c:out></p>
		</div>
		<div>
			<petclinic:inputField type="number" label="Cantidad a donar" name="cantidadDonada"/>
			<br>
			<br>
			<form:input class="not-show" path="causa"></form:input>
			<form:input class="not-show" path="donante"></form:input>
			<form:input class="not-show" path="fechaDonacion"></form:input> 
		</div>
		</div>
		<br>
		<br>
		<div align="center">
				<button class="btn btn-default" type="submit">Donar</button>
			</div>
	</form:form>


</petclinic:layout>