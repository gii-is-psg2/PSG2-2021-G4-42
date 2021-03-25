<%@ page session="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="error">

    <spring:url value="/resources/images/capo.png" var="petsImage"/>
    <img class=img-responsive src="${petsImage}"/>

    <h2>Algo pasó...</h2>

    <p>${exception.message}</p>

</petclinic:layout>
