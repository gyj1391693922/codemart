package com.codemart.codemart_back_java.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.codemart.codemart_back_java.exceptions.ServiceException;
import com.codemart.codemart_back_java.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private FilterURI filterURI;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        filterURI.flushAll();
        filterURI.addFilter("/login",
                                "/register",
                                "/index",
                                "/font/userCnt",
                                "/font/projectCnt",
                                "/font/moneyCnt",
                                "/",
                                "/project/list",
                                "/userInfo");
        if(filterURI.isin(request.getRequestURI())){
            try{
                filterChain.doFilter(request, response);
                return ;
            }catch (IOException | ServletException e){
                e.printStackTrace();
                toError(request,response,"500","通信失败");
            }
            return ;
        }
        //获取token
        String token = request.getHeader("token");
        if(token == null){
            toError(request,response,"403","请先登录");
        }

        if (!StringUtils.hasText(token)) {
            //放行
            try {
                filterChain.doFilter(request, response);
                return;
            }catch (IOException | ServletException e){
                toError(request,response,"500","通信失败");
            }
        }
        //解析token
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            toError(request,response,"403","登录信息非法,请重新登录");
            return;
        }
        //从redis中获取用户信息
        String redisKey = "user_id:" + userid;
        String co = redisCache.getCacheObject(redisKey);
        //角色鉴权
        if(Objects.isNull(co)){
            toError(request,response,"777","请先登录");
        }else{
            JSONObject cacheObject = JSON.parseObject(co);
            JSONArray page = cacheObject.getJSONArray("page");
            page.forEach(p->{
                JSONObject p_json = JSONObject.parseObject(p.toString());
                String per_page = p_json.getString("per_page");
                filterURI.addFilter(per_page);
            });
            if(filterURI.isin(request.getRequestURI())){
                try{
                    filterChain.doFilter(request, response);
                }catch (IOException | ServletException e){
                    toError(request,response,"500","通信失败");
                    throw e;
                }
                    return ;
                }else{
                    toError(request,response,"407","您无权访问");
                }
        }
        //存入SecurityContextHolder
//        获取权限信息封装到Authentication中
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(loginUser,null,null);
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        try{
            filterChain.doFilter(request, response);
        }catch (IOException | ServletException e){
            logger.error(e.getMessage());
            toError(request,response,"500","通信失败");
        }
    }

    public void toError(HttpServletRequest request,
                        HttpServletResponse response,
                        String code,
                        String message) throws ServletException, IOException {
        request.setAttribute("filter.error",new ServiceException(code,message));
        request.getRequestDispatcher("/error/exthrow").forward(request, response);
    }
}
