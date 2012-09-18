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
    <h1>Pronto para rodar</h1>
    <p>A central entrara em contato para os proximos passos.</p>
    <p><a class="btn primary" href="<c:url value='/'/>">Voltar ao inicio</a></p>
</div>
</body>
</html>
