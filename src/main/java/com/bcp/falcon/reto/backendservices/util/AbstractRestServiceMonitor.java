package com.bcp.falcon.reto.backendservices.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * @author Kei Takayama
 * Created on 2/17/20.
 */
public class AbstractRestServiceMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRestServiceMonitor.class);
    private static final Gson gson = new Gson();

    public AbstractRestServiceMonitor() {
        LOGGER.info("Starting LOGGER aspect + " + getClass());
    }

    public Object restLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Logger classLog = LoggerFactory.getLogger(proceedingJoinPoint.getTarget().getClass());

        try {
            StringBuilder requestBuilder = new StringBuilder("");
            requestBuilder.append("MONITOR: Request Parameters: ");

            logParameters(classLog, requestBuilder, proceedingJoinPoint.getArgs());

        } catch (Exception e) {
            LOGGER.info("Unable to write to logs, check " + getClass(), e);
        }

        try {
            Object retVal = proceedingJoinPoint.proceed();
            Object[] args = new Object[]{retVal};

            StringBuilder responseBuilder = new StringBuilder("");

            responseBuilder.append("MONITOR: Response Parameters ");

            logParameters(classLog, responseBuilder, args);

            return retVal;
        } catch (
                Exception ex) {
            classLog.error("Error found: ", ex);
            throw ex;
        }


    }

    private void logParameters(Logger classLog, StringBuilder builder, Object[] args) {

        String parameter = null;

        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];

                if (arg != null && arg instanceof OAuth2Authentication) {
                    continue;
                }

                if (!(arg instanceof String)) {

                    String jsonString = gson.toJson(arg);
                    JsonElement jsonElement = null;

                    try {
                        jsonElement = new JsonParser().parse(jsonString);
                    } catch (JsonParseException e) {
                        LOGGER.error("Invalid string to convert=" + e.getMessage());
                    }

                    if (jsonElement != null) {
                        parameter = gson.toJson(jsonElement);
                    }

                } else {
                    parameter = gson.toJson(arg);
                }

                if (i > 0 && i <= args.length - 1) {
                    builder.append(",");
                }
                builder.append(parameter);
            }
            classLog.info(builder.toString());
        }
    }
}
