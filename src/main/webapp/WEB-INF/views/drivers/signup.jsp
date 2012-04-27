<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Seja um motorista myCAB</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
</head>
<body>
<div class="navbar">
    <a class="brand" href="<c:url value='/'/>">myCAB</a>
</div>
<div class="container narrow signup">
    <h1>Seja um motorista</h1>
    <p class="lead">Cadastre-se e comece a receber chamadas pela central.</p>
    <c:if test="${not empty error}">
        <div class="alert">${error}</div>
    </c:if>
    <form method="post" action="<c:url value='/signup'/>">
        <label>Nome completo</label>
        <input type="text" name="fullName" required/>

        <label>E-mail</label>
        <input type="email" name="email" required/>

        <label>Telefone</label>
        <input type="text" name="phone" placeholder="(51) 99999-1234" required/>

        <label>Numero da CNH</label>
        <input type="text" name="licenseNumber" required/>

        <label>Senha</label>
        <input type="password" name="password" required/>

        <label>Categoria preferida</label>
        <select name="preferredCategory" required>
            <c:forEach var="cat" items="${categories}">
                <option value="${cat}">${cat}</option>
            </c:forEach>
        </select>

        <p>
            <button class="btn primary" type="submit">Cadastrar</button>
            <a class="btn" href="<c:url value='/'/>">Voltar</a>
        </p>
    </form>
</div>
</body>
</html>
