<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Painel do motorista - myCAB</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
</head>
<body>
<div class="navbar"><a class="brand" href="<c:url value='/'/>">myCAB</a></div>
<div class="container">
    <h1>Bem-vindo, ${driver.fullName}</h1>
    <p>Categoria preferida: <strong>${driver.preferredCategory}</strong></p>
    <p>E-mail: ${driver.email}</p>
    <p>Telefone: ${driver.phone.value}</p>

    <form method="post" action="<c:url value='/logout'/>" style="margin-top: 24px">
        <button class="btn" type="submit">Sair</button>
    </form>
</div>
</body>
</html>
