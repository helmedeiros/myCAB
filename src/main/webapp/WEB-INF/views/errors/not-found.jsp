<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Nao encontrado - myCAB</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
</head>
<body>
<div class="navbar"><a class="brand" href="<c:url value='/'/>">myCAB</a></div>
<div class="container narrow">
    <h1>Nao encontramos isso</h1>
    <p>${message}</p>
    <p><a class="btn primary" href="<c:url value='/'/>">Voltar ao inicio</a></p>
</div>
</body>
</html>
