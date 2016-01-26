<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="com.qm.th.terminal.Terminal"%>
<%@ page import="com.qm.th.helpers.Conn_MES"%>

<%
	Connection con = null;
	try {
		con = new Conn_MES().getConn();
		String car = request.getParameter("car");
		Terminal ter = new Terminal();
		int existnum = ter.getCarExist(con, car);
		response.getWriter().print(String.valueOf(existnum));
	} finally {

		if (con != null) {
			try {
				con.close();
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			} finally {
				con = null;
			}
		}
	}
%>