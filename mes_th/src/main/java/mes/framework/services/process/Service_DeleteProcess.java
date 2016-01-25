package mes.framework.services.process;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import mes.framework.DataBaseType;
import mes.framework.DefService;
import mes.framework.ExecuteResult;
import mes.framework.IMessage;
import mes.framework.IProcess;
import mes.framework.IService;
import mes.framework.ProcessFactory;
import mes.framework.ServiceException;
import mes.framework.ServiceExceptionType;
import mes.framework.dao.DAOFactory_Core;
import mes.framework.dao.IDAO_Core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 删除流程服务 输入依赖：需要在otherparameter中放置一个Connection对象，名为con。<br>
 * 需要在用户输入中含有processid,sid值 <br>
 * 流程服务执行 doService() <br>
 * 流程服务初始化 initFordo() <br>
 * 流程服务回退 undoService() <br>
 * 输出：无输出
 * 
 * @author 于丽达 2007-12-25
 * 
 */
public class Service_DeleteProcess extends DefService implements IService {

	private Connection con = null;

	private String processid = null;

	private String sid = null;

	private final Log log = LogFactory.getLog(Service_DeleteProcess.class);

	public ExecuteResult doService(IMessage message, String soa_processid) {
		// 获取运行流程项
		IProcess process = ProcessFactory.getInstance(soa_processid);
		String processInfo = process.getNameSpace() + "." + process.getName();
		if (!initFordo(message, soa_processid)) {
			log.error(processInfo + ",删除流程服务初始化加载失败");
			return ExecuteResult.fail;
		}

		try {
			Statement stmt = null;
			ResultSet rs = null;
			try {
				IDAO_Core dao = DAOFactory_Core.getInstance(DataBaseType
						.getDataBaseType(con));
				if (dao == null) {
					message.addServiceException(new ServiceException(
							ServiceExceptionType.UNKNOWNDATABASETYPE, "dao为空",
							this.getId(), soa_processid, new Date(), null));
					log.error(processInfo + ",删除流程服务时,数据库加载错误,未知的数据库类型");
					return ExecuteResult.fail;
				}
				log.debug(processInfo + ",删除流程服务,数据库加载成功");
				stmt = con.createStatement();
				String sql_check = dao
						.getSQL_QueryCountProcessServerForProcessidAndSid(
								processid, sid);
				log.debug("检查流程服务表是否有重复数据的sql语句: sql_check = " + sql_check);
				rs = stmt.executeQuery(sql_check);
				int count = 0;
				if (rs.next())
					count = rs.getInt(1);
				rs.close();

				if (count == 0) {
					message.addServiceException(new ServiceException(
							ServiceExceptionType.UNKNOWN, "要删除的数据不存在", this
									.getId(), soa_processid, new Date(), null));
					log.error(processInfo + ",删除流程服务,要删除的数据不存在  count = "
							+ count);
					return ExecuteResult.fail;
				} else {
					Map<String, String> data = new HashMap<String, String>();
					String map_processid = null;
					String map_sid = null;
					String map_serverid = null;
					String map_aliasname = null;
					String map_actid = null;
					String data_sql_select = dao
							.getSQL_QueryProcessServerInfoForProcessidAndSid(
									processid, sid);
				
					log.debug(processInfo
							+ "查询此流程号,运行号的所有信息的sql语句: data_sql_select = "
							+ data_sql_select);
					rs = stmt.executeQuery(data_sql_select);
					while (rs.next()) {
						map_processid = rs.getString(1);
						map_sid = rs.getString(2);
						map_serverid = rs.getString(3);
						map_aliasname = rs.getString(4);
						map_actid = rs.getString(5);
					}
					String debug_result_map = "select_map_processid = "
							+ map_processid + "\n" + "select_map_sid = "
							+ map_sid + "\n" + "select_map_serverid = "
							+ map_serverid + "\n" + "select_map_aliasname = "
							+ map_aliasname + "\n" + "select_map_actid = "
							+ map_actid;
					log.debug(processInfo + " , " + this.getClass().getName()
							+ "查询此流程号,运行号的所有信息是: debug_result_map = "
							+ debug_result_map);
					data.put("map_processid", map_processid);
					data.put("map_sid", map_sid);
					data.put("map_serverid", map_serverid);
					data.put("map_aliasname", map_aliasname);
					data.put("map_actid", map_actid);
					message.setOtherParameter(this.getClass().getName(), data);
					/*
					 * 删除流程服务
					 */
					String sql_delete = dao
							.getSQL_DeleteProcess(processid, sid);
					log.debug("删除流程服务的sql语句: sql_delete = " + sql_delete);
					stmt.executeUpdate(sql_delete);
				}

			} catch (SQLException sqle) {
				message.addServiceException(new ServiceException(
						ServiceExceptionType.DATABASEERROR, "数据库错误" + sqle,
						this.getId(), soa_processid, new Date(), sqle));
				log.fatal(processInfo + ",删除流程服务,数据库异常" + sqle.toString());
				return ExecuteResult.fail;
			} finally {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			}
		} catch (Exception e) {
			message.addServiceException(new ServiceException(
					ServiceExceptionType.UNKNOWN, e.toString(), this.getId(),
					soa_processid, new java.util.Date(), e));
			log.fatal(processInfo + ",删除流程服务,未知异常" + e.toString());
			return ExecuteResult.fail;
		}
		return ExecuteResult.sucess;
	}

	public Service_DeleteProcess() {
		super();
	}

	private boolean initFordo(IMessage message, String soa_processid) {
		con = null;
		// 流程号
		processid = null;
		// 运行号
		sid = null;
		con = (Connection) message.getOtherParameter("con");
		processid = message.getUserParameterValue("processid");
		sid = message.getUserParameterValue("sid");
		// 获取运行流程项
		IProcess process = ProcessFactory.getInstance(soa_processid);
		String processInfo = process.getNameSpace() + "." + process.getName();
		String debug_DeleteProcess = "processid = " + processid + "\n"
				+ "sid = " + sid + "\n";
		log.debug(processInfo + ",删除流程服务时用户提交的参数: " + debug_DeleteProcess);

		if (processid == null || sid == null) {
			message.addServiceException(new ServiceException(
					ServiceExceptionType.PARAMETERLOST, "deleteprocess参数为空",
					this.getId(), soa_processid, new java.util.Date(), null));
			log.error(processInfo + ",删除流程服务时,缺少输入参数");
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public ExecuteResult undoService(IMessage message, String soa_processid) {
		IProcess process = ProcessFactory.getInstance(soa_processid);
		String processInfo = process.getNameSpace() + "." + process.getName();
		Object obj = message.getOtherParameter(this.getClass().getName());
		if (obj instanceof Map) {
			Map<String, String> map = (Map<String, String>) obj;
			String map_processid = map.get("map_processid");
			String map_sid = map.get("map_sid");
			String map_serverid = map.get("map_serverid");
			String map_aliasname = map.get("map_aliasname");
			String map_actid = map.get("map_actid");
			String debug_map = "map_processid = " + map_processid + "\n"
					+ "map_sid = " + map_sid + "\n" + "map_serverid = "
					+ map_serverid + "\n" + "map_aliasname = " + map_aliasname
					+ "\n" + "map_actid = " + map_actid;
			log.debug(processInfo + " , " + this.getClass().getName()
					+ "删除流程服务中回退操作Map接收到的值: debug_map = " + debug_map);
			try {
				Statement stmt = null;
				try {
					IDAO_Core dao = DAOFactory_Core.getInstance(DataBaseType
							.getDataBaseType(con));
					if (dao == null) {
						message.addServiceException(new ServiceException(
								ServiceExceptionType.UNKNOWNDATABASETYPE,
								"dao为空", this.getId(), soa_processid,
								new Date(), null));
						log
								.error(processInfo
										+ ",删除流程服务回退操作时,数据库加载错误,未知的数据库类型");
						return ExecuteResult.fail;
					}
					log.debug(processInfo + ",删除流程服务回退操作,数据库加载成功");
					stmt = con.createStatement();
					String undo_sql_insert = dao.getSQL_InsertProcess(
							map_processid, map_sid, map_serverid,
							map_aliasname, map_actid);
					log.debug(processInfo + " , " + this.getClass().getName()
							+ "删除流程服务回退操作的sql语句: undo_sql_insert = "
							+ undo_sql_insert);
					stmt.executeUpdate(undo_sql_insert);
				} catch (SQLException sqle) {
					message.addServiceException(new ServiceException(
							ServiceExceptionType.DATABASEERROR, "数据库错误" + sqle,
							this.getId(), soa_processid, new Date(), sqle));
					log.fatal(processInfo + ",删除流程服务回退操作,数据库异常"
							+ sqle.toString());
					return ExecuteResult.fail;
				} finally {
					if (stmt != null)
						stmt.close();
				}
			} catch (Exception e) {
				message.addServiceException(new ServiceException(
						ServiceExceptionType.UNKNOWN, e.toString(), this
								.getId(), soa_processid, new java.util.Date(),
						e));
				log.fatal(processInfo + ",删除流程服务回退操作,未知异常" + e.toString());
				return ExecuteResult.fail;
			}
		}
		return ExecuteResult.sucess;
	}
}
