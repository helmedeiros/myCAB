<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>myCAB - seu amigo com um carro</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/home.css'/>"/>
</head>
<body class="home">
<div class="home-wrap">
    <h1 class="hero">Seu amigo com um carro</h1>

    <a class="cta apple" href="#"><span class="logo"></span>Baixar para iPhone</a>
    <a class="cta android" href="#"><span class="logo"></span>Baixar para Android</a>

    <div class="or"><span>ou</span></div>

    <h2 class="signup">Cadastre-se como motorista</h2>

    <div class="balloon">
        <svg viewBox="0 0 120 220" xmlns="http://www.w3.org/2000/svg" width="120" height="220" aria-hidden="true">
            <ellipse cx="60" cy="70" rx="48" ry="58" fill="#f15a89"/>
            <ellipse cx="42" cy="48" rx="14" ry="9" fill="#ffffff" opacity="0.5"/>
            <path d="M55 100 q5 14 10 0" fill="none" stroke="#ffffff" stroke-width="3" stroke-linecap="round"/>
            <circle cx="42" cy="78" r="5" fill="#ffffff"/>
            <circle cx="78" cy="78" r="5" fill="#ffffff"/>
            <polygon points="56,128 64,128 60,140" fill="#f15a89"/>
            <line x1="60" y1="140" x2="60" y2="185" stroke="#cccccc" stroke-width="1"/>
            <rect x="40" y="185" width="40" height="22" rx="3" fill="#4cbfb3"/>
            <circle cx="48" cy="210" r="4" fill="#2d2d2d"/>
            <circle cx="72" cy="210" r="4" fill="#2d2d2d"/>
        </svg>
        <a class="signup-link" href="<c:url value='/signup'/>">Comecar cadastro</a>
    </div>

    <p class="safety">
        <a href="#"><span class="shield"></span>Aprenda sobre seguranca</a>
    </p>

    <footer>
        <a href="#">Seguranca</a>
        <span>&middot;</span>
        <a href="#">Vagas</a>
        <span>&middot;</span>
        <a href="#">Imprensa</a>
        <span>&middot;</span>
        <a href="#">Blog</a>
        <span>&middot;</span>
        <a href="<c:url value='/signup'/>">Seja motorista</a>
        <span>&middot;</span>
        <a href="#">Ajuda</a>
        <span>&middot;</span>
        <a href="#">@myCAB</a>
    </footer>
</div>
</body>
</html>
