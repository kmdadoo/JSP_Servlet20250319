package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/NaverSearchAPI.do")
public class NaverSerchSevlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	// service() 메서드를 오버라이딩
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException
	{
		// 1. 인증 정보 설정
		String clientId = "sT0zSK9cSIxerS3igNp8"; //애플리케이션 클라이언트 아이디
        String clientSecret = "I7LS78OQYw"; //애플리케이션 클라이언트 시크릿
        
        // 2. 검색 조건 설정
        int startNum = 0;	// 검색 시작 위치
        String text = null;	// 검색어
        
        try
		{
        	// 매개변수
        	startNum = Integer.parseInt(req.getParameter("startNum"));	// 시작위치
        	String searchText = req.getParameter("keyword"); 	// 검색어
        	text = URLEncoder.encode(searchText, "UTF-8");	// 한글깨짐 방지
		} catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException("검색어 인코딩 실패", e);
		}
        
        // 3. API URL 조합
        // 검색 결과 데이터를 JSON으로 받기 위한 API입니다. 
		// 검색어(text)를 쿼리스트링으로 보내는데, 여기에 
		// display와 start 매개변수도 추가했습니다. display는 
		// 한 번에 가져올 검색 결과의 개수이며, start는 검색 시작 위치입니다.
        String apiURL = "https://openapi.naver.com/v1/search/blog?query=" + text
        		+ "&display=10&start=" + startNum;    // JSON 결과
        //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // XML 결과
        
        // 4. API 호출
        // 클라이언트 아이디와 시크릿을 요청 헤더로 전달
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String responseBody = get(apiURL,requestHeaders);	// API 호출
        
        // 5. 결과 출력
        System.out.println(responseBody);	// 검색 결과를 콘솔에 출력
        
        resp.setContentType("text/html; charset=utf-8");
        resp.getWriter().write(responseBody);	 // 서블릿에서 즉시 출력
	}

	private String get(String apiURL, Map<String, String> requestHeaders)
	{
		HttpURLConnection con = connect(apiURL);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
	}

	private String readBody(InputStream body)
	{
		InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }


            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
	}

	private HttpURLConnection connect(String apiURL)
	{
		try {
            URL url = new URL(apiURL);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiURL, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiURL, e);
        }
	}
}
