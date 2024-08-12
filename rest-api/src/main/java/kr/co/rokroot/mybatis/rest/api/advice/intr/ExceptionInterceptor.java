/*
 * Author: rok_root
 * Created time: 2021. 07. 25
 * Copyrights rok_root. All rights reserved.
 */

package kr.co.rokroot.mybatis.rest.api.advice.intr;

import kr.co.rokroot.mybatis.core.abstracts.AbstractRestResponse;
import kr.co.rokroot.mybatis.core.exceptions.DemoException;
import kr.co.rokroot.mybatis.core.types.ResultType;
import kr.co.rokroot.mybatis.core.wrappers.res.RestSingleResponse;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.Serializable;
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
    private RestSingleResponse<Serializable> interceptBindException(HttpServletRequest req, HttpServletResponse res, BindException e) {
        return this.setMessage(RestSingleResponse.create(), this.loggingValidateErrors(e.getFieldErrors()));
    }

    @ExceptionHandler(RejectedExecutionException.class)
    @ResponseBody
    private RestSingleResponse<Serializable> interceptRejectedExecutionException(HttpServletRequest req, HttpServletResponse res, RejectedExecutionException e) {
        return this.responseException(req, e);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    private RestSingleResponse<Serializable> interceptHttpRequestMethodNotSupportedException(HttpServletRequest req, HttpServletResponse res, HttpRequestMethodNotSupportedException e) {
        return this.responseException(req, e);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    private RestSingleResponse<Serializable> interceptNoHandlerFoundException(HttpServletRequest req, HttpServletResponse res, NoHandlerFoundException e) {
        // 404로 보내야겠지
        return this.responseException(req, e);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseBody
    private RestSingleResponse<Serializable> interceptHttpClientErrorException(HttpServletRequest req, HttpServletResponse res, HttpClientErrorException e) {
        return this.responseException(req, e);
    }

    @ExceptionHandler(TransactionException.class)
    @ResponseBody
    private RestSingleResponse<Serializable> interceptTransactionException(HttpServletRequest req, HttpServletResponse res, TransactionException e) {
        return this.responseException(req, e);
    }

    @ExceptionHandler(MyBatisSystemException.class)
    @ResponseBody
    private RestSingleResponse<Serializable> interceptPersistenceException(HttpServletRequest req, HttpServletResponse res, MyBatisSystemException e) {
        return this.responseException(req, e);
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseBody
    private RestSingleResponse<Serializable> interceptDataAccessException(HttpServletRequest req, HttpServletResponse res, DataAccessException e) {
        return this.responseException(req, e);
    }

    @ExceptionHandler(SQLException.class)
    @ResponseBody
    private RestSingleResponse<Serializable> interceptSQLException(HttpServletRequest req, HttpServletResponse res, SQLException e) {
        return this.responseException(req, e);
    }

    @ExceptionHandler(DemoException.class)
    @ResponseBody
    private RestSingleResponse<Serializable> interceptDemoException(HttpServletRequest req, HttpServletResponse res, Exception e) {
        return this.responseException(req, e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    private RestSingleResponse<Serializable> interceptException(HttpServletRequest req, HttpServletResponse res, Exception e) {
        return this.responseException(req, e);
    }

    private RestSingleResponse<Serializable> responseException(HttpServletRequest req, Exception e) {
        return this.setMessage(RestSingleResponse.create(), this.loggingException(req, e));
    }


    private <S extends AbstractRestResponse> S setMessage(S res, String msg) {
        res.setResultType(ResultType.ERROR);
        res.setResultMsg(msg);
        res.setResultCnt(0);

        return res;
    }

    private String loggingException(HttpServletRequest req, Exception e) {
        log.error("=============== EXCEPTION ===============");
        log.error("Request URI : {}", req.getRequestURI());
        log.error("Token : {}", req.getHeader("oauth-token"));
        log.error("Exception name : {}", e.getClass().getSimpleName());
        log.error("Exception message : {}", e.getMessage());
        if (log.isDebugEnabled()) e.printStackTrace();
        log.error("=============== PARAMETER ===============");

        Enumeration<?> en = req.getParameterNames();
        String param;
        String[] values;
        while (en.hasMoreElements()) {
            param = (String) en.nextElement();
            values = req.getParameterValues(param);

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

    private String loggingValidateErrors(Collection<FieldError> errors) {
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
