<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Minha conta - myCAB</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
</head>
<body class="device">
<div class="device-frame">
    <header class="device-header">
        <span class="brand">myCAB</span>
        <span class="customer">${customer.name}</span>
    </header>

    <section class="driver-greeting">
        <h2>Ola, ${customer.name}</h2>
        <p>${customer.phone.value}</p>
    </section>

    <c:choose>
        <c:when test="${activeDispatch != null && activeDispatch.status == 'ASSIGNED' && assignedDriver != null}">
            <section class="pickup-card">
                <h3>Seu carro esta a caminho</h3>
                <p class="customer-name">${assignedDriver.fullName}</p>
                <c:if test="${activeDispatch.assignedCab != null}">
                    <p class="pickup-address">
                        Frota <strong>#${activeDispatch.assignedCab.fleetId}</strong>
                        &middot;
                        <c:if test="${not empty activeDispatch.assignedCab.color}">${activeDispatch.assignedCab.color} </c:if>${activeDispatch.assignedCab.model.displayName}
                        &middot;
                        <strong>${activeDispatch.assignedCab.plate.value}</strong>
                    </p>
                </c:if>
                <p>Procure por esse carro no endereco abaixo:</p>
                <p class="pickup-address"><strong>${activeDispatch.pickupAddress}</strong></p>
            </section>
        </c:when>
        <c:when test="${activeDispatch != null && activeDispatch.status == 'REQUESTED'}">
            <section class="status-card pending">
                <h3>Chamada registrada</h3>
                <p>A central esta procurando um carro proximo. Aguarde, em instantes voce sera notificado.</p>
                <c:if test="${not empty activeDispatch.pickupAddress}">
                    <p>Endereco: <strong>${activeDispatch.pickupAddress}</strong></p>
                </c:if>
            </section>
        </c:when>
        <c:otherwise>
            <section class="status-card waiting">
                <h3>Sem chamada ativa</h3>
                <c:choose>
                    <c:when test="${flows.anyEnabled}">
                        <p>Como voce quer pedir um carro?</p>
                        <div class="flow-actions">
                            <c:if test="${flows.autoDispatchEnabled}">
                                <form method="post" action="<c:url value='/me/auto'/>">
                                    <button class="btn primary" type="submit">Pedir agora</button>
                                </form>
                                <small>Sistema escolhe e o motorista aceita.</small>
                            </c:if>
                            <c:if test="${flows.requestEnabled}">
                                <form method="post" action="<c:url value='/me/request'/>" class="call-form">
                                    <label>Origem</label>
                                    <input type="text" name="pickup" placeholder="Onde voce esta" value="${customer.defaultAddress}" required/>
                                    <label>Destino</label>
                                    <input type="text" name="destination" placeholder="Para onde vai" required/>
                                    <label>Categoria</label>
                                    <select name="category" required>
                                        <c:forEach var="cat" items="${categories}">
                                            <option value="${cat}">${cat}</option>
                                        </c:forEach>
                                    </select>
                                    <label>Sua localizacao (latitude, longitude)</label>
                                    <div class="coord-row">
                                        <input type="text" name="latitude" placeholder="-30.0277" required/>
                                        <input type="text" name="longitude" placeholder="-51.2287" required/>
                                    </div>
                                    <button class="btn primary" type="submit">Pedir carro</button>
                                </form>
                                <small>A central recebe um pedido anonimo com seu trajeto.</small>
                            </c:if>
                            <c:if test="${flows.phoneCallEnabled}">
                                <form method="post" action="<c:url value='/me/call-operator'/>" class="call-form">
                                    <label>Origem</label>
                                    <input type="text" name="pickup" placeholder="Onde voce esta" value="${customer.defaultAddress}" required/>
                                    <label>Destino</label>
                                    <input type="text" name="destination" placeholder="Para onde vai" required/>
                                    <button class="btn" type="submit">Falar com a central</button>
                                </form>
                                <small>A central recebe so origem e destino e depois decide aceitar.</small>
                            </c:if>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p>O pedido pelo aplicativo nao esta disponivel nesta cidade. Ligue para a central.</p>
                    </c:otherwise>
                </c:choose>
            </section>
        </c:otherwise>
    </c:choose>

    <c:if test="${not empty history}">
        <section class="history">
            <h3>Corridas recentes</h3>
            <ul class="msg-list">
                <c:forEach var="d" items="${history}" end="4">
                    <li>
                        <time>${d.createdAt}</time>
                        <p>
                            <span class="badge ${d.status}">${d.status}</span>
                            <c:if test="${not empty d.pickupAddress}"> &middot; ${d.pickupAddress}</c:if>
                            <c:if test="${d.assignedCab != null && not empty d.assignedCab.fleetId}"> &middot; Frota #${d.assignedCab.fleetId}</c:if>
                        </p>
                    </li>
                </c:forEach>
            </ul>
        </section>
    </c:if>

    <form method="post" action="<c:url value='/customer-logout'/>" class="logout-row">
        <button class="btn" type="submit">Sair</button>
    </form>
</div>

<script type="text/javascript">
    setInterval(function() { window.location.reload(); }, 20000);
</script>
</body>
</html>
