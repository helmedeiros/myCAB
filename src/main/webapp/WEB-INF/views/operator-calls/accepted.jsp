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
    <h1>Pedido aceito</h1>
    <p>Voce aceitou o pedido. Agora veja quem e o passageiro para informar ao motorista.</p>

    <section class="panel reveal">
        <h2>Trajeto</h2>
        <p><strong>${message.pickupAddress}</strong> &rarr; <strong>${message.destinationAddress}</strong></p>
    </section>

    <c:if test="${customer != null}">
        <section class="panel reveal customer">
            <h2>Passageiro</h2>
            <p class="customer-name">${customer.name}</p>
            <p class="customer-phone">${customer.phone.value}</p>
            <c:if test="${not empty customer.defaultAddress}">
                <p>Endereco padrao: ${customer.defaultAddress}</p>
            </c:if>
        </section>

        <p>
            <a class="btn primary" href="<c:url value='/dispatches/new?customerId=${customer.id}&pickupAddress=${message.pickupAddress}'/>">Despachar</a>
            <a class="btn" href="<c:url value='/operator'/>">Voltar a central</a>
        </p>
    </c:if>
</div>
</body>
</html>
