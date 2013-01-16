<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Chamadas ativas - myCAB</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
</head>
<body>
<div class="navbar">
    <a class="brand" href="<c:url value='/'/>">myCAB</a>
    <ul class="nav">
        <li class="active"><a href="<c:url value='/operator'/>">Central</a></li>
        <li><a href="<c:url value='/cabs'/>">Frota</a></li>
        <li><a href="<c:url value='/review'/>">Aprovacoes</a></li>
        <li><a href="<c:url value='/drivers'/>">Motoristas</a></li>
        <li><a href="<c:url value='/customers'/>">Clientes</a></li>
    </ul>
</div>
<div class="container">
    <h1>Chamadas ativas</h1>
<p>Despache para o carro mais proximo ou cancele rapidamente.</p>
    <p><a class="btn primary" href="<c:url value='/dispatches/new'/>">Nova chamada</a></p>
    <table class="table">
        <thead>
        <tr>
            <th>Cliente</th>
            <th>Endereco</th>
            <th>Categoria</th>
            <th>Status</th>
            <th>Carro</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="d" items="${dispatches}">
            <tr>
                <td>${d.customer.name}</td>
                <td>${d.pickupAddress}</td>
                <td>${d.requestedCategory}</td>
                <td><span class="badge ${d.status}">${d.status}</span></td>
                <td>
                    <c:if test="${d.assignedCab != null}">${d.assignedCab.plate.value}</c:if>
                </td>
                <td>
                    <c:if test="${d.status == 'REQUESTED'}">
                        <form method="post" action="<c:url value='/dispatches/${d.id}/assign'/>" style="display:inline">
                            <button class="btn primary">Buscar carro</button>
                        </form>
                    </c:if>
                    <c:if test="${d.status == 'ASSIGNED'}">
                        <form method="post" action="<c:url value='/dispatches/${d.id}/complete'/>" style="display:inline">
                            <button class="btn">Finalizar</button>
                        </form>
                    </c:if>
                    <form method="post" action="<c:url value='/dispatches/${d.id}/cancel'/>" style="display:inline">
                        <button class="btn">Cancelar</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty dispatches}">
            <tr><td colspan="6">Nenhuma chamada ativa.</td></tr>
        </c:if>
        </tbody>
    </table>
</div>
</body>
</html>
