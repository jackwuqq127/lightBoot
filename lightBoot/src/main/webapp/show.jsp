<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>

<%
	List<Map<String, Object>> list=(List<Map<String, Object>>)request.getAttribute("list");
	application.setAttribute("test","contextTest");
%>

<body>
	<table border="1">
	<c:forEach items="${list}" var="li">
		<tr>
			<td>${li.empno }</td>
			<td>${li.ename }</td>
			<td>${li.hiredate }</td>
		</tr>
	</c:forEach>
	</table>
</body>
</html>