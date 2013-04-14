/*
 * Copyright (c) 1998-2012 Caucho Technology -- all rights reserved
 *
 * This file is part of Resin(R) Open Source
 *
 * Each copy or derived work must preserve the copyright notice and this
 * notice unmodified.
 *
 * Resin Open Source is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Resin Open Source is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, or any warranty
 * of NON-INFRINGEMENT.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Resin Open Source; if not, write to the
 *
 *   Free Software Foundation, Inc.
 *   59 Temple Place, Suite 330
 *   Boston, MA 02111-1307  USA
 *
 * @author Scott Ferguson
 */

package org.ireland.jnetty.dispatch;

import org.ireland.jnetty.webapp.WebApp;

import com.caucho.server.http.HttpServletRequestImpl;

/**
 * A repository for request information gleaned from the uri. KEN
 */
public class Invocation extends ServletInvocation
{
	private String _rawHost;

	// canonical host and port
	private String _hostName;
	private int _port;

	private String _rawURI;
	
	private boolean _isSecure;

	private String _uri;
	private String _sessionId;

	private WebApp _webApp;

	public Invocation()
	{
	}

	/**
	 * Returns the secure flag
	 */
	public final boolean isSecure()
	{
		return _isSecure;
	}

	/**
	 * Sets the secure flag
	 */
	public final void setSecure(boolean isSecure)
	{
		_isSecure = isSecure;
	}

	/**
	 * Returns the raw host from the protocol. This may be different from the canonical host name.
	 */
	public final String getHost()
	{
		return _rawHost;
	}

	/**
	 * Sets the protocol's host.
	 */
	public final void setHost(String host)
	{
		_rawHost = host;
	}

	/**
	 * Returns canonical host name.
	 */
	public final String getHostName()
	{
		return _hostName;
	}

	/**
	 * Sets the protocol's host.
	 */
	public final void setHostName(String hostName)
	{
		if (hostName != null && !hostName.equals(""))
			_hostName = hostName;
	}

	/**
	 * Returns canonical port
	 */
	public final int getPort()
	{
		return _port;
	}

	/**
	 * Sets the canonical port
	 */
	public final void setPort(int port)
	{
		_port = port;
	}

	/**
	 * Returns the raw URI from the protocol before any normalization. The raw URI includes the query string. (?)
	 */
	public final String getRawURI()
	{
		return _rawURI;
	}

	/**
	 * Sets the raw URI from the protocol before any normalization. The raw URI includes the query string. (?)
	 */
	public final void setRawURI(String uri)
	{
		_rawURI = uri;
	}

	/**
	 * Returns the raw URI length.
	 */
	public int getURLLength()
	{
		if (_rawURI != null)
			return _rawURI.length();
		else
			return 0;
	}

	/**
	 * Returns the URI after normalization, e.g. character escaping, URL session, and query string.
	 */
	public final String getURI()
	{
		return _uri;
	}

	/**
	 * Sets the URI after normalization.
	 */
	public final void setURI(String uri)
	{
		_uri = uri;

		setContextURI(uri);
	}

	/**
	 * Returns a URL-based session id.
	 */
	public final String getSessionId()
	{
		return _sessionId;
	}

	/**
	 * Sets the URL-based session id.
	 */
	public final void setSessionId(String sessionId)
	{
		_sessionId = sessionId;
	}

	/**
	 * Returns the mapped webApp.
	 */
	public final WebApp getWebApp()
	{
		return _webApp;
	}

	/**
	 * Sets the mapped webApp.
	 */
	public void setWebApp(WebApp app)
	{
		_webApp = app;
	}

	/**
	 * Returns the versioned invocation based on this request.
	 * 
	 * @param request
	 *            the servlet request
	 */
	public Invocation getRequestInvocation(HttpServletRequestImpl request)
	{
		return this;
	}

	/**
	 * Copies from the invocation.
	 */
	public void copyFrom(Invocation invocation)
	{
		super.copyFrom(invocation);

		_rawHost = invocation._rawHost;
		_rawURI = invocation._rawURI;

		_hostName = invocation._hostName;
		_port = invocation._port;
		_uri = invocation._uri;

		// server/1h25
		_sessionId = invocation._sessionId;

		_webApp = invocation._webApp;
	}

	/**
	 * Returns the invocation's hash code.
	 */
	public int hashCode()
	{
		int hash = _rawURI.hashCode();

		if (_rawHost != null)
			hash = hash * 65521 + _rawHost.hashCode();

		hash = hash * 65521 + _port;

		return hash;
	}

	/**
	 * Checks for equality
	 */
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		else if (o == null)
			return false;

		if (getClass() != o.getClass())
			return false;

		Invocation inv = (Invocation) o;

		if (_isSecure != inv._isSecure)
			return false;

		if (_rawURI != inv._rawURI && (_rawURI == null || !_rawURI.equals(inv._rawURI)))
			return false;

		if (_rawHost != inv._rawHost && (_rawHost == null || !_rawHost.equals(inv._rawHost)))
			return false;

		if (_port != inv._port)
			return false;

		String aQuery = getQueryString();
		String bQuery = inv.getQueryString();

		if (aQuery != bQuery && (aQuery == null || !aQuery.equals(bQuery)))
			return false;

		return true;
	}

	void close()
	{
		_webApp = null;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(getClass().getSimpleName());
		sb.append("[");
		sb.append(getContextPath());

		if (getQueryString() != null)
			sb.append("?").append(getQueryString());

		sb.append(",").append(_webApp);

		sb.append("]");

		return sb.toString();
	}
}
