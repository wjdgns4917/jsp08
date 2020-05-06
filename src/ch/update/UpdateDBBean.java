package ch.update;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import work.crypt.BCrypt;
import work.crypt.SHA256;

public class UpdateDBBean {
	private static UpdateDBBean instance=new UpdateDBBean();
	public static UpdateDBBean getInstance() {
		return instance;
	}
	public UpdateDBBean() {
		// TODO Auto-generated constructor stub
	}
	private Connection getConnection() throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		
		String url="jdbc:oracle:thin:@localhost:1521:orcl";
		String user="scott";
		String pw="123456";
		Connection con=DriverManager.getConnection(url,user,pw);

		return con;

	}
	//member2테이블의 내용을 얻어,  cryptProcessList.jsp에 전달
	public List<UpdateDataBean> getMember() throws Exception {
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		List<UpdateDataBean> memberList=null;
		
		int x=0;
		try {

			conn=getConnection();
			String sql="select count(*) from member2";
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next())
				x=rs.getInt(1);
			System.out.println("xxxxx : "+x);
			
			sql="select id,passwd from member2";
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				memberList=new ArrayList<UpdateDataBean>();
				do {
					UpdateDataBean member=new UpdateDataBean();
					member.setId(rs.getString("id"));
					member.setPasswd(rs.getString("passwd"));
					memberList.add(member);
				} while (rs.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs!=null) {try {rs.close();	} catch (Exception e2) {} }
			if (pstmt!=null) {try {pstmt.close();	} catch (Exception e2) {} }
			if (conn!=null) {try {conn.close();	} catch (Exception e2) {} }
			
		}
		return memberList;
	}
	public void updateMember() {
		//암호화 진행
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		SHA256 sha=SHA256.getInsatnce();
		
		try {
			conn=getConnection();
			String sql="select id,passwd from member2";
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			while (rs.next()) {
				String id=rs.getString("id");
				String orgPass=rs.getString("passwd");
				
				//SHA256클래스의  getSha256()사용해
				//비밀번호를 SHA방식으로 암호화
				String shaPass=sha.getSha256(orgPass.getBytes());
				System.out.println("shaPass : "+shaPass);
				//SHA256방식으로 암호화된 값을 다시 BCrypt 방식의 
				//hashpw()방식으로 bcrypt암호화 처리
				//BCrypt.getsolt()메소드는 salt값을 난수를 사용해 생성
				String salt1=BCrypt.gensalt();
//				String salt1="$2a$10$TSa3zViBHmFOZtxkX6hVUO";
				String bcPass=BCrypt.hashpw(shaPass, salt1);
				System.out.println("salt : "+salt1);
				
				sql="update member2 set passwd=?,salt=? where id=?";
				pstmt=conn.prepareStatement(sql);
				pstmt.setString(1, bcPass);
				pstmt.setString(2, salt1);
				pstmt.setString(3, id);
				
				pstmt.executeUpdate();
			}		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs!=null) {try {rs.close();	} catch (Exception e2) {} }
			if (pstmt!=null) {try {pstmt.close();	} catch (Exception e2) {} }
			if (conn!=null) {try {conn.close();	} catch (Exception e2) {} }
		}

	}
	

}
