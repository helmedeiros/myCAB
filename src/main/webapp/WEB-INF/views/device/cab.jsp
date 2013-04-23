<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Frota ${cab.fleetId} - myCAB</title>
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/leaflet.css"/>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/leaflet.js"></script>
</head>
<body class="device">
<div class="device-frame">
    <header class="device-header">
        <span class="brand">myCAB</span>
        <c:choose>
            <c:when test="${not empty cab.fleetId}">
                <span class="fleet-number">#${cab.fleetId}</span>
            </c:when>
            <c:otherwise>
                <span class="fleet-number pending">sem frota</span>
            </c:otherwise>
        </c:choose>
        <span class="plate">${cab.plate.value}</span>
        <span class="badge ${cab.status}">${cab.status}</span>
    </header>

    <section class="messages" id="messages">
        <h2>Mensagens</h2>
        <ul class="msg-list" id="msg-list">
            <c:forEach var="m" items="${recent}">
                <li><time>${m.createdAt}</time><p>${m.body}</p></li>
            </c:forEach>
        </ul>
    </section>

    <section class="location">
        <h2>Posicao</h2>
        <div class="geolocate-row">
            <button class="btn ghost" type="button" id="geolocate-btn">Usar minha localizacao</button>
            <span class="geolocate-status" id="geolocate-status" aria-live="polite"></span>
        </div>
        <div id="cab-map" class="device-map"></div>
        <small class="coord-hint">Toque no mapa para reportar onde voce esta.</small>
        <c:set var="postUrl" value="${not empty cab.fleetId ? '/fleet/' : '/cab/'}${not empty cab.fleetId ? cab.fleetId : cab.id}/location"/>
        <form method="post" action="<c:url value='${postUrl}'/>" id="location-form" class="hidden-form">
            <input type="hidden" name="latitude" id="location-lat"/>
            <input type="hidden" name="longitude" id="location-lon"/>
        </form>
    </section>
</div>

<script type="text/javascript">
(function() {
    var url = "<c:url value='/api/messages/CAB/${cab.id}'/>";
    function poll() {
        $.getJSON(url, function(data) {
            for (var i = 0; i < data.length; i++) {
                var li = $("<li/>");
                li.append($("<time/>").text(new Date(data[i].createdAt).toLocaleTimeString()));
                li.append($("<p/>").text(data[i].body));
                $("#msg-list").prepend(li);
            }
        });
    }
    setInterval(poll, 5000);
})();

(function() {
    var mapEl = document.getElementById('cab-map');
    if (!mapEl || typeof L === 'undefined') return;
    var status = document.getElementById('geolocate-status');
    var btn = document.getElementById('geolocate-btn');
    var latInput = document.getElementById('location-lat');
    var lonInput = document.getElementById('location-lon');
    var form = document.getElementById('location-form');
    var snapshotUrl = '<c:url value="/api/cab-map/${cab.id}"/>';

    var DEFAULT_CENTER = [-30.0277, -51.2287];
    var map = L.map(mapEl).setView(DEFAULT_CENTER, 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap',
        maxZoom: 18
    }).addTo(map);
    setTimeout(function () { map.invalidateSize(); }, 80);

    var LETTERS = {
        cab:      { FREE: 'L', BUSY: 'O', OFFLINE: 'X' },
        dispatch: { REQUESTED: 'P', PROPOSED: 'C', ASSIGNED: 'A' }
    };
    function dotIcon(kind, status) {
        var letter = (LETTERS[kind] && LETTERS[kind][status]) || '?';
        return L.divIcon({ className: 'map-dot ' + kind + ' ' + status, html: letter, iconSize: [24, 24] });
    }

    var cabMarker = null;
    var pickupMarker = null;
    var hasCabPos = false;

    function placeCab(lat, lon, st) {
        var pos = [lat, lon];
        if (cabMarker) {
            cabMarker.setLatLng(pos);
            cabMarker.setIcon(dotIcon('cab', st));
        } else {
            cabMarker = L.marker(pos, { icon: dotIcon('cab', st) }).addTo(map).bindPopup('Voce esta aqui');
        }
        hasCabPos = true;
    }

    function placePickup(lat, lon, st, label) {
        var pos = [lat, lon];
        if (pickupMarker) {
            pickupMarker.setLatLng(pos);
            pickupMarker.setIcon(dotIcon('dispatch', st));
            pickupMarker.setPopupContent(label);
        } else {
            pickupMarker = L.marker(pos, { icon: dotIcon('dispatch', st) }).addTo(map).bindPopup(label);
        }
    }

    function clearPickup() {
        if (pickupMarker) { map.removeLayer(pickupMarker); pickupMarker = null; }
    }

    function fitToAll() {
        var points = [];
        if (cabMarker) points.push(cabMarker.getLatLng());
        if (pickupMarker) points.push(pickupMarker.getLatLng());
        if (points.length) map.fitBounds(L.latLngBounds(points), { padding: [40, 40], maxZoom: 16 });
    }

    function refresh(autofit) {
        $.getJSON(snapshotUrl, function (data) {
            var cab = data && data.cab;
            if (cab && cab.lat != null && cab.lon != null) {
                placeCab(cab.lat, cab.lon, cab.status);
            }
            var cd = data && data.currentDispatch;
            if (cd && cd.pickupLat != null) {
                var html = '<strong>' + (cd.pickupAddress || 'Pedido') + '</strong> <span class="badge ' + cd.status + '">' + cd.status + '</span>';
                if (cd.destinationAddress) html += '<br/>Destino: ' + cd.destinationAddress;
                if (cd.customerName) html += '<br/>Passageiro: ' + cd.customerName + (cd.customerPhone ? ' (' + cd.customerPhone + ')' : '');
                placePickup(cd.pickupLat, cd.pickupLon, cd.status, html);
            } else {
                clearPickup();
            }
            if (autofit) fitToAll();
        });
    }

    function submitLocation(lat, lon) {
        latInput.value = lat.toFixed(6);
        lonInput.value = lon.toFixed(6);
        if (status) status.textContent = 'Enviando posicao...';
        $.post(form.action, { latitude: lat.toFixed(6), longitude: lon.toFixed(6) }, function () {
            if (status) status.textContent = 'Posicao atualizada.';
            refresh(false);
        }).fail(function () {
            if (status) status.textContent = 'Nao consegui enviar. Tente novamente.';
        });
    }

    map.on('click', function (ev) {
        submitLocation(ev.latlng.lat, ev.latlng.lng);
    });

    if (btn && 'geolocation' in navigator) {
        btn.addEventListener('click', function () {
            status.textContent = 'Buscando GPS...';
            btn.disabled = true;
            navigator.geolocation.getCurrentPosition(function (pos) {
                btn.disabled = false;
                map.setView([pos.coords.latitude, pos.coords.longitude], 16);
                submitLocation(pos.coords.latitude, pos.coords.longitude);
            }, function () {
                btn.disabled = false;
                status.textContent = 'Nao consegui ler o GPS. Toque no mapa.';
            }, { enableHighAccuracy: true, timeout: 10000, maximumAge: 30000 });
        });
    } else if (btn) {
        btn.disabled = true;
        status.textContent = 'GPS nao disponivel.';
    }

    refresh(true);
    setInterval(function () { refresh(false); }, 8000);
})();
</script>
</body>
</html>
