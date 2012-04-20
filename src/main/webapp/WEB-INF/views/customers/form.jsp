<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Novo cliente - myCAB</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
</head>
<body>
<div class="navbar"><a class="brand" href="<c:url value='/'/>">myCAB</a></div>
<div class="container narrow">
    <h1>Novo cliente</h1>
    <form method="post" action="<c:url value='/customers'/>">
        <label>Nome</label>
        <input type="text" name="name" required/>

        <label>Telefone</label>
        <input type="text" name="phone" placeholder="(51) 99999-1234" required/>

        <label>Endereco padrao</label>
        <input type="text" name="defaultAddress" placeholder="Rua, numero, bairro"/>

        <p>
            <button class="btn primary" type="submit">Cadastrar</button>
            <a class="btn" href="<c:url value='/customers'/>">Cancelar</a>
        </p>
    </form>
</div>
</body>
</html>
