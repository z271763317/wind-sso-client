package org.wind.sso.client.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

/**
 * @描述 : 通用工具类
 * @版权 : 胡璐璐
 * @时间 : 2020年12月1日 00:10:28
 */
public final class _ToolUtil {

//	private static final Logger logger=Logger.getLogger(ToolUtil.class);

	/** 获取 : 请求者IP **/
	public static String getRequestIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/** 获取 : 当前请求的完整URL **/
	public static String getRequestURL(HttpServletRequest request) {
		try {
			String url = request.getScheme() + "://" + request.getServerName();
			int port=request.getServerPort();
			if(port!=80 && port!=443) {
				url+=":"+port;
			}
			url+=request.getRequestURI();
			String queryStr = request.getQueryString();
			if (queryStr != null) {
				url += "?" + queryStr;
			}
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}