package com.example.routingandfilteringgateway.filters.pre;

import javax.servlet.http.HttpServletRequest;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.ZuulFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleFilter extends ZuulFilter {
	
	private static Logger log = LoggerFactory.getLogger(SimpleFilter.class);
	
	@Override
	public String filterType() {
		return "pre";			//pre, route. post, error
	}
	
	@Override
	public int filterOrder() {
		return 1;
	}
	

	public boolean shouldFilter() {
	   return true;
	}
	

	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		
		log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
		
		return null;
	}
	
}






