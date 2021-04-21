<%@ page session="false" trimDirectiveWhitespaces="true" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="donacion">

	<h2>Participa en nuestra causa con esta donación</h2>
	<br>
	<form:form modelAttribute="donacion" class="form_horizontal" id="add-donacion-form">
		<div class="form-group">
			Cantidad a Donar <input type="number" name="cantidadDonada"/>
			<br>
			<br>
			Seleccione la causa <select style="height: 25px" name="causa">
				<c:forEach items="${causas}" var="causa">
					<option value="${causa.id}">"${causa.nombre}"</option>
				</c:forEach>
			</select>
			<input type="hidden" name="donante"/>
			<input type="hidden" name="fechaDonacion"/>
		</div>
		<br>
		<div align="center">
				<button class="btn btn-default" type="submit">Donar</button>
			</div>
	</form:form>


</petclinic:layout>