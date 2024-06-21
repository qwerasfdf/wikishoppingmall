package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import shop.ShopUserDAO;
import shop.ShopUserDTO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

@WebServlet("/CtrlMember.do")
public class CtrlMember extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CtrlMember() {
	}

	public void init(ServletConfig config) throws ServletException {
	}

	public void destroy() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		execute(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		execute(request, response);
	}

	protected void execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 글자 깨짐 방지
		request.setCharacterEncoding("utf-8");
		/*
		 * Html로 전송 시 : text/html 일반 텍스트로 전송 시 : text/plain XML 데이터로 전송 시 :
		 * application/xml
		 */
		response.setContentType("text/plain;charset=utf-8");

		// 객체 생성 및 가져오기
		// String contextPath = request.getContextPath(); // >> forward 있어서 사용 안함
		String realPath = "/shopFrame/jsp/";
		String page = request.getParameter("page");
		String msg = request.getParameter("msg");
		String search = request.getParameter("search");
		String shop_id = request.getParameter("shop_id");// sms.jsp에 name
		String shop_phone = request.getParameter("shop_phone");// sms.jsp에 name

		String user_id = request.getParameter("user_id");
		String user_pwd = request.getParameter("user_pwd");
		String user_name = request.getParameter("user_name");
		String user_email = request.getParameter("user_email");
		String user_phone = request.getParameter("user_phone");
		String user_addr = request.getParameter("user_addr");
		String user_rank = request.getParameter("user_rank");
		
		String column = request.getParameter("column");
		String up_column = request.getParameter("up_column");
		String userDel = request.getParameter("userDel");
		String check = request.getParameter("check");
		
		Reader reader = Resources.getResourceAsReader("mybatis/mybatis-config.xml");
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session1 = sqlSessionFactory.openSession();
		request.setAttribute("session1", session1);
		session1.commit();
		
		ArrayList<ShopUserDTO> userArr = new ArrayList<ShopUserDTO>();
		
		String phone = request.getParameter("phone");

		String log = "";
		int su = 0;

		// DTO , DAO
		ShopUserDAO userDAO = new ShopUserDAO();
		ShopUserDTO userDTO = new ShopUserDTO(user_id , user_pwd , user_name , user_phone , user_email , user_addr , user_rank);
 
		ShopUserDTO userDTOmem = new ShopUserDTO(user_id, user_pwd, user_name, user_phone, user_email, user_addr,
				user_rank);
		PrintWriter out = response.getWriter();

		// 세션 객체 생성
		HttpSession session = request.getSession();

		// Page ====================================

		// 메인 페이지 이동
		if (page.equals("pageMain")) {
			realPath = "/CtrlPd.do?page=pageMain";
		}
		// 로그인 페이지 이동 및 로그아웃
		else if (page.equals("pageLog") || page.equals("pageLogout")) {
			// 로그아웃
			if (page.equals("pageLogout")) {
				session.invalidate();
				realPath = "/CtrlPd.do?page=pageMain";
			}
			// 로그인
			else {
				realPath += "shop_login.jsp";
			}
		}
		// 마이페이지 이동  /  마이바티스 적용
		else if (page.equals("pageMy")) {
			realPath += "shop_myPage.jsp";
			
			ArrayList<Object> myPage = new ArrayList<Object>();
			String id = (String)session.getAttribute("user_id");
			userDTO = new ShopUserDTO();
			userDTO.setUser_id(id);
			
			userArr = (ArrayList)session1.selectList("userMyPageId", userDTO);
			
			String rank[] = userArr.get(0).getUser_rank().split("1");
			 
			myPage.add(rank[0]);
			myPage.add(rank[1]);
			
			/*광고 동의 선택 시 실행*/
			if(check!=null){
				  //userDTO = new ShopUserDTO(id, "user_rank", rank[0] + "1" + check);
				  userDTO = new ShopUserDTO();
				  userDTO.setUser_id(id);
				  userDTO.setUser_pwd("user_rank");
				  userDTO.setUser_name(rank[0] + "1" + check);
				  su = session1.update("userMyPageUp",userDTO);
				  myPage.set(1, check);
			  }
			
			  /*변경할 시 실행*/
			  if(column!=null || up_column!=null){
				  if(column!="" || up_column!="") {
					//userDTO = new ShopUserDTO(id, column, up_column);
					userDTO = new ShopUserDTO();
					userDTO.setUser_id(id);
					userDTO.setUser_pwd(column);
					userDTO.setUser_name(up_column);
				  	su = session1.update("userMyPageUp",userDTO);
				  }
			  }
			  
			  /*삭제 오류 방지*/
			  if(userDel==null){
				  userDel = "0";
			  }
			  
			  myPage.add(userDel);
			  
			  /*삭제 시 실행*/
			  if(userDel.equals("1")){
				  realPath = "/CtrlPd.do?page=pageMain";
				  su = session1.delete("userMyPageDel",userDTO);
				  session.setAttribute("user_id", null);
			  }

			  if(su!=0 && (user_id!="")) {
				  session.removeAttribute("user_id");
				  session.setAttribute("user_id", user_id);
				  userDTO = userDAO.myPageChk(user_id);
			  }
			  
			session.setAttribute("myPage",myPage);
		}
		
		// 회원가입
		else if (page.equals("pageMember")) {
			realPath += "shop_member.jsp";
		}

		// 아이디찾기
		else if (page.equals("pageIdfind")) {
			realPath += "shop_idfind.jsp";
		}

		// 비밀번호 찾기
		else if (page.equals("pagePwdfind")) {
			realPath += "shop_passwordfind.jsp";
		}
		// 인증번호 받는 페이지
		else if (page.equals("pageSms")) {
			realPath += "shop_sms.jsp";
			session.setAttribute("phone", phone);
		}
		// 회원가입 휴대폰 인증페이지
		else if (page.equals("pagePhoneSms")) {
			realPath += "shop_phoneSms.jsp";
		}

		// Pro ====================================
		//로그인 아이디 , 비밀번호 확인  / 마이바티스 적용
		if (user_id != null && user_pwd != null && page.equals("proLog")) {
			//마이바티스 사용
			userArr = (ArrayList)session1.selectList("userLogAll", userDTO);
			if(userArr.size()!=0) {
				msg = userDTO.getUser_id();
				session.setAttribute("user_id", user_id);
				realPath = "/CtrlPd.do?page=pageMain";
			}else {
				msg = "로그인 실패\n아이디 또는 비밀번호 오류입니다";
				realPath += "shop_login.jsp";
			}
			request.setAttribute("msg", msg);
		}

		// 비밀번호찾기 Pro
		if (shop_id != null && shop_phone != null && page.equals("proPwdfind")) {
			//ShopUserDTO dto = userDAO.shopFind(shop_phone);
			userDTO = new ShopUserDTO();
			userDTO.setUser_id(user_id);
			userDTO.setUser_phone(shop_phone);
			ArrayList<ShopUserDTO> dto = (ArrayList)session1.selectList("userFind", userDTO);
			if (dto != null) {
				msg = ("비밀번호는" + dto.get(0).getUser_pwd() + "입니다.");
			} else {
				msg = ("\n\t^존재하지 않는 회원 입니다♬");
			}
			realPath += "shop_login.jsp";
			request.setAttribute("msg", msg);
		}

		// 아이디찾기 Pro
		if (shop_phone != null && page.equals("proIdfind")) {
			userDTO = new ShopUserDTO();
			userDTO.setUser_id(user_id);
			userDTO.setUser_phone(shop_phone);
			ArrayList<ShopUserDTO> dto = (ArrayList)session1.selectList("userFind", userDTO);
			if (dto != null) {
				msg = ("아이디는" + dto.get(0).getUser_id() + "입니다.");
			} else {
				msg = ("\n\t^존재하지 않는 회원 입니다♬");
			}
			realPath += "shop_login.jsp";
			request.setAttribute("msg", msg);
		}

		// 회원가입 Pro
		if (page.equals("proMember")) {
			if (user_name == null) {
				// su = userDAO.shopLogin(id, pwd);
				if (su != 0) {
					log = "1";
				} else {
					log = "0";
				}
				realPath = "./shop_header.jsp?log=" + URLEncoder.encode(log, "UTF-8");
			} else {
				//su = userDAO.shop_register(userDTOmem);
				su = session1.insert("userRegister", userDTOmem);
				if (su != 0) {
					if(su==-1) {
						msg = "이미 존재하는 아이디입니다.";
					}else {
						msg = userDTOmem.getUser_name() + "님 회원가입에 성공하셨습니다.";
					}
					realPath += "shop_login.jsp?msg=" + URLEncoder.encode(msg, "UTF-8");
				} else {
					msg = "회원가입에 실패하셨습니다.";
					realPath += "shop_login.jsp?msg=" + URLEncoder.encode(msg, "UTF-8");
				}
			}
			realPath += "shop_login.jsp";
			request.setAttribute("msg", msg);
		}
		session1.commit();
		RequestDispatcher rd = request.getRequestDispatcher(realPath);
		rd.forward(request, response);
	}
}