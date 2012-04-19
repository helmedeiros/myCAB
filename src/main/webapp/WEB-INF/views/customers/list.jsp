<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Clientes - myCAB</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
</head>
<body>
<div class="navbar">
    <a class="brand" href="<c:url value='/'/>">myCAB</a>
    <ul class="nav">
        <li><a href="<c:url value='/operator'/>">Central</a></li>
        <li><a href="<c:url value='/cabs'/>">Frota</a></li>
        <li class="active"><a href="<c:url value='/customers'/>">Clientes</a></li>
    </ul>
</div>
<div class="container">
    <h1>Clientes</h1>
    <form method="get" action="<c:url value='/customers'/>" class="search">
        <input type="text" name="q" placeholder="Buscar por nome" value="${query}"/>
        <button class="btn" type="submit">Buscar</button>
        <a class="btn primary" href="<c:url value='/customers/new'/>">Novo cliente</a>
    </form>
    <table class="table">
        <thead>
        <tr>
            <th>Nome</th>
            <th>Telefone</th>
            <th>Endereco padrao</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="c" items="${customers}">
            <tr>
                <td>${c.name}</td>
                <td>${c.phone.value}</td>
                <td>${c.defaultAddress}</td>
                <td>
                    <a class="btn" href="<c:url value='/customers/${c.id}/edit'/>">Editar</a>
                    <form method="post" action="<c:url value='/customers/${c.id}/delete'/>" style="display:inline">
                        <button class="btn" type="submit">Remover</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty customers}">
            <tr><td colspan="4">Nenhum cliente encontrado.</td></tr>
        </c:if>
        </tbody>
    </table>
</div>
</body>
</html>
