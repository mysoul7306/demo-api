/*
 * Author: rok_root
 * Created time: 2021. 07. 25
 * Copyrights rok_root. All rights reserved.
 */

package kr.co.rokroot.spring.demo.api.swagger.jpa.advice.intr;

import kr.co.rokroot.core.abstracts.AbstractRestResponse;
import kr.co.rokroot.core.exceptions.DemoException;
import kr.co.rokroot.core.type.ResultType;
import kr.co.rokroot.core.wrappers.RestSingleResponse;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.TransactionException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
@ControllerAdvice
public class ExceptionInterceptor {

    @ExceptionHandler(BindException.class)
    @ResponseBody
    protected RestSingleResponse interceptBindException(HttpServletRequest request, HttpServletResponse response, BindException e) {
        return this.setMessage(RestSingleResponse.create(), this.translateErrors(e.getFieldErrors()));
    }

    @ExceptionHandler(RejectedExecutionException.class)
    @ResponseBody
    protected RestSingleResponse interceptRejectedExecutionException(HttpServletRequest request, HttpServletResponse response, RejectedExecutionException e) {
        return this.responseException(request, e);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    protected RestSingleResponse interceptHttpRequestMethodNotSupportedException(HttpServletRequest request, HttpServletResponse response, HttpRequestMethodNotSupportedException e) {
        return this.responseException(request, e);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    protected RestSingleResponse interceptNoHandlerFoundException(HttpServletRequest request, HttpServletResponse response, NoHandlerFoundException e) {
        return this.responseException(request, e);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseBody
    protected RestSingleResponse interceptHttpClientErrorException(HttpServletRequest request, HttpServletResponse response, HttpClientErrorException e) {
        return this.responseException(request, e);
    }

    @ExceptionHandler(TransactionException.class)
    @ResponseBody
    protected RestSingleResponse interceptTransactionException(HttpServletRequest request, HttpServletResponse response, TransactionException e) {
        return this.responseException(request, e);
    }

    @ExceptionHandler(MyBatisSystemException.class)
    @ResponseBody
    protected RestSingleResponse interceptPersistenceException(HttpServletRequest request, HttpServletResponse response, MyBatisSystemException e) {
        return this.responseException(request, e);
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseBody
    protected RestSingleResponse interceptDataAccessException(HttpServletRequest request, HttpServletResponse response, DataAccessException e) {
        return this.responseException(request, e);
    }

    @ExceptionHandler(SQLException.class)
    @ResponseBody
    protected RestSingleResponse interceptSQLException(HttpServletRequest request, HttpServletResponse response, SQLException e) {
        return this.responseException(request, e);
    }

    @ExceptionHandler(DemoException.class)
    @ResponseBody
    protected RestSingleResponse interceptDemoException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        return this.responseException(request, e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    protected RestSingleResponse interceptException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        return this.responseException(request, e);
    }

    protected RestSingleResponse responseException(HttpServletRequest request, Exception e) {
        return this.setMessage(RestSingleResponse.create(), this.logException(request, e));
    }


    protected <RES extends AbstractRestResponse> RES setMessage(RES response, String msg) {
        response.setResultType(ResultType.ERROR);
        response.setResultMsg(msg);
        response.setResultCnt(0);

        return response;
    }

    protected String logException(HttpServletRequest request, Exception e) {
        log.error("=============== EXCEPTION ===============");
        log.error("Request URI : {}", request.getRequestURI());
        log.error("Token : {}", request.getHeader("oauth-token"));
        log.error("Exception name : {}", e.getClass().getSimpleName());
        log.error("Exception message : {}", e.getMessage());
        if (log.isDebugEnabled()) e.printStackTrace();
        log.error("=============== PARAMETER ===============");

        Enumeration<?> en = request.getParameterNames();
        String param;
        String[] values;
        while (en.hasMoreElements()) {
            param = (String) en.nextElement();
            values = request.getParameterValues(param);

            if (values.length == 1) {
                log.error(param + " : " + values[0]);
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < values.length; i++) {
                    sb.append(values[i]);
                    if (i != values.length - 1) {
                        sb.append(",");
                    }
                }
                log.error(param + " : " + sb);
            }
        }
        log.error("=========================================");

        return e.getMessage();
    }

    protected String translateErrors(Collection<FieldError> errors) {
        StringBuilder msg = new StringBuilder();
        Iterator<FieldError> it = errors.stream().sorted(Comparator.comparing(FieldError::getField)).iterator();
        while (it.hasNext()) {
            FieldError error = it.next();
            msg.append(error.getField());
            msg.append(": ");
            msg.append(error.getDefaultMessage());

            if (it.hasNext()) msg.append(", ");
        }

        return msg.toString();
    }
}
