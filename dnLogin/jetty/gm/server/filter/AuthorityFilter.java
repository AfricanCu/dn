package gm.server.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.NTxtAbs;

/**
 * <pre>
 * 1.权限过滤器 
 * 2.日志记录
 * 3.防止sql注入
 * </pre>
 * 
 * @author lixing 2014年9月22日
 */
public class AuthorityFilter implements Filter {
	private Pattern p = null;

	/**
	 * 将请求数据重新组装
	 * 
	 * @param req
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String ecodeParameter(ServletRequest req)
			throws UnsupportedEncodingException {
		StringBuilder builder = new StringBuilder();
		Enumeration<?> attributeNames = req.getParameterNames();
		while (attributeNames.hasMoreElements()) {
			Object element = attributeNames.nextElement();
			String[] parameterValues = req.getParameterValues((String) element);
			for (String parameterValue : parameterValues) {
				builder.append(element).append("=").append(parameterValue)
						.append("&");
			}
		}
		return builder.toString();
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		String regex = filterConfig.getInitParameter("regex");
		p = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		response.setContentType("text/html; charset=utf8");
		HttpServletRequest req = (HttpServletRequest) request;
		if (!req.getRequestURI().startsWith("/Login/innerServlet")) {
			String ecodeParameter = ecodeParameter(request);
			Matcher matcher = p.matcher(ecodeParameter);
			if (matcher.matches()) {
				LoggerService.getLogicLog().warn("sql注入攻击,{}", ecodeParameter);
				LoggerService.getLogicLog().warn(matcher.group());
				response.getOutputStream().write(
						new JSONObject()
								.put("code", NTxtAbs.SQL_CONTAINS_HIT)
								.toString().getBytes("UTF-8"));
				return;
			}
		}
		chain.doFilter(request, response);
	}
}