//package com.codemart.codemart_back_java.component;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.codemart.codemart_back_java.exceptions.ServiceException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class RoleFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private FilterURI filterURI;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        filterURI.addFilter("/login");
//
//        String header_page = request.getHeader("page");
//        if(filterURI.isin(request.getRequestURI()) && header_page == null){
//            try{
//                filterChain.doFilter(request, response);
//            }catch (IOException | ServletException e){
//                logger.error(e.getMessage());
//                toError(request,response,"500","通信失败");
//            }
//            return ;
//        }
//        JSONObject page_json = (JSONObject) JSON.parse(header_page);
//        JSONArray page = page_json.getJSONArray("page");
//
//        page.forEach(p->{
//            JSONObject p_json = JSONObject.parseObject(p.toString());
//            String per_page = p_json.getString("per_page");
//            filterURI.addFilter(per_page);
//        });
//
//        if(filterURI.isin(request.getRequestURI())){
//            try{
//                filterChain.doFilter(request, response);
//            }catch (IOException | ServletException e){
//                logger.error(e.getMessage());
//                toError(request,response,"500","通信失败");
//            }
//            return ;
//        }else{
//            toError(request,response,"405","您无权访问");
//        }
//
//    }
//
//    public void toError(HttpServletRequest request,
//                        HttpServletResponse response,
//                        String code,
//                        String message) throws ServletException, IOException {
//        request.setAttribute("filter.error",new ServiceException(code,message));
//        request.getRequestDispatcher("/error/exthrow").forward(request, response);
//    }
//}
