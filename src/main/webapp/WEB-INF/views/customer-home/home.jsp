<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Minha conta - myCAB</title>
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/leaflet.css"/>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/leaflet.js"></script>
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
                        <p>Preencha o trajeto e escolha como quer pedir.</p>
                        <form method="post" class="request-form" id="request-form">
                            <label>Origem</label>
                            <input type="text" name="pickup" placeholder="Onde voce esta" value="${customer.defaultAddress}" required/>

                            <label>Destino</label>
                            <input type="text" name="destination" placeholder="Para onde vai" required/>

                            <c:if test="${flows.autoDispatchEnabled || flows.requestEnabled}">
                                <label>Categoria</label>
                                <select name="category">
                                    <c:forEach var="cat" items="${categories}">
                                        <option value="${cat}">${cat}</option>
                                    </c:forEach>
                                </select>

                                <label>Onde voce esta</label>
                                <div class="geolocate-row">
                                    <button class="btn ghost" type="button" id="geolocate-btn">Usar minha localizacao</button>
                                    <span class="geolocate-status" id="geolocate-status" aria-live="polite"></span>
                                </div>
                                <div id="pickup-map" class="pickup-map"></div>
                                <input type="hidden" name="latitude" id="latitude"/>
                                <input type="hidden" name="longitude" id="longitude"/>
                                <small class="coord-hint">Toque no mapa para marcar o ponto de embarque.</small>
                            </c:if>

                            <div class="cta-stack">
                                <c:if test="${flows.autoDispatchEnabled}">
                                    <button class="btn primary" type="submit" formaction="<c:url value='/me/auto'/>">
                                        Pedir agora
                                    </button>
                                    <small>O sistema escolhe o carro mais proximo automaticamente.</small>
                                </c:if>
                                <c:if test="${flows.requestEnabled}">
                                    <button class="btn" type="submit" formaction="<c:url value='/me/request'/>">
                                        Pedir pela central
                                    </button>
                                    <small>A central recebe seu pedido anonimo e confirma um carro.</small>
                                </c:if>
                                <c:if test="${flows.phoneCallEnabled}">
                                    <button class="btn ghost" type="submit" formaction="<c:url value='/me/call-operator'/>">
                                        Falar com a central
                                    </button>
                                    <small>A central decide aceitar antes de te identificar.</small>
                                </c:if>
                            </div>
                        </form>
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
    (function() {
        var mapEl = document.getElementById('pickup-map');
        var lat = document.getElementById('latitude');
        var lon = document.getElementById('longitude');
        var btn = document.getElementById('geolocate-btn');
        var status = document.getElementById('geolocate-status');
        if (!mapEl || !lat || !lon || typeof L === 'undefined') return;

        var DEFAULT_CENTER = [-30.0277, -51.2287];
        var map = L.map(mapEl).setView(DEFAULT_CENTER, 13);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; OpenStreetMap',
            maxZoom: 18
        }).addTo(map);
        setTimeout(function () { map.invalidateSize(); }, 80);

        var marker = null;
        function setPin(latitude, longitude) {
            var ll = L.latLng(latitude, longitude);
            if (marker) {
                marker.setLatLng(ll);
            } else {
                marker = L.marker(ll, { draggable: true }).addTo(map);
                marker.on('dragend', function () {
                    var p = marker.getLatLng();
                    writeCoords(p.lat, p.lng);
                });
            }
            writeCoords(latitude, longitude);
        }

        function writeCoords(latitude, longitude) {
            lat.value = latitude.toFixed(6);
            lon.value = longitude.toFixed(6);
        }

        map.on('click', function (ev) {
            setPin(ev.latlng.lat, ev.latlng.lng);
        });

        if (!btn) return;
        if (!('geolocation' in navigator)) {
            btn.disabled = true;
            status.textContent = 'GPS nao disponivel neste navegador.';
            return;
        }
        btn.addEventListener('click', function () {
            status.textContent = 'Buscando GPS...';
            btn.disabled = true;
            navigator.geolocation.getCurrentPosition(function (pos) {
                setPin(pos.coords.latitude, pos.coords.longitude);
                map.setView([pos.coords.latitude, pos.coords.longitude], 16);
                status.textContent = 'Localizacao capturada.';
                btn.disabled = false;
            }, function () {
                status.textContent = 'Nao consegui ler o GPS. Toque no mapa para marcar.';
                btn.disabled = false;
            }, { enableHighAccuracy: true, timeout: 10000, maximumAge: 30000 });
        });
    })();
    (function() {
        var form = document.getElementById('request-form');
        if (!form) return;
        var preciseFields = form.querySelectorAll('[name="latitude"], [name="longitude"], [name="category"]');
        var ctas = form.querySelectorAll('[formaction]');
        for (var i = 0; i < ctas.length; i++) {
            ctas[i].addEventListener('click', function (ev) {
                var path = ev.currentTarget.getAttribute('formaction');
                var precise = path.indexOf('/me/auto') !== -1 || path.indexOf('/me/request') !== -1;
                for (var j = 0; j < preciseFields.length; j++) {
                    preciseFields[j].required = precise;
                }
            });
        }
    })();
</script>
</body>
</html>
