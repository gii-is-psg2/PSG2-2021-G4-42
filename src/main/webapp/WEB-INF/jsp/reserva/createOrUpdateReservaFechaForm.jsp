<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="reserva">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
            	 fechaIni = $("#fechaIni").datepicker({dateFormat: 'dd/mm/yy', showAnim: 'slideDown', minDate: new Date()})
            	 .on("change", function() {
            		 fechaFin.datepicker("option", "minDate", getDate( this ) );
			     }),
            	 fechaFin = $("#fechaFin").datepicker({dateFormat: 'dd/mm/yy', showAnim: 'slideDown', minDate: new Date()})
            	 .on("change", function() {
            		 fechaIni.datepicker("option", "maxDate", getDate( this ) );
			     });
            	 function getDate( element ) {
            	      var date;
            	      try {
            	        date = $.datepicker.parseDate( 'dd/mm/yy', element.value );
            	      } catch( error ) {
            	        date = null;
            	      }
            	 
            	      return date;
            	    }
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <h2>
            <c:if test="${reserva['new']}">Nueva </c:if> Reserva
        </h2>
        <form:form action="new/fechas" modelAttribute="reserva" class="form-horizontal">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Fecha de inicio" name="fechaIni" autocomplete="off"/>
                <petclinic:inputField label="fecha de fin" name="fechaFin" autocomplete="off"/>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                	<button class="btn btn-default" type="submit">Siguiente</button>
                </div>
            </div>
        </form:form>
        <c:if test="${!pet['new']}">
        </c:if>
    </jsp:body>
</petclinic:layout>
