package org.jim.server.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jim.server.base.bo.ResultContent;
import org.jim.server.common.enums.ResultMapInfo;
import org.jim.server.domain.ImUser;

import com.google.gson.Gson;

@WebFilter(filterName = "sessionFilter",urlPatterns = {"/im/*"})
public class SessionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    	HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Content-Type", "application/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");  
        response.setHeader("Access-Control-Max-Age", "3600");  
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,XFILENAME,XFILECATEGORY,XFILESIZE");  
        System.out.println("***********CorsFilter被使用, 请求地址:" + request.getRequestURI().toString() + "::: ip:" + getIpAddr(request) + "***********");
        ImUser imUser = (ImUser) request.getSession().getAttribute("imUser");
        if (imUser == null) {
        	Gson gson = new Gson();
        	System.out.println(gson.toJson(new ResultContent(ResultMapInfo.RELOGIN)));
        	response.getWriter().write(gson.toJson(new ResultContent(ResultMapInfo.RELOGIN)));
        	return;
        }
        filterChain.doFilter(request, response);
    }
    @Override
    public void destroy() {

    }
    
    /**
     * 获取登陆IP
     * @param request
     * @param response
     * @return 
     */
    public String getIpAddr(HttpServletRequest request) {
    	//处理代理访问获取不到真正的ip问题的        
    	String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        	//获取代理中中的ip
            ip = request.getHeader("PRoxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        	//获取代理中中的ip

            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        	//非代理的情况获取ip
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
        	ip = "127.0.0.1";
        }
        return ip;
    }
}
