import java.sql.*;
import java.util.Scanner;

public class booklist {
	Scanner scanner = new Scanner(System.in);
	Connection con;

	public booklist() {
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String userid = "c##madang"; /* 12버전 이상은 c##을 붙인다. */
		String pwd = "madang";

		try { /* 드라이버를 찾는 과정 */
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("드라이버 로드 성공");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try { /* 데이터베이스를 연결하는 과정 */
			System.out.println("데이터베이스 연결 준비 ...");
			con = DriverManager.getConnection(url, userid, pwd);
			System.out.println("데이터베이스 연결 성공");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void sqlRun() {
		System.out.print("조회할 고객의 이름을 입력하세요: ");
		String name = scanner.nextLine();

		String query = "SELECT address FROM CUSTOMER WHERE name = ?"; 
		try (PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();

			rs.next();
			System.out.println("이름: " + name);
			System.out.println("주소: " + rs.getString(1));
			
		} catch (SQLException e) {
			System.out.println("존재하지않거나 잘못된 이름입니다");
			return;
		}

		System.out.println("[" + name + "] 고객이 존재합니다. 주소를 수정하시겠습니까? (Y/N):");
		String answer = scanner.nextLine();
		if ((answer.toUpperCase()).equals("Y")) {
			System.out.println("수정할 주소를 입력하세요: ");
			String address = scanner.nextLine();
			String query1 = "UPDATE CUSTOMER SET ADDRESS = ? WHERE name = ?";
			
			try (PreparedStatement pstmt1 = con.prepareStatement(query1)) {
				pstmt1.setString(1, address);
				pstmt1.setString(2, name);
				int result = pstmt1.executeUpdate();
				
				if(result==1) {
					System.out.println("주소가 성공적으로 수정되었습니다. " + "(" + address + ")"); }
				
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("잘못된 응답입니다.");
			return;
		}

	}

	public static void main(String args[]) {
		booklist so = new booklist();
		while(true) {
			so.sqlRun();
		}
	}
}