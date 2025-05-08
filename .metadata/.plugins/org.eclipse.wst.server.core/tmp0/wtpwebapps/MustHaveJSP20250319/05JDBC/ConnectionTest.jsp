<%@page import="common.DBConnPool"%>
<%@page import="common.JDBConnect"%> <!-- JDBConnect 임포트 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>JDBC</title>
	</head>
	<body>
		<h2>JDBC 테스트 1</h2>
		<%
		JDBConnect jdbc1 = new JDBConnect();  // 객체를 생성
		jdbc1.close();	// 자원 해제
		%>
		
		<h2>JDBC 테스트 2</h2>
		<%
		// application내장객체를 통해 컨테스트 초기화 파라미터(getInitParameter)를 얻어 온다.
		// (web.xml의 컨텍스트 초기화 매개변수를 가져옴)
		String driver = application.getInitParameter("OracleDriver");
		String url = application.getInitParameter("OracleURL");
		String id = application.getInitParameter("OracleId");
		String pwd = application.getInitParameter("OraclePwd");
		JDBConnect jdbc2 = new JDBConnect(driver, url, id, pwd);  // 객체를 생성
		jdbc2.close();	// 자원 해제
		%>
		
		<h2>JDBC 테스트 3</h2>
		<%
		JDBConnect jdbc3 = new JDBConnect(application);  // 객체를 생성
		jdbc3.close();	// 자원 해제
		%>
		
		<h2>커넥션 풀 테스트 </h2>
		<%
		DBConnPool pool = new DBConnPool();  // 객체를 생성
		pool.close();	// 자원 해제
		%>
	</body>
</html>