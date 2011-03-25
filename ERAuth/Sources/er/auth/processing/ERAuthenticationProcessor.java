package er.auth.processing;

import com.webobjects.appserver.WOActionResults;

import er.auth.model.ERAuthenticationRequest;

public abstract class ERAuthenticationProcessor<T extends ERAuthenticationRequest> {	
	public abstract WOActionResults authenticate(T authRequest);
}