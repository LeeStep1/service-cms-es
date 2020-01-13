package com.bit.filter;

import com.bit.base.vo.BaseVo;
import com.bit.common.BaseConst;
import com.bit.common.Const;
import com.bit.common.ResultCode;
import com.bit.utils.CacheUtil;
import com.bit.utils.StringUtil;
import com.bit.utils.thread.RequestThreadBinder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import net.sf.json.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;

@WebFilter(urlPatterns = {"/*"}, filterName = "tokenAuthorFilter")
public class TokenFilter implements Filter {

    private static Log logger = LogFactory.getLog(TokenFilter.class);


    @Value("${account.token}")
    private String accountToken;
    @Value("${noFilterUrl}")
    private String[] noFilterUrl;
    @Value("${expireTime}")
    private long expireTime;

    @Autowired
    private CacheUtil cacheUtil;


    private final String tokenName = "at";

    private final String terminalName = "tid";

    private final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("----------------过滤器初始化------------------------");
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, X-Custom-Header,at,tid");


        //如果是OPTIONS请求就return 往后执行会到业务代码中 他不带参数会产生异常
        if (request.getMethod().equals("OPTIONS")) {
            return;
        }
        if (isInclude(((HttpServletRequest) servletRequest).getRequestURI())) {//白名单不拦截
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        } else {
            String token = "";
            String terminalId = getTerminalId(request);

            if (StringUtil.isEmpty(terminalId)) {//判断是否为空
                responseOutWithJson(response, new BaseVo(HttpStatus.UNAUTHORIZED.value(), "接入端为空", null));
                return;
            }

            if (StringUtil.isNotEmpty(request.getHeader(tokenName))) {//支持从header头中取值
                token = request.getHeader(tokenName);
            }

            if (StringUtil.isEmpty(token)) {//判断是否为空
                responseOutWithJson(response, new BaseVo(HttpStatus.UNAUTHORIZED.value(), "无token", null));
                return;
            } else {
                String user = (String) cacheUtil.get(accountToken + ":" + terminalId + ":" + token);
                if (user != null) {
                    long l = cacheUtil.getExpire(token);
                    if (l < expireTime) {
                        // 更新过期时间
                        cacheUtil.expire(token, Const.TOKEN_EXPIRE_SECONDS);
                    }

                    RequestThreadBinder.bindUser(user);
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                } else {
                    responseOutWithJson(response, new BaseVo(HttpStatus.UNAUTHORIZED.value(), "无效token", null));
                    return;
                }
            }
        }
    }

    private String getTerminalId(HttpServletRequest request) {
        String terminalId = request.getHeader(terminalName);
        if (terminalId == null) {
            terminalId = request.getParameter(terminalName);
        }
        return terminalId;
    }


    @Override
    public void destroy() {
        logger.info("--------------过滤器销毁--------------");
    }

    /**
     * 是否需要过滤
     *
     * @param url
     * @return
     */
    private boolean isInclude(String url) {
        for (String pattern : noFilterUrl) {
            if (url.indexOf(pattern) != -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 以JSON格式输出
     *
     * @param response
     */
    protected void responseOutWithJson(HttpServletResponse response,
                                       Object responseObject) {
        //将实体对象转换为JSON Object转换
        JSONObject responseJSONObject = JSONObject.fromObject(responseObject);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(responseJSONObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
