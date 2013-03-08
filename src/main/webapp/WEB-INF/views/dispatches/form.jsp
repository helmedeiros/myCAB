<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Nova chamada - myCAB</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
</head>
<body>
<div class="navbar"><a class="brand" href="<c:url value='/'/>">myCAB</a></div>
<div class="container narrow">
    <h1>Nova chamada</h1>
    <c:if test="${lockCustomer && customer != null}">
        <div class="success">Atendendo chamada de <strong>${customer.name}</strong> (${customer.phone.value}).</div>
    </c:if>
    <form method="post" action="<c:url value='/dispatches'/>">
        <c:if test="${callId != null}">
            <input type="hidden" name="callId" value="${callId}"/>
        </c:if>

        <label>Cliente</label>
        <c:choose>
            <c:when test="${lockCustomer && customer != null}">
                <input type="hidden" name="customerId" value="${customer.id}"/>
                <input type="text" value="${customer.name} - ${customer.phone.value}" disabled/>
            </c:when>
            <c:otherwise>
                <select name="customerId" required>
                    <c:forEach var="c" items="${customers}">
                        <option value="${c.id}">${c.name} - ${c.phone.value}</option>
                    </c:forEach>
                </select>
            </c:otherwise>
        </c:choose>

        <label>Latitude</label>
        <input type="text" name="latitude" required/>

        <label>Longitude</label>
        <input type="text" name="longitude" required/>

        <label>Endereco</label>
        <input type="text" name="pickupAddress" placeholder="Rua, numero, bairro" value="${customer != null ? customer.defaultAddress : ''}"/>

        <label>Categoria</label>
        <select name="category" required>
            <c:forEach var="cat" items="${categories}">
                <option value="${cat}">${cat}</option>
            </c:forEach>
        </select>

        <p>
            <button class="btn primary" type="submit">Registrar</button>
            <a class="btn" href="<c:url value='/dispatches'/>">Cancelar</a>
        </p>
    </form>
</div>
</body>
</html>
