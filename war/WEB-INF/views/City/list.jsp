<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<div>
		<ul>
			<c:if test="${not empty list}">
				<c:forEach var="obj" items="${list}">
					<li>
						<div>${obj.id}</div> ${obj.cityName}/${obj.state}
					</li>	
				</c:forEach>
			</c:if>
			<c:if test="${empty list}">
					<li>
						No cities available
					</li>
			</c:if>
		</ul>
	</div>
</body>
</html>