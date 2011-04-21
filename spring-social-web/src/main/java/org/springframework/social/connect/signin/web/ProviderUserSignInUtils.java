/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.connect.signin.web;

import org.springframework.social.connect.ServiceProviderConnection;
import org.springframework.web.context.request.WebRequest;

/**
 * Helper methods that support provider user sign-in scenarios.
 * @author Keith Donald
 */
public class ProviderUserSignInUtils {
	
	/**
	 * Get the connection to the provider user the client attempted to sign-in as.
	 * Using this connection you may fetch a {@link ServiceProviderConnection#fetchUserProfile() provider user profile} and use that to pre-populate a local user registration/signup form.
	 * You can also lookup the id of the provider and use that to display a provider-specific user-sign-in-attempt flash message e.g. "Your Facebook Account is not connected to a Local account. Please sign up."
	 * Returns null if no provider sign-in has been attempted for the current user session.
	 * @param request the current web request, used to extract sign-in attempt information from the current user session
	 */
	public static ServiceProviderConnection<?> getConnection(WebRequest request) {
		ProviderUserSignInAttempt signInAttempt = getProviderUserSignInAttempt(request);
		return signInAttempt != null ? signInAttempt.getConnection() : null;
	}

	/**
	 * Add the connection to the provider user the client attempted to sign-in with to the new local user's set of connections.
	 * Should be called after signing-up a new user in the context of a provider sign-in attempt.
	 * In this context, the user did not yet have a local account but attempted to sign-in using one of his or her existing provider accounts.
	 * Ensures provider sign-in attempt session context is cleaned up.
	 * Does nothing if no provider sign-in was attempted for the current user session (is safe to call in that case).
	 * @param request the current web request, used to extract sign-in attempt information from the current user session
	 */
	public static void handlePostSignUp(WebRequest request) {
		ProviderUserSignInAttempt signInAttempt = getProviderUserSignInAttempt(request);
		if (signInAttempt != null) {
			signInAttempt.addConnection();
			request.removeAttribute(ProviderUserSignInAttempt.SESSION_ATTRIBUTE, WebRequest.SCOPE_SESSION);
		}		
	}

	// internal helpers
	
	private ProviderUserSignInUtils() {	
	}
	
	private static ProviderUserSignInAttempt getProviderUserSignInAttempt(WebRequest request) {
		return (ProviderUserSignInAttempt) request.getAttribute(ProviderUserSignInAttempt.SESSION_ATTRIBUTE, WebRequest.SCOPE_SESSION);
	}
	
}