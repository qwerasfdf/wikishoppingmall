package ajax;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import shop.ShopProductDAO;
import shop.ShopProductDTO;

/**
 * Servlet implementation class AjaxSearch
 */
@WebServlet("/AjaxSearch.do")
public class AjaxSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String query_search;
	private String result = ""; // 검색 리스트 출력

    public AjaxSearch() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		execute(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		execute(request, response);
	}

	private void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		PrintWriter out = response.getWriter();
		
		// MyBatis
		String resource = "mybatis/mybatis-config.xml";
		Reader reader = Resources.getResourceAsReader(resource);
		SqlSessionFactory sqlFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession sqlSession = sqlFactory.openSession();
		
		query_search = request.getParameter("search");
		ShopProductDAO productDAO = new ShopProductDAO();
		
		if(query_search != null) {
			try {
				//ArrayList<String> items = productDAO.autoCompleteName(query_search);
				ShopProductDTO searchDTO = new ShopProductDTO();
				searchDTO.setProduct_name(query_search);
				List<ShopProductDTO> items = sqlSession.selectList("autoCompleteName", searchDTO);
				result += items.get(0).getProduct_name();
				for(int idx = 1; idx < items.size(); idx++) {
					result += "," + items.get(idx).getProduct_name();
				}
				out.print(result); // <- header 영역의 hidden에 할당 후 onchange 이벤트로 변환될때마다 리스트 갱신
			} catch(IndexOutOfBoundsException e) {
				out.print("");
			} finally {
				result = "";
				sqlSession.close();
			}
		}
	}
}