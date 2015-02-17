/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.enseirb.telecom.dngroup.dvd2c;

import java.io.IOException;
import java.security.Principal;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountService;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountServiceImpl;

/**
 * @author Michal Gajdos (michal.gajdos at oracle.com)
 * The first file has been taken on https://github.com/jersey/jersey
 * Thus the first author is Michal Gajdos
 * Then it has been modified for our needs
 */
@Provider
@PreMatching
public class SecurityRequestFilter implements ContainerRequestFilter {
	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityRequestFilter.class);
    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return new Principal() {
                    @Override
                    public String getName() {
                        return "Jersey";
                    }
                };
            }
 
            @Override
            public boolean isUserInRole(final String role) {
            	String auth = "denied";
        		AccountService uManager = new AccountServiceImpl(new UserRepositoryMongo("mediahome"));
        		String userConnected = requestContext.getCookies().get("authentication").getValue(); // get the cookie
        		//System.out.println(requestContext.getCookies().get("test").getValue()); 
        		String[] test = requestContext.getUriInfo().getPath().split("/");
        		LOGGER.debug("Get Path from Request: {}", requestContext.getUriInfo().getPath());  
            	
            	if(role.equals("account")){
	        		LOGGER.debug("{}",test[test.length-1]);   
	        		// User is authenticated and access to his own page
	        		if(uManager.getUserOnLocal(userConnected) != null && userConnected.equals(test[test.length-1])){
	        			auth = "account";
					}      	
            	}
            	else if (role.equals("other")) {
            		LOGGER.debug("{}", test[1]); 
	        		// User is authenticated and access to his own page of contents
	        		if(uManager.getUserOnLocal(userConnected) != null && userConnected.equals(test[1])){
	        			auth = "other";
					}	
				}
        		
        		return auth.equals(role);
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public String getAuthenticationScheme() {
                return null;
            }
        });
    }
}
