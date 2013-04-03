<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Painel do motorista - myCAB</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
</head>
<body class="device">
<div class="device-frame">
    <header class="device-header">
        <span class="brand">myCAB</span>
        <c:if test="${driver.cab != null && not empty driver.cab.fleetId}">
            <span class="fleet-number">#${driver.cab.fleetId}</span>
        </c:if>
        <c:if test="${driver.cab == null || empty driver.cab.fleetId}">
            <span class="fleet-number pending">sem frota</span>
        </c:if>
        <span class="badge ${driver.status}">${driver.status}</span>
    </header>

    <section class="driver-greeting">
        <h2>Ola, ${driver.fullName}</h2>
    </section>

    <c:choose>
        <c:when test="${driver.status == 'PENDING'}">
            <section class="status-card pending">
                <h3>Aguardando aprovacao da central</h3>
                <p>Seu cadastro esta sendo revisado pela equipe. Voce sera notificado por e-mail assim que for aprovado e seu numero de frota for atribuido.</p>
                <c:if test="${driver.cab != null}">
                    <p>Veiculo cadastrado: <strong>${driver.cab.plate.value}</strong> - ${driver.cab.model.displayName}</p>
                </c:if>
            </section>
        </c:when>

        <c:when test="${driver.status == 'REJECTED'}">
            <section class="status-card rejected">
                <h3>Cadastro nao aprovado</h3>
                <p>A central revisou seu cadastro e nao pode aprova-lo no momento. Entre em contato com o suporte para mais informacoes.</p>
            </section>
        </c:when>

        <c:when test="${driver.status == 'ACTIVE'}">
            <section class="status-card active">
                <h3>Voce esta na frota</h3>
                <p>Frota <strong>#${driver.cab.fleetId}</strong> &middot; ${driver.cab.model.displayName} &middot; <strong>${driver.cab.plate.value}</strong></p>
                <p>Status do carro:
                    <span class="badge ${driver.cab.status}">${driver.cab.status}</span>
                </p>
                <c:choose>
                    <c:when test="${driver.cab.status == 'OFFLINE'}">
                        <form method="post" action="<c:url value='/driver/online'/>">
                            <button class="btn primary" type="submit">Ficar disponivel</button>
                        </form>
                    </c:when>
                    <c:when test="${driver.cab.status == 'FREE'}">
                        <p>Aguardando proxima chamada da central.</p>
                        <form method="post" action="<c:url value='/driver/offline'/>">
                            <button class="btn" type="submit">Sair da fila</button>
                        </form>
                    </c:when>
                </c:choose>
            </section>

            <c:if test="${proposal != null}">
                <section class="pickup-card proposal-card">
                    <h3>Nova corrida disponivel</h3>
                    <p class="trip"><strong>${proposal.pickupAddress}</strong> &rarr; <strong>${proposal.destinationAddress}</strong></p>
                    <p>Categoria: <strong>${proposal.requestedCategory}</strong></p>
                    <p class="muted">Aceite para ver os dados do passageiro.</p>
                    <div class="proposal-actions">
                        <form method="post" action="<c:url value='/driver/proposal/${proposal.id}/accept'/>" style="display:inline">
                            <button class="btn primary" type="submit">Aceitar</button>
                        </form>
                        <form method="post" action="<c:url value='/driver/proposal/${proposal.id}/decline'/>" style="display:inline">
                            <button class="btn" type="submit">Recusar</button>
                        </form>
                    </div>
                </section>
            </c:if>

            <c:if test="${currentDispatch != null}">
                <section class="pickup-card">
                    <h3>Proximo passageiro</h3>
                    <p class="customer-name">${currentDispatch.customer.name}</p>
                    <p class="customer-phone">${currentDispatch.customer.phone.value}</p>
                    <p class="pickup-address">
                        <strong>Endereco:</strong>
                        <c:choose>
                            <c:when test="${not empty currentDispatch.pickupAddress}">${currentDispatch.pickupAddress}</c:when>
                            <c:otherwise>${currentDispatch.pickup.latitude}, ${currentDispatch.pickup.longitude}</c:otherwise>
                        </c:choose>
                    </p>
                    <form method="post" action="<c:url value='/driver/complete'/>">
                        <input type="hidden" name="dispatchId" value="${currentDispatch.id}"/>
                        <button class="btn primary" type="submit">Finalizar corrida</button>
                    </form>
                </section>
            </c:if>

            <c:if test="${currentDispatch == null && driver.cab.status == 'FREE'}">
                <section class="status-card waiting">
                    <p>Sem corrida atribuida. A central vai te avisar pelo dispositivo quando houver uma.</p>
                    <p><a class="btn" href="<c:url value='/fleet/${driver.cab.fleetId}'/>">Abrir tela do carro</a></p>
                </section>
            </c:if>
        </c:when>
    </c:choose>

    <form method="post" action="<c:url value='/logout'/>" class="logout-row">
        <button class="btn" type="submit">Sair</button>
    </form>
</div>

<c:if test="${driver.status == 'ACTIVE'}">
<script type="text/javascript">
(function() {
    var driverId = ${driver.id};
    function checkForDispatch() {
        if (window.location.search.indexOf('reload') === -1 ||
            performance.now() < 500) {
            return;
        }
    }
    setInterval(function() { window.location.reload(); }, 20000);
})();
</script>
</c:if>
</body>
</html>
