<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Cadastro recebido - myCAB</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
</head>
<body>
<div class="navbar"><a class="brand" href="<c:url value='/'/>">myCAB</a></div>
<div class="container narrow signup-success">
    <div class="success">Cadastro recebido com sucesso.</div>
    <h1>Aguarde aprovacao</h1>
    <p>A central vai revisar seus dados, conferir seu carro e atribuir um numero de frota. Voce recebera a notificacao por e-mail.</p>
    <p>Enquanto isso voce pode entrar em <a href="<c:url value='/login'/>">/login</a> para acompanhar.</p>
    <p><a class="btn primary" href="<c:url value='/'/>">Voltar ao inicio</a></p>
</div>
</body>
</html>
