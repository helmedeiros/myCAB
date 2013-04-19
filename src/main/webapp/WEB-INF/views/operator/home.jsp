<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Central - myCAB</title>
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/leaflet.css"/>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/leaflet.js"></script>
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

    <section class="ops-map-section">
        <div id="ops-map" class="ops-map"></div>
        <div class="ops-map-legend">
            <span class="legend-chip cab FREE">L</span> Livre
            <span class="legend-chip cab BUSY">O</span> Ocupado
            <span class="legend-chip cab OFFLINE">X</span> Fora do ar
            <span class="legend-chip dispatch REQUESTED">P</span> Pedido
            <span class="legend-chip dispatch PROPOSED">C</span> Conferindo
            <span class="legend-chip dispatch ASSIGNED">A</span> Atribuido
        </div>
    </section>

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
    setTimeout(function() { window.location.reload(); }, 60000);
    (function() {
        var mapEl = document.getElementById('ops-map');
        if (!mapEl || typeof L === 'undefined') return;
        var map = L.map(mapEl).setView([-30.0277, -51.2287], 13);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; OpenStreetMap',
            maxZoom: 18
        }).addTo(map);
        setTimeout(function () { map.invalidateSize(); }, 80);

        var cabMarkers = {};
        var dispatchMarkers = {};
        var fitted = false;

        var LETTERS = {
            cab:      { FREE: 'L', BUSY: 'O', OFFLINE: 'X' },
            dispatch: { REQUESTED: 'P', PROPOSED: 'C', ASSIGNED: 'A' }
        };

        function dotIcon(kind, status) {
            var letter = (LETTERS[kind] && LETTERS[kind][status]) || '?';
            return L.divIcon({
                className: 'map-dot ' + kind + ' ' + status,
                html: letter,
                iconSize: [24, 24]
            });
        }

        function refresh() {
            $.getJSON('<c:url value="/api/operator-map"/>', function (data) {
                var seenCabs = {};
                var seenDispatches = {};
                var allPoints = [];

                (data.cabs || []).forEach(function (c) {
                    seenCabs[c.id] = true;
                    var pos = [c.lat, c.lon];
                    allPoints.push(pos);
                    var label = (c.fleetId ? '#' + c.fleetId + ' &middot; ' : '') + c.plate +
                        ' <span class="badge ' + c.status + '">' + c.status + '</span>';
                    if (cabMarkers[c.id]) {
                        cabMarkers[c.id].setLatLng(pos);
                        cabMarkers[c.id].setIcon(dotIcon('cab', c.status));
                        cabMarkers[c.id].setPopupContent(label);
                    } else {
                        cabMarkers[c.id] = L.marker(pos, { icon: dotIcon('cab', c.status) })
                            .addTo(map).bindPopup(label);
                    }
                });
                Object.keys(cabMarkers).forEach(function (id) {
                    if (!seenCabs[id]) { map.removeLayer(cabMarkers[id]); delete cabMarkers[id]; }
                });

                (data.dispatches || []).forEach(function (d) {
                    seenDispatches[d.id] = true;
                    var pos = [d.lat, d.lon];
                    allPoints.push(pos);
                    var name = d.anonymized ? 'Anonimo' : (d.customerName || 'Cliente');
                    var html = '<strong>' + name + '</strong> <span class="badge ' + d.status + '">' + d.status + '</span><br/>' +
                        (d.pickupAddress || '') +
                        (d.destinationAddress ? ' &rarr; ' + d.destinationAddress : '') +
                        '<br/>Categoria: ' + d.category;
                    if (dispatchMarkers[d.id]) {
                        dispatchMarkers[d.id].setLatLng(pos);
                        dispatchMarkers[d.id].setIcon(dotIcon('dispatch', d.status));
                        dispatchMarkers[d.id].setPopupContent(html);
                    } else {
                        dispatchMarkers[d.id] = L.marker(pos, { icon: dotIcon('dispatch', d.status) })
                            .addTo(map).bindPopup(html);
                    }
                });
                Object.keys(dispatchMarkers).forEach(function (id) {
                    if (!seenDispatches[id]) { map.removeLayer(dispatchMarkers[id]); delete dispatchMarkers[id]; }
                });

                if (!fitted && allPoints.length > 0) {
                    map.fitBounds(allPoints, { padding: [40, 40], maxZoom: 15 });
                    fitted = true;
                }
            });
        }

        refresh();
        setInterval(refresh, 8000);
    })();
</script>
</body>
</html>
