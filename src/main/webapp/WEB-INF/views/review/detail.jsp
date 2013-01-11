<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Revisar ${driver.fullName} - myCAB</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
</head>
<body>
<div class="navbar">
    <a class="brand" href="<c:url value='/'/>">myCAB</a>
    <ul class="nav">
        <li><a href="<c:url value='/operator'/>">Central</a></li>
        <li><a href="<c:url value='/cabs'/>">Frota</a></li>
        <li class="active"><a href="<c:url value='/review'/>">Aprovacoes</a></li>
        <li><a href="<c:url value='/drivers'/>">Motoristas</a></li>
        <li><a href="<c:url value='/customers'/>">Clientes</a></li>
    </ul>
</div>
<div class="container">
    <h1>${driver.fullName}</h1>
    <p>Status: <span class="badge ${driver.status}">${driver.status}</span></p>

    <div class="operator-grid">
        <section class="panel">
            <h2>Dados pessoais</h2>
            <form method="post" action="<c:url value='/review/${driver.id}/personal'/>">
                <label>Nome completo</label>
                <input type="text" name="fullName" value="${driver.fullName}" required/>

                <label>E-mail</label>
                <input type="email" name="email" value="${driver.email}" required/>

                <label>Telefone</label>
                <input type="text" name="phone" value="${driver.phone.value}" required/>

                <label>Numero da CNH</label>
                <input type="text" name="licenseNumber" value="${driver.licenseNumber}" required/>

                <label>Categoria preferida</label>
                <select name="preferredCategory" required>
                    <c:forEach var="cat" items="${categories}">
                        <option value="${cat}" ${cat == driver.preferredCategory ? 'selected' : ''}>${cat}</option>
                    </c:forEach>
                </select>

                <p><button class="btn" type="submit">Salvar dados pessoais</button></p>
            </form>
        </section>

        <section class="panel">
            <h2>Veiculo</h2>
            <c:choose>
                <c:when test="${driver.cab == null}">
                    <p>Sem veiculo cadastrado.</p>
                </c:when>
                <c:otherwise>
                    <form method="post" action="<c:url value='/review/${driver.id}/vehicle'/>">
                        <label>Placa</label>
                        <input type="text" name="plate" value="${driver.cab.plate.value}" required/>

                        <label>Modelo</label>
                        <select name="modelId" required>
                            <c:forEach var="m" items="${models}">
                                <option value="${m.id}" ${m.id == driver.cab.model.id ? 'selected' : ''}>${m.displayName} (${m.category})</option>
                            </c:forEach>
                        </select>

                        <p><button class="btn" type="submit">Salvar veiculo</button></p>
                    </form>
                </c:otherwise>
            </c:choose>
        </section>

        <section class="panel">
            <h2>Decisao</h2>
            <c:if test="${driver.status == 'PENDING'}">
                <form method="post" action="<c:url value='/review/${driver.id}/approve'/>">
                    <label>Numero de frota</label>
                    <input type="text" name="fleetId" placeholder="042" required/>
                    <p><button class="btn primary" type="submit">Aprovar</button></p>
                </form>
                <form method="post" action="<c:url value='/review/${driver.id}/reject'/>">
                    <p><button class="btn" type="submit">Recusar</button></p>
                </form>
            </c:if>
            <c:if test="${driver.status == 'ACTIVE' && driver.cab != null}">
                <p>Frota: <strong>${driver.cab.fleetId}</strong></p>
                <p>Dispositivo: <a href="<c:url value='/fleet/${driver.cab.fleetId}'/>">/fleet/${driver.cab.fleetId}</a></p>
            </c:if>
        </section>
    </div>

    <p><a class="btn" href="<c:url value='/review'/>">Voltar a fila</a></p>
</div>
</body>
</html>
