<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Criar conta - myCAB</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
</head>
<body>
<div class="navbar"><a class="brand" href="<c:url value='/'/>">myCAB</a></div>
<div class="container narrow">
    <h1>Criar conta de cliente</h1>
    <p>Cadastre-se para pedir taxi pelo aplicativo e acompanhar suas corridas.</p>
    <c:if test="${not empty error}">
        <div class="alert">${error}</div>
    </c:if>
    <form method="post" action="<c:url value='/customer-signup'/>">
        <label>Nome</label>
        <input type="text" name="name" required/>

        <label>E-mail</label>
        <input type="email" name="email" required/>

        <label>Telefone</label>
        <input type="text" name="phone" placeholder="(51) 99999-1234" required/>

        <label>Senha</label>
        <input type="password" name="password" minlength="6" required/>

        <label>Endereco padrao (opcional)</label>
        <input type="text" name="defaultAddress" placeholder="Rua, numero, bairro"/>

        <p>
            <button class="btn primary" type="submit">Criar conta</button>
            <a class="btn" href="<c:url value='/customer-login'/>">Ja tenho conta</a>
        </p>
    </form>
</div>
</body>
</html>
