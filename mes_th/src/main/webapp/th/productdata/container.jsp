<%@ page contentType="text/html;charset=GBK" language="java" pageEncoding="GBK"%>
<html>
	<head>
		<meta http-equiv=content-type content="text/html;charset=GBK">
	</head>
	
	<!-- 不能使用BODY标签 -->
	<frameset COLS="70%,30%"  id="left" border="0">
		<frame NAME="middle" ID="middle" src="specKin.jsp" marginwidth="0" marginheight="0" noresize>
	    <frameset ROWS="65, 35" id="left" border="0">
			<frame NAME="middle" ID="middle" src="processed.jsp" marginwidth="0" marginheight="0" scrolling="no" noresize>
			<frame NAME="middle" ID="middle" src="productDataError.jsp" marginwidth="0" marginheight="0" scrolling="no" noresize>
		</frameset>
	</frameset>
</html>