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

package com.enseirb.telecom.dngroup.dvd2c.filter;

import java.io.IOException;
import java.security.Principal;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.glassfish.grizzly.http.server.GrizzlyPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountService;
import com.google.common.io.BaseEncoding;

/**
 * @author Michal Gajdos (michal.gajdos at oracle.com) The first file has been
 *         taken on https://github.com/jersey/jersey Thus the first author is
 *         Michal Gajdos Then it has been modified for our needs
 */
@Provider
@PreMatching
public class SecurityRequestFilter implements ContainerRequestFilter {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SecurityRequestFilter.class);

	@Inject
	AccountService uManager;

	@Override
	public void filter(final ContainerRequestContext requestContext)
			throws IOException {

		requestContext.setSecurityContext(new SecurityContext() {

			@Override
			public Principal getUserPrincipal() {
				String auth = requestContext.getHeaders().getFirst(
						"authorization");

				if (auth != null && auth.startsWith("Basic ")) {
					String authData[] = (new String(BaseEncoding.base64()
							.decode(auth.substring(6)))).split(":");
					final String userName = authData[0];
					final String password = authData[1];
					try {
						if (uManager.getUserVerification(userName, password)) {
							return new GrizzlyPrincipal(userName);
						}
					} catch (NoSuchUserException e) {
						throw new WebApplicationException(Status.NOT_FOUND);
					}
					return null;
				}
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

				String[] test = requestContext.getUriInfo().getPath()
						.split("/");
				LOGGER.debug("Get Path from Request: {}", requestContext
						.getUriInfo().getPath());
				System.out.println(requestContext.getSecurityContext()
						.getAuthenticationScheme());
				if (role.equals("account")
						&& !requestContext.getCookies().isEmpty()) {
					System.out.println(requestContext.getCookies().isEmpty());
					// get the cookie
					String userConnected = requestContext.getCookies()
							.get("authentication").getValue();
					LOGGER.debug("{}", test[2]);
					// User is authenticated and access to his own page
					try {
						if (uManager.getUserOnLocal(userConnected) != null
								&& userConnected.equals(test[2])) {
							auth = "account";
						}
					} catch (NoSuchUserException e) {
						throw new WebApplicationException(Status.NOT_FOUND);
					}
				} else if (role.equals("other")) {
					// get the cookie

					try {
						String userConnected = requestContext.getCookies()
								.get("authentication").getValue();
						LOGGER.debug("{}", test[1]);
						// User is authenticated and access to his own page of
						// contents
						if (uManager.getUserOnLocal(userConnected) != null
								&& userConnected.equals(test[1])) {
							auth = "other";
						}
					} catch (Exception e) {
						LOGGER.debug("no cookies ?");
					}
				} else if (role.equals("authenticated")) {
					Principal user = getUserPrincipal();

					return ((user != null) && (!user.getName().equals("Jersey")));

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