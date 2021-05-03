<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<petclinic:layout pageName="causa">

	<table id="causaTable" class="table table-striped">
            <tr>
                <th>                    
                   <h2><c:out value="${causa.nombre}"/></h2> 
                   <br>
                   Organización:<p><c:out value="${causa.organizacion}"/></p>   
                   Descripción:<p><c:out value="${causa.descripcion}"/></p>
                   <c:if test="${recaudado < causa.recaudacionObjetivo}">
                   		Recaudacion:<p><c:out value="${recaudado}"/>/<c:out value="${causa.recaudacionObjetivo}"/></p>
                </c:if>
                <c:if test="${recaudado >= causa.recaudacionObjetivo}">
                	Recaudacion:<p> Hemos conseguido el objetivo, muchas gracias!!!</p>
                </c:if>
                </th>
            </tr>     
                
    </table>
    
    <style type="text/css">
    	#scroll{
    		height: 300px;
			width: 100%;
			border: 1px solid #ddd;
			background: #f1f1f1;
			overflow-y: auto;
    	}
    	#donaciones{
    		height:auto;
    	}
    </style>
    
    <div id="scroll">
    	<table id="donacionesTable" class="table table-green table-striped">
    	<thead>
        <tr>
        	<th>Fecha</th>
            <th>Nombre</th>
            <th>Cantidad</th>
        </tr>
        </thead>
        <tbody>
    	<c:forEach items="${donaciones}" var="donacion">
    		<div id="donaciones">
    			<tr>
    				<td>
    					<c:out value="${donacion.fechaDonacion}"></c:out>
    				</td>
    				<td>
    					<c:out value="${donacion.donante.firstName}"></c:out>&nbsp;<c:out value="${donacion.donante.lastName}"></c:out>
    				</td>
    				<td>
    					 <c:out value="${donacion.cantidadDonada}"></c:out>
    				</td>
    			</tr>
    		</div>
    	</c:forEach>
    	</tbody>
    	</table>
    	</div>
    	<br>
    	<c:if test="${recaudado < causa.recaudacionObjetivo}">
    		<sec:authorize access="hasAuthority('owner')"> 
			<a href="/donacion/${causa.id}/new/">
	        	    <button style="size: 30px">Realizar una donacion</button>
	 		</a>
   			</sec:authorize>
   		</c:if>
 
</petclinic:layout>
