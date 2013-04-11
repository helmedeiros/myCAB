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

                                <label>Sua localizacao</label>
                                <div class="geolocate-row">
                                    <button class="btn ghost" type="button" id="geolocate-btn">Usar minha localizacao</button>
                                    <span class="geolocate-status" id="geolocate-status" aria-live="polite"></span>
                                </div>
                                <div class="coord-row">
                                    <input type="text" name="latitude" id="latitude" placeholder="latitude" inputmode="decimal"/>
                                    <input type="text" name="longitude" id="longitude" placeholder="longitude" inputmode="decimal"/>
                                </div>
                                <small class="coord-hint">Se preferir, edite latitude e longitude manualmente.</small>
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
        var btn = document.getElementById('geolocate-btn');
        var status = document.getElementById('geolocate-status');
        var lat = document.getElementById('latitude');
        var lon = document.getElementById('longitude');
        if (!btn || !lat || !lon) return;
        if (!('geolocation' in navigator)) {
            btn.disabled = true;
            status.textContent = 'GPS nao disponivel neste navegador.';
            return;
        }
        btn.addEventListener('click', function () {
            status.textContent = 'Buscando GPS...';
            btn.disabled = true;
            navigator.geolocation.getCurrentPosition(function (pos) {
                lat.value = pos.coords.latitude.toFixed(6);
                lon.value = pos.coords.longitude.toFixed(6);
                status.textContent = 'Localizacao capturada.';
                btn.disabled = false;
            }, function (err) {
                status.textContent = 'Nao consegui ler o GPS. Voce pode digitar manualmente.';
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
