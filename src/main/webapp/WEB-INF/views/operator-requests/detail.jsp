<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Pedido aceito - myCAB</title>
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
<div class="container narrow">
    <h1>Pedido no app aceito</h1>
    <p>Voce aceitou o pedido. Veja o trajeto, o passageiro e despache para o carro mais proximo.</p>

    <section class="panel reveal">
        <h2>Trajeto</h2>
        <p><strong>${dispatch.pickupAddress}</strong> &rarr; <strong>${dispatch.destinationAddress}</strong></p>
        <p>Categoria: <strong>${dispatch.requestedCategory}</strong></p>
        <p>Coordenadas: ${dispatch.pickup.latitude}, ${dispatch.pickup.longitude}</p>
    </section>

    <section class="panel reveal customer">
        <h2>Passageiro</h2>
        <p class="customer-name">${dispatch.customer.name}</p>
        <p class="customer-phone">${dispatch.customer.phone.value}</p>
        <c:if test="${not empty dispatch.customer.defaultAddress}">
            <p>Endereco padrao: ${dispatch.customer.defaultAddress}</p>
        </c:if>
    </section>

    <p>
        <form method="post" action="<c:url value='/operator/requests/${dispatch.id}/assign'/>" style="display:inline">
            <button class="btn primary" type="submit">Buscar carro mais proximo</button>
        </form>
        <form method="post" action="<c:url value='/operator/requests/${dispatch.id}/cancel'/>" style="display:inline">
            <button class="btn" type="submit">Recusar pedido</button>
        </form>
        <a class="btn" href="<c:url value='/operator'/>">Voltar a central</a>
    </p>
</div>
</body>
</html>
