
<%@page import="com.qm.th.tg.factory.SearchSetFactory"%>
<%@page import="com.qm.th.tg.bean.SearchSet"%><html>
	<!-- InstanceBegin template="/Templates/new_处理提交.dwt.jsp" codeOutsideHTMLIsLocked="true" -->
	<%@ page language="java" import="java.sql.*"
		contentType="text/html;charset=gb2312"%>
	<%@page import="java.util.*"%>
	<%@page import="com.qm.mes.util.*"%>
    <%@page import="com.qm.mes.pm.bean.*,com.qm.mes.pm.factory.*,com.qm.mes.pm.dao.*"%>
<%@page import="org.apache.commons.logging.Log,org.apache.commons.logging.LogFactory"%>
    <%@page import="com.qm.mes.system.dao.*,com.qm.mes.framework.*"%>
	<!-- InstanceBeginEditable name="获得连接对象" -->
	<jsp:useBean id="Conn" scope="page" class="com.qm.th.helper.Conn_MES" />
	<%@page import="com.qm.mes.framework.*"%>
	<!-- InstanceEndEditable -->

	<%
		
	response.setHeader("Pragma","No-cache");  
   	response.setHeader("Cache-Control","no-cache");  
  	response.setDateHeader("Expires", 0); 
		Connection con = null;
		SearchSetFactory factory = new SearchSetFactory();
		SearchSet ss = new SearchSet();
		int intpage = 0;
		String info = null;
		try {
			//获取资源
			con = Conn.getConn();
			intpage = Integer.parseInt(request.getParameter("intpage"));
			info = request.getParameter("info");
			ss.setCsearchName(request.getParameter("cSearchname"));
			ss.setCwa(request.getParameter("cWA"));
			ss.setCfactory(request.getParameter("cFactory"));
			ss.setCdscFactory(request.getParameter("cDscFactory"));
			ss.setCcarType(request.getParameter("cCarType"));
			ss.setCdscCarType(request.getParameter("cDscCarType"));
			ss.setCremark(request.getParameter("cRemark"));
			factory.saveSearchSet(ss,con);
		} catch (Exception e) {
			throw e;
		}finally{
			if (con != null)
				con.close();//释放资源
		}
	%>
	<!-- InstanceEnd -->
<script type="text/javascript">
function back(){
	window.location.href="searchset_manage.jsp?page=<%=intpage%>&info=<%=info%>";
}back();
</script>
</html>