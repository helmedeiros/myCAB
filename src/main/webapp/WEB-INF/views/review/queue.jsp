<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Aprovacoes de motorista - myCAB</title>
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
    <h1>Aprovacoes pendentes</h1>
    <p>Cadastros novos esperando revisao e atribuicao de numero de frota.</p>
    <table class="table">
        <thead>
        <tr>
            <th>Motorista</th>
            <th>E-mail</th>
            <th>Telefone</th>
            <th>Categoria</th>
            <th>Veiculo</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="d" items="${pending}">
            <tr>
                <td>${d.fullName}</td>
                <td>${d.email}</td>
                <td>${d.phone.value}</td>
                <td>${d.preferredCategory}</td>
                <td>
                    <c:if test="${d.cab != null}">${d.cab.model.displayName} - ${d.cab.plate.value}</c:if>
                </td>
                <td><a class="btn primary" href="<c:url value='/review/${d.id}'/>">Revisar</a></td>
            </tr>
        </c:forEach>
        <c:if test="${empty pending}">
            <tr><td colspan="6">Nenhum cadastro pendente.</td></tr>
        </c:if>
        </tbody>
    </table>
</div>
</body>
</html>
