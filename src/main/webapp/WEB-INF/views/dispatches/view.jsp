<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Chamada #${dispatch.id} - myCAB</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
</head>
<body>
<div class="navbar"><a class="brand" href="<c:url value='/'/>">myCAB</a></div>
<div class="container">
    <h1>Chamada #${dispatch.id}</h1>
    <p>Cliente: <strong>${dispatch.customer.name}</strong></p>
    <p>Endereco: <strong>${dispatch.pickupAddress}</strong></p>
    <p>Categoria solicitada: <strong>${dispatch.requestedCategory}</strong></p>
    <p>Status: <span class="badge ${dispatch.status}">${dispatch.status}</span></p>
    <c:if test="${dispatch.assignedCab != null}">
        <p>Carro: <strong>${dispatch.assignedCab.plate.value}</strong>
           (${dispatch.assignedCab.model.displayName})</p>
    </c:if>
    <p>
        <c:if test="${dispatch.status == 'REQUESTED'}">
            <form method="post" action="<c:url value='/dispatches/${dispatch.id}/assign'/>" style="display:inline">
                <button class="btn primary">Buscar carro mais proximo</button>
            </form>
        </c:if>
        <c:if test="${dispatch.status == 'ASSIGNED'}">
            <form method="post" action="<c:url value='/dispatches/${dispatch.id}/complete'/>" style="display:inline">
                <button class="btn primary">Finalizar</button>
            </form>
        </c:if>
        <a class="btn" href="<c:url value='/dispatches'/>">Voltar</a>
    </p>
</div>
</body>
</html>
