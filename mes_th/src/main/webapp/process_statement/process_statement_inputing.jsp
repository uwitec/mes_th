<%@ page language="java" import="java.sql.*"
	contentType="text/html;charset=gb2312"%>
<jsp:useBean id="Conn" scope="page" class="com.qm.th.helper.Conn_MES" />
<%@page import="java.util.*"%>
<%@page import="com.qm.mes.framework.*"%>
<%
	 /*
	 * ҵ��������������������Ϣ��У���������Ƿ��ظ���������Ӧ�������
	 */

	//��ȡ�������
	String processid = request.getParameter("tfNPROCESSID");
	String processname = request.getParameter("tfCPROCESSNAME");
	String description = request.getParameter("tfCDESCRIPTION");
	String namespace = request.getParameter("tfCNAMESPACE");
    String intpage="";
	intpage=request.getParameter("intpage");
	if (processid == null || processid.trim().equals("")
			|| processname == null || processname.trim().equals("")
			|| description == null || description.trim().equals("")
			|| namespace == null || namespace.trim().equals("")) {
%>
<script>
<!-- 
alert("����Ϊ��");window.location.href='process_statement_input.jsp';
 -->
</script>
<%
	return;
	}

	Connection con = null;
	ExecuteResult er = null;
	ServiceException se = null;
	//String questiondesc="";

	List list = null;
	try {
		//��ȡ��Դ
		con = Conn.getConn();

		IServiceBus bus = ServiceBusFactory.getInstance();
		IMessage message = MessageFactory.createInstance();
		//�����û�����
		message.setUserParameter("processid", processid);
		message.setUserParameter("processname", processname);
		message.setUserParameter("description", description);
		message.setUserParameter("namespace", namespace);

		message.setOtherParameter("con", con);

		//����soa�е����� ��ʱ��Ϊ1 
		//todo
		er = bus.doProcess("10", message);

		//�ͷ���Դ
		if (con != null)
			con.close();

		//��ִ�н���Ĵ���
		if (er == ExecuteResult.sucess) {
%>
<script>
<!-- 
alert("�����ɹ���");window.location.href='process_statement_view.jsp?page=<%=intpage%>';
 -->
</script>
<%
			} else {
			list = message.getServiceException();
			if (list == null || list.size() == 0) {
%>
<script>
<!-- 
alert("����ʧ�ܣ�ԭ�������������Ա��ϵ��");window.location.href='process_statement_view.jsp?page=<%=intpage%>';
 -->
</script>
<%
		} else {
		se = (ServiceException) list.get(list.size() - 1);
%>
<script>
<!-- 
alert("����ʧ�ܣ�<%=se.getDescr().replaceAll("\n", "")%>");window.location.href='process_statement_view.jsp?page=<%=intpage%>';
 -->
</script>
<%
		}
		}

	} catch (Exception e) {
		//�ͷ���Դ
		if (con != null)
			con.close();
%>
<script>
<!--  
alert("�����쳣��<%=e.toString().replaceAll("\n", "")%>");window.location.href='process_statement_view.jsp?page=<%=intpage%>';
-->
</script>
<%
	throw e;
	}
%>