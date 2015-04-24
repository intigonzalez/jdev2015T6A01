package com.enseirb.telecom.dngroup.dvd2c;

import java.io.File;
import java.util.logging.Level;

import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.grizzly.http.util.Header;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * this class  enable access from a other box 
 * and Options is enable
 * you can use a server file like apache for remove this file
 * @author dbourasseau
 *
 */
public final class StaticHttpHandlerCORS extends StaticHttpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(StaticHttpHandlerCORS.class);
	public StaticHttpHandlerCORS(String[] docRoots) {
		super(docRoots);
	}

	protected boolean handle(final String uri,
	        final Request request,
	        final Response response) throws Exception {
		//DEV: change * by a better uri
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type, origin, authorization, accept, client-security-token, range");
		response.setHeader("Access-Control-Max-Age","1000");
		response.setHeader("Allow","OPTIONS,GET,HEAD,POST");


        boolean found = false;

        final File[] fileFolders = docRoots.getArray();
        if (fileFolders == null) {
            return false;
        }

        File resource = null;

        for (int i = 0; i < fileFolders.length; i++) {
            final File webDir = fileFolders[i];
            // local file
            resource = new File(webDir, uri);
            final boolean exists = resource.exists();
            final boolean isDirectory = resource.isDirectory();

            if (exists && isDirectory) {
                final File f = new File(resource, "/index.html");
                if (f.exists()) {
                    resource = f;
                    found = true;
                    break;
                }
            }

            if (isDirectory || !exists) {
                found = false;
            } else {
                found = true;
                break;
            }
        }

        if (!found) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug( "File not found {0}", resource);
            }
            return false;
        }

        assert resource != null;
        
//        // If it's not HTTP GET - return method is not supported status
//        if (!Method.GET.equals(request.getMethod())) {
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug( "File found {0}, but HTTP method {1} is not allowed",
//                        new Object[] {resource, request.getMethod()});
//            }
//            response.setStatus(HttpStatus.METHOD_NOT_ALLOWED_405);
//            response.setHeader(Header.Allow, "GET");
//            return true;
//        }
        
        pickupContentType(response, resource.getPath());
        
        addToFileCache(request, response, resource);
        sendFile(response, resource);

        return true;
    

	}
}