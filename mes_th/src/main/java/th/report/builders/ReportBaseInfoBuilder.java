package th.report.builders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;

import th.pz.bean.PrintSet;
import th.report.api.IReportBaseInfoBuilder;
import th.report.entities.ReportBaseInfo;
import th.report.entities.RequestParam;

/**
 * ����������Ϣ
 * 
 * @author Ajaxfan
 */
public class ReportBaseInfoBuilder implements IReportBaseInfoBuilder {
	private Connection conn;
	private PrintSet printSet;
	private RequestParam requestParam;
	private ReportBaseInfo reportBaseInfo;

	/**
	 * ���캯��
	 * 
	 * @param conn
	 * @param printSet
	 * @param requestParam
	 */
	public ReportBaseInfoBuilder(Connection conn, PrintSet printSet, RequestParam requestParam) {
		this.conn = conn;
		this.printSet = printSet;
		this.requestParam = requestParam;

		this.reportBaseInfo = new ReportBaseInfo();
	}

	/**
	 * ������װ��Ϣ
	 */
	public void buildDAInfo() {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.prepareStatement("SELECT dabegin, cseqno_a FROM cardata WHERE cvincode = ?");
			stmt.setString(1, printSet.getCLastVin());

			rs = stmt.executeQuery();
			if (rs.next()) {
				reportBaseInfo.setDabegin(rs.getString("dabegin"));// ��װ����ʱ��
				reportBaseInfo.setDaseqa(rs.getString("cseqno_a"));// ��װ˳���
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					rs = null;
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					stmt = null;
				}
			}
		}
	}

	/**
	 * �����ܺ���Ϣ
	 */
	public void buildMaxCarNo() {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.prepareStatement("SELECT max(iCarNo) FROM print_data WHERE iPrintGroupId= ? AND cremark = ?");
			stmt.setString(1, requestParam.getGroupId());
			stmt.setString(2, requestParam.getRequestDate());

			rs = stmt.executeQuery();
			if (rs.next()) {
				reportBaseInfo.setCarno(rs.getInt(1) + 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					rs = null;
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					stmt = null;
				}
			}
		}
	}

	/**
	 * �������ID
	 */
	public void buildTFassId() {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.prepareStatement("SELECT id FROM tfassname WHERE ctfassname = ?");
			stmt.setString(1, printSet.getCTFASSName());

			rs = stmt.executeQuery();
			if (rs.next()) {
				reportBaseInfo.setTfassId(rs.getString("id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					rs = null;
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					stmt = null;
				}
			}
		}
	}

	/**
	 * ��������
	 */
	public void buildMaxPageNo() {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.prepareStatement("SELECT MAX(cpageNo) FROM print_data WHERE iPrintGroupId = ? AND cremark = ?");
			stmt.setString(1, requestParam.getGroupId());
			stmt.setString(2, requestParam.getRequestDate());

			rs = stmt.executeQuery();
			if (rs.next()) {
				String pageNo = rs.getString(1);

				if (pageNo != null && pageNo.trim().length() > 12) {
					reportBaseInfo.setPageNo(Integer.valueOf(pageNo.substring(8, 12)));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					rs = null;
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					stmt = null;
				}
			}
		}
	}

	/**
	 * �������̺�
	 */
	public void buildChassisNumber() {
		StringBuilder chassisNumber = new StringBuilder();
		chassisNumber.append(requestParam.getRequestDate().replaceAll("-", ""));
		chassisNumber.append(new DecimalFormat("00").format(printSet.getId()));
		chassisNumber.append(new DecimalFormat("0000").format(Math.max(1, reportBaseInfo.getPageNo()) + 1));

		reportBaseInfo.setChassisNumber(chassisNumber.toString());
	}

	/**
	 * ����Vin�ͳ��͵Ķ�Ӧ��ϵ
	 */
	public void buildVinAndCarTypePair() {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.prepareStatement("SELECT ctype, clastvin FROM printSetVin WHERE printid = ?");
			stmt.setInt(1, printSet.getId());

			rs = stmt.executeQuery();
			while (rs.next()) {
				reportBaseInfo.putVinMap2CarType(rs.getString("ctype"), rs.getString("clastvin"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					rs = null;
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					stmt = null;
				}
			}
		}
	}

	/**
	 */
	public ReportBaseInfo getReportBaseInfo() {
		return reportBaseInfo;
	}
}