<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Central - myCAB</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
</head>
<body>
<div class="navbar">
    <a class="brand" href="<c:url value='/'/>">myCAB</a>
    <ul class="nav">
        <li class="active"><a href="<c:url value='/operator'/>">Central</a></li>
        <li><a href="<c:url value='/cabs'/>">Frota</a></li>
        <li><a href="<c:url value='/review'/>">Aprovacoes <c:if test="${pendingCount > 0}"><span class="badge REQUESTED">${pendingCount}</span></c:if></a></li>
        <li><a href="<c:url value='/drivers'/>">Motoristas</a></li>
        <li><a href="<c:url value='/customers'/>">Clientes</a></li>
    </ul>
</div>
<div class="container">
    <h1>Central de operacoes</h1>
<p>Visao geral em tempo real da frota e das chamadas em andamento.</p>
    <c:if test="${flows.requestEnabled && not empty customerRequests}">
        <section class="panel call-inbox request-inbox">
            <h2>Pedidos no app (${fn:length(customerRequests)})</h2>
            <p class="muted">Decida com base no trajeto e na categoria. A identidade do cliente sera revelada apos aceitar.</p>
            <ul class="call-list">
                <c:forEach var="req" items="${customerRequests}">
                    <li>
                        <div class="call-meta">
                            <span class="trip"><strong>${req.pickup}</strong> &rarr; <strong>${req.destination}</strong></span>
                            <span class="category-tag">${req.category}</span>
                            <time>${req.createdAt}</time>
                        </div>
                        <div class="call-actions">
                            <a class="btn primary" href="<c:url value='/operator/requests/${req.id}'/>">Aceitar</a>
                        </div>
                    </li>
                </c:forEach>
            </ul>
        </section>
    </c:if>

    <c:if test="${flows.phoneCallEnabled && not empty operatorCalls}">
        <section class="panel call-inbox">
            <h2>Pedidos anonimos (${fn:length(operatorCalls)})</h2>
            <p class="muted">Decida com base em origem e destino. A identidade do cliente sera revelada apos aceitar.</p>
            <ul class="call-list">
                <c:forEach var="call" items="${operatorCalls}">
                    <li>
                        <div class="call-meta">
                            <span class="trip"><strong>${call.pickup}</strong> &rarr; <strong>${call.destination}</strong></span>
                            <time>${call.createdAt}</time>
                        </div>
                        <div class="call-actions">
                            <a class="btn primary" href="<c:url value='/operator/calls/${call.id}'/>">Aceitar</a>
                        </div>
                    </li>
                </c:forEach>
            </ul>
        </section>
    </c:if>

    <div class="operator-grid">
        <section class="panel">
            <h2>Chamadas ativas</h2>
            <ul class="dispatch-list">
                <c:forEach var="d" items="${dispatches}">
                    <li>
                        <a href="<c:url value='/dispatches/${d.id}'/>">
                            ${d.customer.name} - ${d.pickupAddress} - <span class="badge ${d.status}">${d.status}</span>
                        </a>
                    </li>
                </c:forEach>
                <c:if test="${empty dispatches}"><li>Sem chamadas ativas.</li></c:if>
            </ul>
            <p><a class="btn primary" href="<c:url value='/dispatches/new'/>">Nova chamada</a></p>
        </section>
        <section class="panel">
            <h2>Frota (${fn:length(fleet)})</h2>
            <ul class="fleet-list">
                <c:forEach var="cab" items="${fleet}">
                    <li>${cab.plate.value} - <span class="badge ${cab.status}">${cab.status}</span></li>
                </c:forEach>
                <c:if test="${empty fleet}"><li>Nenhum carro cadastrado.</li></c:if>
            </ul>
        </section>
        <section class="panel">
            <h2>Aprovacoes pendentes (${pendingCount})</h2>
            <ul class="pending-list">
                <c:forEach var="p" items="${pendingDrivers}" end="4">
                    <li><a href="<c:url value='/review/${p.id}'/>">${p.fullName} (${p.preferredCategory})</a></li>
                </c:forEach>
                <c:if test="${empty pendingDrivers}"><li>Nenhum cadastro pendente.</li></c:if>
            </ul>
            <c:if test="${pendingCount > 0}">
                <p><a class="btn primary" href="<c:url value='/review'/>">Abrir fila</a></p>
            </c:if>
        </section>
        <section class="panel">
            <h2>Clientes recentes</h2>
            <ul class="customer-list">
                <c:forEach var="c" items="${customers}" end="9">
                    <li>${c.name} - ${c.phone.value}</li>
                </c:forEach>
                <c:if test="${empty customers}"><li>Nenhum cliente cadastrado.</li></c:if>
            </ul>
        </section>
    </div>
</div>
<script>
    setTimeout(function() { window.location.reload(); }, 15000);
</script>
</body>
</html>
